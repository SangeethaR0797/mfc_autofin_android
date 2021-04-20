package v2.view

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.navigation.NavArgument
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.dto.DataSelectionDTO
import v2.model.dto.VehicleAddUpdateDTO
import v2.model.response.Get_IBB_MasterDetailsResponse
import v2.model.response.master.KmsDrivenResponse
import v2.model.response.master.Types
import v2.model_view.IBB.IBB_MasterViewModel
import v2.model_view.MasterViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.DataRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.itemClickCallBack
import v2.view.utility_view.GridItemDecoration


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddOrUpdateVehicleDetailsMakeFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddOrUpdateVehicleDetailsMakeFrag : BaseFragment() {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var ivBack: ImageView
    lateinit var tvTitle: TextView
    lateinit var tvSelectedText: TextView

    lateinit var llOwnership: LinearLayout
    lateinit var llKilometresDriven: LinearLayout
    lateinit var llFuleType: LinearLayout

    lateinit var llVehiclePrice: LinearLayout
    lateinit var llPrice: LinearLayout

    lateinit var llAddVehicleNumber: LinearLayout
    lateinit var llVehicleNumber: LinearLayout

    lateinit var etPrice: EditText
    lateinit var etVehicleNumber: EditText

    lateinit var btnNext: Button

    lateinit var rvOwnership: RecyclerView
    lateinit var rvKilometresDriven: RecyclerView
    lateinit var rvFuleType: RecyclerView
    lateinit var ownershipDetailsAdapter: DataRecyclerViewAdapter
    lateinit var fuleDetailsAdapter: DataRecyclerViewAdapter
    lateinit var kmsDrivenAdapter: DataRecyclerViewAdapter

    lateinit var masterViewModel: MasterViewModel

    lateinit var vehicleAddUpdateDTO: VehicleAddUpdateDTO

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AddOrUpdateVehicleDetailsMakeFrag().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        masterViewModel = ViewModelProvider(this@AddOrUpdateVehicleDetailsMakeFrag).get(
                MasterViewModel::class.java
        )
        masterViewModel!!.getKmsDrivenLiveData()
                .observe(this@AddOrUpdateVehicleDetailsMakeFrag, { mApiResponse: ApiResponse? ->
                    onKmsDrivenResponse(
                            mApiResponse!!
                    )
                })


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.v2_fragment_veh_make, container, false)

        arguments?.let {
            val safeArgs = AddOrUpdateVehicleDetailsMakeFragArgs.fromBundle(it)
            vehicleAddUpdateDTO = safeArgs.vehicleDetails

        }

        ivBack = view.findViewById(R.id.iv_back)
        tvTitle = view.findViewById(R.id.tv_title)
        tvSelectedText = view.findViewById(R.id.tv_selected_text)

        llOwnership = view.findViewById(R.id.ll_ownership)
        llKilometresDriven = view.findViewById(R.id.ll_kilometres_driven)
        llFuleType = view.findViewById(R.id.ll_fule_type)
        llVehiclePrice = view.findViewById(R.id.ll_vehicle_price)

        rvOwnership = view.findViewById(R.id.rv_ownership)
        rvKilometresDriven = view.findViewById(R.id.rv_kilometres_driven)
        rvFuleType = view.findViewById(R.id.rv_fule_type)


        btnNext = view.findViewById(R.id.btn_next)

        llPrice = view.findViewById(R.id.ll_price)

        llAddVehicleNumber = view.findViewById(R.id.ll_add_vehicle_number)
        llVehicleNumber = view.findViewById(R.id.ll_vehicle_number)

        etPrice = view.findViewById(R.id.et_price)
        etVehicleNumber = view.findViewById(R.id.et_vehicle_number)

        tvTitle.text = vehicleAddUpdateDTO.make
        tvSelectedText.text = vehicleAddUpdateDTO.year + "-" + vehicleAddUpdateDTO.make + "-" + vehicleAddUpdateDTO.model + "-" + vehicleAddUpdateDTO.variant
        addEvent()
        addOwnershipDetails()
        addFuleDetails()

        masterViewModel.getKmsDrivenDetails(Global.customerDetails_BaseURL + CommonStrings.KMS_DRIVEN)
        return view
    }

    fun addEvent() {
        ivBack.setOnClickListener(View.OnClickListener { activity?.onBackPressed() })
        llPrice.setOnClickListener(View.OnClickListener { etPrice.requestFocus() })
        llAddVehicleNumber.setOnClickListener(View.OnClickListener { etVehicleNumber.requestFocus() })

        btnNext.setOnClickListener(View.OnClickListener {
            hideSoftKeyboard()
            if (vehicleAddUpdateDTO.price == null) {

                showToast("Please enter price details.")
            } else if (vehicleAddUpdateDTO.registrationNumber == null && llVehicleNumber.visibility.equals(View.GONE)) {
                llVehicleNumber.visibility = View.VISIBLE
            } else if (vehicleAddUpdateDTO.registrationNumber == null && llVehicleNumber.visibility.equals(View.VISIBLE)) {
                showToast("Please enter vehicle registration No.")
            } else {
                showToast("Save Data")
            }
        })

        etPrice.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                if (s != "") {
                    //do your work here
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (TextUtils.isEmpty(etPrice.text)) {
                    vehicleAddUpdateDTO.price = null
                } else {
                    vehicleAddUpdateDTO.price = etPrice.text.toString()
                }
            }
        })

        etVehicleNumber.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                if (s != "") {
                    //do your work here
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (TextUtils.isEmpty(etVehicleNumber.text)) {
                    vehicleAddUpdateDTO.registrationNumber = null
                } else {
                    vehicleAddUpdateDTO.registrationNumber = etVehicleNumber.text.toString()
                }
            }
        })

        llKilometresDriven.visibility = View.GONE
        llFuleType.visibility = View.GONE
        llVehiclePrice.visibility = View.GONE
        llVehicleNumber.visibility = View.GONE
        btnNext.visibility = View.GONE

    }

    private fun addOwnershipDetails() {

        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        list.add(DataSelectionDTO("1", "st", "1", false))
        list.add(DataSelectionDTO("2", "nd", "2", false))
        list.add(DataSelectionDTO("3", "rd", "3", false))
        list.add(DataSelectionDTO("4", "th", "4", false))
        list.add(DataSelectionDTO("5", "th", "5", false))

        ownershipDetailsAdapter = DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
            override fun itemClick(item: Any?, position: Int) {


                ownershipDetailsAdapter.dataListFilter!!.forEachIndexed { index, item ->
                    run {
                        if (index == position) {
                            item.selected = true
                            vehicleAddUpdateDTO.ownership = item.value
                            llKilometresDriven.visibility = View.VISIBLE
                        } else {
                            item.selected = false
                        }
                    }
                }
                ownershipDetailsAdapter.notifyDataSetChanged()
            }
        })


        val layoutManagerStaggeredGridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 3)

        rvOwnership.addItemDecoration(GridItemDecoration(25, 3))

        rvOwnership.setLayoutManager(layoutManagerStaggeredGridLayoutManager)

        rvOwnership.setAdapter(ownershipDetailsAdapter)
    }

    private fun addFuleDetails() {

        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        list.add(DataSelectionDTO("Petrol", null, "Petrol", false))
        list.add(DataSelectionDTO("Diesel", null, "Diesel", false))
        list.add(DataSelectionDTO("Electric", null, "Electric", false))
        list.add(DataSelectionDTO("CNG", null, "CNG", false))
        list.add(DataSelectionDTO("LPG", null, "LPG", false))

        fuleDetailsAdapter = DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
            override fun itemClick(item: Any?, position: Int) {


                fuleDetailsAdapter.dataListFilter!!.forEachIndexed { index, item ->
                    run {
                        if (index == position) {
                            item.selected = true
                            llVehiclePrice.visibility = View.VISIBLE
                            btnNext.visibility = View.VISIBLE
                            vehicleAddUpdateDTO.fule_type = item.value
                        } else {
                            item.selected = false
                        }
                    }
                }
                fuleDetailsAdapter.notifyDataSetChanged()
            }
        })


        val layoutManagerStaggeredGridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 3)

        rvFuleType.addItemDecoration(GridItemDecoration(25, 3))

        rvFuleType.setLayoutManager(layoutManagerStaggeredGridLayoutManager)

        rvFuleType.setAdapter(fuleDetailsAdapter)
    }

    private fun onKmsDrivenResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val masterResponse: KmsDrivenResponse? = mApiResponse.data as KmsDrivenResponse?
                setKmsDrivenData(masterResponse!!.data.types)

            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

    private fun setKmsDrivenData(types: List<Types>) {
        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        types.forEachIndexed { index, types ->
            list.add(DataSelectionDTO(types.displayLabel, null, types.value, false))
        }



        kmsDrivenAdapter = DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
            override fun itemClick(item: Any?, position: Int) {


                kmsDrivenAdapter.dataListFilter!!.forEachIndexed { index, item ->
                    run {
                        if (index == position) {
                            item.selected = true
                            llFuleType.visibility = View.VISIBLE
                            vehicleAddUpdateDTO.kilometres_driven = item.value
                        } else {
                            item.selected = false
                        }
                    }
                }
                kmsDrivenAdapter.notifyDataSetChanged()
            }
        })


        val layoutManagerStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

        rvKilometresDriven.addItemDecoration(GridItemDecoration(25, 2))

        rvKilometresDriven.setLayoutManager(layoutManagerStaggeredGridLayoutManager)

        rvKilometresDriven.setAdapter(kmsDrivenAdapter)
    }


}