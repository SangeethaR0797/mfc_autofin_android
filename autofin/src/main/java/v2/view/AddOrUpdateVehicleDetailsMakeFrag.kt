package v2.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.dto.DataSelectionDTO
import v2.model.response.Get_IBB_MasterDetailsResponse
import v2.model.response.master.KmsDrivenResponse
import v2.model.response.master.Types
import v2.model_view.IBB.IBB_MasterViewModel
import v2.model_view.MasterViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.DataRecyclerViewAdapter
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
class AddOrUpdateVehicleDetailsMakeFrag : Fragment() {
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
    lateinit var btnNext: Button

    lateinit var rvOwnership: RecyclerView
    lateinit var rvKilometresDriven: RecyclerView
    lateinit var rvFuleType: RecyclerView
    lateinit var ownershipDetailsAdapter: DataRecyclerViewAdapter
    lateinit var fuleDetailsAdapter: DataRecyclerViewAdapter
    lateinit var kmsDrivenAdapter: DataRecyclerViewAdapter

    lateinit var masterViewModel: MasterViewModel

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


        addEvent()
        addOwnershipDetails()
        addFuleDetails()

        masterViewModel.getKmsDrivenDetails(Global.customerDetails_BaseURL + CommonStrings.KMS_DRIVEN)
        return view
    }

    fun addEvent() {
        ivBack.setOnClickListener(View.OnClickListener { activity?.onBackPressed() })

        btnNext.setOnClickListener(View.OnClickListener { })

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