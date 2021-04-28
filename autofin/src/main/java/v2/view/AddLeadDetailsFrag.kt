package v2.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import v2.model.request.OTPRequest
import v2.model.request.OTPRequestData
import v2.model.request.add_lead.BasicDetails
import v2.model.response.AddLeadResponse
import v2.model.response.OTPResponse
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

        rv_salutation = view.findViewById(R.id.rv_salutation)
        llNameAndEmailV2 = view.findViewById(R.id.llNameAndEmailV2)
        etFirstName = view.findViewById(R.id.et_first_name)

        masterViewModel = ViewModelProvider(this).get(
                MasterViewModel::class.java
        )

        tvResendOTPV2.setOnClickListener(this)
        btnMobileNum.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnMobileNum -> {
                when {
                    ll_otp_v2.visibility == View.GONE && llNameAndEmailV2.visibility==View.GONE -> {
                        sendOTP()
                    }
                    ll_otp_v2.visibility == View.VISIBLE && llNameAndEmailV2.visibility==View.GONE -> {
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

    // API call region starts

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
                if(otpResponse?.status!!)
                ll_otp_v2.visibility = View.VISIBLE

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
                if(addLeadResponse?.statusCode.equals("200"))
                {
                    caseId = addLeadResponse?.mData.toString()
                    showToast(addLeadResponse!!.message.toString())
                }
               showToast(addLeadResponse?.message.toString())

            }
            ApiResponse.Status.ERROR -> {

            }
            else -> {
                showToast("Please enter valid details")
            }
        }

    }

    // OnResponse region ends

    private fun displayNameLayout() {
        ll_otp_v2.visibility=View.GONE
        tv_mobile_num_hint.visibility=View.GONE

        tv_mobile_num_verified.visibility=View.VISIBLE

        llNameAndEmailV2.visibility = View.VISIBLE

        masterViewModel!!.getSalutations(Global.customerDetails_BaseURL + CommonStrings.SALUTATION_END_POINT)
        masterViewModel!!.getSalutationsLiveData()
                .observe(this, { mApiResponse: ApiResponse? ->
                    onSalutationRes(
                            mApiResponse!!
                    )
                })
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

    private fun validName(name: String): Boolean {
        val expression = "[a-zA-Z]"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(name)
        return matcher.matches()
    }


    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    // Validation region ends
}