package v2.view

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.dto.AddLeadRequest
import v2.model.dto.DataSelectionDTO
import v2.model.request.OTPRequest
import v2.model.request.OTPRequestData
import v2.model.response.BankListResponse
import v2.model.response.OTPResponse
import v2.model.response.master.MasterResponse
import v2.model.response.master.Types
import v2.model_view.MasterViewModel
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.DataRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.DatePickerCallBack
import v2.view.callBackInterface.itemClickCallBack
import v2.view.utility_view.GridItemDecoration
import java.util.*


public class AddLeadDetailsFrag : BaseFragment(), View.OnClickListener {


    lateinit var etMobileNumberV2: EditText
    lateinit var etOTPV2: EditText
    lateinit var etBirthDate: EditText
    lateinit var etWorkExpriance: EditText
    lateinit var tvWorkExprianceErrorMessage: TextView
    lateinit var cbTermsAndConditions: CheckBox
    lateinit var rvEmploymentType: RecyclerView
    lateinit var llBirthDate: LinearLayout
    lateinit var llWorkExpriance: LinearLayout
    lateinit var tvBirthErrorMessage: TextView

    lateinit var tvResendOTPV2: TextView
    lateinit var tvOTPTimerV2: TextView
    lateinit var btnMobileNum: Button
    lateinit var ll_otp_v2: LinearLayout
    lateinit var cbMoreThanOneYearInCurrentOrganization: CheckBox


    lateinit var llBirthDateSection: LinearLayout
    lateinit var llEmploymentSection: LinearLayout
    lateinit var llAccoutDetailsSection: LinearLayout
    lateinit var etSearchBank: EditText
    lateinit var rvBankList: RecyclerView
    lateinit var rlEditYearOfExperience: RelativeLayout

    lateinit var llNetIncomeSection: LinearLayout
    lateinit var llNetIncome: LinearLayout
    lateinit var etNetIncome: EditText
    lateinit var tvNetIncomeErrorMessage: TextView
    lateinit var tvNetIncomeInWords: TextView

    lateinit var transactionViewModel: TransactionViewModel
    lateinit var addLeadRequest: AddLeadRequest
    lateinit var masterViewModel: MasterViewModel
    lateinit var employmentDetailsAdapter: DataRecyclerViewAdapter
    lateinit var bankListDetailsAdapter: DataRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        masterViewModel = ViewModelProvider(this@AddLeadDetailsFrag).get(
                MasterViewModel::class.java
        )
        masterViewModel!!.getEmploymentTypeLiveData()
                .observe(this@AddLeadDetailsFrag, { mApiResponse: ApiResponse? ->
                    onEmploymentDetails(
                            mApiResponse!!
                    )
                })

        masterViewModel!!.getBankListLiveData()
                .observe(this@AddLeadDetailsFrag, { mApiResponse: ApiResponse? ->
                    onBankList(
                            mApiResponse!!
                    )
                })


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
        tvBirthErrorMessage = view.findViewById(R.id.tv_birth_error_message)
        tvBirthErrorMessage.visibility = View.GONE


        rvEmploymentType = view.findViewById(R.id.rv_employment_type)
        llWorkExpriance = view.findViewById(R.id.ll_work_expriance)
        etWorkExpriance = view.findViewById(R.id.et_work_expriance)
        tvWorkExprianceErrorMessage = view.findViewById(R.id.tv_work_expriance_error_message)
        tvWorkExprianceErrorMessage.visibility = View.GONE

        llBirthDateSection = view.findViewById(R.id.ll_birth_date_section)
        llEmploymentSection = view.findViewById(R.id.ll_employment_section)
        llAccoutDetailsSection = view.findViewById(R.id.ll_accout_details_section)

        etSearchBank = view.findViewById(R.id.et_search_bank)
        rvBankList = view.findViewById(R.id.rv_bank_list)

        cbMoreThanOneYearInCurrentOrganization = view.findViewById(R.id.cb_more_than_one_year_in_current_organization)

        rlEditYearOfExperience = view.findViewById(R.id.rl_edit_year_of_experience)

        llNetIncomeSection = view.findViewById(R.id.ll_net_income_section)
        llNetIncome = view.findViewById(R.id.ll_net_income)
        etNetIncome = view.findViewById(R.id.et_net_income)
        tvNetIncomeErrorMessage = view.findViewById(R.id.tv_net_income_error_message)
        tvNetIncomeInWords = view.findViewById(R.id.tv_net_income_in_words)

        tvNetIncomeErrorMessage.visibility = View.GONE
        tvNetIncomeInWords.visibility = View.GONE


        tvResendOTPV2.setOnClickListener(this)
        btnMobileNum.setOnClickListener(this)
        llBirthDate.setOnClickListener(this)
        etBirthDate.setOnClickListener(this)
        setEploymentDetailsAdapter()
        setBankDetailsAdapter()

