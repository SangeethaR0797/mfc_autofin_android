package v2.view

import android.app.Activity
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.core.view.marginLeft
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.dto.AddLeadRequest
import v2.model.dto.CustomLoanProcessCompletedData
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
import v2.model.response.master.KYCDocumentResponse
import v2.model_view.MasterViewModel
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
    lateinit var masterViewModel: MasterViewModel

    var rootView: View? = null
    var customerResponse: CustomerDetailsResponse? = null
    lateinit var cust: CustomerDetailsResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionViewModel = ViewModelProvider(this).get(
            TransactionViewModel::class.java
        )
        masterViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)


        transactionViewModel.getCustomerDetailsLiveData()
            .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                onCustomerDetails(
                    mApiResponse!!
                )
            })

        masterViewModel.getKYCDocumentLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onGetKYCDocumentResponse(mApiResponse!!)
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
                    checkForNextNavigation()
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

            if(customerResponse?.data?.status==getString(R.string.v2_lead_status_submitted_to_bank))
               btnComplete.visibility=View.GONE
            else
                btnComplete.visibility=View.VISIBLE

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
                        "Bank",
                        customerResponse!!.data!!.employmentDetails!!.salaryAccount
                    )
                )
            } else if (customerResponse!!.data!!.employmentDetails!!.primaryAccount != null) {
                list.add(
                    KeyValueDTO(
                        "Bank",
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

    private fun checkForNextNavigation() {

        if ((customerResponse!!.data!!.status.equals(ApplicationStatusEnum.Registered.value) && customerResponse!!.data!!.subStatus.equals(
                ApplicationStatusEnum.Registered.value
            )) ||
            (customerResponse!!.data!!.status.equals(ApplicationStatusEnum.Registered.value) && customerResponse!!.data!!.subStatus.equals(
                ApplicationStatusEnum.Employment_Details_Submitted.value
            )) ||
            (customerResponse!!.data!!.status.equals(ApplicationStatusEnum.KYC_Done.value) && customerResponse!!.data!!.subStatus.equals(
                ApplicationStatusEnum.KYC_Done.value
            )) ||
            (customerResponse!!.data!!.status.equals(ApplicationStatusEnum.KYC_Done.value) && customerResponse!!.data!!.subStatus.equals(
                ApplicationStatusEnum.Employment_Details_Submitted.value
            ))

        ) {
            var addLeadRequest = AddLeadRequest()
            var vehicleDetails = AddLeadVehicleDetails()

            vehicleDetails!!.RegistrationYear =
                customerResponse!!.data!!.vehicleDetails!!.registrationYear
            vehicleDetails!!.Make = customerResponse!!.data!!.vehicleDetails!!.make
            vehicleDetails!!.Model = customerResponse!!.data!!.vehicleDetails!!.model
            vehicleDetails!!.Variant = customerResponse!!.data!!.vehicleDetails!!.variant
            vehicleDetails!!.Ownership = customerResponse!!.data!!.vehicleDetails!!.ownership
            vehicleDetails!!.VehicleNumber =
                customerResponse!!.data!!.vehicleDetails!!.vehicleNumber
            vehicleDetails!!.KMs = customerResponse!!.data!!.vehicleDetails!!.kMs
            vehicleDetails!!.FuelType = customerResponse!!.data!!.vehicleDetails!!.fuelType

            var basicDetails = BasicDetails()
            basicDetails.FirstName =
                customerResponse!!.data!!.basicDetails!!.firstName
            basicDetails.LastName =
                customerResponse!!.data!!.basicDetails!!.lastName
            basicDetails.Email =
                customerResponse!!.data!!.basicDetails!!.email
            basicDetails.Salutation =
                customerResponse!!.data!!.basicDetails!!.salutation
            basicDetails.CustomerMobile =
                customerResponse!!.data!!.basicDetails!!.customerMobile

            var data = AddLeadData()

            data!!.addLeadVehicleDetails = vehicleDetails
            data!!.basicDetails = basicDetails
            addLeadRequest.Data = data
            navigateToAddLeadFragment(
                addLeadRequest,
                customerId.toInt(),
                customerResponse!!.data!!.basicDetails!!.customerMobile
            )
        } else {
            when (customerResponse!!.data?.status) {
                getString(R.string.v2_lead_status_kyc_done) -> {
                    navToSoftOffer(
                        customerResponse!!,
                        customerId.toString(),
                        CommonStrings.APPLICATION_LIST_FRAGMENT_TAG
                    )
                }
                getString(R.string.v2_lead_status_lender_selected) -> {
                    navigateToAddressAdditionalFields(customerId.toInt(), customerResponse!!)
                }
                getString(R.string.v2_lead_status_bank_form_filled) -> {
                    cust = customerResponse!!
                    masterViewModel.getKYCDocumentResponse(Global.baseURL + CommonStrings.KYC_UPLOAD_URL_END_POINT + customerId)
                }
                getString(R.string.v2_lead_status_document_upload) -> {
                    navigateToBankOfferStatusFromApplicationListFrag(
                        customerId.toInt(),
                        customerResponse!!
                    )
                }
                getString(R.string.v2_lead_status_submitted_to_bank) -> {
                    val salutation = customerResponse!!.data?.basicDetails?.salutation
                    val name =
                        customerResponse!!.data?.basicDetails?.firstName + " " + customerResponse!!.data?.basicDetails?.lastName
                    val caseId = customerResponse!!.data?.caseId
                    caseId?.let { CustomLoanProcessCompletedData(customerId.toInt(),salutation + " " + name, it) }
                        ?.let { navigateToBankSuccessPageFromSoftOffer(it) }
                }
            }
        }

    }

    //region observer
    private fun onCustomerDetails(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                btnComplete.visibility = View.GONE
                llData.visibility = View.GONE
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                btnComplete.visibility = View.VISIBLE
                llData.visibility = View.VISIBLE
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
                        cust.data?.caseId?.let {
                            navigateToKYCDocumentUploadFromApplicationList(
                                customerId.toString(),
                                kycDocumentRes,
                                it,
                                cust
                            )
                        }
                    else if (kycDocumentRes.data.groupedDoc.isEmpty() && kycDocumentRes.data.nonGroupedDoc.isEmpty())
                        navigateToBankOfferStatusFromApplicationListFrag(customerId.toInt(), cust)
                } else {
                    navigateToBankOfferStatusFromApplicationListFrag(customerId.toInt(), cust)
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
    //endregion observer


}







