package v2.view.utility_view

import android.media.Image
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mfc.autofin.framework.R
import utility.CommonStrings

class WebViewActivity : AppCompatActivity() {

    lateinit var tAndCWebView: WebView
    var progressBar: ProgressBar? = null
    var mySwipeRefreshLayout: SwipeRefreshLayout? = null
    var scrollView: ScrollView? = null
    var parentLayout: RelativeLayout? = null
    var totalHeight = 0
    var width: Int = 0
    var webViewLayout: LinearLayout? = null
    var webURL = ""

    lateinit var ivBackToAddDetails: ImageView
    lateinit var textViewWebViewTitle:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.v2_activity_web_view)

        if (intent.getStringExtra("WEB_URL").isNullOrEmpty()) {
            intent.getStringExtra("WEB_URL").also { this.webURL = it }
        }

        tAndCWebView = findViewById(R.id.webview_main) as WebView
        progressBar = findViewById(R.id.indeterminateBar)
        scrollView = findViewById(R.id.scrollView)
        webViewLayout = findViewById(R.id.wvLayout)
        parentLayout = findViewById(R.id.parentLayout)

        totalHeight = scrollView?.getChildAt(0)?.getHeight()!!
        mySwipeRefreshLayout = findViewById<View>(R.id.swipeContainer) as SwipeRefreshLayout

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

        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
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

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && tAndCWebView.canGoBack()) {
            tAndCWebView.goBack()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}