package v2.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.mfc.autofin.framework.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit_config.RetroBase
import utility.CommonStrings
import utility.Global
import v2.model.dto.DataSelectionDTO
import v2.model.request.*
import v2.model.request.CurrentAddress
import v2.model.request.PermanentAddress
import v2.model.response.*
import v2.model.response.master.APIDropDownResponse
import v2.model.response.master.Details
import v2.model.response.master.KYCDocumentResponse
import v2.model.response.master.PinCodeResponse
import v2.model_view.MasterViewModel
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.AdditionalFieldsAdapter
import v2.view.adapter.DataRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.AdditionalFieldsDetailsInterface
import v2.view.callBackInterface.DatePickerCallBack
import v2.view.callBackInterface.itemClickCallBack
import v2.view.utility_view.GridItemDecoration

public class AddressAndAdditionalFieldsFragment : BaseFragment(), View.OnClickListener {

    lateinit var linearLayoutAddNewCurrentAddress: LinearLayout
    lateinit var linearLayoutEditCurrentAddress: LinearLayout
    lateinit var linearLayoutAddNewPermanentAddress: LinearLayout
    lateinit var linearLayoutEditPermanentAddress: LinearLayout
    lateinit var linearLayoutAdditionalFieldsUILayout: LinearLayout
    lateinit var scrollViewPostOffer: ScrollView
    lateinit var checkboxCurrentAndPermanentAddress: CheckBox
    lateinit var imageViewEditCurrentAddress: ImageView
    lateinit var imageViewEditPermanentAddress: ImageView

    lateinit var textViewSelectBankLabel: TextView
    lateinit var textViewCurrentAddress1: TextView
    lateinit var textViewCurrentAddress2: TextView
    lateinit var textViewCurrentAddress3: TextView
    lateinit var textViewPermanentAddressEdit: TextView
    lateinit var textViewPermanentAddress1: TextView
    lateinit var textViewPermanentAddress2: TextView
    lateinit var textViewPermanentAddress3: TextView
    private var additionaFieldPinCode: String = ""

    lateinit var currentAddress: CurrentAddress
    lateinit var permanentAddress: PermanentAddress
    lateinit var additionalFieldsData: AdditionalFieldsData
    lateinit var additionalFieldAdapter: DataRecyclerViewAdapter
    var sectionMap = HashMap<String, ArrayList<FieldDetails>>()
    private val currentFilledFieldData = HashMap<String, FieldDetails>()
    private val submitAdditionalFieldsList = HashMap<String, FieldDetails>()

    lateinit var fragView: View

    var checkList = ArrayList<Details>()

    var address = ""
    var pincode = ""
    var typeOfAddress = ""
    var state = ""
    var city = ""
    var address1 = ""
    var address2 = ""
    var address3 = ""
    var isPermanentAddress = true
    var cityMovedInYear: String = ""
    private var caseID: String = ""
    var delay: Long = 1000 // 1 seconds after user stops typing

    var last_text_edit: Long = 0


    lateinit var pinCodeViewModel: MasterViewModel
    lateinit var addressViewModel: TransactionViewModel
    lateinit var additionalFieldsViewModel: TransactionViewModel
    lateinit var additionalFieldsAPIViewModel: MasterViewModel
    lateinit var submitAdditionalFieldsViewModel: TransactionViewModel
    lateinit var kycDocumentViewModel: MasterViewModel
    lateinit var customerDetailsResponse: CustomerDetailsResponse

    var handler: Handler = Handler()

