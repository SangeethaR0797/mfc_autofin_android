package  v2.view.utility_view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.mfc.autofin.framework.R
import utility.CommonStrings
import v2.model.dto.AddLeadRequest
import v2.model_view.StockAPIViewModel
import v2.view.base.BaseFragment
import v2.view.other_activity.VehBasicDetailsActivity

class StockAPIFragment : BaseFragment(), View.OnClickListener {

    private val args by navArgs<StockAPIFragmentArgs>()


    lateinit var tvVehMake: TextView
    lateinit var tvVehModelVariant: TextView
    lateinit var tvVehDetailsDesc: TextView
    lateinit var tvVehRegNum: TextView
    lateinit var ibEditVehDetails: ImageButton
    lateinit var btnVehicleReg: Button
    val separator = " | "

    private lateinit var viewModel: StockAPIViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.stock_a_p_i_fragment, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View?) {

        tvVehMake = view?.findViewById(R.id.tvVehMake)!!
        tvVehModelVariant = view?.findViewById(R.id.tvVehModelVariant)!!
        tvVehDetailsDesc = view?.findViewById(R.id.tvVehDetailsDesc)!!
        tvVehRegNum = view?.findViewById(R.id.tvVehRegNum)!!
        ibEditVehDetails = view?.findViewById(R.id.ibEditVehDetails)!!
        btnVehicleReg = view?.findViewById(R.id.btnVehicleReg)!!

        val modelVariantVal = args.stockResArgs.ibbModel + " " + args.stockResArgs.ibbVariant
        val vehDetailsDesVal=args.stockResArgs.owner + separator + args.stockResArgs.vehicleSellingPrice + separator + args.stockResArgs.fuelType + args.stockResArgs.year
        tvVehMake.text = args.stockResArgs.ibbMake
        tvVehModelVariant.text = modelVariantVal
        tvVehDetailsDesc.text = vehDetailsDesVal
        tvVehRegNum.text = args.stockResArgs.registrationNumber

        ibEditVehDetails.setOnClickListener(this)
        btnVehicleReg.setOnClickListener(this)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StockAPIViewModel::class.java)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.ibEditVehDetails -> {
                    navigateVehBasicDetailsActivity(CommonStrings.CAR_BASIC_DETAIL_ACTIVITY_REQUEST_CODE)
                    val carBasicDetailsActivity = Intent(activity, VehBasicDetailsActivity::class.java)
                    startActivityForResult(carBasicDetailsActivity, CommonStrings.CAR_BASIC_DETAIL_ACTIVITY_REQUEST_CODE)
                }
                R.id.btnVehicleReg -> {
                    showToast("Development is in progress")
                }

            }
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