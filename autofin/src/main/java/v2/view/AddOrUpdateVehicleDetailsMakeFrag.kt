package v2.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.mfc.autofin.framework.R
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import utility.CommonStrings
import utility.Global
import v2.model.dto.AddLeadRequest
import v2.model.dto.DataSelectionDTO
import v2.model.request.add_lead.IBBPriceData
import v2.model.request.add_lead.IBBPriceRequest
import v2.model.response.master.IBBPriceResponse
import v2.model.response.master.MasterResponse
import v2.model.response.master.Types
import v2.model_view.IBB.IBB_MasterViewModel
import v2.model_view.MasterViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.DataRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.itemClickCallBack
import v2.view.utility_view.GridItemDecoration
import java.lang.Exception
import java.lang.NullPointerException
import java.lang.NumberFormatException
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

public class AddOrUpdateVehicleDetailsMakeFrag : BaseFragment(), KeyboardVisibilityEventListener, View.OnClickListener {


    private var param1: String? = null
    private var param2: String? = null
    lateinit var ivBack: ImageView
    lateinit var scrollView1: ScrollView
    lateinit var tvTitle: TextView
    lateinit var tvSelectedText: TextView
    lateinit var tvVehiclePriceInWords: TextView
    lateinit var tvVehiclePriceErrorMessage: TextView
    lateinit var tvVehicleNumberErrorMessage: TextView

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
    lateinit var viewEmpty: View

    lateinit var rvOwnership: RecyclerView
    lateinit var rvKilometresDriven: RecyclerView
    lateinit var rvFuleType: RecyclerView
    lateinit var ownershipDetailsAdapter: DataRecyclerViewAdapter
    lateinit var fuleDetailsAdapter: DataRecyclerViewAdapter
    lateinit var kmsDrivenAdapter: DataRecyclerViewAdapter

    lateinit var masterViewModel: MasterViewModel
    lateinit var ibbMasterViewModel: IBB_MasterViewModel

    lateinit var addLeadRequest: AddLeadRequest
    var rootView: View? = null


    //region KeyBoardVisible
    override fun onVisibilityChanged(isKeyBoardVisible: Boolean) {
        if (viewEmpty != null) {
            if (isKeyBoardVisible) {
                if (viewEmpty.visibility == View.VISIBLE) {
                    viewEmpty.visibility = View.GONE
                    Thread.sleep(200)
                }

                viewEmpty.visibility = View.VISIBLE

                val view = requireActivity().currentFocus
                if (view != null && view is EditText) {
                    var viewToScroll: View? = null
                    when {
                        etVehicleNumber.hasFocus() -> {
                            viewToScroll = llVehicleNumber
                        }
                        etPrice.hasFocus() -> {
                            viewToScroll = llVehiclePrice
                        }
                        viewToScroll != null -> {
                            scrollToBottom(viewToScroll)
                        }
                    }
                    /* Handler().postDelayed({
                         ThreadUtils.runOnUiThread(Runnable {

                         })

                     }, 500)*/
                }

            } else {
                viewEmpty.visibility = View.GONE
                val view = requireActivity().currentFocus
                if (view != null && view is EditText) {
                    view.clearFocus()
                }

            }
        }
    }

