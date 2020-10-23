package retrofit_services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitInterface
{
    /*void OnSuccess(Object obj);

    void OnFailure(Throwable mThrowable);
*/
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    Call<Object> getFromWeb(@Body Object request, @Url String url);


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    Call<Object> getFromWeb(@Url String url, @Query("event_start_date_time") String eventStartDate, @Query("event_end_date_time") String eventEndDate);

}
