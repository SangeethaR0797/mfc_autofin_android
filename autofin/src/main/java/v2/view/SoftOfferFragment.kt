package v2.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import android.view.View.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.mfc.autofin.framework.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit_config.RetroBase.retrofitInterface
import utility.CommonStrings
import utility.Global
import v2.model.dto.DataSelectionDTO
import v2.model.request.*
import v2.model.request.CurrentAddress
import v2.model.request.PermanentAddress
import v2.model.request.bank_offers.BankOfferData
import v2.model.request.bank_offers.BankOffersForApplicationRequest
import v2.model.request.bank_offers.LeadApplicationData
import v2.model.request.bank_offers.SelectRecommendedBankOfferRequest
import v2.model.response.*
import v2.model.response.bank_offers.*
import v2.model.response.master.*
import v2.model_view.BankOffersViewModel
import v2.model_view.MasterViewModel
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.*
import v2.view.base.BaseFragment
import v2.view.callBackInterface.AdditionalFieldsDetailsInterface
import v2.view.callBackInterface.DatePickerCallBack
import v2.view.callBackInterface.itemClickCallBack
import v2.view.utility_view.GridItemDecoration


class SoftOfferFragment : BaseFragment(), OnClickListener {

    private var list = ArrayList<DataSelectionDTO>()
    private var additionaFieldPinCode: String = ""
    private val currentFilledFieldData = HashMap<String, FieldDetails>()
    private val submitAdditionalFieldsList = HashMap<String, FieldDetails>()

    var initialCall: Boolean = true
    private var caseID: String = ""
    lateinit var scrollViewBankOffer: ScrollView

    lateinit var bankAdapter: SoftOfferAdapter
    lateinit var postSoftOfferAdapter: PostSoftOfferAdapter

    lateinit var customerDetailsResponse: CustomerDetailsResponse

    lateinit var ivBackToRedDetails: ImageView
    lateinit var imageViewEditCurrentAddress: ImageView
    lateinit var imageViewEditPermanentAddress: ImageView
    lateinit var skLoanAmount: SeekBar
    lateinit var skTenure: SeekBar
    lateinit var checkboxCurrentAndPermanentAddress: CheckBox

    lateinit var llBankOfferParent: LinearLayout
    lateinit var linearLayoutCalculation: LinearLayout
    lateinit var llSoftOfferDialog: LinearLayout
    lateinit var linearLayoutPostOffer: LinearLayout
    lateinit var linearLayoutAddNewCurrentAddress: LinearLayout
    lateinit var linearLayoutEditCurrentAddress: LinearLayout
    lateinit var linearLayoutAddNewPermanentAddress: LinearLayout
    lateinit var linearLayoutEditPermanentAddress: LinearLayout
    lateinit var linearLayoutAdditionalFieldsUILayout: LinearLayout

    lateinit var tvLoanAmountVal: TextView
    lateinit var tvLoanTenureVal: TextView
    lateinit var tvBankOfferTitleV2: TextView
    lateinit var textViewNoDataFound: TextView
    lateinit var textViewSelectBankLabel: TextView
    lateinit var textViewCurrentAddress1: TextView
    lateinit var textViewCurrentAddress2: TextView
    lateinit var textViewCurrentAddress3: TextView
    lateinit var textViewPermanentAddressEdit: TextView
    lateinit var textViewPermanentAddress1: TextView
    lateinit var textViewPermanentAddress2: TextView
    lateinit var textViewPermanentAddress3: TextView

    lateinit var recyclerViewBankOffer: RecyclerView


    lateinit var buttonLoanDetailsSubmit: Button
    lateinit var buttonAddNewAddress: Button
    lateinit var buttonAddNewPermanentAddress: Button

    lateinit var loanAmountViewModel: MasterViewModel
    lateinit var bankViewModel: BankOffersViewModel
    lateinit var pinCodeViewModel: MasterViewModel
    lateinit var addressViewModel: TransactionViewModel
    lateinit var additionalFieldsViewModel: TransactionViewModel
    lateinit var additionalFieldsAPIViewModel: MasterViewModel
    lateinit var submitAdditionalFieldsViewModel: TransactionViewModel
    lateinit var kycDocumentViewModel: MasterViewModel

    lateinit var currentAddress: CurrentAddress
    lateinit var permanentAddress: PermanentAddress
    lateinit var additionalFieldsData: AdditionalFieldsData
    lateinit var additionalFieldAdapter: DataRecyclerViewAdapter
    var sectionMap = HashMap<String, ArrayList<FieldDetails>>()
    var moveToBankOfferPage: Boolean = false
    var stateList = ArrayList<Details>()
    var cityList = ArrayList<Details>()
    var checkList = ArrayList<Details>()
    var professionList = ArrayList<Details>()
    var companyList = ArrayList<Details>()
    var educationList = ArrayList<Details>()


    lateinit var fragView: View
    var loanAmountDefault: Int = 0
    var loanTenureDefault: Int = 0
    var cityMovedInYear: String = ""
    var loanAmountMaximum: Int = 0
    var loanTenureMaximum: Int = 0

    private var loanAmountMinimum: Int = 0
    private var loanTenureMinimum: Int = 0

    var loanAmount = ""
    var loanTenure = ""
    var address = ""
    var pincode = ""
    var typeOfAddress = ""
    var state = ""
    var city = ""
    var address1 = ""
    var address2 = ""
    var address3 = ""
    var isPermanentAddress = true
    var delay: Long = 1000 // 1 seconds after user stops typing

    var last_text_edit: Long = 0
    var handler: Handler = Handler()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // region Loan-MasterViewModel

        loanAmountViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)

        loanAmountViewModel.getBankOfferLoanLiveData().observe(this) { mApiResponse: ApiResponse? ->
            onLoanAmountResponse(mApiResponse!!)
        }

        pinCodeViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)
        pinCodeViewModel.getPinCodeDataLiveData().observe(this) { mApiResponse: ApiResponse? ->
            onPinCodeResponse(mApiResponse!!)
        }

        //endregion Loan-MasterViewModel


        bankViewModel = ViewModelProvider(this).get(
                BankOffersViewModel::class.java
        )
        bankViewModel.getBankOffersForLeadApplicationLiveData().observe(this) { mApiResponse: ApiResponse? ->
            onBankResponse(
                    mApiResponse!!
            )
        }

        bankViewModel.getSetSelectRecommendedBankOfferLiveData().observe(this) { mApiResponse: ApiResponse? ->
            onSetBankRes(
                    mApiResponse!!
            )
        }

        addressViewModel = ViewModelProvider(this).get(
                TransactionViewModel::class.java
        )

        addressViewModel.getUpdateAddressLiveData()
                .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                    onUpdateResponse(
                            mApiResponse!!
                    )
                }
        addressViewModel.getCustomerDetailsLiveData()
                .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                    onCustomerDetails(
                            mApiResponse!!
                    )
                })

        additionalFieldsViewModel = ViewModelProvider(this).get(
                TransactionViewModel::class.java
        )

        additionalFieldsViewModel.getAdditionalFieldsLiveData().observe(requireActivity()) { mApiResponse: ApiResponse? ->
            onAdditionalFieldsResponse(
                    mApiResponse!!
            )
        }
        additionalFieldsAPIViewModel = ViewModelProvider(this).get(
                MasterViewModel::class.java
        )

        additionalFieldsAPIViewModel.getAdditionalFieldAPILiveData().observe(requireActivity()) { mApiResponse: ApiResponse? ->
            onAdditionalFieldAPIResponse(
                    mApiResponse!!
            )
        }

        submitAdditionalFieldsViewModel = ViewModelProvider(this).get(
                TransactionViewModel::class.java
        )

        submitAdditionalFieldsViewModel.mSubmitAdditionalFieldsLiveData().observe(requireActivity()) { mApiResponse: ApiResponse? ->
            onSubmitOfAdditionFields(
                    mApiResponse!!
            )
        }

        kycDocumentViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)
        kycDocumentViewModel.getKYCDocumentLiveData().observe(requireActivity()) { mApiResponse: ApiResponse? ->
            onGetKYCDocumentResponse(mApiResponse!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.v2_soft_offer_loading_fragment, container, false)
        arguments?.let {
            val safeArgs = SoftOfferFragmentArgs.fromBundle(it)
            customerDetailsResponse = safeArgs.CustomerDetails
            caseID = customerDetailsResponse.data?.caseId.toString()
            //caseID = "0242210316000103"
            customerId = safeArgs.customerId

            //customerId = "448"
        }
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            initViews(view)
        }
        return view
    }

    @SuppressLint("NewApi")
    private fun initViews(view: View?) {
        if (view != null) {
            fragView = view
            ivBackToRedDetails = view.findViewById(R.id.ivBackToRedDetails)
            imageViewEditCurrentAddress = view.findViewById(R.id.imageViewEditCurrentAddress)
            imageViewEditPermanentAddress = view.findViewById(R.id.imageViewEditPermanentAddress)
            scrollViewBankOffer = view.findViewById(R.id.scrollViewBankOffer)

            llBankOfferParent = view.findViewById(R.id.llBankOfferParent)
            llSoftOfferDialog = view.findViewById(R.id.llSoftOfferDialog)
            linearLayoutCalculation = view.findViewById(R.id.linearLayoutCalculation)
            linearLayoutPostOffer = view.findViewById(R.id.linearLayoutPostOffer)
            linearLayoutAddNewCurrentAddress = view.findViewById(R.id.linearLayoutAddNewCurrentAddress)
            linearLayoutEditCurrentAddress = view.findViewById(R.id.linearLayoutEditCurrentAddress)
            linearLayoutAddNewPermanentAddress = view.findViewById(R.id.linearLayoutAddNewPermanentAddress)
            linearLayoutEditPermanentAddress = view.findViewById(R.id.linearLayoutEditPermanentAddress)
            linearLayoutAdditionalFieldsUILayout = view.findViewById(R.id.linearLayoutAdditionalFieldsUILayout)

            tvLoanAmountVal = view.findViewById(R.id.tvLoanAmountValV2)
            tvLoanTenureVal = view.findViewById(R.id.tvLoanTenureValV2)
            tvBankOfferTitleV2 = view.findViewById(R.id.tvBankOfferTitleV2)
            textViewNoDataFound = view.findViewById(R.id.textViewNoDataFound)
            textViewSelectBankLabel = view.findViewById(R.id.textViewSelectBankLabel)
            textViewCurrentAddress1 = view.findViewById(R.id.textViewCurrentAddress1)
            textViewCurrentAddress2 = view.findViewById(R.id.textViewCurrentAddress2)
            textViewCurrentAddress3 = view.findViewById(R.id.textViewCurrentAddress3)
            textViewPermanentAddressEdit = view.findViewById(R.id.textViewPermanentAddressEdit)
            textViewPermanentAddress1 = view.findViewById(R.id.textViewPermanentAddress1)
            textViewPermanentAddress2 = view.findViewById(R.id.textViewPermanentAddress2)
            textViewPermanentAddress3 = view.findViewById(R.id.textViewPermanentAddress3)

            skLoanAmount = view.findViewById(R.id.skLoanAmount)
            skTenure = view.findViewById(R.id.skTenure)

            buttonLoanDetailsSubmit = view.findViewById(R.id.buttonLoanDetailsSubmit)

            recyclerViewBankOffer = view.findViewById(R.id.recyclerViewBankOffer)

            checkboxCurrentAndPermanentAddress = view.findViewById(R.id.checkboxCurrentAndPermanentAddress)
            loanAmountViewModel.getBankOffersLoanAmount(Global.customerAPI_Master_URL + CommonStrings.LOAN_AMOUNT_URL + customerId)

            // region ChangeAndClickListeners

            skLoanAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                    val rounded: Int = (progress + 999) / 1000 * 1000
                    val loanAmountVal = formatAmount(rounded.toString())

                    loanAmount = rounded.toString()
                    if (rounded <= 1000) {
                        tvLoanAmountVal.text = resources.getString(R.string.rupees_symbol) + 0
                        loanAmount = "0"
                    } else
                        tvLoanAmountVal.text = resources.getString(R.string.rupees_symbol) + loanAmountVal


                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

        skTenure.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                loanTenure = progress.toString()
                tvLoanTenureVal.text = "$loanTenure Years"


            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


        ivBackToRedDetails.setOnClickListener(OnClickListener { activity?.onBackPressed() })

        buttonLoanDetailsSubmit.setOnClickListener(OnClickListener {
            if (loanAmount.isNotEmpty() && loanTenure.isNotEmpty() &&
                    !tvLoanAmountVal.equals(getString(R.string.rupees_symbol)) &&
                    !tvLoanTenureVal.equals(getString(R.string.v2_tenure_lbl))) {
                initialCall = false
                bankViewModel.getBankOffersForLeadApplication(getBankRequest(), Global.customer_bank_baseURL + "get-recommended-bank")
            } else {
                showToast("Please select Loan Amount and Loan Tenure")
            }
        })

        // endregion ChangeAndClickListeners


    }


    // region Response

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onLoanAmountResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val loanAmountResponse: LoanAmountResponse? = mApiResponse.data as LoanAmountResponse?
                try {
                    val loanData: LoanData = loanAmountResponse?.data as LoanData

                    loanAmountMinimum = loanData.minValues.loanAmount
                    loanTenureMinimum = loanData.minValues.tenureInMonths / 12

                    loanAmountMaximum = loanData.maxValues.loanAmount
                    loanTenureMaximum = loanData.maxValues.tenureInMonths / 12

                    loanData.defaultValues.loanAmount.also { loanAmountDefault = it }
                    loanTenureDefault = loanData.defaultValues.tenureInMonths / 12

                    loanAmount = loanAmountDefault.toString()
                    loanTenure = loanTenureDefault.toString()

                    bankViewModel.getBankOffersForLeadApplication(getBankRequest(), Global.customer_bank_baseURL + "get-recommended-bank")

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                Log.i("SoftOfferFragment", "onBankResponse: " + ApiResponse.Status.ERROR)

            }
            else -> {
                Log.i("SoftOfferFragment", "onBankResponse: ")
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun onBankResponse(mApiResponse: ApiResponse) {

        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                if (!initialCall)
                    showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                if (!initialCall)
                    hideProgressDialog()

                val bankOfferRes: BankOffersForApplicationResponse? = mApiResponse.data as BankOffersForApplicationResponse?
                try {
                    if (bankOfferRes?.data?.isNotEmpty() == true) {

                        enableCalculatorLayout()
                        setData()

                        val bankOffersData: List<BankOffersData>? = bankOfferRes?.data as List<BankOffersData>
                        bankAdapter = SoftOfferAdapter(activity as Activity, bankOffersData!!, object : itemClickCallBack {
                            override fun itemClick(item: Any?, position: Int) {
                                callSetRecommendedBank(item)
                            }

                        })
                        val layoutManager = LinearLayoutManager(activity)
                        recyclerViewBankOffer.layoutManager = layoutManager

                        this.recyclerViewBankOffer.adapter = bankAdapter
                        setFocusOnView(tvBankOfferTitleV2)

                    } else {

                        //textViewNoDataFound.visibility = View.VISIBLE
                        llBankOfferParent.setBackgroundResource(R.drawable.v2_soft_offer_bg)
                        llSoftOfferDialog.visibility = View.GONE
                        linearLayoutCalculation.visibility = View.VISIBLE
                        tvBankOfferTitleV2.visibility = View.GONE
                        recyclerViewBankOffer.visibility = View.GONE

                        setData()
                        showToast("No Bank Offers found!")
                        setFocusOnView(tvBankOfferTitleV2)

                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                Log.i("SoftOfferFragment", "onBankResponse: " + ApiResponse.Status.ERROR)

            }
            else -> {
                Log.i("SoftOfferFragment", "onBankResponse: ")
            }
        }

        Log.i("TAG", "onBankResponse: " + mApiResponse.status)

    }

    private fun onSetBankRes(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val bankOfferRes: SelectRecommendedBankOfferResponse? = apiResponse.data as SelectRecommendedBankOfferResponse?
                if (bankOfferRes?.status == true) {
                    Log.i("TAG", "onBankResponse: " + bankOfferRes?.message)
                    addressViewModel.getCustomerDetails(
                            createCustomerDetailsRequest(customerId.toInt()),
                            Global.customerAPI_BaseURL + CommonStrings.CUSTOMER_DETAILS_END_URL)
                } else {
                    showToast(bankOfferRes?.message.toString())
                }

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                Log.i("SoftOfferFragment", "onBankResponse: " + ApiResponse.Status.ERROR)

            }
            else -> {
                Log.i("SoftOfferFragment", "onBankResponse: ")
            }
        }
        Log.i("TAG", "onBankResponse: " + apiResponse.status)
    }

    private fun onCustomerDetails(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val customerResponse: CustomerDetailsResponse? =
                        mApiResponse.data as CustomerDetailsResponse?
                if (customerResponse != null) {
                    customerDetailsResponse = customerResponse
                }
                enablePostOfferLayout()
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

    private fun onPinCodeResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val pinCodeResponse: PinCodeResponse? = mApiResponse.data as PinCodeResponse?
                if (pinCodeResponse?.statusCode == "100") {
                    val pinCodeData = pinCodeResponse.data
                    pincode = pinCodeData.pincode
                    state = pinCodeData.state
                    city = pinCodeData.city

                    linearLayoutAddNewPermanentAddress.visibility = View.VISIBLE
                    if (typeOfAddress == getString(R.string.v2_current_address)) {
                        linearLayoutAddNewCurrentAddress.removeAllViews()
                        addNewAddress(linearLayoutAddNewCurrentAddress, getString(R.string.v2_current_address))
                    } else if (typeOfAddress == getString(R.string.v2_permanent_address)) {
                        linearLayoutAddNewPermanentAddress.removeAllViews()
                        addNewAddress(linearLayoutAddNewPermanentAddress, getString(R.string.v2_permanent_address))
                    }

                } else
                    showToast(pinCodeResponse?.message.toString())
            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                showToast("Error")
            }
            else -> {
            }
        }
    }

    private fun onUpdateResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val response: SimpleResponse? = mApiResponse.data as SimpleResponse?
