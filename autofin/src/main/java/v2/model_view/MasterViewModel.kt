package v2.model_view

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import model.ibb_models.IBBVehDetailsReq
import v2.model.request.GetTokenDetailsRequest
import v2.model.request.Get_IBB_TokenRequest
import v2.model_view.Base.BaseViewModel
import v2.repository.AuthenticationRepository
import v2.repository.MasterRepository
import v2.service.utility.ApiResponse

class MasterViewModel(application: Application) : BaseViewModel(application) {
    val repository: MasterRepository

    init {
        repository = MasterRepository()
    }

    //region KmsDrivenDetails
    private val mKmsDrivenLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getKmsDrivenLiveData(): MutableLiveData<ApiResponse> {
        return mKmsDrivenLiveData
    }


    public fun getKmsDrivenDetails(url: String) {
        repository.getKmsDrivenDetails(url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mKmsDrivenLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it.subscribe(
                                    { result ->
                                        mKmsDrivenLiveData.setValue(result?.let {
                                            ApiResponse.success(
                                                    it
                                            )
                                        })
                                    }
                            ) { throwable ->
                                mKmsDrivenLiveData.setValue(
                                        ApiResponse.error(
                                                throwable
                                        )
                                )
                            })
                }
    }
    //endregion KmsDrivenDetails

    //region SalutationsDetails
    private val mSalutationsLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getSalutationsLiveData(): MutableLiveData<ApiResponse> {
        return mSalutationsLiveData
    }


    public fun getSalutations(url: String) {
        repository.getSalutations(url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mSalutationsLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it.subscribe(
                                    { result ->
                                        mSalutationsLiveData.setValue(result?.let {
                                            ApiResponse.success(
                                                    it
                                            )
                                        })
                                    }
                            ) { throwable ->
                                mSalutationsLiveData.setValue(
                                        ApiResponse.error(
                                                throwable
                                        )
                                )
                            })
                }
    }
//endregion SalutationsDetails

    //region ResidentTypeDetails
    private val mResidentTypeLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getResidentTypeLiveData(): MutableLiveData<ApiResponse> {
        return mResidentTypeLiveData
    }


    public fun getResidentType(url: String) {
        repository.getResidentType(url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mResidentTypeLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it.subscribe(
                                    { result ->
                                        mResidentTypeLiveData.setValue(result?.let {
                                            ApiResponse.success(
                                                    it
                                            )
                                        })
                                    }
                            ) { throwable ->
                                mResidentTypeLiveData.setValue(
                                        ApiResponse.error(
                                                throwable
                                        )
                                )
                            })
                }
    }
    //endregion ResidentTypeDetails

    //region ResidentYearsDetails
    private val mResidentYearsLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getResidentYearsLiveData(): MutableLiveData<ApiResponse> {
        return mResidentYearsLiveData
    }


    public fun getResidentYears(url: String) {
        repository.getResidentYears(url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mResidentYearsLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it.subscribe(
                                    { result ->
                                        mResidentYearsLiveData.setValue(result?.let {
                                            ApiResponse.success(
                                                    it
                                            )
                                        })
                                    }
                            ) { throwable ->
                                mResidentYearsLiveData.setValue(
                                        ApiResponse.error(
                                                throwable
                                        )
                                )
                            })
                }
    }
//endregion ResidentYearsDetails

    //region EmploymentTypeDetails
    private val mEmploymentTypeLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getEmploymentTypeLiveData(): MutableLiveData<ApiResponse> {
        return mEmploymentTypeLiveData
    }


    public fun getEmploymentTypeDetails(url: String) {
        repository.getEmploymentType(url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mEmploymentTypeLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it.subscribe(
                                    { result ->
                                        mEmploymentTypeLiveData.setValue(result?.let {
                                            ApiResponse.success(
                                                    it
                                            )
                                        })
                                    }
                            ) { throwable ->
                                mEmploymentTypeLiveData.setValue(
                                        ApiResponse.error(
                                                throwable
                                        )
                                )
                            })
                }
    }
