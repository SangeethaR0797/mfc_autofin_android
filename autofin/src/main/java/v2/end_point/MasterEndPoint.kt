package v2.end_point

import io.reactivex.Observable
import retrofit2.http.*
import v2.model.response.BankListResponse
import v2.model.response.CityNameListResponse
import v2.model.response.CommonBanksResponse
import v2.model.response.bank_offers.LoanAmountResponse
import v2.model.response.master.APIDropDownResponse
import v2.model.response.master.KYCDocumentResponse
import v2.model.response.master.MasterResponse
import v2.model.response.master.PinCodeResponse

interface MasterEndPoint {
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getKmsDrivenDetails( @Url url: String?): Observable<MasterResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getSalutations( @Url url: String?): Observable<MasterResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getResidentType( @Url url: String?): Observable<MasterResponse?>?


    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getResidentYears( @Url url: String?): Observable<MasterResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getEmploymentType( @Url url: String?): Observable<MasterResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getBankList( @Url url: String?): Observable<BankListResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getCityNameList( @Url url: String?): Observable<CityNameListResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getCommonBanks( @Url url: String?): Observable<CommonBanksResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getLoanAmountDetails( @Url url: String?): Observable<LoanAmountResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getPinCodeData( @Url url: String?): Observable<PinCodeResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getAdditionalFieldAPIDetails( @Url url: String?): Observable<APIDropDownResponse?>?

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET
    fun getKYCDocuments( @Url url: String?): Observable<KYCDocumentResponse?>?

}