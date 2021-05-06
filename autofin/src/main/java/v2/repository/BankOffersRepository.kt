package v2.repository

import io.reactivex.Observable
import v2.end_point.BankOffersEndPoint
import v2.model.request.bank_offers.BankOffersForApplicationRequest
import v2.model.response.bank_offers.BankOffersForApplicationResponse
import v2.service.ApiServiceGenerator

class BankOffersRepository
{
    var bankOffersEndPoint: BankOffersEndPoint = ApiServiceGenerator.createService<BankOffersEndPoint>(
            BankOffersEndPoint::class.java)

    fun getStockDetailsRes(request: BankOffersForApplicationRequest, url: String): Observable<BankOffersForApplicationResponse?>? {
        return bankOffersEndPoint.getBankOffersForLeadApplication(request,url)
    }


}