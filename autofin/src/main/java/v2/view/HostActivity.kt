package v2.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mfc.autofin.framework.R
import model.token.GetTokenReq
import utility.CommonStrings
import utility.Global
import v2.model.TokenDetails
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

    private fun getTokenRequest(): GetTokenReq? {
        val getTokenReq = GetTokenReq()
        getTokenReq.userId = "242"
        getTokenReq.userType = "Dealer"
        getTokenReq.requestFrom = "Dealer"
        getTokenReq.data = "Token"
        return getTokenReq
    }

    private fun onTokenDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val token: TokenDetails? = mApiResponse.data as TokenDetails?
                Toast.makeText(this@HostActivity, token!!.data.toString(), Toast.LENGTH_LONG)
                        .show()


            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }
}