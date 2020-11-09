package extraction;

import retrofit2.Call;
import retrofit2.http.*;

public interface WikipediaService {

    /**
     * retrieves the specified wikipedia page(s)
     * @author Malte Brockmeier
     * @param action should be set to submit to export pages
     * @param pages page to retrieve
     * @param curonly boolean; if true retrieve only current revision
     * @return the XML source of the specified wikipedia page
     */
    @FormUrlEncoded
    @POST("/wiki/Special:Export")
    Call<String> getWikiPageByTitle(@Field("action") String action, @Field("pages") String pages, @Field("curonly") String curonly);

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
    @GET("/w/api.php?action=query&format=json&list=backlinks")
    Call<String> getBackLinks(@Query("bltitle") String bltitle);
}
