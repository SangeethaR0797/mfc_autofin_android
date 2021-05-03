package v2.view

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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
import v2.model.response.*
import v2.model.response.AddLeadResponse
import v2.model.response.BankListResponse
import v2.model.response.CustomerDetailsResponse
import v2.model.response.OTPResponse
import v2.model.response.master.MasterResponse
import v2.model.response.master.Types
import v2.model_view.MasterViewModel
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.CustomAutoTextViewListAdapter
import v2.view.adapter.DataRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.DatePickerCallBack
import v2.view.callBackInterface.itemClickCallBack
import v2.view.utility_view.GridItemDecoration
import java.lang.NumberFormatException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


public class AddLeadDetailsFrag : BaseFragment(), View.OnClickListener {


    private var onClickNext: Boolean = false
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
    lateinit var dialogConfilctForAddLead: Dialog
    lateinit var timer: CountDownTimer

    var make: String = ""
    var model: String = ""
    var variant: String = ""
    var mobileNum: String = ""
    var userId: String = ""
    var userType: String = ""
    var salutation: String = ""
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
    lateinit var llAddSearchBank: LinearLayout
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

    lateinit var llResidenceTypeSection: LinearLayout
    lateinit var llResidenceType: LinearLayout
    lateinit var etAutoResidenceCity: AutoCompleteTextView
    lateinit var tvResidenceTypeErrorMessage: TextView

    lateinit var rvResidenceTypeList: RecyclerView
    lateinit var rvResidenceYears: RecyclerView
    lateinit var llResidenceTypeDetails: LinearLayout
    lateinit var residenceTypeDetailsAdapter: DataRecyclerViewAdapter
    lateinit var residenceYearsAdapter: DataRecyclerViewAdapter

    lateinit var addEmploymentDetailsRequest: AddEmploymentDetailsRequest
    lateinit var customAutoTextViewListAdapter: CustomAutoTextViewListAdapter
    var isEmploymentDataSaved: Boolean = false

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
        masterViewModel.getEmploymentTypeLiveData()
                .observe(this, { mApiResponse: ApiResponse? ->
                    onEmploymentTypeListDetails(
                            mApiResponse!!
                    )
                })

