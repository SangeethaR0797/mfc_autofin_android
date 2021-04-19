package v2.model_view.IBB

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model_view.Base.BaseViewModel
import v2.repository.IBB_MasterDetailsRepository
import v2.service.utility.ApiResponse

class IBB_MasterViewModel(application: Application) : BaseViewModel(application) {
    val repository: IBB_MasterDetailsRepository

    init {
        repository = IBB_MasterDetailsRepository()
    }

    //region getIBB_MasterDetails
    private val mIBB_MasterDetailsLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getTokenDetailsLiveDataData(): MutableLiveData<ApiResponse> {
        return mIBB_MasterDetailsLiveData
    }


    public fun getIBB_MasterDetails(request: Get_IBB_MasterDetailsRequest, url: String) {
        repository.getIBB_MasterDetails(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mIBB_MasterDetailsLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mIBB_MasterDetailsLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mIBB_MasterDetailsLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
    //endregion getIBB_MasterDetails


}