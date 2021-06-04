package v2.view.utility_view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.mfc.autofin.framework.R
import kotlinx.android.synthetic.*
import okhttp3.internal.format
import utility.CommonStrings
import utility.Global
import v2.model.dto.CustomLoanProcessCompletedData
import v2.model.request.*
import v2.model.response.CustomerDetailsData
import v2.model.response.CustomerDetailsResponse
import v2.model.response.OTPResponse
import v2.model.response.bank_offers.BankTAndCResponse
import v2.model.response.bank_offers.ValidateFinalOTPResponse
import v2.model_view.BankOffersViewModel
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.DocumentUploadFragmentArgs
import v2.view.base.BaseFragment

class SelectedBankOfferStatusFragment : BaseFragment() {
    private var customerID: String? = ""
    private var param2: String? = null
    private lateinit var customerViewModel: TransactionViewModel
    private lateinit var bankTAndCViewModel: BankOffersViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    lateinit var customerDetailsResponse: CustomerDetailsData
    lateinit var textViewBSMake: TextView
    lateinit var textViewBSModelVariant: TextView
    lateinit var textViewBSVehicleDetails: TextView
    lateinit var textViewBSBankName: TextView
    lateinit var tvBSLoanAmountValV2: TextView
    lateinit var tvBSROIValV2: TextView
    lateinit var tvBSEMIValV2: TextView
    lateinit var tvBSTenureValV2: TextView
    lateinit var textViewBSTermsAndCondition: TextView
    lateinit var cbBSTermsAndConditions: CheckBox
    lateinit var buttonDocumentSubmitWithOTP: Button
    lateinit var loanProcessCompletedData:CustomLoanProcessCompletedData

    lateinit var fragmentContext: Context
    var mobileNum = ""
    var salutation = ""
    var name = ""
    var caseId = ""
    var bankId = ""
    var currentOTP = ""
    var bankTermsURL = ""
    var privacyPolicyURL = ""

