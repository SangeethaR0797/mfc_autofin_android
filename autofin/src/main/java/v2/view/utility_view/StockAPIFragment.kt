package  v2.view.utility_view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.navArgs
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.dto.AddLeadRequest
import v2.model.request.add_lead.AddLeadData
import v2.model.request.add_lead.AddLeadVehicleDetails
import v2.model.request.add_lead.IBBPriceData
import v2.model.request.add_lead.IBBPriceRequest
import v2.model.response.master.IBBPriceResponse
import v2.model_view.IBB.IBB_MasterViewModel
import v2.model_view.StockAPIViewModel
import v2.service.utility.ApiResponse
import v2.view.base.BaseFragment

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
    lateinit var ibbMasterViewModel: IBB_MasterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ibbMasterViewModel = ViewModelProvider(this@StockAPIFragment).get(
                IBB_MasterViewModel::class.java)

        ibbMasterViewModel.getIBBPriceLiveData().observe(this@StockAPIFragment, { mApiResponse: ApiResponse? ->
            onIBBPriceResponse(
                    mApiResponse!!
            )
        })

    }
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

        var owner=""

        owner = if(args.stockResArgs.owner!="NA" )
            formatOwner(args.stockResArgs.owner)
        else
            "NA"

        val price = resources.getString(R.string.rupees_symbol) + " " + formatAmount(args.stockResArgs.vehicleSellingPrice)

        var kms = ""
        kms = if(args.stockResArgs.kMs.matches(Regex("[0-9]+")))
        {
            formatAmount(args.stockResArgs.kMs)
        }
        else
            args.stockResArgs.kMs


        val vehRegNo = formatVehNum(args.stockResArgs.registrationNumber)

        val vehDetailsDesVal = owner + separator + price + separator + kms + separator + args.stockResArgs.fuelType + separator + args.stockResArgs.year
        tvVehMake.text = args.stockResArgs.ibbMake
        tvVehModelVariant.text = modelVariantVal
        tvVehDetailsDesc.text = vehDetailsDesVal
        tvVehRegNum.text = vehRegNo

        val vehicleDetails=AddLeadVehicleDetails()
        vehicleDetails.Make=args.stockResArgs.ibbMake
        vehicleDetails.Model=args.stockResArgs.ibbModel
        vehicleDetails.Variant=args.stockResArgs.ibbVariant
        vehicleDetails.VehicleNumber=args.stockResArgs.registrationNumber
        vehicleDetails.FuelType=args.stockResArgs.fuelType
        vehicleDetails.RegistrationYear=args.stockResArgs.year.toInt()
        vehicleDetails.KMs=args.stockResArgs.kMs
        if(args.stockResArgs.owner.matches(Regex("[0-9]+")))

            vehicleDetails.Ownership=args.stockResArgs.owner.toInt()
        else
            vehicleDetails.Ownership=0

        vehicleDetails.VehicleSellingPrice=args.stockResArgs.vehicleSellingPrice

        val addLeadData=AddLeadData()
        addLeadData.addLeadVehicleDetails=vehicleDetails

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
                    ibbMasterViewModel.getIBBPrice(getIBBPriceRequest(), Global.customerDetails_BaseURL+CommonStrings.IBB_PRICE_END_POINT)

                }

            }
        }
    }

    private fun getIBBPriceRequest(): IBBPriceRequest {

        val ibbPriceData= IBBPriceData(addLeadRequest.Data?.addLeadVehicleDetails?.RegistrationYear.toString(),
                addLeadRequest.Data?.addLeadVehicleDetails?.Make.toString(),
                addLeadRequest.Data?.addLeadVehicleDetails?.Model.toString(),
                addLeadRequest.Data?.addLeadVehicleDetails?.Variant.toString(),
                addLeadRequest.Data?.addLeadVehicleDetails?.KMs.toString(),
                addLeadRequest.Data?.addLeadVehicleDetails?.Ownership.toString(),
                addLeadRequest.Data?.addLeadVehicleDetails?.VehicleNumber.toString())

        return IBBPriceRequest(CommonStrings.DEALER_ID,CommonStrings.USER_TYPE,ibbPriceData)
    }


    private fun onIBBPriceResponse(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)

        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val ibbResponse: IBBPriceResponse? = mApiResponse.data as IBBPriceResponse?
                validateVehiclePrice(ibbResponse)
            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

    private fun validateVehiclePrice(ibbResponse: IBBPriceResponse?) {
        if (ibbResponse?.data != null) {
            val ibbPrice=getString(R.string.rupees_symbol)+" "+formatAmount(ibbResponse.data.toString())
            if (ibbResponse.data >= args.stockResArgs.vehicleSellingPrice.toInt()) {
                stockToAddLeadFragment(addLeadRequest,0,null)
            } else {
               Toast.makeText(requireContext(),getString(R.string.vehicle_price_error)+ibbPrice,Toast.LENGTH_SHORT).show()
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