package v2.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.contains
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.google.gson.Gson
import com.mfc.autofin.framework.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.v2_add_new_address_layout.*
import kotlinx.android.synthetic.main.v2_address_additional_fields_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
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
import v2.view.callBackInterface.ActivityBackPressed
import v2.view.callBackInterface.AdditionalFieldsDetailsInterface
import v2.view.callBackInterface.DatePickerCallBack
import v2.view.callBackInterface.itemClickCallBack
import v2.view.utility_view.GridItemDecoration
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


public class AddressAndAdditionalFieldsFragment : BaseFragment(), View.OnClickListener,
        KeyboardVisibilityEventListener, ActivityBackPressed {

    private var isEditFlow: Boolean = false
    lateinit var imageViewSelectedBankName: ImageView
    lateinit var linearLayoutAddNewCurrentAddress: LinearLayout
    lateinit var linearLayoutEditCurrentAddress: LinearLayout
    lateinit var linearLayoutAddNewPermanentAddress: LinearLayout
    lateinit var linearLayoutEditPermanentAddress: LinearLayout
    lateinit var linearLayoutAdditionalFieldsUILayout: LinearLayout
    lateinit var scrollViewPostOffer: ScrollView
    lateinit var checkboxCurrentAndPermanentAddress: CheckBox
    lateinit var imageViewEditCurrentAddress: ImageView
    lateinit var imageViewEditPermanentAddress: ImageView
    private lateinit var currentSectionButton: Button

    lateinit var textViewSelectBankLabel: TextView
    lateinit var textViewCurrentAddress1: TextView
    lateinit var textViewCurrentAddress2: TextView
    lateinit var textViewCurrentAddress3: TextView
    lateinit var textViewPermanentAddressEdit: TextView
    lateinit var textViewPermanentAddress1: TextView
    lateinit var textViewPermanentAddress2: TextView
    lateinit var textViewPermanentAddress3: TextView
    lateinit var buttonMoveToNextPage: Button
    private var additionaFieldPinCode: String = ""

    lateinit var currentAddress: CurrentAddress
    lateinit var currentAddressResponse: v2.model.response.CurrentAddress
    lateinit var permanentAddress: PermanentAddress
    var permanentAddressResponse: v2.model.response.PermanentAddress? = null
    lateinit var additionalFieldsData: AdditionalFieldsData
    lateinit var additionalFieldAdapter: DataRecyclerViewAdapter
    var sectionMap = HashMap<String, ArrayList<FieldDetails>>()
    private val currentFilledFieldData = HashMap<String, FieldDetails>()
    private val submitAdditionalFieldsList = HashMap<String, FieldDetails>()
    private val mandatoryFieldsList = HashMap<String, String>()
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
    var isPermanentAddress = false
    var cityMovedInYear: String = ""
    private var caseID: String = ""
    var delay: Long = 1000 // 1 seconds after user stops typing

    var last_text_edit: Long = 0

    lateinit var masterViewModel: MasterViewModel
    lateinit var transactionViewModel: TransactionViewModel
    lateinit var customerDetailsResponse: CustomerDetailsResponse
    lateinit var ivBackFromAddressAndAdditionalFields: ImageView

    var handler: Handler = Handler()

    private var list = ArrayList<DataSelectionDTO>()
    lateinit var viewEmpty: View

    var editTextCurrentAddress1: EditText? = null
    var linearLayoutCurrentAddress1: LinearLayout? = null

    var editTextCurrentAddress2: EditText? = null
    var linearLayoutCurrentAddress2: LinearLayout? = null

    var editTextCurrentAddress3: EditText? = null
    var linearLayoutCurrentAddress3: LinearLayout? = null

    var editTextPermanentAddress1: EditText? = null
    var linearLayoutPermanentAddress1: LinearLayout? = null

    var editTextPermanentAddress2: EditText? = null
    var linearLayoutPermanentAddress2: LinearLayout? = null

    var editTextPermanentAddress3: EditText? = null
    var linearLayoutPermanentAddress3: LinearLayout? = null
    var custId: Int = 0

    override fun onVisibilityChanged(isKeyBoardVisible: Boolean) {
        if (viewEmpty != null) {
            if (isKeyBoardVisible) {
                if (viewEmpty.visibility == View.VISIBLE) {
                    viewEmpty.visibility = View.GONE
                    Thread.sleep(200)
                }


                val view = requireActivity().currentFocus
                if (view != null && view is EditText) {
                    checkForFocusAndScroll(view)


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

    override fun onActivityBackPressed() {
        checkBackPress()

    }

    override fun onResume() {
        super.onResume()
        if (activity is HostActivity) {
            (activity as HostActivity).activityBackPressed = AddressAndAdditionalFieldsFragment@ this
        }
    }

    override fun onStop() {
        super.onStop()
        if (activity is HostActivity) {
            (activity as HostActivity).activityBackPressed = null
        }
    }

    private fun checkBackPress() {
        navigateDashboardTop()
    }

    private fun checkForFocusAndScroll(view: View) {
        var viewToScroll: View? = null
        val etCurrent = view as EditText
        if (editTextCurrentAddress1 != null && editTextCurrentAddress1!!.hasFocus()) {
            viewToScroll = linearLayoutCurrentAddress1
        } else if (editTextCurrentAddress2 != null && editTextCurrentAddress2!!.hasFocus()) {
            viewToScroll = linearLayoutCurrentAddress2
        } else if (editTextCurrentAddress3 != null && editTextCurrentAddress3!!.hasFocus()) {
            viewToScroll = linearLayoutCurrentAddress3
        } else if (editTextPermanentAddress1 != null && editTextPermanentAddress1!!.hasFocus()) {
            viewToScroll = linearLayoutPermanentAddress1
        } else if (editTextPermanentAddress2 != null && editTextPermanentAddress2!!.hasFocus()) {
            viewToScroll = linearLayoutPermanentAddress2
        } else if (editTextPermanentAddress3 != null && editTextPermanentAddress3!!.hasFocus()) {
            viewToScroll = linearLayoutPermanentAddress3
        } else if (etCurrent.hasFocus() || etCurrent.hasFocus()) {
            //  viewToScroll = linearLayoutAddNewCurrentAddress
        }



        if (viewToScroll != null) {
            if (viewEmpty.visibility == View.GONE) {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        ThreadUtils.runOnUiThread(Runnable {
                            viewEmpty.visibility = View.VISIBLE
                            scrollToBottom(viewToScroll)
                            //scrollToRow(linearLayoutAddNewCurrentAddress,etCrrent)
                        });

                    }
                }, 300)


            }

        }
    }

    fun scrollToBottom(nextView: View?) {
        if (nextView != null) {
            scrollViewPostOffer.post {
                // scrollView1.fullScroll(View.FOCUS_DOWN)
                scrollViewPostOffer.scrollTo(0, nextView.top);
            }
        }
    }

    fun scrollToAdditionalFieldsBottom() {
        scrollViewPostOffer.post {
            // scrollView1.fullScroll(View.FOCUS_DOWN)
            scrollViewPostOffer.scrollTo(0, linearLayoutAdditionalFieldsUILayout.bottom);
        }
    }

    fun scrollToAdditionalFieldsBottom(nextView: View?) {
        if (nextView != null) {
            scrollViewPostOffer.post {
                // scrollView1.fullScroll(View.FOCUS_DOWN)
                scrollViewPostOffer.scrollTo(0, nextView.bottom);
            }
        }
    }

    private fun scrollToRow(
            linearLayout: LinearLayout,
            textViewToShow: EditText
    ) {
        val delay: Long = 100 //delay to let finish with possible modifications to ScrollView
        scrollViewPostOffer.postDelayed({
            val textRect = Rect() //coordinates to scroll to
            textViewToShow.getHitRect(textRect) //fills textRect with coordinates of TextView relative to its parent (LinearLayout)
            scrollViewPostOffer.requestChildRectangleOnScreen(
                    linearLayout,
                    textRect,
                    false
            ) //ScrollView will make sure, the given textRect is visible
        }, delay)
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val safeArgs = AddressAndAdditionalFieldsFragmentArgs.fromBundle(it)
            custId = safeArgs.customerID
            customerDetailsResponse = safeArgs.customerDetailsResponse
            caseID = customerDetailsResponse.data?.caseId.toString()

        }

        masterViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)

        transactionViewModel = ViewModelProvider(this).get(
                TransactionViewModel::class.java
        )

        masterViewModel.getPinCodeDataLiveData().observe(this) { mApiResponse: ApiResponse? ->
            onPinCodeResponse(mApiResponse!!)
        }

        transactionViewModel.getUpdateAddressLiveData()
                .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                    onUpdateResponse(
                            mApiResponse!!
                    )
                }
        transactionViewModel.getCustomerDetailsLiveData()
                .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                    onCustomerDetails(
                            mApiResponse!!
                    )
                })

        transactionViewModel.getAdditionalFieldsLiveData()
                .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                    onAdditionalFieldsResponse(
                            mApiResponse!!
                    )
                }

        masterViewModel.getAdditionalFieldAPILiveData()
                .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                    onAdditionalFieldAPIResponse(
                            mApiResponse!!
                    )
                }


        transactionViewModel.mSubmitAdditionalFieldsLiveData()
                .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                    onSubmitOfAdditionFields(
                            mApiResponse!!
                    )
                }

        masterViewModel.getKYCDocumentLiveData()
                .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                    onGetKYCDocumentResponse(mApiResponse!!)
                }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view: View =
                inflater.inflate(R.layout.v2_address_additional_fields_fragment, container, false)
        fragView = view
        initView(view)
        return view
    }

    private fun initView(view: View) {
        setKeyBoardShowHideEvent(AddressAndAdditionalFieldsFragment@ this)

        viewEmpty = view.findViewById(R.id.view_empty)!!
        scrollViewPostOffer = view.findViewById(R.id.scrollViewPostOffer)
        imageViewSelectedBankName = view.findViewById(R.id.imageViewSelectedBankName)
        linearLayoutAddNewCurrentAddress = view.findViewById(R.id.linearLayoutAddNewCurrentAddress)
        linearLayoutEditCurrentAddress = view.findViewById(R.id.linearLayoutEditCurrentAddress)
        linearLayoutAddNewPermanentAddress =
                view.findViewById(R.id.linearLayoutAddNewPermanentAddress)
        linearLayoutEditPermanentAddress = view.findViewById(R.id.linearLayoutEditPermanentAddress)
        linearLayoutAdditionalFieldsUILayout =
                view.findViewById(R.id.linearLayoutAdditionalFieldsUILayout)
        ivBackFromAddressAndAdditionalFields =
                view.findViewById(R.id.ivBackFromAddressAndAdditionalFields)
        imageViewEditCurrentAddress = view.findViewById(R.id.imageViewEditCurrentAddress)
        imageViewEditPermanentAddress = view.findViewById(R.id.imageViewEditPermanentAddress)


        textViewSelectBankLabel = view.findViewById(R.id.textViewSelectBankLabel)
        textViewCurrentAddress1 = view.findViewById(R.id.textViewCurrentAddress1)
        textViewCurrentAddress2 = view.findViewById(R.id.textViewCurrentAddress2)
        textViewCurrentAddress3 = view.findViewById(R.id.textViewCurrentAddress3)
        checkboxCurrentAndPermanentAddress =
                view.findViewById(R.id.checkboxCurrentAndPermanentAddress)
        textViewPermanentAddressEdit = view.findViewById(R.id.textViewPermanentAddressEdit)
        textViewPermanentAddress1 = view.findViewById(R.id.textViewPermanentAddress1)
        textViewPermanentAddress2 = view.findViewById(R.id.textViewPermanentAddress2)
        textViewPermanentAddress3 = view.findViewById(R.id.textViewPermanentAddress3)
        buttonMoveToNextPage = view.findViewById(R.id.buttonMoveToNextPage)


        textViewSelectBankLabel.text =
                "You have selected " + customerDetailsResponse.data?.loanDetails?.bankName
        setBankLogo()
        ivBackFromAddressAndAdditionalFields.setOnClickListener(this)
        imageViewEditCurrentAddress.setOnClickListener(this)
        imageViewEditPermanentAddress.setOnClickListener(this)
        buttonMoveToNextPage.setOnClickListener(this)

        if (hasConnectivityNetwork()) {
            transactionViewModel.getCustomerDetails(
                    createCustomerDetailsRequest(custId),
                    Global.customerAPI_BaseURL + CommonStrings.CUSTOMER_DETAILS_END_URL
            )
        }
        initiateView()

    }

    private fun setBankLogo() {
        Picasso.get()
                .load(customerDetailsResponse.data?.loanDetails?.bankLogoURL)
                .into(imageViewSelectedBankName, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        imageViewSelectedBankName.visibility = View.VISIBLE
                    }

                    override fun onError(ex: Exception) {
                        imageViewSelectedBankName.visibility = View.GONE
                    }
                })
    }

    private fun initiateView() {
        if (customerDetailsResponse.data != null) {
            val customerData = customerDetailsResponse.data
            if (customerData?.residentialDetails?.currentAddress?.addressLine1?.isNotEmpty() == true) {
                currentAddressResponse =
                        customerDetailsResponse.data?.residentialDetails?.currentAddress!!
                permanentAddressResponse =
                        customerDetailsResponse.data?.residentialDetails?.permanentAddress!!
                isPermanentAddress =
                        customerDetailsResponse.data?.residentialDetails?.currentAddress?.isPermanent!!
                showEditCurrentAddress()

            } else {
                if (linearLayoutAddNewCurrentAddress.childCount == 0) {
                    showNewCurrentAddress()
                } else {
                    linearLayoutAddNewCurrentAddress.removeAllViews()
                    showNewCurrentAddress()
                }
            }
        }

    }

    private fun showNewCurrentAddress() {

        val addressView: View = LayoutInflater.from(fragView.context)
                .inflate(R.layout.v2_add_new_address_layout, linearLayoutAddNewCurrentAddress, false)
        val textViewTypeOfAddress = addressView.findViewById<TextView>(R.id.textViewTypeOfAddress)
        val editTextPinCode = addressView.findViewById<EditText>(R.id.editTextPinCode)
        val buttonPinCodeCheck = addressView.findViewById<Button>(R.id.buttonPincodeCheck)
        val textViewState = addressView.findViewById<TextView>(R.id.textViewState)
        val textViewCity = addressView.findViewById<TextView>(R.id.textViewCity)
        val textViewCityMovedInLbl = addressView.findViewById<TextView>(R.id.textViewCityMovedInLbl)
        val linearLayoutCityMovedInYear =
                addressView.findViewById<LinearLayout>(R.id.linearLayoutCityMovedInYear)
        val editTextCityMovedInYear =
                addressView.findViewById<EditText>(R.id.editTextCityMovedInYear)

        editTextCurrentAddress1 = addressView.findViewById<EditText>(R.id.editTextAddress1)
        linearLayoutCurrentAddress1 =
                addressView.findViewById<LinearLayout>(R.id.linearLayoutAddress)

        editTextCurrentAddress2 = addressView.findViewById<EditText>(R.id.editTextAddress2)
        linearLayoutCurrentAddress2 =
                addressView.findViewById<LinearLayout>(R.id.linearLayoutAddress2)

        editTextCurrentAddress3 = addressView.findViewById<EditText>(R.id.editTextAddress3)
        linearLayoutCurrentAddress3 =
                addressView.findViewById<LinearLayout>(R.id.linearLayoutAddress3)

        editTextCurrentAddress1?.onFocusChangeListener =
                View.OnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        viewEmpty.visibility = View.GONE
                        checkForFocusAndScroll(editTextCurrentAddress1!!)
                    }
                }
        editTextCurrentAddress2?.onFocusChangeListener =
                View.OnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        viewEmpty.visibility = View.GONE
                        checkForFocusAndScroll(editTextCurrentAddress2!!)

                    }
                }
        editTextCurrentAddress3?.onFocusChangeListener =
                View.OnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        viewEmpty.visibility = View.GONE
                        checkForFocusAndScroll(editTextCurrentAddress3!!)
                    }
                }

        val checkboxIsPermanentAdd = addressView.findViewById<CheckBox>(R.id.checkboxIsPermanentAdd)

        val buttonSubmitAddress = addressView.findViewById<Button>(R.id.buttonSubmitAddress)

        textViewTypeOfAddress.text = resources.getString(R.string.v2_current_address)
        setFocusOnView(textViewTypeOfAddress)

        typeOfAddress = resources.getString(R.string.v2_current_address)

        checkboxIsPermanentAdd.visibility = View.VISIBLE
        isPermanentAddress = checkboxIsPermanentAdd.isChecked

        editTextPinCode.setText(pincode)
        textViewState.text = state
        textViewCity.text = city

        linearLayoutCityMovedInYear.setOnClickListener(View.OnClickListener {

            popUpDatePickerDialog(editTextCityMovedInYear)
        })

        checkboxIsPermanentAdd.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkboxIsPermanentAdd.isChecked = isChecked
                isPermanentAddress = true
            } else {
                checkboxIsPermanentAdd.isChecked = isChecked
                isPermanentAddress = false
                permanentAddress = PermanentAddress("", "")
                permanentAddressResponse = v2.model.response.PermanentAddress("", "", "", "", "", "")

            }

        }
        buttonPinCodeCheck.setOnClickListener(View.OnClickListener {
            if (editTextPinCode.text.toString()
                            .isNotEmpty() && editTextPinCode.text.toString().length == 6
            ) {
                if (hasConnectivityNetwork()) {
                    masterViewModel.getPinCodeData(Global.customerDetails_BaseURL + "Pincode/city/" + editTextPinCode.text.toString())
                }
            } else
                showToast("Please enter valid PinCode")
        })

        buttonSubmitAddress.setOnClickListener(View.OnClickListener {
            if (editTextPinCode.text.toString().isNotEmpty() &&
                    textViewState.text.toString().isNotEmpty() &&
                    textViewCity.text.toString().isNotEmpty() &&
                    editTextCurrentAddress1!!.text.toString().isNotEmpty() &&
                    editTextCurrentAddress2!!.text.toString().isNotEmpty() &&
                    editTextCurrentAddress3!!.text.toString().isNotEmpty()
            ) {

                if (linearLayoutCityMovedInYear.visibility == View.VISIBLE && cityMovedInYear.isEmpty()) {
                    showToast("Please select city moved in year")
                } else {
                    address1 = editTextCurrentAddress1!!.text.toString()
                    address2 = editTextCurrentAddress2!!.text.toString()
                    address3 = editTextCurrentAddress3!!.text.toString()
                    address = "$address1**$address2**$address3"
                    submitCurrentAddress()
                }
            } else {
                showToast("Please enter all Fields")
            }
        })

        linearLayoutAddNewCurrentAddress.addView(addressView)

    }

    private fun popUpDatePickerDialog(editText: EditText) {

        var lastSelectedDate: String? = null

        object : DatePickerCallBack {
            override fun dateSelected(dateDisplayValue: String, dateValue: String) {
                editText.setText(dateDisplayValue)
                cityMovedInYear = dateValue
            }
        }.callDatePickerDialog(
                lastSelectedDate,
                null,
                getTodayDate())

    }

    private fun showEditCurrentAddress() {
        if (linearLayoutAddNewCurrentAddress.visibility == View.VISIBLE)
            linearLayoutAddNewCurrentAddress.visibility = View.GONE

        linearLayoutEditCurrentAddress.visibility = View.VISIBLE
        textViewCurrentAddress1.text = currentAddressResponse.addressLine1
        textViewCurrentAddress2.text = currentAddressResponse.addressLine2
        textViewCurrentAddress3.text =
                currentAddressResponse.addressLine3 + ", " + currentAddressResponse.pincode

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
            if (permanentAddressResponse != null && permanentAddressResponse!!.pincode.isNullOrEmpty()) {
                showNewPermanentAddress()
            } else {
                permanentAddressResponse
                showEditPermanentAddress()
            }
        }

    }


    private fun showNewPermanentAddress() {

        if (linearLayoutEditPermanentAddress.visibility == View.VISIBLE)
            linearLayoutEditPermanentAddress.visibility = View.GONE

        linearLayoutAddNewPermanentAddress.visibility = View.VISIBLE
        val addressView: View = LayoutInflater.from(fragView.context)
                .inflate(R.layout.v2_add_new_address_layout, linearLayoutAddNewPermanentAddress, false)
        val textViewTypeOfAddress = addressView.findViewById<TextView>(R.id.textViewTypeOfAddress)
        val editTextPinCode = addressView.findViewById<EditText>(R.id.editTextPinCode)
        val buttonPinCodeCheck = addressView.findViewById<Button>(R.id.buttonPincodeCheck)
        val textViewState = addressView.findViewById<TextView>(R.id.textViewState)
        val textViewCity = addressView.findViewById<TextView>(R.id.textViewCity)
        val textViewCityMovedInLbl = addressView.findViewById<TextView>(R.id.textViewCityMovedInLbl)
        val linearLayoutCityMovedInYear =
                addressView.findViewById<LinearLayout>(R.id.linearLayoutCityMovedInYear)

        editTextPermanentAddress1 = addressView.findViewById<EditText>(R.id.editTextAddress1)
        linearLayoutPermanentAddress1 =
                addressView.findViewById<LinearLayout>(R.id.linearLayoutAddress)

        editTextPermanentAddress2 = addressView.findViewById<EditText>(R.id.editTextAddress2)
        linearLayoutPermanentAddress2 =
                addressView.findViewById<LinearLayout>(R.id.linearLayoutAddress2)

        editTextPermanentAddress3 = addressView.findViewById<EditText>(R.id.editTextAddress3)
        linearLayoutPermanentAddress3 =
                addressView.findViewById<LinearLayout>(R.id.linearLayoutAddress3)


        editTextPermanentAddress1?.onFocusChangeListener =
                View.OnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        viewEmpty.visibility = View.GONE
                        checkForFocusAndScroll(editTextPermanentAddress1!!)
                    }
                }

        editTextPermanentAddress2?.onFocusChangeListener =
                View.OnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        viewEmpty.visibility = View.GONE
                        checkForFocusAndScroll(editTextPermanentAddress2!!)
                    }
                }
        editTextPermanentAddress3?.onFocusChangeListener =
                View.OnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        viewEmpty.visibility = View.GONE
                        checkForFocusAndScroll(editTextPermanentAddress3!!)
                    }
                }

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
            if (editTextPinCode.text.toString()
                            .isNotEmpty() && editTextPinCode.text.toString().length == 6
            ) {
                if (hasConnectivityNetwork()) {
                    masterViewModel.getPinCodeData(Global.customerDetails_BaseURL + "Pincode/city/" + editTextPinCode.text.toString())
                }
            } else
                showToast("Please enter valid PinCode")
        })

        buttonSubmitAddress.setOnClickListener(View.OnClickListener {
            if (editTextPinCode.text.toString().isNotEmpty() &&
                    textViewState.text.toString().isNotEmpty() &&
                    textViewCity.text.toString().isNotEmpty() &&
                    editTextPermanentAddress1!!.text.toString().isNotEmpty() &&
                    editTextPermanentAddress2!!.text.toString().isNotEmpty() &&
                    editTextPermanentAddress3!!.text.toString().isNotEmpty()
            ) {

                address1 = editTextPermanentAddress1!!.text.toString()
                address2 = editTextPermanentAddress2!!.text.toString()
                address3 = editTextPermanentAddress3!!.text.toString()
                address = "$address1***$address2***$address3"

                submitPermanentAddress()

            } else {
                showToast("Please enter all Fields")
            }
        })

        linearLayoutAddNewPermanentAddress.addView(addressView)
    }

    private fun showEditPermanentAddress() {

        if (linearLayoutAddNewPermanentAddress.visibility == View.VISIBLE)
            linearLayoutAddNewPermanentAddress.visibility = View.GONE

        linearLayoutEditPermanentAddress.visibility = View.VISIBLE

        try {
            textViewPermanentAddress1.text = permanentAddressResponse?.addressLine1!!
            textViewPermanentAddress2.text = permanentAddressResponse?.addressLine2!!
            textViewPermanentAddress3.text =
                    "${permanentAddressResponse?.addressLine3}, ${permanentAddressResponse?.pincode!!}"
        } catch (ex: NullPointerException) {

        } catch (ex: Exception) {

        }

        initiateAdditionalFields()
    }


    private fun submitCurrentAddress() {

        currentAddress = CurrentAddress(isPermanentAddress, pincode, address)

        if (isPermanentAddress) {

            permanentAddress = PermanentAddress(pincode, address)
            val addressData =
                    AddressData(custId, currentAddress, permanentAddress, cityMovedInYear)
            val updateAddressRequest =
                    UpdateAddressRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, addressData)

            if (hasConnectivityNetwork()) {
                transactionViewModel.updateAddress(
                        updateAddressRequest,
                        Global.customerAPI_BaseURL + CommonStrings.UPDATE_ADDRESS_URL
                )
            }


        } else {
            currentAddressResponse = v2.model.response.CurrentAddress(
                    address1,
                    address2,
                    address3,
                    city,
                    isPermanentAddress,
                    pincode,
                    state
            )
            address1 = ""
            address2 = ""
            address3 = ""
            city = ""
            pincode = ""
            state = ""
            showEditCurrentAddress()
        }
    }

    private fun submitPermanentAddress() {

        permanentAddress = PermanentAddress(pincode, address)
        val addressData =
                AddressData(custId, currentAddress, permanentAddress, cityMovedInYear)
        val updateAddressRequest =
                UpdateAddressRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, addressData)
        if (hasConnectivityNetwork()) {
            transactionViewModel.updateAddress(
                    updateAddressRequest,
                    Global.customerAPI_BaseURL + CommonStrings.UPDATE_ADDRESS_URL
            )
        }
    }

    //region AddressFunctions


    private fun initiateAdditionalFields() {


        if (linearLayoutAdditionalFieldsUILayout.visibility == View.INVISIBLE || linearLayoutAdditionalFieldsUILayout.visibility == View.GONE) {
            if (hasConnectivityNetwork()) {
                transactionViewModel.getAdditionalFieldsData(
                        CustomerRequest(
                                ResetCustomerJourneyDataRequest(custId.toString()),
                                CommonStrings.USER_TYPE,
                                CommonStrings.USER_TYPE
                        ), Global.baseURL + CommonStrings.ADDITIONAL_FIELDS_URL
                )
            }
        }

    }


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
                initiateView()

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
                mApiResponse.data as SimpleResponse?

                if (typeOfAddress == getString(R.string.v2_current_address)) {
                    currentAddressResponse = v2.model.response.CurrentAddress(
                            address1,
                            address2,
                            address3,
                            city,
                            isPermanentAddress,
                            pincode,
                            state
                    )
                    permanentAddressResponse = v2.model.response.PermanentAddress(
                            address1,
                            address2,
                            address3,
                            city,
                            pincode,
                            state
                    )
                    address1 = ""
                    address2 = ""
                    address3 = ""
                    city = ""
                    pincode = ""
                    state = ""
                    editTextCurrentAddress1?.text?.clear()
                    editTextCurrentAddress2?.text?.clear()
                    editTextCurrentAddress3?.text?.clear()
                    showEditCurrentAddress()
                } else if (typeOfAddress == getString(R.string.v2_permanent_address)) {
                    permanentAddressResponse = v2.model.response.PermanentAddress(
                            address1,
                            address2,
                            address3,
                            city,
                            pincode,
                            state
                    )
                    address1 = ""
                    address2 = ""
                    address3 = ""
                    city = ""
                    pincode = ""
                    state = ""
                    editTextPermanentAddress1?.text?.clear()
                    editTextPermanentAddress2?.text?.clear()
                    editTextPermanentAddress2?.text?.clear()
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
                additionalFieldsData = response?.data!!

                if (response.data.sections.isNotEmpty()) {

                    if (buttonMoveToNextPage.visibility == View.VISIBLE) {
                        viewEmpty.visibility = View.GONE
                        buttonMoveToNextPage.visibility = View.GONE
                    }
                    linearLayoutAddNewPermanentAddress.removeAllViews()
                    linearLayoutAddNewPermanentAddress.visibility = View.GONE
                    linearLayoutEditCurrentAddress.visibility = View.VISIBLE

                    if (!isPermanentAddress)
                        linearLayoutEditPermanentAddress.visibility = View.VISIBLE

                    if (linearLayoutAdditionalFieldsUILayout.visibility != View.VISIBLE) {
                        linearLayoutAdditionalFieldsUILayout.visibility = View.VISIBLE
                        scrollToBottom(linearLayoutAdditionalFieldsUILayout)
                        setAdditionalField()
                    }


                } else {
                    viewEmpty.visibility = View.GONE

                    if (buttonMoveToNextPage.visibility != View.VISIBLE)
                        buttonMoveToNextPage.visibility = View.VISIBLE

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

                val additionalFieldRes: APIDropDownResponse? =
                        mApiResponse.data as APIDropDownResponse?
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
                    if (hasConnectivityNetwork()) {
                        masterViewModel.getKYCDocumentResponse(Global.baseURL + CommonStrings.KYC_UPLOAD_URL_END_POINT + custId)
                    }
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
                        navigateToKYCDocumentUpload(
                                custId.toString(),
                                kycDocumentRes,
                                caseID,
                                customerDetailsResponse,
                                ""
                        )
                    else if (kycDocumentRes.data.groupedDoc.isEmpty() && kycDocumentRes.data.nonGroupedDoc.isEmpty())
                        navigateToBankOfferStatus(
                                custId.toString(),
                                customerDetailsResponse,
                                "AddressAdditionalFields"
                        )
                } else {
                    navigateToBankOfferStatus(
                            custId.toString(),
                            customerDetailsResponse,
                            "AddressAdditionalFields"
                    )
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

            Log.i("TAG", "setAdditionalField: $sectionIndex" + " -----> " + linearLayoutAdditionalFieldsUILayout.childCount + "---->" + linearLayoutAdditionalFieldsUILayout.getChildAt(linearLayoutAdditionalFieldsUILayout.childCount - 1).rootView)

            if (isEditFlow || sectionIndex == sectionsList.size - 1)
                scrollToAdditionalFieldsBottom(linearLayoutAdditionalFieldsUILayout.getChildAt(sectionIndex).findViewById(R.id.linearLayoutSectionLayout))
            else {
                if (sectionIndex != 0) {
                    scrollToAdditionalFieldsBottom(linearLayoutAdditionalFieldsUILayout)
                }
            }

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
        val currentSectionLayout: View
        if (isSectionPreFilled(sectionData.sectionName)) {
            currentSectionLayout = if (sectionMap.size == additionalFieldsData.sections.size && isLastSection)
                generateEditSectionUI(sectionData, true)
            else
                generateEditSectionUI(sectionData, false)

        } else {

            if (buttonMoveToNextPage.visibility == View.VISIBLE)
                buttonMoveToNextPage.visibility = View.GONE

            if (sectionData.type == "Address" || isLastSection) {

                if (currentFilledFieldData.isEmpty()) {
                    if (mandatoryFieldsList.isEmpty())
                        addMandatoryFieldsToMandatoryList(fieldList)
                    else {
                        mandatoryFieldsList.clear()
                        addMandatoryFieldsToMandatoryList(fieldList)
                    }

                }

                currentSectionLayout = LayoutInflater.from(fragView.context).inflate(
                        R.layout.v2_custom_address_parent_layout,
                        linearLayoutAdditionalFieldsUILayout,
                        false
                )

                val linearLayout =
                        currentSectionLayout.findViewById<LinearLayout>(R.id.linearLayoutCustomAddressSectionLayout)

                val addressButton =
                        currentSectionLayout.findViewById<Button>(R.id.buttonSubmitAddressDetails)
                currentSectionButton = addressButton

                if (sectionData.displayName) {
                    val currentSectionTitle: View = LayoutInflater.from(fragView.context)
                            .inflate(R.layout.v2_custom_title_text_view, linearLayout, false)
                    val sectionTitle: TextView =
                            currentSectionTitle.findViewById(R.id.textViewTitleLabel)
                    sectionTitle.text = sectionData.sectionName
                    linearLayout.addView(currentSectionTitle)

                }

                addressButton.setOnClickListener(View.OnClickListener {
                    if (isAllMandatoryFieldsFilledInCurrentSection()) {
                        if (sectionData.type == "Address" && !isLastSection) {
                            moveCurrentDetailsToMap(sectionData.sectionName)
                        } else {
                            submitAdditionalFieldsList.putAll(currentFilledFieldData)
                            val fieldList: ArrayList<FieldDetails> =
                                    ArrayList<FieldDetails>(submitAdditionalFieldsList.values)
                            sectionMap[sectionData.sectionName] = fieldList
                            currentFilledFieldData.clear()
                            mandatoryFieldsList.clear()
                            submitAdditionalFields()
                        }
                    } else {
                        Log.i(
                                "SoftOffer",
                                "generateSectionUI: " + "" + currentFilledFieldData.size + "=====>" + fieldList.size
                        )
                        showToast("Please fill all fields")
                    }
                })

                generateFieldUI(sectionData.sectionName, linearLayout, fieldList, true)

            } else {
                addMandatoryFieldsToMandatoryList(fieldList)

                currentSectionLayout = LayoutInflater.from(fragView.context).inflate(
                        R.layout.v2_custom_parent_layout,
                        linearLayoutAdditionalFieldsUILayout,
                        false
                )
                val linearLayout =
                        currentSectionLayout.findViewById<LinearLayout>(R.id.linearLayoutCustomParentLayout)
                if (sectionData.displayName) {
                    val currentSectionTitle: View = LayoutInflater.from(fragView.context)
                            .inflate(R.layout.v2_custom_title_text_view, linearLayout, false)
                    val sectionTitle: TextView =
                            currentSectionTitle.findViewById(R.id.textViewTitleLabel)
                    sectionTitle.text = sectionData.sectionName
                    linearLayout.addView(currentSectionTitle)
                }
                generateFieldUI(sectionData.sectionName, linearLayout, fieldList, false)
            }
        }
        return currentSectionLayout
    }

    private fun addMandatoryFieldsToMandatoryList(fieldList: List<Fields>) {

        for (index in fieldList.indices) {
            if (fieldList[index].isMandatory)
                mandatoryFieldsList[fieldList[index].apiDetails.apiKey] = "empty"
        }

    }

    private fun isAllMandatoryFieldsFilledInCurrentSection(): Boolean {
        return !mandatoryFieldsList.values.contains("empty")
    }


    private fun generateFieldUI(
            sectionName: String,
            linearLayout: LinearLayout?,
            fieldList: List<Fields>,
            isLastSection: Boolean
    ) {

        for (fieldIndex in fieldList.indices) {
            if (fieldList[fieldIndex].displayLabel) {
                val fieldTitleText = fieldList[fieldIndex].label
                val isMandatoryField = fieldList[fieldIndex].isMandatory
                val titleView: View? = linearLayout?.let { getTitleView(fieldTitleText, it, isMandatoryField) }
                linearLayout?.addView(titleView)
            }
            val fieldVal: Fields = fieldList[fieldIndex]
            val fieldView: View? = linearLayout?.let {
                getFieldView(
                        sectionName,
                        fieldVal,
                        fieldList,
                        fieldIndex == fieldList.size - 1,
                        isLastSection,
                        it
                )
            }

            linearLayout?.addView(fieldView)

            if(fieldList[fieldIndex].caption.isNotEmpty())
            {
                val titleView: View? = linearLayout?.let { getCaptionView(fieldList[fieldIndex].caption,linearLayout) }
                val textView:TextView= titleView!!.findViewById(R.id.tvAdditionalFieldsCaption)
                linearLayout.addView(titleView )
            }

            if (fieldList[fieldIndex].apiDetails.apiKey == "CompanyPincode" && isFieldFilled(
                            fieldList[fieldIndex].apiDetails.apiKey
                    ).isEmpty()
            )
                break
            else
                continue

        }

    }

    private fun getCaptionView(caption: String,linearLayout: LinearLayout): View {
        val currentCaptionView: View = LayoutInflater.from(fragView.context)
                .inflate(R.layout.v2_custom_caption_view, linearLayout, false)
        val fieldTitle: TextView = currentCaptionView.findViewById(R.id.tvAdditionalFieldsCaption)
        fieldTitle.text = caption
        return currentCaptionView
    }

    private fun getTitleView(title: String, linearLayout: LinearLayout, isMandatory: Boolean): View {
        val currentFieldViewTitle: View = LayoutInflater.from(fragView.context)
                .inflate(R.layout.v2_custom_title_text_view, linearLayout, false)
        val fieldTitle: TextView = currentFieldViewTitle.findViewById(R.id.textViewTitleLabel)

        if (isMandatory) {
            val text = "$title "
            val colored = requireContext().getString(R.string.lbl_asterick)
            val builder = SpannableStringBuilder()

            builder.append(text)
            val start = builder.length
            builder.append(colored)
            val end = builder.length

            builder.setSpan(
                    ForegroundColorSpan(Color.RED), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            fieldTitle.text = builder
        } else {
            fieldTitle.text = title
        }
        return currentFieldViewTitle
    }

    private fun getFieldView(
            sectionName: String,
            fieldData: Fields,
            cFieldList: List<Fields>,
            isLastItem: Boolean,
            isLastSection: Boolean,
            linearLayout: LinearLayout
    ): View {

        val currentFieldInputView: View = LayoutInflater.from(fragView.context)
                .inflate(R.layout.v2_custom_field_parent_layout, linearLayout, false)

        val parentLayout: LinearLayout = currentFieldInputView.findViewById(R.id.linearLayoutFieldParent)

        GlobalScope.launch(Dispatchers.Main) {
            when (fieldData.fieldType) {
                "Text" -> {

                    val currentFieldView: View = getEditTextView(sectionName, fieldData, isLastItem, isLastSection, cFieldList, linearLayout, parentLayout, currentFieldInputView.context)
                    parentLayout.removeAllViews()
                    parentLayout.addView(currentFieldView)
                    val fieldInputValue: EditText =
                            currentFieldView.findViewById(R.id.editTextFieldInput)

                    fieldInputValue.hint = fieldData.placeHolder

                    // prefill
                    fieldInputValue.setText(isFieldFilled(fieldData.apiDetails.apiKey))

                    fieldInputValue.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                        ) {
                        }

                        override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                        ) {
                        }

                        override fun afterTextChanged(s: Editable?) {
                            if (!TextUtils.isEmpty(s.toString())) {

                                if (fieldData.apiDetails.apiKey == "CompanyPincode") {
                                    if (s?.length == 6) {
                                        updateEditTextValues(
                                                fieldInputValue,
                                                fieldData,
                                                sectionName,
                                                isLastItem,
                                                linearLayout,
                                                cFieldList,
                                                isLastSection,
                                                currentFieldView
                                        )
                                    } else if (s?.length!! > 6)
                                        showToast("Enter valid Pincode")
                                } else {
                                    last_text_edit = System.currentTimeMillis()
                                    handler.removeCallbacksAndMessages(null)
                                    handler.postDelayed(input_finish_checker, delay)
                                }


                            } else {
                                showToast("Please enter Value")
                            }
                        }

                        val input_finish_checker = Runnable {
                            if (System.currentTimeMillis() > last_text_edit + delay - 3000) {
                                updateEditTextValues(
                                        fieldInputValue,
                                        fieldData,
                                        sectionName,
                                        isLastItem,
                                        linearLayout,
                                        cFieldList,
                                        isLastSection,
                                        currentFieldView
                                )


                            }
                        }

                    })

                    // getVal
                    fieldInputValue.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                        if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || event != null && event.keyCode == KeyEvent.KEYCODE_BACK ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                actionId == EditorInfo.IME_ACTION_NEXT
                        ) {

                            updateEditTextValues(
                                    fieldInputValue,
                                    fieldData,
                                    sectionName,
                                    isLastItem,
                                    linearLayout,
                                    cFieldList,
                                    isLastSection,
                                    currentFieldView
                            )

                        }
                        false
                    })

                    fieldInputValue.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                        if (!hasFocus) {
                            updateEditTextValues(
                                    fieldInputValue,
                                    fieldData,
                                    sectionName,
                                    isLastItem,
                                    linearLayout,
                                    cFieldList,
                                    isLastSection,
                                    currentFieldView
                            )

                        }
                    }


                }

                "DropDown" -> {
                    val currentFieldView: View = LayoutInflater.from(fragView.context)
                            .inflate(R.layout.v2_custom_input_text_view, parentLayout, false)
                    val fieldInput: TextView = currentFieldView.findViewById(R.id.textViewDropDown)
                    val linearLayoutCustomDropDownTextView: LinearLayout =
                            currentFieldView.findViewById(R.id.linearLayoutCustTextView)


                    fieldInput.hint = fieldData.placeHolder
                  val freeTextView:View = LayoutInflater.from(fragView.context)
                            .inflate(R.layout.v2_custom_edit_text, parentLayout, false)
                    val fieldInputValue: EditText =
                            freeTextView.findViewById(R.id.editTextFieldInput)


                    if (isFieldFilled(fieldData.apiDetails.apiKey).isNotEmpty()) {
                        fieldInput.text = isFieldFilled(fieldData.apiDetails.apiKey)

                    }

                    parentLayout.removeAllViews()
                    parentLayout.addView(currentFieldView)
                    if ((fieldData.showFreeText) && (fieldData.showFreeTextWhenValueIs.equals(fieldInput.text.toString(), true))) {
                        fieldInputValue.setText(getFreeText(fieldData.apiDetails.apiKey))
                        parentLayout.addView(freeTextView)
                    }


                    if (fieldData.apiDetails.apiKey == "CompanyState" || fieldData.apiDetails.apiKey == "CompanyCity") {
                        setStateOrCityValue(
                                sectionName,
                                fieldData.apiDetails.apiKey,
                                fieldInput,
                                fieldData.isMandatory,
                                fieldData.apiDetails.url,
                                currentFieldInputView,
                                linearLayout, cFieldList, isLastSection
                        )

                    }

                    val apiURL = fieldData.apiDetails.url
                    fieldInput.setOnClickListener(View.OnClickListener {
                        linearLayoutCustomDropDownTextView.performClick()
                    })
                    linearLayoutCustomDropDownTextView.setOnClickListener(View.OnClickListener {
                        if (fieldData.apiDetails.apiKey != "CompanyState" && fieldData.apiDetails.apiKey != "CompanyCity") {
                            if (hasConnectivityNetwork()) {

                                RetroBase.retrofitInterface.getFromWeb(apiURL)
                                        ?.enqueue(object : Callback<Any> {

                                            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                                val strRes = Gson().toJson(response.body())
                                                val dpRes =
                                                        Gson().fromJson(strRes, APIDropDownResponse::class.java)
                                                if (dpRes != null && dpRes.status) {
                                                    val optionList = dpRes.data.details
                                                    if (optionList.isNotEmpty() && optionList.size == 1) {
                                                        if (listOf(optionList).any { true }) {
                                                            val details = Details(
                                                                    optionList[0].displayLabel,
                                                                    optionList[0].value
                                                            )
                                                            fieldInput.text = details.displayLabel
                                                            if (isLastSection && isLastItem) {
                                                                val editTextString: String = details.value
                                                                val currentFieldDetails = FieldDetails(
                                                                        fieldData.apiDetails.apiKey,
                                                                        editTextString,
                                                                        editTextString,
                                                                        ""
                                                                )
                                                                addToCurrentFilledFieldData(
                                                                        fieldData.apiDetails.apiKey,
                                                                        fieldData.isMandatory,
                                                                        currentFieldDetails,
                                                                        false,
                                                                        sectionName, linearLayout, cFieldList, isLastSection
                                                                )
                                                            } else {
                                                                validateInput(
                                                                        sectionName,
                                                                        fieldData.apiDetails.apiKey,
                                                                        details.value,
                                                                        fieldData.isMandatory,
                                                                        isLastItem,
                                                                        "",
                                                                        fieldData.apiDetails.apiKey,
                                                                        details.displayLabel,
                                                                        currentFieldInputView,
                                                                        "",
                                                                        linearLayout, cFieldList, isLastSection
                                                                )
                                                            }

                                                        } else {
                                                            showToast("Something went wrong! Please try again!")
                                                        }
                                                    } else if (optionList.isNotEmpty() && optionList.size > 1) {
                                                        showDropDownDialog(
                                                                fieldData.apiDetails.url,
                                                                fieldData.label,
                                                                optionList,
                                                                object : AdditionalFieldsDetailsInterface {
                                                                    override fun returnDetails(details: Details) {
                                                                        fieldInput.text = details.displayLabel
                                                                        if (isLastSection && isLastItem) {
                                                                            val editTextString: String =
                                                                                    details.value

                                                                            val currentFieldDetails = FieldDetails(
                                                                                    fieldData.apiDetails.apiKey,
                                                                                    details.value,
                                                                                    details.displayLabel,
                                                                                    ""
                                                                            )
                                                                            addToCurrentFilledFieldData(
                                                                                    fieldData.apiDetails.apiKey,
                                                                                    fieldData.isMandatory,
                                                                                    currentFieldDetails,
                                                                                    false,
                                                                                    sectionName,
                                                                                    linearLayout, cFieldList, isLastSection
                                                                            )
                                                                        } else {
                                                                            val currentFieldDetails = FieldDetails(
                                                                                    fieldData.apiDetails.apiKey,
                                                                                    details.value,
                                                                                    details.displayLabel,
                                                                                    ""
                                                                            )

                                                                            if (fieldData.apiDetails.apiKey == "Company" && fieldData.showFreeText && details.displayLabel == fieldData.showFreeTextWhenValueIs) {


                                                                                if(!parentLayout.contains(freeTextView))
                                                                                {
                                                                                    parentLayout.addView(freeTextView)
                                                                                }
                                                                                fieldInputValue.addTextChangedListener(object : TextWatcher {
                                                                                    override fun beforeTextChanged(
                                                                                            s: CharSequence?,
                                                                                            start: Int,
                                                                                            count: Int,
                                                                                            after: Int
                                                                                    ) {
                                                                                    }

                                                                                    override fun onTextChanged(
                                                                                            s: CharSequence?,
                                                                                            start: Int,
                                                                                            before: Int,
                                                                                            count: Int
                                                                                    ) {
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
                                                                                        if (System.currentTimeMillis() > last_text_edit + delay - 3000) {
                                                                                            val currentFDtls = FieldDetails(currentFieldDetails.APIKey, currentFieldDetails.Value, currentFieldDetails.DisplayLabel, fieldInputValue.text.toString())
                                                                                            if (isLastItem)
                                                                                                addToCurrentFilledFieldData(
                                                                                                        fieldData.apiDetails.apiKey,
                                                                                                        fieldData.isMandatory,
                                                                                                        currentFDtls,
                                                                                                        true,
                                                                                                        sectionName,
                                                                                                        linearLayout, cFieldList, isLastSection
                                                                                                )
                                                                                            else
                                                                                                addToCurrentFilledFieldData(
                                                                                                        fieldData.apiDetails.apiKey,
                                                                                                        fieldData.isMandatory,
                                                                                                        currentFDtls,
                                                                                                        false,
                                                                                                        sectionName,
                                                                                                        linearLayout, cFieldList, isLastSection
                                                                                                )

                                                                                        }
                                                                                    }

                                                                                })

                                                                                // getVal
                                                                                fieldInputValue.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                                                                                    if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || event != null && event.keyCode == KeyEvent.KEYCODE_BACK ||
                                                                                            actionId == EditorInfo.IME_ACTION_DONE ||
                                                                                            actionId == EditorInfo.IME_ACTION_NEXT
                                                                                    ) {

                                                                                        val currentFDtls = FieldDetails(currentFieldDetails.APIKey, currentFieldDetails.Value, currentFieldDetails.DisplayLabel, fieldInputValue.text.toString())

                                                                                        addToCurrentFilledFieldData(
                                                                                                fieldData.apiDetails.apiKey,
                                                                                                fieldData.isMandatory,
                                                                                                currentFDtls,
                                                                                                false,
                                                                                                sectionName,
                                                                                                linearLayout, cFieldList, isLastSection
                                                                                        )


                                                                                    }
                                                                                    false
                                                                                })

                                                                                fieldInputValue.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                                                                                    if (!hasFocus) {
                                                                                        val currentFDtls = FieldDetails(currentFieldDetails.APIKey, currentFieldDetails.Value, currentFieldDetails.DisplayLabel, fieldInputValue.text.toString())

                                                                                        addToCurrentFilledFieldData(
                                                                                                fieldData.apiDetails.apiKey,
                                                                                                fieldData.isMandatory,
                                                                                                currentFDtls,
                                                                                                false,
                                                                                                sectionName, linearLayout, cFieldList, isLastSection
                                                                                        )

                                                                                    }
                                                                                }

                                                                            } else {

                                                                                if(fieldInputValue.visibility==View.VISIBLE)
                                                                                {
                                                                                    fieldInputValue.text.clear()
                                                                                    parentLayout.removeView(freeTextView)
                                                                                }

                                                                                if (isLastItem)
                                                                                    addToCurrentFilledFieldData(
                                                                                            fieldData.apiDetails.apiKey,
                                                                                            fieldData.isMandatory,
                                                                                            currentFieldDetails,
                                                                                            true,
                                                                                            sectionName,
                                                                                            linearLayout, cFieldList, isLastSection)
                                                                                else
                                                                                    addToCurrentFilledFieldData(
                                                                                            fieldData.apiDetails.apiKey,
                                                                                            fieldData.isMandatory,
                                                                                            currentFieldDetails,
                                                                                            false,
                                                                                            sectionName,
                                                                                            linearLayout, cFieldList, isLastSection
                                                                                    )

                                                                            }

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


                            }
                        }

                    })


                }

                "Check" -> {

                    val currentFieldView: View = LayoutInflater.from(fragView.context)
                            .inflate(R.layout.v2_custom_check_type_layout, parentLayout, false)
                    val recyclerView: RecyclerView =
                            currentFieldView.findViewById(R.id.recyclerViewAdditionalField)
                    parentLayout.removeAllViews()
                    parentLayout.addView(currentFieldView)

                    if (hasConnectivityNetwork()) {
                        RetroBase.retrofitInterface.getFromWeb(fieldData.apiDetails.url)
                                ?.enqueue(object : Callback<Any> {
                                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                        val strRes = Gson().toJson(response.body())
                                        val dpRes = Gson().fromJson(strRes, APIDropDownResponse::class.java)
                                        if (dpRes != null && dpRes.status) {
                                            checkList = dpRes.data.details as ArrayList<Details>
                                            getDTOList()
                                            if (list.isNotEmpty()) {
                                                additionalFieldAdapter = DataRecyclerViewAdapter(
                                                        activity as Activity,
                                                        list,
                                                        object : itemClickCallBack {
                                                            override fun itemClick(item: Any?, position: Int) {

                                                                additionalFieldAdapter.dataListFilter!!.forEachIndexed { index, item ->
                                                                    run {
                                                                        if (index == position) {
                                                                            item.selected = true
                                                                            val displayLabel: String =
                                                                                    item.displayValue as String
                                                                            val value: String = item.value as String

                                                                            if (isLastSection && isLastItem) {

                                                                                val currentFieldDetails =
                                                                                        FieldDetails(
                                                                                                fieldData.apiDetails.apiKey,
                                                                                                value,
                                                                                                displayLabel,
                                                                                                ""
                                                                                        )

                                                                                if (!isEditFlow)
                                                                                    scrollToAdditionalFieldsBottom()

                                                                                addToCurrentFilledFieldData(
                                                                                        fieldData.apiDetails.apiKey,
                                                                                        fieldData.isMandatory,
                                                                                        currentFieldDetails,
                                                                                        false,
                                                                                        sectionName,
                                                                                        linearLayout, cFieldList, isLastSection
                                                                                )
                                                                            } else {
                                                                                val currentFieldDetails =
                                                                                        FieldDetails(
                                                                                                fieldData.apiDetails.apiKey,
                                                                                                value,
                                                                                                displayLabel,
                                                                                                ""
                                                                                        )

                                                                                if (!isEditFlow)
                                                                                    scrollToAdditionalFieldsBottom()

                                                                                addToCurrentFilledFieldData(
                                                                                        fieldData.apiDetails.apiKey,
                                                                                        fieldData.isMandatory,
                                                                                        currentFieldDetails,
                                                                                        true,
                                                                                        sectionName,
                                                                                        linearLayout, cFieldList, isLastSection
                                                                                )


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
                                                        StaggeredGridLayoutManager(
                                                                2,
                                                                StaggeredGridLayoutManager.VERTICAL
                                                        )
                                                val layoutManagerGridLayoutManager =
                                                        GridLayoutManager(activity, 2)

                                                recyclerView.addItemDecoration(GridItemDecoration(25, 2))

                                                recyclerView.layoutManager =
                                                        layoutManagerStaggeredGridLayoutManager

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

            }

        }

        return currentFieldInputView
    }

    private fun getEditTextView(sectionName: String, fieldData: Fields, isLastItem: Boolean, isLastSection: Boolean, cFieldList: List<Fields>, parentLayout: LinearLayout, fieldLayout: LinearLayout, fieldParentViewContext: Context): View {
        val currentFieldView: View = LayoutInflater.from(fieldParentViewContext)
                .inflate(R.layout.v2_custom_edit_text, fieldLayout, false)
        val fieldInputValue: EditText =
                currentFieldView.findViewById(R.id.editTextFieldInput)
        return currentFieldView
    }

    private fun showFreeText(showText: Boolean,
                             currentLinearLayout: LinearLayout,
                             sectionName: String,
                             isLastSection: Boolean,
                             isMandatory: Boolean,
                             lastItem: Boolean,
                             cFieldList: List<Fields>,
                             fieldData: Fields,
                             prefilledFreeText: String,
                             freeTextDetails: FreeTextDetails,
                             linearLayout: LinearLayout,
                             currentFieldDetails: FieldDetails) : View {


        val freeTextView: View = LayoutInflater.from(fragView.context)
                .inflate(R.layout.v2_custom_edit_text, currentLinearLayout, true)
        val fieldInputValue: EditText =
                freeTextView.findViewById(R.id.editTextFieldInput)


        if (showText) {

            if (fieldInputValue.parent != null) {
                (fieldInputValue.parent as ViewGroup).removeView(fieldInputValue) // <- fix
            }
            currentLinearLayout.addView(fieldInputValue)
            fieldInputValue.hint = freeTextDetails.placeHolder

            if (prefilledFreeText.isNotEmpty())
                fieldInputValue.setText(prefilledFreeText)


        } else {

            if (fieldInputValue.parent != null) {
                (fieldInputValue.parent as ViewGroup).removeView(fieldInputValue)
            }

                }

return freeTextView
    }

    private fun updateEditTextValues(
            fieldInputValue: EditText,
            fieldData: Fields,
            sectionName: String,
            isLastItem: Boolean,
            linearLayout: LinearLayout,
            cFieldList: List<Fields>,
            isLastSection: Boolean,
            currentFieldInputView: View
    ) {
        if (fieldInputValue.text.isNotEmpty()) {

            if (fieldData.apiDetails.apiKey == "CompanyPincode") {

                val editTextString: String = fieldInputValue.text.toString()
                if (editTextString.length == 6) {
                    additionaFieldPinCode = editTextString
                    validateInput(
                            sectionName,
                            fieldData.apiDetails.apiKey,
                            editTextString,
                            fieldData.isMandatory,
                            isLastItem,
                            fieldData.regexValidation,
                            fieldData.apiDetails.apiKey,
                            editTextString,
                            currentFieldInputView,
                            "",
                            linearLayout,
                            cFieldList,
                            isLastSection
                    )
                    refreshFieldView(sectionName, linearLayout, cFieldList, isLastSection)
                } else {
                    showToast("Enter valid Pincode")
                }

            } else if (sectionName == "Address" && isLastItem || isLastSection && isLastItem) {

                val editTextString: String = fieldInputValue.text.toString()
                val currentFieldDetails =
                        FieldDetails(fieldData.apiDetails.apiKey, editTextString, editTextString, "")
                scrollToAdditionalFieldsBottom(linearLayout)
                addToCurrentFilledFieldData(
                        fieldData.apiDetails.apiKey,
                        fieldData.isMandatory,
                        currentFieldDetails,
                        false,
                        sectionName,
                        linearLayout, cFieldList, isLastSection
                )

            } else {
                val editTextString: String = fieldInputValue.text.toString()

                validateInput(
                        sectionName,
                        fieldData.apiDetails.apiKey,
                        editTextString,
                        fieldData.isMandatory,
                        isLastItem,
                        fieldData.regexValidation,
                        fieldData.apiDetails.apiKey,
                        editTextString,
                        currentFieldInputView,
                        "",
                        linearLayout,
                        cFieldList,
                        isLastSection
                )
            }

        } else
            showToast("Please fill the field")

    }

    private fun setStateOrCityValue(
            sectionName: String,
            apiKey: String,
            fieldInput: TextView,
            isMandatory: Boolean,
            url: String,
            currentFieldInputView: View,
            linearLayout: LinearLayout, cFieldList: List<Fields>, isLastSection: Boolean
    ) {
        val apiURL = url + additionaFieldPinCode
        var textVal = ""
        if (hasConnectivityNetwork()) {
            showProgressDialog(requireContext())
            RetroBase.retrofitInterface.getFromWeb(apiURL)?.enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    val strRes = Gson().toJson(response.body())
                    val dpRes = Gson().fromJson(strRes, APIDropDownResponse::class.java)
                    hideProgressDialog()
                    if (dpRes != null && dpRes.status) {
                        val optionList = dpRes.data.details
                        if (optionList.isNotEmpty() && optionList.size == 1) {
                            if (listOf(optionList).any { true }) {
                                val details = Details(optionList[0].displayLabel, optionList[0].value)
                                fieldInput.text = details.displayLabel
                                textVal = fieldInput.text.toString()
                                val currentFieldDetails =
                                        FieldDetails(apiKey, details.value, details.displayLabel, "")

                                if (!isEditFlow)
                                    scrollToAdditionalFieldsBottom()
                                else
                                    scrollToAdditionalFieldsBottom(currentFieldInputView)

                                addToCurrentFilledFieldData(
                                        apiKey,
                                        isMandatory,
                                        currentFieldDetails,
                                        false,
                                        sectionName,
                                        linearLayout, cFieldList, isLastSection
                                )
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
    }

    private fun validateInput(
            sectionName: String,
            fieldName: String,
            editTextVal: String,
            isMandatory: Boolean,
            lastItem: Boolean,
            regexResponse: String?,
            apiKey: String,
            displayKey: String,
            fieldView: View,
            freeText: String,
            linearLayout: LinearLayout,
            cFieldList: List<Fields>,
            isLastSection: Boolean
    ) {
        if (editTextVal.isNotEmpty()) {

            if (regexResponse != null && regexResponse.isNotEmpty()) {
                val regex = Regex(regexResponse)
                if (regex.matches(editTextVal)) {
                    val currentFieldDetails = FieldDetails(apiKey, editTextVal, displayKey, freeText)
                    if(apiKey=="RefMobile")
                    {
                        if(editTextVal != customerDetailsResponse.data?.dealerDetails?.dealerMobile &&
                                editTextVal != customerDetailsResponse.data?.basicDetails?.customerMobile)
                        {
                            if (lastItem)
                                addToCurrentFilledFieldData(
                                        fieldName,
                                        isMandatory,
                                        currentFieldDetails,
                                        true,
                                        sectionName,
                                        linearLayout, cFieldList, isLastSection
                                )
                            else
                                addToCurrentFilledFieldData(
                                        fieldName,
                                        isMandatory,
                                        currentFieldDetails,
                                        false,
                                        sectionName,
                                        linearLayout, cFieldList, isLastSection
                                )

                        }
                        else {
                            showToast("Please enter valid $fieldName")
                        }

                    }
                    else
                    {
                        if (lastItem)
                            addToCurrentFilledFieldData(
                                    fieldName,
                                    isMandatory,
                                    currentFieldDetails,
                                    true,
                                    sectionName,
                                    linearLayout, cFieldList, isLastSection
                            )
                        else
                            addToCurrentFilledFieldData(
                                    fieldName,
                                    isMandatory,
                                    currentFieldDetails,
                                    false,
                                    sectionName,
                                    linearLayout, cFieldList, isLastSection
                            )

                    }

                } else {
                    showToast("Please enter valid $fieldName")
                }

                if (apiKey == "CompanyPincode") {
                    if (!isEditFlow && sectionName != "Reference Details")
                        scrollToAdditionalFieldsBottom()
                    else {
                        scrollToAdditionalFieldsBottom(fieldView)
                    }
                }

            } else {
                val currentFieldDetails = FieldDetails(apiKey, editTextVal, displayKey, freeText)
                if (lastItem)
                    addToCurrentFilledFieldData(
                            fieldName,
                            isMandatory,
                            currentFieldDetails,
                            true,
                            sectionName,
                            linearLayout, cFieldList, isLastSection
                    )
                else
                    addToCurrentFilledFieldData(
                            fieldName,
                            isMandatory,
                            currentFieldDetails,
                            false,
                            sectionName,
                            linearLayout, cFieldList, isLastSection
                    )

            }

            if (!isEditFlow && sectionName != "Reference Details")
                scrollToAdditionalFieldsBottom()

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

    private fun addToCurrentFilledFieldData(
            fieldName: String,
            isMandatory: Boolean,
            currentFieldDetails: FieldDetails,
            isLastItem: Boolean,
            sectionName: String,
            linearLayout: LinearLayout,
            cFieldList: List<Fields>,
            isLastSection: Boolean
    ) {
        currentFilledFieldData[fieldName] = currentFieldDetails
        Log.i(
                "TAG",
                "addToCurrentFilledFieldData: " + currentFilledFieldData[fieldName]?.Value.toString()
        )
        if (isMandatory) {
            mandatoryFieldsList[currentFieldDetails.APIKey] = currentFieldDetails.Value

            if (currentFilledFieldData.size >= mandatoryFieldsList.size) {
                if (isAllMandatoryFieldsFilledInCurrentSection() && !isLastItem) {
                    //  currentSectionButton.background=resources.getDrawable(R.drawable.vtwo_next_btn_bg)
                    currentSectionButton.background.setColorFilter(
                            ContextCompat.getColor(
                                    requireContext(),
                                    R.color.vtwo_black
                            ), PorterDuff.Mode.MULTIPLY
                    );

                     /* if(currentFieldDetails.APIKey=="Company" && currentFieldDetails.FreeText.isNotEmpty())
                      {
                          refreshFieldView(sectionName, linearLayout, cFieldList, isLastSection)
                      }
  */
                }
            } else {
                currentSectionButton.background.setColorFilter(
                        ContextCompat.getColor(
                                requireContext(),
                                R.color.vtwo_pale_grey
                        ), PorterDuff.Mode.MULTIPLY
                );

                //  currentSectionButton.background=resources.getDrawable(R.drawable.v2_rounded_light_grey_bg)
                // currentSectionButton.setPadding(85,13,85,13)

            }
        }

        if (isLastItem)
            moveCurrentDetailsToMap(sectionName)
    }

    private fun moveCurrentDetailsToMap(sectionName: String) {

        submitAdditionalFieldsList.putAll(currentFilledFieldData)
        val fieldList: ArrayList<FieldDetails> =
                ArrayList<FieldDetails>(submitAdditionalFieldsList.values)

        sectionMap[sectionName] = fieldList
        currentFilledFieldData.clear()
        mandatoryFieldsList.clear()
        isEditFlow = false
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
        value =
                if (currentFilledFieldData.isNotEmpty() && currentFilledFieldData.containsKey(fieldName)) {
                    val fieldData = currentFilledFieldData.getValue(fieldName)
                    fieldData.DisplayLabel
                } else {
                    ""
                }
        return value
    }

    private fun getFreeText(fieldName: String): String {
        var value = ""
        value =
                if (currentFilledFieldData.isNotEmpty() && currentFilledFieldData.containsKey(fieldName)) {
                    val fieldData = currentFilledFieldData.getValue(fieldName)
                    fieldData.FreeText
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

    @SuppressLint("SetTextI18n")
    private fun generateEditSectionUI(sectionData: Sections, isLastSect: Boolean): View {
        var view: View = LayoutInflater.from(fragView.context)
                .inflate(R.layout.v2_edit_custom_address, linearLayoutAdditionalFieldsUILayout, false)

        when (sectionData.type) {
            "Address" -> {
                view = LayoutInflater.from(fragView.context).inflate(
                        R.layout.v2_edit_custom_address,
                        linearLayoutAdditionalFieldsUILayout,
                        false
                )
                val textViewOfficeAddressEdit =
                        view.findViewById<TextView>(R.id.textViewOfficeAddressEdit)
                val imageViewEditOfficeAddress =
                        view.findViewById<ImageView>(R.id.imageViewEditOfficeAddress)
                val textViewOfficeAddress1 =
                        view.findViewById<TextView>(R.id.textViewOfficeAddress1)
                val textViewOfficeAddress2 =
                        view.findViewById<TextView>(R.id.textViewOfficeAddress2)
                val textViewOfficeAddress3 =
                        view.findViewById<TextView>(R.id.textViewOfficeAddress3)
                textViewOfficeAddressEdit.text = sectionData.sectionName
                textViewOfficeAddress1.text =
                        isFieldFilled1(sectionData.fields[0].apiDetails.apiKey)
                (isFieldFilled1(sectionData.fields[4].apiDetails.apiKey) + "," + isFieldFilled1(
                        sectionData.fields[5].apiDetails.apiKey
                )).also { textViewOfficeAddress2.text = it }
                textViewOfficeAddress3.text =
                        isFieldFilled1(sectionData.fields[6].apiDetails.apiKey) + "," + isFieldFilled1(
                                sectionData.fields[1].apiDetails.apiKey
                        )
                imageViewEditOfficeAddress.setOnClickListener(View.OnClickListener {
                    if (sectionData.fields[1].apiDetails.apiKey == "CompanyPincode")
                        additionaFieldPinCode = ""

                    sectionMap.remove(sectionData.sectionName)
                    isEditFlow = true
                    refreshFieldView()
                })


            }
            "Standard" -> {
                view = LayoutInflater.from(fragView.context).inflate(
                        R.layout.v2_edit_dropdown_layout,
                        linearLayoutAdditionalFieldsUILayout,
                        false
                )
                val titleText = view.findViewById<TextView>(R.id.textViewLbl)
                val imageViewEdit: ImageView = view.findViewById(R.id.imageViewEditCurrentDropDown)
                val editValText: TextView = view.findViewById(R.id.textViewEditDropDown)
                titleText.text = sectionData.sectionName
                editValText.text = isFieldFilled1(sectionData.fields[0].apiDetails.apiKey)
                imageViewEdit.setOnClickListener(View.OnClickListener {
                    sectionMap.remove(sectionData.sectionName)
                    isEditFlow = true
                    refreshFieldView()
                })

            }
        }
        if (isLastSect) {
            viewEmpty.visibility = View.GONE

            if (buttonMoveToNextPage.visibility != View.VISIBLE)
                buttonMoveToNextPage.visibility = View.VISIBLE
        } else {
            viewEmpty.visibility = View.VISIBLE

            if (buttonMoveToNextPage.visibility == View.VISIBLE)
                buttonMoveToNextPage.visibility = View.GONE
        }
        return view
    }


    private fun refreshFieldView(
            sectionName: String,
            linearLayout: LinearLayout,
            cFieldList: List<Fields>,
            isLastItem: Boolean
    ) {
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
        val fieldList: ArrayList<FieldDetails> =
                ArrayList<FieldDetails>(submitAdditionalFieldsList.values)

        val fieldDataRequest = FieldData(custId, fieldList)
        val submitAdditionalFieldsRequest = SubmitAdditionalFieldRequest(
                CommonStrings.DEALER_ID,
                CommonStrings.USER_TYPE,
                fieldDataRequest
        )
        if (hasConnectivityNetwork()) {
            transactionViewModel.submitAdditionalFields(
                    submitAdditionalFieldsRequest,
                    Global.customerAPI_BaseURL + "submit-additional-details"
            )
        }
    }


    private fun showDropDownDialog(
            apiURL: String,
            title: String,
            optionList: List<Details>,
            detailsCallBack: AdditionalFieldsDetailsInterface
    ) {

        val returnDetailsCallBack: AdditionalFieldsDetailsInterface = detailsCallBack
        val dialog = Dialog(fragView.context, R.style.FullScreenDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.v2_additional_fields_drop_down_options_layout)

        val textViewTitle: TextView = dialog.findViewById(R.id.textViewSelectTitle) as TextView
        val editTextAdditionalFieldsSearchOption: EditText =
                dialog.findViewById(R.id.editTextAdditionalFieldsSearch)
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

        var reviewAdapter =
                AdditionalFieldsAdapter(apiURL, optionList, object : AdditionalFieldsDetailsInterface {
                    override fun returnDetails(details: Details) {
                        returnDetailsCallBack.returnDetails(details)
                        dialog.dismiss()
                    }
                })

        val layoutManager = LinearLayoutManager(fragView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = reviewAdapter

        editTextAdditionalFieldsSearchOption.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                    s: CharSequence, start: Int, before: Int,
                    count: Int
            ) {
                if (s != "") {
                    handler.removeCallbacks(input_finish_checker)
                }
            }

            override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int,
                    after: Int
            ) {
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
                    if (hasConnectivityNetwork()) {
                        showProgressDialog(fragView.context)

                        RetroBase.retrofitInterface.getFromWeb(apiURL + editTextAdditionalFieldsSearchOption.text.toString())
                                ?.enqueue(object : Callback<Any> {
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
        scrollViewPostOffer.post(Runnable {
            textView.top.let {
                scrollViewPostOffer.scrollTo(
                        0,
                        it
                )
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBackFromAddressAndAdditionalFields -> {
                checkBackPress()
            }
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
            R.id.buttonMoveToNextPage -> {
                if (linearLayoutEditCurrentAddress.visibility == View.VISIBLE) {
                    if (checkboxCurrentAndPermanentAddress.visibility == View.VISIBLE) {
                        if (additionalFieldsData.sections.isNotEmpty()) {
                            submitAdditionalFields()
                        } else {
                            navigateToBankOfferStatus(
                                    custId.toString(),
                                    customerDetailsResponse,
                                    "AddressAdditionalFields"
                            )
                        }
                    } else {
                        if (linearLayoutEditPermanentAddress.visibility == View.VISIBLE) {
                            if (additionalFieldsData != null && additionalFieldsData.sections.isNotEmpty()) {
                                submitAdditionalFields()
                            } else {
                                navigateToBankOfferStatus(
                                        custId.toString(),
                                        customerDetailsResponse,
                                        "AddressAdditionalFields"
                                )
                            }
                        } else
                            showToast("Please fill Permanent Address")
                    }
                } else
                    showToast("Please fill All the fields")
            }

        }
    }

}