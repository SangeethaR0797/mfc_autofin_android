package v2.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.mfc.autofin.framework.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit_config.RetroBase
import utility.CommonStrings
import v2.model.dto.AddLeadRequest
import utility.Global
import v2.model.dto.VehicleAddUpdateDTO
import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model.request.StockDetailsReq
import v2.repository.MasterRepository
import v2.service.utility.ApiResponse
import v2.view.base.BaseFragment
import v2.view.other_activity.VehBasicDetailsActivity

public class VehicleSelectionFrag : BaseFragment(), View.OnClickListener {

    lateinit var etVehRegNum: EditText
    lateinit var btnVehicleReg: Button
    lateinit var tvSearchCar: TextView

    var regNoVal: String = ""
    private val stockDetailsData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vehicle_selection, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View?) {
        etVehRegNum = view?.findViewById(R.id.etVehRegNum)!!
        btnVehicleReg = view.findViewById(R.id.btnVehicleReg)
        tvSearchCar = view.findViewById(R.id.tvSearchCar)
        tvSearchCar.setOnClickListener(this)
        btnVehicleReg.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
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
        if (isValidRegNo()) {
            var stockDetailsReq= StockDetailsReq()
            stockDetailsReq.vehicleNumber=regNoVal
        if (isValidVehicleRegNo(regNoVal)) {
            showToast("Valid RegNo")
            // need to write API call to check given reg no is available

            val repository=MasterRepository()
            repository.getStockDetails(stockDetailsReq!!, Global.stock_details_base_url + CommonStrings.STOCK_DETAILS_URL_END)?.subscribeOn(Schedulers.io())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.doOnSubscribe { d -> stockDetailsData.setValue(ApiResponse.loading()) }

            navigateToStockResFrag()
        } else {
            showToast("Please enter valid Registration Number")
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