package v2.service.unused

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import utility.CommonStrings
import utility.Global
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

public class RetroBaseService
{
    val BASE_URL = Global.currentBaseURL

    private val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    protected var tokenInterceptor = TokenInterceptor()
    protected var client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging) // Comment for Prod
            .addInterceptor(tokenInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .hostnameVerifier(HostVerifier())
            .build()


    protected var gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()

    var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


    var retroInf = retrofit.create(InfRetroBase::class.java)

    class TokenInterceptor() : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var initialRequest: Request = chain.request()
            val sToken = ""
            if (sToken != null) {
                initialRequest = initialRequest.newBuilder()
                        .addHeader("Accept", "application/json; charset=utf-8")
                        .addHeader("token", CommonStrings.TOKEN_VALUE)
                        .build()
            }
            return chain.proceed(initialRequest)
        }
    }

    class HostVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }


}