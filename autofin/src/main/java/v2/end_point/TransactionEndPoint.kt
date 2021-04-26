package v2.end_point

import io.reactivex.Observable
import retrofit2.http.*
import v2.model.dto.AddLeadRequest
import v2.model.request.GenerateOTPRequest
import v2.model.response.AddLeadResponse
import v2.model.response.OTPResponse

interface TransactionEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun addLead( @Body request: AddLeadRequest,@Url url: String?): Observable<AddLeadResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun generateOTP( @Body request: GenerateOTPRequest,@Url url: String?): Observable<OTPResponse?>?

}