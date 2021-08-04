package v2.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.core.app.ActivityCompat
import androidx.core.view.marginLeft
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.dto.AddLeadRequest
import v2.model.dto.CustomLoanProcessCompletedData
import v2.model.dto.DataSelectionDTO
import v2.model.enum_class.ApplicationStatusEnum
import v2.model.enum_class.ScreenTypeEnum
import v2.model.request.ApplicationListRequest
import v2.model.request.ApplicationListRequestData
import v2.model.request.CustomerRequest
import v2.model.request.ResetCustomerJourneyDataRequest
import v2.model.request.add_lead.AddLeadData
import v2.model.request.add_lead.AddLeadVehicleDetails
import v2.model.request.add_lead.BasicDetails
import v2.model.response.*
import v2.model.response.master.KYCDocumentResponse
import v2.model_view.DashboardViewModel
import v2.model_view.MasterViewModel
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.ApplicationListAdapter
import v2.view.adapter.DataRecyclerViewAdapter
import v2.view.adapter.PartnerBankRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.ApplicationListClickCallBack
import v2.view.callBackInterface.itemClickCallBack

import java.util.*


class ApplicationListFragment : BaseFragment(), View.OnClickListener {

    lateinit var llProgress: LinearLayout
    lateinit var tvTitle: TextView
    lateinit var tvBankName: TextView
    lateinit var tvResultCount: TextView
    lateinit var ivBack: ImageView
    lateinit var ivNotification: ImageView
    lateinit var ivSearch: ImageView
    lateinit var ivStartSearch: ImageView
    lateinit var rvData: RecyclerView
    lateinit var rvBankList: RecyclerView
    lateinit var llData: LinearLayout
    lateinit var llNoDataFound: LinearLayout
    lateinit var llSearch: LinearLayout
    lateinit var llSearchSection: LinearLayout
    lateinit var viewEmptyBlack: View
    lateinit var etSearch: EditText
    lateinit var tvSearchResult: TextView
    lateinit var cust: CustomerDetailsResponse

    lateinit var screenType: String
    var screenStatus: String? = null
    var rootView: View? = null
    lateinit var transactionViewModel: TransactionViewModel
    lateinit var masterViewModel: MasterViewModel
    lateinit var bankNameDataRecyclerViewAdapter: DataRecyclerViewAdapter

    var PER_PAGE: Int = 10
    var PAGE_NUMBER: Int = 0
    var TOTAL: Int = 0
    var selectedBankName: String? = null
    lateinit var applicationListAdapter: ApplicationListAdapter

    var layoutManager: LinearLayoutManager? = null
    var isLoading: Boolean = false
    var selectedCustomerId: Int = 0
    var timerWait: Timer? = null
    var allowEditCity: Boolean = true

    lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        transactionViewModel = ViewModelProvider(this).get(
            TransactionViewModel::class.java
        )

        masterViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)

        transactionViewModel.getApplicationListLiveData()
            .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                onApplicationList(
                    mApiResponse!!
                )
            })
        transactionViewModel.getCustomerDetailsLiveData()
            .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                onCustomerDetails(
                    mApiResponse!!
                )
            })

        masterViewModel.getKYCDocumentLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onGetKYCDocumentResponse(mApiResponse!!)
            }

        dashboardViewModel.getRuleEngineBanksLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onRuleEngineBanksResponse(mApiResponse!!)
            }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView != null) {

            return rootView

        } else {
            rootView = inflater.inflate(R.layout.v2_fragment_application_list, container, false)
            arguments?.let {
                val safeArgs = ApplicationListFragmentArgs.fromBundle(it)
                screenType = safeArgs.screenType
                screenStatus = safeArgs.screenStatus
            }
            initializationOfObject()
        }
        return rootView
    }

    fun initializationOfObject() {
        tvTitle = rootView!!.findViewById(R.id.tv_title)
        tvBankName = rootView!!.findViewById(R.id.tv_bank_name)
        llProgress = rootView!!.findViewById(R.id.ll_progress)
        tvResultCount = rootView!!.findViewById(R.id.tv_result_count)
        ivBack = rootView!!.findViewById(R.id.iv_back)
        ivNotification = rootView!!.findViewById(R.id.iv_notification)
        ivSearch = rootView!!.findViewById(R.id.iv_search)
        ivStartSearch = rootView!!.findViewById(R.id.iv_start_search)
        rvData = rootView!!.findViewById(R.id.rv_data)
        rvBankList = rootView!!.findViewById(R.id.rv_bank_list)
        llData = rootView!!.findViewById(R.id.ll_data)
        llNoDataFound = rootView!!.findViewById(R.id.ll_no_data_found)
        viewEmptyBlack = rootView!!.findViewById(R.id.view_empty_black)
        llSearchSection = rootView!!.findViewById(R.id.ll_search_section)
        llSearch = rootView!!.findViewById(R.id.ll_search)
        etSearch = rootView!!.findViewById(R.id.et_search)
        tvSearchResult = rootView!!.findViewById(R.id.tv_search_result)

        tvSearchResult.visibility = View.GONE

        tvResultCount.visibility = View.GONE
        llNoDataFound.visibility = View.GONE
        llData.visibility = View.GONE
        llProgress.visibility = View.GONE

        rvBankList.visibility = View.GONE
        tvBankName.visibility = View.GONE


        ivBack.setOnClickListener(this)
        ivNotification.setOnClickListener(this)
        ivSearch.setOnClickListener(this)
        ivStartSearch.setOnClickListener(this)
        llSearch.setOnClickListener(this)
        etSearch.setOnClickListener(this)

        callData()
        setTextChangeOfetAutoResidenceCity()

        if (screenStatus == getString(R.string.v2_logged_in_title) || screenStatus == getString(R.string.v2_soft_offer_title) || screenStatus == ScreenTypeEnum.Approved.value || screenStatus == ScreenTypeEnum.Disbursed.value) {
            dashboardViewModel.getRuleEngineBanks(
                Global.baseURL + CommonStrings.GET_RULE_ENGINE_BANKS_END_POINT
            )
        }
    }

    fun callData() {
        when (screenType) {
            ScreenTypeEnum.Search.value -> {
                ivSearch.visibility = View.GONE
                tvTitle.text = "Search"
                etSearch.requestFocus()
                showKeyBoardByForced()

            }
            ScreenTypeEnum.StausWithSearch.value -> {
                ivSearch.visibility = View.GONE
                tvTitle.text = screenStatus
                callApplicationStatusWiseFilterAPI(null)
                etSearch.requestFocus()
                showKeyBoardByForced()
            }
            else -> {
                tvTitle.text = screenStatus
                rvData.setPadding(0, llData.marginLeft, 0, 0)
                ivNotification.visibility = View.GONE
                llSearchSection.visibility = View.GONE
                viewEmptyBlack.visibility = View.GONE
                callApplicationStatusWiseFilterAPI(null)
            }
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_back -> {
                    activity?.onBackPressed()
                }
                R.id.iv_notification -> {
                    navigateNoticeBoardAndNotificationFragment(ScreenTypeEnum.Notification.value)
                }
                R.id.iv_search -> {
                    navigateApplicationListFragment(
                        ScreenTypeEnum.StausWithSearch.value,
                        screenStatus
                    )

                }
                R.id.iv_start_search -> {
                    if (timerWait != null) {
                        timerWait!!.cancel();
                    }
                    startSearchApiCall()

                }
                R.id.ll_search -> {
                    etSearch.requestFocus()
                    showKeyBoardByForced()
                }
                R.id.et_search -> {
                    etSearch.requestFocus()
                    showKeyBoardByForced()
                }
            }
        }
    }

    private fun startSearchApiCall() {
        if (!TextUtils.isEmpty(etSearch.text.toString())) {
            PAGE_NUMBER = 0
            if (screenType == ScreenTypeEnum.Search.value) {
                callSearchAPI()
            } else {
                callApplicationStatusWiseFilterAPI(etSearch.text.toString())
            }

        }
    }

    private fun setTextChangeOfetAutoResidenceCity() {
        var timerWait: Timer? = null
        var allowEditCity: Boolean = true

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                try {
                    if (timerWait != null) {
                        timerWait!!.cancel();

                    }
                    allowEditCity = true
                    if (TextUtils.isEmpty(etSearch.text.toString())) {
                        allowEditCity = true
                    }
                    if (allowEditCity) {
                        timerWait = Timer()
                        timerWait!!.schedule(object : TimerTask() {
                            override fun run() {


                                allowEditCity = false
                                ThreadUtils.runOnUiThread(Runnable {
                                    //call Search
                                    if (!TextUtils.isEmpty(etSearch.text.toString())) {
                                        PAGE_NUMBER = 0
                                        if (screenType == ScreenTypeEnum.Search.value) {
                                            callSearchAPI()
                                        } else {
                                            callApplicationStatusWiseFilterAPI(etSearch.text.toString())
                                        }
                                    }
                                    startSearchApiCall()
                                });


                            }
                        }, 3000)
                    } else {

                    }
                } catch (e: Exception) {

                }
            }
        })

        etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                startSearchApiCall()
            }
            false
        })
    }

    private fun callSearchAPI() {
        PAGE_NUMBER += 1
        transactionViewModel.getApplicationList(
            ApplicationListRequest(
                ApplicationListRequestData(
                    etSearch.text.toString(),
                    null,
                    selectedBankName,
                    PAGE_NUMBER,
                    PER_PAGE
                ), CommonStrings.DEALER_ID,
                CommonStrings.USER_TYPE
            ),
            Global.customerAPI_BaseURL + CommonStrings.SEARCH_APPLICATION_END_POINT
        )
    }

    private fun callApplicationStatusWiseFilterAPI(searchKey: String?) {
        PAGE_NUMBER += 1
        var url =
            Global.customerAPI_BaseURL + CommonStrings.APPLICATION_STATUS_WISE_FILTER_END_POINT

        if (selectedBankName != null) {
            url = Global.customerAPI_BaseURL + CommonStrings.APPLICATION_STATUS_WISE_FILTER_END_POINT
          /*  url =
                Global.customerAPI_BaseURL + CommonStrings.APPLICATION_BANK_WISE_FILTER_END_POINT*/
        }
        transactionViewModel.getApplicationList(
            ApplicationListRequest(
                ApplicationListRequestData(
                        selectedBankName,
                    screenStatus?.replace("\\s".toRegex(), ""),
                    searchKey,
                    PAGE_NUMBER,
                    PER_PAGE
                ), CommonStrings.DEALER_ID,
                CommonStrings.USER_TYPE
            ), url

        )
    }

    fun callCustomerDetailsApi(customerIdValue: Int) {

        transactionViewModel.getCustomerDetails(
            createCustomerDetailsRequest(customerIdValue),
            Global.customerAPI_BaseURL + CommonStrings.CUSTOMER_DETAILS_END_URL
        )

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

    private fun setResultData(response: ApplicationListResponse?) {
        if (selectedBankName != null) {
            tvBankName.text = selectedBankName
        }
        if (response?.data != null && response.data?.customers != null && response.data?.customers?.size!! > 0) {

            if (PAGE_NUMBER == 1) {
                TOTAL = response.data!!.total!!
                if (TOTAL == 0) {
                    rvData.visibility = View.GONE
                    llNoDataFound.visibility = View.VISIBLE
                    return
                } else {
                    rvData.visibility = View.VISIBLE
                    llNoDataFound.visibility = View.GONE
                }
                layoutManager = LinearLayoutManager(activity)
                rvData.layoutManager = layoutManager
                tvResultCount.visibility = View.VISIBLE
                if (TOTAL > 1) {
                    tvResultCount.text = "Total $TOTAL leads"
                } else {
                    tvResultCount.text = "Total $TOTAL lead"
                }

                applicationListAdapter =
                    ApplicationListAdapter(
                        ApplicationListFragment@ this,
                        response.data!!.customers!!,
                        object :
                            ApplicationListClickCallBack {
                            override fun onItemClick(item: Any?, position: Int) {
                                var applicationDataItems = item as ApplicationDataItems
                                navigateApplicationDetailsFragment(applicationDataItems!!.customerId!!)
                            }

                            override fun onCompleteClick(item: Any?, position: Int) {
                                var applicationDataItems = item as ApplicationDataItems
                                selectedCustomerId = applicationDataItems!!.customerId!!
                                callCustomerDetailsApi(applicationDataItems!!.customerId!!)
                            }

                            override fun onCallClick(item: Any?, position: Int) {
                                var applicationDataItems = item as ApplicationDataItems
                                if (checkCallPermissions()) {
                                    if (applicationDataItems.customerMobile!!.isNotEmpty()) {
                                        makeCallOfMobileNumber(applicationDataItems.customerMobile!!)
                                    }
                                } else {
                                    askCallPermissions()
                                }


                            }

                        })
                llData.visibility = View.VISIBLE
                rvData.adapter = applicationListAdapter
            } else {
                applicationListAdapter.dataListFilter =
                    applicationListAdapter.dataListFilter!!.plus(response!!.data!!.customers!!)

                applicationListAdapter.notifyDataSetChanged()
            }


            rvData.addOnScrollListener(recyclerViewOnScrollListener)

        } else if (response!!.data!!.total == 0) {
            llNoDataFound.visibility = View.VISIBLE
            rvData.visibility = View.GONE
            tvResultCount.text = "Total 0 leads"
        }


    }

    private fun checkForNextScreenAfterCustomerResponse(customerResponse: CustomerDetailsResponse?) {

        if ((customerResponse!!.data!!.status.equals(ApplicationStatusEnum.Registered.value) && customerResponse!!.data!!.subStatus.equals(
                ApplicationStatusEnum.Registered.value
            )) ||
            (customerResponse!!.data!!.status.equals(ApplicationStatusEnum.Registered.value) && customerResponse!!.data!!.subStatus.equals(
                ApplicationStatusEnum.Employment_Details_Submitted.value
            )) ||
            (customerResponse!!.data!!.status.equals(ApplicationStatusEnum.KYC_Done.value) && customerResponse.data!!.subStatus.equals(
                ApplicationStatusEnum.KYC_Done.value
            )) ||
            (customerResponse!!.data!!.status.equals(ApplicationStatusEnum.KYC_Done.value) && customerResponse.data!!.subStatus.equals(
                ApplicationStatusEnum.Employment_Details_Submitted.value
            ))

        ) {
            var addLeadRequest = AddLeadRequest()
            var vehicleDetails = AddLeadVehicleDetails()

            vehicleDetails!!.RegistrationYear =
                customerResponse.data!!.vehicleDetails!!.registrationYear
            vehicleDetails!!.Make = customerResponse.data!!.vehicleDetails!!.make
            vehicleDetails!!.Model = customerResponse.data!!.vehicleDetails!!.model
            vehicleDetails!!.Variant = customerResponse.data!!.vehicleDetails!!.variant
            vehicleDetails!!.Ownership = customerResponse.data!!.vehicleDetails!!.ownership
            vehicleDetails!!.VehicleNumber = customerResponse.data!!.vehicleDetails!!.vehicleNumber
            vehicleDetails!!.KMs = customerResponse.data!!.vehicleDetails!!.kMs
            vehicleDetails!!.FuelType = customerResponse.data!!.vehicleDetails!!.fuelType

            var basicDetails = BasicDetails()
            basicDetails.FirstName =
                customerResponse.data!!.basicDetails!!.firstName
            basicDetails.LastName =
                customerResponse.data!!.basicDetails!!.lastName
            basicDetails.Email =
                customerResponse.data!!.basicDetails!!.email
            basicDetails.Salutation =
                customerResponse.data!!.basicDetails!!.salutation
            basicDetails.CustomerMobile =
                customerResponse.data!!.basicDetails!!.customerMobile

            var data = AddLeadData()

            data!!.addLeadVehicleDetails = vehicleDetails
            data!!.basicDetails = basicDetails
            addLeadRequest.Data = data
            navigateToAddLeadFragment(
                addLeadRequest,
                selectedCustomerId,
                customerResponse.data!!.basicDetails!!.customerMobile
            )
        } else {
            when (customerResponse.data?.status) {
                getString(R.string.v2_lead_status_kyc_done) -> {
                    navToSoftOffer(
                        customerResponse,
                        selectedCustomerId.toString(),
                        CommonStrings.APPLICATION_LIST_FRAGMENT_TAG
                    )
                }
                ApplicationStatusEnum.Lender_Selected.value -> {
                    navigateToAddressAdditionalFields(selectedCustomerId, customerResponse,"")
                }
                ApplicationStatusEnum.Bank_Form_Filled.value -> {
                    cust = customerResponse
                    masterViewModel.getKYCDocumentResponse(Global.baseURL + CommonStrings.KYC_UPLOAD_URL_END_POINT + selectedCustomerId)
                }
                ApplicationStatusEnum.Document_Uploaded.value -> {
                    navigateToBankOfferStatusFromApplicationListFrag(
                        selectedCustomerId,
                        customerResponse
                    )
                }
                ApplicationStatusEnum.Submitted_To_Bank.value -> {
                    /*val salutation = customerResponse.data?.basicDetails?.salutation
                    val name =
                        customerResponse.data?.basicDetails?.firstName + " " + customerResponse.data?.basicDetails?.lastName
                    val caseId = customerResponse.data?.caseId
                    caseId?.let { CustomLoanProcessCompletedData(selectedCustomerId,salutation + " " + name, it) }
                        ?.let { navigateToBankSuccessPageFromSoftOffer(it) }
*/
                    navigateToLeadDetails(selectedCustomerId)
                }
            }

        }

    }

    private fun setPartnerBanksDetails(ruleEngineBanksResponse: RuleEngineBanksResponse) {
        ruleEngineBanksResponse.data
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvBankList.layoutManager = layoutManager

        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()
        if (ruleEngineBanksResponse.data != null && ruleEngineBanksResponse.data!!.isNotEmpty()) {

            ruleEngineBanksResponse.data!!.forEachIndexed { index, bankItem ->
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

            bankNameDataRecyclerViewAdapter =
                DataRecyclerViewAdapter(
                    activity as Activity,
                    list,
                    object : itemClickCallBack {
                        override fun itemClick(item: Any?, position: Int) {
                            var selectedBankDataSelectionDTO: DataSelectionDTO =
                                item as DataSelectionDTO
                            updateBankSelection(selectedBankDataSelectionDTO.displayValue!!)
                            selectedBankName = selectedBankDataSelectionDTO.displayValue!!
                            PAGE_NUMBER = 0
                            callData()
                            rvBankList.scrollToPosition(position)
                        }
                    })
            bankNameDataRecyclerViewAdapter.unSelectedBackgroundResource = R.drawable.v2_white_bg
            bankNameDataRecyclerViewAdapter.selectedBackgroundResource = R.drawable.v2_yellow_bg
            bankNameDataRecyclerViewAdapter.resourceLayoutFile = R.layout.v2_data_bank_item_layout
            rvBankList.adapter = bankNameDataRecyclerViewAdapter
            rvBankList.visibility = View.VISIBLE
            tvBankName.visibility = View.VISIBLE
            if (selectedBankName == null) {
                tvBankName.text = "All Bank"
            } else {
                tvBankName.text = selectedBankName
            }
        }
    }

    fun updateBankSelection(selectedBankDisplayName: String) {
        bankNameDataRecyclerViewAdapter!!.dataListFilter!!.forEachIndexed { index, item ->
            run {
                var previousSelectedValue = item.selected

                item.selected = selectedBankDisplayName == item.displayValue
                if (previousSelectedValue != item.selected) {
                    bankNameDataRecyclerViewAdapter!!.notifyItemChanged(index)
                }

            }
        }


    }


    //region Observer
    private fun onApplicationList(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                isLoading = true

                if (PAGE_NUMBER > 1 || (screenType.equals(ScreenTypeEnum.Search.value) || screenType.equals(
                        ScreenTypeEnum.StausWithSearch.value
                    ))
                ) {
                    llProgress.visibility = View.VISIBLE

                } else {
                    showProgressDialog(requireContext())
                }
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                llProgress.visibility = View.GONE
                val response: ApplicationListResponse? =
                    mApiResponse.data as ApplicationListResponse?
                setResultData(response)
                isLoading = false

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                llProgress.visibility = View.GONE
            }
            else -> {

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
                hideProgressDialog()
                val customerResponse: CustomerDetailsResponse? =
                    mApiResponse.data as CustomerDetailsResponse?
                checkForNextScreenAfterCustomerResponse(customerResponse)


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
                        cust.data?.caseId?.let {
                            navigateToKYCDocumentUploadFromApplicationList(
                                selectedCustomerId.toString(),
                                kycDocumentRes,
                                it,
                                cust
                            )
                        }
                    else if (kycDocumentRes.data.groupedDoc.isEmpty() && kycDocumentRes.data.nonGroupedDoc.isEmpty())
                        navigateToBankOfferStatusFromApplicationListFrag(selectedCustomerId, cust)
                } else {
                    navigateToBankOfferStatusFromApplicationListFrag(selectedCustomerId, cust)
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

    private fun onRuleEngineBanksResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val ruleEngineBanksResponse: RuleEngineBanksResponse =
                    mApiResponse.data as RuleEngineBanksResponse
                setPartnerBanksDetails(ruleEngineBanksResponse)

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()


            }
            else -> {

            }
        }

    }


//endregion Observer

    private val recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = layoutManager!!.childCount
                val totalItemCount: Int = layoutManager!!.itemCount
                val firstVisibleItemPosition: Int = layoutManager!!.findFirstVisibleItemPosition()
                if (!isLoading && applicationListAdapter.itemCount < TOTAL) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                        if (screenType == ScreenTypeEnum.Search.value) {
                            callSearchAPI()
                        } else {
                            if (TextUtils.isEmpty(etSearch.text.toString())) {
                                callApplicationStatusWiseFilterAPI(null)
                            } else {
                                callApplicationStatusWiseFilterAPI(etSearch.text.toString())
                            }
                        }
                    }
                }
            }
        }


}







