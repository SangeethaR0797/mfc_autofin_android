package v2.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.*
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.enum_class.ApplicationStatusEnum
import v2.model.request.*
import v2.model.request.bank_offers.BankOfferData
import v2.model.request.bank_offers.BankOffersForApplicationRequest
import v2.model.request.bank_offers.LeadApplicationData
import v2.model.request.bank_offers.SelectRecommendedBankOfferRequest
import v2.model.response.*
import v2.model.response.bank_offers.*
import v2.model.response.master.*
import v2.model_view.BankOffersViewModel
import v2.model_view.MasterViewModel
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.*
import v2.view.base.BaseFragment
import v2.view.callBackInterface.ActivityBackPressed
import v2.view.callBackInterface.itemClickCallBack


class SoftOfferFragment : BaseFragment(), ActivityBackPressed {

    lateinit var loanAmountViewModel: MasterViewModel
    lateinit var bankViewModel: BankOffersViewModel
    lateinit var transactionViewModel: TransactionViewModel


    lateinit var fragView: View

    var initialCall: Boolean = true
    private var caseID: String = ""
    lateinit var scrollViewBankOffer: ScrollView

    lateinit var bankAdapter: SoftOfferAdapter
    lateinit var postSoftOfferAdapter: PostSoftOfferAdapter

    lateinit var customerDetailsResponse: CustomerDetailsResponse

    lateinit var ivBackToRedDetails: ImageView
    lateinit var skLoanAmount: SeekBar
    lateinit var skTenure: SeekBar

    lateinit var llBankOfferParent: LinearLayout
    lateinit var linearLayoutCalculation: LinearLayout
    lateinit var llSoftOfferDialog: LinearLayout

    lateinit var tvLoanAmountVal: TextView
    lateinit var tvLoanTenureVal: TextView
    lateinit var tvBankOfferTitleV2: TextView
    lateinit var textViewNoDataFound: TextView

    lateinit var recyclerViewBankOffer: RecyclerView


    lateinit var buttonLoanDetailsSubmit: Button

    var loanAmountDefault: Int = 0
    var loanTenureDefault: Int = 0
    var loanAmountMaximum: Int = 0
    var loanTenureMaximum: Int = 0

    private var loanAmountMinimum: Int = 0
    private var loanTenureMinimum: Int = 0

    var loanAmount = ""
    var loanTenure = ""

    var leadStatus: String? = null
    var leadSubStatus: String? = null

    override fun onActivityBackPressed() {
        checkBackPress()
    }

    fun checkBackPress() {
        if ((leadStatus.equals(ApplicationStatusEnum.Registered.value) && leadSubStatus.equals(
                ApplicationStatusEnum.Registered.value
            )) ||
            (leadStatus.equals(ApplicationStatusEnum.Registered.value) && leadSubStatus.equals(
                ApplicationStatusEnum.Employment_Details_Submitted.value
            )) ||
            (leadStatus.equals(ApplicationStatusEnum.KYC_Done.value) && leadSubStatus.equals(
                ApplicationStatusEnum.KYC_Done.value
            )) ||
            (leadStatus.equals(ApplicationStatusEnum.KYC_Done.value) && leadSubStatus.equals(
                ApplicationStatusEnum.Employment_Details_Submitted.value
            ))

        ) {
            //Case 1 Allow to change basic details here bank has not selected
            //Case 2 Edit flow user can go back to Application List Page
            if (activity is HostActivity) {
                (activity as HostActivity).activityBackPressed = null
            }
            activity?.onBackPressed()
        } else {
            //Not Able to change mvvm details after Add Lead so User can go back on VehicleSelectionFrag only
            navigateDashboardTop()
        }

    }

    override fun onResume() {
        super.onResume()
        if (activity is HostActivity) {
            (activity as HostActivity).activityBackPressed = AddLeadDetailsFrag@ this
        }
    }

