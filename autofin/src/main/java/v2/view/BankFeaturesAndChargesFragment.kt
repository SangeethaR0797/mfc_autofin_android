package v2.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.response.BankFeaturesAndChargesResponse
import v2.model.response.RuleEngineBankData
import v2.model_view.DashboardViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.PartnerBankRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.itemClickCallBack


class BankFeaturesAndChargesFragment : BaseFragment(), View.OnClickListener {


    lateinit var tvTitle: TextView
    lateinit var tvHeader: TextView
    lateinit var tvDescriptions: TextView
    lateinit var tvFeaturesTitle: TextView
    lateinit var tvChargesTitle: TextView

    lateinit var ivBack: ImageView

    lateinit var llNext: LinearLayout
    lateinit var llFeaturesData: LinearLayout
    lateinit var llChargesData: LinearLayout
    lateinit var llParnerBankData: LinearLayout

    lateinit var rvFeaturesData: RecyclerView
    lateinit var rvChargesData: RecyclerView
    lateinit var rvPartnerBankData: RecyclerView

    var rootView: View? = null
    var bankId: Int = 0
    var bankName: String? = null

    lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        dashboardViewModel.getBankFeaturesAndChargesDetailsLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onBankFeaturesAndChargesDetails(mApiResponse!!)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView != null) {

            return rootView

        } else {
            rootView = inflater.inflate(R.layout.v2_bank_features_and_chgarger, container, false)
            arguments?.let {
                val safeArgs = BankFeaturesAndChargesFragmentArgs.fromBundle(it)
                bankName = safeArgs.bankName
                bankId = safeArgs.bankId
            }
            initializationOfObject()
        }
        return rootView
    }

    fun initializationOfObject() {
        tvTitle = rootView!!.findViewById(R.id.tv_title)
        ivBack = rootView!!.findViewById(R.id.iv_back)
        tvHeader = rootView!!.findViewById(R.id.tv_header)
        tvDescriptions = rootView!!.findViewById(R.id.tv_descriptions)



        llNext = rootView!!.findViewById(R.id.ll_next)

        llFeaturesData = rootView!!.findViewById(R.id.ll_features_data)
        tvFeaturesTitle = rootView!!.findViewById(R.id.tv_features_title)
        rvFeaturesData = rootView!!.findViewById(R.id.rv_features_data)

        llChargesData = rootView!!.findViewById(R.id.ll_charges_data)
        tvChargesTitle = rootView!!.findViewById(R.id.tv_charges_title)
        rvChargesData = rootView!!.findViewById(R.id.rv_charges_data)

        llParnerBankData = rootView!!.findViewById(R.id.ll_banking_partner_data)
        rvPartnerBankData = rootView!!.findViewById(R.id.rv_banking_partner_data)


        ivBack.setOnClickListener(this)

        llParnerBankData.visibility = View.GONE
        llChargesData.visibility = View.GONE
        llFeaturesData.visibility = View.GONE
        llNext.visibility = View.GONE

        tvTitle.text = bankName
        tvFeaturesTitle.text = tvFeaturesTitle.text.toString() + " " + bankName
        tvChargesTitle.text = tvChargesTitle.text.toString() + " " + bankName

        dashboardViewModel.getBankFeaturesAndChargesDetails(
            Global.baseURL + CommonStrings.GET_BANKS_FEATURES_AND_CHARGES_END_POINT + bankId
        )
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_back -> {
                    activity?.onBackPressed()
                }
                R.id.ll_next -> {

                }

            }
        }
    }

    private fun setPartnerBanksDetails(otherPartners: List<RuleEngineBankData>) {
        if (otherPartners != null && otherPartners!!.size > 0) {
            llParnerBankData.visibility = View.VISIBLE
            val layoutManager = LinearLayoutManager(activity)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rvPartnerBankData.layoutManager = layoutManager

            var partnerBankRecyclerViewAdapter =
                PartnerBankRecyclerViewAdapter(
                    activity as Activity,
                    otherPartners,
                    object : itemClickCallBack {
                        override fun itemClick(item: Any?, position: Int) {
                            var ruleEngineBankData = item as RuleEngineBankData
                            navigateBankFeaturesAndChargesFragment(
                                ruleEngineBankData.bankName!!,
                                ruleEngineBankData!!.id!!
                            )

                        }
                    })
            rvPartnerBankData.adapter = partnerBankRecyclerViewAdapter
        }
    }

    //region observer
    private fun onBankFeaturesAndChargesDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val bankFeaturesAndChargesResponse: BankFeaturesAndChargesResponse =
                    mApiResponse.data as BankFeaturesAndChargesResponse
                if (bankFeaturesAndChargesResponse != null && bankFeaturesAndChargesResponse.data != null && bankFeaturesAndChargesResponse!!.data!!.otherPartners != null) {
                    setPartnerBanksDetails(bankFeaturesAndChargesResponse!!.data!!.otherPartners!!)
                }


            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()


            }
            else -> {

            }
        }

    }
//endregion observer

}







