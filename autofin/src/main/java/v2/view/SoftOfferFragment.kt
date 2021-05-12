package v2.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.request.bank_offers.BankOfferData
import v2.model.request.bank_offers.BankOffersForApplicationRequest
import v2.model.request.bank_offers.LeadApplicationData
import v2.model.request.bank_offers.SelectRecommendedBankOfferRequest
import v2.model.response.CustomerDetailsResponse
import v2.model.response.bank_offers.*
import v2.model_view.BankOffersViewModel
import v2.model_view.MasterViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.SoftOfferAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.itemClickCallBack


class SoftOfferFragment : BaseFragment() {
    private var caseID: String = ""
    lateinit var bankAdapter: SoftOfferAdapter
    lateinit var customerDetailsResponse: CustomerDetailsResponse
    lateinit var tvLoanAmountVal: TextView
    lateinit var tvLoanTenureVal: TextView
    lateinit var tvBankOfferTitleV2: TextView
    lateinit var ivBackToRedDetails: ImageView
    lateinit var skLoanAmount: SeekBar
    lateinit var skTenure: SeekBar
    lateinit var llBankOfferParent: LinearLayout
    lateinit var linearLayoutCalculation: LinearLayout
    lateinit var llSoftOfferDialog: LinearLayout
    lateinit var buttonLoanDetailsSubmit: Button
    lateinit var recyclerViewBankOffer: RecyclerView

    lateinit var loanAmountViewModel: MasterViewModel
    lateinit var bankViewModel: BankOffersViewModel

    var loanAmountDefault: Int = 0
    var loanTenureDefault: Int = 0

    var loanAmountMaximum: Int = 0
    var loanTenureMaximum: Int = 0

    private var loanAmountMinimum: Int = 0
    private var loanTenureMinimum: Int = 0

    var loanAmount = ""
    var loanTenure = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // region Loan-MasterViewModel

        loanAmountViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)

        loanAmountViewModel.getBankOfferLoanLiveData().observe(this, { mApiResponse: ApiResponse? ->
            onLoanAmountResponse(mApiResponse!!)
        })

        //endRegion Loan-MasterViewModel

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

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.v2_soft_offer_loading_fragment, container, false)
        arguments?.let {
            val safeArgs = SoftOfferFragmentArgs.fromBundle(it)
            customerDetailsResponse = safeArgs.CustomerDetails
            // caseID= customerDetailsResponse.data?.caseId.toString()
            caseID = "0242210316000103"
            // customerId = safeArgs.customerId
            customerId = "448"
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            initViews(view)
        }
        return view
    }


    @SuppressLint("NewApi")
    private fun initViews(view: View?) {
        if (view != null) {

            tvLoanAmountVal = view.findViewById(R.id.tvLoanAmountValV2)
            tvLoanTenureVal = view.findViewById(R.id.tvLoanTenureValV2)
            tvBankOfferTitleV2 = view.findViewById(R.id.tvBankOfferTitleV2)

            skLoanAmount = view.findViewById(R.id.skLoanAmount)
            skTenure = view.findViewById(R.id.skTenure)

            llBankOfferParent = view.findViewById(R.id.llBankOfferParent)
            llSoftOfferDialog = view.findViewById(R.id.llSoftOfferDialog)
            linearLayoutCalculation = view.findViewById(R.id.linearLayoutCalculation)
            recyclerViewBankOffer = view.findViewById(R.id.recyclerViewBankOffer)

            buttonLoanDetailsSubmit = view.findViewById(R.id.buttonLoanDetailsSubmit)

            loanAmountViewModel.getBankOffersLoanAmount(Global.customerAPI_Master_URL + CommonStrings.LOAN_AMOUNT_URL + customerId)

            skLoanAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                    val rounded: Int = (progress + 999) / 1000 * 100
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

        ivBackToRedDetails.setOnClickListener(View.OnClickListener { activity?.onBackPressed() })

        buttonLoanDetailsSubmit.setOnClickListener(View.OnClickListener {
            if (loanAmount.isNotEmpty() && loanTenure.isNotEmpty() &&
                    !tvLoanAmountVal.equals(getString(R.string.rupees_symbol)) &&
                    !tvLoanTenureVal.equals(getString(R.string.v2_tenure_lbl))) {
                bankViewModel.getBankOffersForLeadApplication(getBankRequest(), Global.customer_bank_baseURL + "get-recommended-bank")
            } else {
                showToast("Please select Loan Amount and Loan Tenure")
            }
        })

    }

    private fun getBankRequest(): BankOffersForApplicationRequest {
        val leadApplicationData = LeadApplicationData(caseID, customerId, loanAmount.toInt(), loanTenure.toInt() * 12)
        return BankOffersForApplicationRequest(leadApplicationData, CommonStrings.DEALER_ID, CommonStrings.USER_TYPE)
    }


    private fun enableCalculatorLayout() {

        llBankOfferParent.setBackgroundResource(R.drawable.v2_soft_offer_bg)
        llSoftOfferDialog.visibility = View.GONE
        linearLayoutCalculation.visibility = View.VISIBLE

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

                    enableCalculatorLayout()
                    setData()

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

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {

        tvLoanAmountVal.text = resources.getString(R.string.rupees_symbol) + loanAmountDefault
        tvLoanTenureVal.text = "$loanTenureDefault Years"

        skLoanAmount.max = loanAmountMaximum
        skLoanAmount.progress=loanAmountDefault
        skLoanAmount.min=loanAmountMinimum

        skTenure.progress = loanTenureDefault
        skTenure.min = loanTenureMinimum
        skTenure.max = loanTenureMaximum


    }


    private fun onBankResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                val bankOfferRes: BankOffersForApplicationResponse? = mApiResponse.data as BankOffersForApplicationResponse?
                tvBankOfferTitleV2.visibility = View.VISIBLE
                recyclerViewBankOffer.visibility = View.VISIBLE
                try {
                    if (bankOfferRes?.data?.isNotEmpty() == true) {
                        val bankOffersData: List<BankOffersData>? = bankOfferRes?.data as List<BankOffersData>
                        bankAdapter = SoftOfferAdapter(activity as Activity, bankOffersData!!, object : itemClickCallBack {
                            override fun itemClick(item: Any?, position: Int) {
                                callSetRecommendedBank(item)
                            }

                        })
                        val layoutManager = LinearLayoutManager(activity)
                        recyclerViewBankOffer.layoutManager = layoutManager

                        this.recyclerViewBankOffer.adapter = bankAdapter

                    } else {
                        showToast("No Banks found")
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

    // endregion Response

    private fun callSetRecommendedBank(item: Any?) {

        // val bankOfferData = BankOfferData(caseID, customerId, )

        val bankOffersData = BankOfferData(caseID, customerId, item.toString())
        val bankOffersForApplicationRequest = SelectRecommendedBankOfferRequest(bankOffersData, CommonStrings.DEALER_ID, CommonStrings.USER_TYPE)
        bankViewModel.setSelectRecommendedBankOffer(bankOffersForApplicationRequest, Global.customer_bank_baseURL + "select-recommended-bank")
    }

}