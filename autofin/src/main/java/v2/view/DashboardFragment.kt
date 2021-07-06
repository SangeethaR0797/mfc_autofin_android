package v2.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.dto.*
import v2.model.enum_class.ApplicationStatusEnum
import v2.model.enum_class.MenuEnum
import v2.model.enum_class.ScreenTypeEnum
import v2.model.request.*
import v2.model.request.add_lead.AddLeadData
import v2.model.request.add_lead.AddLeadVehicleDetails
import v2.model.request.add_lead.BasicDetails
import v2.model.response.*
import v2.model.response.master.KYCDocumentResponse
import v2.model_view.DashboardViewModel
import v2.model_view.MasterViewModel
import v2.model_view.NoticeBoardViewModel
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.DataRecyclerViewAdapter
import v2.view.adapter.MenuForDashboardAdapter
import v2.view.adapter.NoticeRecyclerViewAdapter
import v2.view.adapter.PartnerBankRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.AppTokenChangeInterface
import v2.view.callBackInterface.NoticeItemClickCallBack
import v2.view.callBackInterface.itemClickCallBack
import v2.view.utility_view.GridItemDecoration


class DashboardFragment : BaseFragment(), View.OnClickListener, AppTokenChangeInterface {

    lateinit var llDashBoard: LinearLayout
    lateinit var ivBack: ImageView
    lateinit var ivNotification: ImageView
    lateinit var etSearch: EditText
    lateinit var llSearch: LinearLayout
    lateinit var rvMenu: RecyclerView

    lateinit var rvNoticeBoard: RecyclerView
    lateinit var llViewAllNotice: LinearLayout
    lateinit var llNoticeBoardSection: LinearLayout

    lateinit var rvCommissionDaysDays: RecyclerView
    lateinit var tvTotalCommission: TextView
    lateinit var tvPotentialCommission: TextView
    lateinit var tvNoticeBoardCount: TextView
    lateinit var tvNotificationCount: TextView
    lateinit var rlNotification: RelativeLayout


    lateinit var rvBankingPartner: RecyclerView
    lateinit var llBankingPartner: LinearLayout

    lateinit var tvAmount: TextView
    lateinit var tvYear: TextView
    lateinit var tvInterestRate: TextView
    lateinit var tvEmiAmount: TextView
    lateinit var btnApplyNow: TextView

    lateinit var skAmount: SeekBar
    lateinit var skYear: SeekBar
    lateinit var skInterestRate: SeekBar

    lateinit var menuForDashboardAdapter: MenuForDashboardAdapter

    lateinit var kycDocumentViewModel: MasterViewModel
    private val hardCodedCustomerId = "1724"
    private val hardCodedCaseId = "0242210415000012"
    lateinit var dashboardViewModel: DashboardViewModel
    lateinit var hostActivity: HostActivity

    lateinit var commissionDaysDaysDataAdapter: DataRecyclerViewAdapter
    lateinit var noticeRecyclerViewAdapter: NoticeRecyclerViewAdapter
    lateinit var partnerBankRecyclerViewAdapter: PartnerBankRecyclerViewAdapter

    lateinit var noticeBoardViewModel: NoticeBoardViewModel
    var rootView: View? = null

    lateinit var transactionViewModel: TransactionViewModel
    lateinit var masterViewModel: MasterViewModel

    var selectedCustomerId: Int = 0
    lateinit var cust: CustomerDetailsResponse

    companion object {

        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onStop() {
        super.onStop()
        if (hostActivity != null) {
            hostActivity.appTokenChangeInterface = null
        }
    }

    override fun onResume() {
        super.onResume()
        if (hostActivity != null) {
            hostActivity.appTokenChangeInterface = DashboardFragment@ this
        }
        if (!TextUtils.isEmpty(CommonStrings.TOKEN_VALUE) &&
            !TextUtils.isEmpty(CommonStrings.IBB_ACCESS_TOKEN_URL_END)
        ) {
            setRefreshData()
        }
        hideSoftKeyboard()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hostActivity = activity as Activity as HostActivity

        kycDocumentViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        noticeBoardViewModel = ViewModelProvider(this).get(NoticeBoardViewModel::class.java)
        transactionViewModel = ViewModelProvider(this).get(
            TransactionViewModel::class.java
        )
        masterViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)


        kycDocumentViewModel.getKYCDocumentLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onGetKYCDocumentResponse(mApiResponse!!)
            }

