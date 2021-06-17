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


class ApplicationDetailsFragment : BaseFragment(), View.OnClickListener {


    lateinit var tvTitle: TextView
    lateinit var tvSubTitle: TextView
    lateinit var ivBack: ImageView
    lateinit var rvData: RecyclerView
    lateinit var llData: LinearLayout
    lateinit var tvStatus: TextView
    lateinit var tvViewAll: TextView
    lateinit var rlCall: RelativeLayout
    lateinit var btnComplete: Button
    lateinit var transactionViewModel: TransactionViewModel

    var rootView: View? = null
    var customerResponse: CustomerDetailsResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionViewModel = ViewModelProvider(this).get(
            TransactionViewModel::class.java
        )


        transactionViewModel.getCustomerDetailsLiveData()
            .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                onCustomerDetails(
                    mApiResponse!!
                )
            })

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
            rootView = inflater.inflate(R.layout.v2_fragment_application_details, container, false)
            arguments?.let {
                val safeArgs = ApplicationDetailsFragmentArgs.fromBundle(it)
                customerId = safeArgs.customerId.toString()
            }
            initializationOfObject()
        }
        return rootView
    }

    fun initializationOfObject() {
        tvTitle = rootView!!.findViewById(R.id.tv_title)
        tvSubTitle = rootView!!.findViewById(R.id.tv_sub_title)
        tvStatus = rootView!!.findViewById(R.id.tv_status)

        ivBack = rootView!!.findViewById(R.id.iv_back)

        rvData = rootView!!.findViewById(R.id.rv_data)
        llData = rootView!!.findViewById(R.id.ll_data)
        rlCall = rootView!!.findViewById(R.id.rl_call)
        btnComplete = rootView!!.findViewById(R.id.btn_complete)
        tvViewAll = rootView!!.findViewById(R.id.tv_view_all)

        ivBack.setOnClickListener(this)
        rlCall.setOnClickListener(this)
        btnComplete.setOnClickListener(this)
        tvViewAll.setOnClickListener(this)
        callCustomerDetailsApi(customerId.toInt())
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_back -> {
                    activity?.onBackPressed()
                }
                R.id.rl_call -> {

                }
                R.id.btn_complete -> {

                }
                R.id.tv_view_all -> {
                    activity?.onBackPressed()
                }

            }
        }
    }

    fun createCustomerDetailsRequest(customerId: Int): CustomerRequest {
        var customerDetailsRequest = CustomerRequest()
        customerDetailsRequest.UserId = CommonStrings.DEALER_ID
        customerDetailsRequest.UserType = CommonStrings.USER_TYPE
        var customerJourneyDataRequest = ResetCustomerJourneyDataRequest();
        customerJourneyDataRequest.CustomerId = customerId.toString()
        customerDetailsRequest.Data = customerJourneyDataRequest
        return customerDetailsRequest
    }

    fun callCustomerDetailsApi(customerIdValue: Int) {

        transactionViewModel.getCustomerDetails(
            createCustomerDetailsRequest(customerIdValue),
            Global.customerAPI_BaseURL + CommonStrings.CUSTOMER_DETAILS_END_URL
        )

    }

    private fun setCustomerData() {
        if (customerResponse != null) {
            tvTitle.text =
                customerResponse!!.data!!.basicDetails!!.firstName + " " + customerResponse!!.data!!.basicDetails!!.lastName
            tvStatus.text = customerResponse!!.data!!.status
            tvSubTitle.text =
                customerResponse!!.data!!.vehicleDetails!!.make + customerResponse!!.data!!.vehicleDetails!!.model
            val list: ArrayList<KeyValueDTO> = arrayListOf<KeyValueDTO>()
            list.add(KeyValueDTO("Case id", customerResponse!!.data!!.caseId))
            list.add(
                KeyValueDTO(
                    "Name",
                    customerResponse!!.data!!.basicDetails!!.firstName + " " + customerResponse!!.data!!.basicDetails!!.lastName
                )
            )
            list.add(
                KeyValueDTO(
                    "Mobile No",
                    customerResponse!!.data!!.basicDetails!!.customerMobile
                )
            )
            list.add(KeyValueDTO("Email", customerResponse!!.data!!.basicDetails!!.email))

            list.add(KeyValueDTO("Make", customerResponse!!.data!!.vehicleDetails!!.make))
            list.add(KeyValueDTO("Model", customerResponse!!.data!!.vehicleDetails!!.model))
            list.add(KeyValueDTO("Variant", customerResponse!!.data!!.vehicleDetails!!.variant))
            list.add(
                KeyValueDTO(
                    "Year",
                    customerResponse!!.data!!.vehicleDetails!!.registrationYear.toString()
                )
            )
            list.add(
                KeyValueDTO(
                    "Owner",
                    customerResponse!!.data!!.vehicleDetails!!.ownership.toString()
                )
            )
            if (customerResponse!!.data!!.employmentDetails!!.salaryAccount != null) {
                list.add(
                    KeyValueDTO(
                        "Owner",
                        customerResponse!!.data!!.employmentDetails!!.salaryAccount
                    )
                )
            } else if (customerResponse!!.data!!.employmentDetails!!.primaryAccount != null) {
                list.add(
                    KeyValueDTO(
                        "Owner",
                        customerResponse!!.data!!.employmentDetails!!.primaryAccount
                    )
                )
            }

            if (customerResponse!!.data!!.loanDetails != null && customerResponse!!.data!!.loanDetails!!.loanAmount != null) {
                list.add(
                    KeyValueDTO(
                        "Loan Amount",
                        formatAmount(customerResponse!!.data!!.loanDetails!!.loanAmount!!)
                    )
                )
            }
            var keyValueRecyclerViewAdapter =
                KeyValueRecyclerViewAdapter(activity as Activity, list, object :
                    itemClickCallBack {
                    override fun itemClick(item: Any?, position: Int) {


                    }
                })
            var layoutManager = LinearLayoutManager(activity)
            rvData.layoutManager = layoutManager
            rvData.adapter = keyValueRecyclerViewAdapter
        }

    }

    //region observer
    private fun onCustomerDetails(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                customerResponse = mApiResponse.data as CustomerDetailsResponse?
                setCustomerData()


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


    //endregion observer


}







