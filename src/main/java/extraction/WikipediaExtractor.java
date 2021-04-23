package extraction;

import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tinylog.Logger;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * @author Malte Brockmeier
 * WikiPageExtractor: use for locating and downloading a specified wikipedia page via its title or ID
 */

/**
 * Class providing methods for XML source extraction from Wikipedia
 */
public class WikipediaExtractor {
    private WikipageService wikipageService;
    private BacklinksService backlinksService;

    public WikipediaExtractor(String wikiBaseUrl, String apiBaseUrl) {
        initializeService(wikiBaseUrl, apiBaseUrl);
    }

    private void initializeService(String baseUrl, String apiBaseUrl) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(30))
                .callTimeout(Duration.ofSeconds(30))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();

        Retrofit apiRetrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(apiBaseUrl)
                .client(okHttpClient)
                .build();

        wikipageService = retrofit.create(WikipageService.class);
        backlinksService = apiRetrofit.create(BacklinksService.class);
    }

    /**
     * @author Sunita Pateer
     * @param title the title of the wikipedia for which the backlinks should be retrieved
     * @return the list of backlinks for the specified wikipedia page
     */
    public List<String> getBackLinks(String title) {
        String responseString;
        List<String> backlinkTitles = new ArrayList<>();

        //check whether to include backlinks
        if (KnowledgeGraphConfiguration.getIncludeBacklinks()) {
            int backlinksCount = KnowledgeGraphConfiguration.getBacklinksCount();
            int backlinksRetrieved = 0;
            int backlinksToRetrieve = (backlinksCount > 500 || backlinksCount == 0) ? 500 : backlinksCount;

            try {
                String continueString = null;
                do {
                    Response<String> response = backlinksService.getBackLinks(title, backlinksToRetrieve, continueString).execute();

                    responseString = response.body();
                    backlinkTitles.addAll(parseJSONBacklinksResponse(responseString));
                    continueString = getContinueString(responseString);
                    backlinksRetrieved = backlinkTitles.size();
                    backlinksToRetrieve = ((backlinksCount - backlinksRetrieved) > 500 || backlinksCount == 0) ? 500 : (backlinksCount - backlinksRetrieved);
                    Logger.info("Retrieved " + backlinksRetrieved + " backlinks of limit " + backlinksCount);
                } while (((backlinksRetrieved < backlinksCount) || backlinksCount == 0) && continueString != null);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return backlinkTitles;
    }

    /**
     * @author Sunita Pateer
     * @param title the title of the wikipedia for which the extract should be retrieved
     * @return the extract for the specified wikipedia page
     */
    public String getExtract(String title) {
        String abstractString = "";

        try {
            Response<String> response = backlinksService.getExtract(title).execute();
            String responseString = response.body();
            JSONObject obj = new JSONObject(responseString);
            JSONObject pages = obj.getJSONObject("query").getJSONObject("pages");
            Iterator<String> keys = pages.keys();
            if( keys.hasNext() ){
                String pageid = (String) keys.next(); // First key in json object
                try {
                    abstractString = pages.getJSONObject(pageid).getString("extract");
                } catch (JSONException jsonException) {
                    Logger.info("No abstract found for '" + title + "'");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return abstractString;
    }

    /**
     * @author Malte Brockmeier
     * retrieves the specified Wikipedia Page
     * @param titles the titles of the wikipedia pages to retrieve
     * @return the XML source of the wikipedia page as a String
     */
    public String retrieveWikiPagesByTitle(List<String> titles) {
        String pageSource = null;

        String formattedTitles = "";

        for (String title : titles) {
            formattedTitles += title + "\n";
        }

        try {
            Logger.info("Start downloading wikipages...");
            Call<String> call = wikipageService.getWikiPageByTitle("submit", formattedTitles, "true");
            Response<String> response = call.execute();
            Logger.info("Finished downloading wikipages.");
            pageSource = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pageSource;
    }

    /**
     * @author Sunita Pateer
     * @param response the backlinks response from the wikipedia API
     * @return the list of backlinks to the wikipedia page
     */
    private List<String> parseJSONBacklinksResponse(String response) {
        List<String> backlinkTitles = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray backlinks = obj.getJSONObject("query").getJSONArray("backlinks");
            for (int i = 0; i < backlinks.length(); i++)
            {
                String title = backlinks.getJSONObject(i).getString("title");
                backlinkTitles.add(title);
            }
        } catch (Exception e) {
            Logger.warn(e);
        }
        return backlinkTitles;
    }

    /**
     * @author Sunita Pateer
     * @param response the backlinks response
     * @return the continue string
     */
    private String getContinueString(String response) {
        String continueString = null;
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.has("continue")) {
                continueString = obj.getJSONObject("continue").getString("blcontinue");
                Logger.info("Use continue string " + continueString + " to complete backlinks query");
            }
        } catch (Exception e) {
            Logger.warn(e);
        }
        return continueString;
    }
}
