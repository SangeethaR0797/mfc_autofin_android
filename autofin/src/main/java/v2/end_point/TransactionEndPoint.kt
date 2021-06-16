package v2.end_point

import io.reactivex.Observable
import model.addtional_fields.AdditionalFieldResponse
import retrofit2.http.*
import v2.model.dto.AddLeadRequest
import v2.model.request.*
import v2.model.request.update.UpdateLeadBasicDetailsRequest
import v2.model.response.*
import v2.model.response.bank_offers.ValidateFinalOTPResponse

interface TransactionEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun addLead(@Body request: AddLeadRequest, @Url url: String?): Observable<AddLeadResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun updateLeadBasicDetails(@Body request: UpdateLeadBasicDetailsRequest, @Url url: String?): Observable<AddLeadResponse?>?


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
    fun resetCustomerJourney(@Body request: CustomerRequest, @Url url: String?): Observable<ResetCustomerJourneyResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getCustomerDetails(@Body request: CustomerRequest, @Url url: String?): Observable<CustomerDetailsResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun addEmploymentDetails(@Body request: AddEmploymentDetailsRequest, @Url url: String?): Observable<AddLeadResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun addResidentDetails(@Body request: AddResidentDetailsRequest, @Url url: String?): Observable<AddLeadResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun updateAddress(@Body request: UpdateAddressRequest, @Url url: String?): Observable<SimpleResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getAdditionalFieldsData(@Body request: CustomerRequest, @Url url: String?): Observable<AdditionalFields?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun submitAdditionalFields(@Body request: SubmitAdditionalFieldRequest, @Url url: String?): Observable<CommonResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun generateFinalOTP(@Body request: GenerateFinalOTPRequest, @Url url: String?): Observable<OTPResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun validateFinalOTP(@Body request: ValidateFinalOTPRequest, @Url url: String?): Observable<ValidateFinalOTPResponse?>?


    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getApplicationList(@Body request: ApplicationListRequest, @Url url: String?): Observable<ApplicationListResponse?>?

}