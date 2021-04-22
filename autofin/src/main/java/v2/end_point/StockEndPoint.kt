package v2.end_point

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url
import v2.model.request.StockDetailsReq
import v2.model.response.StockDetails
import v2.model.response.StockResponse

public interface StockEndPoint
{
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getStockDetails(@Body request: StockDetailsReq?, @Url url: String?): Observable<StockResponse?>
}