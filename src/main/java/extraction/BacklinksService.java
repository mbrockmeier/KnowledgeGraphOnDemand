package extraction;

import retrofit2.Call;
import retrofit2.http.*;

public interface BacklinksService {
    /**
     * retrieves the backlinks for the specified wikipedia page
     * @author Sunita Pateer
     * @param bltitle the title of the wikipedia page for which the backlinks should be retrieved
     * @return the backlinks as a JSON object
     */
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("api.php?action=query&format=json&list=backlinks&blnamespace=0")
    Call<String> getBackLinks(@Query("bltitle") String bltitle, @Query("bllimit") int limit, @Query("blcontinue") String blcontinue);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1")
    Call<String> getExtract(@Query("titles") String titles);
}
