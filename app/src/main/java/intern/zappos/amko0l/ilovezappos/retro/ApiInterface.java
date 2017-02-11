package intern.zappos.amko0l.ilovezappos.retro;

import intern.zappos.amko0l.ilovezappos.model.ItemResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by amitn on 27-01-2017.
 */

public interface ApiInterface {
    @GET("Search")
    Call<ItemResponse> getSearch(@Query("term") String _term,
                                 @Query("key") String _apiKey);
}
