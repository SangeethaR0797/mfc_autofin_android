package v2.view

import android.widget.TextView.OnEditorActionListener
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.mfc.autofin.framework.R
import kotlinx.android.synthetic.main.v2_reg_name_email_layout.*
import kotlinx.android.synthetic.main.vtwo_mobile_num_layout.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import utility.CommonStrings
import utility.Global
import v2.model.dto.AddLeadRequest
import v2.model.dto.DataSelectionDTO
import v2.model.enum_class.ApplicationStatusEnum
import v2.model.request.*
import v2.model.request.add_lead.AddLeadData
import v2.model.request.add_lead.AddLeadVehicleDetails
import v2.model.request.add_lead.BasicDetails
import v2.model.request.update.LeadBasicBasicDetails
import v2.model.request.update.LeadUpdateData
import v2.model.request.update.UpdateLeadBasicDetailsRequest
import v2.model.response.*
import v2.model.response.master.KYCDocumentResponse
import v2.model.response.master.MasterResponse
import v2.model.response.master.Types
import v2.model_view.MasterViewModel
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.CustomAutoTextViewListAdapter
import v2.view.adapter.DataRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.ActivityBackPressed
import v2.view.callBackInterface.DatePickerCallBack
import v2.view.callBackInterface.itemClickCallBack
import v2.view.utility_view.GridItemDecoration
import java.util.*


