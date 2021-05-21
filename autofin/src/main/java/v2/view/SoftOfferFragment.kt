package v2.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import model.addtional_fields.SubmitAdditionalFieldData
import utility.CommonStrings
import utility.Global
import v2.model.request.*
import v2.model.request.bank_offers.BankOfferData
import v2.model.request.bank_offers.BankOffersForApplicationRequest
import v2.model.request.bank_offers.LeadApplicationData
import v2.model.request.bank_offers.SelectRecommendedBankOfferRequest
import v2.model.response.AdditionalFields
import v2.model.response.AdditionalFieldsData
import v2.model.response.CustomerDetailsResponse
import v2.model.response.SimpleResponse
import v2.model.response.bank_offers.*
import v2.model.response.master.Addres
import v2.model.response.master.PinCodeResponse
import v2.model_view.BankOffersViewModel
import v2.model_view.MasterViewModel
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.PostSoftOfferAdapter
import v2.view.adapter.SoftOfferAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.itemClickCallBack


class SoftOfferFragment : BaseFragment(), OnClickListener {
    var initialCall: Boolean = true
    private var caseID: String = ""
    lateinit var scrollViewBankOffer: ScrollView

    lateinit var bankAdapter: SoftOfferAdapter
    lateinit var postSoftOfferAdapter: PostSoftOfferAdapter

    lateinit var customerDetailsResponse: CustomerDetailsResponse

    lateinit var ivBackToRedDetails: ImageView
    lateinit var imageViewEditCurrentAddress: ImageView
    lateinit var imageViewEditPermanentAddress: ImageView
    lateinit var skLoanAmount: SeekBar
    lateinit var skTenure: SeekBar
    lateinit var checkboxCurrentAndPermanentAddress: CheckBox

    lateinit var llBankOfferParent: LinearLayout
    lateinit var linearLayoutCalculation: LinearLayout
    lateinit var llSoftOfferDialog: LinearLayout
    lateinit var linearLayoutPostOffer: LinearLayout
    lateinit var linearLayoutEquiFaxAddress: LinearLayout
    lateinit var linearLayoutAddNewCurrentAddress: LinearLayout
    lateinit var linearLayoutEditCurrentAddress: LinearLayout
    lateinit var linearLayoutAddNewPermanentAddress: LinearLayout
    lateinit var linearLayoutEditPermanentAddress: LinearLayout
    lateinit var linearLayoutPermanentAddress: LinearLayout

    lateinit var tvLoanAmountVal: TextView
    lateinit var tvLoanTenureVal: TextView
    lateinit var tvBankOfferTitleV2: TextView
    lateinit var textViewNoDataFound: TextView
    lateinit var textViewSelectBankLabel: TextView
    lateinit var textViewCurrentAddress1: TextView
    lateinit var textViewCurrentAddress2: TextView
    lateinit var textViewCurrentAddress3: TextView
    lateinit var textViewPermanentAddressEdit: TextView
    lateinit var textViewPermanentAddress1: TextView
    lateinit var textViewPermanentAddress2: TextView
    lateinit var textViewPermanentAddress3: TextView

    lateinit var recyclerViewBankOffer: RecyclerView
    lateinit var recyclerViewEquiFaxAddress: RecyclerView

    lateinit var buttonLoanDetailsSubmit: Button
    lateinit var buttonAddNewAddress: Button
    lateinit var buttonAddNewPermanentAddress: Button

    lateinit var loanAmountViewModel: MasterViewModel
    lateinit var bankViewModel: BankOffersViewModel
    lateinit var pinCodeViewModel: MasterViewModel
    lateinit var addressViewModel: TransactionViewModel
    lateinit var additionalFieldsViewModel: TransactionViewModel
    lateinit var currentAddress: CurrentAddress
    lateinit var permanentAddress: PermanentAddress
    lateinit var additionalFieldsData: AdditionalFieldsData
    lateinit var submitAdditionalFieldsData: SubmitAdditionalFieldData


    lateinit var fragView: View
    var loanAmountDefault: Int = 0
    var loanTenureDefault: Int = 0

    var loanAmountMaximum: Int = 0
    var loanTenureMaximum: Int = 0

    private var loanAmountMinimum: Int = 0
    private var loanTenureMinimum: Int = 0

    var loanAmount = ""
    var loanTenure = ""
    var address = ""
    var pincode = ""
    var typeOfAddress = ""
    var state = ""
    var city = ""
    var address1 = ""
    var address2 = ""
    var address3 = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // region Loan-MasterViewModel

        loanAmountViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)

        loanAmountViewModel.getBankOfferLoanLiveData().observe(this, { mApiResponse: ApiResponse? ->
            onLoanAmountResponse(mApiResponse!!)
        })

        pinCodeViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)
        pinCodeViewModel.getPinCodeDataLiveData().observe(this, { mApiResponse: ApiResponse? ->
            onPinCodeResponse(mApiResponse!!)
        })

        //endregion Loan-MasterViewModel


        bankViewModel = ViewModelProvider(this).get(
                BankOffersViewModel::class.java
        )
        bankViewModel.getBankOffersForLeadApplicationLiveData().observe(this, { mApiResponse: ApiResponse? ->
            onBankResponse(
                    mApiResponse!!
            )
        })

        bankViewModel.getSetSelectRecommendedBankOfferLiveData().observe(this, { mApiResponse: ApiResponse? ->
            onSetBankRes(
                    mApiResponse!!
            )
        })

        addressViewModel = ViewModelProvider(this).get(
                TransactionViewModel::class.java
        )

        addressViewModel.getUpdateAddressLiveData()
                .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                    onUpdateResponse(
                            mApiResponse!!
                    )
                })

        additionalFieldsViewModel = ViewModelProvider(this).get(
                TransactionViewModel::class.java
        )

        additionalFieldsViewModel.getAdditionalFieldsLiveData().observe(requireActivity(), { mApiResponse: ApiResponse? ->
            onAdditionalFieldsResponse(
                    mApiResponse!!
            )
        })


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.v2_soft_offer_loading_fragment, container, false)
        arguments?.let {
            val safeArgs = SoftOfferFragmentArgs.fromBundle(it)
            customerDetailsResponse = safeArgs.CustomerDetails
            caseID = customerDetailsResponse.data?.caseId.toString()
            //caseID = "0242210316000103"
            customerId = safeArgs.customerId
            //customerId = "448"
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            initViews(view)
        }
        return view
    }


    @SuppressLint("NewApi")
    private fun initViews(view: View?) {
        if (view != null) {
            fragView = view
            ivBackToRedDetails = view.findViewById(R.id.ivBackToRedDetails)
            imageViewEditCurrentAddress = view.findViewById(R.id.imageViewEditCurrentAddress)
            imageViewEditPermanentAddress = view.findViewById(R.id.imageViewEditPermanentAddress)
            scrollViewBankOffer = view.findViewById(R.id.scrollViewBankOffer)

            llBankOfferParent = view.findViewById(R.id.llBankOfferParent)
            llSoftOfferDialog = view.findViewById(R.id.llSoftOfferDialog)
            linearLayoutCalculation = view.findViewById(R.id.linearLayoutCalculation)
            linearLayoutPostOffer = view.findViewById(R.id.linearLayoutPostOffer)
            linearLayoutEquiFaxAddress = view.findViewById(R.id.linearLayoutEquiFaxAddress)
            linearLayoutAddNewCurrentAddress = view.findViewById(R.id.linearLayoutAddNewCurrentAddress)
            linearLayoutEditCurrentAddress = view.findViewById(R.id.linearLayoutEditCurrentAddress)
            linearLayoutPermanentAddress = view.findViewById(R.id.linearLayoutPermanentAddress)
            linearLayoutAddNewPermanentAddress = view.findViewById(R.id.linearLayoutAddNewPermanentAddress)
            linearLayoutEditPermanentAddress = view.findViewById(R.id.linearLayoutEditPermanentAddress)

            tvLoanAmountVal = view.findViewById(R.id.tvLoanAmountValV2)
            tvLoanTenureVal = view.findViewById(R.id.tvLoanTenureValV2)
            tvBankOfferTitleV2 = view.findViewById(R.id.tvBankOfferTitleV2)
            textViewNoDataFound = view.findViewById(R.id.textViewNoDataFound)
            textViewSelectBankLabel = view.findViewById(R.id.textViewSelectBankLabel)
            textViewCurrentAddress1 = view.findViewById(R.id.textViewCurrentAddress1)
            textViewCurrentAddress2 = view.findViewById(R.id.textViewCurrentAddress2)
            textViewCurrentAddress3 = view.findViewById(R.id.textViewCurrentAddress3)
            textViewPermanentAddressEdit = view.findViewById(R.id.textViewPermanentAddressEdit)
            textViewPermanentAddress1 = view.findViewById(R.id.textViewPermanentAddress1)
            textViewPermanentAddress2 = view.findViewById(R.id.textViewPermanentAddress2)
            textViewPermanentAddress3 = view.findViewById(R.id.textViewPermanentAddress3)

            skLoanAmount = view.findViewById(R.id.skLoanAmount)
            skTenure = view.findViewById(R.id.skTenure)

            buttonLoanDetailsSubmit = view.findViewById(R.id.buttonLoanDetailsSubmit)
            buttonAddNewAddress = view.findViewById(R.id.buttonAddNewAddress)
            buttonAddNewPermanentAddress = view.findViewById(R.id.buttonAddNewPermanentAddress)

            recyclerViewBankOffer = view.findViewById(R.id.recyclerViewBankOffer)
            recyclerViewEquiFaxAddress = view.findViewById(R.id.recyclerViewEquiFaxAddress)

            checkboxCurrentAndPermanentAddress = view.findViewById(R.id.checkboxCurrentAndPermanentAddress)
            loanAmountViewModel.getBankOffersLoanAmount(Global.customerAPI_Master_URL + CommonStrings.LOAN_AMOUNT_URL + customerId)

            // region ChangeAndClickListeners

            skLoanAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                    val rounded: Int = (progress + 999) / 1000 * 1000
                    val loanAmountVal = formatAmount(rounded.toString())

                    loanAmount = rounded.toString()
                    tvLoanAmountVal.text = resources.getString(R.string.rupees_symbol) + loanAmountVal


                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

        skTenure.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                loanTenure = progress.toString()
                tvLoanTenureVal.text = "$loanTenure Years"


            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


        ivBackToRedDetails.setOnClickListener(OnClickListener { activity?.onBackPressed() })

        buttonLoanDetailsSubmit.setOnClickListener(OnClickListener {
            if (loanAmount.isNotEmpty() && loanTenure.isNotEmpty() &&
                    !tvLoanAmountVal.equals(getString(R.string.rupees_symbol)) &&
                    !tvLoanTenureVal.equals(getString(R.string.v2_tenure_lbl))) {
                initialCall = false
                bankViewModel.getBankOffersForLeadApplication(getBankRequest(), Global.customer_bank_baseURL + "get-recommended-bank")
            } else {
                showToast("Please select Loan Amount and Loan Tenure")

            }
        })

        // endregion ChangeAndClickListeners

        buttonAddNewAddress.setOnClickListener(this)
        buttonAddNewPermanentAddress.setOnClickListener(this)

    }


    // region Response

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onLoanAmountResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val loanAmountResponse: LoanAmountResponse? = mApiResponse.data as LoanAmountResponse?
                try {
                    val loanData: LoanData = loanAmountResponse?.data as LoanData
                    loanAmountMinimum = loanData.minValues.loanAmount
                    loanTenureMinimum = loanData.minValues.tenureInMonths / 12

                    loanAmountMaximum = loanData.maxValues.loanAmount
                    loanTenureMaximum = loanData.maxValues.tenureInMonths / 12

                    loanData.defaultValues.loanAmount.also { loanAmountDefault = it }
                    loanTenureDefault = loanData.defaultValues.tenureInMonths / 12

                    loanAmount = loanAmountDefault.toString()
                    loanTenure = loanTenureDefault.toString()

                    bankViewModel.getBankOffersForLeadApplication(getBankRequest(), Global.customer_bank_baseURL + "get-recommended-bank")

                } catch (ex: Exception) {
                    ex.printStackTrace()
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


    @RequiresApi(Build.VERSION_CODES.O)
    private fun onBankResponse(mApiResponse: ApiResponse) {

        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                if (!initialCall)
                    showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                if (!initialCall)
                    hideProgressDialog()

                val bankOfferRes: BankOffersForApplicationResponse? = mApiResponse.data as BankOffersForApplicationResponse?
                try {
                    if (bankOfferRes?.data?.isNotEmpty() == true) {

                        enableCalculatorLayout()
                        setData()

                        val bankOffersData: List<BankOffersData>? = bankOfferRes?.data as List<BankOffersData>
                        bankAdapter = SoftOfferAdapter(activity as Activity, bankOffersData!!, object : itemClickCallBack {
                            override fun itemClick(item: Any?, position: Int) {
                                callSetRecommendedBank(item)
                            }

                        })
                        val layoutManager = LinearLayoutManager(activity)
                        recyclerViewBankOffer.layoutManager = layoutManager

                        this.recyclerViewBankOffer.adapter = bankAdapter
                        setFocusOnView()

                    } else {

                        //textViewNoDataFound.visibility = View.VISIBLE
                        llBankOfferParent.setBackgroundResource(R.drawable.v2_soft_offer_bg)
                        llSoftOfferDialog.visibility = View.GONE
                        linearLayoutCalculation.visibility = View.VISIBLE
                        tvBankOfferTitleV2.visibility = View.GONE
                        recyclerViewBankOffer.visibility = View.GONE

                        setData()
                        showToast("No Bank Offers found!")
                        setFocusOnView()

                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
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

        Log.i("TAG", "onBankResponse: " + mApiResponse.status)

    }

    private fun onSetBankRes(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val bankOfferRes: SelectRecommendedBankOfferResponse? = apiResponse.data as SelectRecommendedBankOfferResponse?
                showToast(bankOfferRes?.message.toString())
                enablePostOfferLayout()
                Log.i("TAG", "onBankResponse: " + bankOfferRes?.message)

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                Log.i("SoftOfferFragment", "onBankResponse: " + ApiResponse.Status.ERROR)

            }
            else -> {
                Log.i("SoftOfferFragment", "onBankResponse: ")
            }
        }
        Log.i("TAG", "onBankResponse: " + apiResponse.status)
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

                    linearLayoutAddNewPermanentAddress.visibility = View.VISIBLE
                    if (typeOfAddress == getString(R.string.v2_current_address)) {
                        linearLayoutAddNewCurrentAddress.removeAllViews()
                        addNewAddress(linearLayoutAddNewCurrentAddress, getString(R.string.v2_current_address))
                    } else if (typeOfAddress == getString(R.string.v2_permanent_address)) {
                        linearLayoutAddNewPermanentAddress.removeAllViews()
                        addNewAddress(linearLayoutAddNewPermanentAddress, getString(R.string.v2_permanent_address))
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

                showToast(response?.message.toString())
                additionalFieldsViewModel.getAdditionalFieldsData(CustomerRequest(ResetCustomerJourneyDataRequest(customerId), CommonStrings.USER_TYPE, CommonStrings.USER_TYPE), Global.baseURL + CommonStrings.ADDITIONAL_FIELDS_URL)

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
            }
            else -> {
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onAdditionalFieldsResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val response: AdditionalFields? = mApiResponse.data as AdditionalFields?
                if (response?.data != null) {
                    linearLayoutAddNewPermanentAddress.removeAllViews()
                    linearLayoutAddNewPermanentAddress.visibility = View.GONE
                    linearLayoutEditPermanentAddress.visibility = View.VISIBLE
                    additionalFieldsData = response.data

                    generateAdditionalFieldsUI()
                }
                showToast(response?.message.toString())

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
            }
            else -> {
            }
        }
    }


    // endregion Response

    private fun getBankRequest(): BankOffersForApplicationRequest {
        val leadApplicationData = LeadApplicationData(caseID, customerId, loanAmount.toInt(), loanTenure.toInt() * 12)
        return BankOffersForApplicationRequest(leadApplicationData, CommonStrings.DEALER_ID, CommonStrings.USER_TYPE)
    }


    private fun enableCalculatorLayout() {

        if (initialCall) {
            llBankOfferParent.setBackgroundResource(R.drawable.v2_soft_offer_bg)
            llSoftOfferDialog.visibility = View.GONE
            linearLayoutCalculation.visibility = View.VISIBLE
        }

        tvBankOfferTitleV2.visibility = View.VISIBLE
        recyclerViewBankOffer.visibility = View.VISIBLE
        textViewNoDataFound.visibility = View.GONE


    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {

        tvLoanAmountVal.text = resources.getString(R.string.rupees_symbol) + loanAmountDefault
        tvLoanTenureVal.text = "$loanTenureDefault Years"

        skLoanAmount.max = loanAmountMaximum
        skLoanAmount.progress = loanAmountDefault
        skLoanAmount.min = loanAmountMinimum

        skTenure.progress = loanTenureDefault
        skTenure.min = loanTenureMinimum
        skTenure.max = loanTenureMaximum

    }

    private fun callSetRecommendedBank(item: Any?) {

        // val bankOfferData = BankOfferData(caseID, customerId, )

        val bankOffersData = BankOfferData(caseID, customerId, item.toString())
        val bankOffersForApplicationRequest = SelectRecommendedBankOfferRequest(bankOffersData, CommonStrings.DEALER_ID, CommonStrings.USER_TYPE)
        bankViewModel.setSelectRecommendedBankOffer(bankOffersForApplicationRequest, Global.customer_bank_baseURL + "select-recommended-bank")
    }

    private fun setFocusOnView() {
        scrollViewBankOffer.post(Runnable { tvBankOfferTitleV2.top.let { scrollViewBankOffer.scrollTo(0, it) } })
    }

    private fun enablePostOfferLayout() {
        linearLayoutCalculation.visibility = View.GONE
        llBankOfferParent.setBackgroundResource(0)
        linearLayoutPostOffer.visibility = View.VISIBLE
        textViewSelectBankLabel.text = "You have selected " + customerDetailsResponse.data?.loanDetails?.bankName + " bank"
        val customerAddress = customerDetailsResponse.data?.equifaxFields?.address

        if (customerAddress?.isNotEmpty() == true) {
            postSoftOfferAdapter = PostSoftOfferAdapter(activity as Activity, customerAddress, object : PostSoftOfferAdapter.AddressSelectionCallBack {
                override fun onSelect(addressVal: Addres?, isPermanentAddress: Boolean) {
                    address = addressVal?.address.toString()
                    pincode = addressVal?.pincode.toString()
                    // Need to get clarity on Delimiters of Address to separate it as 3 lines
                    if(isPermanentAddress)
                        submitPermanentAddress()
                    else
                        submitCurrentAddress()
                }

            })
            val layoutManager = LinearLayoutManager(activity)
            recyclerViewEquiFaxAddress.layoutManager = layoutManager

            this.recyclerViewEquiFaxAddress.adapter = postSoftOfferAdapter
        }

        typeOfAddress = "Current Address"

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonAddNewAddress -> {
                linearLayoutEquiFaxAddress.visibility = View.GONE
                linearLayoutAddNewCurrentAddress.visibility = View.VISIBLE
                linearLayoutAddNewPermanentAddress.visibility = View.GONE
                addNewAddress(linearLayoutAddNewCurrentAddress, getString(R.string.v2_current_address))
            }

            R.id.buttonAddNewPermanentAddress -> {
                linearLayoutPermanentAddress.visibility = View.GONE
                linearLayoutAddNewCurrentAddress.visibility = View.GONE
                linearLayoutAddNewPermanentAddress.visibility = View.VISIBLE
                pincode = ""
                state = ""
                city = ""
                address1 = ""
                address2 = ""
                address3 = ""
                addNewAddress(linearLayoutAddNewPermanentAddress, getString(R.string.v2_permanent_address))
            }

        }

    }

    private fun saveCurrentAddress(item: Addres?) {

        if (typeOfAddress == "Permanent Address") {
            // save the address for Current and Permanent Address
            // And enable Office Address Layout
            val currentAddress = item?.pincode?.let { item.address?.let { it1 -> CurrentAddress(true, it, it1) } }
            val permanentAddress = item?.pincode?.let { item.address?.let { it1 -> PermanentAddress(it, it1) } }
            val addressData = currentAddress?.let { permanentAddress?.let { it1 -> AddressData(customerId.toInt(), it, it1, "2012-02-21") } }

            val updateAddressRequest = addressData?.let { UpdateAddressRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, it) }
            if (updateAddressRequest != null) {
                addressViewModel.updateAddress(updateAddressRequest, Global.customerDetails_BaseURL + CommonStrings.UPDATE_ADDRESS_URL)
            }
        } else {
            //  save address as current address and enable Permanent address Layout
            val currentAddress = item?.pincode?.let { item?.address?.let { it1 -> CurrentAddress(false, it, it1) } }
            val permanentAddress = PermanentAddress("", "")
            val addressData = AddressData(customerId.toInt(), currentAddress!!, permanentAddress, "2012-02-21")
            val updateAddressRequest = UpdateAddressRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, addressData)
            if (updateAddressRequest != null) {
                addressViewModel.updateAddress(updateAddressRequest, Global.customerDetails_BaseURL + CommonStrings.UPDATE_ADDRESS_URL)
            }
        }

    }

    private fun generateAdditionalFieldsUI() {

        val section1: View = LayoutInflater.from(context).inflate(R.layout.v2_custom_dropdown_layout, null, false)
        val textViewSectionTitle1 = section1.findViewById<TextView>(R.id.textViewSectionTitle)
        val editTextSection = section1.findViewById<TextView>(R.id.editTextSectionInput)
        textViewSectionTitle1.text = additionalFieldsData.sections[0].fields[0].label

        val section2: View = LayoutInflater.from(context).inflate(R.layout.v2_custom_dropdown_layout, null, false)
        val textViewSectionTitle2 = section2.findViewById<TextView>(R.id.textViewSectionTitle)
        val editTextSection2 = section2.findViewById<TextView>(R.id.editTextSectionInput)
        textViewSectionTitle2.text = additionalFieldsData.sections[1].fields[1].label

        llBankOfferParent.addView(section1)
        llBankOfferParent.addView(section2)

    }

    private fun addNewAddress(linearLayout: LinearLayout, title: String) {

        val addressView: View = LayoutInflater.from(fragView.context).inflate(R.layout.v2_add_new_address_layout, null, false)
        val textViewTypeOfAddress = addressView.findViewById<TextView>(R.id.textViewTypeOfAddress)
        val editTextPinCode = addressView.findViewById<EditText>(R.id.editTextPinCode)
        val buttonPinCodeCheck = addressView.findViewById<Button>(R.id.buttonPincodeCheck)
        val textViewState = addressView.findViewById<TextView>(R.id.textViewState)
        val textViewCity = addressView.findViewById<TextView>(R.id.textViewCity)
        val editTextAddress1 = addressView.findViewById<EditText>(R.id.editTextAddress1)
        val editTextAddress2 = addressView.findViewById<EditText>(R.id.editTextAddress2)
        val editTextAddress3 = addressView.findViewById<EditText>(R.id.editTextAddress3)
        val buttonSubmitAddress = addressView.findViewById<Button>(R.id.buttonSubmitAddress)

        //Orgname field need to be added here
        textViewTypeOfAddress.text = title
        typeOfAddress = title

        buttonPinCodeCheck.setOnClickListener(View.OnClickListener {
            if (editTextPinCode.text.toString().isNotEmpty() && editTextPinCode.text.toString().length == 6) {
                pinCodeViewModel.getPinCodeData(Global.customerDetails_BaseURL + "Pincode/city/" + editTextPinCode.text.toString())
            } else
                showToast("Please enter valid PinCode")
        })

        editTextPinCode.setText(pincode)
        textViewState.text = state
        textViewCity.text = city


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

                if (typeOfAddress == getString(R.string.v2_current_address)) {
                    submitCurrentAddress()
                } else if (typeOfAddress == getString(R.string.v2_permanent_address)) {
                    submitPermanentAddress()
                }

            } else {
                showToast("Please enter all Fields")
            }
        })
        linearLayout.addView(addressView)

    }


    private fun submitCurrentAddress() {
        linearLayoutEquiFaxAddress.visibility = View.GONE
        linearLayoutEditCurrentAddress.visibility = View.VISIBLE

        textViewCurrentAddress1.text = address1
        textViewCurrentAddress2.text = address2
        textViewCurrentAddress3.text = address3 + ", " + pincode


        currentAddress = CurrentAddress(false, pincode, address)
        linearLayoutAddNewCurrentAddress.visibility = View.GONE
        linearLayoutPermanentAddress.visibility = View.VISIBLE
        checkboxCurrentAndPermanentAddress.isClickable = false
        checkboxCurrentAndPermanentAddress.isFocusable = false
    }

    private fun submitPermanentAddress() {
        linearLayoutEquiFaxAddress.visibility = View.GONE
        linearLayoutEditCurrentAddress.visibility = View.VISIBLE
        linearLayoutAddNewCurrentAddress.visibility = View.GONE


        textViewPermanentAddress1.text = address1
        textViewPermanentAddress2.text = address2
        textViewPermanentAddress3.text = address3 + ", " + pincode
        permanentAddress = PermanentAddress(pincode, address)
        val addressData = AddressData(customerId.toInt(), currentAddress, permanentAddress, "2012-02-21")
        val updateAddressRequest = UpdateAddressRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, addressData)

        addressViewModel.updateAddress(updateAddressRequest, Global.customerAPI_BaseURL + CommonStrings.UPDATE_ADDRESS_URL)
    }

}