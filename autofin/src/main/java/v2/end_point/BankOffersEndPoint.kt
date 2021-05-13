package v2.end_point

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url
import v2.model.request.bank_offers.BankOffersForApplicationRequest
import v2.model.request.bank_offers.SelectRecommendedBankOfferRequest
import v2.model.response.bank_offers.BankOffersForApplicationResponse
import v2.model.response.bank_offers.SelectRecommendedBankOfferResponse

public interface BankOffersEndPoint
{
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun getBankOffersForLeadApplication(@Body request: BankOffersForApplicationRequest?, @Url url: String?): Observable<BankOffersForApplicationResponse?>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    fun setSelectRecommendedBankOffer(@Body request: SelectRecommendedBankOfferRequest?, @Url url: String?): Observable<SelectRecommendedBankOfferResponse?>
}