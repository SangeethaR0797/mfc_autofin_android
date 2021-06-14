package v2.model_view

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import v2.model.dto.DashBoardDetailsRequest
import v2.model_view.Base.BaseViewModel
import v2.repository.DashboardRepository
import v2.service.utility.ApiResponse

class DashboardViewModel(application: Application) : BaseViewModel(application) {
    val repository: DashboardRepository

    init {
        repository = DashboardRepository()
    }

    //region DashboardDetails
    private val mDashboardDetailsLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
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

   

}