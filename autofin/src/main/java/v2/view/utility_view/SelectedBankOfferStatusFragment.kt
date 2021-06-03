package v2.view.utility_view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
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
import utility.CommonStrings
import utility.Global
import v2.model.request.CustomerRequest
import v2.model.request.ResetCustomerJourneyDataRequest
import v2.model.response.CustomerDetailsData
import v2.model.response.CustomerDetailsResponse
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.DocumentUploadFragmentArgs
import v2.view.base.BaseFragment

class SelectedBankOfferStatusFragment : BaseFragment() {
    private var customerID: String? = ""
    private var param2: String? = null
    private lateinit var customerViewModel:TransactionViewModel
    lateinit var customerDetailsResponse: CustomerDetailsData
    lateinit var textViewBSMake:TextView
    lateinit var textViewBSModelVariant:TextView
    lateinit var textViewBSVehicleDetails:TextView
    lateinit var textViewBSBankName:TextView
    lateinit var tvBSLoanAmountValV2:TextView
    lateinit var tvBSROIValV2:TextView
    lateinit var tvBSEMIValV2:TextView
    lateinit var tvBSTenureValV2:TextView
    lateinit var textViewBSTermsAndCondition:TextView
    lateinit var cbBSTermsAndConditions: CheckBox
    lateinit var buttonDocumentSubmitWithOTP: Button

    lateinit var fragmentContext:Context
    var mobileNum=""
    var salutation=""
    var name=""
    var caseId=""
    var bankId=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val safeArgs = SelectedBankOfferStatusFragmentArgs.fromBundle(it)
            customerID=safeArgs.customerID
        }

        customerViewModel=ViewModelProvider(this).get(
                TransactionViewModel::class.java
        )
        customerViewModel.getCustomerDetailsLiveData().observe(this) { mApiResponse: ApiResponse? ->
            onCustomerResponse(
                    mApiResponse!!
            )
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view:View= inflater.inflate(R.layout.v2_fragment_selected_bank_offer_status, container, false)
        fragmentContext=view.context
        customerViewModel.getCustomerDetails(
                getCustomerDetailsRequest(),
                Global.customerAPI_BaseURL + CommonStrings.CUSTOMER_DETAILS_END_URL
        )
        initView(view)
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

    private fun initView(view: View) {
        textViewBSMake=view.findViewById(R.id.textViewBSMake)
        textViewBSModelVariant=view.findViewById(R.id.textViewBSModelVariant)
        textViewBSVehicleDetails=view.findViewById(R.id.textViewBSVehicleDetails)
        textViewBSBankName=view.findViewById(R.id.textViewBSBankName)
        tvBSLoanAmountValV2=view.findViewById(R.id.tvBSLoanAmountValV2)
        tvBSROIValV2=view.findViewById(R.id.tvBSROIValV2)
        tvBSEMIValV2=view.findViewById(R.id.tvBSEMIValV2)
        tvBSTenureValV2=view.findViewById(R.id.tvBSTenureValV2)
        textViewBSTermsAndCondition=view.findViewById(R.id.textViewBSTermsAndCondition)
        cbBSTermsAndConditions=view.findViewById(R.id.cbBSTermsAndConditions)
        buttonDocumentSubmitWithOTP=view.findViewById(R.id.buttonDocumentSubmitWithOTP)

        if(customerDetailsResponse!=null)
        {
            textViewBSMake.text=customerDetailsResponse?.vehicleDetails?.make
            textViewBSModelVariant.text=customerDetailsResponse?.vehicleDetails?.model+customerDetailsResponse?.vehicleDetails?.variant
            textViewBSVehicleDetails.text=customerDetailsResponse?.vehicleDetails?.ownership.toString()+" | "+customerDetailsResponse?.vehicleDetails?.vehicleSellingPrice.toString()+" | "+
                    customerDetailsResponse?.vehicleDetails?.kMs+" | "+customerDetailsResponse?.vehicleDetails?.fuelType+" | "+customerDetailsResponse?.vehicleDetails?.registrationYear

            textViewBSBankName.text=customerDetailsResponse?.loanDetails?.bankName
            tvBSLoanAmountValV2.text=customerDetailsResponse?.loanDetails?.loanAmount
            tvBSROIValV2.text=customerDetailsResponse?.loanDetails?.roi
            tvBSEMIValV2.text=customerDetailsResponse?.loanDetails?.emi
            tvBSTenureValV2.text=customerDetailsResponse?.loanDetails?.tenure

        }

        buttonDocumentSubmitWithOTP.setOnClickListener(View.OnClickListener {
            generateOTPDialog()
        })


    }

    private fun generateOTPDialog() {
        val dialog = Dialog(fragmentContext, R.style.FullScreenDialogTheme)
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.v2_final_otp_layout)

        val textViewMobileNumber:TextView=dialog.findViewById(R.id.textViewMobileNumber)
        val etFinalOTPV2:EditText=dialog.findViewById(R.id.etFinalOTPV2)
        val tvFinalOTPTimerV2:TextView=dialog.findViewById(R.id.tvFinalOTPTimerV2)
        val tvFinalResendOTPV2:TextView=dialog.findViewById(R.id.tvFinalResendOTPV2)
        val buttonSubmitFinalOTP:Button=dialog.findViewById(R.id.buttonSubmitFinalOTP)
        textViewMobileNumber.text=mobileNum


    }

    private fun onCustomerResponse(mApiResponse: ApiResponse)
    {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                val customerResponse: CustomerDetailsResponse? =
                        mApiResponse.data as CustomerDetailsResponse?
                customerDetailsResponse = customerResponse?.data!!

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