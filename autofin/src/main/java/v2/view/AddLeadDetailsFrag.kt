package v2.view

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
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


    private var onClickNext: Boolean=false
    lateinit var validateLeadDataRes: ValidateLeadDataResponse
    lateinit var etMobileNumberV2: EditText
    lateinit var ivBackToVehDetails: ImageView
    lateinit var etOTPV2: EditText
    lateinit var llOTPNumInput: LinearLayout
    lateinit var cbTermsAndConditions: CheckBox
    lateinit var tvResendOTPV2: TextView
    lateinit var tvOTPTimerV2: TextView
    lateinit var tvOTPEHint: TextView
    lateinit var btnMobileNum: Button
    lateinit var ll_otp_v2: LinearLayout
    lateinit var llTAndC: LinearLayout
    lateinit var transactionViewModel: TransactionViewModel
    lateinit var addLeadRequest: AddLeadRequest
    var basicDetails = BasicDetails()
    lateinit var rv_salutation: RecyclerView
    lateinit var masterViewModel: MasterViewModel
    lateinit var salutationAdapter: DataRecyclerViewAdapter
    lateinit var llNameAndEmailV2: LinearLayout
    lateinit var etFirstName: EditText
    lateinit var dialog: Dialog
    lateinit var timer: CountDownTimer

    var make: String = ""
    var model: String = ""
    var variant: String = ""
    var mobileNum: String = ""
    var userId: String = ""
    var userType: String = ""
    var salutation: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        masterViewModel = ViewModelProvider(this).get(
                MasterViewModel::class.java
        )

        transactionViewModel = ViewModelProvider(this).get(
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

        transactionViewModel.getAddLeadLiveData().observe(requireActivity(), { mApiResponse: ApiResponse? ->
            onAddLead(
                    mApiResponse!!
            )
        })


        transactionViewModel.getValidateLeadLiveData().observe(requireActivity(), { mApiResponse: ApiResponse? ->
            onValidateLead(
                    mApiResponse!!
            )
        })

        transactionViewModel.getResetCustomerJourneyLiveData().observe(requireActivity(), { mAPIResponse: ApiResponse? -> onResetJourney(mAPIResponse!!) })

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
        ivBackToVehDetails = view?.findViewById(R.id.ivBackToVehDetails)!!
        etMobileNumberV2 = view?.findViewById(R.id.etMobileNumberV2)!!

        etOTPV2 = view.findViewById(R.id.etOTPV2)!!
        llOTPNumInput = view.findViewById(R.id.llOTPNumInput)!!
        cbTermsAndConditions = view.findViewById(R.id.cbTermsAndConditions)
        tvResendOTPV2 = view.findViewById(R.id.tvResendOTPV2)
        tvOTPTimerV2 = view.findViewById(R.id.tvOTPTimerV2)
        tvOTPEHint = view.findViewById(R.id.tvOTPEHint)

        btnMobileNum = view.findViewById(R.id.btnMobileNum)

        ll_otp_v2 = view.findViewById(R.id.ll_otp_v2)
        ll_otp_v2.visibility = View.GONE
        llTAndC = view.findViewById(R.id.llTAndC)

        rv_salutation = view.findViewById(R.id.rv_salutation)
        llNameAndEmailV2 = view.findViewById(R.id.llNameAndEmailV2)
        etFirstName = view.findViewById(R.id.et_first_name)


        make = addLeadRequest.Data?.vehicleDetails?.Make.toString()
        model = addLeadRequest.Data?.vehicleDetails?.Model.toString()
        variant = addLeadRequest.Data?.vehicleDetails?.Variant.toString()

        tvResendOTPV2.setOnClickListener(this)
        btnMobileNum.setOnClickListener(this)
        ivBackToVehDetails.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.ivBackToVehDetails -> {
                activity?.onBackPressed()
            }
            R.id.btnMobileNum -> {
                when {
                    ll_otp_v2.visibility == View.GONE && llNameAndEmailV2.visibility == View.GONE -> {
                        sendOTP()
                    }
                    ll_otp_v2.visibility == View.VISIBLE && llNameAndEmailV2.visibility == View.GONE -> {

                        if (cbTermsAndConditions.isChecked) {
                            onClickNext=true
                            timer.onFinish()
                            validateOTP()
                        } else
                            showToast("Please check Terms and Condition")
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

            etMobileNumberV2.setTextColor(resources.getColor(R.color.black))
            tv_mobile_num_hint.setTextColor(resources.getColor(R.color.vtwo_light_grey))
            tv_mobile_num_hint.visibility = View.GONE
            llMobileNumInput.setBackgroundResource(R.drawable.vtwo_input_bg)
        } else {
            if (etMobileNumberV2.text.isEmpty()) {
                tv_mobile_num_hint.setText("Please enter Mobile number")
            } else {
                etMobileNumberV2.setTextColor(resources.getColor(R.color.error_red))
                tv_mobile_num_hint.setText("Please enter Valid mobile number")
            }
            tv_mobile_num_hint.setTextColor(resources.getColor(R.color.error_red))
            llMobileNumInput.setBackgroundResource(R.drawable.v2_error_layout_bg)


        }
    }

    // API call region starts

    private fun validateOTP() {
        if (etOTPV2.text.length == 6) {
            showProgressDialog(requireContext())
            transactionViewModel!!.validateOTP(getOtpRequest(etOTPV2.text.toString(), etMobileNumberV2.text.toString()), Global.customerAPI_BaseURL + CommonStrings.VALIDATE_OTP_URL_END)
            tvOTPEHint.visibility = View.GONE
            llOTPNumInput.setBackgroundResource(R.drawable.vtwo_input_bg)
        } else {
            tvOTPEHint.visibility = View.VISIBLE
            llOTPNumInput.setBackgroundResource(R.drawable.v2_error_layout_bg)
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
        if (salutation.isNotEmpty() && etFirstName.text.isNotEmpty() && et_last_name.text.isNotEmpty() && et_email.text.isNotEmpty()) {

            ll_first_name_input.setBackgroundResource(R.drawable.vtwo_input_bg)
            tv_fname_hint.visibility = View.GONE

            ll_last_name_input.setBackgroundResource(R.drawable.vtwo_input_bg)
            tv_lname_hint.visibility = View.GONE


            basicDetails.FirstName = etFirstName.text.toString()
            basicDetails.LastName = et_last_name.text.toString()
            val email = et_email.text.toString()
            if (isEmailValid(email)) {
                ll_last_email_input.setBackgroundResource(R.drawable.vtwo_input_bg)
                tv_email_hint.visibility = View.GONE
                basicDetails.Email = et_email.text.toString()
                addLeadRequest.Data?.basicDetails = basicDetails
                addLeadRequest.UserType = CommonStrings.USER_TYPE
                addLeadRequest.UserId = CommonStrings.DEALER_ID
                showProgressDialog(requireContext())

                transactionViewModel.addLead(addLeadRequest, Global.customerAPI_BaseURL + CommonStrings.ADD_LEAD_URL_END)

            } else {
                ll_last_email_input.setBackgroundResource(R.drawable.v2_error_layout_bg)
                et_email.setTextColor(resources.getColor(R.color.error_red))
                tv_email_hint.visibility = View.VISIBLE
                tv_email_hint.setText("Please enter Valid Email ID")
            }
        } else {
            if (etFirstName.text.isEmpty()) {
                ll_first_name_input.setBackgroundResource(R.drawable.v2_error_layout_bg)
                tv_fname_hint.visibility = View.VISIBLE
            }
            if (et_last_name.text.isEmpty()) {
                ll_last_name_input.setBackgroundResource(R.drawable.v2_error_layout_bg)
                tv_lname_hint.visibility = View.VISIBLE

            }
            if (et_email.text.isEmpty()) {
                ll_last_email_input.setBackgroundResource(R.drawable.v2_error_layout_bg)
                tv_email_hint.visibility = View.VISIBLE
                tv_email_hint.setText("Please enter Email ID")

            }
            if (salutation.isEmpty()) {
                showToast("Please select Salutation")
            }
        }
    }

    // API call region ends

    // OnResponse Region starts

    private fun onGenerateOTP(mApiResponse: ApiResponse) = when (mApiResponse.status) {
        ApiResponse.Status.LOADING -> {
        }
        ApiResponse.Status.SUCCESS -> {
            try {
                val otpResponse: OTPResponse? = mApiResponse.data as OTPResponse?
                if (otpResponse?.status!!) {
                    ll_otp_v2.visibility = View.VISIBLE
                    llTAndC.visibility = View.VISIBLE
                    mobileNum = etMobileNumberV2.text.toString()
                }

                enableTimer()
                // showToast(otpResponse.message.toString())
            } catch (e: Exception) {

            }
        }
        ApiResponse.Status.ERROR -> {
            showToast(mApiResponse.error?.message.toString())
        }
        else -> {
            showToast("Please enter correct value")
        }
    }

    private fun enableTimer() {
       timer= object : CountDownTimer(120000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                tvOTPTimerV2.text = "" + millisUntilFinished / 1000 + " Sec"
                onClickNext=false
            }

            override fun onFinish() {
                tvOTPTimerV2.setText("0 Sec")
                timer.cancel()
                if(!onClickNext)
                {
                    showToast("Your OTP got expired, Please click on Resend OTP to get the new one.")
                }
            }
        }.start()

    }

    private fun onValidateOTP(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val otpResponse: OTPResponse? = mApiResponse.data as OTPResponse?
                if (otpResponse?.status!!) {
                    // Toast.makeText(activity, "OTP Validate", Toast.LENGTH_LONG).show()
                    basicDetails.CustomerMobile = etMobileNumberV2.text.toString()
                    displayNameLayout()
                    checkValidLead()
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
                hideProgressDialog()
                val addLeadResponse: AddLeadResponse? = mApiResponse.data as AddLeadResponse?
                caseId = addLeadResponse?.mData.toString()

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
                hideProgressDialog()
                val validateLeadResponse: ValidateLeadResponse? = mApiResponse.data as ValidateLeadResponse?
                val validateLeadDataResponse: ValidateLeadDataResponse? = validateLeadResponse?.data
                if (!validateLeadDataResponse?.message.equals("Success"))
                {
                    validateLeadDataRes = validateLeadDataResponse!!
                    generateAlertDialog()
                }

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
                hideProgressDialog()
                val resetJourneyRes: ResetCustomerJourneyResponse? = mAPIResponse.data as ResetCustomerJourneyResponse?

                if (dialog.isShowing)
                    dialog.dismiss()

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

        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.vtwo_layout_custom_dialog)
        val ivCloseDialog = dialog.findViewById(R.id.ivCloseDialogV2) as ImageView
        val tvAlertDialogText = dialog.findViewById(R.id.tvAlertDialogText) as TextView
        tvAlertDialogText.setText(validateLeadDataRes.message)
        val btnNewFlow = dialog.findViewById(R.id.btnStartNewFlowV2) as Button
        val btnContinueWithOldFlow = dialog.findViewById(R.id.btnExistingFlowV2) as Button

        ivCloseDialog.setOnClickListener {
            dialog.dismiss()
        }

        btnNewFlow.setOnClickListener {
            resetJourney()
            dialog.dismiss()
        }
        btnContinueWithOldFlow.setOnClickListener {
            showToast("Start with old lead application is in-progress.")
            dialog.dismiss()
        }
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show()

    }

    private fun resetJourney() {
        showProgressDialog(requireContext())
        transactionViewModel.resetCustomerJourney(getCustomerRequest(), Global.customerAPI_BaseURL + CommonStrings.RESET_JOURNEY)
    }

    private fun getCustomerRequest(): CustomerRequest {
        val resetJourney = ResetCustomerJourneyDataRequest(validateLeadDataRes.oldCustomerId)
        return CustomerRequest(resetJourney, CommonStrings.DEALER_ID, CommonStrings.USER_TYPE)
    }

    private fun getValidateLeadReq(): ValidateLeadRequest {
        val validateLeadDataRequest = ValidateLeadDataRequest(mobileNum, make, model, variant)
        return ValidateLeadRequest(validateLeadDataRequest, CommonStrings.DEALER_ID, CommonStrings.USER_TYPE)
        /*val validateLeadDataRequest= ValidateLeadDataRequest(etMobileNumberV2.text.toString(),addLeadRequest.Data?.vehicleDetails?.Make,addLeadRequest.Data?.vehicleDetails?.Model,addLeadRequest.Data?.vehicleDetails?.Variant)
        return ValidateLeadRequest(validateLeadDataRequest, addLeadRequest.UserId, addLeadRequest.UserType)*/
    }


    // OnResponse region ends

    private fun displayNameLayout() {
        ll_otp_v2.visibility = View.GONE
        llTAndC.visibility = View.GONE

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
                            salutation = item.value.toString()
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