package v2.view

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mfc.autofin.framework.R
import utility.CommonMethods
import utility.CommonStrings
import utility.Global
import v2.model.request.GetTokenDetailsRequest
import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model.request.Get_IBB_TokenRequest
import v2.model.response.Get_IBB_MasterDetailsResponse
import v2.model.response.IBB_TokenResponse
import v2.model.response.TokenDetailsResponse
import v2.model_view.AuthenticationViewModel
import v2.model_view.IBB.IBB_MasterViewModel
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

        authenticationViewModel!!.getIBB_TokenDetailsLiveDataData()
                .observe(this, { mApiResponse: ApiResponse? ->
                    onIBB_TokenDetails(
                            mApiResponse!!
                    )
                })

        authenticationViewModel!!.getIBBToken(getIBB_TokenRequest()!!, Global.ibb_base_url + CommonStrings.IBB_ACCESS_TOKEN_URL_END)




    }

    private fun get_IBB_MasterDetailsRequest(): Get_IBB_MasterDetailsRequest? {
        return Get_IBB_MasterDetailsRequest(CommonStrings.IBB_TOKEN_VALUE, "year", "0", "app", null, null, null, null)
    }

    private fun getTokenRequest(): GetTokenDetailsRequest? {
        return GetTokenDetailsRequest(
                "242",
                "Dealer",
                "Dealer",
                "Token")

    }

    private fun getIBB_TokenRequest(): Get_IBB_TokenRequest? {
        return Get_IBB_TokenRequest(
                "dHk69ffu7ebP",
                "mfc@ibb.com")

    }

    private fun onTokenDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val tokenResponse: TokenDetailsResponse? = mApiResponse.data as TokenDetailsResponse?
                CommonMethods.setValueAgainstKey(this@HostActivity, CommonStrings.PREFF_ENCRYPT_TOKEN, tokenResponse!!.data.toString())
                CommonStrings.TOKEN_VALUE = tokenResponse!!.data.toString()


            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

    private fun onIBB_TokenDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val tokenResponse: IBB_TokenResponse? = mApiResponse.data as IBB_TokenResponse?
                CommonMethods.setValueAgainstKey(this@HostActivity, CommonStrings.PREFF_ENCRYPT_IBB_TOKEN, tokenResponse!!.access_token.toString())
                CommonStrings.IBB_TOKEN_VALUE = tokenResponse!!.access_token.toString()


            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }



    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                   /* v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)*/

                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}