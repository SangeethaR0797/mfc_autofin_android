package v2.service.utility.unused

import retrofit2.Call
import retrofit2.http.*

public interface InfRetroBase
{
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getFromWeb(@Body request: Any?, @Url url: String?): Call<Any?>?


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getFromWeb(@Url url: String?): Call<Any?>?

}