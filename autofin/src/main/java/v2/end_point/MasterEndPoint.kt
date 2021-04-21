package v2.end_point

import io.reactivex.Observable
import retrofit2.http.*
import v2.model.dto.VehicleAddUpdateDTO
import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model.request.StockDetailsReq
import v2.model.response.Get_IBB_MasterDetailsResponse
import v2.model.response.master.KmsDrivenResponse

interface MasterEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getKmsDrivenDetails( @Url url: String?): Observable<KmsDrivenResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getStockDetails(@Body request: StockDetailsReq?, @Url url: String?): Observable<VehicleAddUpdateDTO?>?

}