//                additionalFieldsViewModel.getAdditionalFieldsData(CustomerRequest(ResetCustomerJourneyDataRequest(customerId), CommonStrings.USER_TYPE, CommonStrings.USER_TYPE), Global.baseURL + CommonStrings.ADDITIONAL_FIELDS_URL)
                additionalFieldsViewModel.getAdditionalFieldsData(CustomerRequest(ResetCustomerJourneyDataRequest(customerId), CommonStrings.USER_TYPE, CommonStrings.USER_TYPE), Global.baseURL + CommonStrings.ADDITIONAL_FIELDS_URL)

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
            }
            else -> {
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun onAdditionalFieldsResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val response: AdditionalFields? = mApiResponse.data as AdditionalFields?
                if (response?.data != null && response.data.sections.isNotEmpty()) {

                    linearLayoutAddNewPermanentAddress.removeAllViews()
                    linearLayoutAddNewPermanentAddress.visibility = View.GONE
                    linearLayoutEditCurrentAddress.visibility = View.VISIBLE

                    if (!isPermanentAddress)
                        linearLayoutEditPermanentAddress.visibility = View.VISIBLE

                    additionalFieldsData = response.data
                    linearLayoutAdditionalFieldsUILayout.visibility = VISIBLE

                    setAdditionalField()

                } else {
                    navigateToBankOfferStatus(customerId, customerDetailsResponse, "SoftOffer")
                }
            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
            }
            else -> {
            }
        }
    }


    private fun onAdditionalFieldAPIResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val additionalFieldRes: APIDropDownResponse? = mApiResponse.data as APIDropDownResponse?
                checkList = additionalFieldRes?.data?.details!! as ArrayList<Details>
                getDTOList()
                refreshFieldView()

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                Log.i("SoftOfferFragment", "onBankResponse: " + ApiResponse.Status.ERROR)

            }
            else -> {
                Log.i("SoftOfferFragment", "onBankResponse: ")
            }
        }

    }


    private fun onSubmitOfAdditionFields(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val submitAdditionalFieldRes: CommonResponse = mApiResponse.data as CommonResponse
                if (submitAdditionalFieldRes.statusCode == "100") {
                    kycDocumentViewModel.getKYCDocumentResponse(Global.baseURL + CommonStrings.KYC_UPLOAD_URL_END_POINT + customerId)
                } else {
                    if (submitAdditionalFieldRes.message != null)

                        showToast(submitAdditionalFieldRes.message)

                }
            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                Log.i("SoftOfferFragment", "onBankResponse: " + ApiResponse.Status.ERROR)

            }
            else -> {
                Log.i("SoftOfferFragment", "onBankResponse: ")
            }
        }
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
                        navigateToKYCDocumentUpload(customerId, kycDocumentRes, caseID, customerDetailsResponse)
                    else if (kycDocumentRes.data.groupedDoc.isEmpty() && kycDocumentRes.data.nonGroupedDoc.isEmpty())
                        navigateToBankOfferStatus(customerId, customerDetailsResponse, "SoftOffer")
                } else {
                    navigateToBankOfferStatus(customerId, customerDetailsResponse, "SoftOffer")
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


    // endregion Response

    fun createCustomerDetailsRequest(customerId: Int): CustomerRequest {
        var customerDetailsRequest = CustomerRequest()
        customerDetailsRequest.UserId = CommonStrings.DEALER_ID
        customerDetailsRequest.UserType = CommonStrings.USER_TYPE
        var customerJourneyDataRequest = ResetCustomerJourneyDataRequest();
        customerJourneyDataRequest.CustomerId = customerId.toString()
        customerDetailsRequest.Data = customerJourneyDataRequest
        return customerDetailsRequest
    }
    // region additionalFieldMethods

    private fun setAdditionalField() {
        val sectionsList = additionalFieldsData.sections
        for (sectionIndex in sectionsList.indices) {
            val sectionData = sectionsList[sectionIndex]
            val fieldsList = sectionsList[sectionIndex].fields
            val sectionView = generateSectionUI(sectionData, sectionIndex == sectionsList.size - 1)
            linearLayoutAdditionalFieldsUILayout.addView(sectionView)

            if (sectionMap.size >= sectionIndex + 1)//|| sectionDataList.size == fieldDetails)
            {
                continue
            } else {
                break
            }
        }

    }

    private fun generateSectionUI(sectionData: Sections, isLastSection: Boolean): View {
        val fieldList = sectionData.fields
        var currentSectionLayout = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_address_parent_layout, linearLayoutAdditionalFieldsUILayout, false)
        if (isSectionPreFilled(sectionData.sectionName)) {
            currentSectionLayout = generateEditSectionUI(sectionData)
        } else {

            if (sectionData.type == "Address" || isLastSection) {

                // if section is not

                currentSectionLayout = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_address_parent_layout, linearLayoutAdditionalFieldsUILayout, false)

                val linearLayout = currentSectionLayout.findViewById<LinearLayout>(R.id.linearLayoutCustomAddressSectionLayout)

                val addressButton = currentSectionLayout.findViewById<Button>(R.id.buttonSubmitAddressDetails)

                if (sectionData.displayName) {
                    val currentSectionTitle: View = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_title_text_view, linearLayout, false)
                    val sectionTitle: TextView = currentSectionTitle.findViewById(R.id.textViewTitleLabel)
                    sectionTitle.text = sectionData.sectionName
                    linearLayout.addView(currentSectionTitle)
                }

                addressButton.setOnClickListener(View.OnClickListener {
                    if (currentFilledFieldData.size == fieldList.size) {
                        if (sectionData.type == "Address" && !isLastSection) {
                            addressButton.setBackgroundResource(R.drawable.vtwo_next_btn_bg)
                            moveCurrentDetailsToMap(sectionData.sectionName)
                        } else {
                            addressButton.setBackgroundResource(R.drawable.vtwo_next_btn_bg)
                            submitAdditionalFieldsList.putAll(currentFilledFieldData)
                            val fieldList: ArrayList<FieldDetails> = ArrayList<FieldDetails>(submitAdditionalFieldsList.values)
                            sectionMap[sectionData.sectionName] = fieldList
                            submitAdditionalFields()
                        }
                    } else {
                        Log.i("SoftOffer", "generateSectionUI: " + "" + currentFilledFieldData.size + "=====>" + fieldList.size)
                        showToast("Please fill all fields")
                    }
                })

                generateFieldUI(sectionData.sectionName, linearLayout, fieldList, true)

            } else {
                currentSectionLayout = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_parent_layout, linearLayoutAdditionalFieldsUILayout, false)
                val linearLayout = currentSectionLayout.findViewById<LinearLayout>(R.id.linearLayoutCustomParentLayout)
                if (sectionData.displayName) {
                    val currentSectionTitle: View = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_title_text_view, linearLayout, false)
                    val sectionTitle: TextView = currentSectionTitle.findViewById(R.id.textViewTitleLabel)
                    sectionTitle.text = sectionData.sectionName
                    linearLayout.addView(currentSectionTitle)
                }
                generateFieldUI(sectionData.sectionName, linearLayout, fieldList, false)
            }
        }
        return currentSectionLayout
    }


    private fun generateFieldUI(sectionName: String, linearLayout: LinearLayout?, fieldList: List<Fields>, isLastSection: Boolean) {

        val cFieldList = fieldList

        for (fieldIndex in fieldList.indices) {
            if (fieldList[fieldIndex].displayLabel) {
                val fieldTitleText = fieldList[fieldIndex].label
                val isMandatoryField = fieldList[fieldIndex].isMandatory
                val titleView: View = getTitleView(fieldTitleText, isMandatoryField)
                linearLayout?.addView(titleView)
            }

            val fieldVal: Fields = fieldList[fieldIndex]
            val fieldView: View? = linearLayout?.let { getFieldView(sectionName, fieldVal, cFieldList, fieldIndex == fieldList.size - 1, isLastSection, it) }

            linearLayout?.addView(fieldView)

            if (fieldList[fieldIndex].apiDetails.apiKey == "CompanyPincode" && isFieldFilled(fieldList[fieldIndex].apiDetails.apiKey).isEmpty())
                break
            else
                continue

        }

    }

    private fun getTitleView(title: String, isMandatory: Boolean): View {
        val currentFieldViewTitle: View = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_title_text_view, null, true)
        val fieldTitle: TextView = currentFieldViewTitle.findViewById(R.id.textViewTitleLabel)

        if (isMandatory) {
            val text = "$title "
            val colored = getString(R.string.lbl_asterick)
            val builder = SpannableStringBuilder()

            builder.append(text)
            val start = builder.length
            builder.append(colored)
            val end = builder.length

            builder.setSpan(ForegroundColorSpan(Color.RED), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            fieldTitle.text = builder
        } else {
            fieldTitle.text = title
        }
        return currentFieldViewTitle
    }

    private fun getFieldView(sectionName: String, fieldData: Fields, cFieldList: List<Fields>, isLastItem: Boolean, isLastSection: Boolean, linearLayout: LinearLayout): View {
        var currentFieldInputView = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_edit_text, linearLayout, false)

        when (fieldData.fieldType) {
            "Text" -> {
                currentFieldInputView = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_edit_text, linearLayout, false)
                val fieldInputValue: EditText = currentFieldInputView.findViewById(R.id.editTextFieldInput)
                fieldInputValue.hint = fieldData.placeHolder

                // prefill
                fieldInputValue.setText(isFieldFilled(fieldData.apiDetails.apiKey))
                fieldInputValue.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        if (!TextUtils.isEmpty(s.toString())) {

                            last_text_edit = System.currentTimeMillis()
                            handler.removeCallbacksAndMessages(null)
                            handler.postDelayed(input_finish_checker, delay)
                        } else {
                            showToast("Please enter Value")
                        }
                    }

                    val input_finish_checker = Runnable {
                        if (System.currentTimeMillis() > last_text_edit + delay - 500) {
                            if (fieldData.apiDetails.apiKey == "CompanyPincode") {

                                val editTextString: String = fieldInputValue.text.toString()
                                if (editTextString.length == 6) {
                                    additionaFieldPinCode = editTextString
                                    validateInput(sectionName, fieldData.apiDetails.apiKey, editTextString, isLastItem, fieldData.regexValidation, fieldData.apiDetails.apiKey, editTextString)
                                    refreshFieldView(sectionName, linearLayout, cFieldList, isLastSection)
                                } else {
                                    showToast("Enter valid Pincode")
                                }

                            } else if (isLastSection && isLastItem || sectionName == "Address" && isLastItem) {

                                val editTextString: String = fieldInputValue.text.toString()
                                val currentFieldDetails = FieldDetails(fieldData.apiDetails.apiKey, editTextString, editTextString)
                                addToCurrentFilledFieldData(fieldData.apiDetails.apiKey, currentFieldDetails, false, sectionName)

                            } else {
                                val editTextString: String = fieldInputValue.text.toString()
                                validateInput(sectionName, fieldData.apiDetails.apiKey, editTextString, isLastItem, fieldData.regexValidation, fieldData.apiDetails.apiKey, editTextString)
                            }

                        }
                    }

                })

                // getVal
                fieldInputValue.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                    if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || event != null && event.keyCode == KeyEvent.KEYCODE_BACK ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            actionId == EditorInfo.IME_ACTION_NEXT) {

                        if (fieldInputValue.text.isNotEmpty()) {

                            if (fieldData.apiDetails.apiKey == "CompanyPincode") {

                                val editTextString: String = fieldInputValue.text.toString()
                                if (editTextString.length == 6) {
                                    additionaFieldPinCode = editTextString
                                    validateInput(sectionName, fieldData.apiDetails.apiKey, editTextString, isLastItem, fieldData.regexValidation, fieldData.apiDetails.apiKey, editTextString)
                                    refreshFieldView(sectionName, linearLayout, cFieldList, isLastSection)
                                } else {
                                    showToast("Enter valid Pincode")
                                }

                            } else if (isLastSection && isLastItem || sectionName == "Address" && isLastItem) {

                                val editTextString: String = fieldInputValue.text.toString()
                                val currentFieldDetails = FieldDetails(fieldData.apiDetails.apiKey, editTextString, editTextString)
                                addToCurrentFilledFieldData(fieldData.apiDetails.apiKey, currentFieldDetails, false, sectionName)

                            } else {
                                val editTextString: String = fieldInputValue.text.toString()
                                validateInput(sectionName, fieldData.apiDetails.apiKey, editTextString, isLastItem, fieldData.regexValidation, fieldData.apiDetails.apiKey, editTextString)
                            }

                        } else
                            showToast("Please fill the field")
                    }
                    false
                })


            }
            "DropDown" -> {
                currentFieldInputView = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_input_text_view, linearLayout, false)
                val fieldInput: TextView = currentFieldInputView.findViewById(R.id.textViewDropDown)
                fieldInput.hint = fieldData.placeHolder
                fieldInput.text = isFieldFilled(fieldData.apiDetails.apiKey)

                if (fieldData.apiDetails.apiKey == "CompanyState" || fieldData.apiDetails.apiKey == "CompanyCity") {
                    setStateOrCityValue(sectionName, fieldData.apiDetails.apiKey, fieldInput, fieldData.apiDetails.url)
                }

                val apiURL = fieldData.apiDetails.url

                fieldInput.setOnClickListener(View.OnClickListener {
                    retrofitInterface.getFromWeb(apiURL)?.enqueue(object : Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            val strRes = Gson().toJson(response.body())
                            val dpRes = Gson().fromJson(strRes, APIDropDownResponse::class.java)
                            if (dpRes != null && dpRes.status) {
                                val optionList = dpRes.data.details
                                if (optionList.isNotEmpty() && optionList.size == 1) {
                                    if (listOf(optionList).any { true }) {
                                        val details = Details(optionList[0].displayLabel, optionList[0].value)
                                        fieldInput.text = details.displayLabel
                                        if (isLastSection && isLastItem) {
                                            val editTextString: String = details.value
                                            val currentFieldDetails = FieldDetails(fieldData.apiDetails.apiKey, editTextString, editTextString)
                                            addToCurrentFilledFieldData(fieldData.apiDetails.apiKey, currentFieldDetails, false, sectionName)
                                        } else {
                                            validateInput(sectionName, fieldData.apiDetails.apiKey, details.value, isLastItem, "", fieldData.apiDetails.apiKey, details.displayLabel)
                                        }
                                    } else {
                                        showToast("Something went wrong! Please try again!")
                                    }
                                } else if (optionList.isNotEmpty() && optionList.size > 1) {
                                    showDropDownDialog(fieldData.apiDetails.url, fieldData.label, optionList, object : AdditionalFieldsDetailsInterface {
                                        override fun returnDetails(details: Details) {
                                            fieldInput.text = details.displayLabel
                                            if (isLastSection && isLastItem) {
                                                val editTextString: String = details.value

                                                val currentFieldDetails = FieldDetails(fieldData.apiDetails.apiKey, editTextString, editTextString)
                                                addToCurrentFilledFieldData(fieldData.apiDetails.apiKey, currentFieldDetails, false, sectionName)
                                            } else {
                                                validateInput(sectionName, fieldData.apiDetails.apiKey, details.value, isLastItem, "", fieldData.apiDetails.apiKey, details.displayLabel)
                                            }

                                        }
                                    })

                                }

                            } else {
                                showToast("Something went wrong! Please try again!")
                            }
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            t.printStackTrace()
                        }

                    })


                })
            }
            "Check" -> {

                currentFieldInputView = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_check_type_layout, linearLayout, false)
                val recyclerView: RecyclerView = currentFieldInputView.findViewById(R.id.recyclerViewAdditionalField)
                retrofitInterface.getFromWeb(fieldData.apiDetails.url)?.enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        val strRes = Gson().toJson(response.body())
                        val dpRes = Gson().fromJson(strRes, APIDropDownResponse::class.java)
                        if (dpRes != null && dpRes.status) {
                            checkList = dpRes.data.details as ArrayList<Details>
                            getDTOList()
                            if (list.isNotEmpty()) {
                                additionalFieldAdapter = DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                                    override fun itemClick(item: Any?, position: Int) {

                                        additionalFieldAdapter.dataListFilter!!.forEachIndexed { index, item ->
                                            run {
                                                if (index == position) {
                                                    item.selected = true
                                                    val displayLabel: String = item.displayValue as String
                                                    val value: String = item.value as String

                                                    if (isLastSection && isLastItem) {

                                                        val currentFieldDetails = FieldDetails(fieldData.apiDetails.apiKey, value, displayLabel)
                                                        addToCurrentFilledFieldData(fieldData.apiDetails.apiKey, currentFieldDetails, false, sectionName)
                                                    } else {
                                                        val currentFieldDetails = FieldDetails(fieldData.apiDetails.apiKey, value, displayLabel)
                                                        addToCurrentFilledFieldData(fieldData.apiDetails.apiKey, currentFieldDetails, true, sectionName)
                                                    }


                                                } else {
                                                    item.selected = false
                                                }
                                            }
                                        }
                                        additionalFieldAdapter.notifyDataSetChanged()
                                    }
                                })


                                val layoutManagerStaggeredGridLayoutManager =
                                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                                val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

                                recyclerView.addItemDecoration(GridItemDecoration(25, 2))

                                recyclerView.layoutManager = layoutManagerStaggeredGridLayoutManager

                                recyclerView.adapter = additionalFieldAdapter
                            }


                        }
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        t.printStackTrace()
                    }
                })


            }
        }

        return currentFieldInputView
    }

    private fun setStateOrCityValue(sectionName: String, apiKey: String, fieldInput: TextView, url: String) {
        val apiURL = url + additionaFieldPinCode
        var textVal = ""
        retrofitInterface.getFromWeb(apiURL)?.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                val strRes = Gson().toJson(response.body())
                val dpRes = Gson().fromJson(strRes, APIDropDownResponse::class.java)
                if (dpRes != null && dpRes.status) {
                    val optionList = dpRes.data.details
                    if (optionList.isNotEmpty() && optionList.size == 1) {
                        if (listOf(optionList).any { true }) {
                            val details = Details(optionList[0].displayLabel, optionList[0].value)
                            fieldInput.text = details.displayLabel
                            textVal = fieldInput.text.toString()
                            val currentFieldDetails = FieldDetails(apiKey, details.value, details.displayLabel)
                            addToCurrentFilledFieldData(apiKey, currentFieldDetails, false, sectionName)
                        } else {
                            showToast("Something went wrong! Please try again!")
                        }
                    } else {
                        showToast("Something went wrong! Please try again!")
                    }
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun validateInput(sectionName: String, fieldName: String, editTextVal: String, lastItem: Boolean, regexResponse: String?, apiKey: String, displayKey: String) {
        if (editTextVal.isNotEmpty()) {

            if (regexResponse != null && regexResponse.isNotEmpty()) {
                val regex = Regex(regexResponse)
                if (regex.matches(editTextVal)) {
                    val currentFieldDetails = FieldDetails(apiKey, editTextVal, displayKey)
                    if (lastItem)
                        addToCurrentFilledFieldData(fieldName, currentFieldDetails, true, sectionName)
                    else
                        addToCurrentFilledFieldData(fieldName, currentFieldDetails, false, sectionName)

                } else {
                    showToast("Please enter valid $fieldName")
                }

            } else {
                val currentFieldDetails = FieldDetails(apiKey, editTextVal, displayKey)
                if (lastItem)
                    addToCurrentFilledFieldData(fieldName, currentFieldDetails, true, sectionName)
                else
                    addToCurrentFilledFieldData(fieldName, currentFieldDetails, false, sectionName)

            }
        } else {
            showToast("Please Fill the Field")
        }


        /*if (lastItem) {
            if (regexResponse?.contains('{') == true) {
                val regex = Regex(regexResponse)
                if (regex.matches(editTextVal)) {
                    val currentFieldDetails = FieldDetails(apiKey, editTextVal, displayKey)
                    addToCurrentFilledFieldData(fieldName, currentFieldDetails, true, sectionName)
                    //  moveCurrentDetailsToMap(sectionName)
                } else {
                    showToast("Please enter valid Input")
                }
            } else {
                val currentFieldDetails = FieldDetails(apiKey, editTextVal, displayKey)
                addToCurrentFilledFieldData(fieldName, currentFieldDetails, true, sectionName)
                //  moveCurrentDetailsToMap(sectionName)
                //refreshFieldView()
            }
        } else {
            val currentFieldDetails = FieldDetails(apiKey, editTextVal, displayKey)
            addToCurrentFilledFieldData(fieldName, currentFieldDetails, false, sectionName)
        }
*/

    }

    private fun addToCurrentFilledFieldData(fieldName: String, currentFieldDetails: FieldDetails, isLastItem: Boolean, sectionName: String) {
        currentFilledFieldData[fieldName] = currentFieldDetails
        Log.i("TAG", "addToCurrentFilledFieldData: " + currentFilledFieldData[fieldName]?.Value.toString())
        if (isLastItem)
            moveCurrentDetailsToMap(sectionName)
    }

    private fun moveCurrentDetailsToMap(sectionName: String) {

        submitAdditionalFieldsList.putAll(currentFilledFieldData)
        val fieldList: ArrayList<FieldDetails> = ArrayList<FieldDetails>(submitAdditionalFieldsList.values)

        sectionMap[sectionName] = fieldList
        currentFilledFieldData.clear()
        refreshFieldView()
    }

    private fun isSectionPreFilled(sectionName: String): Boolean {
        var isSectionFilled = false
        for ((key, value) in sectionMap) {
            if (key == sectionName) {
                if (listOf(value).any { it.isNotEmpty() }) {
                    isSectionFilled = true
                } else {
                    showToast("SomeFields are empty")
                }

            }
        }
        return isSectionFilled
    }


    private fun isFieldFilled(fieldName: String): String {
        var value = ""
        value = if (currentFilledFieldData.isNotEmpty() && currentFilledFieldData.containsKey(fieldName)) {
            val fieldData = currentFilledFieldData.getValue(fieldName)
            fieldData.DisplayLabel
        } else {
            ""
        }
        return value
    }

    private fun isFieldFilled1(fieldName: String): String {
        var value = ""
        value = if (submitAdditionalFieldsList.containsKey(fieldName)) {
            val fieldData = submitAdditionalFieldsList.getValue(fieldName)
            fieldData.DisplayLabel
        } else {
            ""
        }
        return value
    }

    private fun generateEditSectionUI(sectionData: Sections): View {
        var view: View = LayoutInflater.from(fragView.context).inflate(R.layout.v2_edit_custom_address, linearLayoutAdditionalFieldsUILayout, false)

        when (sectionData.type) {
            "Address" -> {
                view = LayoutInflater.from(fragView.context).inflate(R.layout.v2_edit_custom_address, linearLayoutAdditionalFieldsUILayout, false)
                val textViewOfficeAddressEdit = view.findViewById<TextView>(R.id.textViewOfficeAddressEdit)
                val imageViewEditOfficeAddress = view.findViewById<ImageView>(R.id.imageViewEditOfficeAddress)
                val textViewOfficeAddress1 = view.findViewById<TextView>(R.id.textViewOfficeAddress1)
                val textViewOfficeAddress2 = view.findViewById<TextView>(R.id.textViewOfficeAddress2)
                val textViewOfficeAddress3 = view.findViewById<TextView>(R.id.textViewOfficeAddress3)
                textViewOfficeAddressEdit.text = sectionData.sectionName
                textViewOfficeAddress1.text = isFieldFilled1(sectionData.fields[0].apiDetails.apiKey)
                textViewOfficeAddress2.text = isFieldFilled1(sectionData.fields[4].apiDetails.apiKey) + "," + isFieldFilled1(sectionData.fields[5].apiDetails.apiKey)
                textViewOfficeAddress3.text = isFieldFilled1(sectionData.fields[6].apiDetails.apiKey) + "," + isFieldFilled1(sectionData.fields[1].apiDetails.apiKey)
            }
            "Standard" -> {
                view = LayoutInflater.from(fragView.context).inflate(R.layout.v2_edit_dropdown_layout, linearLayoutAdditionalFieldsUILayout, false)
                val titleText = view.findViewById<TextView>(R.id.textViewLbl)
                val imageViewEdit: ImageView = view.findViewById(R.id.imageViewEditCurrentDropDown)
                val editValText: TextView = view.findViewById(R.id.textViewEditDropDown)
                titleText.text = sectionData.sectionName
                editValText.text = isFieldFilled1(sectionData.fields[0].apiDetails.apiKey)
            }
        }
        return view
    }


    private fun refreshFieldView(sectionName: String, linearLayout: LinearLayout, cFieldList: List<Fields>, isLastItem: Boolean) {
        linearLayout.removeAllViews()
        generateFieldUI(sectionName, linearLayout, cFieldList, isLastItem)
    }


    private fun refreshFieldView() {
        linearLayoutAdditionalFieldsUILayout.removeAllViews()
        setAdditionalField()
    }


    // endregion additionalFieldMethods
    private fun getBankRequest(): BankOffersForApplicationRequest {
        val leadApplicationData = LeadApplicationData(caseID, customerId, loanAmount.toInt(), loanTenure.toInt() * 12)
        return BankOffersForApplicationRequest(leadApplicationData, CommonStrings.DEALER_ID, CommonStrings.USER_TYPE)
    }

    private fun enableCalculatorLayout() {

        if (initialCall) {
            llBankOfferParent.setBackgroundResource(R.drawable.v2_soft_offer_bg)
            llSoftOfferDialog.visibility = View.GONE
            linearLayoutCalculation.visibility = View.VISIBLE
        }

        tvBankOfferTitleV2.visibility = View.VISIBLE
        recyclerViewBankOffer.visibility = View.VISIBLE
        textViewNoDataFound.visibility = View.GONE


    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {

        if (initialCall) {

            tvLoanTenureVal.text = "$loanTenureDefault Years"
            skLoanAmount.max = loanAmountMaximum

            if (loanAmountDefault != 1000) {
                tvLoanAmountVal.text = resources.getString(R.string.rupees_symbol) + loanAmountDefault
                skLoanAmount.progress = loanAmountDefault
            } else {
                tvLoanAmountVal.text = resources.getString(R.string.rupees_symbol) + 0
                skLoanAmount.progress = 100000

            }

            skLoanAmount.min = loanAmountMinimum

            skTenure.progress = loanTenureDefault
            skTenure.min = loanTenureMinimum
            skTenure.max = loanTenureMaximum

        } else {
            loanAmountDefault = loanAmount.toInt()
            loanTenureDefault = loanTenure.toInt()

            if (loanAmountDefault != 1000) {
                tvLoanAmountVal.text = resources.getString(R.string.rupees_symbol) + loanAmountDefault
                skLoanAmount.progress = loanAmountDefault
            } else {
                tvLoanAmountVal.text = resources.getString(R.string.rupees_symbol) + 0
                skLoanAmount.progress = 100000

            }

            tvLoanTenureVal.text = "$loanTenureDefault Years"

            skLoanAmount.max = loanAmountMaximum
            skLoanAmount.min = loanAmountMinimum

            skTenure.progress = loanTenureDefault
            skTenure.min = loanTenureMinimum
            skTenure.max = loanTenureMaximum

        }

    }

    private fun callSetRecommendedBank(item: Any?) {

        // val bankOfferData = BankOfferData(caseID, customerId, )

        val bankOffersData = BankOfferData(caseID, customerId, item.toString())
        val bankOffersForApplicationRequest = SelectRecommendedBankOfferRequest(bankOffersData, CommonStrings.DEALER_ID, CommonStrings.USER_TYPE)
        bankViewModel.setSelectRecommendedBankOffer(bankOffersForApplicationRequest, Global.customer_bank_baseURL + "select-recommended-bank")
    }

    private fun enablePostOfferLayout() {

        linearLayoutCalculation.visibility = View.GONE
        llBankOfferParent.setBackgroundResource(0)
        linearLayoutPostOffer.visibility = View.VISIBLE
        textViewSelectBankLabel.text = "You have selected " + customerDetailsResponse.data?.loanDetails?.bankName + " bank"

        addNewAddress(linearLayoutAddNewCurrentAddress, getString(R.string.v2_current_address))
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }

    }


    private fun addNewAddress(linearLayout: LinearLayout, title: String) {

        if (title == getString(R.string.v2_permanent_address))
            linearLayoutEditCurrentAddress.visibility = View.VISIBLE

        val addressView: View = LayoutInflater.from(fragView.context).inflate(R.layout.v2_add_new_address_layout, linearLayout, false)
        val textViewTypeOfAddress = addressView.findViewById<TextView>(R.id.textViewTypeOfAddress)
        val editTextPinCode = addressView.findViewById<EditText>(R.id.editTextPinCode)
        val buttonPinCodeCheck = addressView.findViewById<Button>(R.id.buttonPincodeCheck)
        val textViewState = addressView.findViewById<TextView>(R.id.textViewState)
        val textViewCity = addressView.findViewById<TextView>(R.id.textViewCity)
        val textViewCityMovedInLbl = addressView.findViewById<TextView>(R.id.textViewCityMovedInLbl)
        val linearLayoutCityMovedInYear = addressView.findViewById<LinearLayout>(R.id.linearLayoutCityMovedInYear)
        val editTextCityMovedInYear = addressView.findViewById<EditText>(R.id.editTextCityMovedInYear)

        val editTextAddress1 = addressView.findViewById<EditText>(R.id.editTextAddress1)
        val editTextAddress2 = addressView.findViewById<EditText>(R.id.editTextAddress2)
        val editTextAddress3 = addressView.findViewById<EditText>(R.id.editTextAddress3)
        val checkboxIsPermanentAdd = addressView.findViewById<CheckBox>(R.id.checkboxIsPermanentAdd)

        val buttonSubmitAddress = addressView.findViewById<Button>(R.id.buttonSubmitAddress)

        textViewTypeOfAddress.text = title
        setFocusOnView(textViewTypeOfAddress)

        typeOfAddress = title
        if (title == getString(R.string.v2_current_address)) {
            checkboxIsPermanentAdd.visibility = View.VISIBLE
        } else {
            checkboxIsPermanentAdd.visibility = View.GONE
            linearLayoutCityMovedInYear.visibility = View.GONE
            textViewCityMovedInLbl.visibility = View.GONE
        }
        editTextPinCode.setText(pincode)
        textViewState.text = state
        textViewCity.text = city

        linearLayoutCityMovedInYear.setOnClickListener(View.OnClickListener {
            var lastSelectedDate = ""

            /*  lastSelectedDate = if (editTextCityMovedInYear.text.toString().isNotEmpty())
                  editTextCityMovedInYear.text.toString()
              else
                  getTodayDate().toString()
  */

            callDatePickerDialog(
                    lastSelectedDate,
                    null,
                    getTodayDate(),
                    object : DatePickerCallBack {
                        override fun dateSelected(dateDisplayValue: String, dateValue: String) {
                            editTextCityMovedInYear.setText(dateDisplayValue)
                            cityMovedInYear = dateValue
                        }
                    })
        })

        checkboxIsPermanentAdd.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkboxIsPermanentAdd.isChecked = isChecked
                isPermanentAddress = checkboxIsPermanentAdd.isChecked
            } else {
                checkboxIsPermanentAdd.isChecked = isChecked
                isPermanentAddress = checkboxIsPermanentAdd.isChecked

            }

        }
        buttonPinCodeCheck.setOnClickListener(View.OnClickListener {
            if (editTextPinCode.text.toString().isNotEmpty() && editTextPinCode.text.toString().length == 6) {
                pinCodeViewModel.getPinCodeData(Global.customerDetails_BaseURL + "Pincode/city/" + editTextPinCode.text.toString())
            } else
                showToast("Please enter valid PinCode")
        })

        buttonSubmitAddress.setOnClickListener(View.OnClickListener {
            if (editTextPinCode.text.toString().isNotEmpty() &&
                    textViewState.text.toString().isNotEmpty() &&
                    textViewCity.text.toString().isNotEmpty() &&
                    editTextAddress1.text.toString().isNotEmpty() &&
                    editTextAddress2.text.toString().isNotEmpty() &&
                    editTextAddress3.text.toString().isNotEmpty()) {

                if (linearLayoutCityMovedInYear.visibility == GONE && cityMovedInYear.isEmpty()) {
                    showToast("Please select city moved in year")
                } else {
                    address1 = editTextAddress1.text.toString()
                    address2 = editTextAddress2.text.toString()
                    address3 = editTextAddress3.text.toString()
                    address = "$address1***$address2***$address3"

                    if (typeOfAddress == getString(R.string.v2_current_address)) {
                        submitCurrentAddress()
                    } else if (typeOfAddress == getString(R.string.v2_permanent_address)) {
                        submitPermanentAddress()
                    }

                }

            } else {
                showToast("Please enter all Fields")
            }
        })
        linearLayout.addView(addressView)
    }


    private fun submitCurrentAddress() {
        textViewCurrentAddress1.text = address1
        textViewCurrentAddress2.text = address2
        textViewCurrentAddress3.text = address3 + ", " + pincode


        currentAddress = CurrentAddress(isPermanentAddress, pincode, address)
        if (isPermanentAddress) {
            permanentAddress = PermanentAddress(pincode, address)
            val addressData = AddressData(customerId.toInt(), currentAddress, permanentAddress, cityMovedInYear)
            val updateAddressRequest = UpdateAddressRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, addressData)

            addressViewModel.updateAddress(updateAddressRequest, Global.customerAPI_BaseURL + CommonStrings.UPDATE_ADDRESS_URL)
            checkboxCurrentAndPermanentAddress.isChecked = true
            checkboxCurrentAndPermanentAddress.isClickable = false
            checkboxCurrentAndPermanentAddress.isFocusable = false


        } else {
            linearLayoutAddNewPermanentAddress.visibility = View.VISIBLE
            pincode = ""
            state = ""
            city = ""
            address1 = ""
            address2 = ""
            address3 = ""

            addNewAddress(linearLayoutAddNewPermanentAddress, getString(R.string.v2_permanent_address))
            checkboxCurrentAndPermanentAddress.visibility = GONE
        }

        linearLayoutAddNewCurrentAddress.visibility = View.GONE

    }

    private fun submitPermanentAddress() {

        textViewPermanentAddress1.text = address1
        textViewPermanentAddress2.text = address2
        textViewPermanentAddress3.text = "$address3, $pincode"
        permanentAddress = PermanentAddress(pincode, address)
        val addressData = AddressData(customerId.toInt(), currentAddress, permanentAddress, cityMovedInYear)
        val updateAddressRequest = UpdateAddressRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, addressData)

        addressViewModel.updateAddress(updateAddressRequest, Global.customerAPI_BaseURL + CommonStrings.UPDATE_ADDRESS_URL)
    }

    private fun submitAdditionalFields() {
        val fieldList: ArrayList<FieldDetails> = ArrayList<FieldDetails>(submitAdditionalFieldsList.values)

        val fieldDataRequest = FieldData(customerId.toInt(), fieldList)
        val submitAdditionalFieldsRequest = SubmitAdditionalFieldRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, fieldDataRequest)
        submitAdditionalFieldsViewModel.submitAdditionalFields(submitAdditionalFieldsRequest, Global.customerAPI_BaseURL + "submit-additional-details")
    }

    private fun getDTOList() {
        list = arrayListOf<DataSelectionDTO>()
        checkList.forEachIndexed { index, types ->
            list.add(DataSelectionDTO(types.displayLabel, null, types.value, false))
        }
    }

    private fun showDropDownDialog(apiURL: String, title: String, optionList: List<Details>, detailsCallBack: AdditionalFieldsDetailsInterface) {

        val returnDetailsCallBack: AdditionalFieldsDetailsInterface = detailsCallBack
        val dialog = Dialog(fragView.context, R.style.FullScreenDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.v2_additional_fields_drop_down_options_layout)

        val textViewTitle: TextView = dialog.findViewById(R.id.textViewSelectTitle) as TextView
        val editTextAdditionalFieldsSearchOption: EditText = dialog.findViewById(R.id.editTextAdditionalFieldsSearch)
        val backToSoftOffer = dialog.findViewById<ImageView>(R.id.imageViewBackToSoftOffer)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewOptions)
        textViewTitle.text = title

        var valueSet = false
        var details = Details("", "")
        backToSoftOffer.setOnClickListener(View.OnClickListener {
            valueSet = false
            dialog.dismiss()
        })

        editTextAdditionalFieldsSearchOption.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard(editTextAdditionalFieldsSearchOption)
            }
            false
        })

        var reviewAdapter = AdditionalFieldsAdapter(apiURL, optionList, object : AdditionalFieldsDetailsInterface {
            override fun returnDetails(details:Details) {
                returnDetailsCallBack.returnDetails(details)
                dialog.dismiss()
            }
        })

        val layoutManager = LinearLayoutManager(fragView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = reviewAdapter

        editTextAdditionalFieldsSearchOption.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                if (s != "") {
                    handler.removeCallbacks(input_finish_checker)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (!TextUtils.isEmpty(s.toString())) {

                    last_text_edit = System.currentTimeMillis()
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed(input_finish_checker, delay)
                } else {
                    reviewAdapter.updateList(optionList)
                }
            }

            val input_finish_checker = Runnable {
                if (System.currentTimeMillis() > last_text_edit + delay - 500) {
                    showProgressDialog(fragView.context)

                    retrofitInterface.getFromWeb(apiURL + editTextAdditionalFieldsSearchOption.text.toString())?.enqueue(object : Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            val strRes = Gson().toJson(response.body())
                            val dpRes = Gson().fromJson(strRes, APIDropDownResponse::class.java)
                            if (dpRes != null && dpRes.status) {
                                val filteredOptionList: List<Details> = dpRes.data.details
                                if (filteredOptionList.isNotEmpty()) {
                                    hideProgressDialog()
                                    reviewAdapter.updateList(filteredOptionList)
                                } else {
                                    hideProgressDialog()
                                    showToast("No filter found for your query")
                                }
                            }
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                }
            }
        })
        dialog.show()
    }

    private fun getStringList(filteredOptionList: List<Details>): List<String> {
        val stringList = ArrayList<String>()
        for (option in filteredOptionList.indices) {
            stringList.add(filteredOptionList[option].displayLabel)
        }

        return stringList
    }

    private fun setFocusOnView(textView: TextView) {
        scrollViewBankOffer.post(Runnable { textView.top.let { scrollViewBankOffer.scrollTo(0, it) } })
    }

}

