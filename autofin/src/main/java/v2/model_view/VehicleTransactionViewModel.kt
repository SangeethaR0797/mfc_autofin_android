package v2.model_view

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import model.ibb_models.IBBVehDetailsReq
import v2.model.dto.AddLeadRequest
import v2.model.request.GetTokenDetailsRequest
import v2.model.request.Get_IBB_TokenRequest
import v2.model_view.Base.BaseViewModel
import v2.repository.AuthenticationRepository
import v2.repository.MasterRepository
import v2.repository.VehicleTransactionRepository
import v2.service.utility.ApiResponse

class VehicleTransactionViewModel(application: Application) : BaseViewModel(application) {
    val repository: VehicleTransactionRepository

    init {
        repository = VehicleTransactionRepository()
    }

    //region addLead
    private val mAddLeadLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    public fun getKmsDrivenLiveData(): MutableLiveData<ApiResponse> {
        return mAddLeadLiveData
    }


    public fun addLead(request: AddLeadRequest, url: String?) {
        repository.addLead(request,url)?.subscribeOn(Schedulers.io())
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


}