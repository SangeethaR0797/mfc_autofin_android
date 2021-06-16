package v2.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.dto.*
import v2.model.enum_class.MenuEnum
import v2.model.request.CommonRequest
import v2.model.response.*
import v2.model.response.master.KYCDocumentResponse
import v2.model_view.DashboardViewModel
import v2.model_view.MasterViewModel
import v2.model_view.NoticeBoardViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.DataRecyclerViewAdapter
import v2.view.adapter.MenuForDashboardAdapter
import v2.view.adapter.NoticeRecyclerViewAdapter
import v2.view.adapter.PartnerBankRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.AppTokenChangeInterface
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


    lateinit var rvBankingPartner: RecyclerView
    lateinit var llBankingPartner: LinearLayout

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hostActivity = activity as Activity as HostActivity

        kycDocumentViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        noticeBoardViewModel = ViewModelProvider(this).get(NoticeBoardViewModel::class.java)

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
            llViewAllNotice = rootView!!.findViewById(R.id.ll_view_all_notice)
            llNoticeBoardSection = rootView!!.findViewById(R.id.ll_notice_board_section)

            rvCommissionDaysDays = rootView!!.findViewById(R.id.rv_commission_days)
            tvTotalCommission = rootView!!.findViewById(R.id.tv_total_commission)
            tvPotentialCommission = rootView!!.findViewById(R.id.tv_potential_commission)

            rvBankingPartner = rootView!!.findViewById(R.id.rv_banking_partner)
            llBankingPartner = rootView!!.findViewById(R.id.ll_banking_partner)


            ivBack.setOnClickListener(this)
            ivNotification.setOnClickListener(this)
            etSearch.setOnClickListener(this)
            llSearch.setOnClickListener(this)
            llViewAllNotice.setOnClickListener(this)
            setScreenData()
        }
        return rootView
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
    }

    fun setScreenData() {
        setCommissionDaysData()


    }

    private fun setCommissionDaysData() {

        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        list.add(DataSelectionDTO("30 days", null, "30", true))
        list.add(DataSelectionDTO("60 days", null, "60", false))
        list.add(DataSelectionDTO("90 days", "rd", "90", false))
        list.add(DataSelectionDTO("All time", "th", "0", false))

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

        rvCommissionDaysDays.addItemDecoration(GridItemDecoration(25, 4))

        rvCommissionDaysDays.setLayoutManager(layoutManagerStaggeredGridLayoutManager)

        rvCommissionDaysDays.setAdapter(commissionDaysDaysDataAdapter)

    }

    private fun setNoticeBoardData(list: ArrayList<NoticeData>?) {
        val layoutManager = LinearLayoutManager(activity)
        rvNoticeBoard.layoutManager = layoutManager

        noticeRecyclerViewAdapter =
            NoticeRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
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

                }
            })
        rvNoticeBoard.adapter = noticeRecyclerViewAdapter

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
                        showToast("Know More")

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
                R.id.iv_notification -> {
                    navigateNoticeBoardAndNotificationFragment("Notification")
                }

                R.id.et_search -> {
                    openSearchFragment()
                }

                R.id.ll_search -> {
                    openSearchFragment()
                }
                R.id.ll_view_all_notice -> {
                    navigateNoticeBoardAndNotificationFragment("Notice Board")
                }

                R.id.btnRegisteredList -> {
                    kycDocumentViewModel.getKYCDocumentResponse(Global.baseURL + CommonStrings.KYC_UPLOAD_URL_END_POINT + hardCodedCustomerId)
                }

                R.id.btnNewVehicle -> {
                    navigateFromDashBoard(R.id.vehicleSelectionFrag2)
                }
            }
        }
    }

    fun openSearchFragment() {
        navigateApplicationListFragment("Search")
    }



    fun openNextFragment(menuCode: String, menuName: String) {
        showToast(menuCode + " >> " + menuName)
        when (menuCode) {

            MenuEnum.Registered.value -> {
                navigateApplicationListFragment("Registered")
            }
            MenuEnum.Soft_offer.value -> {
                navigateApplicationListFragment("SoftOffer")
            }
            MenuEnum.Logged_In.value -> {
                navigateApplicationListFragment("LoggedIn")
            }
            MenuEnum.Approved.value -> {
                navigateApplicationListFragment("Approved")
            }
            MenuEnum.Disbursed.value -> {
                navigateApplicationListFragment("Disbursed")
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
                    dashboardDetailsResponse.data!!.noticeBoard!!.newCount.toString()
                setNoticeBoardData(dashboardDetailsResponse.data!!.noticeBoard!!.notices as ArrayList<NoticeData>?)
                if (dashboardDetailsResponse.data!!.noticeBoard!!.totalCount!! > 3) {
                    llViewAllNotice.visibility = View.VISIBLE
                } else {
                    llViewAllNotice.visibility = View.GONE
                }
            } else {
                llNoticeBoardSection.visibility = View.GONE
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
                    dashboardDetailsResponse.data!!.commissionDetails!!.totalCommission!!.toString(),
                    dashboardDetailsResponse.data!!.approved!!,
                    R.drawable.ic_menu_approved,
                    R.drawable.v2_menu_bright_green
                )
            )
            menuList.add(
                getMenu(
                    "Disbursed",
                    MenuEnum.Disbursed.value,
                    null,
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


    private fun onGetKYCDocumentResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val kycDocumentRes: KYCDocumentResponse = mApiResponse.data as KYCDocumentResponse

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


//endregion Observer
}