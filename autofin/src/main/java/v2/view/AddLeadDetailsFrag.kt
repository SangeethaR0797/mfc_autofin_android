package v2.view

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.activity_basic_details.*
import kotlinx.android.synthetic.main.v2_reg_name_email_layout.*
import kotlinx.android.synthetic.main.vtwo_mobile_num_layout.*
import utility.CommonStrings
import utility.Global
import v2.model.dto.AddLeadRequest
import v2.model.dto.DataSelectionDTO
import v2.model.request.*
import v2.model.request.add_lead.BasicDetails
import v2.model.response.AddLeadResponse
import v2.model.response.BankListResponse
import v2.model.response.CustomerDetailsResponse
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
import java.lang.NumberFormatException
import java.util.*


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
    lateinit var employmentDetailsAdapter: DataRecyclerViewAdapter
    lateinit var bankListDetailsAdapter: DataRecyclerViewAdapter

    lateinit var etBirthDate: EditText
    lateinit var etWorkExpriance: EditText
    lateinit var llAddWorkExpriance: LinearLayout
    lateinit var tvWorkExprianceErrorMessage: TextView

    lateinit var rvEmploymentType: RecyclerView
    lateinit var llBirthDate: LinearLayout
    lateinit var llWorkExpriance: LinearLayout
    lateinit var tvBirthErrorMessage: TextView

    lateinit var cbMoreThanOneYearInCurrentOrganization: CheckBox


    lateinit var llBirthDateSection: LinearLayout
    lateinit var llEmploymentSection: LinearLayout
    lateinit var llAccoutDetailsSection: LinearLayout
    lateinit var etSearchBank: EditText
    lateinit var rvBankList: RecyclerView
    lateinit var tvBankTitle: TextView
    lateinit var rlEditYearOfExperience: RelativeLayout

    lateinit var llNetIncomeSection: LinearLayout
    lateinit var llNetIncome: LinearLayout
    lateinit var etNetIncome: EditText
    lateinit var tvNetIncomeErrorMessage: TextView
    lateinit var tvNetIncomeInWords: TextView


    lateinit var llEMISection: LinearLayout
    lateinit var llEMI: LinearLayout
    lateinit var etEMI: EditText
    lateinit var tvEMIErrorMessage: TextView
    lateinit var tvEMIInWords: TextView
    lateinit var rvEMIList: RecyclerView
    lateinit var llEmiDetails: LinearLayout
    lateinit var eMIDetailsAdapter: DataRecyclerViewAdapter
    lateinit var addEmploymentDetailsRequest: AddEmploymentDetailsRequest
    var addEmploymentDataApiCalled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionViewModel = ViewModelProvider(requireActivity()).get(
                TransactionViewModel::class.java
        )


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
        masterViewModel!!.getSalutationsLiveData()
                .observe(this, { mApiResponse: ApiResponse? ->
                    onSalutationRes(
                            mApiResponse!!
                    )
                })

        transactionViewModel!!.getAddEmploymentDetailsLiveData()
                .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                    onAddEmploymentDetails(
                            mApiResponse!!
                    )
                })
        transactionViewModel!!.getCustomerDetailsLiveData()
                .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                    onCustomerDetails(
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


        llBirthDate = view.findViewById(R.id.ll_date)
        etBirthDate = view.findViewById(R.id.et_date)
        tvBirthErrorMessage = view.findViewById(R.id.tv_birth_error_message)
        tvBirthErrorMessage.visibility = View.GONE


        rvEmploymentType = view.findViewById(R.id.rv_employment_type)
        llWorkExpriance = view.findViewById(R.id.ll_work_expriance)
        etWorkExpriance = view.findViewById(R.id.et_work_expriance)
        llAddWorkExpriance = view.findViewById(R.id.ll_add_work_expriance)
        tvWorkExprianceErrorMessage = view.findViewById(R.id.tv_work_expriance_error_message)
        tvWorkExprianceErrorMessage.visibility = View.GONE

        llBirthDateSection = view.findViewById(R.id.ll_birth_date_section)
        llEmploymentSection = view.findViewById(R.id.ll_employment_section)
        llAccoutDetailsSection = view.findViewById(R.id.ll_accout_details_section)

        etSearchBank = view.findViewById(R.id.et_search_bank)
        rvBankList = view.findViewById(R.id.rv_bank_list)
        tvBankTitle = view.findViewById(R.id.tv_bank_title)

        cbMoreThanOneYearInCurrentOrganization = view.findViewById(R.id.cb_more_than_one_year_in_current_organization)

        rlEditYearOfExperience = view.findViewById(R.id.rl_edit_year_of_experience)
        rlEditYearOfExperience.visibility = View.GONE

        llNetIncomeSection = view.findViewById(R.id.ll_net_income_section)
        llNetIncome = view.findViewById(R.id.ll_net_income)
        etNetIncome = view.findViewById(R.id.et_net_income)
        tvNetIncomeErrorMessage = view.findViewById(R.id.tv_net_income_error_message)
        tvNetIncomeInWords = view.findViewById(R.id.tv_net_income_in_words)

        tvNetIncomeErrorMessage.visibility = View.GONE








        llEMISection = view.findViewById(R.id.ll_emi_section)
        llEMI = view.findViewById(R.id.ll_emi)
        etEMI = view.findViewById(R.id.et_emi)
        tvEMIErrorMessage = view.findViewById(R.id.tv_emi_error_message)
        tvEMIInWords = view.findViewById(R.id.tv_emi_in_words)
        rvEMIList = view.findViewById(R.id.rv_emi_list)
        llEmiDetails = view.findViewById(R.id.ll_emi_details)
        llEmiDetails.visibility = View.GONE

        tvEMIErrorMessage.visibility = View.GONE

        masterViewModel = ViewModelProvider(this).get(
                MasterViewModel::class.java
        )

        tvResendOTPV2.setOnClickListener(this)
        btnMobileNum.setOnClickListener(this)
        llBirthDate.setOnClickListener(this)
        etBirthDate.setOnClickListener(this)
        setEploymentDetailsAdapter()
        setBankDetailsAdapter()

        //Hide All Section
        llNameAndEmailV2.visibility = View.GONE
        llBirthDateSection.visibility = View.GONE
        llEmploymentSection.visibility = View.GONE
        llAccoutDetailsSection.visibility = View.GONE
        llNetIncomeSection.visibility = View.GONE
        llEMISection.visibility = View.GONE

        setTextChangeOfWorkExpirance()
        setCheckBoxEvent()
        setTextChangeOfNetIncome()
        setTextChangeOfEMI()
        setEMIDetailsAdapter()

    }

    fun setCheckBoxEvent() {
        cbMoreThanOneYearInCurrentOrganization.setOnCheckedChangeListener(null)
        cbMoreThanOneYearInCurrentOrganization.setOnClickListener { addEmploymentDetailsRequest.Data!!.employmentDetails!!.CurrentCompanyExpMoreThanOne = cbMoreThanOneYearInCurrentOrganization.isChecked }

    }
