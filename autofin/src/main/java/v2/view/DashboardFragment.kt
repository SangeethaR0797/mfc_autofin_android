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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.dto.MenuDTO
import v2.model.response.master.KYCDocumentResponse
import v2.model_view.MasterViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.MenuForDashboardAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.itemClickCallBack
import v2.view.utility_view.GridItemDecoration


class DashboardFragment : BaseFragment(), View.OnClickListener {

    lateinit var ivBack: ImageView
    lateinit var ivNotification: ImageView
    lateinit var etSearch: EditText
    lateinit var llSearch: LinearLayout
    lateinit var rvMenu: RecyclerView

    lateinit var menuForDashboardAdapter: MenuForDashboardAdapter

    lateinit var kycDocumentViewModel: MasterViewModel
    private val hardCodedCustomerId = "1724"
    private val hardCodedCaseId = "0242210415000012"

    companion object {

        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kycDocumentViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)
        kycDocumentViewModel.getKYCDocumentLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onGetKYCDocumentResponse(mApiResponse!!)
            }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.v2_fragment_dashboard, container, false)

        ivBack = view.findViewById(R.id.iv_back)
        ivNotification = view.findViewById(R.id.iv_notification)
        etSearch = view.findViewById(R.id.et_search)
        etSearch.isEnabled = false
        llSearch = view.findViewById(R.id.ll_search)
        rvMenu = view.findViewById(R.id.rv_menu)

        ivBack.setOnClickListener(this)
        ivNotification.setOnClickListener(this)
        etSearch.setOnClickListener(this)
        llSearch.setOnClickListener(this)
        setScreenData()
        return view
    }

    fun setScreenData() {
        setMenuDataAdapter()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_back -> {
                    activity?.onBackPressed()
                }
                R.id.iv_notification -> {
                    openNotificationFragment()
                }

                R.id.ll_search -> {
                    openSearchFragment()
                }

                R.id.ll_search -> {
                    openSearchFragment()
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
        showToast("Search")
    }

    fun openNotificationFragment() {
        showToast("Notification")
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


    fun setMenuDataAdapter() {
        val menuList: ArrayList<MenuDTO> = arrayListOf<MenuDTO>()
        menuList?.add(getMenu("Registered", "Registered", null, 0, R.drawable.ic_menu_registered,R.drawable.v2_menu_dark_blue))
        menuList?.add(getMenu("Soft offer", "Soft offer", null, 0, R.drawable.ic_menu_softoffers,R.drawable.v2_menu_light_blue))
        menuList?.add(getMenu("Logged in", "Logged in", null, 0, R.drawable.ic_logged_in,R.drawable.v2_menu_light_green))
        menuList?.add(getMenu("Approved", "Approved", null, 0, R.drawable.ic_menu_approved,R.drawable.v2_menu_bright_green))
        menuList?.add(getMenu("Disbursed", "Disbursed", null, 0, R.drawable.ic_menu_disbursed,R.drawable.v2_menu_dark_green))
        menuList?.add(getMenu("Add New", "Add New", null, 0, R.drawable.ic_menu_add,R.drawable.v2_menu_bright_yellow))


        val layoutManagerStaggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)
        rvMenu.addItemDecoration(GridItemDecoration(45, 2))

        rvMenu.setLayoutManager(layoutManagerStaggeredGridLayoutManager)
        menuForDashboardAdapter = MenuForDashboardAdapter(
            activity as Activity,
            menuList,
            object : itemClickCallBack {
                override fun itemClick(item: Any?, position: Int) {

                }
            })
        rvMenu.setAdapter(menuForDashboardAdapter)
    }

    fun getMenu(
        menuName: String?, menuCode: String?, amount: String?, total: Int, menuIcon: Int,backgroundResource: Int
    ): MenuDTO {

        return MenuDTO(menuName, menuCode, amount, total, menuIcon,backgroundResource)
    }

}