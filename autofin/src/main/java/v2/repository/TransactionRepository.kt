package v2.repository

import io.reactivex.Observable
import v2.end_point.TransactionEndPoint
import v2.model.dto.AddLeadRequest
import v2.model.request.OTPRequest
import v2.model.request.ResetCustomerJourneyRequest
import v2.model.request.ValidateLeadDataRequest
import v2.model.request.ValidateLeadRequest
import v2.model.response.AddLeadResponse
import v2.model.response.OTPResponse
import v2.model.response.ResetCustomerJourneyResponse
import v2.model.response.ValidateLeadResponse
import v2.service.ApiServiceGenerator

class TransactionRepository {
    lateinit var transactionEndPoint: TransactionEndPoint

    init {
        transactionEndPoint = ApiServiceGenerator.createService<TransactionEndPoint>(
                TransactionEndPoint::class.java
        )
    }

    fun addLead(request: AddLeadRequest, url: String?): Observable<AddLeadResponse?>? {
        return transactionEndPoint.addLead(request, url)
    }

    fun generateOTP(request: OTPRequest, url: String?): Observable<OTPResponse?>? {
        return transactionEndPoint.generateOTP(request, url)
    }

    fun validateOTP(request: OTPRequest, url: String?): Observable<OTPResponse?>? {
        return transactionEndPoint.validateOTP(request, url)
    }
    fun validateLead(request: ValidateLeadRequest, url: String?): Observable<ValidateLeadResponse?>? {
        return transactionEndPoint.validateLead(request, url)
    }

    fun resetCustomerJourney(request: ResetCustomerJourneyRequest, url: String?): Observable<ResetCustomerJourneyResponse?>? {
        return transactionEndPoint.resetCustomerJourney (request, url)
    }


}