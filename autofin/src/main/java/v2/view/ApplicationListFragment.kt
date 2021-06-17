package v2.view

import android.app.Activity
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import androidx.core.view.marginLeft
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.enum_class.ApplicationStatusEnum
import v2.model.enum_class.ScreenTypeEnum
import v2.model.request.ApplicationListRequest
import v2.model.request.ApplicationListRequestData
import v2.model.request.CustomerRequest
import v2.model.request.ResetCustomerJourneyDataRequest
import v2.model.response.ApplicationDataItems
import v2.model.response.ApplicationListResponse
import v2.model.response.CustomerDetailsResponse
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.ApplicationListAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.ApplicationListClickCallBack

import java.util.*


class ApplicationListFragment : BaseFragment(), View.OnClickListener {

    lateinit var llProgress: LinearLayout
    lateinit var tvTitle: TextView
    lateinit var tvResultCount: TextView
    lateinit var ivBack: ImageView
    lateinit var ivNotification: ImageView
    lateinit var ivSearch: ImageView
    lateinit var rvData: RecyclerView
    lateinit var llData: LinearLayout
    lateinit var llNoDataFound: LinearLayout
    lateinit var llSearch: LinearLayout
    lateinit var llSearchSection: LinearLayout
    lateinit var viewEmptyBlack: View
    lateinit var etSearch: EditText
    lateinit var tvSearchResult: TextView

    lateinit var screenType: String
    var rootView: View? = null
    lateinit var transactionViewModel: TransactionViewModel

    var PER_PAGE: Int = 10
    var PAGE_NUMBER: Int = 0
    var TOTAL: Int = 0
    lateinit var applicationListAdapter: ApplicationListAdapter

