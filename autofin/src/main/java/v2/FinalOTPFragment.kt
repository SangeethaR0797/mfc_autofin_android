package v2

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
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

class FinalOTPFragment : BaseFragment() {

    var currentOTP = ""
    private lateinit var transactionViewModel: TransactionViewModel

    private lateinit var textViewMobileNumber: TextView
    private lateinit var etFinalOTPV2: EditText
    private lateinit var tvFinalOTPTimerV2: TextView
    private lateinit var tvFinalResendOTPV2: TextView
    private lateinit var buttonSubmitFinalOTP: Button

    lateinit var loanProcessCompletedData: CustomLoanProcessCompletedData
    var mobileNum = ""
    lateinit var timer: CountDownTimer
    var customerID = ""
    lateinit var fragmentContext: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val safeArgs = FinalOTPFragmentArgs.fromBundle(it)
            customerID = safeArgs.cusotmerID
            mobileNum = safeArgs.mobileNumber
            loanProcessCompletedData = safeArgs.loanData

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
        val view: View = inflater.inflate(R.layout.v2_final_otp_fragment_blank, container, false)
        fragmentContext = view.context

        textViewMobileNumber = view.findViewById(R.id.textViewMobileNumber)

        etFinalOTPV2 = view.findViewById(R.id.etFinalOTPV2)
        tvFinalOTPTimerV2 = view.findViewById(R.id.tvFinalOTPTimerV2)
        tvFinalResendOTPV2 = view.findViewById(R.id.tvFinalResendOTPV2)

        buttonSubmitFinalOTP = view.findViewById(R.id.buttonSubmitFinalOTP)

        tvFinalResendOTPV2.setOnClickListener(View.OnClickListener {
            timer.cancel()
            callGenerateFinalOTP()
        })

        buttonSubmitFinalOTP.setOnClickListener(View.OnClickListener {
            if (etFinalOTPV2.text.toString().isNotEmpty()) {
                currentOTP = etFinalOTPV2.text.toString()
                timer.cancel()
                callValidateFinalOTP()
            } else {
                showToast("Enter OTP!")
            }
        })
        callGenerateFinalOTP()
        initView()
        return view
    }

    private fun initView() {

        textViewMobileNumber.text = mobileNum.substring(0, 2) + "******" + mobileNum.substring(7, 9)
        etFinalOTPV2.setText(currentOTP)
    }

    private fun initiateTimer()
    {
        timer = object : CountDownTimer(60000, 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                tvFinalOTPTimerV2.text = "" + millisUntilFinished / 1000 + " Sec"
            }

            override fun onFinish() {
                timer.cancel()
                tvFinalOTPTimerV2.text = "0 Sec"
            }
        }.start()

    }

    private fun callGenerateFinalOTP() {
        getFinalOTPRequest()?.let { it1 -> transactionViewModel.generateFinalOTP(it1, Global.customerAPI_BaseURL + CommonStrings.GET_FINAL_OTP_URL_END_POINT) }
    }

    private fun callValidateFinalOTP() {
        getValidateFinalOTP()?.let { transactionViewModel.validateFinalOTP(it, Global.customerAPI_BaseURL + CommonStrings.VALIDATE_FINAL_OTP) }
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
                    etFinalOTPV2.setText(currentOTP)
                    initiateTimer()
                } else {
                    showToast("Something went wrong! Please try again later!")
                }
            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                val otpRes: OTPResponse? =
                        mApiResponse.data as OTPResponse?
                showToast("Something went wrong ")
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