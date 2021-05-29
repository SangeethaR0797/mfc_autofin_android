package v2.service

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit_config.RetroBase
import retrofit_services.RetrofitInterface
import utility.CommonStrings
import utility.Global

import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


object ApiServiceGenerator {

    val BASE_URL = Global.currentBaseURL

    private val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
    var retrofit: Retrofit = builder.build()
        private set
    private val logging = HttpLoggingInterceptor()
    private var tokenInterceptor = TokenInterceptor()

    private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .hostnameVerifier(HostVerifier())

    fun <S> createService(
            serviceClass: Class<S>?
    ): S {
        logging.level = HttpLoggingInterceptor.Level.BODY
        // httpClient.interceptors().clear()
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging)
            builder.client(httpClient.build())
            retrofit = builder.build()
        }
        return retrofit.create(serviceClass)
    }

    fun resetAuthToken() {
        httpClient.interceptors().clear()
    }
    var v2RetrofitInterface: RetrofitInterface? = retrofit.create(RetrofitInterface::class.java)

    class TokenInterceptor() : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var initialRequest: Request = chain.request()
            if (!TextUtils.isEmpty(CommonStrings.TOKEN_VALUE)) {
                initialRequest = initialRequest.newBuilder()
                        .addHeader("Accept", "application/json; charset=utf-8")
                        .addHeader("token", CommonStrings.TOKEN_VALUE)
                        .addHeader("Authorization", " Bearer " + CommonStrings.TOKEN_VALUE)
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

    @JvmStatic
    fun getRetrofitInstance(): Retrofit? {
        return retrofit
    }

}