    private var list = ArrayList<DataSelectionDTO>()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val safeArgs = AddressAndAdditionalFieldsFragmentArgs.fromBundle(it)
            customerId = safeArgs.customerID.toString()
            customerDetailsResponse = safeArgs.customerDetailsResponse

        }

        pinCodeViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)

        addressViewModel = ViewModelProvider(this).get(
                TransactionViewModel::class.java
        )
        additionalFieldsViewModel = ViewModelProvider(this).get(
                TransactionViewModel::class.java
        )

        additionalFieldsAPIViewModel = ViewModelProvider(this).get(
                MasterViewModel::class.java
        )

        submitAdditionalFieldsViewModel = ViewModelProvider(this).get(
                TransactionViewModel::class.java
        )

        kycDocumentViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)

        pinCodeViewModel.getPinCodeDataLiveData().observe(this) { mApiResponse: ApiResponse? ->
            onPinCodeResponse(mApiResponse!!)
        }

        addressViewModel.getUpdateAddressLiveData()
                .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                    onUpdateResponse(
                            mApiResponse!!
                    )
                }
        addressViewModel.getCustomerDetailsLiveData()
                .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                    onCustomerDetails(
                            mApiResponse!!
                    )
                })

        additionalFieldsViewModel.getAdditionalFieldsLiveData().observe(requireActivity()) { mApiResponse: ApiResponse? ->
            onAdditionalFieldsResponse(
                    mApiResponse!!
            )
        }

        additionalFieldsAPIViewModel.getAdditionalFieldAPILiveData().observe(requireActivity()) { mApiResponse: ApiResponse? ->
            onAdditionalFieldAPIResponse(
                    mApiResponse!!
            )
        }


        submitAdditionalFieldsViewModel.mSubmitAdditionalFieldsLiveData().observe(requireActivity()) { mApiResponse: ApiResponse? ->
            onSubmitOfAdditionFields(
                    mApiResponse!!
            )
        }

        kycDocumentViewModel.getKYCDocumentLiveData().observe(requireActivity()) { mApiResponse: ApiResponse? ->
            onGetKYCDocumentResponse(mApiResponse!!)
        }

        addressViewModel.getCustomerDetails(
                createCustomerDetailsRequest(customerId.toInt()),
                Global.customerAPI_BaseURL + CommonStrings.CUSTOMER_DETAILS_END_URL)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.v2_address_additional_fields_fragment, container, false)
        fragView = view
        initView(view)
        return view
    }

    private fun initView(view: View) {
        scrollViewPostOffer = view.findViewById(R.id.scrollViewPostOffer)
        linearLayoutAddNewCurrentAddress = view.findViewById(R.id.linearLayoutAddNewCurrentAddress)
        linearLayoutEditCurrentAddress = view.findViewById(R.id.linearLayoutEditCurrentAddress)
        linearLayoutAddNewPermanentAddress = view.findViewById(R.id.linearLayoutAddNewPermanentAddress)
        linearLayoutEditPermanentAddress = view.findViewById(R.id.linearLayoutEditPermanentAddress)
        linearLayoutAdditionalFieldsUILayout = view.findViewById(R.id.linearLayoutAdditionalFieldsUILayout)

        imageViewEditCurrentAddress = view.findViewById(R.id.imageViewEditCurrentAddress)
        imageViewEditPermanentAddress = view.findViewById(R.id.imageViewEditPermanentAddress)


        textViewSelectBankLabel = view.findViewById(R.id.textViewSelectBankLabel)
        textViewCurrentAddress1 = view.findViewById(R.id.textViewCurrentAddress1)
        textViewCurrentAddress2 = view.findViewById(R.id.textViewCurrentAddress2)
        textViewCurrentAddress3 = view.findViewById(R.id.textViewCurrentAddress3)
        checkboxCurrentAndPermanentAddress = view.findViewById(R.id.checkboxCurrentAndPermanentAddress)
        textViewPermanentAddressEdit = view.findViewById(R.id.textViewPermanentAddressEdit)
        textViewPermanentAddress1 = view.findViewById(R.id.textViewPermanentAddress1)
        textViewPermanentAddress2 = view.findViewById(R.id.textViewPermanentAddress2)
        textViewPermanentAddress3 = view.findViewById(R.id.textViewPermanentAddress3)


        textViewSelectBankLabel.text = "You have selected " + customerDetailsResponse.data?.loanDetails?.bankName + " bank"

        imageViewEditCurrentAddress.setOnClickListener(this)
        imageViewEditPermanentAddress.setOnClickListener(this)

        initiateView()

    }

    private fun initiateView() {
        if (customerDetailsResponse.data != null) {
            val customerData = customerDetailsResponse.data
            if (customerData?.residentialDetails?.currentAddress?.addressLine1?.isNotEmpty() == true) {
                val currentAddress1 = customerDetailsResponse.data?.residentialDetails?.currentAddress
                showEditCurrentAddress()
            } else {
                showNewCurrentAddress()
            }
        }

    }

    private fun showNewCurrentAddress() {

        val addressView: View = LayoutInflater.from(fragView.context).inflate(R.layout.v2_add_new_address_layout, linearLayoutAddNewCurrentAddress, false)
        val textViewTypeOfAddress = addressView.findViewById<TextView>(R.id.textViewTypeOfAddress)
        val editTextPinCode = addressView.findViewById<EditText>(R.id.editTextPinCode)
        val buttonPinCodeCheck = addressView.findViewById<Button>(R.id.buttonPincodeCheck)
        val textViewState = addressView.findViewById<TextView>(R.id.textViewState)
        val textViewCity = addressView.findViewById<TextView>(R.id.textViewCity)
        val textViewCityMovedInLbl = addressView.findViewById<TextView>(R.id.textViewCityMovedInLbl)
        val linearLayoutCityMovedInYear = addressView.findViewById<LinearLayout>(R.id.linearLayoutCityMovedInYear)
        val editTextCityMovedInYear = addressView.findViewById<EditText>(R.id.editTextCityMovedInYear)

        val editTextAddress1 = addressView.findViewById<EditText>(R.id.editTextAddress1)
        val editTextAddress2 = addressView.findViewById<EditText>(R.id.editTextAddress2)
        val editTextAddress3 = addressView.findViewById<EditText>(R.id.editTextAddress3)
        val checkboxIsPermanentAdd = addressView.findViewById<CheckBox>(R.id.checkboxIsPermanentAdd)

        val buttonSubmitAddress = addressView.findViewById<Button>(R.id.buttonSubmitAddress)

        textViewTypeOfAddress.text = resources.getString(R.string.v2_current_address)
        setFocusOnView(textViewTypeOfAddress)

        typeOfAddress = resources.getString(R.string.v2_current_address)

            checkboxIsPermanentAdd.visibility = View.VISIBLE

        editTextPinCode.setText(pincode)
        textViewState.text = state
        textViewCity.text = city

        linearLayoutCityMovedInYear.setOnClickListener(View.OnClickListener {
            var lastSelectedDate = ""

            callDatePickerDialog(
                    lastSelectedDate,
                    null,
                    getTodayDate(),
                    object : DatePickerCallBack {
                        override fun dateSelected(dateDisplayValue: String, dateValue: String) {
                            editTextCityMovedInYear.setText(dateDisplayValue)
                            cityMovedInYear = dateValue
                        }
                    })
        })

        checkboxIsPermanentAdd.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkboxIsPermanentAdd.isChecked = isChecked
                isPermanentAddress = true
            } else {
                checkboxIsPermanentAdd.isChecked = isChecked
                isPermanentAddress = false

            }

        }
        buttonPinCodeCheck.setOnClickListener(View.OnClickListener {
            if (editTextPinCode.text.toString().isNotEmpty() && editTextPinCode.text.toString().length == 6) {
                pinCodeViewModel.getPinCodeData(Global.customerDetails_BaseURL + "Pincode/city/" + editTextPinCode.text.toString())
            } else
                showToast("Please enter valid PinCode")
        })

        buttonSubmitAddress.setOnClickListener(View.OnClickListener {
            if (editTextPinCode.text.toString().isNotEmpty() &&
                    textViewState.text.toString().isNotEmpty() &&
                    textViewCity.text.toString().isNotEmpty() &&
                    editTextAddress1.text.toString().isNotEmpty() &&
                    editTextAddress2.text.toString().isNotEmpty() &&
                    editTextAddress3.text.toString().isNotEmpty()) {

                if (linearLayoutCityMovedInYear.visibility == View.VISIBLE && cityMovedInYear.isEmpty()) {
                    showToast("Please select city moved in year")
                } else {
                    address1 = editTextAddress1.text.toString()
                    address2 = editTextAddress2.text.toString()
                    address3 = editTextAddress3.text.toString()
                    address = "$address1***$address2***$address3"
                    submitCurrentAddress()
                }} else {
                showToast("Please enter all Fields")
            }
        })

        linearLayoutAddNewCurrentAddress.addView(addressView)
    }

    private fun showEditCurrentAddress() {
        if(linearLayoutAddNewCurrentAddress.visibility==View.VISIBLE)
            linearLayoutAddNewCurrentAddress.visibility=View.GONE

        linearLayoutEditCurrentAddress.visibility = View.VISIBLE
        textViewCurrentAddress1.text = address1
        textViewCurrentAddress2.text = address2
        textViewCurrentAddress3.text = address3 + ", " + pincode

        if (isPermanentAddress) {
            checkboxCurrentAndPermanentAddress.visibility = View.VISIBLE
            checkboxCurrentAndPermanentAddress.isChecked = true
            checkboxCurrentAndPermanentAddress.isClickable = false
            checkboxCurrentAndPermanentAddress.isFocusable = false

            if (linearLayoutAddNewPermanentAddress.visibility == View.VISIBLE)
                linearLayoutAddNewPermanentAddress.visibility = View.GONE

            if (linearLayoutEditPermanentAddress.visibility == View.VISIBLE)
                linearLayoutEditPermanentAddress.visibility = View.GONE

            initiateAdditionalFields()
        } else {
            checkboxCurrentAndPermanentAddress.visibility = View.GONE

            if (customerDetailsResponse.data?.residentialDetails?.permanentAddress?.addressLine1?.isNotEmpty() == true || permanentAddress.Pincode.isNotEmpty())
            {
                if(linearLayoutAddNewPermanentAddress.visibility==View.VISIBLE)
                    linearLayoutAddNewPermanentAddress.visibility=View.GONE

                showEditPermanentAddress()
            } else {
                showNewPermanentAddress()
            }
        }

    }

    private fun initiateAdditionalFields() {

        if (linearLayoutAdditionalFieldsUILayout.visibility != View.VISIBLE) {
            additionalFieldsViewModel.getAdditionalFieldsData(CustomerRequest(ResetCustomerJourneyDataRequest(customerId), CommonStrings.USER_TYPE, CommonStrings.USER_TYPE), Global.baseURL + CommonStrings.ADDITIONAL_FIELDS_URL)
        }
    }

    private fun showNewPermanentAddress() {

        val addressView: View = LayoutInflater.from(fragView.context).inflate(R.layout.v2_add_new_address_layout, linearLayoutAddNewPermanentAddress, false)
        val textViewTypeOfAddress = addressView.findViewById<TextView>(R.id.textViewTypeOfAddress)
        val editTextPinCode = addressView.findViewById<EditText>(R.id.editTextPinCode)
        val buttonPinCodeCheck = addressView.findViewById<Button>(R.id.buttonPincodeCheck)
        val textViewState = addressView.findViewById<TextView>(R.id.textViewState)
        val textViewCity = addressView.findViewById<TextView>(R.id.textViewCity)
        val textViewCityMovedInLbl = addressView.findViewById<TextView>(R.id.textViewCityMovedInLbl)
        val linearLayoutCityMovedInYear = addressView.findViewById<LinearLayout>(R.id.linearLayoutCityMovedInYear)

        val editTextAddress1 = addressView.findViewById<EditText>(R.id.editTextAddress1)
        val editTextAddress2 = addressView.findViewById<EditText>(R.id.editTextAddress2)
        val editTextAddress3 = addressView.findViewById<EditText>(R.id.editTextAddress3)
        val checkboxIsPermanentAdd = addressView.findViewById<CheckBox>(R.id.checkboxIsPermanentAdd)

        val buttonSubmitAddress = addressView.findViewById<Button>(R.id.buttonSubmitAddress)

        textViewTypeOfAddress.text = resources.getString(R.string.v2_permanent_address)
        setFocusOnView(textViewTypeOfAddress)

        typeOfAddress = resources.getString(R.string.v2_permanent_address)

        checkboxIsPermanentAdd.visibility = View.GONE
        linearLayoutCityMovedInYear.visibility = View.GONE
        textViewCityMovedInLbl.visibility = View.GONE

        editTextPinCode.setText(pincode)
        textViewState.text = state
        textViewCity.text = city

        buttonPinCodeCheck.setOnClickListener(View.OnClickListener {
            if (editTextPinCode.text.toString().isNotEmpty() && editTextPinCode.text.toString().length == 6) {
                pinCodeViewModel.getPinCodeData(Global.customerDetails_BaseURL + "Pincode/city/" + editTextPinCode.text.toString())
            } else
                showToast("Please enter valid PinCode")
        })

        buttonSubmitAddress.setOnClickListener(View.OnClickListener {
            if (editTextPinCode.text.toString().isNotEmpty() &&
                    textViewState.text.toString().isNotEmpty() &&
                    textViewCity.text.toString().isNotEmpty() &&
                    editTextAddress1.text.toString().isNotEmpty() &&
                    editTextAddress2.text.toString().isNotEmpty() &&
                    editTextAddress3.text.toString().isNotEmpty()) {

                    address1 = editTextAddress1.text.toString()
                    address2 = editTextAddress2.text.toString()
                    address3 = editTextAddress3.text.toString()
                    address = "$address1***$address2***$address3"

                        submitPermanentAddress()

            } else {
                showToast("Please enter all Fields")
            }
        })

        linearLayoutAddNewPermanentAddress.addView(addressView)
    }

    private fun showEditPermanentAddress() {
            textViewPermanentAddress1.text = address1
            textViewPermanentAddress2.text = address2
            textViewPermanentAddress3.text = "$address3, $pincode"

    }

    //region AddressFunctions

    private fun submitCurrentAddress() {

        currentAddress = CurrentAddress(isPermanentAddress, pincode, address)

        if (isPermanentAddress) {

            permanentAddress = PermanentAddress(pincode, address)
            val addressData = AddressData(customerId.toInt(), currentAddress, permanentAddress, cityMovedInYear)
            val updateAddressRequest = UpdateAddressRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, addressData)

            addressViewModel.updateAddress(updateAddressRequest, Global.customerAPI_BaseURL + CommonStrings.UPDATE_ADDRESS_URL)

        }
        showEditCurrentAddress()
    }

    private fun submitPermanentAddress() {

        permanentAddress = PermanentAddress(pincode, address)
        val addressData = AddressData(customerId.toInt(), currentAddress, permanentAddress, cityMovedInYear)
        val updateAddressRequest = UpdateAddressRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, addressData)

        addressViewModel.updateAddress(updateAddressRequest, Global.customerAPI_BaseURL + CommonStrings.UPDATE_ADDRESS_URL)
    }

    //endregion AddressFunctions


    // region OnResponse

    private fun onCustomerDetails(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val customerResponse: CustomerDetailsResponse? =
                        mApiResponse.data as CustomerDetailsResponse?
                if (customerResponse != null) {
                    customerDetailsResponse = customerResponse
                }

                // InitViews
            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
            }
            else -> {
                hideProgressDialog()
                showToast("Please enter valid details")
            }
        }
    }

    private fun onPinCodeResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val pinCodeResponse: PinCodeResponse? = mApiResponse.data as PinCodeResponse?
                if (pinCodeResponse?.statusCode == "100") {
                    val pinCodeData = pinCodeResponse.data
                    pincode = pinCodeData.pincode
                    state = pinCodeData.state
                    city = pinCodeData.city


                    if (typeOfAddress == getString(R.string.v2_current_address)) {
                        linearLayoutAddNewCurrentAddress.removeAllViews()
                        showNewCurrentAddress()
                    } else if (typeOfAddress == getString(R.string.v2_permanent_address)) {
                        linearLayoutAddNewPermanentAddress.removeAllViews()
                        showNewPermanentAddress()
                    }

                } else
                    showToast(pinCodeResponse?.message.toString())
            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                showToast("Error")
            }
            else -> {
            }
        }
    }

    private fun onUpdateResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val response: SimpleResponse? = mApiResponse.data as SimpleResponse?

                if (typeOfAddress == getString(R.string.v2_current_address)) {
                    showEditCurrentAddress()
                } else if (typeOfAddress == getString(R.string.v2_permanent_address)) {
                 showEditPermanentAddress()
                }

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
            }
            else -> {
            }
        }
    }


    private fun onAdditionalFieldsResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val response: AdditionalFields? = mApiResponse.data as AdditionalFields?
                if (response?.data != null && response.data.sections.isNotEmpty()) {

                    linearLayoutAddNewPermanentAddress.removeAllViews()
                    linearLayoutAddNewPermanentAddress.visibility = View.GONE
                    linearLayoutEditCurrentAddress.visibility = View.VISIBLE

                    if (!isPermanentAddress)
                        linearLayoutEditPermanentAddress.visibility = View.VISIBLE

                    additionalFieldsData = response.data
                    linearLayoutAdditionalFieldsUILayout.visibility = View.VISIBLE
                    setAdditionalField()

                } else {
                    navigateToBankOfferStatus(customerId, customerDetailsResponse, "SoftOffer")
                }
            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
            }
            else -> {
            }
        }
    }


    private fun onAdditionalFieldAPIResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val additionalFieldRes: APIDropDownResponse? = mApiResponse.data as APIDropDownResponse?
                checkList = additionalFieldRes?.data?.details!! as ArrayList<Details>
                getDTOList()
                refreshFieldView()

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                Log.i("SoftOfferFragment", "onBankResponse: " + ApiResponse.Status.ERROR)

            }
            else -> {
                Log.i("SoftOfferFragment", "onBankResponse: ")
            }
        }

    }


    private fun onSubmitOfAdditionFields(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val submitAdditionalFieldRes: CommonResponse = mApiResponse.data as CommonResponse
                if (submitAdditionalFieldRes.statusCode == "100") {
                    kycDocumentViewModel.getKYCDocumentResponse(Global.baseURL + CommonStrings.KYC_UPLOAD_URL_END_POINT + customerId)
                } else {
                    if (submitAdditionalFieldRes.message != null)

                        showToast(submitAdditionalFieldRes.message)

                }
            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                Log.i("SoftOfferFragment", "onBankResponse: " + ApiResponse.Status.ERROR)

            }
            else -> {
                Log.i("SoftOfferFragment", "onBankResponse: ")
            }
        }
    }

    private fun onGetKYCDocumentResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val kycDocumentRes: KYCDocumentResponse = mApiResponse.data as KYCDocumentResponse
                if (kycDocumentRes.statusCode == "100") {
                    if (kycDocumentRes.data.groupedDoc.isNotEmpty() || kycDocumentRes.data.nonGroupedDoc.isNotEmpty())
                        navigateToKYCDocumentUpload(customerId, kycDocumentRes, caseID, customerDetailsResponse)
                    else if (kycDocumentRes.data.groupedDoc.isEmpty() && kycDocumentRes.data.nonGroupedDoc.isEmpty())
                        navigateToBankOfferStatus(customerId, customerDetailsResponse, "AddressAdditionalFields")
                } else {
                    navigateToBankOfferStatus(customerId, customerDetailsResponse, "AddressAdditionalFields")
                }

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


    // endregion OnResponse

    private fun setAdditionalField() {
        val sectionsList = additionalFieldsData.sections
        for (sectionIndex in sectionsList.indices) {
            val sectionData = sectionsList[sectionIndex]
            val fieldsList = sectionsList[sectionIndex].fields
            val sectionView = generateSectionUI(sectionData, sectionIndex == sectionsList.size - 1)
            linearLayoutAdditionalFieldsUILayout.addView(sectionView)

            if (sectionMap.size >= sectionIndex + 1)//|| sectionDataList.size == fieldDetails)
            {
                continue
            } else {
                break
            }
        }

    }

    private fun generateSectionUI(sectionData: Sections, isLastSection: Boolean): View {
        val fieldList = sectionData.fields
        var currentSectionLayout = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_address_parent_layout, linearLayoutAdditionalFieldsUILayout, false)
        if (isSectionPreFilled(sectionData.sectionName)) {
            currentSectionLayout = generateEditSectionUI(sectionData)
        } else {

            if (sectionData.type == "Address" || isLastSection) {

                // if section is not

                currentSectionLayout = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_address_parent_layout, linearLayoutAdditionalFieldsUILayout, false)

                val linearLayout = currentSectionLayout.findViewById<LinearLayout>(R.id.linearLayoutCustomAddressSectionLayout)

                val addressButton = currentSectionLayout.findViewById<Button>(R.id.buttonSubmitAddressDetails)

                if (sectionData.displayName) {
                    val currentSectionTitle: View = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_title_text_view, linearLayout, false)
                    val sectionTitle: TextView = currentSectionTitle.findViewById(R.id.textViewTitleLabel)
                    sectionTitle.text = sectionData.sectionName
                    linearLayout.addView(currentSectionTitle)
                }

                if (currentFilledFieldData.size == fieldList.size) {
                    addressButton.setBackgroundResource(R.drawable.vtwo_next_btn_bg)
                }
                addressButton.setOnClickListener(View.OnClickListener {
                    if (currentFilledFieldData.size == fieldList.size) {
                        if (sectionData.type == "Address" && !isLastSection) {
                            moveCurrentDetailsToMap(sectionData.sectionName)
                        } else {
                            submitAdditionalFieldsList.putAll(currentFilledFieldData)
                            val fieldList: ArrayList<FieldDetails> = ArrayList<FieldDetails>(submitAdditionalFieldsList.values)
                            sectionMap[sectionData.sectionName] = fieldList
                            submitAdditionalFields()
                        }
                    } else {
                        Log.i("SoftOffer", "generateSectionUI: " + "" + currentFilledFieldData.size + "=====>" + fieldList.size)
                        showToast("Please fill all fields")
                    }
                })

                generateFieldUI(sectionData.sectionName, linearLayout, fieldList, true)

            } else {
                currentSectionLayout = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_parent_layout, linearLayoutAdditionalFieldsUILayout, false)
                val linearLayout = currentSectionLayout.findViewById<LinearLayout>(R.id.linearLayoutCustomParentLayout)
                if (sectionData.displayName) {
                    val currentSectionTitle: View = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_title_text_view, linearLayout, false)
                    val sectionTitle: TextView = currentSectionTitle.findViewById(R.id.textViewTitleLabel)
                    sectionTitle.text = sectionData.sectionName
                    linearLayout.addView(currentSectionTitle)
                }
                generateFieldUI(sectionData.sectionName, linearLayout, fieldList, false)
            }
        }
        return currentSectionLayout
    }


    private fun generateFieldUI(sectionName: String, linearLayout: LinearLayout?, fieldList: List<Fields>, isLastSection: Boolean) {

        val cFieldList = fieldList

        for (fieldIndex in fieldList.indices) {
            if (fieldList[fieldIndex].displayLabel) {
                val fieldTitleText = fieldList[fieldIndex].label
                val isMandatoryField = fieldList[fieldIndex].isMandatory
                val titleView: View = getTitleView(fieldTitleText, isMandatoryField)
                linearLayout?.addView(titleView)
            }

            val fieldVal: Fields = fieldList[fieldIndex]
            val fieldView: View? = linearLayout?.let { getFieldView(sectionName, fieldVal, cFieldList, fieldIndex == fieldList.size - 1, isLastSection, it) }

            linearLayout?.addView(fieldView)

            if (fieldList[fieldIndex].apiDetails.apiKey == "CompanyPincode" && isFieldFilled(fieldList[fieldIndex].apiDetails.apiKey).isEmpty())
                break
            else
                continue

        }

    }

    private fun getTitleView(title: String, isMandatory: Boolean): View {
        val currentFieldViewTitle: View = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_title_text_view, null, true)
        val fieldTitle: TextView = currentFieldViewTitle.findViewById(R.id.textViewTitleLabel)

        if (isMandatory) {
            val text = "$title "
            val colored = getString(R.string.lbl_asterick)
            val builder = SpannableStringBuilder()

            builder.append(text)
            val start = builder.length
            builder.append(colored)
            val end = builder.length

            builder.setSpan(ForegroundColorSpan(Color.RED), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            fieldTitle.text = builder
        } else {
            fieldTitle.text = title
        }
        return currentFieldViewTitle
    }

    private fun getFieldView(sectionName: String, fieldData: Fields, cFieldList: List<Fields>, isLastItem: Boolean, isLastSection: Boolean, linearLayout: LinearLayout): View {
        var currentFieldInputView = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_edit_text, linearLayout, false)

        when (fieldData.fieldType) {
            "Text" -> {
                currentFieldInputView = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_edit_text, linearLayout, false)
                val fieldInputValue: EditText = currentFieldInputView.findViewById(R.id.editTextFieldInput)
                fieldInputValue.hint = fieldData.placeHolder

                // prefill
                fieldInputValue.setText(isFieldFilled(fieldData.apiDetails.apiKey))



                fieldInputValue.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        if (!TextUtils.isEmpty(s.toString())) {

                            last_text_edit = System.currentTimeMillis()
                            handler.removeCallbacksAndMessages(null)
                            handler.postDelayed(input_finish_checker, delay)
                        } else {
                            showToast("Please enter Value")
                        }
                    }

                    val input_finish_checker = Runnable {
                        if (System.currentTimeMillis() > last_text_edit + delay - 1000) {
                            updateEditTextValues(fieldInputValue, fieldData, sectionName, isLastItem, linearLayout, cFieldList, isLastSection)
                            /* if (fieldData.apiDetails.apiKey == "CompanyPincode") {

                                 val editTextString: String = fieldInputValue.text.toString()
                                 if (editTextString.length == 6) {
                                     additionaFieldPinCode = editTextString
                                     validateInput(sectionName, fieldData.apiDetails.apiKey, editTextString, isLastItem, fieldData.regexValidation, fieldData.apiDetails.apiKey, editTextString)
                                     refreshFieldView(sectionName, linearLayout, cFieldList, isLastSection)
                                 } else {
                                     showToast("Enter valid Pincode")
                                 }

                             } else if (sectionName == "Address" && isLastItem) {

                                 val editTextString: String = fieldInputValue.text.toString()
                                 val currentFieldDetails = FieldDetails(fieldData.apiDetails.apiKey, editTextString, editTextString)
                                 addToCurrentFilledFieldData(fieldData.apiDetails.apiKey, currentFieldDetails, false, sectionName)

                             } else {
                                 val editTextString: String = fieldInputValue.text.toString()
                                 validateInput(sectionName, fieldData.apiDetails.apiKey, editTextString, isLastItem, fieldData.regexValidation, fieldData.apiDetails.apiKey, editTextString)
                             }
*/
                        }
                    }

                })

                // getVal
                fieldInputValue.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                    if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || event != null && event.keyCode == KeyEvent.KEYCODE_BACK ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            actionId == EditorInfo.IME_ACTION_NEXT) {

                        updateEditTextValues(fieldInputValue, fieldData, sectionName, isLastItem, linearLayout, cFieldList, isLastSection)
                    }
                    false
                })

                fieldInputValue.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        updateEditTextValues(fieldInputValue, fieldData, sectionName, isLastItem, linearLayout, cFieldList, isLastSection)
                    }
                }


            }
            "DropDown" -> {
                currentFieldInputView = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_input_text_view, linearLayout, false)
                val fieldInput: TextView = currentFieldInputView.findViewById(R.id.textViewDropDown)
                fieldInput.hint = fieldData.placeHolder
                fieldInput.text = isFieldFilled(fieldData.apiDetails.apiKey)

                if (fieldData.apiDetails.apiKey == "CompanyState" || fieldData.apiDetails.apiKey == "CompanyCity") {
                    setStateOrCityValue(sectionName, fieldData.apiDetails.apiKey, fieldInput, fieldData.apiDetails.url)
                }

                val apiURL = fieldData.apiDetails.url

                fieldInput.setOnClickListener(View.OnClickListener {
                    RetroBase.retrofitInterface.getFromWeb(apiURL)?.enqueue(object : Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            val strRes = Gson().toJson(response.body())
                            val dpRes = Gson().fromJson(strRes, APIDropDownResponse::class.java)
                            if (dpRes != null && dpRes.status) {
                                val optionList = dpRes.data.details
                                if (optionList.isNotEmpty() && optionList.size == 1) {
                                    if (listOf(optionList).any { true }) {
                                        val details = Details(optionList[0].displayLabel, optionList[0].value)
                                        fieldInput.text = details.displayLabel
                                        if (isLastSection && isLastItem) {
                                            val editTextString: String = details.value
                                            val currentFieldDetails = FieldDetails(fieldData.apiDetails.apiKey, editTextString, editTextString)
                                            addToCurrentFilledFieldData(fieldData.apiDetails.apiKey, currentFieldDetails, false, sectionName)
                                        } else {
                                            validateInput(sectionName, fieldData.apiDetails.apiKey, details.value, isLastItem, "", fieldData.apiDetails.apiKey, details.displayLabel)
                                        }
                                    } else {
                                        showToast("Something went wrong! Please try again!")
                                    }
                                } else if (optionList.isNotEmpty() && optionList.size > 1) {
                                    showDropDownDialog(fieldData.apiDetails.url, fieldData.label, optionList, object : AdditionalFieldsDetailsInterface {
                                        override fun returnDetails(details: Details) {
                                            fieldInput.text = details.displayLabel
                                            if (isLastSection && isLastItem) {
                                                val editTextString: String = details.value

                                                val currentFieldDetails = FieldDetails(fieldData.apiDetails.apiKey, editTextString, editTextString)
                                                addToCurrentFilledFieldData(fieldData.apiDetails.apiKey, currentFieldDetails, false, sectionName)
                                            } else {
                                                validateInput(sectionName, fieldData.apiDetails.apiKey, details.value, isLastItem, "", fieldData.apiDetails.apiKey, details.displayLabel)
                                            }

                                        }
                                    })

                                }

                            } else {
                                showToast("Something went wrong! Please try again!")
                            }
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            t.printStackTrace()
                        }

                    })


                })
            }
            "Check" -> {

                currentFieldInputView = LayoutInflater.from(fragView.context).inflate(R.layout.v2_custom_check_type_layout, linearLayout, false)
                val recyclerView: RecyclerView = currentFieldInputView.findViewById(R.id.recyclerViewAdditionalField)
                RetroBase.retrofitInterface.getFromWeb(fieldData.apiDetails.url)?.enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        val strRes = Gson().toJson(response.body())
                        val dpRes = Gson().fromJson(strRes, APIDropDownResponse::class.java)
                        if (dpRes != null && dpRes.status) {
                            checkList = dpRes.data.details as ArrayList<Details>
                            getDTOList()
                            if (list.isNotEmpty()) {
                                additionalFieldAdapter = DataRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                                    override fun itemClick(item: Any?, position: Int) {

                                        additionalFieldAdapter.dataListFilter!!.forEachIndexed { index, item ->
                                            run {
                                                if (index == position) {
                                                    item.selected = true
                                                    val displayLabel: String = item.displayValue as String
                                                    val value: String = item.value as String

                                                    if (isLastSection && isLastItem) {

                                                        val currentFieldDetails = FieldDetails(fieldData.apiDetails.apiKey, value, displayLabel)
                                                        addToCurrentFilledFieldData(fieldData.apiDetails.apiKey, currentFieldDetails, false, sectionName)
                                                    } else {
                                                        val currentFieldDetails = FieldDetails(fieldData.apiDetails.apiKey, value, displayLabel)
                                                        addToCurrentFilledFieldData(fieldData.apiDetails.apiKey, currentFieldDetails, true, sectionName)
                                                    }

                                                } else {
                                                    item.selected = false
                                                }
                                            }
                                        }
                                        additionalFieldAdapter.notifyDataSetChanged()
                                    }
                                })


                                val layoutManagerStaggeredGridLayoutManager =
                                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                                val layoutManagerGridLayoutManager = GridLayoutManager(activity, 2)

                                recyclerView.addItemDecoration(GridItemDecoration(25, 2))

                                recyclerView.layoutManager = layoutManagerStaggeredGridLayoutManager

                                recyclerView.adapter = additionalFieldAdapter
                            }


                        }
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        t.printStackTrace()
                    }
                })


            }
        }

        return currentFieldInputView
    }

    private fun updateEditTextValues(fieldInputValue: EditText, fieldData: Fields, sectionName: String, isLastItem: Boolean, linearLayout: LinearLayout, cFieldList: List<Fields>, isLastSection: Boolean) {
        if (fieldInputValue.text.isNotEmpty()) {

            if (fieldData.apiDetails.apiKey == "CompanyPincode") {

                val editTextString: String = fieldInputValue.text.toString()
                if (editTextString.length == 6) {
                    additionaFieldPinCode = editTextString
                    validateInput(sectionName, fieldData.apiDetails.apiKey, editTextString, isLastItem, fieldData.regexValidation, fieldData.apiDetails.apiKey, editTextString)
                    refreshFieldView(sectionName, linearLayout, cFieldList, isLastSection)
                } else {
                    showToast("Enter valid Pincode")
                }

            } else if (sectionName == "Address" && isLastItem || isLastSection && isLastItem) {

                val editTextString: String = fieldInputValue.text.toString()
                val currentFieldDetails = FieldDetails(fieldData.apiDetails.apiKey, editTextString, editTextString)
                addToCurrentFilledFieldData(fieldData.apiDetails.apiKey, currentFieldDetails, false, sectionName)

            } else {
                val editTextString: String = fieldInputValue.text.toString()
                validateInput(sectionName, fieldData.apiDetails.apiKey, editTextString, isLastItem, fieldData.regexValidation, fieldData.apiDetails.apiKey, editTextString)
            }

        } else
            showToast("Please fill the field")

    }

    private fun setStateOrCityValue(sectionName: String, apiKey: String, fieldInput: TextView, url: String) {
        val apiURL = url + additionaFieldPinCode
        var textVal = ""
        RetroBase.retrofitInterface.getFromWeb(apiURL)?.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                val strRes = Gson().toJson(response.body())
                val dpRes = Gson().fromJson(strRes, APIDropDownResponse::class.java)
                if (dpRes != null && dpRes.status) {
                    val optionList = dpRes.data.details
                    if (optionList.isNotEmpty() && optionList.size == 1) {
                        if (listOf(optionList).any { true }) {
                            val details = Details(optionList[0].displayLabel, optionList[0].value)
                            fieldInput.text = details.displayLabel
                            textVal = fieldInput.text.toString()
                            val currentFieldDetails = FieldDetails(apiKey, details.value, details.displayLabel)
                            addToCurrentFilledFieldData(apiKey, currentFieldDetails, false, sectionName)
                        } else {
                            showToast("Something went wrong! Please try again!")
                        }
                    } else {
                        showToast("Something went wrong! Please try again!")
                    }
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun validateInput(sectionName: String, fieldName: String, editTextVal: String, lastItem: Boolean, regexResponse: String?, apiKey: String, displayKey: String) {
        if (editTextVal.isNotEmpty()) {

            if (regexResponse != null && regexResponse.isNotEmpty()) {
                val regex = Regex(regexResponse)
                if (regex.matches(editTextVal)) {
                    val currentFieldDetails = FieldDetails(apiKey, editTextVal, displayKey)
                    if (lastItem)
                        addToCurrentFilledFieldData(fieldName, currentFieldDetails, true, sectionName)
                    else
                        addToCurrentFilledFieldData(fieldName, currentFieldDetails, false, sectionName)

                } else {
                    showToast("Please enter valid $fieldName")
                }

            } else {
                val currentFieldDetails = FieldDetails(apiKey, editTextVal, displayKey)
                if (lastItem)
                    addToCurrentFilledFieldData(fieldName, currentFieldDetails, true, sectionName)
                else
                    addToCurrentFilledFieldData(fieldName, currentFieldDetails, false, sectionName)

            }
        } else {
            showToast("Please Fill the Field")
        }


        /*if (lastItem) {
            if (regexResponse?.contains('{') == true) {
                val regex = Regex(regexResponse)
                if (regex.matches(editTextVal)) {
                    val currentFieldDetails = FieldDetails(apiKey, editTextVal, displayKey)
                    addToCurrentFilledFieldData(fieldName, currentFieldDetails, true, sectionName)
                    //  moveCurrentDetailsToMap(sectionName)
                } else {
                    showToast("Please enter valid Input")
                }
            } else {
                val currentFieldDetails = FieldDetails(apiKey, editTextVal, displayKey)
                addToCurrentFilledFieldData(fieldName, currentFieldDetails, true, sectionName)
                //  moveCurrentDetailsToMap(sectionName)
                //refreshFieldView()
            }
        } else {
            val currentFieldDetails = FieldDetails(apiKey, editTextVal, displayKey)
            addToCurrentFilledFieldData(fieldName, currentFieldDetails, false, sectionName)
        }
*/

    }

    private fun addToCurrentFilledFieldData(fieldName: String, currentFieldDetails: FieldDetails, isLastItem: Boolean, sectionName: String) {
        currentFilledFieldData[fieldName] = currentFieldDetails
        Log.i("TAG", "addToCurrentFilledFieldData: " + currentFilledFieldData[fieldName]?.Value.toString())
        if (isLastItem)
            moveCurrentDetailsToMap(sectionName)
    }

    private fun moveCurrentDetailsToMap(sectionName: String) {

        submitAdditionalFieldsList.putAll(currentFilledFieldData)
        val fieldList: ArrayList<FieldDetails> = ArrayList<FieldDetails>(submitAdditionalFieldsList.values)

        sectionMap[sectionName] = fieldList
        currentFilledFieldData.clear()
        refreshFieldView()
    }

    private fun isSectionPreFilled(sectionName: String): Boolean {
        var isSectionFilled = false
        for ((key, value) in sectionMap) {
            if (key == sectionName) {
                if (listOf(value).any { it.isNotEmpty() }) {
                    isSectionFilled = true
                } else {
                    showToast("SomeFields are empty")
                }

            }
        }
        return isSectionFilled
    }


    private fun isFieldFilled(fieldName: String): String {
        var value = ""
        value = if (currentFilledFieldData.isNotEmpty() && currentFilledFieldData.containsKey(fieldName)) {
            val fieldData = currentFilledFieldData.getValue(fieldName)
            fieldData.DisplayLabel
        } else {
            ""
        }
        return value
    }

    private fun isFieldFilled1(fieldName: String): String {
        var value = ""
        value = if (submitAdditionalFieldsList.containsKey(fieldName)) {
            val fieldData = submitAdditionalFieldsList.getValue(fieldName)
            fieldData.DisplayLabel
        } else {
            ""
        }
        return value
    }

    private fun generateEditSectionUI(sectionData: Sections): View {
        var view: View = LayoutInflater.from(fragView.context).inflate(R.layout.v2_edit_custom_address, linearLayoutAdditionalFieldsUILayout, false)

        when (sectionData.type) {
            "Address" -> {
                view = LayoutInflater.from(fragView.context).inflate(R.layout.v2_edit_custom_address, linearLayoutAdditionalFieldsUILayout, false)
                val textViewOfficeAddressEdit = view.findViewById<TextView>(R.id.textViewOfficeAddressEdit)
                val imageViewEditOfficeAddress = view.findViewById<ImageView>(R.id.imageViewEditOfficeAddress)
                val textViewOfficeAddress1 = view.findViewById<TextView>(R.id.textViewOfficeAddress1)
                val textViewOfficeAddress2 = view.findViewById<TextView>(R.id.textViewOfficeAddress2)
                val textViewOfficeAddress3 = view.findViewById<TextView>(R.id.textViewOfficeAddress3)
                textViewOfficeAddressEdit.text = sectionData.sectionName
                textViewOfficeAddress1.text = isFieldFilled1(sectionData.fields[0].apiDetails.apiKey)
                textViewOfficeAddress2.text = isFieldFilled1(sectionData.fields[4].apiDetails.apiKey) + "," + isFieldFilled1(sectionData.fields[5].apiDetails.apiKey)
                textViewOfficeAddress3.text = isFieldFilled1(sectionData.fields[6].apiDetails.apiKey) + "," + isFieldFilled1(sectionData.fields[1].apiDetails.apiKey)
                imageViewEditOfficeAddress.setOnClickListener(View.OnClickListener {
                    if (sectionData.fields[1].apiDetails.apiKey == "CompanyPincode")
                        additionaFieldPinCode = ""

                    sectionMap.remove(sectionData.sectionName)
                    refreshFieldView()
                })
            }
            "Standard" -> {
                view = LayoutInflater.from(fragView.context).inflate(R.layout.v2_edit_dropdown_layout, linearLayoutAdditionalFieldsUILayout, false)
                val titleText = view.findViewById<TextView>(R.id.textViewLbl)
                val imageViewEdit: ImageView = view.findViewById(R.id.imageViewEditCurrentDropDown)
                val editValText: TextView = view.findViewById(R.id.textViewEditDropDown)
                titleText.text = sectionData.sectionName
                editValText.text = isFieldFilled1(sectionData.fields[0].apiDetails.apiKey)
                imageViewEdit.setOnClickListener(View.OnClickListener {
                    sectionMap.remove(sectionData.sectionName)
                    refreshFieldView()
                })
            }
        }
        return view
    }


    private fun refreshFieldView(sectionName: String, linearLayout: LinearLayout, cFieldList: List<Fields>, isLastItem: Boolean) {
        handler.postDelayed({
            linearLayout.removeAllViews()
            generateFieldUI(sectionName, linearLayout, cFieldList, isLastItem)

        }, 500)

    }

    private fun refreshFieldView() {
        handler.postDelayed({
            linearLayoutAdditionalFieldsUILayout.removeAllViews()
            setAdditionalField()

        }, 500)

    }

    private fun getDTOList() {
        list = arrayListOf<DataSelectionDTO>()
        checkList.forEachIndexed { index, types ->
            list.add(DataSelectionDTO(types.displayLabel, null, types.value, false))
        }
    }


    private fun submitAdditionalFields() {
        val fieldList: ArrayList<FieldDetails> = ArrayList<FieldDetails>(submitAdditionalFieldsList.values)

        val fieldDataRequest = FieldData(customerId.toInt(), fieldList)
        val submitAdditionalFieldsRequest = SubmitAdditionalFieldRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, fieldDataRequest)
        submitAdditionalFieldsViewModel.submitAdditionalFields(submitAdditionalFieldsRequest, Global.customerAPI_BaseURL + "submit-additional-details")
    }


    private fun showDropDownDialog(apiURL: String, title: String, optionList: List<Details>, detailsCallBack: AdditionalFieldsDetailsInterface) {

        val returnDetailsCallBack: AdditionalFieldsDetailsInterface = detailsCallBack
        val dialog = Dialog(fragView.context, R.style.FullScreenDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.v2_additional_fields_drop_down_options_layout)

        val textViewTitle: TextView = dialog.findViewById(R.id.textViewSelectTitle) as TextView
        val editTextAdditionalFieldsSearchOption: EditText = dialog.findViewById(R.id.editTextAdditionalFieldsSearch)
        val backToSoftOffer = dialog.findViewById<ImageView>(R.id.imageViewBackToSoftOffer)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewOptions)
        textViewTitle.text = title

        var valueSet = false
        var details = Details("", "")
        backToSoftOffer.setOnClickListener(View.OnClickListener {
            valueSet = false
            dialog.dismiss()
        })

        editTextAdditionalFieldsSearchOption.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard(editTextAdditionalFieldsSearchOption)
            }
            false
        })

        var reviewAdapter = AdditionalFieldsAdapter(apiURL, optionList, object : AdditionalFieldsDetailsInterface {
            override fun returnDetails(details: Details) {
                returnDetailsCallBack.returnDetails(details)
                dialog.dismiss()
            }
        })

        val layoutManager = LinearLayoutManager(fragView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = reviewAdapter

        editTextAdditionalFieldsSearchOption.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                if (s != "") {
                    handler.removeCallbacks(input_finish_checker)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (!TextUtils.isEmpty(s.toString())) {

                    last_text_edit = System.currentTimeMillis()
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed(input_finish_checker, delay)
                } else {
                    reviewAdapter.updateList(optionList)
                }
            }

            val input_finish_checker = Runnable {
                if (System.currentTimeMillis() > last_text_edit + delay - 500) {
                    showProgressDialog(fragView.context)

                    RetroBase.retrofitInterface.getFromWeb(apiURL + editTextAdditionalFieldsSearchOption.text.toString())?.enqueue(object : Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            val strRes = Gson().toJson(response.body())
                            val dpRes = Gson().fromJson(strRes, APIDropDownResponse::class.java)
                            if (dpRes != null && dpRes.status) {
                                val filteredOptionList: List<Details> = dpRes.data.details
                                if (filteredOptionList.isNotEmpty()) {
                                    hideProgressDialog()
                                    reviewAdapter.updateList(filteredOptionList)
                                } else {
                                    hideProgressDialog()
                                    showToast("No filter found for your query")
                                }
                            }
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                }
            }
        })
        dialog.show()
    }

    private fun getStringList(filteredOptionList: List<Details>): List<String> {
        val stringList = ArrayList<String>()
        for (option in filteredOptionList.indices) {
            stringList.add(filteredOptionList[option].displayLabel)
        }

        return stringList
    }

    private fun createCustomerDetailsRequest(customerId: Int): CustomerRequest {
        var customerDetailsRequest = CustomerRequest()
        customerDetailsRequest.UserId = CommonStrings.DEALER_ID
        customerDetailsRequest.UserType = CommonStrings.USER_TYPE
        var customerJourneyDataRequest = ResetCustomerJourneyDataRequest();
        customerJourneyDataRequest.CustomerId = customerId.toString()
        customerDetailsRequest.Data = customerJourneyDataRequest
        return customerDetailsRequest
    }

    private fun setFocusOnView(textView: TextView) {
        scrollViewPostOffer.post(Runnable { textView.top.let { scrollViewPostOffer.scrollTo(0, it) } })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageViewEditCurrentAddress -> {
                linearLayoutEditCurrentAddress.visibility = View.GONE
                linearLayoutAddNewCurrentAddress.visibility = View.VISIBLE
                linearLayoutAddNewCurrentAddress.removeAllViews()
                showNewCurrentAddress()
            }
            R.id.imageViewEditPermanentAddress -> {
                linearLayoutEditPermanentAddress.visibility = View.GONE
                linearLayoutAddNewPermanentAddress.visibility = View.VISIBLE
                linearLayoutAddNewPermanentAddress.removeAllViews()
                showNewPermanentAddress()
            }

        }
    }


}