package  v2.view.utility_view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.mfc.autofin.framework.R
import utility.CommonStrings
import v2.model.dto.AddLeadRequest
import v2.model.request.add_lead.AddLeadData
import v2.model.request.add_lead.VehicleDetails
import v2.model_view.StockAPIViewModel
import v2.view.base.BaseFragment
import v2.view.other_activity.VehBasicDetailsActivity

class StockAPIFragment : BaseFragment(), View.OnClickListener {

    private val args by navArgs<StockAPIFragmentArgs>()


    lateinit var tvVehMake: TextView
    lateinit var tvVehModelVariant: TextView
    lateinit var tvVehDetailsDesc: TextView
    lateinit var tvVehRegNum: TextView
    lateinit var ivBackToVehSelection: ImageView
    lateinit var ibEditVehDetails: ImageButton
    lateinit var btnVehicleReg: Button
    val separator = " | "
    val addLeadRequest=AddLeadRequest()

    private lateinit var viewModel: StockAPIViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.stock_a_p_i_fragment, container, false)
        Log.i("StockAPIFragment", "onCreateView: " + args.stockResArgs.stockId)
        initViews(view)
        return view
    }

    private fun initViews(view: View?) {

        tvVehMake = view?.findViewById(R.id.tvVehMake)!!
        tvVehModelVariant = view?.findViewById(R.id.tvVehModelVariant)!!
        tvVehDetailsDesc = view?.findViewById(R.id.tvVehDetailsDesc)!!
        tvVehRegNum = view?.findViewById(R.id.tvVehRegNum)!!
        ivBackToVehSelection = view?.findViewById(R.id.ivBackToVehSelection)
        ibEditVehDetails = view?.findViewById(R.id.ibEditVehDetails)!!
        btnVehicleReg = view?.findViewById(R.id.btnVehicleReg)!!

        ivBackToVehSelection.setOnClickListener(this)
        ibEditVehDetails.setOnClickListener(this)
        btnVehicleReg.setOnClickListener(this)

        displayStockDetails()
    }

    private fun displayStockDetails() {

        val modelVariantVal = args.stockResArgs.ibbModel + " " + args.stockResArgs.ibbVariant
        val owner = formatOwner(args.stockResArgs.owner)
        val price = resources.getString(R.string.rupees_symbol) + " " + formatAmount(args.stockResArgs.vehicleSellingPrice)
        val kms = formatAmount(args.stockResArgs.kMs)
        val vehRegNo = formatVehNum(args.stockResArgs.registrationNumber)
        val vehDetailsDesVal = owner + separator + price + separator + kms + separator + args.stockResArgs.fuelType + separator + args.stockResArgs.year
        tvVehMake.text = args.stockResArgs.ibbMake
        tvVehModelVariant.text = modelVariantVal
        tvVehDetailsDesc.text = vehDetailsDesVal
        tvVehRegNum.text = vehRegNo

        val vehicleDetails=VehicleDetails()
        vehicleDetails.Make=args.stockResArgs.ibbMake
        vehicleDetails.Model=args.stockResArgs.ibbModel
        vehicleDetails.Variant=args.stockResArgs.ibbVariant
        vehicleDetails.VehicleNumber=args.stockResArgs.registrationNumber
        vehicleDetails.FuelType=args.stockResArgs.fuelType
        vehicleDetails.RegistrationYear=args.stockResArgs.year.toInt()
        vehicleDetails.KMs=args.stockResArgs.kMs
        vehicleDetails.Ownership=args.stockResArgs.owner.toInt()
        vehicleDetails.VehicleSellingPrice=args.stockResArgs.vehicleSellingPrice

        val addLeadData=AddLeadData()
        addLeadData.vehicleDetails=vehicleDetails

        addLeadRequest.Data=addLeadData
        //addLeadRequest.Data?.vehicleDetails=addLeadData.vehicleDetails

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StockAPIViewModel::class.java)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.ivBackToVehSelection -> {
                    activity?.onBackPressed()
                }
                R.id.ibEditVehDetails -> {
                    navigateVehBasicDetailsActivity(CommonStrings.CAR_BASIC_DETAIL_ACTIVITY_REQUEST_CODE)

                }
                R.id.btnVehicleReg -> {
                    stockToAddLeadFragment(addLeadRequest)
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