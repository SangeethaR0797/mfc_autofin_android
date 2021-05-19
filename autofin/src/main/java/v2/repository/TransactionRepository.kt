package v2.repository

import io.reactivex.Observable
import v2.end_point.TransactionEndPoint
import v2.model.dto.AddLeadRequest
import v2.model.request.*
import v2.model.request.update.UpdateLeadBasicDetailsRequest
import v2.model.response.*
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
    fun updateLeadBasicDetails(request: UpdateLeadBasicDetailsRequest, url: String?): Observable<AddLeadResponse?>? {
        return transactionEndPoint.updateLeadBasicDetails(request, url)
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

    fun resetCustomerJourney(request: CustomerRequest, url: String?): Observable<ResetCustomerJourneyResponse?>? {
        return transactionEndPoint.resetCustomerJourney(request, url)
    }

    fun getCustomerDetails(request: CustomerRequest, url: String?): Observable<CustomerDetailsResponse?>? {
        return transactionEndPoint.getCustomerDetails(request, url)
    }

    fun addEmploymentDetails(request: AddEmploymentDetailsRequest, url: String?): Observable<AddLeadResponse?>? {
        return transactionEndPoint.addEmploymentDetails(request, url)
    }
    fun addResidentDetails(request: AddResidentDetailsRequest, url: String?): Observable<AddLeadResponse?>? {
        return transactionEndPoint.addResidentDetails(request, url)
    }

    fun updateAddress(request: UpdateAddressRequest, url: String?): Observable<SimpleResponse?>? {
        return transactionEndPoint.updateAddress(request, url)
    }

}