//region setTextChangeEvent

    fun setTextChangeOfWorkExpirance() {
        etWorkExpriance.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tvWorkExprianceErrorMessage.visibility = View.GONE
                llAddWorkExpriance.setBackgroundResource(R.drawable.vtwo_input_bg)
                etWorkExpriance.setTextColor(resources.getColor(R.color.vtwo_black))
                if (TextUtils.isEmpty(etWorkExpriance.text)) {
                    addEmploymentDetailsRequest.Data!!.employmentDetails!!.TotalWorkExperience = null
                } else {
                    addEmploymentDetailsRequest.Data!!.employmentDetails!!.TotalWorkExperience = etWorkExpriance.text.toString()
                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {


            }
        })
    }

    fun setTextChangeOfNetIncome() {
        var timerNeftIncome: Timer? = null
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
                try {


                    if (timerNeftIncome != null) {
                        timerNeftIncome!!.cancel();

                    }
                    if (!unformatAmount(etNetIncome.text.toString()).equals(addEmploymentDetailsRequest.Data?.employmentDetails?.NetAnualIncome.toString()) || TextUtils.isEmpty(etNetIncome.text.toString())) {
                        allowEdit = true
                    }
                    if (allowEdit == true) {
                        timerNeftIncome = Timer()
                        timerNeftIncome!!.schedule(object : TimerTask() {
                            override fun run() {

                                if (TextUtils.isEmpty(etNetIncome.text)) {
                                    //Set Null Income
                                    addEmploymentDetailsRequest.Data!!.employmentDetails!!.NetAnualIncome = 0
                                } else {
                                    //Set Income
                                    try {
                                        addEmploymentDetailsRequest.Data!!.employmentDetails!!.NetAnualIncome = unformatAmount(etNetIncome.text.toString()).toInt()
                                    } catch (e: Exception) {

                                    } catch (e: NumberFormatException) {

                                    }

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
                } catch (e: Exception) {

                }
            }
        })
    }

    fun setTextChangeOfEMI() {
        var timerEMI: Timer? = null
        var allowEdit: Boolean = true
        etEMI.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tvEMIErrorMessage.visibility = View.GONE
                llEMI.setBackgroundResource(R.drawable.vtwo_input_bg)
                etEMI.setTextColor(resources.getColor(R.color.vtwo_black))

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                try {
                    if (timerEMI != null) {
                        timerEMI!!.cancel();

                    }
                    if (TextUtils.isEmpty(etEMI.text.toString())) {
                        allowEdit = true
                    }
                    if (allowEdit == true) {
                        timerEMI = Timer()
                        timerEMI!!.schedule(object : TimerTask() {
                            override fun run() {

                                if (TextUtils.isEmpty(etEMI.text)) {
                                    //Set Null Income
                                } else {
                                    //Set Null Income

                                }
                                allowEdit = false
                                ThreadUtils.runOnUiThread(Runnable {

                                    if (!TextUtils.isEmpty(etEMI.text.toString())) {
                                        etEMI.setText(formatAmount(unformatAmount(etEMI.text.toString())))
                                        tvEMIInWords.text = (getAmountInWords(unformatAmount(etEMI.text.toString())))
                                        etEMI.setSelection(etEMI.text.toString().length)
                                    } else {
                                        tvEMIInWords.text = ""
                                    }
                                })


                            }
                        }, 600)
                    } else {
                        tvEMIInWords.setText("")
                    }
                } catch (e: Exception) {

                }
            }
        })
    }

    //endregion setTextChangeEvent
    private fun setEMIDetailsAdapter() {
        val layoutManagerStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

        rvEMIList.addItemDecoration(GridItemDecoration(25, 2))
        rvEMIList.setLayoutManager(layoutManagerStaggeredGridLayoutManager)

        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()
        list.add(DataSelectionDTO("Yes", null, "Yes", false))
        list.add(DataSelectionDTO("No", null, "No", false))


        eMIDetailsAdapter = DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
            override fun itemClick(item: Any?, position: Int) {


                eMIDetailsAdapter.dataListFilter!!.forEachIndexed { index, item ->
                    run {
                        if (index == position) {
                            item.selected = true
                            if (item.value.equals("Yes")) {
                                llEmiDetails.visibility = View.VISIBLE
                            } else {
                                llEmiDetails.visibility = View.GONE
                            }

                        } else {
                            item.selected = false
                        }

                    }
                }
                eMIDetailsAdapter.notifyDataSetChanged()
            }
        })


        rvEMIList.setAdapter(eMIDetailsAdapter)
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
                hideSoftKeyboard()
                when {
                    ll_otp_v2.visibility == View.GONE && llNameAndEmailV2.visibility == View.GONE -> {
                        sendOTP()
                    }
                    ll_otp_v2.visibility == View.VISIBLE && llNameAndEmailV2.visibility == View.GONE -> {
                        validateOTP()
                    }
                    TextUtils.isEmpty(caseId) && llNameAndEmailV2.visibility == View.VISIBLE -> {
                        addLead()
                    }

                    llBirthDateSection.visibility == View.VISIBLE && TextUtils.isEmpty(etBirthDate.text) -> {
                        tvBirthErrorMessage.visibility = View.VISIBLE
                        tvBirthErrorMessage.text = "Please add date of birth."
                        llBirthDate.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etBirthDate.setTextColor(resources.getColor(R.color.error_red))
                    }
                    llEmploymentSection.visibility == View.GONE -> {
                        llEmploymentSection.visibility = View.VISIBLE
                    }
                    llEmploymentSection.visibility == View.VISIBLE && llWorkExpriance.visibility == View.VISIBLE && TextUtils.isEmpty(etWorkExpriance.text) -> {
                        tvWorkExprianceErrorMessage.visibility = View.VISIBLE
                        tvWorkExprianceErrorMessage.text = "Please enter total years of work experiences."
                        llAddWorkExpriance.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etWorkExpriance.setTextColor(resources.getColor(R.color.error_red))
                    }
                    llAccoutDetailsSection.visibility == View.GONE -> {
                        llAccoutDetailsSection.visibility = View.VISIBLE
                    }
                    llNetIncomeSection.visibility == View.GONE -> {
                        llNetIncomeSection.visibility = View.VISIBLE
                    }
                    llNetIncomeSection.visibility == View.VISIBLE && TextUtils.isEmpty(etNetIncome.text) -> {
                        tvNetIncomeErrorMessage.visibility = View.VISIBLE
                        tvNetIncomeErrorMessage.text = "Please enter net annual income."
                        llNetIncome.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etNetIncome.setTextColor(resources.getColor(R.color.error_red))
                    }
                    addEmploymentDataApiCalled == false -> {
                        //Call Add EmploymentDetails Api
                        transactionViewModel.addEmploymentDetails(addEmploymentDetailsRequest, Global.customerAPI_BaseURL + CommonStrings.ADD_EMPLOYMENT_URL_END)

                    }

                    llEMISection.visibility == View.GONE -> {
                        llEMISection.visibility = View.VISIBLE
                    }
                    llEMISection.visibility == View.VISIBLE && llEmiDetails.visibility == View.VISIBLE && TextUtils.isEmpty(etEMI.text) -> {
                        tvEMIErrorMessage.visibility = View.VISIBLE
                        tvEMIErrorMessage.text = "Please enter EMI amount."
                        llEMI.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etEMI.setTextColor(resources.getColor(R.color.error_red))
                    }
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
            override fun dateSelected(dateDisplayValue: String, dateValue: String) {
                addEmploymentDetailsRequest.Data!!.personalDetails!!.BirthDate = dateValue
                etBirthDate.setText(dateDisplayValue)
                tvBirthErrorMessage.visibility = View.GONE
                llBirthDate.setBackgroundResource(R.drawable.vtwo_input_bg)
                etBirthDate.setTextColor(resources.getColor(R.color.vtwo_black))
                llEmploymentSection.visibility = View.VISIBLE
            }
        })
    }


    private fun sendOTP() {
        if (etMobileNumberV2.text.length == 10) {

            transactionViewModel!!.getGenerateOTPLiveData()
                    .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                        onGenerateOTP(
                                mApiResponse!!
                        )
                    })
            transactionViewModel!!.generateOTP(getOtpRequest(null, etMobileNumberV2.text.toString()), Global.customerAPI_BaseURL + CommonStrings.OTP_URL_END)


        } else {
            showToast("Please enter Valid Mobile Number")
        }
    }

