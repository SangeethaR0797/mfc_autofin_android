package v2.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.EditText
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


}







