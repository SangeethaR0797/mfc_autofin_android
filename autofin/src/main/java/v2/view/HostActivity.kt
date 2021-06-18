package v2.view

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mfc.autofin.framework.R
import utility.AutoFinConstants
import utility.CommonMethods
import utility.CommonStrings
import utility.Global
import v2.model.request.*
import v2.model.response.IBB_TokenResponse
import v2.model.response.TokenDetailsResponse
import v2.model_view.AuthenticationViewModel
import v2.service.utility.ApiResponse
import v2.utility.connectivity.ConnectivityReceiver
import v2.utility.connectivity.ConnectivityReceiverListener
import v2.view.callBackInterface.AppTokenChangeInterface


class HostActivity : AppCompatActivity(), ConnectivityReceiverListener {
    var tvConnectivityMessage: TextView? = null

    var authenticationViewModel: AuthenticationViewModel? = null
    private var myConnectivityReceiver: ConnectivityReceiver? = null

    var appTokenChangeInterface: AppTokenChangeInterface? = null


    private fun broadcastIntent() {
        myConnectivityReceiver = ConnectivityReceiver()
        registerReceiver(
            myConnectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            tvConnectivityMessage!!.visibility = View.GONE
        } else {
            tvConnectivityMessage!!.visibility = View.VISIBLE
        }

    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this@HostActivity
        broadcastIntent()
    }

    override fun onPause() {
        super.onPause()
        try {
            if (myConnectivityReceiver != null) {
                unregisterReceiver(myConnectivityReceiver)
                myConnectivityReceiver = null
            }
        } catch (e: Exception) {
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_host)
        tvConnectivityMessage = findViewById(R.id.tv_connectivity_message)

        myConnectivityReceiver = ConnectivityReceiver()


        CommonStrings.APP_NAME = intent.getStringExtra(AutoFinConstants.APP_NAME)
        CommonStrings.DEALER_ID = intent.getStringExtra(AutoFinConstants.DEALER_ID)
        CommonStrings.USER_TYPE = intent.getStringExtra(AutoFinConstants.USER_TYPE)


        authenticationViewModel = ViewModelProvider(this@HostActivity).get(
            AuthenticationViewModel::class.java
        )




        authenticationViewModel!!.getTokenDetailsLiveDataData()
            .observe(this, { mApiResponse: ApiResponse? ->
                onTokenDetails(
                    mApiResponse!!
                )
            })

        authenticationViewModel!!.getToken(
            getTokenRequest()!!,
            Global.customerDetails_BaseURL + CommonStrings.TOKEN_URL_END
        )

        authenticationViewModel!!.getIBB_TokenDetailsLiveDataData()
            .observe(this, { mApiResponse: ApiResponse? ->
                onIBB_TokenDetails(
                    mApiResponse!!
                )
            })

        authenticationViewModel!!.getIBBToken(
            getIBB_TokenRequest()!!,
            Global.ibb_base_url + CommonStrings.IBB_ACCESS_TOKEN_URL_END
        )


    }

    private fun get_IBB_MasterDetailsRequest(): Get_IBB_MasterDetailsRequest? {
        return Get_IBB_MasterDetailsRequest(
            CommonStrings.IBB_TOKEN_VALUE,
            "year",
            "0",
            "app",
            null,
            null,
            null,
            null
        )
    }

    private fun getTokenRequest(): GetTokenDetailsRequest? {
        return GetTokenDetailsRequest(
            CommonStrings.DEALER_ID,
            CommonStrings.USER_TYPE,
            CommonStrings.USER_TYPE,
            "Token"
        )

    }

    private fun getIBB_TokenRequest(): Get_IBB_TokenRequest? {
        return Get_IBB_TokenRequest(
            CommonStrings.IBB_PASSWORD,
            CommonStrings.IBB_USERNAME
        )

    }

    private fun onTokenDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val tokenResponse: TokenDetailsResponse? =
                    mApiResponse.data as TokenDetailsResponse?
                CommonMethods.setValueAgainstKey(
                    this@HostActivity,
                    CommonStrings.PREFF_ENCRYPT_TOKEN,
                    tokenResponse!!.data!!.token.toString()
                )
                CommonStrings.TOKEN_VALUE = tokenResponse!!.data!!.token.toString()
                refresh()

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
                CommonMethods.setValueAgainstKey(
                    this@HostActivity,
                    CommonStrings.PREFF_ENCRYPT_IBB_TOKEN,
                    tokenResponse!!.access_token.toString()
                )
                CommonStrings.IBB_TOKEN_VALUE = tokenResponse!!.access_token.toString()
                refresh()
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

    override fun onBackPressed() {
        super.onBackPressed()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.primaryNavigationFragment!!.childFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun refresh() {
        if (!TextUtils.isEmpty(CommonStrings.TOKEN_VALUE) &&
            !TextUtils.isEmpty(CommonStrings.IBB_ACCESS_TOKEN_URL_END) &&
            appTokenChangeInterface != null
        ) {

            appTokenChangeInterface!!.onTokenReceivedOrRefresh()
        }
    }

}