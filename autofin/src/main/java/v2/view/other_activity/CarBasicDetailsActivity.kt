package v2.view.other_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model.response.Get_IBB_MasterDetailsResponse
import v2.model.response.TokenDetailsResponse
import v2.model_view.AuthenticationViewModel
import v2.model_view.IBB.IBB_MasterViewModel
import v2.service.utility.ApiResponse


class CarBasicDetailsActivity : AppCompatActivity() {

    var iBB_MasterViewModel: IBB_MasterViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.v2_activity_basic_data_selection)


        iBB_MasterViewModel = ViewModelProvider(this@CarBasicDetailsActivity).get(
                IBB_MasterViewModel::class.java
        )


        iBB_MasterViewModel!!.getIBB_MasterDetailsLiveData()
                .observe(this, { mApiResponse: ApiResponse? ->
                    onIBB_MasterDetails(
                            mApiResponse!!
                    )
                })

        var yearRequest = getIBBMasterDetailsRequest(CommonStrings.IBB_TOKEN_VALUE, "year", "0", "app", null, null, null, null)
        iBB_MasterViewModel!!.getIBB_MasterDetails(yearRequest, Global.ibb_base_url + CommonStrings.IBB_VEH_DETAILS_END_POINT)

    }

    private fun getIBBMasterDetailsRequest(access_token: String? = null,
                                           mfor: String? = null,
                                           pricefor: String? = null,
                                           tag: String? = null,
                                           year: String? = null,
                                           month: String? = null,
                                           make: String? = null,
                                           model: String? = null): Get_IBB_MasterDetailsRequest? {
        return Get_IBB_MasterDetailsRequest(access_token, mfor, pricefor, tag, year, month, make, model)
    }

    private fun onIBB_MasterDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val masterResponse: Get_IBB_MasterDetailsResponse? = mApiResponse.data as Get_IBB_MasterDetailsResponse?


            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }
}