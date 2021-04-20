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

    //region getDealer Token
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
                            it
                                    .subscribe(
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
    //endregion getDealer Token


}