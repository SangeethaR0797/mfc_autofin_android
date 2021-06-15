package v2.model_view

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import v2.model.request.CommonRequest
import v2.model_view.Base.BaseViewModel
import v2.repository.NoticeBoardRepository
import v2.service.utility.ApiResponse

class NoticeBoardViewModel(application: Application) : BaseViewModel(application) {
    val repository: NoticeBoardRepository

    init {
        repository = NoticeBoardRepository()
    }

    //region noticeBoardAction
    private val mNoticeBoardActionLiveData: MutableLiveData<ApiResponse> =
        MutableLiveData<ApiResponse>()

    public fun getNoticeBoardActionLiveData(): MutableLiveData<ApiResponse> {
        return mNoticeBoardActionLiveData
    }


    public fun noticeBoardAction(request: CommonRequest, url: String?) {
        repository.noticeBoardAction(request, url)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { d -> mNoticeBoardActionLiveData.setValue(ApiResponse.loading()) }
            ?.let {
                disposables.add(
                    it
                        .subscribe(
                            { result ->
                                mNoticeBoardActionLiveData.setValue(result?.let {
                                    ApiResponse.success(
                                        it
                                    )
                                })
                            }
                        ) { throwable ->
                            mNoticeBoardActionLiveData.setValue(
                                ApiResponse.error(
                                    throwable
                                )
                            )
                        })
            }
    }
    //endregion noticeBoardAction


    //region getNoticeBoardDetails
    private val mNoticeBoardDetailsLiveData: MutableLiveData<ApiResponse> =
        MutableLiveData<ApiResponse>()

    public fun getNoticeBoardDetailsLiveData(): MutableLiveData<ApiResponse> {
        return mNoticeBoardDetailsLiveData
    }


    public fun getNoticeBoardDetails(request: CommonRequest, url: String?) {
        repository.getNoticeBoardData(request, url)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { d -> mNoticeBoardDetailsLiveData.setValue(ApiResponse.loading()) }
            ?.let {
                disposables.add(
                    it
                        .subscribe(
                            { result ->
                                mNoticeBoardDetailsLiveData.setValue(result?.let {
                                    ApiResponse.success(
                                        it
                                    )
                                })
                            }
                        ) { throwable ->
                            mNoticeBoardDetailsLiveData.setValue(
                                ApiResponse.error(
                                    throwable
                                )
                            )
                        })
            }
    }
    //endregion getNoticeBoardDetails

}