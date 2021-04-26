package v2.end_point

import io.reactivex.Observable
import retrofit2.http.*
import v2.model.dto.AddLeadRequest
import v2.model.request.OTPRequest
import v2.model.request.ResetCustomerJourneyRequest
import v2.model.request.ValidateLeadDataRequest
import v2.model.request.ValidateLeadRequest
import v2.model.response.*

interface TransactionEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun addLead( @Body request: AddLeadRequest,@Url url: String?): Observable<AddLeadResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun generateOTP(@Body request: OTPRequest, @Url url: String?): Observable<OTPResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun validateOTP(@Body request: OTPRequest, @Url url: String?): Observable<OTPResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun validateLead(@Body request: ValidateLeadRequest, @Url url: String?): Observable<ValidateLeadResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun resetCustomerJourney(@Body request: ResetCustomerJourneyRequest, @Url url: String?): Observable<ResetCustomerJourneyResponse?>?

}