public class AddLeadDetailsFrag : BaseFragment(), View.OnClickListener,
    KeyboardVisibilityEventListener, ActivityBackPressed {


    private var onClickNext: Boolean = false
    lateinit var validateLeadDataRes: ValidateLeadDataResponse
    lateinit var etMobileNumberV2: EditText
    lateinit var llMobileNum: LinearLayout
    lateinit var ivBackToVehDetails: ImageView
    lateinit var scrollView1: ScrollView
    lateinit var viewEmpty: View
    lateinit var etOTPV2: EditText
    lateinit var llOTPNumInput: LinearLayout
    lateinit var cbTermsAndConditions: CheckBox
    lateinit var tvResendOTPV2: TextView
    lateinit var tvOTPTimerV2: TextView
    lateinit var tvOTPEHint: TextView
    lateinit var textViewTermsAndCondition: TextView
    lateinit var btnMobileNum: Button
    lateinit var ll_otp_v2: LinearLayout
    lateinit var llTAndC: LinearLayout
    lateinit var transactionViewModel: TransactionViewModel
    lateinit var addLeadRequest: AddLeadRequest
    var basicDetails = BasicDetails()
    lateinit var rv_salutation: RecyclerView
    lateinit var masterViewModel: MasterViewModel
    var salutationAdapter: DataRecyclerViewAdapter? = null
    lateinit var llNameAndEmailV2: LinearLayout
    lateinit var etFirstName: EditText
    lateinit var etLastName: EditText
    lateinit var etEmailId: EditText
    var dialogConfilctForAddLead: Dialog? = null
    lateinit var timer: CountDownTimer

    var make: String = ""
    var model: String = ""
    var variant: String = ""
    var mobileNum: String = ""
    var userId: String = ""
    var userType: String = ""
    var salutation: String = ""
    var employmentDetailsAdapter: DataRecyclerViewAdapter? = null
    var commonBankListDetailsAdapter: DataRecyclerViewAdapter? = null

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
    var residenceTypeDetailsAdapter: DataRecyclerViewAdapter? = null
    var residenceYearsAdapter: DataRecyclerViewAdapter? = null

    lateinit var llPanNumberSection: LinearLayout
    lateinit var llPanNumber: LinearLayout
    lateinit var etPanNumber: EditText
    lateinit var tvPanNumberErrorMessage: TextView

    lateinit var addEmploymentDetailsRequest: AddEmploymentDetailsRequest
    lateinit var addResidentDetailsRequest: AddResidentDetailsRequest
    lateinit var customAutoTextViewListAdapter: CustomAutoTextViewListAdapter
    lateinit var customerDetailsResponse: CustomerDetailsResponse

    var isEmploymentDataSaved: Boolean = false
    var isResidentDataSaved: Boolean = false

    private var previousAddEmploymentDetailsRequest: AddEmploymentDetailsRequest? = null
    private var previousAddResidentDetailsRequest: AddResidentDetailsRequest? = null
    lateinit var previousAddLeadRequest: AddLeadRequest

    var rootView: View? = null

    //region fragment core function

    var selectedCustomerIdForEdit: Int = 0
    var selectedCustomerMobileNumberForEdit: String? = null


    //region KeyBoardVisible
    override fun onVisibilityChanged(isKeyBoardVisible: Boolean) {
        if (isKeyBoardVisible) {
            if (viewEmpty.visibility == View.VISIBLE) {
                viewEmpty.visibility = View.GONE
                Thread.sleep(200)
            }

            viewEmpty.visibility = View.VISIBLE

            val view = requireActivity().currentFocus
            if (view != null && view is EditText) {
                var viewToScroll: View? = null

                if (etMobileNumberV2.hasFocus() || etOTPV2.hasFocus()) {
                    viewToScroll = llMobileNum
                } else if (etFirstName.hasFocus() || etLastName.hasFocus() || etEmailId.hasFocus()) {
                    viewToScroll = llNameAndEmailV2
                } else if (etWorkExpriance.hasFocus()) {
                    viewToScroll = llEmploymentSection
                } else if (etNetIncome.hasFocus()) {
                    viewToScroll = llNetIncomeSection
                } else if (etEMI.hasFocus()) {
                    viewToScroll = llEMISection
                } else if (etAutoResidenceCity.hasFocus()) {
                    viewToScroll = llResidenceTypeSection
                } else if (etPanNumber.hasFocus()) {
                    viewToScroll = llPanNumberSection
                }



                if (viewToScroll != null) {
                    scrollToBottom(viewToScroll)
                }

            }

        } else {
            viewEmpty.visibility = View.GONE
            val view = requireActivity().currentFocus
            if (view != null && view is EditText) {
                view.clearFocus()
            }

        }
    }

    //endregion KeyBoardVisible

    override fun onActivityBackPressed() {
        checkBackPress()

    }

    fun checkBackPress() {

        if (TextUtils.isEmpty(customerId)) {
            //Case 1 Able to change mvvm details before Add Lead
            //Case 2 Edit flow user can go back to Application List Page
            if (activity is HostActivity) {
                (activity as HostActivity).activityBackPressed = null
            }
            activity?.onBackPressed()
        } else {
            //Not Able to change mvvm details after Add Lead so User can go back on VehicleSelectionFrag only
            navigateDashboardTop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (activity is HostActivity) {
            (activity as HostActivity).activityBackPressed = AddLeadDetailsFrag@ this
        }
    }

    override fun onStop() {
        super.onStop()
        if (activity is HostActivity) {
            (activity as HostActivity).activityBackPressed = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val safeArgs = AddLeadDetailsFragArgs.fromBundle(it)
            addLeadRequest = safeArgs.addLeadRequestDetails
            selectedCustomerIdForEdit = safeArgs.customerId
            selectedCustomerMobileNumberForEdit = safeArgs.mobile
        }

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

        masterViewModel.getCommonBanksLiveData()
            .observe(this, { mApiResponse: ApiResponse? ->
                onCommonBanksResponse(
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

        transactionViewModel.getAddLeadLiveData()
            .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                onAddLead(
                    mApiResponse!!
                )
            })
        masterViewModel.getKYCDocumentLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onGetKYCDocumentResponse(mApiResponse!!)
            }

        transactionViewModel.getUpdateLeadBasicDetailsLiveData()
            .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                onUpdateAddLeadBasicDetails(
                    mApiResponse!!
                )
            })


        transactionViewModel.getValidateLeadLiveData()
            .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                onValidateLead(
                    mApiResponse!!
                )
            })
        transactionViewModel.getAddEmploymentDetailsLiveData()
            .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                onAddEmploymentDetails(
                    mApiResponse!!
                )
            })
        transactionViewModel.getAddResidentDetailsLiveData()
            .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                onAddResidentDetails(
                    mApiResponse!!
                )
            })
        transactionViewModel.getCustomerDetailsLiveData()
            .observe(requireActivity(), { mApiResponse: ApiResponse? ->
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
        masterViewModel.getCityNameListLiveData()
            .observe(this, { mApiResponse: ApiResponse? ->
                onResidentCityName(
                    mApiResponse!!
                )
            })


        transactionViewModel.getResetCustomerJourneyLiveData().observe(
            requireActivity(),
            { mAPIResponse: ApiResponse? -> onResetJourney(mAPIResponse!!) })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView != null) {
            return rootView
        } else {
            rootView = inflater.inflate(R.layout.fragment_add_lead_details, container, false)


                if (addLeadRequest.Data?.basicDetails == null) {
                    val basicDetails = BasicDetails()
                    addLeadRequest.Data?.basicDetails = basicDetails;

                }

            initViews(rootView)
        }

        return rootView
    }


    private fun initViews(view: View?) {

        setKeyBoardShowHideEvent(AddLeadDetailsFrag@ this)

        scrollView1 = rootView?.findViewById(R.id.scrollView1)!!
        viewEmpty = rootView?.findViewById(R.id.view_empty)!!
        ivBackToVehDetails = rootView?.findViewById(R.id.ivBackToVehDetails)!!
        etMobileNumberV2 = rootView?.findViewById(R.id.etMobileNumberV2)!!
        llMobileNum = rootView?.findViewById(R.id.llMobileNum)!!

        etOTPV2 = rootView?.findViewById(R.id.etOTPV2)!!
        llOTPNumInput = rootView?.findViewById(R.id.llOTPNumInput)!!
        tvOTPEHint = rootView!!.findViewById(R.id.tvOTPEHint)

        cbTermsAndConditions = rootView!!.findViewById(R.id.cbTermsAndConditions)
        tvResendOTPV2 = rootView!!.findViewById(R.id.tvResendOTPV2)
        tvOTPTimerV2 = rootView!!.findViewById(R.id.tvOTPTimerV2)
        btnMobileNum = rootView!!.findViewById(R.id.btnMobileNum)
        ll_otp_v2 = rootView!!.findViewById(R.id.ll_otp_v2)
        ll_otp_v2.visibility = View.GONE
        llTAndC = rootView!!.findViewById(R.id.llTAndC)
        textViewTermsAndCondition = rootView!!.findViewById(R.id.textViewTermsAndCondition)

        rv_salutation = rootView!!.findViewById(R.id.rv_salutation)
        llNameAndEmailV2 = rootView!!.findViewById(R.id.llNameAndEmailV2)
        etFirstName = rootView!!.findViewById(R.id.et_first_name)
        etLastName = rootView!!.findViewById(R.id.et_last_name)
        etEmailId = rootView!!.findViewById(R.id.et_email)
        ll_otp_v2.visibility = View.GONE
        llTAndC = rootView!!.findViewById(R.id.llTAndC)


        make = addLeadRequest.Data?.addLeadVehicleDetails?.Make.toString()
        model = addLeadRequest.Data?.addLeadVehicleDetails?.Model.toString()
        variant = addLeadRequest.Data?.addLeadVehicleDetails?.Variant.toString()

        llBirthDate = rootView!!.findViewById(R.id.ll_date)
        etBirthDate = rootView!!.findViewById(R.id.et_date)
        tvBirthErrorMessage = rootView!!.findViewById(R.id.tv_birth_error_message)
        tvBirthErrorMessage.visibility = View.GONE


        rvEmploymentType = rootView!!.findViewById(R.id.rv_employment_type)
        llWorkExpriance = rootView!!.findViewById(R.id.ll_work_expriance)
        etWorkExpriance = rootView!!.findViewById(R.id.et_work_expriance)
        llAddWorkExpriance = rootView!!.findViewById(R.id.ll_add_work_expriance)
        tvWorkExprianceErrorMessage = rootView!!.findViewById(R.id.tv_work_expriance_error_message)
        tvWorkExprianceErrorMessage.visibility = View.GONE

        llBirthDateSection = rootView!!.findViewById(R.id.ll_birth_date_section)
        llEmploymentSection = rootView!!.findViewById(R.id.ll_employment_section)
        llAccoutDetailsSection = rootView!!.findViewById(R.id.ll_accout_details_section)

        etSearchBank = rootView!!.findViewById(R.id.et_search_bank)
        etSearchBank.isFocusable = false
        rvBankList = rootView!!.findViewById(R.id.rv_bank_list)
        tvBankTitle = rootView!!.findViewById(R.id.tv_bank_title)
        llAddSearchBank = rootView!!.findViewById(R.id.ll_search_bank)

        cbMoreThanOneYearInCurrentOrganization =
            rootView!!.findViewById(R.id.cb_more_than_one_year_in_current_organization)
        cbMoreThanOneYearInCurrentOrganization.visibility = View.GONE

        rlEditYearOfExperience = rootView!!.findViewById(R.id.rl_edit_year_of_experience)
        rlEditYearOfExperience.visibility = View.GONE

        llNetIncomeSection = rootView!!.findViewById(R.id.ll_net_income_section)
        llNetIncome = rootView!!.findViewById(R.id.ll_net_income)
        etNetIncome = rootView!!.findViewById(R.id.et_net_income)
        tvNetIncomeErrorMessage = rootView!!.findViewById(R.id.tv_net_income_error_message)
        tvNetIncomeInWords = rootView!!.findViewById(R.id.tv_net_income_in_words)

        tvNetIncomeErrorMessage.visibility = View.GONE

        llEMISection = rootView!!.findViewById(R.id.ll_emi_section)
        llEMI = rootView!!.findViewById(R.id.ll_emi)
        etEMI = rootView!!.findViewById(R.id.et_emi)
        tvEMIErrorMessage = rootView!!.findViewById(R.id.tv_emi_error_message)
        tvEMIInWords = rootView!!.findViewById(R.id.tv_emi_in_words)
        rvEMIList = rootView!!.findViewById(R.id.rv_emi_list)
        llEmiDetails = rootView!!.findViewById(R.id.ll_emi_details)
        llEmiDetails.visibility = View.GONE

        tvEMIErrorMessage.visibility = View.GONE


        llResidenceTypeSection = rootView!!.findViewById(R.id.ll_residence_type_section)
        llResidenceType = rootView!!.findViewById(R.id.ll_residence_type)
        etAutoResidenceCity = rootView!!.findViewById(R.id.et_residence_type)
        tvResidenceTypeErrorMessage = rootView!!.findViewById(R.id.tv_residence_type_error_message)

        rvResidenceTypeList = rootView!!.findViewById(R.id.rv_residence_type_list)
        rvResidenceYears = rootView!!.findViewById(R.id.rv_residence_year)
        llResidenceTypeDetails = rootView!!.findViewById(R.id.ll_residence_type_details)

        tvResidenceTypeErrorMessage.visibility = View.GONE

        llPanNumberSection = rootView!!.findViewById(R.id.ll_pan_number_section)
        llPanNumber = rootView!!.findViewById(R.id.ll_pan_number)
        etPanNumber = rootView!!.findViewById(R.id.et_pan_number)
        tvPanNumberErrorMessage = rootView!!.findViewById(R.id.tv_pan_number_error_message)


        tvPanNumberErrorMessage.visibility = View.GONE

        val ss = SpannableString(getString(R.string.vtwo_t_and_c_hint))
        val span1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                openWebViewActivity("Terms And Conditions", CommonStrings.TERMS_AND_CONDITION_URL)
            }
        }

        val span2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                openWebViewActivity("Privacy Policy", CommonStrings.PRIVACY_AND_POLICY_URL)
            }
        }
        ss.setSpan(span1, 15, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(span2, 40, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textViewTermsAndCondition.text = ss
        textViewTermsAndCondition.movementMethod = LinkMovementMethod.getInstance()


        etMobileNumberV2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length != 10) {
                    if (tvOTPTimerV2.text != getString(R.string.otp_timer_hint)) {
                        makeOTPLayoutInvisible()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

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
        llResidenceTypeSection.visibility = View.GONE
        llPanNumberSection.visibility = View.GONE

        setTextChangedFirstName()
        setTextChangedLastName()
        setTextChangedEmailId()
        setTextChangeOfWorkExpirance()
        setCheckBoxEvent()
        setTextChangeOfNetIncome()
        setTextChangeOfEMI()
        setTextChangeOfetAutoResidenceCity()
        setTextChangeOfPanNumber()

        setSalutaionAdapterData();
        setEploymentDetailsAdapter()
        setBankDetailsAdapter()
        setEMIDetailsAdapter()
        setResidenceTypeAdapter()
        setResidenceYearsAdapter()

        if (selectedCustomerIdForEdit > 0 && selectedCustomerIdForEdit != null) {
            customerId = selectedCustomerIdForEdit.toString()
            setDataForEditFlow()
        }


    }

    fun setDataForEditFlow() {

        showProgressDialog(requireContext())

        if (salutationAdapter != null && salutationAdapter?.itemCount!! > 0 && employmentDetailsAdapter != null && employmentDetailsAdapter?.itemCount!! > 0
            && commonBankListDetailsAdapter != null && commonBankListDetailsAdapter?.itemCount!! > 0 && residenceTypeDetailsAdapter != null && residenceTypeDetailsAdapter?.itemCount!! > 0
            && residenceYearsAdapter != null && residenceYearsAdapter?.itemCount!! > 0
        ) {
            displayNameLayout()
            etMobileNumberV2.setText(selectedCustomerMobileNumberForEdit)
            callCustomerDetailsApi(selectedCustomerIdForEdit)
            etMobileNumberV2.setTextColor(resources.getColor(R.color.black))
        } else {
            Log.d("EditWait", "EditWait")
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    ThreadUtils.runOnUiThread(Runnable {
                        setDataForEditFlow()
                    });

                }
            }, 3000)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonStrings.MASTER_DETAIL_ACTIVITY_REQUEST_CODE && resultCode == CommonStrings.RESULT_CODE) {
            var dataSelectionDTO: DataSelectionDTO? =
                data?.getParcelableExtra(CommonStrings.SELECTED_DATA)
            var dataType: String? = data?.getStringExtra(CommonStrings.SELECTED_DATA_TYPE)
            if (dataType.equals(CommonStrings.BANK_DATA_CALL)) {
                etSearchBank.setText(dataSelectionDTO?.displayValue)
                if (addEmploymentDetailsRequest.Data?.employmentDetails?.EmploymentType.equals(
                        "Self Employed"
                    )
                ) {
                    addEmploymentDetailsRequest.Data?.employmentDetails?.PrimaryAccount =
                        dataSelectionDTO?.displayValue
                    addEmploymentDetailsRequest.Data?.employmentDetails?.SalaryAccount = null
                } else {
                    addEmploymentDetailsRequest.Data?.employmentDetails?.PrimaryAccount = null
                    addEmploymentDetailsRequest.Data?.employmentDetails?.SalaryAccount =
                        dataSelectionDTO?.displayValue
                }
                llNetIncomeSection.visibility = View.VISIBLE

                updateBankSelection(dataSelectionDTO?.displayValue!!)
            }


        }
    }

    //endregion fragment core function

    //region Object request clone function
    private fun createRequestCloneOfAddLead() {
        //Create AddLead request Copy
        val addLeadBasicDetails = BasicDetails()
        val addLeadVehicleDetails = AddLeadVehicleDetails()

        if (addLeadRequest.Data?.basicDetails != null) {
            addLeadBasicDetails.FirstName = addLeadRequest.Data?.basicDetails?.FirstName
            addLeadBasicDetails.LastName = addLeadRequest.Data?.basicDetails?.LastName
            addLeadBasicDetails.Email = addLeadRequest.Data?.basicDetails?.Email
            addLeadBasicDetails.Salutation = addLeadRequest.Data?.basicDetails?.Salutation
            addLeadBasicDetails.CustomerMobile =
                addLeadRequest.Data?.basicDetails?.CustomerMobile
        }
        if (addLeadRequest.Data?.addLeadVehicleDetails != null) {
            addLeadVehicleDetails.Make = addLeadRequest.Data?.addLeadVehicleDetails?.Make
            addLeadVehicleDetails.Model = addLeadRequest.Data?.addLeadVehicleDetails?.Model
            addLeadVehicleDetails.Variant = addLeadRequest.Data?.addLeadVehicleDetails?.Variant
            addLeadVehicleDetails.VehicleNumber =
                addLeadRequest.Data?.addLeadVehicleDetails?.VehicleNumber
            addLeadVehicleDetails.FuelType = addLeadRequest.Data?.addLeadVehicleDetails?.FuelType
            addLeadVehicleDetails.RegistrationYear =
                addLeadRequest.Data?.addLeadVehicleDetails?.RegistrationYear
            addLeadVehicleDetails.KMs = addLeadRequest.Data?.addLeadVehicleDetails?.KMs
            addLeadVehicleDetails.Ownership =
                addLeadRequest.Data?.addLeadVehicleDetails?.Ownership
            addLeadVehicleDetails.VehicleSellingPrice =
                addLeadRequest.Data?.addLeadVehicleDetails?.VehicleSellingPrice
        }

        previousAddLeadRequest = AddLeadRequest(
            addLeadRequest.UserId,
            addLeadRequest.UserType,
            AddLeadData(addLeadBasicDetails, addLeadVehicleDetails)
        )

    }

    private fun createRequestCloneAddEmploymentEmploymentDetails() {
        //Create Copy of Employment request object
        val employment = AddEmploymentEmploymentDetails(
            addEmploymentDetailsRequest.Data?.employmentDetails?.CurrentCompanyExpMoreThanOne,
            addEmploymentDetailsRequest.Data?.employmentDetails?.EmploymentType,
            addEmploymentDetailsRequest.Data?.employmentDetails?.NetAnualIncome,
            addEmploymentDetailsRequest.Data?.employmentDetails?.PrimaryAccount,
            addEmploymentDetailsRequest.Data?.employmentDetails?.SalaryAccount,
            addEmploymentDetailsRequest.Data?.employmentDetails?.TotalWorkExperience
        )
        val personal =
            AddEmploymentPersonalDetails(addEmploymentDetailsRequest.Data?.personalDetails?.BirthDate)
        val employmentData =
            AddEmploymentData(addEmploymentDetailsRequest.Data?.CustomerId, employment, personal)
        previousAddEmploymentDetailsRequest = AddEmploymentDetailsRequest(
            employmentData,
            addEmploymentDetailsRequest.UserId,
            addEmploymentDetailsRequest.UserType
        )

    }

    private fun createRequestCloneAddResidentDetails() {
        //Create Copy of Resident request object
        val residentPersonal = ResidentDetailsDataPersonalDetails(
            addResidentDetailsRequest.Data?.personalDetails?.HaveExistingEMI,
            addResidentDetailsRequest.Data?.personalDetails?.PANNumber,
            addResidentDetailsRequest.Data?.personalDetails?.TotalEMI
        )
        val resident = ResidentDetailsDataResidentialDetails(
            addResidentDetailsRequest.Data?.residentialDetails?.CustomerCity,
            addResidentDetailsRequest.Data?.residentialDetails?.NoOfYearInResident,
            addResidentDetailsRequest.Data?.residentialDetails?.ResidenceType
        )
        val residentData = ResidentDetailsData(
            addResidentDetailsRequest.Data?.CustomerId,
            residentPersonal,
            resident
        )
        previousAddResidentDetailsRequest = AddResidentDetailsRequest(
            residentData,
            addResidentDetailsRequest.UserId,
            addResidentDetailsRequest.UserType
        )
    }
    //endregion Object request clone function

    //region custom function
    private fun displayNameLayout() {
        ll_otp_v2.visibility = View.GONE
        llTAndC.visibility = View.GONE

        tv_mobile_num_hint.visibility = View.GONE


        etMobileNumberV2.isFocusable = false;
        etMobileNumberV2.isEnabled = false;
        etMobileNumberV2.inputType = InputType.TYPE_NULL;


        tv_mobile_num_verified.visibility = View.VISIBLE

        llNameAndEmailV2.visibility = View.VISIBLE


    }

    private fun setCheckBoxEvent() {
        cbMoreThanOneYearInCurrentOrganization.setOnCheckedChangeListener(null)
        cbMoreThanOneYearInCurrentOrganization.setOnClickListener {
            addEmploymentDetailsRequest.Data?.employmentDetails?.CurrentCompanyExpMoreThanOne =
                cbMoreThanOneYearInCurrentOrganization.isChecked
        }

    }

    private fun checkForNavToSoftOffer() {
        hideProgressDialog()
        if (!TextUtils.isEmpty(customerId) && isEmploymentDataSaved &&
            isResidentDataSaved &&
            addLeadRequest.hashCode() == previousAddLeadRequest.hashCode()
            && addEmploymentDetailsRequest.hashCode() == previousAddEmploymentDetailsRequest.hashCode()
            && addResidentDetailsRequest.hashCode() == previousAddResidentDetailsRequest.hashCode()
        ) {
            navToSoftOffer(customerDetailsResponse, customerId, CommonStrings.ADD_LEAD_FRAGMENT_TAG)
        }
    }

    fun scrollToBottom(nextView: View?) {
        if (nextView != null) {
            scrollView1.post {
                // scrollView1.fullScroll(View.FOCUS_DOWN)
                scrollView1.scrollTo(0, nextView.top);
            }
        }
    }


    private fun openDatePicker() {
        var lastSelectedDate: String? = null
        if (addEmploymentDetailsRequest.Data != null && addEmploymentDetailsRequest.Data?.personalDetails != null && addEmploymentDetailsRequest.Data!!.personalDetails!!.BirthDate != null) {
            lastSelectedDate = addEmploymentDetailsRequest.Data?.personalDetails?.BirthDate
        }
        object : DatePickerCallBack {
            override fun dateSelected(dateDisplayValue: String, dateValue: String) {
                addEmploymentDetailsRequest.Data?.personalDetails?.BirthDate = dateValue
                etBirthDate.setText(dateDisplayValue)
                tvBirthErrorMessage.visibility = View.GONE
                llBirthDate.setBackgroundResource(R.drawable.vtwo_input_bg)
                etBirthDate.setTextColor(resources.getColor(R.color.vtwo_black))
                llEmploymentSection.visibility = View.VISIBLE
                scrollToBottom(llEmploymentSection)
            }
        }.callDatePickerDialog(
                lastSelectedDate,
                null,
                getBackDateFromTodayDate(18))
    }

    private fun sendOTP() {
        if (etMobileNumberV2.text.length == 10 && validMobileNum()) {

            transactionViewModel.generateOTP(
                getOtpRequest(
                    null,
                    etMobileNumberV2.text.toString()
                ), Global.customerAPI_BaseURL + CommonStrings.OTP_URL_END
            )

            etMobileNumberV2.setTextColor(resources.getColor(R.color.black))
            tv_mobile_num_hint.setTextColor(resources.getColor(R.color.vtwo_light_grey))
            tv_mobile_num_hint.visibility = View.GONE
            llMobileNumInput.setBackgroundResource(R.drawable.vtwo_input_bg)
        } else {
            if (etMobileNumberV2.text.isEmpty()) {
                tv_mobile_num_hint.text = "Please enter Mobile number"
            } else {
                etMobileNumberV2.setTextColor(resources.getColor(R.color.error_red))
                tv_mobile_num_hint.text = "Please enter Valid mobile number"
            }
            tv_mobile_num_hint.setTextColor(resources.getColor(R.color.error_red))
            llMobileNumInput.setBackgroundResource(R.drawable.v2_error_layout_bg)


        }
    }

    private fun validMobileNum(): Boolean {
        val mobNum = etMobileNumberV2.text.toString()
        return mobNum[0] == '6' || mobNum[0] == '7' || mobNum[0] == '8' || mobNum[0] == '9'
    }

    private fun validateOTP() {
        if (etOTPV2.text.length == 6) {
            showProgressDialog(requireContext())
            transactionViewModel.validateOTP(
                getOtpRequest(
                    etOTPV2.text.toString(),
                    etMobileNumberV2.text.toString()
                ), Global.customerAPI_BaseURL + CommonStrings.VALIDATE_OTP_URL_END
            )
            tvOTPEHint.visibility = View.GONE
            llOTPNumInput.setBackgroundResource(R.drawable.vtwo_input_bg)


        } else {
            tvOTPEHint.visibility = View.VISIBLE
            llOTPNumInput.setBackgroundResource(R.drawable.v2_error_layout_bg)
        }

    }

    private fun addLead() {
        if (!TextUtils.isEmpty(salutation) && !TextUtils.isEmpty(etFirstName.text.toString())
            && !TextUtils.isEmpty(etLastName.text.toString())
            && !TextUtils.isEmpty(etEmailId.text.toString())
        ) {

            ll_first_name_input.setBackgroundResource(R.drawable.vtwo_input_bg)
            tv_fname_hint.visibility = View.GONE

            ll_last_name_input.setBackgroundResource(R.drawable.vtwo_input_bg)
            tv_lname_hint.visibility = View.GONE

            ll_last_email_input.background = resources.getDrawable(R.drawable.vtwo_input_bg)
            tv_email_hint.visibility = View.GONE

            basicDetails.FirstName = etFirstName.text.toString()
            basicDetails.LastName = etLastName.text.toString()
            val email = etEmailId.text.toString()
            if (Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches() && isEmailValid(email) && email.substring(
                    email.length - 3,
                    email.length
                ) != "web"
            ) {
                setValidEmailUI(true)
                basicDetails.Email = etEmailId.text.toString()
                if (TextUtils.isEmpty(basicDetails.Salutation)) {
                    basicDetails.Salutation = salutation
                }
                addLeadRequest.Data?.basicDetails = basicDetails
                addLeadRequest.UserType = CommonStrings.USER_TYPE
                addLeadRequest.UserId = CommonStrings.DEALER_ID


                callAddLeadApi()

            } else {
                setValidEmailUI(false)
                tv_email_hint.text = "Please enter Valid Email ID"
            }
        } else {
            if (etFirstName.text.isEmpty() || addLeadRequest.Data?.basicDetails?.FirstName?.isEmpty() == true) {
                ll_first_name_input.setBackgroundResource(R.drawable.v2_error_layout_bg)
                tv_fname_hint.visibility = View.VISIBLE
            } else {
                ll_first_name_input.setBackgroundResource(R.drawable.vtwo_input_bg)
                tv_fname_hint.visibility = View.GONE
            }
            if (etLastName.text.isEmpty() || addLeadRequest.Data?.basicDetails?.LastName?.isEmpty() == true) {
                ll_last_name_input.setBackgroundResource(R.drawable.v2_error_layout_bg)
                tv_lname_hint.visibility = View.VISIBLE

            } else {
                ll_last_name_input.setBackgroundResource(R.drawable.vtwo_input_bg)
                tv_lname_hint.visibility = View.GONE
            }

            if (etEmailId.text.isEmpty() || addLeadRequest.Data?.basicDetails?.Email?.isEmpty() == true) {
                setValidEmailUI(false)
                tv_email_hint.text = "Please enter Email ID"

            } else {
                setValidEmailUI(true)
            }
            if (salutation.isEmpty() || addLeadRequest.Data?.basicDetails?.Salutation?.isEmpty() == true) {
                showToast("Please select Salutation")
            }
        }
    }

    private fun setValidEmailUI(isTrue: Boolean) {

        if (isTrue) {
            ll_last_email_input.setBackgroundResource(R.drawable.vtwo_input_bg)
            tv_email_hint.visibility = View.GONE
            etEmailId.setTextColor(resources.getColor(R.color.vtwo_black))
        } else {
            ll_last_email_input.setBackgroundResource(R.drawable.v2_error_layout_bg)
            etEmailId.setTextColor(resources.getColor(R.color.error_red))
            tv_email_hint.visibility = View.VISIBLE
        }

    }

    private fun enableTimer() {
        timer = object : CountDownTimer(60000, 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                tvOTPTimerV2.text = "" + millisUntilFinished / 1000 + " Sec"
                onClickNext = false
            }

            override fun onFinish() {
                tvOTPTimerV2.text = "0 Sec"
                timer.cancel()
                if (!onClickNext) {
                    showToast("Your verification code got expired, Please click on Resend verification code to get the new one.")
                }
            }
        }.start()

    }

    fun updateBankSelection(selectedBankDisplayName: String) {
        var isFound: Boolean = false
        commonBankListDetailsAdapter?.dataListFilter?.forEachIndexed { index, item ->
            run {
                val previousSelectedValue = item.selected

                if (selectedBankDisplayName.equals(item.displayValue)) {
                    item.selected = true
                    isFound = true
                    if (addEmploymentDetailsRequest.Data?.employmentDetails?.EmploymentType.equals(
                            "Self Employed"
                        )
                    ) {
                        addEmploymentDetailsRequest.Data?.employmentDetails?.PrimaryAccount =
                            item.displayValue
                        addEmploymentDetailsRequest.Data?.employmentDetails?.SalaryAccount = null
                    } else {
                        addEmploymentDetailsRequest.Data?.employmentDetails?.PrimaryAccount = null
                        addEmploymentDetailsRequest.Data?.employmentDetails?.SalaryAccount =
                            item.displayValue
                    }
                    llNetIncomeSection.visibility = View.VISIBLE


                } else {
                    item.selected = false
                }
                if (previousSelectedValue != item.selected) {
                    commonBankListDetailsAdapter?.notifyItemChanged(index)
                }

            }
        }

        //commonBankListDetailsAdapter!!.notifyDataSetChanged()
        scrollToBottom(llAccoutDetailsSection)
        if (!isFound) {
            etSearchBank.setText(selectedBankDisplayName)
        }

    }

    //endregion custom function

    //region Transaction Api Call
    private fun checkValidLead() {

        transactionViewModel.validateLead(
            getValidateLeadReq(),
            Global.customerAPI_BaseURL + CommonStrings.VALIDATE_LEAD
        )

    }

    private fun callAddLeadApi() {
        transactionViewModel.addLead(
            addLeadRequest,
            Global.customerAPI_BaseURL + CommonStrings.ADD_LEAD_URL_END
        )
    }

    private fun callUpdateAddLeadBasicDetailsAPIApi() {
        transactionViewModel.updateLeadBasicDetails(
            getUpdateLeadBasicDetailsRequest(),
            Global.customerAPI_BaseURL + CommonStrings.UPDATE_LEAD_BASIC_DETAILS_URL_END
        )
    }

    private fun callAddEmploymentDetails() {
        transactionViewModel.addEmploymentDetails(
            addEmploymentDetailsRequest,
            Global.customerAPI_BaseURL + CommonStrings.ADD_EMPLOYMENT_URL_END
        )
    }

    private fun callAddResidentDetails() {
        transactionViewModel.addResidentDetails(
            addResidentDetailsRequest,
            Global.customerAPI_BaseURL + CommonStrings.ADD_RESIDENT_URL_END
        )
    }

    fun callCustomerDetailsApi(customerIdValue: Int) {
        customerId = customerIdValue.toString()

        addEmploymentDetailsRequest = createAddEmploymentDetailsRequest(customerIdValue)
        addResidentDetailsRequest = createAddResidentDetailsRequest(customerIdValue)
        transactionViewModel.getCustomerDetails(
            createCustomerDetailsRequest(customerIdValue),
            Global.customerAPI_BaseURL + CommonStrings.CUSTOMER_DETAILS_END_URL
        )

    }

    private fun resetJourney() {

        transactionViewModel.resetCustomerJourney(
            getCustomerRequest(),
            Global.customerAPI_BaseURL + CommonStrings.RESET_JOURNEY
        )
    }
    //endregion Transaction Api Call


    //region setTextChangeEvent
    fun setTextChangedFirstName() {
        etFirstName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (TextUtils.isEmpty(etFirstName.text)) {
                    addLeadRequest.Data?.basicDetails?.FirstName = null
                } else {
                    addLeadRequest.Data?.basicDetails?.FirstName = etFirstName.text.toString()
                }

            }
        })
    }

    fun setTextChangedLastName() {
        etLastName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (TextUtils.isEmpty(etLastName.text)) {
                    addLeadRequest.Data?.basicDetails?.LastName = null
                } else {
                    addLeadRequest.Data?.basicDetails?.LastName = etLastName.text.toString()
                }

            }
        })
    }

    private fun setTextChangedEmailId() {
        etEmailId.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (TextUtils.isEmpty(etLastName.text)) {
                    addLeadRequest.Data?.basicDetails?.Email = ""
                } else {
                    setValidEmailUI(true)
                    addLeadRequest.Data?.basicDetails?.Email = etEmailId.text.toString()
                }

            }
        })
    }

    fun setTextChangeOfWorkExpirance() {
        var timerWorkExpirance: Timer? = null
        etWorkExpriance.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tvWorkExprianceErrorMessage.visibility = View.GONE
                llAddWorkExpriance.setBackgroundResource(R.drawable.vtwo_input_bg)
                etWorkExpriance.setTextColor(resources.getColor(R.color.vtwo_black))
                if (TextUtils.isEmpty(etWorkExpriance.text) || etWorkExpriance.text.toString() == "0"
                ) {
                    addEmploymentDetailsRequest.Data?.employmentDetails?.TotalWorkExperience =
                        "0"
                    cbMoreThanOneYearInCurrentOrganization.isChecked = false

                    cbMoreThanOneYearInCurrentOrganization.visibility = View.GONE
                    addEmploymentDetailsRequest.Data?.employmentDetails?.CurrentCompanyExpMoreThanOne =
                        false
                } else {
                    addEmploymentDetailsRequest.Data?.employmentDetails?.TotalWorkExperience =
                        etWorkExpriance.text.toString().toInt().toString()
                    cbMoreThanOneYearInCurrentOrganization.visibility = View.VISIBLE
                }

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (timerWorkExpirance != null) {
                    timerWorkExpirance?.cancel();

                }



                timerWorkExpirance = Timer()
                timerWorkExpirance?.schedule(object : TimerTask() {
                    override fun run() {
                        if (etWorkExpriance.text.toString().equals("0")) {
                            ThreadUtils.runOnUiThread(Runnable {
                                showToast("Please enter work experience more than 0 year.")
                                etWorkExpriance.setText("")
                            })

                        }
                    }
                }, 600)

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

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                try {


                    if (timerNeftIncome != null) {
                        timerNeftIncome?.cancel();

                    }
                    if (unformatAmount(etNetIncome.text.toString()) != addEmploymentDetailsRequest.Data?.employmentDetails?.NetAnualIncome.toString() ||
                            TextUtils.isEmpty(etNetIncome.text.toString())
                    ) {
                        allowEdit = true
                    }

                    if (allowEdit) {
                        timerNeftIncome = Timer()
                        timerNeftIncome?.schedule(object : TimerTask() {
                            override fun run() {

                                if (TextUtils.isEmpty(etNetIncome.text)) {
                                    //Set Null Income
                                    addEmploymentDetailsRequest.Data?.employmentDetails?.NetAnualIncome =
                                        0
                                } else {
                                    //Set Income
                                    try {
                                        addEmploymentDetailsRequest.Data?.employmentDetails?.NetAnualIncome =
                                            unformatAmount(etNetIncome.text.toString()).toInt()
                                    } catch (e: Exception) {

                                    } catch (e: NumberFormatException) {

                                    }

                                }
                                allowEdit = false
                                ThreadUtils.runOnUiThread(Runnable {

                                    if (!TextUtils.isEmpty(etNetIncome.text.toString())) {
                                        etNetIncome.setText(formatAmount(unformatAmount(etNetIncome.text.toString())))
                                        tvNetIncomeInWords.text =
                                            (getAmountInWords(unformatAmount(etNetIncome.text.toString())))
                                        etNetIncome.setSelection(etNetIncome.text.toString().length)
                                    } else {
                                        tvNetIncomeInWords.text = ""
                                    }
                                })


                            }
                        }, 600)
                    } else {
                        tvNetIncomeInWords.text = ""
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

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                try {
                    if (timerEMI != null) {
                        timerEMI?.cancel();

                    }
                    if (!unformatAmount(etEMI.text.toString()).equals(addResidentDetailsRequest.Data?.personalDetails?.TotalEMI.toString()) || TextUtils.isEmpty(
                            etEMI.text.toString()
                        )
                    ) {
                        allowEdit = true
                    }
                    if (allowEdit == true) {
                        timerEMI = Timer()
                        timerEMI?.schedule(object : TimerTask() {
                            override fun run() {

                                if (TextUtils.isEmpty(etEMI.text)) {
                                    //Set Null Income
                                    addResidentDetailsRequest.Data?.personalDetails?.TotalEMI = 0
                                } else {
                                    //Set Null Income
                                    addResidentDetailsRequest.Data?.personalDetails?.TotalEMI =
                                        unformatAmount(etEMI.text.toString()).toString().toInt()

                                }
                                allowEdit = false
                                ThreadUtils.runOnUiThread(Runnable {

                                    if (!TextUtils.isEmpty(etEMI.text.toString())) {
                                        etEMI.setText(formatAmount(unformatAmount(etEMI.text.toString())))
                                        tvEMIInWords.text =
                                            (getAmountInWords(unformatAmount(etEMI.text.toString())))
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

    fun setTextChangeOfetAutoResidenceCity() {
        var timerCity: Timer? = null
        var allowEditCity: Boolean = true
        etAutoResidenceCity.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                llResidenceType.setBackgroundResource(R.drawable.vtwo_input_bg)
                etAutoResidenceCity.setTextColor(resources.getColor(R.color.vtwo_black))
                tvResidenceTypeErrorMessage.visibility = View.GONE
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                try {
                    if (timerCity != null) {
                        timerCity?.cancel();

                    }
                    allowEditCity = true
                    if (TextUtils.isEmpty(etAutoResidenceCity.text.toString())) {
                        allowEditCity = true
                    }
                    if (allowEditCity == true) {
                        timerCity = Timer()
                        timerCity?.schedule(object : TimerTask() {
                            override fun run() {


                                allowEditCity = false
                                ThreadUtils.runOnUiThread(Runnable {
                                    //call Search City
                                    if (!TextUtils.isEmpty(etAutoResidenceCity.text.toString())) {
                                        masterViewModel.getCityNameList(Global.customerDetails_BaseURL + CommonStrings.CITY_SEARCH_VIA_TEXT_END_POINT + etAutoResidenceCity.text.toString())
                                    }
                                });


                            }
                        }, 600)
                    } else {

                    }
                } catch (e: Exception) {

                }
            }
        })

        etAutoResidenceCity.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard(etAutoResidenceCity)
            }
            false
        })
    }

    fun setTextChangeOfPanNumber() {

        etPanNumber.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                llPanNumber.setBackgroundResource(R.drawable.vtwo_input_bg)
                etPanNumber.setTextColor(resources.getColor(R.color.vtwo_black))
                tvPanNumberErrorMessage.visibility = View.GONE
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                addResidentDetailsRequest.Data?.personalDetails?.PANNumber =
                    etPanNumber.text.toString()
            }
        })
    }

    //endregion setTextChangeEvent


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.ivBackToVehDetails -> {
                checkBackPress()
            }

            R.id.et_search_bank -> {
                navigateMasterDataSelectionActivity(
                    CommonStrings.MASTER_DETAIL_ACTIVITY_REQUEST_CODE,
                    CommonStrings.BANK_DATA_CALL
                )
            }

            R.id.ll_search_bank -> {
                navigateMasterDataSelectionActivity(
                    CommonStrings.MASTER_DETAIL_ACTIVITY_REQUEST_CODE,
                    CommonStrings.BANK_DATA_CALL
                )
            }
            R.id.ll_residence_type -> {
                if (etAutoResidenceCity.adapter != null) {
                    etAutoResidenceCity.showDropDown()
                }
            }
            R.id.btnMobileNum -> {

                hideSoftKeyboard()
                if (!hasConnectivityNetwork()) {
                    return
                }
                when {
                    //Step 1 Call Send OTP Api
                    ll_otp_v2.visibility == View.GONE && llNameAndEmailV2.visibility == View.GONE -> {
                        sendOTP()
                    }
                    //Step 2 Open View to Enter OTP
                    ll_otp_v2.visibility == View.VISIBLE && llNameAndEmailV2.visibility == View.GONE -> {

                        if (cbTermsAndConditions.isChecked) {
                            onClickNext = true
                            timer.onFinish() //disable timer
                            validateOTP()    //Validate OTP and Validate Lead

                        } else
                            showToast("Please check Terms and Condition")
                    }
                    //Step 3 Call AddLead API
                    TextUtils.isEmpty(customerId) && llNameAndEmailV2.visibility == View.VISIBLE -> {
                        addLead()
                    }
                    //Step 4 Birth Date Validation
                    llBirthDateSection.visibility == View.GONE -> {
                        llBirthDateSection.visibility = View.VISIBLE
                        scrollToBottom(llBirthDateSection)
                    }
                    llBirthDateSection.visibility == View.VISIBLE && TextUtils.isEmpty(etBirthDate.text) -> {
                        tvBirthErrorMessage.visibility = View.VISIBLE
                        tvBirthErrorMessage.text = "Please add date of birth."
                        llBirthDate.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etBirthDate.setTextColor(resources.getColor(R.color.error_red))
                    }
                    //Step 5 Open Employment Section
                    llEmploymentSection.visibility == View.GONE -> {
                        llEmploymentSection.visibility = View.VISIBLE
                        scrollToBottom(llEmploymentSection)
                    }
                    //Employment type validtaion
                    llEmploymentSection.visibility == View.VISIBLE && addEmploymentDetailsRequest.Data!!.employmentDetails!!.EmploymentType == null -> {
                        showToast("Please select employment type")
                        scrollToBottom(llEmploymentSection)
                    }

                    //Step 6 Validate Employment Details
                    llEmploymentSection.visibility == View.VISIBLE && llWorkExpriance.visibility == View.VISIBLE && TextUtils.isEmpty(
                        etWorkExpriance.text
                    ) -> {
                        tvWorkExprianceErrorMessage.visibility = View.VISIBLE
                        tvWorkExprianceErrorMessage.text =
                            "Please enter total years of work experiences."
                        llAddWorkExpriance.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etWorkExpriance.setTextColor(resources.getColor(R.color.error_red))
                        scrollToBottom(llEmploymentSection)

                    }

                    //Step 7 Open Account Details Section
                    llAccoutDetailsSection.visibility == View.GONE -> {
                        llAccoutDetailsSection.visibility = View.VISIBLE
                        scrollToBottom(llAccoutDetailsSection)
                    }

                    //Bank validtion
                    llAccoutDetailsSection.visibility == View.VISIBLE && (addEmploymentDetailsRequest.Data!!.employmentDetails!!.PrimaryAccount == null && addEmploymentDetailsRequest.Data!!.employmentDetails!!.SalaryAccount == null) -> {
                        showToast(
                            "Please select " + tvBankTitle.text.toString().toLowerCase() + "."
                        )
                        scrollToBottom(llAccoutDetailsSection)
                    }
                    llNetIncomeSection.visibility == View.GONE -> {
                        llNetIncomeSection.visibility = View.VISIBLE
                        scrollToBottom(llNetIncomeSection)
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
                        callAddEmploymentDetails()
                    }
                    //Step 10 Open EMI Section
                    llEMISection.visibility == View.GONE -> {
                        llEMISection.visibility = View.VISIBLE
                        scrollToBottom(llEMISection)
                    }
                    //Step 11 Open EMI Section Validate EMI Details
                    llEMISection.visibility == View.VISIBLE && llEmiDetails.visibility == View.VISIBLE && TextUtils.isEmpty(
                        etEMI.text
                    ) -> {
                        tvEMIErrorMessage.visibility = View.VISIBLE
                        tvEMIErrorMessage.text = "Please enter EMI amount."
                        llEMI.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etEMI.setTextColor(resources.getColor(R.color.error_red))
                    }
                    //Step 12 open Residence details
                    llResidenceTypeSection.visibility == View.GONE -> {
                        llResidenceTypeSection.visibility = View.VISIBLE
                        scrollToBottom(llResidenceTypeSection)
                    }
                    //Step 13 ResidenceType validation
                    llResidenceTypeSection.visibility == View.VISIBLE && TextUtils.isEmpty(
                        addResidentDetailsRequest.Data?.residentialDetails?.ResidenceType
                    ) -> {
                        showToast("Please select residence type.")
                    }
                    //Step 14 residence city validation
                    llResidenceTypeSection.visibility == View.VISIBLE && TextUtils.isEmpty(
                        addResidentDetailsRequest.Data?.residentialDetails?.CustomerCity
                    ) -> {
                        tvResidenceTypeErrorMessage.text = ("Please select residence city.")
                        tvResidenceTypeErrorMessage.visibility = View.VISIBLE
                        llResidenceType.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etAutoResidenceCity.setTextColor(resources.getColor(R.color.error_red))

                    }
                    //Step 15 residence year validation
                    llResidenceTypeSection.visibility == View.VISIBLE && addResidentDetailsRequest.Data!!.residentialDetails!!.NoOfYearInResident!! < 0 -> {
                        showToast("Please select no of years in current residence.")
                    }
                    //Step 16 Open Pan Card Section
                    llPanNumberSection.visibility == View.GONE -> {
                        llPanNumberSection.visibility = View.VISIBLE
                        scrollToBottom(llPanNumberSection)
                    }
                    //Step 17 Pan Card Number validation
                    llPanNumberSection.visibility == View.VISIBLE && (TextUtils.isEmpty(
                        addResidentDetailsRequest.Data?.personalDetails?.PANNumber
                    ) || !isValidPanNo(addResidentDetailsRequest.Data?.personalDetails?.PANNumber!!))
                    -> {
                        tvPanNumberErrorMessage.visibility = View.VISIBLE
                        tvPanNumberErrorMessage.text = ("Please enter valid Pancard.")
                        llPanNumber.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etPanNumber.setTextColor(resources.getColor(R.color.error_red))
                    }
                    //Step 18 Call Add Residence Details API
                    llPanNumberSection.visibility == View.VISIBLE && !isResidentDataSaved -> {
                        callAddResidentDetails()
                    }
                    //Step 19 All Data save
                    !TextUtils.isEmpty(customerId) && isEmploymentDataSaved && isResidentDataSaved -> {

                        if (addLeadRequest.hashCode() != previousAddLeadRequest.hashCode()
                        ) {
                            callUpdateAddLeadBasicDetailsAPIApi()
                        }

                        if (addEmploymentDetailsRequest.hashCode() != previousAddEmploymentDetailsRequest?.hashCode()
                        ) {
                            callAddEmploymentDetails()
                        }
                        if (addResidentDetailsRequest.hashCode() != previousAddResidentDetailsRequest?.hashCode()) {
                            callAddResidentDetails()
                        }

                        //Step 20 All Data Uploaded
                        if (!TextUtils.isEmpty(customerId) &&
                            addEmploymentDetailsRequest.hashCode() == previousAddEmploymentDetailsRequest?.hashCode() &&
                            addResidentDetailsRequest.hashCode() == previousAddResidentDetailsRequest?.hashCode()
                        ) {
                            checkForNavToSoftOffer()
                        }

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

    //region Filled Data
    fun preFilledPersonalBasicDetails(
        salutationValue: String?,
        firstName: String?,
        lastName: String?,
        email: String?
    ) {
        try {


            if (addLeadRequest.Data?.basicDetails == null) {
                val basicDetails = BasicDetails()
                addLeadRequest.Data?.basicDetails = basicDetails;

            }


            addLeadRequest.Data?.basicDetails?.FirstName = firstName
            addLeadRequest.Data?.basicDetails?.LastName = lastName
            addLeadRequest.Data?.basicDetails?.Email = email
            addLeadRequest.Data?.basicDetails?.Salutation = salutationValue
            salutation = salutationValue!!
            etFirstName.setText(firstName)
            etLastName.setText(lastName)
            etEmailId.setText(email)


            salutationAdapter?.dataListFilter?.forEachIndexed { index, dataSelectionDTO ->
                if (dataSelectionDTO.displayValue.toString() == salutation) {
                    dataSelectionDTO.selected = true
                    salutation = dataSelectionDTO.value!!
                    addLeadRequest.Data?.basicDetails?.Salutation = salutation
                } else {
                    dataSelectionDTO.selected = false
                }
            }
            salutationAdapter?.notifyDataSetChanged()

            previousAddLeadRequest = AddLeadRequest(
                    addLeadRequest.UserId,
                    addLeadRequest.UserType,
                    AddLeadData(addLeadRequest.Data?.basicDetails, addLeadRequest.Data?.addLeadVehicleDetails)
            )

        } catch (eNull: NullPointerException) {

        } catch (e: Exception) {

        }
    }

    private fun preFilledData(customerDetailsResponse: CustomerDetailsResponse?) {
        try {

            var birthDateDisplayValue: String? = null
            var birthDateValue: String? = null
            var employmentType: String? = null
            var bankName: String? = null
            var panNumber: String? = null

            var salutationValue: String? = null
            var firstName: String? = null
            var lastName: String? = null
            var email: String? = null


            if (customerDetailsResponse?.data != null) {
                //set Vehicle details
                if (customerDetailsResponse.data?.vehicleDetails != null) {
                    addLeadRequest.Data?.addLeadVehicleDetails?.VehicleNumber =
                        customerDetailsResponse.data?.vehicleDetails?.vehicleNumber
                    addLeadRequest.Data?.addLeadVehicleDetails?.VehicleSellingPrice =
                        customerDetailsResponse.data?.vehicleDetails?.vehicleSellingPrice?.toInt()
                            .toString()
                    addLeadRequest.Data?.addLeadVehicleDetails?.KMs =
                        customerDetailsResponse.data?.vehicleDetails?.kMs
                    addLeadRequest.Data?.addLeadVehicleDetails?.FuelType =
                        customerDetailsResponse.data?.vehicleDetails?.fuelType
                    addLeadRequest.Data?.addLeadVehicleDetails?.Ownership =
                        customerDetailsResponse.data?.vehicleDetails?.ownership
                }
                //set basicDetails
                if (customerDetailsResponse.data?.basicDetails != null) {
                    salutationValue = customerDetailsResponse.data?.basicDetails?.salutation
                    firstName = customerDetailsResponse.data?.basicDetails?.firstName
                    lastName = customerDetailsResponse.data?.basicDetails?.lastName
                    email = customerDetailsResponse.data?.basicDetails?.email
                    preFilledPersonalBasicDetails(salutationValue, firstName, lastName, email)

                }

                //Set Birth Data
                if (!TextUtils.isEmpty(customerDetailsResponse.data?.basicDetails?.birthDate)) {
                    birthDateDisplayValue = stringToDateString(
                        customerDetailsResponse.data?.basicDetails?.birthDate.toString()
                            .subSequence(0, 10) as String,
                        DATE_FORMATE_YYYYMMDD,
                        DATE_FORMATE_DDMMYYYY
                    )
                    birthDateValue = stringToDateString(
                        customerDetailsResponse.data?.basicDetails?.birthDate.toString()
                            .subSequence(0, 10) as String,
                        DATE_FORMATE_YYYYMMDD,
                        DATE_FORMATE_YYYYMMDD
                    )
                }
                if (birthDateDisplayValue == null) {
                    birthDateDisplayValue = stringToDateString(
                        customerDetailsResponse.data?.equifaxFields?.birthDate!![0],
                        DATE_FORMATE_YYYYMMDD,
                        DATE_FORMATE_DDMMYYYY
                    )
                    birthDateValue = stringToDateString(
                        customerDetailsResponse.data?.equifaxFields?.birthDate!![0],
                        DATE_FORMATE_YYYYMMDD,
                        DATE_FORMATE_YYYYMMDD
                    )

                }

                llBirthDateSection.visibility = View.VISIBLE
                etBirthDate.setText(birthDateDisplayValue)
                addEmploymentDetailsRequest.Data?.personalDetails?.BirthDate = birthDateValue

                //Set Employment Details
                if (customerDetailsResponse.data?.employmentDetails != null) {
                    llEmploymentSection.visibility = View.VISIBLE
                    employmentDetailsAdapter?.dataListFilter?.forEachIndexed { index, dataSelectionDTO ->
                        if (dataSelectionDTO.displayValue.toString() == customerDetailsResponse.data?.employmentDetails!!.employmentType
                        ) {
                            dataSelectionDTO.selected = true
                            employmentType =
                                customerDetailsResponse.data?.employmentDetails?.employmentType
                            addEmploymentDetailsRequest.Data?.employmentDetails?.EmploymentType =
                                dataSelectionDTO.value
                        } else {
                            dataSelectionDTO.selected = false
                        }
                    }
                    employmentDetailsAdapter?.notifyDataSetChanged()


                    //set Employment other details
                    if (!TextUtils.isEmpty(employmentType)) {
                        if (employmentType.equals("Self Employed")) {
                            //  llWorkExpriance.visibility = View.GONE
                            if (customerDetailsResponse.data?.employmentDetails?.totalWorkExperience != null && customerDetailsResponse.data!!.employmentDetails!!.totalWorkExperience!!.toInt() > 0) {
                                etWorkExpriance.setText(customerDetailsResponse.data?.employmentDetails!!.totalWorkExperience!!)
                            }
                            llAccoutDetailsSection.visibility = View.VISIBLE
                            tvBankTitle.text = getString(R.string.primary_bank_account)
                            bankName =
                                customerDetailsResponse.data?.employmentDetails?.primaryAccount
                            addEmploymentDetailsRequest.Data?.employmentDetails?.PrimaryAccount =
                                bankName

                            cbMoreThanOneYearInCurrentOrganization.isChecked =
                                customerDetailsResponse.data?.employmentDetails?.currentCompanyExpMoreThanOne!!
                            addEmploymentDetailsRequest.Data?.employmentDetails?.CurrentCompanyExpMoreThanOne =
                                customerDetailsResponse.data?.employmentDetails?.currentCompanyExpMoreThanOne!!

                            //   isEmploymentDataSaved = true
                            cbMoreThanOneYearInCurrentOrganization.text =
                                getString(R.string.more_than_one_year_in_current_Business)
                        } else {
                            bankName =
                                customerDetailsResponse.data?.employmentDetails?.salaryAccount
                            addEmploymentDetailsRequest.Data?.employmentDetails?.SalaryAccount =
                                bankName
                            //  llWorkExpriance.visibility = View.VISIBLE
                            tvBankTitle.text = getString(R.string.salary_bank_account)
                            if (customerDetailsResponse.data?.employmentDetails?.totalWorkExperience != null && customerDetailsResponse.data!!.employmentDetails!!.totalWorkExperience!!.toInt() > 0) {
                                etWorkExpriance.setText(customerDetailsResponse.data!!.employmentDetails!!.totalWorkExperience!!)
                            }
                            //More than one year in same org
                            cbMoreThanOneYearInCurrentOrganization.isChecked =
                                customerDetailsResponse.data!!.employmentDetails!!.currentCompanyExpMoreThanOne!!
                            addEmploymentDetailsRequest.Data!!.employmentDetails!!.CurrentCompanyExpMoreThanOne =
                                customerDetailsResponse.data!!.employmentDetails!!.currentCompanyExpMoreThanOne!!
                            isEmploymentDataSaved = true
                            cbMoreThanOneYearInCurrentOrganization.text =
                                getString(R.string.more_than_one_year_in_current_organization)

                        }
                        if (!TextUtils.isEmpty(bankName)) {
                            updateBankSelection(bankName!!)
                        }
                    }

                    //set Bank Details
                    if (!TextUtils.isEmpty(bankName)) {
                        commonBankListDetailsAdapter!!.dataListFilter!!.forEachIndexed { index, dataSelectionDTO ->
                            dataSelectionDTO.selected =
                                dataSelectionDTO.displayValue.toString() == bankName
                        }
                        commonBankListDetailsAdapter!!.notifyDataSetChanged()
                        llAccoutDetailsSection.visibility = View.VISIBLE
                    }

                    //set net income
                    if (customerDetailsResponse.data!!.employmentDetails!!.netAnualIncome != null && customerDetailsResponse.data!!.employmentDetails!!.netAnualIncome!! > 0) {
                        llNetIncomeSection.visibility = View.VISIBLE
                        etNetIncome.setText(
                            customerDetailsResponse.data!!.employmentDetails!!.netAnualIncome!!.toInt()
                                .toString()
                        )
                        addEmploymentDetailsRequest.Data!!.employmentDetails!!.NetAnualIncome =
                            customerDetailsResponse.data!!.employmentDetails!!.netAnualIncome!!.toInt()
                    }


                }

                //set EMI Details
                var haveEmi: String
                llEMISection.visibility = View.VISIBLE

                if (customerDetailsResponse.data!!.basicDetails!!.haveExistingEMI == true) {
                    haveEmi = "Yes"
                    addResidentDetailsRequest.Data!!.personalDetails!!.HaveExistingEMI = true
                    llEmiDetails.visibility = View.VISIBLE

                    if (customerDetailsResponse.data!!.basicDetails!!.totalEMI!!.toInt() > 0) {
                        etEMI.setText(
                            customerDetailsResponse.data!!.basicDetails!!.totalEMI!!.toInt()
                                .toString()
                        )
                        addResidentDetailsRequest.Data!!.personalDetails!!.TotalEMI =
                            customerDetailsResponse.data!!.basicDetails!!.totalEMI!!.toInt()
                    } else {
                        addResidentDetailsRequest.Data!!.personalDetails!!.TotalEMI = 0
                    }

                } else {
                    haveEmi = "No"
                    addResidentDetailsRequest.Data!!.personalDetails!!.HaveExistingEMI = false
                }

                eMIDetailsAdapter.dataListFilter!!.forEachIndexed { index, dataSelectionDTO ->
                    dataSelectionDTO.selected = dataSelectionDTO.value.toString() == haveEmi
                }
                eMIDetailsAdapter.notifyDataSetChanged()

                //set residentialDetails
                if (customerDetailsResponse.data!!.residentialDetails != null) {
                    //Set residenceType
                    if (customerDetailsResponse.data!!.residentialDetails!!.residenceType != null) {
                        llResidenceTypeSection.visibility = View.VISIBLE
                        residenceTypeDetailsAdapter!!.dataListFilter!!.forEachIndexed { index, dataSelectionDTO ->
                            if (dataSelectionDTO.value.toString() == customerDetailsResponse.data!!.residentialDetails!!.residenceType
                            ) {
                                dataSelectionDTO.selected = true
                                addResidentDetailsRequest.Data!!.residentialDetails!!.ResidenceType =
                                    dataSelectionDTO.value.toString()
                            } else {
                                dataSelectionDTO.selected = false
                            }
                        }
                        residenceTypeDetailsAdapter!!.notifyDataSetChanged()

                        //set noOfYearInResident
                        if (customerDetailsResponse.data!!.residentialDetails!!.noOfYearInResident != null) {

                            residenceYearsAdapter!!.dataListFilter!!.forEachIndexed { index, dataSelectionDTO ->
                                if (dataSelectionDTO.value.toString()
                                        .equals(customerDetailsResponse.data!!.residentialDetails!!.noOfYearInResident.toString())
                                ) {
                                    dataSelectionDTO.selected = true
                                    addResidentDetailsRequest.Data!!.residentialDetails!!.NoOfYearInResident =
                                        dataSelectionDTO.value.toString().toInt()
                                } else {
                                    dataSelectionDTO.selected = false
                                }
                            }
                            residenceYearsAdapter!!.notifyDataSetChanged()
                        }
                        //set Customer Resident City
                        if (customerDetailsResponse.data!!.residentialDetails!!.customerCity != null) {
                            addResidentDetailsRequest.Data!!.residentialDetails!!.CustomerCity =
                                customerDetailsResponse.data!!.residentialDetails!!.customerCity
                            etAutoResidenceCity.setText(customerDetailsResponse.data!!.residentialDetails!!.customerCity)
                        }
                    }


                }
                //set Pan Card Number
                if (customerDetailsResponse!!.data!!.basicDetails!!.panNumber != null) {
                    panNumber = customerDetailsResponse!!.data!!.basicDetails!!.panNumber
                }
                if (panNumber == null && customerDetailsResponse!!.data!!.equifaxFields!!.panNumber != null && customerDetailsResponse!!.data!!.equifaxFields!!.panNumber!!.size > 0) {
                    panNumber = customerDetailsResponse!!.data!!.equifaxFields!!.panNumber!!.get(0)
                        .toString()
                }
                if (panNumber != null) {
                    llPanNumberSection.visibility = View.VISIBLE
                    etPanNumber.setText(panNumber)

                }


            }

            createRequestCloneOfAddLead()
            createRequestCloneAddEmploymentEmploymentDetails()
            createRequestCloneAddResidentDetails()
        } catch (e: Exception) {
            Log.d("Err", e.message)
        }

    }


//endregion Filled Data


    //region setAdapter
    fun setSalutaionAdapterData() {
        masterViewModel!!.getSalutations(Global.customerDetails_BaseURL + CommonStrings.SALUTATION_END_POINT)
    }

    private fun setSalutation(types: List<Types>) {
        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        types.forEachIndexed { index, types ->
            list.add(DataSelectionDTO(types.displayLabel, null, types.value, false))
        }



        salutationAdapter =
            DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                override fun itemClick(item: Any?, position: Int) {


                    salutationAdapter!!.dataListFilter!!.forEachIndexed { index, item ->
                        run {
                            if (index == position) {
                                item.selected = true
                                basicDetails.Salutation = item.value
                                salutation = item.value.toString()
                                addLeadRequest.Data!!.basicDetails!!.Salutation = salutation
                            } else {
                                item.selected = false
                            }
                        }
                    }
                    salutationAdapter!!.notifyDataSetChanged()
                    scrollToBottom(llBirthDateSection)
                }
            })


        val layoutManagerStaggeredGridLayoutManager =
            StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 4)

        rv_salutation.addItemDecoration(GridItemDecoration(25, 4))

        rv_salutation.layoutManager = layoutManagerStaggeredGridLayoutManager

        rv_salutation.adapter = salutationAdapter
    }

    private fun setEMIDetailsAdapter() {
        val layoutManagerStaggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

        rvEMIList.addItemDecoration(GridItemDecoration(25, 2))
        rvEMIList.layoutManager = layoutManagerStaggeredGridLayoutManager

        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()
        list.add(DataSelectionDTO("Yes", null, "Yes", false))
        list.add(DataSelectionDTO("No", null, "No", false))


        eMIDetailsAdapter =
            DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                override fun itemClick(item: Any?, position: Int) {


                    eMIDetailsAdapter.dataListFilter!!.forEachIndexed { index, item ->
                        run {
                            if (index == position) {
                                item.selected = true
                                if (item.value.equals("Yes")) {
                                    llEmiDetails.visibility = View.VISIBLE
                                    addResidentDetailsRequest.Data!!.personalDetails!!.HaveExistingEMI =
                                        true
                                } else {
                                    llEmiDetails.visibility = View.GONE
                                    addResidentDetailsRequest.Data!!.personalDetails!!.HaveExistingEMI =
                                        false
                                }

                            } else {
                                item.selected = false
                            }

                        }
                    }
                    eMIDetailsAdapter.notifyDataSetChanged()
                    scrollToBottom(llEMISection)
                }
            })


        rvEMIList.adapter = eMIDetailsAdapter
    }

    fun setEploymentDetailsAdapter() {
        val layoutManagerStaggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

        rvEmploymentType.addItemDecoration(GridItemDecoration(25, 2))
        //  llWorkExpriance.visibility = View.GONE
        rvEmploymentType.setLayoutManager(layoutManagerStaggeredGridLayoutManager)
        masterViewModel.getEmploymentTypeDetails(Global.customerDetails_BaseURL + CommonStrings.EMPLOYEEMENT_END_POINT)

    }

    fun setBankDetailsAdapter() {
        val layoutManagerStaggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

        rvBankList.addItemDecoration(GridItemDecoration(25, 2))
        rvBankList.layoutManager = layoutManagerStaggeredGridLayoutManager

        masterViewModel.getCommonBanks(Global.customerDetails_BaseURL + CommonStrings.COMMON_BANK_LIST_END_POINT)

        ivBackToVehDetails.setOnClickListener(this)

    }

    fun setResidenceTypeAdapter() {
        val layoutManagerStaggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

        rvResidenceTypeList.addItemDecoration(GridItemDecoration(25, 2))

        rvResidenceTypeList.layoutManager = layoutManagerStaggeredGridLayoutManager
        masterViewModel.getResidentType(Global.customerDetails_BaseURL + CommonStrings.RESIDENT_TYPE_END_POINT)


    }

    fun setResidenceYearsAdapter() {
        val layoutManagerStaggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

        rvResidenceYears.addItemDecoration(GridItemDecoration(25, 2))

        rvResidenceYears.layoutManager = layoutManagerStaggeredGridLayoutManager
        masterViewModel.getResidentYears(Global.customerDetails_BaseURL + CommonStrings.RESIDENT_YEARS_END_POINT)


    }

    private fun setCityAutoTextAdapter(cityList: ArrayList<String>) {
        customAutoTextViewListAdapter = CustomAutoTextViewListAdapter(
            requireContext(),
            R.layout.v2_auto_text_adapter_layout,
            cityList
        )
        etAutoResidenceCity.setAdapter(customAutoTextViewListAdapter)
        etAutoResidenceCity.threshold = 1
        etAutoResidenceCity.showDropDown()
        etAutoResidenceCity.onItemClickListener = OnItemClickListener { parent, arg1, pos, id ->
            addResidentDetailsRequest.Data!!.residentialDetails!!.CustomerCity =
                customAutoTextViewListAdapter.getItem(pos)
            var t: Timer? = null
            t = Timer()
            t.schedule(object : TimerTask() {
                override fun run() {
                    ThreadUtils.runOnUiThread(Runnable {
                        etAutoResidenceCity.dismissDropDown()
                    });
                }
            }, 1000)

        }
    }
    //endregion setAdapter

    // API call region starts

    //region create Transaction request

    private fun getCustomerRequest(): CustomerRequest {
        val resetJourney = ResetCustomerJourneyDataRequest(validateLeadDataRes.oldCustomerId)
        return CustomerRequest(resetJourney, CommonStrings.DEALER_ID, CommonStrings.USER_TYPE)
    }

    private fun getUpdateLeadBasicDetailsRequest(): UpdateLeadBasicDetailsRequest {
        val updateLeadBasicDetailsRequest = UpdateLeadBasicDetailsRequest()
        if (addLeadRequest.Data!!.basicDetails != null) {
            val leadUpdateData = LeadUpdateData(
                customerId.toInt(),
                LeadBasicBasicDetails(
                    addLeadRequest.Data!!.basicDetails!!.Email,
                    addLeadRequest.Data!!.basicDetails!!.FirstName,
                    addLeadRequest.Data!!.basicDetails!!.LastName,
                    addLeadRequest.Data!!.basicDetails!!.Salutation
                )
            )

            updateLeadBasicDetailsRequest.Data = leadUpdateData
            updateLeadBasicDetailsRequest.UserType = CommonStrings.USER_TYPE
            updateLeadBasicDetailsRequest.UserId = CommonStrings.DEALER_ID
        }

        return updateLeadBasicDetailsRequest
    }

    private fun getValidateLeadReq(): ValidateLeadRequest {
        val validateLeadDataRequest = ValidateLeadDataRequest(mobileNum, make, model, variant)
        return ValidateLeadRequest(
            validateLeadDataRequest,
            CommonStrings.DEALER_ID,
            CommonStrings.USER_TYPE
        )
        /*val validateLeadDataRequest= ValidateLeadDataRequest(etMobileNumberV2.text.toString(),addLeadRequest.Data?.vehicleDetails?.Make,addLeadRequest.Data?.vehicleDetails?.Model,addLeadRequest.Data?.vehicleDetails?.Variant)
        return ValidateLeadRequest(validateLeadDataRequest, addLeadRequest.UserId, addLeadRequest.UserType)*/
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

    fun createAddEmploymentDetailsRequest(customerId: Int): AddEmploymentDetailsRequest {
        var addEmploymentDetailsDataRequest = AddEmploymentDetailsRequest()
        addEmploymentDetailsDataRequest.UserId = CommonStrings.DEALER_ID
        addEmploymentDetailsDataRequest.UserType = CommonStrings.USER_TYPE

        var addEmploymentEmploymentDetails = AddEmploymentEmploymentDetails()
        var addEmploymentPersonalDetails = AddEmploymentPersonalDetails()

        var addEmploymentData = AddEmploymentData(
            customerId,
            addEmploymentEmploymentDetails,
            addEmploymentPersonalDetails
        )
        addEmploymentDetailsDataRequest.Data = addEmploymentData
        return addEmploymentDetailsDataRequest

    }

    private fun createAddResidentDetailsRequest(customerId: Int): AddResidentDetailsRequest {
        var addResidentDetailsRequest = AddResidentDetailsRequest()
        addResidentDetailsRequest.UserId = CommonStrings.DEALER_ID
        addResidentDetailsRequest.UserType = CommonStrings.USER_TYPE
        var data = ResidentDetailsData()

        var personalDetails = ResidentDetailsDataPersonalDetails()
        var residentialDetails = ResidentDetailsDataResidentialDetails()

        data.CustomerId = customerId
        data.personalDetails = personalDetails
        data.residentialDetails = residentialDetails
        addResidentDetailsRequest.Data = data
        return addResidentDetailsRequest

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
    //endregion create Transaction request

    //region on Api Callback/Response
    private fun onGenerateOTP(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                try {
                    hideProgressDialog()
                    val otpResponse: OTPResponse? = mApiResponse.data as OTPResponse?
                    if (otpResponse?.status!!) {
                        ll_otp_v2.visibility = View.VISIBLE
                        llTAndC.visibility = View.VISIBLE
                        mobileNum = etMobileNumberV2.text.toString()
                       /* if (!TextUtils.isEmpty(otpResponse.data)) {
                            etOTPV2.setText(otpResponse.data)

                        }
*/

                        if (tvOTPTimerV2.text == getString(R.string.otp_timer_hint)) {
                            enableTimer()
                        } else {
                            timer.cancel()
                            timer.start()
                        }

                    }


                } catch (e: Exception) {

                }
            }
            ApiResponse.Status.ERROR -> {
                showToast(mApiResponse.error?.message.toString())
                hideProgressDialog()
            }
        }

    }


    private fun onValidateOTP(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
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


    private fun onEmploymentTypeListDetails(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
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



        employmentDetailsAdapter =
            DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                override fun itemClick(item: Any?, position: Int) {


                    employmentDetailsAdapter!!.dataListFilter!!.forEachIndexed { index, item ->
                        run {
                            if (index == position) {
                                item.selected = true
                                addEmploymentDetailsRequest.Data!!.employmentDetails!!.EmploymentType =
                                    item.value
                                if (item.value.equals("Self Employed")) {
                                    // llWorkExpriance.visibility = View.GONE
                                    cbMoreThanOneYearInCurrentOrganization.text =
                                        getString(R.string.more_than_one_year_in_current_Business)

                                    etWorkExpriance.setText("")
                                    llAccoutDetailsSection.visibility = View.VISIBLE
                                    tvBankTitle.setText(getString(R.string.primary_bank_account))

                                    if (addEmploymentDetailsRequest.Data!!.employmentDetails?.SalaryAccount?.isNotEmpty() == true) {
                                        addEmploymentDetailsRequest.Data!!.employmentDetails?.PrimaryAccount =
                                            addEmploymentDetailsRequest.Data!!.employmentDetails!!.SalaryAccount
                                    }
                                    addEmploymentDetailsRequest.Data!!.employmentDetails!!.SalaryAccount =
                                        null

                                } else {
                                    etWorkExpriance.setText("")
                                    // llWorkExpriance.visibility = View.VISIBLE
                                    tvBankTitle.setText(getString(R.string.salary_bank_account))
                                    cbMoreThanOneYearInCurrentOrganization.text =
                                        getString(R.string.more_than_one_year_in_current_organization)

                                    if (addEmploymentDetailsRequest.Data?.employmentDetails?.PrimaryAccount?.isNotEmpty() == true) {
                                        addEmploymentDetailsRequest.Data!!.employmentDetails!!.SalaryAccount =
                                            addEmploymentDetailsRequest.Data!!.employmentDetails!!.PrimaryAccount
                                    }
                                    addEmploymentDetailsRequest.Data!!.employmentDetails!!.PrimaryAccount =
                                        null
                                }

                            } else {
                                item.selected = false
                            }

                        }
                    }
                    employmentDetailsAdapter!!.notifyDataSetChanged()
                    scrollToBottom(llEmploymentSection)
                }
            })


        rvEmploymentType.adapter = employmentDetailsAdapter
    }

    //region onResidentType
    private fun onResidentType(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
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



        residenceTypeDetailsAdapter =
            DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                override fun itemClick(item: Any?, position: Int) {


                    residenceTypeDetailsAdapter!!.dataListFilter!!.forEachIndexed { index, item ->
                        run {
                            if (index == position) {
                                item.selected = true

                                addResidentDetailsRequest.Data!!.residentialDetails!!.ResidenceType =
                                    item.value

                            } else {
                                item.selected = false
                            }

                        }
                    }
                    residenceTypeDetailsAdapter!!.notifyDataSetChanged()
                    scrollToBottom(llResidenceTypeSection)
                }
            })


        rvResidenceTypeList.adapter = residenceTypeDetailsAdapter
    }
    //endregion onResidentType

    //region onResident Years
    private fun onResidentYears(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
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



        residenceYearsAdapter =
            DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                override fun itemClick(item: Any?, position: Int) {


                    residenceYearsAdapter!!.dataListFilter!!.forEachIndexed { index, item ->
                        run {
                            if (index == position) {
                                item.selected = true
                                addResidentDetailsRequest.Data!!.residentialDetails!!.NoOfYearInResident =
                                    item.value.toString().toInt()
                                llPanNumberSection.visibility = View.VISIBLE
                            } else {
                                item.selected = false
                            }

                        }
                    }
                    residenceYearsAdapter!!.notifyDataSetChanged()
                    scrollToBottom(llPanNumberSection)
                }
            })


        rvResidenceYears.adapter = residenceYearsAdapter
    }

    //endregion onResidentType
    private fun onCommonBanksResponse(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val response: CommonBanksResponse? = mApiResponse.data as CommonBanksResponse?
                setCommonBankListDetails(response!!)

            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

    private fun setCommonBankListDetails(commonBanksResponse: CommonBanksResponse) {
        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        commonBanksResponse.data!!.forEachIndexed { index, bankItem ->
            if (index < 4) {
                // bankItem.logoUrl="https://4.bp.blogspot.com/-xnBMIjH4Z-4/W2Qfiocw5hI/AAAAAAAAGg4/136qYboj2EQ3Ve5BjqOmFL8rf_UWijelgCPcBGAYYCw/s1600/AX.png"
                list.add(
                    DataSelectionDTO(
                        bankItem.bankName,
                        null,
                        bankItem.bankName,
                        false,
                        bankItem.logoUrl
                    )
                )
            }
        }

        commonBankListDetailsAdapter =
            DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                override fun itemClick(item: Any?, position: Int) {
                    var selectedBankDataSelectionDTO: DataSelectionDTO = item as DataSelectionDTO
                    updateBankSelection(selectedBankDataSelectionDTO.displayValue!!)
                    etSearchBank.setText(selectedBankDataSelectionDTO.displayValue!!)


                }
            })


        rvBankList.adapter = commonBankListDetailsAdapter
    }

    private fun onResidentCityName(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                // showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val response: CityNameListResponse? = mApiResponse.data as CityNameListResponse?
                if (response?.data != null && response.data!!.isNotEmpty()) {
                    var cityList: ArrayList<String> = arrayListOf<String>()

                    response.data!!.forEachIndexed { index, city ->
                        cityList.add(city)
                    }
                    setCityAutoTextAdapter(cityList)

                }

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
            }
        }
    }


    private fun onSalutationRes(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
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
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                createRequestCloneOfAddLead()
                val addLeadResponse: AddLeadResponse? = mApiResponse.data as AddLeadResponse?
                if (addLeadResponse?.statusCode.equals("200") || addLeadResponse?.mData!! > 0) {
                    customerId = addLeadResponse?.mData.toString()
                    llBirthDateSection.visibility = View.VISIBLE
                    //Create Request of Add Employment Details
                    addEmploymentDetailsRequest =
                        createAddEmploymentDetailsRequest(addLeadResponse?.mData!!)
                    addResidentDetailsRequest =
                        createAddResidentDetailsRequest(addLeadResponse.mData!!)
                    callCustomerDetailsApi(addLeadResponse.mData!!)
                    checkForNavToSoftOffer()
                }

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

    private fun onUpdateAddLeadBasicDetails(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                createRequestCloneOfAddLead()
                val addLeadResponse: AddLeadResponse? = mApiResponse.data as AddLeadResponse?
                if (addLeadResponse?.statusCode.equals("200") || addLeadResponse?.mData!! > 0) {
                    customerId = addLeadResponse?.mData.toString()
                }
                showToast(addLeadResponse?.message.toString())

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()


            }
            else -> {
                showToast("Please enter valid details")
            }
        }

    }


    private fun onValidateLead(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val validateLeadResponse: ValidateLeadResponse? =
                    mApiResponse.data as ValidateLeadResponse?
                val validateLeadDataResponse: ValidateLeadDataResponse? = validateLeadResponse?.data

                if (validateLeadDataResponse?.message.equals("Success")) {
                    validateLeadDataRes = validateLeadDataResponse!!
                    if (validateLeadDataRes.details != null) {
                        preFilledPersonalBasicDetails(
                            validateLeadDataRes.details?.salutation,
                            validateLeadDataRes.details?.firstName,
                            validateLeadDataRes.details?.lastName,
                            validateLeadDataRes.details?.email
                        )
                    }
                } else {
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
        parseCommonResponse(mAPIResponse)
        when (mAPIResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val resetJourneyRes: ResetCustomerJourneyResponse? =
                    mAPIResponse.data as ResetCustomerJourneyResponse?

                if (dialogConfilctForAddLead != null && dialogConfilctForAddLead!!.isShowing) {
                    dialogConfilctForAddLead?.dismiss()
                }

                // showToast(resetJourneyRes?.message.toString())

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
        dialogConfilctForAddLead?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogConfilctForAddLead?.setCancelable(false)
        dialogConfilctForAddLead?.setCanceledOnTouchOutside(false)
        dialogConfilctForAddLead?.setContentView(R.layout.vtwo_layout_custom_dialog)
        val tvAlertDialogText =
            dialogConfilctForAddLead?.findViewById(R.id.tvAlertDialogText) as TextView
        tvAlertDialogText.text = validateLeadDataRes.message
        val btnNewFlow = dialogConfilctForAddLead?.findViewById(R.id.btnStartNewFlowV2) as Button
        val btnOk = dialogConfilctForAddLead?.findViewById(R.id.btnOk) as Button
        val btnContinueWithOldFlow =
            dialogConfilctForAddLead?.findViewById(R.id.btnExistingFlowV2) as Button


        if(validateLeadDataRes.details?.status?.equals(ApplicationStatusEnum.Submitted_To_Bank) == true)
        {
            btnContinueWithOldFlow.visibility=View.GONE
        }
        else
        {
            btnContinueWithOldFlow.visibility=View.VISIBLE
        }
        btnNewFlow.setOnClickListener {
            resetJourney()
            dialogConfilctForAddLead?.dismiss()
        }
        btnOk.setOnClickListener {
            dialogConfilctForAddLead?.dismiss()
            navigateDashboardTop()
        }

        btnContinueWithOldFlow.setOnClickListener {
            customerId=validateLeadDataRes.oldCustomerId!!
            callCustomerDetailsApi(validateLeadDataRes.oldCustomerId!!.toInt())
            dialogConfilctForAddLead?.dismiss()
        }
        if (validateLeadDataRes.oldCustomerId!!.toInt() > 0) {
            btnOk.visibility = View.GONE
            btnNewFlow.visibility = View.VISIBLE
            btnContinueWithOldFlow.visibility = View.VISIBLE
        } else {
            btnOk.visibility = View.VISIBLE
            btnNewFlow.visibility = View.GONE
            btnContinueWithOldFlow.visibility = View.GONE
        }
        dialogConfilctForAddLead?.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialogConfilctForAddLead?.show()

    }


    private fun onAddEmploymentDetails(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                createRequestCloneAddEmploymentEmploymentDetails()
                hideProgressDialog()
                val addLeadResponse: AddLeadResponse? = mApiResponse.data as AddLeadResponse?
                if (addLeadResponse?.mData!! > 0) {
                    var addEmploymentDetailsId = addLeadResponse.mData.toString()
                    isEmploymentDataSaved = true
                    if (llEMISection.visibility == View.GONE) {
                        llEMISection.visibility = View.VISIBLE
                    }
                    checkForNavToSoftOffer()
                }
                //  showToast(addLeadResponse?.message.toString())

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
            }
            else -> {
                showToast("Please enter valid details")
            }
        }

    }

    private fun onAddResidentDetails(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                createRequestCloneAddResidentDetails()
                hideProgressDialog()
                val addLeadResponse: AddLeadResponse? = mApiResponse.data as AddLeadResponse?
                if (addLeadResponse?.mData!! > 0) {
                    var addEmploymentDetailsId = addLeadResponse.mData.toString()
                    isResidentDataSaved = true
                    checkForNavToSoftOffer()
                }

                //   showToast(addLeadResponse?.message.toString())

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
            }
            else -> {
                showToast("Please enter valid details")
            }
        }

    }


    private fun onCustomerDetails(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {

                if (dialogConfilctForAddLead != null && dialogConfilctForAddLead!!.isShowing) {
                    dialogConfilctForAddLead?.dismiss()
                }
                val customerResponse: CustomerDetailsResponse? =
                    mApiResponse.data as CustomerDetailsResponse?
                customerDetailsResponse = customerResponse!!


                if (customerDetailsResponse.data != null) {
                    hideProgressDialog()

                    if (customerDetailsResponse.data?.status == ApplicationStatusEnum.Registered.value || customerDetailsResponse.data?.status == ApplicationStatusEnum.KYC_Done.value) {
                        preFilledData(customerDetailsResponse)
                    } else
                    {
                        navigateToNextScreen(customerDetailsResponse)
                    }

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

    private fun navigateToNextScreen(customerDetailsResponse: CustomerDetailsResponse) {
        when (customerDetailsResponse.data?.status) {
            ApplicationStatusEnum.Lender_Selected.value -> {
                navigateToAddressAdditionalFields(
                        customerId.toInt(),
                        customerDetailsResponse,
                        "AddLead"
                )
            }
            ApplicationStatusEnum.Bank_Form_Filled.value -> {
                masterViewModel.getKYCDocumentResponse(Global.baseURL + CommonStrings.KYC_UPLOAD_URL_END_POINT +  customerId.toInt())

            }
            ApplicationStatusEnum.Document_Uploaded.value -> {
                navigateToBankOfferStatus(
                        customerId,
                        customerDetailsResponse,
                        "ADD_LEAD"
                )

            }
            ApplicationStatusEnum.Submitted_To_Bank.value -> {
                navigateToApplicationDetails( customerId.toInt())
            }
        }
    }


    //endregion on Api Callback/Response

    private fun makeOTPLayoutInvisible() {
        tvOTPTimerV2.text = "0 Sec"
        timer.cancel()
        etOTPV2.text.clear()
        ll_otp_v2.visibility = View.GONE
        llTAndC.visibility = View.GONE
        if (cbTermsAndConditions.isChecked)
            cbTermsAndConditions.isClickable = false
    }

    private fun onGetKYCDocumentResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val kycDocumentRes: KYCDocumentResponse = mApiResponse.data as KYCDocumentResponse
                if (kycDocumentRes.statusCode == "100") {
                    if (kycDocumentRes.data.groupedDoc.isNotEmpty() || kycDocumentRes.data.nonGroupedDoc.isNotEmpty())
                        navigateToKYCDocumentUpload(
                            customerId,
                            kycDocumentRes,
                            customerDetailsResponse.data?.caseId.toString(),
                            customerDetailsResponse,
                                "ADD_LEAD"
                        )
                    else if (kycDocumentRes.data.groupedDoc.isEmpty() && kycDocumentRes.data.nonGroupedDoc.isEmpty())
                        navigateToBankOfferStatus(
                            customerId,
                            customerDetailsResponse,
                            "ADD_LEAD"
                        )
                } else {
                    navigateToBankOfferStatus(
                        customerId,
                        customerDetailsResponse,
                        "ADD_LEAD"
                    )
                }

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                Log.i("SoftOfferFragment", ": " + ApiResponse.Status.ERROR)

            }
            else -> {
                Log.i("SoftOfferFragment", ": ")
            }
        }

    }


    // Loan Details
    // CurrentAddress
    // Additional Fields
    // Document upload status

}