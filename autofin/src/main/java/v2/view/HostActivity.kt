package v2.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mfc.autofin.framework.R
import model.token.GetTokenReq
import utility.CommonStrings
import utility.Global
import v2.model.request.GetTokenDetailsRequest
import v2.model.response.TokenDetailsResponse
import v2.model_view.Base.AuthenticationViewModel
import v2.service.utility.ApiResponse


class HostActivity : AppCompatActivity() {
    var authenticationViewModel: AuthenticationViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        authenticationViewModel = ViewModelProvider(this@HostActivity).get(
                AuthenticationViewModel::class.java
        )

        authenticationViewModel!!.getTokenDetailsLiveDataData()
                .observe(this, { mApiResponse: ApiResponse? ->
                    onTokenDetails(
                            mApiResponse!!
                    )
                })

        authenticationViewModel!!.getToken(getTokenRequest()!!, Global.customerDetails_BaseURL + CommonStrings.TOKEN_URL_END)


    }

    private fun getTokenRequest(): GetTokenDetailsRequest? {
        return GetTokenDetailsRequest(
                "242",
                "Dealer",
                "Dealer",
                "Token")

    }

    private fun onTokenDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val tokenResponse: TokenDetailsResponse? = mApiResponse.data as TokenDetailsResponse?
                Toast.makeText(this@HostActivity, tokenResponse!!.data.toString(), Toast.LENGTH_LONG)
                        .show()


            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }
}