    lateinit var timer: CountDownTimer
    lateinit var fragmentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val safeArgs = SelectedBankOfferStatusFragmentArgs.fromBundle(it)
            customerID = safeArgs.customerID
        }

        customerViewModel = ViewModelProvider(this).get(
                TransactionViewModel::class.java
        )
        customerViewModel.getCustomerDetailsLiveData().observe(this) { mApiResponse: ApiResponse? ->
            onCustomerResponse(
                    mApiResponse!!
            )
        }

        bankTAndCViewModel = ViewModelProvider(this).get(
                BankOffersViewModel::class.java
        )

        bankTAndCViewModel.getBankTermsAndConditionLiveData().observe(this) { mApiResponse: ApiResponse? ->
            onTermsAndConditionsResponse(
                    mApiResponse!!
            )
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
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.v2_fragment_selected_bank_offer_status, container, false)
        fragmentContext = view.context
        fragmentView = view
        textViewBSMake = fragmentView.findViewById(R.id.textViewBSMake)
        textViewBSModelVariant = fragmentView.findViewById(R.id.textViewBSModelVariant)
        textViewBSVehicleDetails = fragmentView.findViewById(R.id.textViewBSVehicleDetails)
        textViewBSBankName = fragmentView.findViewById(R.id.textViewBSBankName)
        tvBSLoanAmountValV2 = fragmentView.findViewById(R.id.tvBSLoanAmountValV2)
        tvBSROIValV2 = fragmentView.findViewById(R.id.tvBSROIValV2)
        tvBSEMIValV2 = fragmentView.findViewById(R.id.tvBSEMIValV2)
        tvBSTenureValV2 = fragmentView.findViewById(R.id.tvBSTenureValV2)
        textViewBSTermsAndCondition = fragmentView.findViewById(R.id.textViewBSTermsAndCondition)
        cbBSTermsAndConditions = fragmentView.findViewById(R.id.cbBSTermsAndConditions)
        buttonDocumentSubmitWithOTP = fragmentView.findViewById(R.id.buttonDocumentSubmitWithOTP)
        customerViewModel.getCustomerDetails(
                getCustomerDetailsRequest(),
                Global.customerAPI_BaseURL + CommonStrings.CUSTOMER_DETAILS_END_URL
        )

        return view
    }

    private fun getCustomerDetailsRequest(): CustomerRequest {
        var customerDetailsRequest = CustomerRequest()
        customerDetailsRequest.UserId = CommonStrings.DEALER_ID
        customerDetailsRequest.UserType = CommonStrings.USER_TYPE
        var customerJourneyDataRequest = ResetCustomerJourneyDataRequest();
        customerJourneyDataRequest.CustomerId = customerID
        customerDetailsRequest.Data = customerJourneyDataRequest
        return customerDetailsRequest
    }

    private fun onCustomerResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                val customerResponse: CustomerDetailsResponse? =
                        mApiResponse.data as CustomerDetailsResponse?
                customerDetailsResponse = customerResponse?.data!!
                bankTAndCViewModel.getBankTermsAndConditionData("https://15.207.148.230:3004/api/Bank/" + CommonStrings.BANKTC_END_POINT + customerID)
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

    private fun onTermsAndConditionsResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val tAndCResponse: BankTAndCResponse? =
                        mApiResponse.data as BankTAndCResponse?
                val tAndCData = tAndCResponse?.data
                bankTermsURL = tAndCData?.termsAndCondition.toString()
                privacyPolicyURL = tAndCData?.privacyPolicy.toString()
                initView()
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


    private fun initView() {


        val make = customerDetailsResponse.vehicleDetails?.make
        val model = customerDetailsResponse.vehicleDetails?.model
        val variant = customerDetailsResponse.vehicleDetails?.variant
        val ownership = customerDetailsResponse.vehicleDetails?.ownership.toString()
        val vehPrice = customerDetailsResponse.vehicleDetails?.vehicleSellingPrice?.toInt()
        val vehicleSellingPrice = getString(R.string.rupees_symbol) + " " + formatAmount(vehPrice.toString())
        val kms = customerDetailsResponse.vehicleDetails?.kMs
        val fuelType = customerDetailsResponse.vehicleDetails?.fuelType
        val regYear = customerDetailsResponse.vehicleDetails?.registrationYear
        val separator = " | "
        textViewBSMake.text = make
        textViewBSModelVariant.text = "$model $variant"


        textViewBSVehicleDetails.text = formatOwner(ownership) + separator + vehicleSellingPrice + separator + kms + vehicleSellingPrice + fuelType + separator + regYear

        textViewBSBankName.text = customerDetailsResponse.loanDetails?.bankName
        tvBSLoanAmountValV2.text = customerDetailsResponse.loanDetails?.loanAmount
        tvBSROIValV2.text = customerDetailsResponse.loanDetails?.roi
        tvBSEMIValV2.text = customerDetailsResponse.loanDetails?.emi
        tvBSTenureValV2.text = customerDetailsResponse.loanDetails?.tenure

        val ss = SpannableString(getString(R.string.vtwo_t_and_c_hint))
        val span1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                openWebViewActivity("Terms And Conditions", bankTermsURL)
            }
        }

        val span2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                openWebViewActivity("Privacy Policy", privacyPolicyURL)
            }
        }
        ss.setSpan(span1, 15, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(span2, 40, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textViewBSTermsAndCondition.text = ss
        textViewBSTermsAndCondition.movementMethod = LinkMovementMethod.getInstance()

        cbBSTermsAndConditions.setOnClickListener(View.OnClickListener {
            cbBSTermsAndConditions.isChecked = true
        })

        buttonDocumentSubmitWithOTP.setOnClickListener(View.OnClickListener {
            if (cbBSTermsAndConditions.isChecked) {
                getFinalOTPRequest()?.let { it1 -> transactionViewModel.generateFinalOTP(it1, Global.customerAPI_BaseURL + "generate-final-submit-otp") }
            } else {
                showToast("Please check Terms and Condition and Privacy Policy")
            }
        })

        name = customerDetailsResponse.basicDetails?.firstName + " " + customerDetailsResponse.basicDetails?.lastName
        salutation = customerDetailsResponse.basicDetails?.salutation.toString()
        mobileNum = customerDetailsResponse.basicDetails?.customerMobile.toString()
        caseId = customerDetailsResponse.caseId.toString()

    }

    private fun getFinalOTPRequest(): GenerateFinalOTPRequest? {
        val finalOTPData = customerID?.toInt()?.let { FinalOTPData(it, true) }
        val generateFinalOTPRequest = finalOTPData?.let { GenerateFinalOTPRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, it) }
        return generateFinalOTPRequest
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
                    generateOTPDialog()
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



    private fun generateOTPDialog() {
        val dialog = Dialog(fragmentContext, R.style.FullScreenDialogTheme)
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.v2_final_otp_layout)

        val textViewMobileNumber: TextView = dialog.findViewById(R.id.textViewMobileNumber)

        val etFinalOTPV2: EditText = dialog.findViewById(R.id.etFinalOTPV2)
        val tvFinalOTPTimerV2: TextView = dialog.findViewById(R.id.tvFinalOTPTimerV2)
        val tvFinalResendOTPV2: TextView = dialog.findViewById(R.id.tvFinalResendOTPV2)
        val buttonSubmitFinalOTP: Button = dialog.findViewById(R.id.buttonSubmitFinalOTP)
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
            getFinalOTPRequest()?.let { it1 -> transactionViewModel.generateFinalOTP(it1, Global.customerAPI_BaseURL + "generate-final-submit-otp") }

        })

        buttonSubmitFinalOTP.setOnClickListener(View.OnClickListener {
            if (etFinalOTPV2.text.toString().isNotEmpty()) {
                currentOTP=etFinalOTPV2.text.toString()
                getValidateFinalOTP()?.let { it1 -> transactionViewModel.validateFinalOTP(it1, Global.customerAPI_BaseURL + "validate-final-submit-otp") }

            } else {
                showToast("Enter OTP!")
            }
        })

        dialog.show()
    }

    private fun getValidateFinalOTP(): ValidateFinalOTPRequest? {
        return customerID?.let { ValidateOTPData(it.toInt(), currentOTP) }?.let { ValidateFinalOTPRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, it) }
    }

    private fun onValidateOTPResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val validateFinalOTPRes: ValidateFinalOTPResponse? =
                        mApiResponse.data as ValidateFinalOTPResponse?
                if (validateFinalOTPRes?.data != null) {
                    bankId = validateFinalOTPRes.data
                    loanProcessCompletedData.customerName=salutation+" "+name
                    loanProcessCompletedData.bankApplicationID=bankId
                    loanProcessCompletedData.caseID=caseId
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