    var layoutManager: LinearLayoutManager? = null
    var isLoading: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionViewModel = ViewModelProvider(this).get(
            TransactionViewModel::class.java
        )

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
            }
            initializationOfObject()
        }
        return rootView
    }

    fun initializationOfObject() {
        tvTitle = rootView!!.findViewById(R.id.tv_title)
        llProgress = rootView!!.findViewById(R.id.ll_progress)
        tvResultCount = rootView!!.findViewById(R.id.tv_result_count)
        ivBack = rootView!!.findViewById(R.id.iv_back)
        ivNotification = rootView!!.findViewById(R.id.iv_notification)
        ivSearch = rootView!!.findViewById(R.id.iv_search)
        rvData = rootView!!.findViewById(R.id.rv_data)
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

        tvTitle.text = screenType

        ivBack.setOnClickListener(this)
        ivNotification.setOnClickListener(this)
        ivSearch.setOnClickListener(this)
        llSearch.setOnClickListener(this)

        if (screenType.equals(ScreenTypeEnum.Search.value)) {
            ivSearch.visibility = View.GONE

        } else {
            rvData.setPadding(0, llData.marginLeft, 0, 0)
            ivNotification.visibility = View.GONE
            llSearchSection.visibility = View.GONE
            viewEmptyBlack.visibility = View.GONE
            callApplicationStatusWiseFilterAPI()
        }
        setTextChangeOfetAutoResidenceCity()
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
                    navigateApplicationListFragment(ScreenTypeEnum.Search.value)

                }
                R.id.ll_search -> {
                    etSearch.requestFocus()
                }
            }
        }
    }

    fun setTextChangeOfetAutoResidenceCity() {
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
                    if (allowEditCity == true) {
                        timerWait = Timer()
                        timerWait!!.schedule(object : TimerTask() {
                            override fun run() {


                                allowEditCity = false
                                ThreadUtils.runOnUiThread(Runnable {
                                    //call Search
                                    if (!TextUtils.isEmpty(etSearch.text.toString())) {
                                        PAGE_NUMBER = 0
                                        callSearchAPI()
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

        etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard(etSearch)
            }
            false
        })
    }

    private fun callSearchAPI() {
        PAGE_NUMBER = PAGE_NUMBER + 1
        transactionViewModel.getApplicationList(
            ApplicationListRequest(
                ApplicationListRequestData(
                    etSearch.text.toString(),
                    null,
                    null,
                    PAGE_NUMBER,
                    PER_PAGE
                ), CommonStrings.DEALER_ID,
                CommonStrings.USER_TYPE
            ),
            Global.customerAPI_BaseURL + CommonStrings.SEARCH_APPLICATION_END_POINT
        )
    }

    private fun callApplicationStatusWiseFilterAPI() {
        PAGE_NUMBER = PAGE_NUMBER + 1
        transactionViewModel.getApplicationList(
            ApplicationListRequest(
                ApplicationListRequestData(
                    null,
                    screenType,
                    null,
                    PAGE_NUMBER,
                    PER_PAGE
                ), CommonStrings.DEALER_ID,
                CommonStrings.USER_TYPE
            ),
            Global.customerAPI_BaseURL + CommonStrings.APPLICATION_STATUS_WISE_FILTER_END_POINT
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
        if (response != null && response.data != null && response.data!!.customers != null && response.data!!.customers!!.size > 0) {
            if (PAGE_NUMBER == 1) {
                TOTAL = response.data!!.total!!
                if (TOTAL == 0) {
                    llNoDataFound.visibility = View.VISIBLE
                    return
                }
                layoutManager = LinearLayoutManager(activity)
                rvData.layoutManager = layoutManager
                tvResultCount.visibility = View.VISIBLE
                if (TOTAL > 1) {
                    tvResultCount.text = "Total " + TOTAL.toString() + " leads"
                } else {
                    tvResultCount.text = "Total " + TOTAL.toString() + " lead"
                }
                applicationListAdapter =
                    ApplicationListAdapter(
                        activity as Activity,
                        response.data!!.customers!!,
                        object :
                            ApplicationListClickCallBack {
                            override fun onItemClick(item: Any?, position: Int) {
                                var applicationDataItems = item as ApplicationDataItems
                                showToast("onItemClick")
                            }

                            override fun onCompleteClick(item: Any?, position: Int) {
                                var applicationDataItems = item as ApplicationDataItems
                                if ((applicationDataItems.status.equals(ApplicationStatusEnum.Registered.value) && applicationDataItems.subStatus.equals(
                                        ApplicationStatusEnum.Registered.value
                                    )) ||
                                    (applicationDataItems.status.equals(ApplicationStatusEnum.Registered.value) && applicationDataItems.subStatus.equals(
                                        ApplicationStatusEnum.Employment_Details_Submitted.value
                                    )) ||
                                    (applicationDataItems.status.equals(ApplicationStatusEnum.KYC_Done.value) && applicationDataItems.subStatus.equals(
                                        ApplicationStatusEnum.KYC_Done.value
                                    ))||
                                    (applicationDataItems.status.equals(ApplicationStatusEnum.KYC_Done.value) && applicationDataItems.subStatus.equals(
                                        ApplicationStatusEnum.Employment_Details_Submitted.value
                                    ))
                                ) {
                                    callCustomerDetailsApi(applicationDataItems!!.customerId!!)
                                } else {
                                    showToast("Soft offer")
                                }
                            }

                            override fun onCallClick(item: Any?, position: Int) {
                                var applicationDataItems = item as ApplicationDataItems
                                showToast("onCallClick")
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
            ))||
            (customerResponse!!.data!!.status.equals(ApplicationStatusEnum.KYC_Done.value) && customerResponse.data!!.subStatus.equals(
                ApplicationStatusEnum.Employment_Details_Submitted.value
            ))

        ) {
            showToast("Lead Details Fragment")
        } else {
            //Open Soft offer fragment
        }

    }

    //region Observer
    private fun onApplicationList(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                isLoading = true
                if (PAGE_NUMBER == 1) {
                    showProgressDialog(requireContext())
                } else {
                    llProgress.visibility = View.VISIBLE
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

                val customerResponse: CustomerDetailsResponse? =
                    mApiResponse.data as CustomerDetailsResponse?
                checkForNextScreenAfterCustomerResponse(customerResponse)

                hideProgressDialog()

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


//endregion Observer

    private val recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = layoutManager!!.getChildCount()
                val totalItemCount: Int = layoutManager!!.getItemCount()
                val firstVisibleItemPosition: Int = layoutManager!!.findFirstVisibleItemPosition()
                if (!isLoading && applicationListAdapter.itemCount < TOTAL) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                        if (screenType.equals(ScreenTypeEnum.Search.value)) {
                            callSearchAPI()
                        } else {
                            callApplicationStatusWiseFilterAPI()
                        }
                    }
                }
            }
        }


}







