package v2.repository

import io.reactivex.Observable
import v2.end_point.TransactionEndPoint
import v2.model.dto.AddLeadRequest
import v2.model.request.GenerateOTPRequest
import v2.model.response.AddLeadResponse
import v2.model.response.GenerateOTPResponse
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

    fun generateOTP(request: GenerateOTPRequest, url: String?): Observable<GenerateOTPResponse?>? {
        return transactionEndPoint.generateOTP(request, url)
    }


}