    override fun onStop() {
        super.onStop()
        if (activity is HostActivity) {
            (activity as HostActivity).activityBackPressed = null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val safeArgs = SoftOfferFragmentArgs.fromBundle(it)
            customerDetailsResponse = safeArgs.CustomerDetails
            leadStatus = customerDetailsResponse.data?.status
            leadSubStatus = customerDetailsResponse.data?.subStatus

            caseID = customerDetailsResponse.data?.caseId.toString()
            //caseID = "0242210316000103"
            customerId = safeArgs.customerId

            //customerId = "448"
        }

        // region Loan-MasterViewModel

        loanAmountViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)

        bankViewModel = ViewModelProvider(this).get(
            BankOffersViewModel::class.java
        )

        transactionViewModel = ViewModelProvider(this).get(
            TransactionViewModel::class.java
        )

        loanAmountViewModel.getBankOfferLoanLiveData().observe(this) { mApiResponse: ApiResponse? ->
            onLoanAmountResponse(mApiResponse!!)
        }

        //endregion Loan-MasterViewModel


        bankViewModel.getBankOffersForLeadApplicationLiveData()
            .observe(this) { mApiResponse: ApiResponse? ->
                onBankResponse(
                    mApiResponse!!
                )
            }

        bankViewModel.getSetSelectRecommendedBankOfferLiveData()
            .observe(this) { mApiResponse: ApiResponse? ->
                onSetBankRes(
                    mApiResponse!!
                )
            }

