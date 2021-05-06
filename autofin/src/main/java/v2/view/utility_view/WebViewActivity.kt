package v2.view.utility_view

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.mfc.autofin.framework.R
import utility.CommonStrings


class WebViewActivity : AppCompatActivity() {

    lateinit var tAndCWebView: WebView
    public var progressBar: ProgressBar? = null


    var parentLayout: RelativeLayout? = null
    var totalHeight = 0
    var width: Int = 0
    var webViewLayout: LinearLayout? = null
    var webURL = ""

    lateinit var ivBackToAddDetails: ImageView
    lateinit var textViewWebViewTitle: TextView
    var url: String? = null
    var title: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.v2_activity_web_view)

        if (!TextUtils.isEmpty(intent.getStringExtra(CommonStrings.URL))) {
            url=intent.getStringExtra(CommonStrings.URL)
        } else {
            finish()
        }
        if (!TextUtils.isEmpty(intent.getStringExtra(CommonStrings.TITLE))) {
            title=intent.getStringExtra(CommonStrings.TITLE)
        } else {
            finish()
        }


        tAndCWebView = findViewById(R.id.webview_main) as WebView
        progressBar = findViewById(R.id.indeterminateBar)

        webViewLayout = findViewById(R.id.wvLayout)
        parentLayout = findViewById(R.id.parentLayout)

        ivBackToAddDetails = findViewById(R.id.ivBackToAddDetails)
        textViewWebViewTitle = findViewById(R.id.textViewWebViewTitle)







        val webSettings: WebSettings = tAndCWebView.getSettings()
        webSettings.javaScriptEnabled = true
        tAndCWebView.loadUrl(url)
        textViewWebViewTitle.text = title
        tAndCWebView.webViewClient = WebViewClient(progressBar)

        //textViewWebViewTitle.text=actionBarTitle
        ivBackToAddDetails.setOnClickListener(View.OnClickListener { onBackPressed() })


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

    class WebViewClient(progressBar: ProgressBar?) : android.webkit.WebViewClient() {
        private var mProgressBar: ProgressBar? = null

        init {
            mProgressBar = progressBar
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

            // TODO Auto-generated method stub
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {

            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
            mProgressBar!!.visibility = View.GONE
        }
    }


}