/*
*             /*if (isLastSection) {
                    currentSectionLayout = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_address_parent_layout, linearLayoutAdditionalFieldsUILayout, false)

                    val linearLayout = currentSectionLayout.findViewById<LinearLayout>(R.id.linearLayoutCustomAddressSectionLayout)

                    val submitAdditionalFieldsButton = currentSectionLayout.findViewById<Button>(R.id.buttonSubmitAddressDetails)
                    submitAdditionalFieldsButton.setBackgroundResource(R.drawable.v2_rounded_light_grey_bg)

                    if (sectionData.displayName) {
                        val currentSectionTitle: View = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_title_text_view, linearLayout, false)
                        val sectionTitle: TextView = currentSectionTitle.findViewById(R.id.textViewTitleLabel)
                        sectionTitle.text = sectionData.sectionName
                        linearLayout.addView(currentSectionTitle)
                    }

                    submitAdditionalFieldsButton.setOnClickListener(View.OnClickListener {
                        if (currentFilledFieldData.size == fieldList.size) {
                            submitAdditionalFieldsButton.setBackgroundResource(R.drawable.vtwo_next_btn_bg)
                            submitAdditionalFieldsList.putAll(currentFilledFieldData)
                            val fieldList: ArrayList<FieldDetails> = ArrayList<FieldDetails>(submitAdditionalFieldsList.values)
                            sectionMap[sectionData.sectionName] = fieldList
                            submitAdditionalFields()
                        }
                        else
                        {
                            showToast("Please fill all the fields")
                            Log.i("SoftOffer", "generateSectionUI: " + "" + currentFilledFieldData.size + "=====" + fieldList.size)

                        }


                    })


                    generateFieldUI(sectionData.sectionName, linearLayout, fieldList)

                } else {*/
    */