        etWorkExpriance.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tvWorkExprianceErrorMessage.visibility = View.GONE
                etWorkExpriance.setBackgroundResource(R.drawable.vtwo_input_bg)
                etWorkExpriance.setTextColor(resources.getColor(R.color.vtwo_black))

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {


            }
        })
        var timer: Timer? = null
        var allowEdit: Boolean = true
        etNetIncome.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tvNetIncomeErrorMessage.visibility = View.GONE
                llNetIncome.setBackgroundResource(R.drawable.vtwo_input_bg)
                etNetIncome.setTextColor(resources.getColor(R.color.vtwo_black))

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (timer != null) {
                    timer!!.cancel();

                }
                if (TextUtils.isEmpty(etNetIncome.text.toString())) {
                    allowEdit = true
                }
                if (allowEdit == true) {
                    timer = Timer()
                    timer!!.schedule(object : TimerTask() {
                        override fun run() {

                            if (TextUtils.isEmpty(etNetIncome.text)) {
                                //Set Null Income
                            } else {
                                //Set Null Income

                            }
                            allowEdit = false
                            ThreadUtils.runOnUiThread(Runnable {

                                if (!TextUtils.isEmpty(etNetIncome.text.toString())) {
                                    etNetIncome.setText(formatAmount(unformatAmount(etNetIncome.text.toString())))
                                    tvNetIncomeInWords.text = (getAmountInWords(unformatAmount(etNetIncome.text.toString())))
                                    etNetIncome.setSelection(etNetIncome.text.toString().length)
                                } else {
                                    tvNetIncomeInWords.text = ""
                                }
                            })


                        }
                    }, 600)
                } else {
                    tvNetIncomeInWords.setText("")
                }

            }
        })

    }


    fun setEploymentDetailsAdapter() {
        val layoutManagerStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

        rvEmploymentType.addItemDecoration(GridItemDecoration(25, 2))
        llWorkExpriance.visibility = View.GONE
        rvEmploymentType.setLayoutManager(layoutManagerStaggeredGridLayoutManager)
        masterViewModel.getEmploymentTypeDetails(Global.customerDetails_BaseURL + CommonStrings.EMPLOYEEMENT_END_POINT)

    }

    fun setBankDetailsAdapter() {
        val layoutManagerStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

        rvBankList.addItemDecoration(GridItemDecoration(25, 2))
        rvBankList.setLayoutManager(layoutManagerStaggeredGridLayoutManager)

        masterViewModel.getBankList(Global.customerDetails_BaseURL + CommonStrings.BANK_LIST_END_POINT)


    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnMobileNum -> {
                if (ll_otp_v2.visibility == View.GONE) {
                    sendOTP()
                } else if (ll_otp_v2.visibility == View.VISIBLE) {
                    validateOTP()

                } else if (llBirthDateSection.visibility == View.VISIBLE && TextUtils.isEmpty(etBirthDate.text)) {
                    tvBirthErrorMessage.visibility = View.VISIBLE
                    tvBirthErrorMessage.text = "Please add date of birth."
                    llBirthDate.setBackgroundResource(R.drawable.v2_error_input_bg)
                    etBirthDate.setTextColor(resources.getColor(R.color.error_red))
                } else if (llEmploymentSection.visibility == View.VISIBLE && llWorkExpriance.visibility == View.VISIBLE && TextUtils.isEmpty(etWorkExpriance.text)) {
                    tvWorkExprianceErrorMessage.visibility = View.VISIBLE
                    tvWorkExprianceErrorMessage.text = "Please enter total years of work experiences."
                    etWorkExpriance.setBackgroundResource(R.drawable.v2_error_input_bg)
                    etWorkExpriance.setTextColor(resources.getColor(R.color.error_red))
                } else if (llNetIncomeSection.visibility == View.VISIBLE && TextUtils.isEmpty(etNetIncome.text)) {
                    tvNetIncomeErrorMessage.visibility = View.VISIBLE
                    tvNetIncomeErrorMessage.text = "Please enter net annual income."
                    llNetIncome.setBackgroundResource(R.drawable.v2_error_input_bg)
                    etNetIncome.setTextColor(resources.getColor(R.color.error_red))
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
                tvBirthErrorMessage.visibility = View.GONE
                llBirthDate.setBackgroundResource(R.drawable.vtwo_input_bg)
                etBirthDate.setTextColor(resources.getColor(R.color.vtwo_black))
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

    private fun onEmploymentDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val masterResponse: MasterResponse? = mApiResponse.data as MasterResponse?
                setEmploymentDetails(masterResponse!!.data.types)

            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

    private fun setEmploymentDetails(types: List<Types>) {
        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        types.forEachIndexed { index, types ->
            list.add(DataSelectionDTO(types.displayLabel, null, types.value, false))
        }



        employmentDetailsAdapter = DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
            override fun itemClick(item: Any?, position: Int) {


                employmentDetailsAdapter.dataListFilter!!.forEachIndexed { index, item ->
                    run {
                        if (index == position) {
                            item.selected = true
                            if (item.value.equals("Self Employed")) {
                                llWorkExpriance.visibility = View.GONE
                            } else {
                                llWorkExpriance.visibility = View.VISIBLE
                            }

                        } else {
                            item.selected = false
                        }

                    }
                }
                employmentDetailsAdapter.notifyDataSetChanged()
            }
        })


        rvEmploymentType.setAdapter(employmentDetailsAdapter)
    }

    private fun onBankList(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val response: BankListResponse? = mApiResponse.data as BankListResponse?
                setBankListDetails(response!!)

            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

    private fun setBankListDetails(bankListResponse: BankListResponse) {
        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        bankListResponse.data!!.forEachIndexed { index, types ->
            if (index < 4) {
                list.add(DataSelectionDTO(types, null, types, false, "https://www.pikpng.com/pngl/m/71-719828_axis-bank-png-axis-bank-logo-download-clipart.png"))
            }
        }

        bankListDetailsAdapter = DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
            override fun itemClick(item: Any?, position: Int) {


                bankListDetailsAdapter.dataListFilter!!.forEachIndexed { index, item ->
                    run {
                        if (index == position) {
                            item.selected = true


                        } else {
                            item.selected = false
                        }

                    }
                }
                bankListDetailsAdapter.notifyDataSetChanged()
            }
        })


        rvBankList.setAdapter(bankListDetailsAdapter)
    }

}