package v2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.dto.AddLeadRequest
import v2.model.request.OTPRequest
import v2.model.request.OTPRequestData
import v2.model.response.OTPResponse
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.base.BaseFragment
import v2.view.callBackInterface.DatePickerCallBack


public class AddLeadDetailsFrag : BaseFragment(), View.OnClickListener {


    lateinit var etMobileNumberV2: EditText
    lateinit var etOTPV2: EditText
    lateinit var etBirthDate: EditText
    lateinit var llBirthDate: LinearLayout
    lateinit var cbTermsAndConditions: CheckBox
    lateinit var tvResendOTPV2: TextView
    lateinit var tvOTPTimerV2: TextView
    lateinit var btnMobileNum: Button
    lateinit var ll_otp_v2: LinearLayout
    lateinit var transactionViewModel: TransactionViewModel
    lateinit var addLeadRequest: AddLeadRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_lead_details, container, false)
        arguments?.let {
            val safeArgs = AddLeadDetailsFragArgs.fromBundle(it)
            addLeadRequest = safeArgs.addLeadRequestDetails

        }
        initViews(view)
        transactionViewModel = ViewModelProvider(requireActivity()).get(
                TransactionViewModel::class.java
        )



        return view
    }

    private fun initViews(view: View?) {
        etMobileNumberV2 = view?.findViewById(R.id.etMobileNumberV2)!!
        etOTPV2 = view.findViewById(R.id.etOTPV2)

        cbTermsAndConditions = view.findViewById(R.id.cbTermsAndConditions)
        tvResendOTPV2 = view.findViewById(R.id.tvResendOTPV2)
        tvOTPTimerV2 = view.findViewById(R.id.tvOTPTimerV2)
        btnMobileNum = view.findViewById(R.id.btnMobileNum)
        ll_otp_v2 = view.findViewById(R.id.ll_otp_v2)


        llBirthDate = view.findViewById(R.id.ll_date)
        etBirthDate = view.findViewById(R.id.et_date)

        tvResendOTPV2.setOnClickListener(this)
        btnMobileNum.setOnClickListener(this)
        llBirthDate.setOnClickListener(this)
        etBirthDate.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnMobileNum -> {
                if (ll_otp_v2.visibility == View.GONE) {
                    sendOTP()
                } else {
                    validateOTP()

                }
            }
            R.id.tvResendOTPV2 -> {
                if (etOTPV2.text.isNotEmpty())
                    etOTPV2.text.clear()

                sendOTP()
            }
            R.id.et_date -> {
                openDatePicker()
            }
            R.id.ll_date -> {
                openDatePicker()
            }
        }

    }

    private fun openDatePicker() {
        callDatePickerDialog(object : DatePickerCallBack {
            override fun dateSelected(dateValue: String) {
                etBirthDate.setText(dateValue)
            }
        })
    }

    private fun validateOTP() {
        if (etOTPV2.text.length == 6) {
            transactionViewModel!!.validateOTP(getOtpRequest(etOTPV2.text.toString(), etMobileNumberV2.text.toString()), Global.customerAPI_BaseURL + CommonStrings.VALIDATE_OTP_URL_END)

            transactionViewModel!!.getValidateOTPLiveData()
                    .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                        onValidateOTP(
                                mApiResponse!!
                        )
                    })
        } else {
            showToast("Please enter valid OTP")
        }

    }

    private fun sendOTP() {
        if (etMobileNumberV2.text.length == 10) {
            transactionViewModel!!.generateOTP(getOtpRequest(null, etMobileNumberV2.text.toString()), Global.customerAPI_BaseURL + CommonStrings.OTP_URL_END)

            transactionViewModel!!.getGenerateOTPLiveData()
                    .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                        onGenerateOTP(
                                mApiResponse!!
                        )
                    })

        } else {
            showToast("Please enter Valid Mobile Number")
        }
    }

    private fun getOtpRequest(otp: String?, mobile: String): OTPRequest {
        val otpRequest = OTPRequest()
        otpRequest.UserType = CommonStrings.USER_TYPE
        otpRequest.UserId = CommonStrings.DEALER_ID

        val otpRequestData = OTPRequestData()
        otpRequestData.CustomerMobile = mobile
        otpRequestData.OTP = otp
        otpRequest.Data = otpRequestData

        return otpRequest;
    }

    private fun onGenerateOTP(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val otpResponse: OTPResponse? = mApiResponse.data as OTPResponse?
                ll_otp_v2.visibility = View.VISIBLE
            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

    private fun onValidateOTP(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val otpResponse: OTPResponse? = mApiResponse.data as OTPResponse?
                if (otpResponse?.status!!) {
                    Toast.makeText(activity, "OTP Validate", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(activity, "invalid Validate", Toast.LENGTH_LONG).show()
                }
            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

}