//region API call region starts

    private fun validateOTP() {
        if (etOTPV2.text.length == 6) {
            transactionViewModel!!.getValidateOTPLiveData()
                    .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                        onValidateOTP(
                                mApiResponse!!
                        )
                    })
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
                transactionViewModel.getAddLeadLiveData().observe(requireActivity(), { mApiResponse: ApiResponse? ->
                    onAddLead(
                            mApiResponse!!
                    )
                })
                transactionViewModel.addLead(addLeadRequest, Global.customerAPI_BaseURL + CommonStrings.ADD_LEAD_URL_END)


            } else
                showToast("Please enter valid Email Id")
        } else
            showToast("Please enter all fields to proceed further!")
    }

//endregion API call region ends

//region OnResponse Region starts

    private fun onGenerateOTP(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val otpResponse: OTPResponse? = mApiResponse.data as OTPResponse?
                if (otpResponse?.status!!)
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

    //endregion OnResponse Region starts
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
                            addEmploymentDetailsRequest.Data!!.employmentDetails!!.EmploymentType = item.value
                            if (item.value.equals("Self Employed")) {
                                llWorkExpriance.visibility = View.GONE
                                etWorkExpriance.setText("")
                                llAccoutDetailsSection.visibility = View.VISIBLE
                                tvBankTitle.setText(getString(R.string.primary_bank_account))

                            } else {
                                llWorkExpriance.visibility = View.VISIBLE
                                tvBankTitle.setText(getString(R.string.salary_bank_account))
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
                            if (addEmploymentDetailsRequest.Data!!.employmentDetails!!.EmploymentType.equals("Self Employed")) {
                                addEmploymentDetailsRequest.Data!!.employmentDetails!!.PrimaryAccount = item.displayValue
                                addEmploymentDetailsRequest.Data!!.employmentDetails!!.SalaryAccount = null
                            } else {
                                addEmploymentDetailsRequest.Data!!.employmentDetails!!.PrimaryAccount = null
                                addEmploymentDetailsRequest.Data!!.employmentDetails!!.SalaryAccount = item.displayValue
                            }
                            llNetIncome.visibility = View.VISIBLE


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
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val addLeadResponse: AddLeadResponse? = mApiResponse.data as AddLeadResponse?
                if (addLeadResponse?.statusCode.equals("200") || addLeadResponse?.mData!! > 0) {
                    caseId = addLeadResponse?.mData.toString()
                    showToast(addLeadResponse!!.message.toString())
                    llBirthDateSection.visibility = View.VISIBLE
                    //Create Request of Add Employment Details
                    addEmploymentDetailsRequest = createAddEmploymentDetailsRequest(addLeadResponse.mData!!)

                    transactionViewModel.getCustomerDetails(createCustomerDetailsRequest(addLeadResponse.mData!!), Global.customerAPI_BaseURL + CommonStrings.CUSTOMER_DETAILS_END_URL)
                }
                showToast(addLeadResponse?.message.toString())

            }
            ApiResponse.Status.ERROR -> {
                val addLeadResponse: AddLeadResponse? = mApiResponse.data as AddLeadResponse?
            }
            else -> {
                showToast("Please enter valid details")
            }
        }

    }

    private fun onAddEmploymentDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val addLeadResponse: AddLeadResponse? = mApiResponse.data as AddLeadResponse?
                if (addLeadResponse?.mData!! > 0) {
                    var addEmploymentDetailsId = addLeadResponse?.mData.toString()
                    addEmploymentDataApiCalled = true
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

    fun createAddEmploymentDetailsRequest(customerId: Int): AddEmploymentDetailsRequest {
        var addEmploymentDetailsDataRequest = AddEmploymentDetailsRequest()
        addEmploymentDetailsDataRequest.UserId = CommonStrings.DEALER_ID
        addEmploymentDetailsDataRequest.UserType = CommonStrings.USER_TYPE

        var addEmploymentEmploymentDetails = AddEmploymentEmploymentDetails()
        var addEmploymentPersonalDetails = AddEmploymentPersonalDetails()

        var addEmploymentData = AddEmploymentData(customerId, addEmploymentEmploymentDetails, addEmploymentPersonalDetails)
        addEmploymentDetailsDataRequest.Data = addEmploymentData
        return addEmploymentDetailsDataRequest

    }

    fun createCustomerDetailsRequest(customerId: Int): CustomerRequest {
        var customerDetailsRequest = CustomerRequest()
        customerDetailsRequest.UserId = CommonStrings.DEALER_ID
        customerDetailsRequest.UserType = CommonStrings.USER_TYPE
        var customerJourneyDataRequest = ResetCustomerJourneyDataRequest();
        customerJourneyDataRequest.CustomerId = customerId.toString()
        customerDetailsRequest.Data = customerJourneyDataRequest
        return customerDetailsRequest
    }

    private fun onCustomerDetails(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val customerDetailsResponse: CustomerDetailsResponse? = mApiResponse.data as CustomerDetailsResponse?
                if (customerDetailsResponse?.data != null) {
                    preFilledData(customerDetailsResponse)
                }


            }
            ApiResponse.Status.ERROR -> {

            }
            else -> {
                showToast("Please enter valid details")
            }
        }

    }


    // OnResponse region ends
    fun preFilledData(customerDetailsResponse: CustomerDetailsResponse?) {
        try {
            var birthDateDisplayValue: String? = null
            var birthDateValue: String? = null
            var employmentType: String? = null
            var bankName: String? = null



            if (customerDetailsResponse != null && customerDetailsResponse.data != null) {
                //set Vehicle details
                if (customerDetailsResponse!!.data!!.vehicleDetails != null) {
                    addLeadRequest.Data!!.vehicleDetails!!.VehicleNumber = customerDetailsResponse!!.data!!.vehicleDetails!!.vehicleNumber
                    addLeadRequest.Data!!.vehicleDetails!!.VehicleSellingPrice = customerDetailsResponse!!.data!!.vehicleDetails!!.vehicleSellingPrice!!.toInt().toString()
                    addLeadRequest.Data!!.vehicleDetails!!.KMs = customerDetailsResponse!!.data!!.vehicleDetails!!.kMs
                    addLeadRequest.Data!!.vehicleDetails!!.FuelType = customerDetailsResponse!!.data!!.vehicleDetails!!.fuelType
                    addLeadRequest.Data!!.vehicleDetails!!.Ownership = customerDetailsResponse!!.data!!.vehicleDetails!!.ownership
                }
                //set basicDetails
                if (customerDetailsResponse!!.data!!.basicDetails != null) {
                    displayNameLayout()
                    addLeadRequest.Data!!.basicDetails!!.FirstName = customerDetailsResponse!!.data!!.basicDetails!!.firstName
                    addLeadRequest.Data!!.basicDetails!!.LastName = customerDetailsResponse!!.data!!.basicDetails!!.lastName
                    addLeadRequest.Data!!.basicDetails!!.Email = customerDetailsResponse!!.data!!.basicDetails!!.email
                    addLeadRequest.Data!!.basicDetails!!.Salutation = customerDetailsResponse!!.data!!.basicDetails!!.salutation

                    etFirstName.setText(customerDetailsResponse!!.data!!.basicDetails!!.firstName)
                    et_last_name.setText(customerDetailsResponse!!.data!!.basicDetails!!.lastName)
                    et_email.setText(customerDetailsResponse!!.data!!.basicDetails!!.lastName)

                    var salutation =(customerDetailsResponse!!.data!!.basicDetails!!.salutation)

                    if (salutation != null) {
                        salutationAdapter.dataListFilter!!.forEachIndexed { index, dataSelectionDTO ->
                            if (dataSelectionDTO.displayValue.toString().equals(salutation)) {
                                dataSelectionDTO.selected = true
                            } else {
                                dataSelectionDTO.selected = false
                            }
                        }
                        salutationAdapter.notifyDataSetChanged()

                    }
                }

                //Set Birth Data
                birthDateDisplayValue = stringToDateString(customerDetailsResponse.data!!.basicDetails!!.birthDate.toString().subSequence(0, 10) as String, DATE_FORMATE_YYYYMMDD, DATE_FORMATE_DDMMYYYY)
                birthDateValue = stringToDateString(customerDetailsResponse.data!!.basicDetails!!.birthDate.toString().subSequence(0, 10) as String, DATE_FORMATE_YYYYMMDD, DATE_FORMATE_YYYYMMDD)

                if (birthDateDisplayValue == null) {
                    birthDateDisplayValue = stringToDateString(customerDetailsResponse.data!!.equifaxFields!!.birthDate!![0], DATE_FORMATE_YYYYMMDD, DATE_FORMATE_DDMMYYYY)
                    birthDateValue = stringToDateString(customerDetailsResponse.data!!.equifaxFields!!.birthDate!![0], DATE_FORMATE_YYYYMMDD, DATE_FORMATE_YYYYMMDD)

                }

                if (birthDateDisplayValue != null) {
                    llBirthDateSection.visibility = View.VISIBLE
                    etBirthDate.setText(birthDateDisplayValue)
                    addEmploymentDetailsRequest.Data!!.personalDetails!!.BirthDate = birthDateValue
                }

                //Set Employment Details
                if (customerDetailsResponse.data!!.employmentDetails != null) {
                    llEmploymentSection.visibility = View.VISIBLE
                    employmentDetailsAdapter.dataListFilter!!.forEachIndexed { index, dataSelectionDTO ->
                        if (dataSelectionDTO.displayValue.toString().equals(customerDetailsResponse.data!!.employmentDetails!!.employmentType)) {
                            dataSelectionDTO.selected = true
                            employmentType = customerDetailsResponse.data!!.employmentDetails!!.employmentType
                            addEmploymentDetailsRequest.Data!!.employmentDetails!!.EmploymentType = dataSelectionDTO.value
                        } else {
                            dataSelectionDTO.selected = false
                        }
                    }
                    employmentDetailsAdapter.notifyDataSetChanged()

                    //set Employment other details

                    if (employmentType.equals("Self Employed")) {
                        llWorkExpriance.visibility = View.GONE
                        etWorkExpriance.setText("")
                        llAccoutDetailsSection.visibility = View.VISIBLE
                        tvBankTitle.setText(getString(R.string.primary_bank_account))
                        bankName = customerDetailsResponse.data!!.employmentDetails!!.primaryAccount
                        addEmploymentDetailsRequest.Data!!.employmentDetails!!.PrimaryAccount = bankName

                    } else {
                        bankName = customerDetailsResponse.data!!.employmentDetails!!.salaryAccount
                        addEmploymentDetailsRequest.Data!!.employmentDetails!!.SalaryAccount = bankName
                        llWorkExpriance.visibility = View.VISIBLE
                        tvBankTitle.setText(getString(R.string.salary_bank_account))
                        if (customerDetailsResponse.data!!.employmentDetails!!.totalWorkExperience != null) {
                            etWorkExpriance.setText(customerDetailsResponse.data!!.employmentDetails!!.totalWorkExperience!!)
                        }
                        //More than one year in same org
                        cbMoreThanOneYearInCurrentOrganization.isChecked = customerDetailsResponse.data!!.employmentDetails!!.currentCompanyExpMoreThanOne!!
                        addEmploymentDetailsRequest.Data!!.employmentDetails!!.CurrentCompanyExpMoreThanOne = customerDetailsResponse.data!!.employmentDetails!!.currentCompanyExpMoreThanOne!!


                    }
                    //set Bank Details
                    if (bankName != null) {
                        bankListDetailsAdapter.dataListFilter!!.forEachIndexed { index, dataSelectionDTO ->
                            if (dataSelectionDTO.displayValue.toString().equals(bankName)) {
                                dataSelectionDTO.selected = true
                            } else {
                                dataSelectionDTO.selected = false
                            }
                        }
                        bankListDetailsAdapter.notifyDataSetChanged()
                        llAccoutDetailsSection.visibility = View.VISIBLE
                    }


                    //set net income
                    if (customerDetailsResponse.data!!.employmentDetails!!.netAnualIncome != null) {
                        llNetIncomeSection.visibility = View.VISIBLE
                        etNetIncome.setText(customerDetailsResponse.data!!.employmentDetails!!.netAnualIncome!!.toInt().toString())
                        addEmploymentDetailsRequest.Data!!.employmentDetails!!.NetAnualIncome = customerDetailsResponse.data!!.employmentDetails!!.netAnualIncome!!.toInt()
                    }


                }
            }
        } catch (e: Exception) {

        }

    }

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


}