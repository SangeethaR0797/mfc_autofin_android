package v2.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.request.CommonRequest
import v2.model.response.CommonResponse
import v2.model.response.NoticeBoardDataResponse
import v2.model.response.NoticeData
import v2.model_view.DashboardViewModel
import v2.model_view.NoticeBoardViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.NoticeRecyclerViewAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.itemClickCallBack


class NoticeBoardAndNotificationFragment : BaseFragment(), View.OnClickListener {

    lateinit var tvTitle: TextView
    lateinit var ivBack: ImageView
    lateinit var rvData: RecyclerView
    lateinit var llData: LinearLayout

    lateinit var screenType: String
    lateinit var noticeBoardViewModel: NoticeBoardViewModel
    lateinit var noticeRecyclerViewAdapter: NoticeRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noticeBoardViewModel = ViewModelProvider(this).get(NoticeBoardViewModel::class.java)

        noticeBoardViewModel.getNoticeBoardDetailsLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onNoticeBoardDetails(mApiResponse!!)
            }
        noticeBoardViewModel.getNoticeBoardActionLiveData()
            .observe(requireActivity()) { mApiResponse: ApiResponse? ->
                onNoticeBoardActionResponse(mApiResponse!!)
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvTitle = view.findViewById(R.id.tv_title)
        ivBack = view.findViewById(R.id.iv_back)
        rvData = view.findViewById(R.id.rv_data)
        llData = view.findViewById(R.id.ll_data)
        llData.visibility=View.GONE

        tvTitle.text = screenType

        ivBack.setOnClickListener(this)

        if (screenType.equals("Notice Board")) {
            noticeBoardViewModel.getNoticeBoardDetails(
                CommonRequest(
                    0, CommonStrings.DEALER_ID,
                    CommonStrings.USER_TYPE
                ),
                Global.customerAPI_BaseURL + CommonStrings.NOTICE_BOARD_DETAILS_END_POINT
            )
        } else if (screenType.equals("Notification")) {

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
            NoticeRecyclerViewAdapter(activity as Activity, list, object : itemClickCallBack {
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
                    }

                }
            })
        rvData.adapter = noticeRecyclerViewAdapter

    }


    //region observer
    private fun onNoticeBoardDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                llData.visibility=View.GONE
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()
                llData.visibility=View.VISIBLE

                val noticeBoardDataResponse: NoticeBoardDataResponse =
                    mApiResponse.data as NoticeBoardDataResponse
                setNoticeBoardData(noticeBoardDataResponse.data!!.notices as ArrayList<NoticeData>?)

            }
            ApiResponse.Status.ERROR -> {
                llData.visibility=View.GONE
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

//endregion observer
}







