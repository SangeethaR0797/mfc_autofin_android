package v2.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfc.autofin.framework.R
import kotlinx.android.synthetic.main.fragment_vehicle_selection.*
import utility.CommonStrings
import v2.model.dto.AddLeadRequest
import utility.Global
import v2.model.request.StockDetailsReq
import v2.model.request.VehicleRegNum
import v2.model.request.bank_offers.BankOfferData
import v2.model.request.bank_offers.BankOffersForApplicationRequest
import v2.model.request.bank_offers.LeadApplicationData
import v2.model.request.bank_offers.SelectRecommendedBankOfferRequest
import v2.model.response.StockResponse
import v2.model.response.bank_offers.BankOffersData
import v2.model.response.bank_offers.BankOffersForApplicationResponse
import v2.model.response.bank_offers.SelectRecommendedBankOfferResponse
import v2.model_view.BankOffersViewModel
import v2.model_view.StockAPIViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.SoftOfferAdapter
import v2.view.base.BaseFragment

public class VehicleSelectionFrag : BaseFragment(), View.OnClickListener {

    lateinit var etVehRegNum: EditText
    lateinit var llVehRegNum: LinearLayout
    lateinit var btnVehicleReg: Button
    lateinit var tvSearchCar: TextView
    lateinit var ivBackToDashBoard: ImageView

    lateinit var bankViewModel: BankOffersViewModel


    var regNoVal: String = ""
    var stockAPIViewModel: StockAPIViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bankViewModel = ViewModelProvider(this).get(
            BankOffersViewModel::class.java
        )


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vehicle_selection, container, false)
        initViews(view)

        stockAPIViewModel = ViewModelProvider(requireActivity()).get(
            StockAPIViewModel::class.java
        )
        stockAPIViewModel!!.getStockDetailsLiveDataData()
            .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                onStockDetailsRes(
                    mApiResponse!!
                )
            })
        return view
    }

    private fun initViews(view: View?) {
        etVehRegNum = view?.findViewById(R.id.etVehRegNum)!!
        llVehRegNum = view.findViewById(R.id.llVehRegNum)!!
        btnVehicleReg = view.findViewById(R.id.btnVehicleReg)
        tvSearchCar = view.findViewById(R.id.tvSearchCarV2)
        ivBackToDashBoard = view.findViewById(R.id.ivBackToDashBoard)
        ivBackToDashBoard.setOnClickListener(this)
        tvSearchCar.setOnClickListener(this)
        btnVehicleReg.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.ivBackToDashBoard -> {
                    activity?.onBackPressed()
                }
                R.id.btnVehicleReg -> {

                    if (etVehRegNum.text.isNotEmpty()) {
                        if (hasConnectivityNetwork()) {
                            llVehRegNum.setBackgroundResource(R.drawable.vtwo_input_bg)
                            tv_regno_hint.visibility = View.GONE
                            regNoVal = etVehRegNum.text.toString()
                            checkRegNoAvailable()
                        }
                    } else {
                        llVehRegNum.setBackgroundResource(R.drawable.v2_error_layout_bg)
                        tv_regno_hint.visibility = View.VISIBLE
                    }

                }
                R.id.tvSearchCarV2 -> {
                    if (hasConnectivityNetwork()) {
                        navigateVehBasicDetailsActivity(CommonStrings.CAR_BASIC_DETAIL_ACTIVITY_REQUEST_CODE)
                    }
                    //navToSoftOffer()
                }

            }
        }

    }

    @SuppressLint("CheckResult")
    private fun checkRegNoAvailable() {
        if (isValidVehicleRegNo(regNoVal)) {

            llVehRegNum.setBackgroundResource(R.drawable.vtwo_input_bg)
            tv_regno_hint.visibility = View.GONE
            showProgressDialog(requireActivity())
            if(CommonStrings.APP_NAME.equals("OMS"))
            {
                stockAPIViewModel!!.getStockDetails(
                        getStockRequest(),
                        Global.stock_details_base_url + CommonStrings.STOCK_DETAILS_URL_END
                )
            }
            else if(CommonStrings.APP_NAME.equals("Ediig"))
            {
                stockAPIViewModel!!.getStockDetails(
                        getStockRequest(),
                        CommonStrings.EDIGG_STOCK_API_URL
                )
            }

        } else {
            llVehRegNum.setBackgroundResource(R.drawable.v2_error_layout_bg)
            tv_regno_hint.visibility = View.VISIBLE
            tv_regno_hint.text = "Please enter valid Registration Number"
        }

    }

    private fun getStockRequest(): StockDetailsReq {
        val stockDetailsReq = StockDetailsReq()
        stockDetailsReq.UserId = CommonStrings.DEALER_ID

        if(CommonStrings.APP_NAME.equals(CommonStrings.APP_NAME_OMS))
        {
            stockDetailsReq.UserType = CommonStrings.USER_TYPE
            stockDetailsReq.RequestFrom = "Dealer"
            val vehicleNum = VehicleRegNum()
            vehicleNum.vehicleNumber = regNoVal
            stockDetailsReq.data = vehicleNum
            stockDetailsReq.accessKey=null
            stockDetailsReq.vehicleNumberE=null
        }
        else
        {
            stockDetailsReq.UserType = null
            stockDetailsReq.RequestFrom = null
            val vehicleNum = VehicleRegNum()
            vehicleNum.vehicleNumber =null
            stockDetailsReq.data =null
            stockDetailsReq.accessKey=CommonStrings.EDIGG_ACCESS_KEY
            stockDetailsReq.vehicleNumberE=regNoVal
        }

        return stockDetailsReq
    }

    private fun onStockDetailsRes(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                var stockResponse: StockResponse? = apiResponse.data as StockResponse?
                hideProgressDialog()
                if (stockResponse?.status == true && stockResponse.data != null) {
                    navigateToStockResFrag(stockResponse.data!!)
                } else {
                    showToast(stockResponse?.message.toString())
                }
            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()

            }
            else -> showToast("There is no Data for entered Register number")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonStrings.CAR_BASIC_DETAIL_ACTIVITY_REQUEST_CODE && resultCode == CommonStrings.RESULT_CODE) {
            var addLeadRequest: AddLeadRequest? =
                data?.getParcelableExtra(CommonStrings.VEHICLE_DATA)
            if (addLeadRequest != null) {
                navigateToAddOrUpdateVehicleDetails(addLeadRequest)
            }
        }
    }


}