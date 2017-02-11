package intern.zappos.amko0l.ilovezappos.retro;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amitn on 27-01-2017.
 */

public class RetroClient {
    public static final String BASE_URL = "https://api.zappos.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit==null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit;
    }
}
