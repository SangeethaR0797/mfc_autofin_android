package v2

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.dto.CustomLoanProcessCompletedData
import v2.model.request.FinalOTPData
import v2.model.request.GenerateFinalOTPRequest
import v2.model.request.ValidateFinalOTPRequest
import v2.model.request.ValidateOTPData
import v2.model.response.OTPResponse
import v2.model.response.bank_offers.ValidateFinalOTPResponse
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.base.BaseFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FinalOTPFragment : BaseFragment() {

    var currentOTP = ""
    private lateinit var transactionViewModel: TransactionViewModel

    lateinit var loanProcessCompletedData: CustomLoanProcessCompletedData
var mobileNum=""
    lateinit var timer: CountDownTimer
    var customerID=""
    lateinit var fragmentContext: Context



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
         /*   cutomerId = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)*/
        }
        transactionViewModel = ViewModelProvider(this).get(
                TransactionViewModel::class.java
        )
        transactionViewModel.getGenerateFinalOTPLiveData().observe(this) { mApiResponse: ApiResponse? ->
            onFinalOTPResponse(
                    mApiResponse!!
            )
        }


        transactionViewModel.getValidateFinalOTPLiveData().observe(this) { mApiResponse: ApiResponse? ->
            onValidateOTPResponse(
                    mApiResponse!!
            )
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view:View= inflater.inflate(R.layout.v2_final_otp_fragment_blank, container, false)
        fragmentContext = view.context

        val textViewMobileNumber: TextView = view.findViewById(R.id.textViewMobileNumber)

        val etFinalOTPV2: EditText =  view.findViewById(R.id.etFinalOTPV2)
        val tvFinalOTPTimerV2: TextView =  view.findViewById(R.id.tvFinalOTPTimerV2)
        val tvFinalResendOTPV2: TextView =  view.findViewById(R.id.tvFinalResendOTPV2)
        val buttonSubmitFinalOTP: Button = view.findViewById(R.id.buttonSubmitFinalOTP)
        textViewMobileNumber.text = mobileNum.substring(0, 2) + "******" + mobileNum.substring(7, 9)


        timer = object : CountDownTimer(60000, 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                tvFinalOTPTimerV2.text = "" + millisUntilFinished / 1000 + " Sec"
            }

            override fun onFinish() {
                tvFinalOTPTimerV2.text = "0 Sec"
                timer.cancel()
            }
        }.start()
        etFinalOTPV2.setText(currentOTP)

        tvFinalResendOTPV2.setOnClickListener(View.OnClickListener {
            callGenerateFinalOTP()
        })

        buttonSubmitFinalOTP.setOnClickListener(View.OnClickListener {
            if (etFinalOTPV2.text.toString().isNotEmpty()) {
                currentOTP = etFinalOTPV2.text.toString()

                callValidateFinalOTP()
            } else {
                showToast("Enter OTP!")
            }
        })

        return view
    }

    private fun callValidateFinalOTP() {
    }

    private fun callGenerateFinalOTP() {
        getFinalOTPRequest()?.let { it1 -> transactionViewModel.generateFinalOTP(it1, Global.customerAPI_BaseURL + "generate-final-submit-otp") }
    }

    private fun getFinalOTPRequest(): GenerateFinalOTPRequest? {
        val finalOTPData = customerID?.toInt()?.let { FinalOTPData(it, true) }
        return finalOTPData?.let { GenerateFinalOTPRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, it) }
    }


    private fun onFinalOTPResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(fragmentContext)
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val otpRes: OTPResponse? =
                        mApiResponse.data as OTPResponse?
                if (otpRes?.data != null) {
                    currentOTP = otpRes.data.toString()
                } else {
                    showToast("Something went wrong! Please try again later!")
                }
            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
            }
            else -> {
                hideProgressDialog()
                showToast("Please enter valid details")
            }
        }
    }

    private fun getValidateFinalOTP(): ValidateFinalOTPRequest? {
        return customerID?.let { ValidateOTPData(it.toInt(), currentOTP, true) }?.let { ValidateFinalOTPRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, it) }
    }

    private fun onValidateOTPResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val validateFinalOTPRes: ValidateFinalOTPResponse? =
                        mApiResponse.data as ValidateFinalOTPResponse?
                if (validateFinalOTPRes?.status == true) {
                    navigateToSuccessFragment(loanProcessCompletedData)
                } else {
                    showToast("Please enter valid details")
                }
            }

            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
            }
            else -> {
                hideProgressDialog()
                showToast("Please enter valid details")
            }
        }
    }

}