        transactionViewModel.getCustomerDetailsLiveData()
            .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                onCustomerDetails(
                    mApiResponse!!
                )
            })


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.v2_soft_offer_loading_fragment, container, false)
        fragView = view
        initViews()

        return view
    }


    @SuppressLint("NewApi")
    private fun initViews() {
        if (fragView != null) {
            ivBackToRedDetails = fragView.findViewById(R.id.ivBackToRedDetails)
            scrollViewBankOffer = fragView.findViewById(R.id.scrollViewBankOffer)

            llBankOfferParent = fragView.findViewById(R.id.llBankOfferParent)
            llSoftOfferDialog = fragView.findViewById(R.id.llSoftOfferDialog)
            linearLayoutCalculation = fragView.findViewById(R.id.linearLayoutCalculation)

            tvLoanAmountVal = fragView.findViewById(R.id.tvLoanAmountValV2)
            tvLoanTenureVal = fragView.findViewById(R.id.tvLoanTenureValV2)
            tvBankOfferTitleV2 = fragView.findViewById(R.id.tvBankOfferTitleV2)
            textViewNoDataFound = fragView.findViewById(R.id.textViewNoDataFound)

            skLoanAmount = fragView.findViewById(R.id.skLoanAmount)
            skTenure = fragView.findViewById(R.id.skTenure)

            buttonLoanDetailsSubmit = fragView.findViewById(R.id.buttonLoanDetailsSubmit)

            recyclerViewBankOffer = fragView.findViewById(R.id.recyclerViewBankOffer)

            // region ChangeAndClickListeners

            skLoanAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                    val rounded: Int = (progress + 9999) / 10000 * 10000
                    val loanAmountVal = formatAmount(rounded.toString())

                    loanAmount = rounded.toString()
                    if (rounded <= 10000) {
                        tvLoanAmountVal.text =
                            resources.getString(R.string.rupees_symbol) + formatAmount("50000")
                        loanAmount = "50000"
                    } else
                        tvLoanAmountVal.text =
                            resources.getString(R.string.rupees_symbol) + loanAmountVal


                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

        skTenure.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                loanTenure = (progress).toString()
                tvLoanTenureVal.text = "$progress Years"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


        ivBackToRedDetails.setOnClickListener(OnClickListener { checkBackPress() })

        buttonLoanDetailsSubmit.setOnClickListener(OnClickListener {
            if (loanAmount.isNotEmpty() && loanTenure.isNotEmpty() &&
                !tvLoanAmountVal.equals(getString(R.string.rupees_symbol)) &&
                !tvLoanTenureVal.equals(getString(R.string.v2_tenure_lbl))
            ) {
                initialCall = false
                if (loanAmount.toInt() >= loanAmountMinimum && loanTenure.toInt() >= loanTenureMinimum) {
                    if (hasConnectivityNetwork()) {
                        bankViewModel.getBankOffersForLeadApplication(
                            getBankRequest(),
                            Global.customer_bank_baseURL + "get-recommended-bank"
                        )
                    }
                } else
                    showToast("Please select Loan Amount Minimum " + loanAmountMinimum + "and Loan Tenure Minimum " + loanTenureMinimum)

            } else {
                showToast("Please select Loan Amount and Loan Tenure")
            }
        })

        // endregion ChangeAndClickListeners
        loanAmountViewModel.getBankOffersLoanAmount(Global.customerAPI_Master_URL + CommonStrings.LOAN_AMOUNT_URL + customerId)

        // checkLeadStatus()
    }


    // region Response

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
                    leadStatus = customerDetailsResponse.data!!.status
                    leadSubStatus = customerDetailsResponse.data!!.subStatus
                    navigateToAddressAndAdditionalFieldsFragment(
                        customerId.toInt(),
                        customerDetailsResponse
                    )

                }
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


    @RequiresApi(Build.VERSION_CODES.O)
    private fun onLoanAmountResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val loanAmountResponse: LoanAmountResponse? =
                    mApiResponse.data as LoanAmountResponse?
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

                    bankViewModel.getBankOffersForLeadApplication(
                        getBankRequest(),
                        Global.customer_bank_baseURL + "get-recommended-bank"
                    )

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

                val bankOfferRes: BankOffersForApplicationResponse? =
                    mApiResponse.data as BankOffersForApplicationResponse?
                try {
                    if (bankOfferRes?.data?.isNotEmpty() == true) {

                        enableCalculatorLayout()
                        setData()

                        // val bankOffersData: List<BankOffersData>? = bankOfferRes?.data as List<BankOffersData>

                        val bankOffersData = arrayListOf<BankOffersData>()
                        bankOfferRes.data!!.forEachIndexed { index, bank ->
                            //if (bank.bankName == "Bajaj Finser" || bank.bankName == "Bajaj Finserv")
                            if (bank.bankName.contains("HDFC"))//("IDFC") ) //== "HDFC Bank" , "ICICI Bank" , "Bajaj Finser"
                            {
                                bankOffersData.add(bank)
                            }
                        }


                        tvBankOfferTitleV2.text =
                                resources.getString(R.string.v2_bank_offers) + " " + bankOfferRes?.data?.size
                        bankAdapter = SoftOfferAdapter(
                                activity as Activity,
                                bankOffersData,
                                object : itemClickCallBack {
                                    override fun itemClick(item: Any?, position: Int) {
                                        callSetRecommendedBank(item)
                                    }
                                })
                        val layoutManager = LinearLayoutManager(activity)
                        recyclerViewBankOffer.layoutManager = layoutManager

                        this.recyclerViewBankOffer.adapter = bankAdapter
                        setFocusOnView(tvBankOfferTitleV2)

                        if (bankOffersData.size <= 0) {
                            textViewNoDataFound.visibility = View.VISIBLE
                            llBankOfferParent.setBackgroundResource(R.drawable.v2_soft_offer_bg)
                            llSoftOfferDialog.visibility = View.GONE
                            linearLayoutCalculation.visibility = View.VISIBLE
                            tvBankOfferTitleV2.visibility = View.GONE
                            recyclerViewBankOffer.visibility = View.GONE

                            setData()
                            //  showToast("No Bank Offers found!")
                            setFocusOnView(tvBankOfferTitleV2)
                        }


                    } else {

                        textViewNoDataFound.visibility = View.VISIBLE
                        llBankOfferParent.setBackgroundResource(R.drawable.v2_soft_offer_bg)
                        llSoftOfferDialog.visibility = View.GONE
                        linearLayoutCalculation.visibility = View.VISIBLE
                        tvBankOfferTitleV2.visibility = View.GONE
                        recyclerViewBankOffer.visibility = View.GONE

                        setData()
                        //  showToast("No Bank Offers found!")
                        setFocusOnView(tvBankOfferTitleV2)

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
                val bankOfferRes: SelectRecommendedBankOfferResponse? =
                    apiResponse.data as SelectRecommendedBankOfferResponse?
                if (bankOfferRes?.status == true) {
                    Log.i("TAG", "onBankResponse: " + bankOfferRes?.message)
                    transactionViewModel.getCustomerDetails(
                        createCustomerDetailsRequest(customerId.toInt()),
                        Global.customerAPI_BaseURL + CommonStrings.CUSTOMER_DETAILS_END_URL
                    )
                } else {
                    showToast(bankOfferRes?.message.toString())
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
        Log.i("TAG", "onBankResponse: " + apiResponse.status)
    }


    // endregion Response

    private fun getBankRequest(): BankOffersForApplicationRequest {
        val leadApplicationData =
            LeadApplicationData(caseID, customerId, loanAmount.toInt(), loanTenure.toInt() * 12)
        return BankOffersForApplicationRequest(
            leadApplicationData,
            CommonStrings.DEALER_ID,
            CommonStrings.USER_TYPE
        )
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
    private fun setData() {

        if (initialCall) {

            tvLoanTenureVal.text = (loanTenureDefault).toString() + " Years"
            skLoanAmount.max = loanAmountMaximum

            if (loanAmountDefault != 1000) {
                tvLoanAmountVal.text =
                    resources.getString(R.string.rupees_symbol) + loanAmountDefault
                skLoanAmount.progress = loanAmountDefault
            } else {
                tvLoanAmountVal.text = resources.getString(R.string.rupees_symbol) + 0
                skLoanAmount.progress = 100000

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                skLoanAmount.min = loanAmountMinimum
                skTenure.min = loanTenureMinimum

            }


            skTenure.progress = loanTenureDefault
            skTenure.max = loanTenureMaximum

        } else {
            loanAmountDefault = loanAmount.toInt()
            loanTenureDefault = loanTenure.toInt()

            if (loanAmountDefault != 1000) {
                tvLoanAmountVal.text =
                    resources.getString(R.string.rupees_symbol) + loanAmountDefault
                skLoanAmount.progress = loanAmountDefault
            } else {
                tvLoanAmountVal.text = resources.getString(R.string.rupees_symbol) + 0
                skLoanAmount.progress = 100000

            }

            tvLoanTenureVal.text = (loanTenureDefault).toString() + " Years"

            skLoanAmount.max = loanAmountMaximum

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                skLoanAmount.min = loanAmountMinimum
                skTenure.min = loanTenureMinimum

            }

            skTenure.progress = loanTenureDefault
            skTenure.max = loanTenureMaximum

        }

    }

    private fun callSetRecommendedBank(item: Any?) {
        if (hasConnectivityNetwork()) {
            val bankOffersData = BankOfferData(caseID, customerId, item.toString())
            val bankOffersForApplicationRequest = SelectRecommendedBankOfferRequest(
                bankOffersData,
                CommonStrings.DEALER_ID,
                CommonStrings.USER_TYPE
            )
            bankViewModel.setSelectRecommendedBankOffer(
                bankOffersForApplicationRequest,
                Global.customer_bank_baseURL + "select-recommended-bank"
            )
        }
    }

    private fun setFocusOnView(textView: TextView) {
        scrollViewBankOffer.post(Runnable {
            textView.top.let {
                scrollViewBankOffer.scrollTo(
                    0,
                    it
                )
            }
        })
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


}