//endregion EmploymentTypeDetails

    //region BankListDetails
    private val mBankListLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()

    public fun getBankListLiveData(): MutableLiveData<ApiResponse> {
        return mBankListLiveData
    }


    public fun getBankList(url: String) {
        repository.getBankList(url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mBankListLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it.subscribe(
                                    { result ->
                                        mBankListLiveData.setValue(result?.let {
                                            ApiResponse.success(
                                                    it
                                            )
                                        })
                                    }
                            ) { throwable ->
                                mBankListLiveData.setValue(
                                        ApiResponse.error(
                                                throwable
                                        )
                                )
                            })
                }
    }
//endregion BankListDetails

    //region CityNameListDetails
    private val mCityNameListLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getCityNameListLiveData(): MutableLiveData<ApiResponse> {
        return mCityNameListLiveData
    }


    public fun getCityNameList(url: String) {
        repository.getCityNameList(url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mCityNameListLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it.subscribe(
                                    { result ->
                                        mCityNameListLiveData.setValue(result?.let {
                                            ApiResponse.success(
                                                    it
                                            )
                                        })
                                    }
                            ) { throwable ->
                                mCityNameListLiveData.setValue(
                                        ApiResponse.error(
                                                throwable
                                        )
                                )
                            })
                }
    }
//endregion CityNameListDetails


    //region CommonBanksDetails
    private val mCommonBanksLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getCommonBanksLiveData(): MutableLiveData<ApiResponse> {
        return mCommonBanksLiveData
    }


    public fun getCommonBanks(url: String) {
        repository.getCommonBanks(url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mCommonBanksLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it.subscribe(
                                    { result ->
                                        mCommonBanksLiveData.setValue(result?.let {
                                            ApiResponse.success(
                                                    it
                                            )
                                        })
                                    }
                            ) { throwable ->
                                mCommonBanksLiveData.setValue(
                                        ApiResponse.error(
                                                throwable
                                        )
                                )
                            })
                }
    }
//endregion CommonBanksDetails

    // region getLoanAmount

    var mLoanAmountLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()

    public fun getBankOfferLoanLiveData(): MutableLiveData<ApiResponse> {
        return mLoanAmountLiveData
    }

    public fun getBankOffersLoanAmount(url: String) {
        repository.getLoanAmountDetails(url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mLoanAmountLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mLoanAmountLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mLoanAmountLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }


    // endregion getLoamAmount

    //region CityNameByPinCode

    private val mPinCodeData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()

    public fun getPinCodeDataLiveData(): MutableLiveData<ApiResponse> {
        return mPinCodeData
    }

    public fun getPinCodeData(url: String) {
        repository.getPinCodeData(url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mPinCodeData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it.subscribe(
                                    { result ->
                                        mPinCodeData.setValue(result?.let {
                                            ApiResponse.success(
                                                    it
                                            )
                                        })
                                    }
                            ) { throwable ->
                                mPinCodeData.setValue(
                                        ApiResponse.error(
                                                throwable
                                        )
                                )
                            })
                }
    }
//endregion CityNameByPinCode

//region AdditionalFieldsAPIDetails

    private val mAdditionalFieldAPILiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()

    public fun getAdditionalFieldAPILiveData(): MutableLiveData<ApiResponse> {
        return mAdditionalFieldAPILiveData
    }

    public fun getAdditionalFieldAPIData(url: String) {
        repository.getAdditionalFieldAPIDetails(url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mAdditionalFieldAPILiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it.subscribe(
                                    { result ->
                                        mAdditionalFieldAPILiveData.setValue(result?.let {
                                            ApiResponse.success(
                                                    it
                                            )
                                        })
                                    }
                            ) { throwable ->
                                mAdditionalFieldAPILiveData.setValue(
                                        ApiResponse.error(
                                                throwable
                                        )
                                )
                            })
                }
    }
//endregion AdditionalFieldsAPIDetails

    //region GetKYCUploadDocument

    private val mKYCDocumentLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()

    public fun getKYCDocumentLiveData(): MutableLiveData<ApiResponse> {
        return mKYCDocumentLiveData
    }

    public fun getKYCDocumentResponse(url: String) {
        repository.getKYCDocuments(url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mKYCDocumentLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it.subscribe(
                                    { result ->
                                        mKYCDocumentLiveData.setValue(result?.let {
                                            ApiResponse.success(
                                                    it
                                            )
                                        })
                                    }
                            ) { throwable ->
                                mKYCDocumentLiveData.setValue(
                                        ApiResponse.error(
                                                throwable
                                        )
                                )
                            })
                }
    }

    //endregion GetKYCUploadDocument


}