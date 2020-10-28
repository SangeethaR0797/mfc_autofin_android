package retrofit_config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit_services.RetrofitInterface;

public class RetroBase {

    public static final String BASE_URL = "https://15.207.148.230:3007/api/customer/";

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    protected static TokenInterceptor tokenInterceptor = new TokenInterceptor();
    protected static OkHttpClient client = new OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .addInterceptor(tokenInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .hostnameVerifier(new HostVerifier())
            .build();


    protected static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();


    public static RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

    private static class TokenInterceptor implements Interceptor {
        private TokenInterceptor() {

        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request initialRequest = chain.request();

            String sToken = "";
            if (sToken != null) {
                initialRequest = initialRequest.newBuilder()
                        // .addHeader("Accept", "application/json; charset=utf-8")
                        //.addHeader("token", "token")
                        .build();
            }

            Response response = chain.proceed(initialRequest);
            return response;
        }
    }

    public static class HostVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {

        return true;
        }
    }


}

