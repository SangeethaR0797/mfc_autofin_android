package v2.model_view

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import v2.model.dto.DashBoardDetailsRequest
import v2.model.request.CommonRequest
import v2.model.request.EmiRequest
import v2.model_view.Base.BaseViewModel
import v2.repository.DashboardRepository
import v2.service.utility.ApiResponse

class DashboardViewModel(application: Application) : BaseViewModel(application) {
    val repository: DashboardRepository

    init {
        repository = DashboardRepository()
    }

    //region DashboardDetails
    private val mDashboardDetailsLiveData: MutableLiveData<ApiResponse> =
        MutableLiveData<ApiResponse>()

    public fun getDashboardDetailsLiveData(): MutableLiveData<ApiResponse> {
        return mDashboardDetailsLiveData
    }


    public fun getDashboardDetails(request: DashBoardDetailsRequest, url: String?) {
        repository.getDashboardDetails(request, url)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { d -> mDashboardDetailsLiveData.setValue(ApiResponse.loading()) }
            ?.let {
                disposables.add(
                    it
                        .subscribe(
                            { result ->
                                mDashboardDetailsLiveData.setValue(result?.let {
                                    ApiResponse.success(
                                        it
                                    )
                                })
                            }
                        ) { throwable ->
                            mDashboardDetailsLiveData.setValue(
                                ApiResponse.error(
                                    throwable
                                )
                            )
                        })
            }
    }
    //endregion DashboardDetails

    //region getRuleEngineBanks
    private val mRuleEngineBanksLiveData: MutableLiveData<ApiResponse> =
        MutableLiveData<ApiResponse>()

    public fun getRuleEngineBanksLiveData(): MutableLiveData<ApiResponse> {
        return mRuleEngineBanksLiveData
    }


    public fun getRuleEngineBanks(url: String?) {
        repository.getRuleEngineBanks(url)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { d -> mRuleEngineBanksLiveData.setValue(ApiResponse.loading()) }
            ?.let {
                disposables.add(
                    it
                        .subscribe(
                            { result ->
                                mRuleEngineBanksLiveData.setValue(result?.let {
                                    ApiResponse.success(
                                        it
                                    )
                                })
                            }
                        ) { throwable ->
                            mRuleEngineBanksLiveData.setValue(
                                ApiResponse.error(
                                    throwable
                                )
                            )
                        })
            }
    }
    //endregion getRuleEngineBanks


    //region getDealerCommission
    private val mDealerCommissionLiveData: MutableLiveData<ApiResponse> =
        MutableLiveData<ApiResponse>()

    public fun getDealerCommissionLiveData(): MutableLiveData<ApiResponse> {
        return mDealerCommissionLiveData
    }


    public fun getDealerCommission(request: CommonRequest, url: String?) {
        repository.getDealerCommission(request, url)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { d -> mDealerCommissionLiveData.setValue(ApiResponse.loading()) }
            ?.let {
                disposables.add(
                    it
                        .subscribe(
                            { result ->
                                mDealerCommissionLiveData.setValue(result?.let {
                                    ApiResponse.success(
                                        it
                                    )
                                })
                            }
                        ) { throwable ->
                            mDealerCommissionLiveData.setValue(
                                ApiResponse.error(
                                    throwable
                                )
                            )
                        })
            }
    }
    //endregion getDealerCommission

    //region getBankFeaturesAndChargesDetails
    private val mBankFeaturesAndChargesDetailsLiveData: MutableLiveData<ApiResponse> =
        MutableLiveData<ApiResponse>()

    public fun getBankFeaturesAndChargesDetailsLiveData(): MutableLiveData<ApiResponse> {
        return mBankFeaturesAndChargesDetailsLiveData
    }


    public fun getBankFeaturesAndChargesDetails(url: String?) {
        repository.getBankFeaturesAndChargesDetails(url)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { d -> mBankFeaturesAndChargesDetailsLiveData.setValue(ApiResponse.loading()) }
            ?.let {
                disposables.add(
                    it
                        .subscribe(
                            { result ->
                                mBankFeaturesAndChargesDetailsLiveData.setValue(result?.let {
                                    ApiResponse.success(
                                        it
                                    )
                                })
                            }
                        ) { throwable ->
                            mBankFeaturesAndChargesDetailsLiveData.setValue(
                                ApiResponse.error(
                                    throwable
                                )
                            )
                        })
            }
    }
    //endregion getBankFeaturesAndChargesDetails


    //region getEmiAmount
    private val mEmiAmountLiveData: MutableLiveData<ApiResponse> =
        MutableLiveData<ApiResponse>()

    public fun getEmiAmountLiveData(): MutableLiveData<ApiResponse> {
        return mEmiAmountLiveData
    }


    public fun getEmiAmount(request: EmiRequest, url: String?) {
        repository.getEmiAmount(request, url)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { d -> mEmiAmountLiveData.setValue(ApiResponse.loading()) }
            ?.let {
                disposables.add(
                    it
                        .subscribe(
                            { result ->
                                mEmiAmountLiveData.setValue(result?.let {
                                    ApiResponse.success(
                                        it
                                    )
                                })
                            }
                        ) { throwable ->
                            mEmiAmountLiveData.setValue(
                                ApiResponse.error(
                                    throwable
                                )
                            )
                        })
            }
    }
    //endregion getEmiAmount

    //region getAwsS3Details
    private val mAwsS3DetailsLiveData: MutableLiveData<ApiResponse> =
        MutableLiveData<ApiResponse>()

    public fun getAwsS3DetailsLiveData(): MutableLiveData<ApiResponse> {
        return mAwsS3DetailsLiveData
    }


    public fun getAwsS3Details(url: String?) {
        repository.getAwsS3Details(url)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { d -> mAwsS3DetailsLiveData.setValue(ApiResponse.loading()) }
            ?.let {
                disposables.add(
                    it
                        .subscribe(
                            { result ->
                                mAwsS3DetailsLiveData.setValue(result?.let {
                                    ApiResponse.success(
                                        it
                                    )
                                })
                            }
                        ) { throwable ->
                            mAwsS3DetailsLiveData.setValue(
                                ApiResponse.error(
                                    throwable
                                )
                            )
                        })
            }
    }
    //endregion getAwsS3Details


}