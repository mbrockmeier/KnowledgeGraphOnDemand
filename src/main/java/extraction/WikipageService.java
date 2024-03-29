package extraction;

import retrofit2.Call;
import retrofit2.http.*;

public interface WikipageService {

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
}