    //endregion KeyBoardVisible
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val safeArgs = AddOrUpdateVehicleDetailsMakeFragArgs.fromBundle(it)
            addLeadRequest = safeArgs.addLeadRequestDetails
        }
        masterViewModel = ViewModelProvider(this@AddOrUpdateVehicleDetailsMakeFrag).get(
                MasterViewModel::class.java
        )

        ibbMasterViewModel = ViewModelProvider(this@AddOrUpdateVehicleDetailsMakeFrag).get(
                IBB_MasterViewModel::class.java)

        masterViewModel.getKmsDrivenLiveData()
                .observe(this@AddOrUpdateVehicleDetailsMakeFrag, { mApiResponse: ApiResponse? ->
                    onKmsDrivenResponse(
                            mApiResponse!!
                    )
                })

        ibbMasterViewModel.getIBBPriceLiveData().observe(this@AddOrUpdateVehicleDetailsMakeFrag, { mApiResponse: ApiResponse? ->
            onIBBPriceResponse(
                    mApiResponse!!
            )
        })
    }


    fun scrollToBottom(nextView: View?) {
        if (nextView != null) {
            scrollView1.post {
                // scrollView1.fullScroll(View.FOCUS_DOWN)
                scrollView1.scrollTo(0, nextView.top);
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
          if (rootView != null) {
            return rootView
        } else {
              rootView = inflater.inflate(R.layout.v2_fragment_veh_make, container, false)
              initView()

          }
        return rootView
    }

    private fun initView() {
        viewEmpty = rootView?.findViewById(R.id.view_empty)!!
        ivBack = rootView?.findViewById(R.id.iv_veh_details_make_back)!!
        scrollView1 = rootView?.findViewById(R.id.scrollView1)!!
        tvTitle = rootView?.findViewById(R.id.tv_title)!!
        tvSelectedText = rootView?.findViewById(R.id.tv_selected_text)!!

        tvVehiclePriceInWords = rootView?.findViewById(R.id.tv_vehicle_price_in_words)!!
        tvVehiclePriceErrorMessage = rootView?.findViewById(R.id.tv_vehicle_price_error_message)!!
        tvVehicleNumberErrorMessage = rootView?.findViewById(R.id.tv_vehicle_number_error_message)!!
        tvVehiclePriceErrorMessage.visibility = View.GONE
        tvVehicleNumberErrorMessage.visibility = View.GONE

        llOwnership = rootView?.findViewById(R.id.ll_ownership)!!
        llKilometresDriven = rootView?.findViewById(R.id.ll_kilometres_driven)!!
        llFuleType = rootView?.findViewById(R.id.ll_fule_type)!!
        llVehiclePrice = rootView?.findViewById(R.id.ll_vehicle_price)!!

        rvOwnership = rootView?.findViewById(R.id.rv_ownership)!!
        rvKilometresDriven = rootView?.findViewById(R.id.rv_kilometres_driven)!!
        rvFuleType = rootView?.findViewById(R.id.rv_fule_type)!!


        btnNext = rootView?.findViewById(R.id.btn_next)!!

        llPrice = rootView?.findViewById(R.id.ll_price)!!

        llAddVehicleNumber = rootView?.findViewById(R.id.ll_add_vehicle_number)!!
        llVehicleNumber = rootView?.findViewById(R.id.ll_vehicle_number)!!

        etPrice = rootView?.findViewById(R.id.et_price)!!
        etVehicleNumber = rootView?.findViewById(R.id.et_vehicle_number)!!

        tvTitle.text = addLeadRequest.Data?.addLeadVehicleDetails?.Make
        tvSelectedText.text =
                "" + addLeadRequest.Data?.addLeadVehicleDetails?.RegistrationYear + "-" + addLeadRequest.Data?.addLeadVehicleDetails?.Make + "-" + addLeadRequest.Data?.addLeadVehicleDetails?.Model + "-" + addLeadRequest.Data?.addLeadVehicleDetails?.Variant

        llPrice.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        llAddVehicleNumber.setOnClickListener(this)
        btnNext.setOnClickListener(this)

        addEvent()
        etPriceTextWatcher()

        if (addLeadRequest.Data?.addLeadVehicleDetails?.Ownership == null || addLeadRequest.Data?.addLeadVehicleDetails?.KMs == null ||
                addLeadRequest.Data?.addLeadVehicleDetails?.FuelType == null) {
            llKilometresDriven.visibility = View.GONE
            llFuleType.visibility = View.GONE
            llVehicleNumber.visibility = View.GONE
            llVehiclePrice.visibility = View.GONE
            btnNext.visibility = View.GONE
        }
        setKeyBoardShowHideEvent(AddOrUpdateVehicleDetailsMakeFrag@ this)

        addOwnershipDetails()
        addFuleDetails()

        masterViewModel.getKmsDrivenDetails(Global.customerDetails_BaseURL + CommonStrings.KMS_DRIVEN)

        setLastSelectedData()
    }


    private fun addEvent() {

        etVehicleNumber.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                    s: CharSequence, start: Int, before: Int,
                    count: Int
            ) {
                llAddVehicleNumber.setBackgroundResource(R.drawable.vtwo_input_bg)
                etVehicleNumber.setTextColor(resources.getColor(R.color.vtwo_black))
                tvVehicleNumberErrorMessage.visibility = View.GONE
            }

            override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int,
                    after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (TextUtils.isEmpty(etVehicleNumber.text)) {
                    addLeadRequest.Data?.addLeadVehicleDetails?.VehicleNumber = null
                } else {
                    addLeadRequest.Data?.addLeadVehicleDetails?.VehicleNumber =
                            etVehicleNumber.text.toString()
                }
            }
        })


    }

    private fun etPriceTextWatcher() {

        var timer: Timer? = null
        var allowEdit: Boolean = true

        etPrice.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                llPrice.setBackgroundResource(R.drawable.vtwo_input_bg)
                etPrice.setTextColor(resources.getColor(R.color.vtwo_black))
                tvVehiclePriceErrorMessage.visibility = View.GONE

            }

            override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int,
                    after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {
                try {
                    if (timer != null) {
                        timer!!.cancel();
                    }

                    if (addLeadRequest.Data?.addLeadVehicleDetails?.VehicleSellingPrice != null) {
                        try {
                            if (unformatAmount(etPrice.text.toString()) != addLeadRequest.Data?.addLeadVehicleDetails?.VehicleSellingPrice
                                    || TextUtils.isEmpty(etPrice.text.toString())
                                    || TextUtils.isEmpty(addLeadRequest.Data?.addLeadVehicleDetails?.VehicleSellingPrice) ||
                                    TextUtils.isEmpty(etPrice.text.toString())) {
                                allowEdit = true
                            }
                        } catch (nEx: NumberFormatException) {
                            Log.i("AddOrUpdate", "afterTextChanged: a")
                            nEx.printStackTrace()
                        } catch (nullException: NullPointerException) {
                            Log.i("AddOrUpdate", "afterTextChanged: a")
                            nullException.printStackTrace()
                        }

                    }
                    if (allowEdit) {
                        timer = Timer()
                        timer!!.schedule(object : TimerTask() {
                            override fun run() {

                                if (TextUtils.isEmpty(etPrice.text)) {
                                    addLeadRequest.Data?.addLeadVehicleDetails?.VehicleSellingPrice =
                                            null
                                } else {

                                    addLeadRequest.Data?.addLeadVehicleDetails?.VehicleSellingPrice =
                                            unformatAmount(etPrice.text.toString())
                                }
                                allowEdit = false
                                ThreadUtils.runOnUiThread(Runnable {
                                    if (!TextUtils.isEmpty(etPrice.text.toString())) {
                                        etPrice.setText(formatAmount(unformatAmount(etPrice.text.toString())))
                                        tvVehiclePriceInWords.visibility = View.VISIBLE
                                        tvVehiclePriceInWords.text =
                                                (getAmountInWords(unformatAmount(etPrice.text.toString())))
                                        etPrice.setSelection(etPrice.text.toString().length)
                                    } else {
                                        tvVehiclePriceInWords.text = ""
                                    }
                                })
                            }
                        }, 600)
                    } else {
                        tvVehiclePriceInWords.text = ""
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
        })

    }

    private fun getIBBPriceRequest(): IBBPriceRequest {

        val ibbPriceData = IBBPriceData(addLeadRequest.Data?.addLeadVehicleDetails?.RegistrationYear.toString(),
                addLeadRequest.Data?.addLeadVehicleDetails?.Make.toString(),
                addLeadRequest.Data?.addLeadVehicleDetails?.Model.toString(),
                addLeadRequest.Data?.addLeadVehicleDetails?.Variant.toString(),
                addLeadRequest.Data?.addLeadVehicleDetails?.KMs.toString(),
                addLeadRequest.Data?.addLeadVehicleDetails?.Ownership.toString(),
                addLeadRequest.Data?.addLeadVehicleDetails?.VehicleNumber.toString())

        return IBBPriceRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, ibbPriceData)
    }

    private fun addOwnershipDetails() {

        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        list.add(DataSelectionDTO("1", "st", "1", false))
        list.add(DataSelectionDTO("2", "nd", "2", false))
        list.add(DataSelectionDTO("3", "rd", "3", false))
        list.add(DataSelectionDTO("4", "th", "4", false))
        list.add(DataSelectionDTO("5", "th", "5", false))

        ownershipDetailsAdapter =
                DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                    override fun itemClick(item: Any?, position: Int) {


                        ownershipDetailsAdapter.dataListFilter!!.forEachIndexed { index, item ->
                            run {
                                if (index == position) {
                                    item.selected = true
                                    addLeadRequest.Data?.addLeadVehicleDetails?.Ownership =
                                            item.value?.toInt()
                                    llKilometresDriven.visibility = View.VISIBLE
                                    scrollToBottom(llKilometresDriven)
                                } else {
                                    item.selected = false
                                }
                            }
                        }
                        ownershipDetailsAdapter.notifyDataSetChanged()
                    }
                })


        val layoutManagerStaggeredGridLayoutManager =
                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 3)

        rvOwnership.addItemDecoration(GridItemDecoration(25, 3))

        rvOwnership.layoutManager = layoutManagerStaggeredGridLayoutManager

        rvOwnership.adapter = ownershipDetailsAdapter
    }

    private fun addFuleDetails() {

        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        list.add(DataSelectionDTO("Petrol", null, "Petrol", false))
        list.add(DataSelectionDTO("Diesel", null, "Diesel", false))
        list.add(DataSelectionDTO("Electric", null, "Electric", false))
        list.add(DataSelectionDTO("CNG", null, "CNG", false))
        list.add(DataSelectionDTO("LPG", null, "LPG", false))

        fuleDetailsAdapter =
                DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                    override fun itemClick(item: Any?, position: Int) {


                        fuleDetailsAdapter.dataListFilter!!.forEachIndexed { index, item ->
                            run {
                                if (index == position) {
                                    item.selected = true
                                    llVehicleNumber.visibility = View.VISIBLE
                                    scrollToBottom(llVehicleNumber)
                                    btnNext.visibility = View.VISIBLE
                                    addLeadRequest.Data?.addLeadVehicleDetails?.FuelType = item.value
                                } else {
                                    item.selected = false
                                }
                            }
                        }
                        fuleDetailsAdapter.notifyDataSetChanged()
                    }
                })


        val layoutManagerStaggeredGridLayoutManager =
                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 3)

        rvFuleType.addItemDecoration(GridItemDecoration(25, 3))

        rvFuleType.setLayoutManager(layoutManagerStaggeredGridLayoutManager)

        rvFuleType.setAdapter(fuleDetailsAdapter)
    }

    private fun onKmsDrivenResponse(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)

        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                setStaticKMsDrivenData()
            }
            ApiResponse.Status.SUCCESS -> {
                val masterResponse: MasterResponse? = mApiResponse.data as MasterResponse?
                masterResponse?.data?.let { setKmsDrivenData(it?.types) }
                setLastSelectedData()
            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

    private fun onIBBPriceResponse(mApiResponse: ApiResponse) {

        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val ibbResponse: IBBPriceResponse? = mApiResponse.data as IBBPriceResponse?
                validateVehiclePrice(ibbResponse)
            }
            ApiResponse.Status.ERROR -> {

            }
            else -> {
            }
        }
    }

    private fun validateVehiclePrice(ibbResponse: IBBPriceResponse?) {

        if (ibbResponse?.data != null) {
            val ibbPrice = getString(R.string.rupees_symbol) + " " + formatAmount(ibbResponse.data.toString())
            if (ibbResponse.data >= unformatAmount(etPrice.text.toString()).toInt()) {
                addLeadRequest.Data?.addLeadVehicleDetails?.VehicleSellingPrice = etPrice.text.toString()
                navigateToAddLeadFragment(addLeadRequest, 0, null)
            } else {
                llPrice.setBackgroundResource(R.drawable.v2_error_input_bg)
                etPrice.setTextColor(resources.getColor(R.color.error_red))
                tvVehiclePriceErrorMessage.visibility = View.VISIBLE
                tvVehiclePriceErrorMessage.text = getString(R.string.vehicle_price_error) + ibbPrice
                tvVehiclePriceInWords.visibility = View.GONE
            }
        }

    }

    private fun setStaticKMsDrivenData() {
        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        list.add(DataSelectionDTO("0 - 10,000", null, "5000", false))
        list.add(DataSelectionDTO("10,001 - 25,000", null, "17000", false))
        list.add(DataSelectionDTO("25,001 - 40,000", null, "33000", false))
        list.add(DataSelectionDTO("40,001 - 60,000", null, "50000", false))
        list.add(DataSelectionDTO("60,001 - 80,000", null, "70000", false))
        list.add(DataSelectionDTO("80,001 - 100,000", null, "90000", false))
        list.add(DataSelectionDTO("Above 100,000", null, "120000", false))

        kmsDrivenAdapter =
                DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                    override fun itemClick(item: Any?, position: Int) {


                        kmsDrivenAdapter.dataListFilter?.forEachIndexed { index, item ->
                            run {
                                if (index == position) {
                                    item.selected = true
                                    llFuleType.visibility = View.VISIBLE
                                    addLeadRequest.Data?.addLeadVehicleDetails?.KMs = item.value
                                    scrollToBottom(llFuleType)
                                } else {
                                    item.selected = false
                                }
                            }
                        }
                        kmsDrivenAdapter.notifyDataSetChanged()
                    }
                })


        val layoutManagerStaggeredGridLayoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

        rvKilometresDriven.addItemDecoration(GridItemDecoration(25, 2))

        rvKilometresDriven.layoutManager = layoutManagerStaggeredGridLayoutManager

        rvKilometresDriven.adapter = kmsDrivenAdapter


    }

    private fun setKmsDrivenData(types: List<Types>) {
        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        types.forEachIndexed { index, types ->
            list.add(DataSelectionDTO(types.displayLabel, null, types.value, false))
        }

        kmsDrivenAdapter =
                DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                    override fun itemClick(item: Any?, position: Int) {


                        kmsDrivenAdapter.dataListFilter?.forEachIndexed { index, item ->
                            run {
                                if (index == position) {
                                    item.selected = true
                                    llFuleType.visibility = View.VISIBLE
                                    addLeadRequest.Data?.addLeadVehicleDetails?.KMs = item.value
                                    scrollToBottom(llFuleType)
                                } else {
                                    item.selected = false
                                }
                            }
                        }
                        kmsDrivenAdapter.notifyDataSetChanged()
                    }
                })


        val layoutManagerStaggeredGridLayoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

        rvKilometresDriven.addItemDecoration(GridItemDecoration(25, 2))

        rvKilometresDriven.layoutManager = layoutManagerStaggeredGridLayoutManager

        rvKilometresDriven.adapter = kmsDrivenAdapter
    }

    private fun setLastSelectedData() {
        if (addLeadRequest.Data?.addLeadVehicleDetails?.Ownership != null) {
            ownershipDetailsAdapter.dataListFilter!!.forEachIndexed { index, dataSelectionDTO ->
                dataSelectionDTO.selected =
                        addLeadRequest.Data?.addLeadVehicleDetails?.Ownership?.toString()
                                .equals(dataSelectionDTO.value) == true
            }
            ownershipDetailsAdapter.notifyDataSetChanged()
        }
        if (addLeadRequest.Data?.addLeadVehicleDetails?.KMs != null) {
            kmsDrivenAdapter.dataListFilter?.forEachIndexed { index, dataSelectionDTO ->
                dataSelectionDTO.selected =
                        addLeadRequest.Data?.addLeadVehicleDetails?.KMs?.equals(dataSelectionDTO.value) == true
            }
            kmsDrivenAdapter.notifyDataSetChanged()
        }
        if (addLeadRequest.Data?.addLeadVehicleDetails?.FuelType != null) {
            fuleDetailsAdapter.dataListFilter?.forEachIndexed { index, dataSelectionDTO ->
                dataSelectionDTO.selected =
                        addLeadRequest.Data?.addLeadVehicleDetails?.FuelType?.equals(dataSelectionDTO.value) == true
            }
            fuleDetailsAdapter.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_price -> {
                etPrice.requestFocus()
                showKeyBoardByForced()
            }
            R.id.iv_veh_details_make_back -> {
                activity?.onBackPressed()
                showKeyBoardByForced()
            }
            R.id.ll_add_vehicle_number -> {
                etVehicleNumber.requestFocus()
            }

            R.id.btn_next -> {
                hideSoftKeyboard()
                if (hasConnectivityNetwork()) {

                    if (addLeadRequest.Data?.addLeadVehicleDetails?.VehicleNumber == null && llVehicleNumber.visibility == View.GONE
                    ) {
                        llVehicleNumber.visibility = View.VISIBLE
                        scrollToBottom(llVehicleNumber)
                    } else if (addLeadRequest.Data?.addLeadVehicleDetails?.VehicleNumber == null && llVehicleNumber.visibility == View.VISIBLE
                    ) {
                        llAddVehicleNumber.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etVehicleNumber.setTextColor(resources.getColor(R.color.error_red))
                        tvVehicleNumberErrorMessage.visibility = View.VISIBLE
                        tvVehicleNumberErrorMessage.text = ("Please enter vehicle registration No.")

                    } else if (addLeadRequest.Data?.addLeadVehicleDetails?.VehicleNumber != null && !isValidVehicleRegNo(
                                    addLeadRequest.Data?.addLeadVehicleDetails?.VehicleNumber!!)
                    ) {

                        llAddVehicleNumber.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etVehicleNumber.setTextColor(resources.getColor(R.color.error_red))
                        tvVehicleNumberErrorMessage.visibility = View.VISIBLE
                        tvVehicleNumberErrorMessage.text =
                                ("Please enter valid vehicle registration No.")
                    } else if (addLeadRequest.Data?.addLeadVehicleDetails?.VehicleNumber != null && (isValidVehicleRegNo(
                                    addLeadRequest.Data?.addLeadVehicleDetails?.VehicleNumber!!)) && !llVehiclePrice.isVisible &&
                            addLeadRequest.Data?.addLeadVehicleDetails?.VehicleSellingPrice == null) {
                        llVehiclePrice.visibility = View.VISIBLE
                        scrollToBottom(llVehiclePrice)
                    } else if (llVehiclePrice.visibility == View.VISIBLE && etPrice.text.isEmpty()) {
                        llPrice.setBackgroundResource(R.drawable.v2_error_input_bg)
                        etPrice.setTextColor(resources.getColor(R.color.error_red))
                        tvVehiclePriceErrorMessage.visibility = View.VISIBLE
                        tvVehiclePriceErrorMessage.text = ("Please enter price details.")
                    } else {
                        ibbMasterViewModel.getIBBPrice(getIBBPriceRequest(), Global.customerDetails_BaseURL + CommonStrings.IBB_PRICE_END_POINT)
                    }
                }

            }


        }
    }


}