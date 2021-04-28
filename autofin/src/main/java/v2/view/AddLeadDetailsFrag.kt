package v2.view

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mfc.autofin.framework.R
import kotlinx.android.synthetic.main.v2_reg_name_email_layout.*
import kotlinx.android.synthetic.main.vtwo_mobile_num_layout.*
import okhttp3.internal.userAgent
import utility.CommonStrings
import utility.Global
import v2.model.dto.AddLeadRequest
import v2.model.dto.DataSelectionDTO
import v2.model.request.*
import v2.model.request.add_lead.BasicDetails
import v2.model.response.*
import v2.model.response.master.MasterResponse
import v2.model.response.master.Types
import v2.model_view.MasterViewModel
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.DataRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.itemClickCallBack
import v2.view.utility_view.GridItemDecoration
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


public class AddLeadDetailsFrag : BaseFragment(), View.OnClickListener {


    lateinit var validateLeadDataRes: ValidateLeadDataResponse
    lateinit var etMobileNumberV2: EditText
    lateinit var etOTPV2: EditText
    lateinit var cbTermsAndConditions: CheckBox
    lateinit var tvResendOTPV2: TextView
    lateinit var tvOTPTimerV2: TextView
    lateinit var btnMobileNum: Button
    lateinit var ll_otp_v2: LinearLayout
    lateinit var transactionViewModel: TransactionViewModel
    lateinit var addLeadRequest: AddLeadRequest
    var basicDetails = BasicDetails()
    lateinit var rv_salutation: RecyclerView
    lateinit var masterViewModel: MasterViewModel
    lateinit var salutationAdapter: DataRecyclerViewAdapter
    lateinit var llNameAndEmailV2: LinearLayout
    lateinit var etFirstName: EditText

    var make: String = ""
    var model: String = ""
    var variant: String = ""
    var mobileNum: String = ""
    var userId: String = ""
    var userType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        masterViewModel = ViewModelProvider(this).get(
                MasterViewModel::class.java
        )

        transactionViewModel = ViewModelProvider(requireActivity()).get(
                TransactionViewModel::class.java
        )

        masterViewModel.getSalutationsLiveData()
                .observe(this, { mApiResponse: ApiResponse? ->
                    onSalutationRes(
                            mApiResponse!!
                    )
                })

