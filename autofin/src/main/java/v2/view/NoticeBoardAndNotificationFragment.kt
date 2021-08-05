package v2.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.dto.AddLeadRequest
import v2.model.enum_class.ApplicationStatusEnum
import v2.model.enum_class.ScreenTypeEnum
import v2.model.request.CommonRequest
import v2.model.request.CustomerRequest
import v2.model.request.ResetCustomerJourneyDataRequest
import v2.model.request.add_lead.AddLeadData
import v2.model.request.add_lead.AddLeadVehicleDetails
import v2.model.request.add_lead.BasicDetails
import v2.model.response.*
import v2.model.response.master.KYCDocumentResponse
import v2.model_view.MasterViewModel
import v2.model_view.NoticeBoardViewModel
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.NoticeRecyclerViewAdapter
import v2.view.adapter.NotificationRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.NoticeItemClickCallBack
import v2.view.callBackInterface.itemClickCallBack


class NoticeBoardAndNotificationFragment : BaseFragment(), View.OnClickListener {

    lateinit var tvTitle: TextView
    lateinit var ivBack: ImageView
    lateinit var rvData: RecyclerView
    lateinit var llData: LinearLayout

    lateinit var screenType: String
    lateinit var noticeBoardViewModel: NoticeBoardViewModel
    lateinit var noticeRecyclerViewAdapter: NoticeRecyclerViewAdapter
    lateinit var notificationRecyclerViewAdapter: NotificationRecyclerViewAdapter

    lateinit var transactionViewModel: TransactionViewModel
    lateinit var masterViewModel: MasterViewModel

    var selectedCustomerId: Int = 0
    lateinit var cust: CustomerDetailsResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noticeBoardViewModel = ViewModelProvider(this).get(NoticeBoardViewModel::class.java)

        transactionViewModel = ViewModelProvider(this).get(
            TransactionViewModel::class.java
        )

        masterViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)


        noticeBoardViewModel.getNoticeBoardDetailsLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onNoticeBoardDetails(mApiResponse!!)
            }

        noticeBoardViewModel.getNotificationsListLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onNotificationsListResponse(mApiResponse!!)
            }

        noticeBoardViewModel.getNoticeBoardActionLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onNoticeBoardActionResponse(mApiResponse!!)
            }



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
        tvTitle = view.findViewById(R.id.tv_title)
        ivBack = view.findViewById(R.id.iv_back)
        rvData = view.findViewById(R.id.rv_data)
        llData = view.findViewById(R.id.ll_data)
        llData.visibility = View.GONE

        tvTitle.text = screenType

        ivBack.setOnClickListener(this)

        if (screenType == ScreenTypeEnum.Notice_Board.value) {
            noticeBoardViewModel.getNoticeBoardDetails(
                CommonRequest(
                    0, CommonStrings.DEALER_ID,
                    CommonStrings.USER_TYPE
                ),
                Global.customerAPI_BaseURL + CommonStrings.NOTICE_BOARD_DETAILS_END_POINT
            )
        } else if (screenType == ScreenTypeEnum.Notification.value) {
            noticeBoardViewModel.getNotificationsList(
                CommonRequest(
                    0, CommonStrings.DEALER_ID,
                    CommonStrings.USER_TYPE
                ),
                Global.customerAPI_BaseURL + CommonStrings.NOTIFICATION_DETAILS_END_POINT
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.v2_fragment_noticeboard, container, false)
        arguments?.let {
            val safeArgs = NoticeBoardAndNotificationFragmentArgs.fromBundle(it)
            screenType = safeArgs.screenType
        }
        return view
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_back -> {
                    activity?.onBackPressed()
                }
            }
        }
    }

    private fun setNoticeBoardData(list: ArrayList<NoticeData>?) {
        val layoutManager = LinearLayoutManager(activity)
        rvData.layoutManager = layoutManager

        noticeRecyclerViewAdapter =
            NoticeRecyclerViewAdapter(activity as Activity, list, object : NoticeItemClickCallBack {
                override fun itemClick(item: Any?, position: Int) {
                    var notice = item as NoticeData
                    if (notice.isNew == true) {
                        noticeBoardViewModel.noticeBoardAction(
                            CommonRequest(
                                notice.noticeBoardId, CommonStrings.DEALER_ID,
                                CommonStrings.USER_TYPE
                            ),
                            Global.customerAPI_BaseURL + CommonStrings.NOTICE_BOARD_ACTION_END_POINT
                        )
                        notice.isNew = false
                    }
                    selectedCustomerId = notice!!.customerId!!
                    callCustomerDetailsApi(selectedCustomerId)

                    noticeRecyclerViewAdapter.notifyItemChanged(position)
                }

                override fun moreClick(item: Any?, position: Int) {
                    val notice = item as NoticeData
                    if (notice.isNew == true) {
                        noticeBoardViewModel.noticeBoardAction(
                            CommonRequest(
                                notice.noticeBoardId, CommonStrings.DEALER_ID,
                                CommonStrings.USER_TYPE
                            ),
                            Global.customerAPI_BaseURL + CommonStrings.NOTICE_BOARD_ACTION_END_POINT
                        )
                        notice.isNew = false
                    }
                    selectedCustomerId = notice.customerId!!


                    noticeRecyclerViewAdapter.notifyItemChanged(position)
                }
            })
        rvData.adapter = noticeRecyclerViewAdapter

    }

    fun callCustomerDetailsApi(customerIdValue: Int) {

        transactionViewModel.getCustomerDetails(
            createCustomerDetailsRequest(customerIdValue),
            Global.customerAPI_BaseURL + CommonStrings.CUSTOMER_DETAILS_END_URL
        )

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


    private fun setNotificationData(list: List<NotificationItemData>) {
        val layoutManager = LinearLayoutManager(activity)
        rvData.layoutManager = layoutManager

        notificationRecyclerViewAdapter =
            NotificationRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
                override fun itemClick(item: Any?, position: Int) {
                    val notificationItemData = item as NotificationItemData
                    if (notificationItemData.isNew == true) {
                        noticeBoardViewModel.noticeBoardAction(
                            CommonRequest(
                                notificationItemData.notificationId, CommonStrings.DEALER_ID,
                                CommonStrings.USER_TYPE
                            ),
                            Global.customerAPI_BaseURL + CommonStrings.NOTIFICATION_ACTION_END_POINT
                        )
                        notificationItemData.isNew = false
                    }
                    notificationRecyclerViewAdapter.notifyItemChanged(position)
                }
            })
        rvData.adapter = notificationRecyclerViewAdapter

    }

    private fun checkForNextScreenAfterCustomerResponse(customerResponse: CustomerDetailsResponse?) {

        if ((customerResponse!!.data!!.status.equals(ApplicationStatusEnum.Registered.value) && customerResponse.data!!.subStatus.equals(
                ApplicationStatusEnum.Registered.value
            )) ||
            (customerResponse.data!!.status.equals(ApplicationStatusEnum.Registered.value) && customerResponse.data!!.subStatus.equals(
                ApplicationStatusEnum.Employment_Details_Submitted.value
            )) ||
            (customerResponse.data!!.status.equals(ApplicationStatusEnum.KYC_Done.value) && customerResponse.data!!.subStatus.equals(
                ApplicationStatusEnum.KYC_Done.value
            )) ||
            (customerResponse.data!!.status.equals(ApplicationStatusEnum.KYC_Done.value) && customerResponse.data!!.subStatus.equals(
                ApplicationStatusEnum.Employment_Details_Submitted.value
            ))

        ) {
            val addLeadRequest = AddLeadRequest()
            val vehicleDetails = AddLeadVehicleDetails()

            vehicleDetails.RegistrationYear =
                customerResponse.data!!.vehicleDetails!!.registrationYear
            vehicleDetails.Make = customerResponse.data!!.vehicleDetails!!.make
            vehicleDetails.Model = customerResponse.data!!.vehicleDetails!!.model
            vehicleDetails.Variant = customerResponse.data!!.vehicleDetails!!.variant
            vehicleDetails.Ownership = customerResponse.data!!.vehicleDetails!!.ownership
            vehicleDetails.VehicleNumber = customerResponse.data!!.vehicleDetails!!.vehicleNumber
            vehicleDetails.KMs = customerResponse.data!!.vehicleDetails!!.kMs
            vehicleDetails.FuelType = customerResponse.data!!.vehicleDetails!!.fuelType

            val basicDetails = BasicDetails()
            basicDetails.FirstName =
                customerResponse.data!!.basicDetails!!.firstName
            basicDetails.LastName =
                customerResponse.data!!.basicDetails!!.lastName
            basicDetails.Email =
                customerResponse.data!!.basicDetails!!.email
            basicDetails.Salutation =
                customerResponse.data!!.basicDetails!!.salutation
            basicDetails.CustomerMobile =
                customerResponse.data!!.basicDetails!!.customerMobile

            val data = AddLeadData()

            data.addLeadVehicleDetails = vehicleDetails
            data.basicDetails = basicDetails
            addLeadRequest.Data = data
            navigateToAddLeadFragment(
                addLeadRequest,
                selectedCustomerId,
                customerResponse.data!!.basicDetails!!.customerMobile
            )
        } else {
            when (customerResponse.data?.status) {
                getString(R.string.v2_lead_status_kyc_done) -> {
                    navToSoftOffer(
                        customerResponse,
                        selectedCustomerId.toString(),
                        CommonStrings.APPLICATION_LIST_FRAGMENT_TAG
                    )
                }
                getString(R.string.v2_lead_status_lender_selected) -> {
                    navigateToAddressAdditionalFields(selectedCustomerId, customerResponse,"")
                }
                getString(R.string.v2_lead_status_bank_form_filled) -> {
                    cust = customerResponse
                    masterViewModel.getKYCDocumentResponse(Global.baseURL + CommonStrings.KYC_UPLOAD_URL_END_POINT + selectedCustomerId)
                }
                getString(R.string.v2_lead_status_document_upload) -> {
                    navigateToBankOfferStatusFromApplicationListFrag(
                        selectedCustomerId,
                        customerResponse
                    )
                }
                getString(R.string.v2_lead_status_submitted_to_bank) -> {
                    /*val salutation = customerResponse.data?.basicDetails?.salutation
                    val name =
                        customerResponse.data?.basicDetails?.firstName + " " + customerResponse.data?.basicDetails?.lastName
                    val caseId = customerResponse.data?.caseId
                    caseId?.let { CustomLoanProcessCompletedData(selectedCustomerId,salutation + " " + name, it) }
                        ?.let { navigateToBankSuccessPageFromSoftOffer(it) }
*/
                    navigateToLeadDetails(selectedCustomerId)
                }
                else ->
                    navigateApplicationDetailsFragment(selectedCustomerId)
            }

        }

    }


    //region observer
    private fun onNoticeBoardDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                llData.visibility = View.GONE
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                llData.visibility = View.VISIBLE

                val noticeBoardDataResponse: NoticeBoardDataResponse =
                    mApiResponse.data as NoticeBoardDataResponse
                setNoticeBoardData(noticeBoardDataResponse.data!!.notices as ArrayList<NoticeData>?)

            }
            ApiResponse.Status.ERROR -> {
                llData.visibility = View.GONE
                hideProgressDialog()
                Log.i("SoftOfferFragment", ": " + ApiResponse.Status.ERROR)

            }
            else -> {
                Log.i("SoftOfferFragment", ": ")
            }
        }

    }

    private fun onNotificationsListResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                llData.visibility = View.GONE
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                llData.visibility = View.VISIBLE

                val notificationsListResponse: NotificationsListResponse =
                    mApiResponse.data as NotificationsListResponse
                setNotificationData(notificationsListResponse.data!!.notifications!!)

            }
            ApiResponse.Status.ERROR -> {
                llData.visibility = View.GONE
                hideProgressDialog()
                Log.i("SoftOfferFragment", ": " + ApiResponse.Status.ERROR)

            }
            else -> {
                Log.i("SoftOfferFragment", ": ")
            }
        }

    }

    private fun onNoticeBoardActionResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                //showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val commonResponse: CommonResponse = mApiResponse.data as CommonResponse

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

                checkForNextScreenAfterCustomerResponse(customerResponse)


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
                                selectedCustomerId.toString(),
                                kycDocumentRes,
                                it,
                                cust
                            )
                        }
                    else if (kycDocumentRes.data.groupedDoc.isEmpty() && kycDocumentRes.data.nonGroupedDoc.isEmpty())
                        navigateToBankOfferStatusFromApplicationListFrag(selectedCustomerId, cust)
                } else {
                    navigateToBankOfferStatusFromApplicationListFrag(selectedCustomerId, cust)
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







