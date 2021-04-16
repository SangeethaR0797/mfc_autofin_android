package v2.model_view

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import model.ibb_models.IBBVehDetailsReq
import v2.model.request.GetTokenDetailsRequest
import v2.model_view.Base.BaseViewModel
import v2.repository.AuthenticationRepository
import v2.service.utility.ApiResponse

class AuthenticationViewModel(application: Application) : BaseViewModel(application) {
    val repository: AuthenticationRepository

    init {
        repository = AuthenticationRepository()
    }
//region getDealer Token
    private val mTokenDetailsLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getTokenDetailsLiveDataData(): MutableLiveData<ApiResponse> {
        return mTokenDetailsLiveData
    }


    public fun getToken(request: GetTokenDetailsRequest, url: String) {
        repository.getToken(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mTokenDetailsLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mTokenDetailsLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mTokenDetailsLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
    //endregion getDealer Token

    //region IBB Token
    private val mIBB_TokenDetailsLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getIBB_TokenDetailsLiveDataData(): MutableLiveData<ApiResponse> {
        return mIBB_TokenDetailsLiveData
    }


    public fun getIBBToken(request: IBBVehDetailsReq, url: String) {
        repository.getToken(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mTokenDetailsLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mTokenDetailsLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mTokenDetailsLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
    //endregion getDealer Token
}