        transactionViewModel.getGenerateOTPLiveData()
                .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                    onGenerateOTP(
                            mApiResponse!!
                    )
                })

        transactionViewModel.getValidateOTPLiveData()
                .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                    onValidateOTP(
                            mApiResponse!!
                    )
                })

        transactionViewModel.getValidateLeadLiveData().observe(requireActivity(), { mApiResponse: ApiResponse? ->
            onValidateLead(
                    mApiResponse!!
            )
        })

        transactionViewModel.getResetCustomerJourneyLiveData().observe(requireActivity(),{mAPIResponse:ApiResponse?->onResetJourney(mAPIResponse!!)})

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_lead_details, container, false)
        arguments?.let {
            val safeArgs = AddLeadDetailsFragArgs.fromBundle(it)
            addLeadRequest = safeArgs.addLeadRequestDetails
        }
        initViews(view)

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

        rv_salutation = view.findViewById(R.id.rv_salutation)
        llNameAndEmailV2 = view.findViewById(R.id.llNameAndEmailV2)
        etFirstName = view.findViewById(R.id.et_first_name)


        make = addLeadRequest.Data?.vehicleDetails?.Make.toString()
        model = addLeadRequest.Data?.vehicleDetails?.Model.toString()
        variant = addLeadRequest.Data?.vehicleDetails?.Variant.toString()
        userId = addLeadRequest.UserId.toString()
        userType = addLeadRequest.UserType.toString()

        tvResendOTPV2.setOnClickListener(this)
        btnMobileNum.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnMobileNum -> {
                when {
                    ll_otp_v2.visibility == View.GONE && llNameAndEmailV2.visibility == View.GONE -> {
                        sendOTP()
                    }
                    ll_otp_v2.visibility == View.VISIBLE && llNameAndEmailV2.visibility == View.GONE -> {
                        validateOTP()
                    }
                    llNameAndEmailV2.visibility == View.VISIBLE -> {
                        addLead()
                    }

                }
            }
            R.id.tvResendOTPV2 -> {
                if (etOTPV2.text.isNotEmpty())
                    etOTPV2.text.clear()

                sendOTP()
            }

        }

    }

    private fun sendOTP() {
        if (etMobileNumberV2.text.length == 10) {
            transactionViewModel!!.generateOTP(getOtpRequest(null, etMobileNumberV2.text.toString()), Global.customerAPI_BaseURL + CommonStrings.OTP_URL_END)

        } else {
            showToast("Please enter Valid Mobile Number")
        }
    }

    // API call region starts

    private fun validateOTP() {
        if (etOTPV2.text.length == 6) {
            transactionViewModel!!.validateOTP(getOtpRequest(etOTPV2.text.toString(), etMobileNumberV2.text.toString()), Global.customerAPI_BaseURL + CommonStrings.VALIDATE_OTP_URL_END)


        } else {
            showToast("Please enter valid OTP")
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


    private fun addLead() {
        if (etFirstName.text.isNotEmpty() && et_last_name.text.isNotEmpty() && et_email.text.isNotEmpty()) {
            basicDetails.FirstName = etFirstName.text.toString()
            basicDetails.LastName = et_last_name.text.toString()
            val email = et_email.text.toString()
            if (isEmailValid(email)) {
                basicDetails.Email = et_email.text.toString()
                addLeadRequest.Data?.basicDetails = basicDetails
                transactionViewModel.addLead(addLeadRequest, Global.customerAPI_BaseURL + CommonStrings.ADD_LEAD_URL_END)
                transactionViewModel.getAddLeadLiveData().observe(requireActivity(), { mApiResponse: ApiResponse? ->
                    onAddLead(
                            mApiResponse!!
                    )
                })

            } else
                showToast("Please enter valid Email Id")
        } else
            showToast("Please enter all fields to proceed further!")
    }

    // API call region ends

    // OnResponse Region starts

    private fun onGenerateOTP(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val otpResponse: OTPResponse? = mApiResponse.data as OTPResponse?
                if (otpResponse?.status!!)
                    ll_otp_v2.visibility = View.VISIBLE
                mobileNum = etMobileNumberV2.text.toString()

                showToast(otpResponse.message.toString())

            }
            ApiResponse.Status.ERROR -> {
                showToast(mApiResponse.error?.message.toString())
            }
            else -> {
                showToast("Please enter correct value")
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
                    basicDetails.CustomerMobile = etMobileNumberV2.text.toString()
                    displayNameLayout()
                } else {
                    Toast.makeText(activity, "invalid Validate", Toast.LENGTH_LONG).show()
                }
            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }


    private fun onSalutationRes(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val masterResponse: MasterResponse? = mApiResponse.data as MasterResponse?
                setSalutation(masterResponse!!.data.types)

            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

    private fun onAddLead(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val addLeadResponse: AddLeadResponse? = mApiResponse.data as AddLeadResponse?
                caseId = addLeadResponse?.mData.toString()
                checkValidLead()
                showToast(addLeadResponse?.message.toString())

            }
            ApiResponse.Status.ERROR -> {

            }
            else -> {
                showToast("Please enter valid details")
            }
        }

    }

    private fun checkValidLead() {

        transactionViewModel.validateLead(getValidateLeadReq(), Global.customerAPI_BaseURL + CommonStrings.VALIDATE_LEAD)

    }

    private fun onValidateLead(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val validateLeadResponse: ValidateLeadResponse? = mApiResponse.data as ValidateLeadResponse?
                val validateLeadDataResponse: ValidateLeadDataResponse? = validateLeadResponse?.data
                if (!validateLeadDataResponse?.message.equals("Success")) {
                    validateLeadDataRes = validateLeadDataResponse!!
                    generateAlertDialog()
                }
                showToast(validateLeadDataResponse?.message.toString())

            }
            ApiResponse.Status.ERROR -> {

            }
            else -> {
                showToast("Please enter valid details")
            }
        }

    }

    private fun onResetJourney(mAPIResponse: ApiResponse) {
        when (mAPIResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val resetJourneyRes: ResetCustomerJourneyResponse? = mAPIResponse.data as ResetCustomerJourneyResponse?
                showToast(resetJourneyRes?.message.toString())

            }
            ApiResponse.Status.ERROR -> {

            }
            else -> {
                showToast("Please enter valid details")
            }
        }

    }


    private fun generateAlertDialog() {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.vtwo_layout_custom_dialog)
        val ivCloseDialog = dialog.findViewById(R.id.ivCloseDialogV2) as ImageView
        val tvAlertDialogText = dialog.findViewById(R.id.tvAlertDialogText) as TextView
        tvAlertDialogText.setText(validateLeadDataRes.message)
        val btnNewFlow = dialog.findViewById(R.id.btnStartNewFlowV2) as Button
        val btnContinueWithOldFlow = dialog.findViewById(R.id.btnExistingFlowV2) as Button

        ivCloseDialog.setOnClickListener {
            dialog.dismiss()
        }

        btnNewFlow.setOnClickListener { dialog.dismiss() }
        btnContinueWithOldFlow.setOnClickListener { resetJourney() }

        dialog.show()

    }

    private fun resetJourney() {
        transactionViewModel.resetCustomerJourney(getCustomerRequest(), Global.customerAPI_BaseURL + CommonStrings.RESET_JOURNEY)
    }

    private fun getCustomerRequest(): CustomerRequest {
        val resetJourney = ResetCustomerJourneyDataRequest(validateLeadDataRes.oldCustomerId)
        return CustomerRequest(resetJourney, userId, userType)
    }

    private fun getValidateLeadReq(): ValidateLeadRequest {
        val validateLeadDataRequest = ValidateLeadDataRequest(mobileNum, make, model, variant)
        return ValidateLeadRequest(validateLeadDataRequest, userId, userType)
        /*val validateLeadDataRequest= ValidateLeadDataRequest(etMobileNumberV2.text.toString(),addLeadRequest.Data?.vehicleDetails?.Make,addLeadRequest.Data?.vehicleDetails?.Model,addLeadRequest.Data?.vehicleDetails?.Variant)
        return ValidateLeadRequest(validateLeadDataRequest, addLeadRequest.UserId, addLeadRequest.UserType)*/
    }


    // OnResponse region ends

    private fun displayNameLayout() {
        ll_otp_v2.visibility = View.GONE
        tv_mobile_num_hint.visibility = View.GONE

        tv_mobile_num_verified.visibility = View.VISIBLE

        llNameAndEmailV2.visibility = View.VISIBLE

        masterViewModel!!.getSalutations(Global.customerDetails_BaseURL + CommonStrings.SALUTATION_END_POINT)

    }


    private fun setSalutation(types: List<Types>) {
        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        types.forEachIndexed { index, types ->
            list.add(DataSelectionDTO(types.displayLabel, null, types.value, false))
        }



        salutationAdapter = DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
            override fun itemClick(item: Any?, position: Int) {


                salutationAdapter.dataListFilter!!.forEachIndexed { index, item ->
                    run {
                        if (index == position) {
                            item.selected = true
                            basicDetails.Salutation = item.value
                        } else {
                            item.selected = false
                        }
                    }
                }
                salutationAdapter.notifyDataSetChanged()
            }
        })


        val layoutManagerStaggeredGridLayoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 4)

        rv_salutation.addItemDecoration(GridItemDecoration(25, 4))

        rv_salutation.setLayoutManager(layoutManagerStaggeredGridLayoutManager)

        rv_salutation.setAdapter(salutationAdapter)
    }

    // Validation region starts

    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    // Validation region ends
}