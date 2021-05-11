package v2.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import v2.model.response.bank_offers.BankOffersData
import v2.model.response.bank_offers.BankOffersForApplicationResponse
import v2.model.response.bank_offers.SelectRecommendedBankOfferResponse
import v2.model_view.BankOffersViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.SoftOfferAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.itemClickCallBack
import kotlin.concurrent.fixedRateTimer


class SoftOfferFragment : BaseFragment() {
    private var caseID: String=""
    lateinit var bankAdapter: SoftOfferAdapter
    lateinit var customerDetailsResponse: CustomerDetailsResponse
    lateinit var tvLoanAmountVal: TextView
    lateinit var tvLoanTenureVal: TextView
    lateinit var skLoanAmount: SeekBar
    lateinit var skTenure: SeekBar
    lateinit var llBankOfferParent: LinearLayout
    lateinit var linearLayoutCalculation: LinearLayout
    lateinit var llSoftOfferDialog: LinearLayout
    lateinit var ivBackToRedDetails: ImageView
    lateinit var buttonLoanDetailsSubmit: Button
    lateinit var recyclerViewBankOffer: RecyclerView
    lateinit var bankViewModel: BankOffersViewModel

    var loanAmountMaximum: Int = 1000000
    var loanAmountMinimum: Int = 50000

    var loanTenureMaximum: Int = 15
    var loanTenureMinimum: Int = 1

    var loanAmount = ""
    var loanTenure = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            caseID="0242210316000103"
           // customerId = safeArgs.customerId
            customerId="448"
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            initViews(view)
        }
        return view
    }


    @SuppressLint("NewApi")
    private fun initViews(view: View?) {
        if (view != null) {
            ivBackToRedDetails = view.findViewById(R.id.ivBackToRedDetails)

            tvLoanAmountVal = view.findViewById(R.id.tvLoanAmountValV2)
            tvLoanTenureVal = view.findViewById(R.id.tvLoanTenureValV2)
            skLoanAmount = view.findViewById(R.id.skLoanAmount)
            skTenure = view.findViewById(R.id.skTenure)

            llBankOfferParent = view.findViewById(R.id.llBankOfferParent)
            llSoftOfferDialog = view.findViewById(R.id.llSoftOfferDialog)
            linearLayoutCalculation = view.findViewById(R.id.linearLayoutCalculation)
            recyclerViewBankOffer = view.findViewById(R.id.recyclerViewBankOffer)

            buttonLoanDetailsSubmit = view.findViewById(R.id.buttonLoanDetailsSubmit)

            skLoanAmount.min = loanAmountMinimum
            skLoanAmount.max = loanAmountMaximum


            skLoanAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                    val loanAmountVal = formatAmount(progress.toString())
                    loanAmount=progress.toString()
                    tvLoanAmountVal.text = resources.getString(R.string.rupees_symbol) + loanAmountVal

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

        skTenure.min = loanTenureMinimum
        skTenure.max = loanTenureMaximum

        ivBackToRedDetails.setOnClickListener(View.OnClickListener { activity?.onBackPressed() })

        skTenure.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                loanTenure=progress.toString()
                tvLoanTenureVal.text = "$loanTenure Years"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        buttonLoanDetailsSubmit.setOnClickListener(View.OnClickListener {
            if (loanAmount.isNotEmpty() && loanTenure.isNotEmpty() &&
                    !tvLoanAmountVal.equals(getString(R.string.rupees_symbol)) &&
                    !tvLoanTenureVal.equals(getString(R.string.v2_tenure_lbl))) {
                bankViewModel.getBankOffersForLeadApplication(getBankRequest(), Global.customer_bank_baseURL + "get-recommended-bank")
            } else {
                showToast("Please select Loan Amount and Loan Tenure")
            }
        })
        enableSoftOfferDialog()

    }

    private fun getBankRequest(): BankOffersForApplicationRequest {
        val leadApplicationData = LeadApplicationData(caseID, customerId, loanAmount.toInt(), loanTenure.toInt()*12)
        return BankOffersForApplicationRequest(leadApplicationData, CommonStrings.DEALER_ID, CommonStrings.USER_TYPE)
    }

    private fun enableSoftOfferDialog() {
        val fixedRateTimer = fixedRateTimer(name = "soft-offer",
                initialDelay = 100, period = 100) {
            llSoftOfferDialog.visibility = View.VISIBLE
            println("loading!")
        }
        try {
            Thread.sleep(3000)
        } finally {
            fixedRateTimer.cancel();
        }
        enableCalculatorLayout()

    }

    private fun enableCalculatorLayout() {

        llBankOfferParent.setBackgroundResource(R.drawable.v2_soft_offer_bg)
        llSoftOfferDialog.visibility = View.GONE
        linearLayoutCalculation.visibility = View.VISIBLE

    }

    private fun onBankResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                val bankOfferRes: BankOffersForApplicationResponse? = mApiResponse.data as BankOffersForApplicationResponse?
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

                    }
                    else
                    {
                        showToast("No Banks found")
                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
            ApiResponse.Status.ERROR -> {
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
            }
            ApiResponse.Status.SUCCESS -> {
                val bankOfferRes: SelectRecommendedBankOfferResponse? = apiResponse.data as SelectRecommendedBankOfferResponse?
                showToast(bankOfferRes?.message.toString())
                Log.i("TAG", "onBankResponse: " + bankOfferRes?.message)

            }
            ApiResponse.Status.ERROR -> {
                Log.i("SoftOfferFragment", "onBankResponse: " + ApiResponse.Status.ERROR)

            }
            else -> {
                Log.i("SoftOfferFragment", "onBankResponse: ")
            }
        }
        Log.i("TAG", "onBankResponse: " + apiResponse.status)
    }


    private fun callSetRecommendedBank(item: Any?) {

        // val bankOfferData = BankOfferData(caseID, customerId, )

        val bankId=item
        val bankOffersData=BankOfferData(caseID,customerId, bankId.toString())
        val bankOffersForApplicationRequest = SelectRecommendedBankOfferRequest(bankOffersData, CommonStrings.DEALER_ID, CommonStrings.USER_TYPE)
        bankViewModel.setSelectRecommendedBankOffer(bankOffersForApplicationRequest, Global.customer_bank_baseURL + "select-recommended-bank")
    }

}