        dashboardViewModel.getDashboardDetailsLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onDashboardDetailsResponse(mApiResponse!!)
            }
        dashboardViewModel.getRuleEngineBanksLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onRuleEngineBanksResponse(mApiResponse!!)
            }

        noticeBoardViewModel.getNoticeBoardActionLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onNoticeBoardActionResponse(mApiResponse!!)
            }

        dashboardViewModel.getDealerCommissionLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onCommissionDetailsResponse(mApiResponse!!)
            }
        dashboardViewModel.getEmiAmountLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onEmiAmountResponse(mApiResponse!!)
            }


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

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (rootView != null) {
            setRefreshData()
            return rootView

        } else {
            rootView = inflater.inflate(R.layout.v2_fragment_dashboard, container, false)

            llDashBoard = rootView!!.findViewById(R.id.ll_dash_board)
            llDashBoard.visibility = View.GONE

            ivBack = rootView!!.findViewById(R.id.iv_back)
            ivNotification = rootView!!.findViewById(R.id.iv_notification)
            etSearch = rootView!!.findViewById(R.id.et_search)
            etSearch.isFocusable = false
            llSearch = rootView!!.findViewById(R.id.ll_search)
            rvMenu = rootView!!.findViewById(R.id.rv_menu)

            rvNoticeBoard = rootView!!.findViewById(R.id.rv_notice_board)
            tvNoticeBoardCount = rootView!!.findViewById(R.id.tv_notice_board_count)
            tvNotificationCount = rootView!!.findViewById(R.id.tv_notification_count)
            rlNotification = rootView!!.findViewById(R.id.rl_notification)
            tvNotificationCount.visibility = View.GONE
            llViewAllNotice = rootView!!.findViewById(R.id.ll_view_all_notice)
            llNoticeBoardSection = rootView!!.findViewById(R.id.ll_notice_board_section)

            rvCommissionDaysDays = rootView!!.findViewById(R.id.rv_commission_days)
            tvTotalCommission = rootView!!.findViewById(R.id.tv_total_commission)
            tvPotentialCommission = rootView!!.findViewById(R.id.tv_potential_commission)

            rvBankingPartner = rootView!!.findViewById(R.id.rv_banking_partner)
            llBankingPartner = rootView!!.findViewById(R.id.ll_banking_partner)


            tvAmount = rootView!!.findViewById(R.id.tv_amount)
            tvYear = rootView!!.findViewById(R.id.tv_year)
            tvInterestRate = rootView!!.findViewById(R.id.tv_interest_rate)
            tvEmiAmount = rootView!!.findViewById(R.id.tv_emi_amount)
            btnApplyNow = rootView!!.findViewById(R.id.btn_apply_now)

            tvAmount.isSingleLine = true
            tvYear.isSingleLine = true
            tvInterestRate.isSingleLine = true
            tvEmiAmount.isSingleLine = true
            btnApplyNow.isSingleLine = true

            skAmount = rootView!!.findViewById(R.id.sk_amount)
            skYear = rootView!!.findViewById(R.id.sk_year)
            skInterestRate = rootView!!.findViewById(R.id.sk_interest_rate)

            setSeekBarEvent()

            ivBack.setOnClickListener(this)
            ivNotification.setOnClickListener(this)
            rlNotification.setOnClickListener(this)
            etSearch.setOnClickListener(this)
            llSearch.setOnClickListener(this)
            llViewAllNotice.setOnClickListener(this)
            btnApplyNow.setOnClickListener(this)
            setScreenData()
        }
        return rootView
    }


    private fun setSeekBarEvent() {


        skAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                tvAmount.text =
                    resources.getString(R.string.rupees_symbol) + " " + formatAmount(progress.toString())

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
        skYear.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                if (progress > 1) {
                    tvYear.text = (progress.toString()) + " Years"
                } else {
                    tvYear.text = (progress.toString()) + " Year"
                }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        skInterestRate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                tvInterestRate.text = (progress.toString()) + "%"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            skAmount.min = 50000
            skYear.min = 1
            skInterestRate.min = 8
        }
        skAmount.max = 5000000
        skYear.max = 12
        skInterestRate.max = 25


        skAmount.progress = 50000
        skYear.progress = 1
        skInterestRate.progress = 8


    }

    fun callEmiData() {


        skYear.progress < 1
        skInterestRate.progress < 8
        dashboardViewModel.getEmiAmount(
            EmiRequest(
                EmiRequestData(
                    skAmount.progress,
                    skInterestRate.progress,
                    (skYear.progress * 12)
                ),
                CommonStrings.DEALER_ID,
                CommonStrings.USER_TYPE
            ),
            Global.emi_baseURL + CommonStrings.GET_EMI_AMOUNT_END_POINT
        )
    }

    override fun onTokenReceivedOrRefresh() {
        setRefreshData()
    }

    private fun setRefreshData() {
        //Call Dashboard details
        dashboardViewModel.getDashboardDetails(
            DashBoardDetailsRequest(
                CommonStrings.DEALER_ID,
                CommonStrings.USER_TYPE,
                "Dashboard"
            ), Global.customerAPI_BaseURL + CommonStrings.DASHBOARD_DETAILS_END_POINT
        )
        dashboardViewModel.getRuleEngineBanks(
            Global.baseURL + CommonStrings.GET_RULE_ENGINE_BANKS_END_POINT
        )
        callEmiData()
    }

    fun setScreenData() {
        setCommissionDaysData()


    }

    private fun setCommissionDaysData() {

        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        list.add(DataSelectionDTO("30 days", null, "30", true))
        list.add(DataSelectionDTO("60 days", null, "60", false))
        list.add(DataSelectionDTO("90 days", null, "90", false))
        list.add(DataSelectionDTO("All time", null, "0", false))

        commissionDaysDaysDataAdapter =
            DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                override fun itemClick(item: Any?, position: Int) {

                    dashboardViewModel.getDealerCommission(
                        CommonRequest(
                            (item as DataSelectionDTO).value!!.toInt(), CommonStrings.DEALER_ID,
                            CommonStrings.USER_TYPE
                        ),
                        Global.customerAPI_BaseURL + CommonStrings.DEALER_COMMISSION_END_POINT
                    )


                    commissionDaysDaysDataAdapter.dataListFilter!!.forEachIndexed { index, item ->
                        run {
                            if (index == position) {
                                item.selected = true

                            } else {
                                item.selected = false
                            }
                        }
                    }
                    commissionDaysDaysDataAdapter.notifyDataSetChanged()
                }
            })


        val layoutManagerStaggeredGridLayoutManager =
            StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 4)

        rvCommissionDaysDays.addItemDecoration(GridItemDecoration(15, 4))

        rvCommissionDaysDays.setLayoutManager(layoutManagerStaggeredGridLayoutManager)
        commissionDaysDaysDataAdapter.resourceLayoutFile = R.layout.v2_data_item_layout_small
        rvCommissionDaysDays.setAdapter(commissionDaysDaysDataAdapter)

    }

    private fun setNoticeBoardData(list: ArrayList<NoticeData>?) {
        val layoutManager = LinearLayoutManager(activity)
        rvNoticeBoard.layoutManager = layoutManager

        noticeRecyclerViewAdapter =
            NoticeRecyclerViewAdapter(activity as Activity, list, object : NoticeItemClickCallBack {
                override fun itemClick(item: Any?, position: Int) {
                    var notice = item as NoticeData
                    if (notice.isNew == true) {
                        noticeBoardViewModel.noticeBoardAction(
                            CommonRequest(
                                notice.noticeBoardId, CommonStrings.DEALER_ID,
                                CommonStrings.USER_TYPE
                            ),
                            Global.customerAPI_BaseURL + CommonStrings.NOTICE_BOARD_ACTION_END_POINT
                        )
                    }
                    selectedCustomerId = notice!!.customerId!!
                    callCustomerDetailsApi(selectedCustomerId)

                    noticeRecyclerViewAdapter.notifyItemChanged(position)
                }

                override fun moreClick(item: Any?, position: Int) {
                    var notice = item as NoticeData
                    if (notice.isNew == true) {
                        noticeBoardViewModel.noticeBoardAction(
                            CommonRequest(
                                notice.noticeBoardId, CommonStrings.DEALER_ID,
                                CommonStrings.USER_TYPE
                            ),
                            Global.customerAPI_BaseURL + CommonStrings.NOTICE_BOARD_ACTION_END_POINT
                        )
                    }
                    selectedCustomerId = notice!!.customerId!!


                    noticeRecyclerViewAdapter.notifyItemChanged(position)
                }
            })
        rvNoticeBoard.adapter = noticeRecyclerViewAdapter


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

    private fun setPartnerBanksDetails(ruleEngineBanksResponse: RuleEngineBanksResponse) {
        ruleEngineBanksResponse.data
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvBankingPartner.layoutManager = layoutManager

        partnerBankRecyclerViewAdapter =
            PartnerBankRecyclerViewAdapter(
                activity as Activity,
                ruleEngineBanksResponse.data,
                object : itemClickCallBack {
                    override fun itemClick(item: Any?, position: Int) {
                        var ruleEngineBankData = item as RuleEngineBankData
                        navigateBankFeaturesAndChargesFragment(
                            ruleEngineBankData.bankName!!,
                            ruleEngineBankData!!.id!!
                        )

                    }
                })
        rvBankingPartner.adapter = partnerBankRecyclerViewAdapter
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_back -> {
                    activity?.onBackPressed()
                }
                R.id.btn_apply_now -> {
                    if (skAmount.progress < 50000) {
                        showToast("Please select minimum loan Amount ₹ 50000.")
                    } else if (skYear.progress < 1) {
                        showToast("Please select minimum loan Tenure 1 year.")
                    } else if (skInterestRate.progress < 8
                    ) {
                        showToast("Please select minimum loan Interest rate 8 %.")
                    } else {
                        showProgressDialog(requireContext())
                        callEmiData()
                    }


                }
                R.id.iv_notification -> {
                    navigateNoticeBoardAndNotificationFragment(ScreenTypeEnum.Notification.value)
                }
                R.id.rl_notification -> {
                    navigateNoticeBoardAndNotificationFragment(ScreenTypeEnum.Notification.value)
                }

                R.id.et_search -> {
                    openSearchFragment()
                }

                R.id.ll_search -> {
                    openSearchFragment()
                }
                R.id.ll_view_all_notice -> {
                    navigateNoticeBoardAndNotificationFragment(ScreenTypeEnum.Notice_Board.value)
                }


                R.id.btnNewVehicle -> {
                    navigateFromDashBoard(R.id.vehicleSelectionFrag2)
                }
            }
        }
    }

    fun openSearchFragment() {
        navigateApplicationListFragment(ScreenTypeEnum.Search.value, null)
    }


    fun openNextFragment(menuCode: String, menuName: String) {

        when (menuCode) {

            MenuEnum.Registered.value -> {
                navigateApplicationListFragment(
                    ScreenTypeEnum.Registered.value,
                    ScreenTypeEnum.Registered.value
                )
            }
            MenuEnum.Soft_offer.value -> {
                navigateApplicationListFragment(
                    ScreenTypeEnum.SoftOffer.value,
                    getString(R.string.v2_soft_offer_title)
                )

/*
                navigateApplicationListFragment(ScreenTypeEnum.SoftOffer.value,ScreenTypeEnum.SoftOffer.value)
*/
            }
            MenuEnum.Logged_In.value -> {
                navigateApplicationListFragment(
                    ScreenTypeEnum.LoggedIn.value,
                    getString(R.string.v2_logged_in_title)
                )

                // navigateApplicationListFragment(ScreenTypeEnum.LoggedIn.value,ScreenTypeEnum.LoggedIn.value)
            }
            MenuEnum.Approved.value -> {
                navigateApplicationListFragment(
                    ScreenTypeEnum.Approved.value,
                    ScreenTypeEnum.Approved.value
                )
            }
            MenuEnum.Disbursed.value -> {
                navigateApplicationListFragment(
                    ScreenTypeEnum.Disbursed.value,
                    ScreenTypeEnum.Disbursed.value
                )
            }
            MenuEnum.Add_New.value -> {
                navigateFromDashBoard(R.id.vehicleSelectionFrag2)
            }
        }
    }

    //region set Application menu
    fun setMenuDataAdapter(dashboardDetailsResponse: DashBoardDetailsResponse) {
        if (dashboardDetailsResponse != null) {
            llDashBoard.visibility = View.VISIBLE
            tvTotalCommission.text = "₹" + formatAmount(
                dashboardDetailsResponse.data!!.commissionDetails!!.totalCommission.toString()
            )
            tvPotentialCommission.text = "₹" + formatAmount(
                dashboardDetailsResponse!!.data!!.commissionDetails!!.potentialCommission.toString()
            )
            if (dashboardDetailsResponse.data!!.noticeBoard!!.totalCount!! > 0) {
                llNoticeBoardSection.visibility = View.VISIBLE
                tvNoticeBoardCount.text =
                    dashboardDetailsResponse.data!!.noticeBoard!!.totalCount.toString()
                setNoticeBoardData(dashboardDetailsResponse.data!!.noticeBoard!!.notices as ArrayList<NoticeData>?)
                if (dashboardDetailsResponse.data!!.noticeBoard!!.totalCount!! > 3) {
                    llViewAllNotice.visibility = View.VISIBLE
                } else {
                    llViewAllNotice.visibility = View.GONE
                }
            } else {
                llNoticeBoardSection.visibility = View.GONE
            }
            tvNotificationCount.visibility = View.GONE
            if (dashboardDetailsResponse.data!!.newNotificationCount!! > 0) {
                tvNotificationCount.visibility = View.VISIBLE
                if (dashboardDetailsResponse.data!!.newNotificationCount!! > 99) {
                    tvNotificationCount.text = "99+"
                } else {
                    tvNotificationCount.text =
                        dashboardDetailsResponse.data!!.newNotificationCount!!.toString()
                }
            }


            val menuList: ArrayList<MenuDTO> = arrayListOf<MenuDTO>()
            menuList.add(
                getMenu(
                    "Registered",
                    MenuEnum.Registered.value,
                    null,
                    dashboardDetailsResponse.data!!.registered!!,
                    R.drawable.ic_menu_registered,
                    R.drawable.v2_menu_dark_blue
                )
            )
            menuList.add(
                getMenu(
                    "Soft offer",
                    MenuEnum.Soft_offer.value,
                    null,
                    dashboardDetailsResponse.data!!.softOffer!!,
                    R.drawable.ic_menu_softoffers,
                    R.drawable.v2_menu_light_blue
                )
            )
            menuList.add(
                getMenu(
                    "Logged in",
                    MenuEnum.Logged_In.value,
                    null,
                    dashboardDetailsResponse.data!!.loggedIn!!,
                    R.drawable.ic_logged_in,
                    R.drawable.v2_menu_light_green
                )
            )
            menuList.add(
                getMenu(
                    "Approved",
                    MenuEnum.Approved.value,
                    formatAmount(dashboardDetailsResponse.data!!.approvedAmount!!.toString()),
                    dashboardDetailsResponse.data!!.approved!!,
                    R.drawable.ic_menu_approved,
                    R.drawable.v2_menu_bright_green
                )
            )
            menuList.add(
                getMenu(
                    "Disbursed",
                    MenuEnum.Disbursed.value,
                    formatAmount(dashboardDetailsResponse.data!!.disbursedAmount!!.toString()),
                    dashboardDetailsResponse.data!!.disbursed!!,
                    R.drawable.ic_menu_disbursed,
                    R.drawable.v2_menu_dark_green
                )
            )
            menuList.add(
                getMenu(
                    "Add New",
                    MenuEnum.Add_New.value,
                    null,
                    0,
                    R.drawable.ic_menu_add,
                    R.drawable.v2_menu_bright_yellow
                )
            )

            if (rvMenu.layoutManager == null) {
                val layoutManagerStaggeredGridLayoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)
                rvMenu.addItemDecoration(GridItemDecoration(30, 2))
                rvMenu.setLayoutManager(layoutManagerStaggeredGridLayoutManager)
            }

            menuForDashboardAdapter = MenuForDashboardAdapter(
                activity as Activity,
                menuList,
                object : itemClickCallBack {
                    override fun itemClick(item: Any?, position: Int) {
                        var m = item as MenuDTO
                        openNextFragment(m.menuCode!!, m.menuName!!)
                    }
                })
            rvMenu.setAdapter(menuForDashboardAdapter)
        }
    }

    fun getMenu(
        menuName: String?,
        menuCode: String?,
        amount: String?,
        total: Int,
        menuIcon: Int,
        backgroundResource: Int
    ): MenuDTO {

        return MenuDTO(menuName, menuCode, amount, total, menuIcon, backgroundResource)
    }

    //endregion set Application menu
    //region Observer
    private fun onDashboardDetailsResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                // llDashBoard.visibility = View.GONE
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()


                val dashboardDetailsResponse: DashBoardDetailsResponse =
                    mApiResponse.data as DashBoardDetailsResponse
                setMenuDataAdapter(dashboardDetailsResponse)

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()


            }
            else -> {

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


    private fun onNoticeBoardActionResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                //showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val commonResponse: CommonResponse = mApiResponse.data as CommonResponse

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

    private fun onCommissionDetailsResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val commissionDetailsResponse: CommissionDetailsResponse =
                    mApiResponse.data as CommissionDetailsResponse
                tvTotalCommission.text = "₹" + formatAmount(
                    commissionDetailsResponse!!.data!!.totalCommission.toString()
                )
                tvPotentialCommission.text = "₹" + formatAmount(
                    commissionDetailsResponse!!.data!!.potentialCommission.toString()
                )

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

    private fun onEmiAmountResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {

            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val emiResponse: EmiResponse =
                    mApiResponse.data as EmiResponse
                if (emiResponse != null && emiResponse!!.data != null) {
                    tvEmiAmount.text = "₹" + formatAmount(
                        emiResponse!!.data!!.toInt().toString()
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

//endregion Observer

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
                getString(R.string.v2_lead_status_lender_selected) -> {
                    navigateToAddressAdditionalFields(selectedCustomerId, customerResponse)
                }
                getString(R.string.v2_lead_status_bank_form_filled) -> {
                    cust = customerResponse
                    masterViewModel.getKYCDocumentResponse(Global.baseURL + CommonStrings.KYC_UPLOAD_URL_END_POINT + selectedCustomerId)
                }
                getString(R.string.v2_lead_status_document_upload) -> {
                    navigateToBankOfferStatusFromApplicationListFrag(
                        selectedCustomerId,
                        customerResponse
                    )
                }
                getString(R.string.v2_lead_status_submitted_to_bank) -> {
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
}