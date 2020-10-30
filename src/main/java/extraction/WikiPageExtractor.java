package extraction;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.time.Duration;

/**
 * @author Malte Brockmeier
 * WikiPageExtractor: use for locating and downloading a specified wikipedia page via its title or ID
 */

/**
 * Class providing methods for XML source extraction from Wikipedia
 */
public class WikiPageExtractor {
    private WikipediaService wikipediaService;

    public WikiPageExtractor() {
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
     * retrieves the specified Wikipedia Page
     * @param title the title of the wikipedia page to retrieve
     * @return the XML source of the wikipedia page as a String
     */
    public String retrieveWikiPageByTitle(String title) {
        String pageSource = null;

        try {
            Call<String> call = wikipediaService.getWikiPageByTitle("submit", title, "true");
            Response<String> response = call.execute();
            pageSource = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pageSource;
    }
}
