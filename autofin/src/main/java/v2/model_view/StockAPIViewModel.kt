package v2.model_view

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import utility.CommonMethods
import utility.CommonStrings
import v2.model.request.StockDetailsReq
import v2.model.response.IBB_TokenResponse
import v2.model.response.StockResponse
import v2.model_view.Base.BaseViewModel
import v2.repository.StockRepository
import v2.service.utility.ApiResponse

class StockAPIViewModel(application: Application) : BaseViewModel(application)
{
    var repository = StockRepository()
    var mStockLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()

    // Get Stock details region

    public fun getStockDetails(request: StockDetailsReq, url: String) {
        repository.getStockDetailsRes(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mStockLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mStockLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mStockLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }


    public fun getStockDetailsLiveDataData(): MutableLiveData<ApiResponse> {
        return mStockLiveData
    }

    // GetStock region end



}