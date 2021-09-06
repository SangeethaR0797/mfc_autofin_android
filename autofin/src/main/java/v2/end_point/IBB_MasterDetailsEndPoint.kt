package v2.end_point

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url
import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model.request.add_lead.IBBPriceRequest
import v2.model.response.Get_IBB_MasterDetailsResponse
import v2.model.response.master.IBBPriceResponse

interface IBB_MasterDetailsEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getIBB_MasterDetails(@Body request: Get_IBB_MasterDetailsRequest?, @Url url: String?): Observable<Get_IBB_MasterDetailsResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getIBBPrice(@Body request: IBBPriceRequest, @Url url: String): Observable<IBBPriceResponse>

}