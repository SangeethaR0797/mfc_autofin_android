package v2.end_point

import io.reactivex.Observable
import retrofit2.http.*
import v2.model.dto.AddLeadRequest
import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model.response.AddLeadResponse
import v2.model.response.Get_IBB_MasterDetailsResponse
import v2.model.response.master.KmsDrivenResponse

interface VehicleTransactionEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun addLead( @Body request: AddLeadRequest,@Url url: String?): Observable<AddLeadResponse?>?
}