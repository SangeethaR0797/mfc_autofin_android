package v2.model_view

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import v2.model.dto.AddLeadRequest
import v2.model.request.OTPRequest
import v2.model.request.CustomerRequest
import v2.model.request.ValidateLeadRequest
import v2.model_view.Base.BaseViewModel
import v2.repository.TransactionRepository
import v2.service.utility.ApiResponse

class TransactionViewModel(application: Application) : BaseViewModel(application) {
    val repository: TransactionRepository

    init {
        repository = TransactionRepository()
    }

    //region addLead
    private val mAddLeadLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getKmsDrivenLiveData(): MutableLiveData<ApiResponse> {
        return mAddLeadLiveData
    }


    public fun addLead(request: AddLeadRequest, url: String?) {
        repository.addLead(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mAddLeadLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mAddLeadLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mAddLeadLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
    //endregion addLead

    //region GenerateOTP
    private val mGenerateOTPLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getGenerateOTPLiveData(): MutableLiveData<ApiResponse> {
        return mGenerateOTPLiveData
    }


    public fun generateOTP(request: OTPRequest, url: String?) {
        repository.generateOTP(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mGenerateOTPLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mGenerateOTPLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mGenerateOTPLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
//endregion GenerateOTP

    //region ValidateOTP
    private val mValidateOTPLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getValidateOTPLiveData(): MutableLiveData<ApiResponse> {
        return mValidateOTPLiveData
    }


    public fun validateOTP(request: OTPRequest, url: String?) {
        repository.validateOTP(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mValidateOTPLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mValidateOTPLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mValidateOTPLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
//endregion ValidateOTP


    //region ValidateLead
    private val mValidateLeadLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getValidateLeadLiveData(): MutableLiveData<ApiResponse> {
        return mValidateLeadLiveData
    }


    public fun validateLead(request: ValidateLeadRequest, url: String?) {
        repository.validateLead(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mValidateLeadLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mValidateLeadLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mValidateLeadLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
//endregion ValidateLead

    //region ResetCustomerJourney
    private val mResetCustomerJourneyLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getResetCustomerJourneyLiveData(): MutableLiveData<ApiResponse> {
        return mResetCustomerJourneyLiveData
    }


    public fun resetCustomerJourney(request: CustomerRequest, url: String?) {
        repository.resetCustomerJourney(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mResetCustomerJourneyLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mResetCustomerJourneyLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mResetCustomerJourneyLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
//endregion ResetCustomerJourney

    //region getCustomerDetails
    private val mGetCustomerDetailsLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getCustomerDetailsLiveData(): MutableLiveData<ApiResponse> {
        return mGetCustomerDetailsLiveData
    }


    public fun getCustomerDetails(request: CustomerRequest, url: String?) {
        repository.getCustomerDetails(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mGetCustomerDetailsLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mGetCustomerDetailsLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mGetCustomerDetailsLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
//endregion getCustomerDetails


}