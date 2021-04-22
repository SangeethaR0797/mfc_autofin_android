package v2.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.mfc.autofin.framework.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.newSingleThreadContext
import retrofit_config.RetroBase
import utility.CommonMethods
import utility.CommonStrings
import v2.model.dto.AddLeadRequest
import utility.Global
import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model.request.StockDetailsReq
import v2.model.request.VehicleRegNum
import v2.model.response.IBB_TokenResponse
import v2.model.response.StockDetails
import v2.model.response.StockResponse
import v2.model_view.AuthenticationViewModel
import v2.model_view.StockAPIViewModel
import v2.repository.MasterRepository
import v2.service.utility.ApiResponse
import v2.view.base.BaseFragment
import v2.view.other_activity.VehBasicDetailsActivity

public class VehicleSelectionFrag : BaseFragment(), View.OnClickListener {

    lateinit var etVehRegNum: EditText
    lateinit var btnVehicleReg: Button
    lateinit var tvSearchCar: TextView
    lateinit var ivBackToDashBoard:ImageView

    var regNoVal: String = ""
    var stockAPIViewModel: StockAPIViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vehicle_selection, container, false)
        initViews(view)

        stockAPIViewModel = ViewModelProvider(requireActivity()).get(
                StockAPIViewModel::class.java)

        return view
    }

    private fun initViews(view: View?) {
        etVehRegNum = view?.findViewById(R.id.etVehRegNum)!!
        btnVehicleReg = view.findViewById(R.id.btnVehicleReg)
        tvSearchCar = view.findViewById(R.id.tvSearchCar)
        ivBackToDashBoard=view.findViewById(R.id.ivBackToDashBoard)
        ivBackToDashBoard.setOnClickListener(this)
        tvSearchCar.setOnClickListener(this)
        btnVehicleReg.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.ivBackToDashBoard->
                {
                    activity?.onBackPressed()
                }
                R.id.btnVehicleReg -> {
                    if (etVehRegNum.text.isNotEmpty()) {
                        regNoVal = etVehRegNum.text.toString()
                        checkRegNoAvailable()
                    } else {
                        showToast("Please enter Vehicle Registration Number")
                    }
                }
                R.id.tvSearchCar -> {
                    navigateVehBasicDetailsActivity(CommonStrings.CAR_BASIC_DETAIL_ACTIVITY_REQUEST_CODE)
                    val carBasicDetailsActivity = Intent(activity, VehBasicDetailsActivity::class.java)
                    startActivityForResult(carBasicDetailsActivity, CommonStrings.CAR_BASIC_DETAIL_ACTIVITY_REQUEST_CODE)
                }

            }
        }

    }

    @SuppressLint("CheckResult")
    private fun checkRegNoAvailable() {
        if (isValidVehicleRegNo(regNoVal)) {
            showToast("Valid RegNo")


            // need to write API call to check given reg no is available
            var stockDetailsReq = StockDetailsReq()
            stockDetailsReq.UserId = "242"
            stockDetailsReq.UserType = "Dealer"
            stockDetailsReq.RequestFrom = "Dealer"
            var vehicleNum = VehicleRegNum()
            vehicleNum.vehicleNumber = regNoVal
            stockDetailsReq.data = vehicleNum

            stockAPIViewModel!!.getStockDetails(stockDetailsReq, Global.stock_details_base_url + CommonStrings.STOCK_DETAILS_URL_END)
            stockAPIViewModel!!.getStockDetailsLiveDataData()
                    .observe(this, { mApiResponse: ApiResponse? ->
                        onStockDetailsRes(
                                mApiResponse!!
                        )
                    })
        } else {
            showToast("Please enter valid Registration Number")
        }

    }

    private fun onStockDetailsRes(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                var stockResponse: StockResponse? = apiResponse.data as StockResponse?
                if (stockResponse?.status == true && stockResponse?.data != null) {
                    navigateToStockResFrag(stockResponse?.data!!)
                } else {
                    showToast(stockResponse?.message.toString())
                }
            }
            ApiResponse.Status.ERROR -> {
            }
            else -> showToast("There is no Data for entered Register number")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonStrings.CAR_BASIC_DETAIL_ACTIVITY_REQUEST_CODE && resultCode == CommonStrings.RESULT_CODE) {
            var addLeadRequest: AddLeadRequest? = data?.getParcelableExtra(CommonStrings.VEHICLE_DATA)
            if (addLeadRequest != null) {
                navigateToAddOrUpdateVehicleDetails(addLeadRequest)
            }
        }
    }

}