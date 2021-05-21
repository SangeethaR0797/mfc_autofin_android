package v2.model_view

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import v2.model.dto.AddLeadRequest
import v2.model.request.*
import v2.model.request.update.UpdateLeadBasicDetailsRequest
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
    public fun getAddLeadLiveData(): MutableLiveData<ApiResponse> {
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
    private val mResetCustomerJourneyLiveData: MutableLiveData<ApiResponse> =
            MutableLiveData<ApiResponse>()

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
    private val mGetCustomerDetailsLiveData: MutableLiveData<ApiResponse> =
            MutableLiveData<ApiResponse>()

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
//region AddEmploymentDetails
    private val mAddEmploymentDetails: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getAddEmploymentDetailsLiveData(): MutableLiveData<ApiResponse> {
        return mAddEmploymentDetails
    }


    public fun addEmploymentDetails(request: AddEmploymentDetailsRequest, url: String?) {
        repository.addEmploymentDetails(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mAddEmploymentDetails.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mAddEmploymentDetails.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mAddEmploymentDetails.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
//endregion AddEmploymentDetails

    //region AddResidentDetails
    private val mAddResidentDetails: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getAddResidentDetailsLiveData(): MutableLiveData<ApiResponse> {
        return mAddResidentDetails
    }


    public fun addResidentDetails(request: AddResidentDetailsRequest, url: String?) {
        repository.addResidentDetails(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mAddResidentDetails.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mAddResidentDetails.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mAddResidentDetails.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
//endregion AddResidentDetails

    //region updateLeadBasicDetails
    private val mupdateLeadBasicDetailsLiveData: MutableLiveData<ApiResponse> =
            MutableLiveData<ApiResponse>()

    public fun getUpdateLeadBasicDetailsLiveData(): MutableLiveData<ApiResponse> {
        return mupdateLeadBasicDetailsLiveData
    }


    public fun updateLeadBasicDetails(request: UpdateLeadBasicDetailsRequest, url: String?) {
        repository.updateLeadBasicDetails(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mupdateLeadBasicDetailsLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mupdateLeadBasicDetailsLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mupdateLeadBasicDetailsLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
//endregion updateAddress

    //region updateLeadBasicDetails
    private val mupdateAddressLiveData: MutableLiveData<ApiResponse> =
            MutableLiveData<ApiResponse>()

    public fun getUpdateAddressLiveData(): MutableLiveData<ApiResponse> {
        return mupdateAddressLiveData
    }


    public fun updateAddress(request: UpdateAddressRequest, url: String?) {
        repository.updateAddress(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mupdateAddressLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mupdateAddressLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mupdateAddressLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
//endregion updateAddress


    //region getAdditionalDetails

    private val mAdditionalFieldsLiveData: MutableLiveData<ApiResponse> =
            MutableLiveData<ApiResponse>()

    public fun getAdditionalFieldsLiveData(): MutableLiveData<ApiResponse> {
        return mAdditionalFieldsLiveData
    }


    public fun getAdditionalFieldsData(request: CustomerRequest, url: String?) {
        repository.getAdditionalFieldsData(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mAdditionalFieldsLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mAdditionalFieldsLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mAdditionalFieldsLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
//endregion AdditionalDetails
}