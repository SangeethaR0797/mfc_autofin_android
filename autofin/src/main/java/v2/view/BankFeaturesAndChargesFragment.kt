package v2.view

import android.app.Activity
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.core.view.marginLeft
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.dto.AddLeadRequest
import v2.model.dto.DataSelectionDTO
import v2.model.dto.KeyValueDTO
import v2.model.enum_class.ApplicationStatusEnum
import v2.model.enum_class.ScreenTypeEnum
import v2.model.request.ApplicationListRequest
import v2.model.request.ApplicationListRequestData
import v2.model.request.CustomerRequest
import v2.model.request.ResetCustomerJourneyDataRequest
import v2.model.request.add_lead.AddLeadData
import v2.model.request.add_lead.AddLeadVehicleDetails
import v2.model.request.add_lead.BasicDetails
import v2.model.response.ApplicationDataItems
import v2.model.response.ApplicationListResponse
import v2.model.response.CustomerDetailsResponse
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.ApplicationListAdapter
import v2.view.adapter.KeyValueRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.ApplicationListClickCallBack
import v2.view.callBackInterface.itemClickCallBack

import java.util.*


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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


}







