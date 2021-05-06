package v2.view

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mfc.autofin.framework.R
import utility.CommonStrings
import v2.view.base.BaseFragment


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class WebViewFragment : BaseFragment(),ViewTreeObserver.OnScrollChangedListener{

    lateinit var tAndCWebView: WebView
    var progressBar: ProgressBar? = null
    var mySwipeRefreshLayout: SwipeRefreshLayout? = null
    var scrollView: ScrollView? = null
    var parentLayout: RelativeLayout? = null
    var totalHeight = 0
    var width: Int = 0
    var webViewLayout: LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tAndCWebView = view.findViewById(R.id.webview_main) as WebView
        progressBar = view?.findViewById(R.id.indeterminateBar)
        scrollView = view?.findViewById(R.id.scrollView)
        webViewLayout = view?.findViewById(R.id.wvLayout)
        parentLayout = view?.findViewById(R.id.parentLayout)

        totalHeight = scrollView?.getChildAt(0)?.getHeight()!!
        mySwipeRefreshLayout = view?.findViewById<View>(R.id.swipeContainer) as SwipeRefreshLayout

        tAndCWebView.loadUrl(CommonStrings.TERMS_AND_CONDITION_URL)

        val webSettings: WebSettings = tAndCWebView.getSettings()
        webSettings.javaScriptEnabled = true

        tAndCWebView.webViewClient = WebViewClient()


        mySwipeRefreshLayout!!.setOnRefreshListener {
            tAndCWebView.reload()
            if (mySwipeRefreshLayout!!.isRefreshing) {
                if (progressBar!!.visibility == View.VISIBLE) {
                    progressBar!!.visibility = View.GONE
                }
                mySwipeRefreshLayout!!.isRefreshing = false
            }
        }
        mySwipeRefreshLayout!!.isNestedScrollingEnabled
        val vto = parentLayout!!.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    parentLayout!!.viewTreeObserver
                            .removeOnGlobalLayoutListener(this)
                } else {
                    parentLayout!!.viewTreeObserver
                            .removeGlobalOnLayoutListener(this)
                }
                width = parentLayout!!.measuredWidth
                totalHeight = parentLayout!!.measuredHeight
            }
        })

        onScrollChanged()
        isScrollDown()

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.v2_fragment_web_view, container, false)

        return view
    }

    override fun onScrollChanged() {
        mySwipeRefreshLayout!!.isEnabled = tAndCWebView.getScrollY() == 0
    }


    fun isScrollDown(): Boolean {
        val result = false
        val view = scrollView!!.getChildAt(scrollView!!.childCount - 1) as View
        val diff = view.bottom - (totalHeight + scrollView!!.scrollY)
        if (scrollView!!.height == totalHeight) {
            val p = webViewLayout!!.layoutParams as MarginLayoutParams
            p.bottomMargin = 300
            tAndCWebView.setLayoutParams(p)
            Log.d("ScrollTest", "MyScrollView: Bottom has been reached")
        } else if (scrollView!!.height != totalHeight) {
            val p = webViewLayout!!.layoutParams as MarginLayoutParams
            p.bottomMargin = 10
            tAndCWebView.setLayoutParams(p)
            Log.d("ScrollTest", "MyScrollView:Didn't reach Bottom ")
        }
        return result
    }

   /* class myWebClient : WebViewClient() {

        var progressBar: ProgressBar? =null
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            super.onPageStarted(view, url, favicon)
            if (progressBar.getVisibility() == View.GONE) {
                progressBar.setVisibility(View.VISIBLE)
            }
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(CommonStrings.TERMS_AND_CONDITION_URL)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE)
            }
        }

    }
*/


}







