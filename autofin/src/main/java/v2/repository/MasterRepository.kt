package v2.repository

import io.reactivex.Observable
import v2.end_point.MasterEndPoint
import v2.model.response.BankListResponse
import v2.model.response.CityNameListResponse
import v2.model.response.CommonBanksResponse
import v2.model.response.bank_offers.LoanAmountResponse
import v2.model.response.master.APIDropDownResponse
import v2.model.response.master.MasterResponse
import v2.model.response.master.PinCodeResponse
import v2.service.ApiServiceGenerator

class MasterRepository {
    lateinit var masterEndPoint: MasterEndPoint

    init {
        masterEndPoint = ApiServiceGenerator.createService<MasterEndPoint>(
                MasterEndPoint::class.java
        )
    }

    fun getKmsDrivenDetails(url: String): Observable<MasterResponse?>?
    {
        return masterEndPoint.getKmsDrivenDetails(url)
    }

    fun getSalutations(url: String): Observable<MasterResponse?>?
    {
        return masterEndPoint.getSalutations(url)
    }

    fun getResidentType(url: String): Observable<MasterResponse?>?
    {
        return masterEndPoint.getResidentType(url)
    }

    fun getResidentYears(url: String): Observable<MasterResponse?>?
    {
        return masterEndPoint.getResidentYears(url)
    }

    fun getEmploymentType(url: String): Observable<MasterResponse?>?
    {
        return masterEndPoint.getEmploymentType(url)
    }

    fun getBankList(url: String): Observable<BankListResponse?>?
    {
        return masterEndPoint.getBankList(url)
    }

    fun getCityNameList(url: String): Observable<CityNameListResponse?>?
    {
        return masterEndPoint.getCityNameList(url)
    }

    fun getCommonBanks(url: String): Observable<CommonBanksResponse?>?
    {
        return masterEndPoint.getCommonBanks(url)
    }

    fun getLoanAmountDetails(url: String): Observable<LoanAmountResponse?>?
    {
        return masterEndPoint.getLoanAmountDetails(url)
    }

    fun getPinCodeData(url: String): Observable<PinCodeResponse?>?
    {
        return masterEndPoint.getPinCodeData(url)
    }

    fun getAdditionalFieldAPIDetails(url:String):Observable<APIDropDownResponse?>?
    {
        return masterEndPoint.getAdditionalFieldAPIDetails(url)

    }

}