        masterViewModel.getBankListLiveData()
                .observe(this, { mApiResponse: ApiResponse? ->
                    onBankList(
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
        transactionViewModel.getAddEmploymentDetailsLiveData().observe(requireActivity(), { mApiResponse: ApiResponse? ->
            onAddEmploymentDetails(
                    mApiResponse!!
            )
        })
        transactionViewModel.getCustomerDetailsLiveData().observe(requireActivity(), { mApiResponse: ApiResponse? ->
            onCustomerDetails(
                    mApiResponse!!
            )
        })
        masterViewModel.getResidentTypeLiveData()
                .observe(this, { mApiResponse: ApiResponse? ->
                    onResidentType(
                            mApiResponse!!
                    )
                })

        masterViewModel.getResidentYearsLiveData()
                .observe(this, { mApiResponse: ApiResponse? ->
                    onResidentYears(
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
        tvOTPEHint = view.findViewById(R.id.tvOTPEHint)

        cbTermsAndConditions = view.findViewById(R.id.cbTermsAndConditions)
        tvResendOTPV2 = view.findViewById(R.id.tvResendOTPV2)
        tvOTPTimerV2 = view.findViewById(R.id.tvOTPTimerV2)
        btnMobileNum = view.findViewById(R.id.btnMobileNum)
        ll_otp_v2 = view.findViewById(R.id.ll_otp_v2)
        ll_otp_v2.visibility = View.GONE
        llTAndC = view.findViewById(R.id.llTAndC)

        rv_salutation = view.findViewById(R.id.rv_salutation)
        llNameAndEmailV2 = view.findViewById(R.id.llNameAndEmailV2)
        etFirstName = view.findViewById(R.id.et_first_name)
        ll_otp_v2.visibility = View.GONE
        llTAndC = view.findViewById(R.id.llTAndC)


        make = addLeadRequest.Data?.vehicleDetails?.Make.toString()
        model = addLeadRequest.Data?.vehicleDetails?.Model.toString()
        variant = addLeadRequest.Data?.vehicleDetails?.Variant.toString()

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
        etSearchBank.isFocusable = false
        rvBankList = view.findViewById(R.id.rv_bank_list)
        tvBankTitle = view.findViewById(R.id.tv_bank_title)
        llAddSearchBank = view.findViewById(R.id.ll_search_bank)

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


        llResidenceTypeSection = view.findViewById(R.id.ll_residence_type_section)
        llResidenceType = view.findViewById(R.id.ll_residence_type)
        etAutoResidenceCity = view.findViewById(R.id.et_residence_type)
        tvResidenceTypeErrorMessage = view.findViewById(R.id.tv_residence_type_error_message)

        rvResidenceTypeList = view.findViewById(R.id.rv_residence_type_list)
        rvResidenceYears = view.findViewById(R.id.rv_residence_year)
        llResidenceTypeDetails = view.findViewById(R.id.ll_residence_type_details)
        //  llResidenceTypeDetails.visibility = View.GONE

        tvResidenceTypeErrorMessage.visibility = View.GONE



        tvResendOTPV2.setOnClickListener(this)
        btnMobileNum.setOnClickListener(this)
        llBirthDate.setOnClickListener(this)
        etBirthDate.setOnClickListener(this)
        etSearchBank.setOnClickListener(this)
        llAddSearchBank.setOnClickListener(this)


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

        setSalutaionAdapterData();
        setEploymentDetailsAdapter()
        setBankDetailsAdapter()
        setEMIDetailsAdapter()
        setResidenceTypeAdapter()
        setResidenceYearsAdapter()

        val cityList: ArrayList<String> = arrayListOf<String>()
        cityList.add("Pune")
        cityList.add("Mumbai")
        cityList.add("Kolhapur")

        setCityAutoTextAdapter(cityList)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonStrings.MASTER_DETAIL_ACTIVITY_REQUEST_CODE && resultCode == CommonStrings.RESULT_CODE) {
            var dataSelectionDTO: DataSelectionDTO? = data?.getParcelableExtra(CommonStrings.SELECTED_DATA)
            var dataType: String? = data?.getStringExtra(CommonStrings.SELECTED_DATA_TYPE)
            if (dataType.equals(CommonStrings.BANK_DATA_CALL)) {
                etSearchBank.setText(dataSelectionDTO!!.displayValue)
                updateBankSelection(dataSelectionDTO)
            }


        }
    }

    fun callCustomerDetailsApi(customerId: Int) {
        caseId = customerId.toString()
        addEmploymentDetailsRequest = createAddEmploymentDetailsRequest(customerId)
        transactionViewModel.getCustomerDetails(createCustomerDetailsRequest(customerId), Global.customerAPI_BaseURL + CommonStrings.CUSTOMER_DETAILS_END_URL)

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
    fun setSalutaionAdapterData() {
        masterViewModel!!.getSalutations(Global.customerDetails_BaseURL + CommonStrings.SALUTATION_END_POINT)
    }

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

        ivBackToVehDetails.setOnClickListener(this)

    }

    fun setResidenceTypeAdapter() {
        val layoutManagerStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

        rvResidenceTypeList.addItemDecoration(GridItemDecoration(25, 2))

        rvResidenceTypeList.setLayoutManager(layoutManagerStaggeredGridLayoutManager)
        masterViewModel.getResidentType(Global.customerDetails_BaseURL + CommonStrings.RESIDENT_TYPE_END_POINT)


    }

    fun setResidenceYearsAdapter() {
        val layoutManagerStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

        rvResidenceYears.addItemDecoration(GridItemDecoration(25, 2))

        rvResidenceYears.setLayoutManager(layoutManagerStaggeredGridLayoutManager)
        masterViewModel.getResidentYears(Global.customerDetails_BaseURL + CommonStrings.RESIDENT_YEARS_END_POINT)


    }

    fun setCityAutoTextAdapter(cityList: ArrayList<String>) {
        customAutoTextViewListAdapter = CustomAutoTextViewListAdapter(activity!!.baseContext, R.layout.v2_string_item_layout, cityList)
        etAutoResidenceCity.setAdapter(customAutoTextViewListAdapter)
        etAutoResidenceCity.threshold=1
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.ivBackToVehDetails -> {
                activity?.onBackPressed()
            }
            R.id.et_search_bank -> {
                navigateMasterDataSelectionActivity(CommonStrings.MASTER_DETAIL_ACTIVITY_REQUEST_CODE, CommonStrings.BANK_DATA_CALL)
            }
            R.id.ll_search_bank -> {
                navigateMasterDataSelectionActivity(CommonStrings.MASTER_DETAIL_ACTIVITY_REQUEST_CODE, CommonStrings.BANK_DATA_CALL)
            }
            R.id.btnMobileNum -> {
                hideSoftKeyboard()
                when {
                    //Step 1 Call Send OTP Api
                    ll_otp_v2.visibility == View.GONE && llNameAndEmailV2.visibility == View.GONE -> {
                        sendOTP()
                    }
                    //Step 2 Open View to Enter OTP
                    ll_otp_v2.visibility == View.VISIBLE && llNameAndEmailV2.visibility == View.GONE -> {

                        if (cbTermsAndConditions.isChecked) {
                            onClickNext = true
                            timer.onFinish()
                            //Validate OTP and Validate Lead
                            validateOTP()
                        } else
                            showToast("Please check Terms and Condition")
                    }
                    //Step 3 Call AddLead API
                    TextUtils.isEmpty(caseId) && llNameAndEmailV2.visibility == View.VISIBLE -> {
                        addLead()
                    }
                    //Step 4 Birth Date Validation
                    llBirthDateSection.visibility == View.VISIBLE && TextUtils.isEmpty(etBirthDate.text) -> {
                        tvBirthErrorMessage.visibility = View.VISIBLE
                        tvBirthErrorMessage.text = "Please add date of birth."
                        llBirthDate.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etBirthDate.setTextColor(resources.getColor(R.color.error_red))
                    }
                    //Step 5 Open Employment Section
                    llEmploymentSection.visibility == View.GONE -> {
                        llEmploymentSection.visibility = View.VISIBLE
                    }
                    //Step 6 Validate Employment Details
                    llEmploymentSection.visibility == View.VISIBLE && llWorkExpriance.visibility == View.VISIBLE && TextUtils.isEmpty(etWorkExpriance.text) -> {
                        tvWorkExprianceErrorMessage.visibility = View.VISIBLE
                        tvWorkExprianceErrorMessage.text = "Please enter total years of work experiences."
                        llAddWorkExpriance.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etWorkExpriance.setTextColor(resources.getColor(R.color.error_red))
                    }
                    //Step 7 Open Account Details Section
                    llAccoutDetailsSection.visibility == View.GONE -> {
                        llAccoutDetailsSection.visibility = View.VISIBLE
                    }
                    llNetIncomeSection.visibility == View.GONE -> {
                        llNetIncomeSection.visibility = View.VISIBLE
                    }
                    //Step 8 Validate Acount Details
                    llNetIncomeSection.visibility == View.VISIBLE && TextUtils.isEmpty(etNetIncome.text) -> {
                        tvNetIncomeErrorMessage.visibility = View.VISIBLE
                        tvNetIncomeErrorMessage.text = "Please enter net annual income."
                        llNetIncome.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etNetIncome.setTextColor(resources.getColor(R.color.error_red))
                    }
                    //Step 9 Call Add Employment Details API
                    isEmploymentDataSaved == false -> {
                        //Call Add EmploymentDetails Api
                        transactionViewModel.addEmploymentDetails(addEmploymentDetailsRequest, Global.customerAPI_BaseURL + CommonStrings.ADD_EMPLOYMENT_URL_END)

                    }
                    //Step 10 Open EMI Section
                    llEMISection.visibility == View.GONE -> {
                        llEMISection.visibility = View.VISIBLE
                    }
                    //Step 11 Open EMI Section Validate EMI Details
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
        var lastSelectedDate: String? = null
        if (addEmploymentDetailsRequest.Data != null && addEmploymentDetailsRequest.Data!!.personalDetails != null && addEmploymentDetailsRequest.Data!!.personalDetails!!.BirthDate != null) {
            lastSelectedDate = addEmploymentDetailsRequest.Data!!.personalDetails!!.BirthDate!!
        }
        callDatePickerDialog(lastSelectedDate, object : DatePickerCallBack {
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

//endregion API call region ends

//region OnResponse Region starts

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
        timer = object : CountDownTimer(120000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                tvOTPTimerV2.text = "" + millisUntilFinished / 1000 + " Sec"
                onClickNext = false
            }

            override fun onFinish() {
                tvOTPTimerV2.setText("0 Sec")
                timer.cancel()
                if (!onClickNext) {
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
                hideProgressDialog()

            }
        }
    }

    //endregion OnResponse Region starts
    private fun onEmploymentTypeListDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val masterResponse: MasterResponse? = mApiResponse.data as MasterResponse?
                setEmploymentTypeListAdapterDetails(masterResponse!!.data.types)

            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

    private fun setEmploymentTypeListAdapterDetails(types: List<Types>) {
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

    //region onResidentType
    private fun onResidentType(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val masterResponse: MasterResponse? = mApiResponse.data as MasterResponse?
                setResidentTypeAdapterDetails(masterResponse!!.data.types)

            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

    private fun setResidentTypeAdapterDetails(types: List<Types>) {
        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        types.forEachIndexed { index, types ->
            list.add(DataSelectionDTO(types.displayLabel, null, types.value, false))
        }



        residenceTypeDetailsAdapter = DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
            override fun itemClick(item: Any?, position: Int) {


                residenceTypeDetailsAdapter.dataListFilter!!.forEachIndexed { index, item ->
                    run {
                        if (index == position) {
                            item.selected = true


                        } else {
                            item.selected = false
                        }

                    }
                }
                residenceTypeDetailsAdapter.notifyDataSetChanged()
            }
        })


        rvResidenceTypeList.setAdapter(residenceTypeDetailsAdapter)
    }
    //endregion onResidentType

    //region onResident Years
    private fun onResidentYears(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val masterResponse: MasterResponse? = mApiResponse.data as MasterResponse?
                setResidentYearsAdapterDetails(masterResponse!!.data.types)

            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

    private fun setResidentYearsAdapterDetails(types: List<Types>) {
        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        types.forEachIndexed { index, types ->
            list.add(DataSelectionDTO(types.displayLabel, null, types.value, false))
        }



        residenceYearsAdapter = DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
            override fun itemClick(item: Any?, position: Int) {


                residenceYearsAdapter.dataListFilter!!.forEachIndexed { index, item ->
                    run {
                        if (index == position) {
                            item.selected = true


                        } else {
                            item.selected = false
                        }

                    }
                }
                residenceYearsAdapter.notifyDataSetChanged()
            }
        })


        rvResidenceYears.setAdapter(residenceYearsAdapter)
    }

    //endregion onResidentType
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
                etSearchBank.setText("")
                var selectedBankDataSelectionDTO: DataSelectionDTO = item as DataSelectionDTO
                updateBankSelection(selectedBankDataSelectionDTO)

            }
        })


        rvBankList.setAdapter(bankListDetailsAdapter)
    }

    fun updateBankSelection(selectedBankDataSelectionDTO: DataSelectionDTO) {
        bankListDetailsAdapter.dataListFilter!!.forEachIndexed { index, item ->
            run {
                if (selectedBankDataSelectionDTO.value.equals(item.displayValue)) {
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
                hideProgressDialog()
                val addLeadResponse: AddLeadResponse? = mApiResponse.data as AddLeadResponse?
                if (addLeadResponse?.statusCode.equals("200") || addLeadResponse?.mData!! > 0) {
                    caseId = addLeadResponse?.mData.toString()
                    showToast(addLeadResponse!!.message.toString())
                    llBirthDateSection.visibility = View.VISIBLE
                    //Create Request of Add Employment Details
                    addEmploymentDetailsRequest = createAddEmploymentDetailsRequest(addLeadResponse.mData!!)
                    callCustomerDetailsApi(addLeadResponse.mData!!)
                }
                showToast(addLeadResponse?.message.toString())

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()

                val addLeadResponse: AddLeadResponse? = mApiResponse.data as AddLeadResponse?
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
                if (!validateLeadDataResponse?.message.equals("Success")) {
                    validateLeadDataRes = validateLeadDataResponse!!
                    generateAlertDialog()
                }

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()

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

                if (dialogConfilctForAddLead.isShowing)
                    dialogConfilctForAddLead.dismiss()

                showToast(resetJourneyRes?.message.toString())

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()

            }
            else -> {
                showToast("Please enter valid details")
            }
        }

    }


    private fun generateAlertDialog() {

        dialogConfilctForAddLead = Dialog(requireContext())
        dialogConfilctForAddLead.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogConfilctForAddLead.setCancelable(false)
        dialogConfilctForAddLead.setCanceledOnTouchOutside(false)
        dialogConfilctForAddLead.setContentView(R.layout.vtwo_layout_custom_dialog)
        val ivCloseDialog = dialogConfilctForAddLead.findViewById(R.id.ivCloseDialogV2) as ImageView
        val tvAlertDialogText = dialogConfilctForAddLead.findViewById(R.id.tvAlertDialogText) as TextView
        tvAlertDialogText.setText(validateLeadDataRes.message)
        val btnNewFlow = dialogConfilctForAddLead.findViewById(R.id.btnStartNewFlowV2) as Button
        val btnContinueWithOldFlow = dialogConfilctForAddLead.findViewById(R.id.btnExistingFlowV2) as Button

        ivCloseDialog.setOnClickListener {
            dialogConfilctForAddLead.dismiss()
        }

        btnNewFlow.setOnClickListener {
            resetJourney()
            dialogConfilctForAddLead.dismiss()
        }
        btnContinueWithOldFlow.setOnClickListener {
            callCustomerDetailsApi(validateLeadDataRes.oldCustomerId!!.toInt())
            dialogConfilctForAddLead.dismiss()
        }
        dialogConfilctForAddLead.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        dialogConfilctForAddLead.show()

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


    private fun onAddEmploymentDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val addLeadResponse: AddLeadResponse? = mApiResponse.data as AddLeadResponse?
                if (addLeadResponse?.mData!! > 0) {
                    var addEmploymentDetailsId = addLeadResponse?.mData.toString()
                    isEmploymentDataSaved = true
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
                if (dialogConfilctForAddLead.isShowing)
                    dialogConfilctForAddLead.dismiss()
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
                    if (addLeadRequest.Data!!.basicDetails == null) {
                        var basicDetails = BasicDetails()
                        addLeadRequest.Data!!.basicDetails = basicDetails;

                    }
                    addLeadRequest.Data!!.basicDetails!!.FirstName = customerDetailsResponse!!.data!!.basicDetails!!.firstName
                    addLeadRequest.Data!!.basicDetails!!.LastName = customerDetailsResponse!!.data!!.basicDetails!!.lastName
                    addLeadRequest.Data!!.basicDetails!!.Email = customerDetailsResponse!!.data!!.basicDetails!!.email
                    addLeadRequest.Data!!.basicDetails!!.Salutation = customerDetailsResponse!!.data!!.basicDetails!!.salutation

                    etFirstName.setText(customerDetailsResponse!!.data!!.basicDetails!!.firstName)
                    et_last_name.setText(customerDetailsResponse!!.data!!.basicDetails!!.lastName)
                    et_email.setText(customerDetailsResponse!!.data!!.basicDetails!!.email)

                    var salutation = (customerDetailsResponse!!.data!!.basicDetails!!.salutation)

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
                if (!TextUtils.isEmpty(customerDetailsResponse.data!!.basicDetails!!.birthDate)) {
                    birthDateDisplayValue = stringToDateString(customerDetailsResponse.data!!.basicDetails!!.birthDate.toString().subSequence(0, 10) as String, DATE_FORMATE_YYYYMMDD, DATE_FORMATE_DDMMYYYY)
                    birthDateValue = stringToDateString(customerDetailsResponse.data!!.basicDetails!!.birthDate.toString().subSequence(0, 10) as String, DATE_FORMATE_YYYYMMDD, DATE_FORMATE_YYYYMMDD)
                }
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
                    if (!TextUtils.isEmpty(employmentType)) {
                        if (employmentType.equals("Self Employed")) {
                            llWorkExpriance.visibility = View.GONE
                            etWorkExpriance.setText("")
                            llAccoutDetailsSection.visibility = View.VISIBLE
                            tvBankTitle.setText(getString(R.string.primary_bank_account))
                            bankName = customerDetailsResponse.data!!.employmentDetails!!.primaryAccount
                            addEmploymentDetailsRequest.Data!!.employmentDetails!!.PrimaryAccount = bankName
                            isEmploymentDataSaved = true
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
                            isEmploymentDataSaved = true

                        }
                    }
                    //set Bank Details
                    if (!TextUtils.isEmpty(bankName)) {
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
                    if (customerDetailsResponse.data!!.employmentDetails!!.netAnualIncome != null && customerDetailsResponse.data!!.employmentDetails!!.netAnualIncome!! > 0) {
                        llNetIncomeSection.visibility = View.VISIBLE
                        etNetIncome.setText(customerDetailsResponse.data!!.employmentDetails!!.netAnualIncome!!.toInt().toString())
                        addEmploymentDetailsRequest.Data!!.employmentDetails!!.NetAnualIncome = customerDetailsResponse.data!!.employmentDetails!!.netAnualIncome!!.toInt()
                    }


                }
            }
        } catch (e: Exception) {
            Log.d("Err", e.message)
        }

    }

    private fun displayNameLayout() {
        ll_otp_v2.visibility = View.GONE
        llTAndC.visibility = View.GONE

        tv_mobile_num_hint.visibility = View.GONE

        tv_mobile_num_verified.visibility = View.VISIBLE

        llNameAndEmailV2.visibility = View.VISIBLE


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


}