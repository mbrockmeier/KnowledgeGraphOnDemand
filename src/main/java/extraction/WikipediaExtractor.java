package extraction;

import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Malte Brockmeier
 * WikiPageExtractor: use for locating and downloading a specified wikipedia page via its title or ID
 */

/**
 * Class providing methods for XML source extraction from Wikipedia
 */
public class WikipediaExtractor {
    private WikipediaService wikipediaService;

    public WikipediaExtractor() {
        initializeService();
    }

    private void initializeService() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(30))
                .callTimeout(Duration.ofSeconds(30))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("https://en.wikipedia.org")
                .client(okHttpClient)
                .build();

        wikipediaService = retrofit.create(WikipediaService.class);
    }

    /**
     * @author Sunita Pateer
     * @param title the title of the wikipedia for which the backlinks should be retrieved
     * @return the list of backlinks for the specified wikipedia page
     */
    public List<String> getBackLinks(String title) {
        String responseString;
        List<String> backlinkTitles = new ArrayList<>();

        try {
            Response<String> response = wikipediaService.getBackLinks(title).execute();

            responseString = response.body();
            backlinkTitles = parseJSONBacklinksResponse(responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return backlinkTitles;
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
            Call<String> call = wikipediaService.getWikiPageByTitle("submit", formattedTitles, "true");
            Response<String> response = call.execute();
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
            System.out.println(e.getMessage());
        }
        return backlinkTitles;
    }
}
