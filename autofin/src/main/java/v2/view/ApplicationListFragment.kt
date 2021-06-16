package v2.view

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.mfc.autofin.framework.R
import v2.view.base.BaseFragment
import java.util.*
import android.widget.TextView.OnEditorActionListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import utility.CommonStrings
import utility.Global
import v2.model.request.ApplicationListRequest
import v2.model.request.ApplicationListRequestData
import v2.model.request.CommonRequest
import v2.model.response.AddLeadResponse
import v2.model.response.ApplicationListResponse
import v2.model.response.NoticeData
import v2.model_view.TransactionViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.ApplicationListAdapter
import v2.view.adapter.NoticeRecyclerViewAdapter
import v2.view.callBackInterface.ApplicationListClickCallBack
import v2.view.callBackInterface.itemClickCallBack

class ApplicationListFragment : BaseFragment(), View.OnClickListener {

    lateinit var tvTitle: TextView
    lateinit var tvResultCount: TextView
    lateinit var ivBack: ImageView
    lateinit var ivNotification: ImageView
    lateinit var ivSearch: ImageView
    lateinit var rvData: RecyclerView
    lateinit var llData: LinearLayout
    lateinit var llSearch: LinearLayout
    lateinit var llSearchSection: LinearLayout
    lateinit var viewEmptyBlack: View
    lateinit var etSearch: EditText
    lateinit var tvSearchResult: TextView

    lateinit var screenType: String
    var rootView: View? = null
    lateinit var transactionViewModel: TransactionViewModel

    var PER_PAGE: Int = 20
    var PAGE_NUMBER: Int = 1
    lateinit var applicationListAdapter: ApplicationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionViewModel = ViewModelProvider(this).get(
            TransactionViewModel::class.java
        )

        transactionViewModel.getApplicationListLiveData()
            .observe(requireActivity(), { mApiResponse: ApiResponse? ->
                onApplicationList(
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
            rootView = inflater.inflate(R.layout.v2_fragment_application_list, container, false)
            arguments?.let {
                val safeArgs = ApplicationListFragmentArgs.fromBundle(it)
                screenType = safeArgs.screenType
            }
            initializationOfObject()
        }
        return rootView
    }

    fun initializationOfObject() {
        tvTitle = rootView!!.findViewById(R.id.tv_title)
        tvResultCount = rootView!!.findViewById(R.id.tv_result_count)
        ivBack = rootView!!.findViewById(R.id.iv_back)
        ivNotification = rootView!!.findViewById(R.id.iv_notification)
        ivSearch = rootView!!.findViewById(R.id.iv_search)
        rvData = rootView!!.findViewById(R.id.rv_data)
        llData = rootView!!.findViewById(R.id.ll_data)
        viewEmptyBlack = rootView!!.findViewById(R.id.view_empty_black)
        llSearchSection = rootView!!.findViewById(R.id.ll_search_section)
        llSearch = rootView!!.findViewById(R.id.ll_search)
        etSearch = rootView!!.findViewById(R.id.et_search)
        tvSearchResult = rootView!!.findViewById(R.id.tv_search_result)
        tvSearchResult.visibility = View.GONE

        tvResultCount.visibility = View.GONE
        llData.visibility = View.GONE

        tvTitle.text = screenType

        ivBack.setOnClickListener(this)
        ivNotification.setOnClickListener(this)
        ivSearch.setOnClickListener(this)
        llSearch.setOnClickListener(this)

        if (screenType.equals("Search")) {
            ivSearch.visibility = View.GONE

        } else {
            ivNotification.visibility = View.GONE
            llSearchSection.visibility = View.GONE
            viewEmptyBlack.visibility = View.GONE
        }
        setTextChangeOfetAutoResidenceCity()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_back -> {
                    activity?.onBackPressed()
                }
                R.id.iv_notification -> {
                    navigateNoticeBoardAndNotificationFragment("Notification")
                }
                R.id.iv_search -> {
                    llSearchSection.visibility = View.VISIBLE
                }
                R.id.ll_search -> {
                    etSearch.requestFocus()
                }
            }
        }
    }

    fun setTextChangeOfetAutoResidenceCity() {
        var timerWait: Timer? = null
        var allowEditCity: Boolean = true
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                try {
                    if (timerWait != null) {
                        timerWait!!.cancel();

                    }
                    allowEditCity = true
                    if (TextUtils.isEmpty(etSearch.text.toString())) {
                        allowEditCity = true
                    }
                    if (allowEditCity == true) {
                        timerWait = Timer()
                        timerWait!!.schedule(object : TimerTask() {
                            override fun run() {


                                allowEditCity = false
                                ThreadUtils.runOnUiThread(Runnable {
                                    //call Search
                                    if (!TextUtils.isEmpty(etSearch.text.toString())) {
                                        transactionViewModel.getApplicationList(
                                            ApplicationListRequest(
                                                ApplicationListRequestData(
                                                    etSearch.text.toString(),
                                                    null,
                                                    null,
                                                    PAGE_NUMBER,
                                                    PER_PAGE
                                                ), CommonStrings.DEALER_ID,
                                                CommonStrings.USER_TYPE
                                            ),
                                            Global.customerAPI_BaseURL + CommonStrings.SEARCH_APPLICATION_END_POINT
                                        )
                                    }
                                });


                            }
                        }, 600)
                    } else {

                    }
                } catch (e: Exception) {

                }
            }
        })

        etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard(etSearch)
            }
            false
        })
    }

    private fun setResultData(response: ApplicationListResponse?) {
        if (response != null && response.data != null && response.data!!.customers != null && response.data!!.customers!!.size > 0) {
            val layoutManager = LinearLayoutManager(activity)
            rvData.layoutManager = layoutManager
            applicationListAdapter =
                ApplicationListAdapter(activity as Activity, response.data!!.customers!!, object :
                    ApplicationListClickCallBack {
                    override fun onItemClick(item: Any?, position: Int) {
                        showToast("onItemClick")
                    }

                    override fun onCompleteClick(item: Any?, position: Int) {
                        showToast("onCompleteClick")
                    }

                    override fun onCallClick(item: Any?, position: Int) {
                        showToast("onCallClick")
                    }

                })
            llData.visibility = View.VISIBLE
            rvData.adapter = applicationListAdapter

        }

    }

    //region Observer
    private fun onApplicationList(mApiResponse: ApiResponse) {
        parseCommonResponse(mApiResponse)
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val response: ApplicationListResponse? =
                    mApiResponse.data as ApplicationListResponse?
                setResultData(response)

            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()

            }
            else -> {

            }
        }

    }


//endregion Observer

}







