package v2.model_view

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import v2.model.request.StockDetailsReq
import v2.model.request.bank_offers.BankOffersForApplicationRequest
import v2.model.request.bank_offers.SelectRecommendedBankOfferRequest
import v2.model_view.Base.BaseViewModel
import v2.repository.BankOffersRepository
import v2.repository.StockRepository
import v2.service.utility.ApiResponse

class BankOffersViewModel(application: Application) : BaseViewModel(application) {
    var repository = BankOffersRepository()

    //region getBankOffersForLeadApplication
    var mBankOffersForLeadApplicationLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()

    public fun getBankOffersForLeadApplicationLiveData(): MutableLiveData<ApiResponse> {
        return mBankOffersForLeadApplicationLiveData
    }

    public fun getBankOffersForLeadApplication(request: BankOffersForApplicationRequest, url: String) {
        repository.getBankOffersForLeadApplication(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mBankOffersForLeadApplicationLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mBankOffersForLeadApplicationLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mBankOffersForLeadApplicationLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
    //endregion getBankOffersForLeadApplication


    //region setSelectRecommendedBankOffer
    var mSetSelectRecommendedBankOfferLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()

    public fun getSetSelectRecommendedBankOfferLiveData(): MutableLiveData<ApiResponse> {
        return mSetSelectRecommendedBankOfferLiveData
    }

    public fun setSelectRecommendedBankOffer(request: SelectRecommendedBankOfferRequest, url: String) {
        repository.setSelectRecommendedBankOffer(request, url)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { d -> mSetSelectRecommendedBankOfferLiveData.setValue(ApiResponse.loading()) }
                ?.let {
                    disposables.add(
                            it
                                    .subscribe(
                                            { result ->
                                                mSetSelectRecommendedBankOfferLiveData.setValue(result?.let {
                                                    ApiResponse.success(
                                                            it
                                                    )
                                                })
                                            }
                                    ) { throwable ->
                                        mSetSelectRecommendedBankOfferLiveData.setValue(
                                                ApiResponse.error(
                                                        throwable
                                                )
                                        )
                                    })
                }
    }
//endregion setSelectRecommendedBankOffer


}