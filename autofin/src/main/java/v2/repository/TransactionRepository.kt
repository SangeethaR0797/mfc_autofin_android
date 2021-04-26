package v2.repository

import io.reactivex.Observable
import v2.end_point.TransactionEndPoint
import v2.model.dto.AddLeadRequest
import v2.model.request.OTPRequest
import v2.model.response.AddLeadResponse
import v2.model.response.OTPResponse
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


}