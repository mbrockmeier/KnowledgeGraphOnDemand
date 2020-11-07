package extraction;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.HttpUrl;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

import org.json.*;

public class WikiBacklinksExtractor {
    private OkHttpClient client;
    // private String arrayName[];

    public WikiBacklinksExtractor() { initializeService(); }

    private void initializeService() {
        try {
            client = new OkHttpClient.Builder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .callTimeout(Duration.ofSeconds(5))
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String> getBackLinks(String title) {
        String responseString;
        List<String> backlinkTitles = new ArrayList<>();
        try {

            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("en.wikipedia.org")
                    .addPathSegment("w")
                    .addPathSegment("api.php")
                    .addQueryParameter("action", "query")
                    .addQueryParameter("format", "json")
                    .addQueryParameter("list", "backlinks")
                    .addQueryParameter("bltitle", title)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .method("GET", null)
                    .build();

            Response response = client.newCall(request).execute();

            responseString = response.body().string();
            backlinkTitles = this.parseJSONResponse(responseString);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseString = e.getMessage();
        }
        System.out.println(responseString);
        return backlinkTitles;
    }
    
    public List<String> parseJSONResponse(String response) {
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