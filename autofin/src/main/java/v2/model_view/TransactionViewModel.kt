package v2.model_view

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import v2.model.dto.AddLeadRequest
import v2.model.request.GenerateOTPRequest
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


    public fun generateOTP(request: GenerateOTPRequest, url: String?) {
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


}