package com.mobatia.nasmanila.activities.pdf

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebSettings.PluginState
import android.webkit.WebSettings.RenderPriority
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.common.common_classes.AppUtils

class PdfReaderActivity  : AppCompatActivity() {
    private var mContext: Activity? = null
    private var mWebView: WebView? = null
    private var mProgressRelLayout: RelativeLayout? = null
    private var mwebSettings: WebSettings? = null
    private var loadingFlag = true
    private var mLoadUrl: String? = null
    private var pdfUrl: String? = null
    private var mErrorFlag = false
    var extras: Bundle? = null
    var backImageView: ImageView? = null
    var pdfDownloadImgView: ImageView? = null
    var anim: RotateAnimation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pdf_view_layout);
        mContext = this
        extras = intent.extras

        if (extras != null) {
            mLoadUrl = AppUtils.replace(extras!!.getString("pdf_url")!!.replace("&", "%26"))
            pdfUrl = AppUtils.replace(extras!!.getString("pdf_url")!!.replace("&", "%26"))
        }
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        //		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        initialiseUI()
        getWebViewSettings()
    }

    private fun getWebViewSettings() {
        backImageView!!.setOnClickListener { finish() }
        mProgressRelLayout!!.visibility = View.VISIBLE
        anim = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim!!.setInterpolator(mContext, android.R.interpolator.linear)
        anim!!.repeatCount = Animation.INFINITE
        anim!!.duration = 4000
        mProgressRelLayout!!.animation = anim
        mProgressRelLayout!!.startAnimation(anim)
        mWebView!!.isFocusable = true
        mWebView!!.isFocusableInTouchMode = true
        mWebView!!.setBackgroundColor(0Xffffff)
        mWebView!!.isVerticalScrollBarEnabled = true
        mWebView!!.isHorizontalScrollBarEnabled = true
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            mWebView!!.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        mWebView!!.webChromeClient = WebChromeClient()
        val sdk = Build.VERSION.SDK_INT
        if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebView!!.setBackgroundColor(Color.argb(1, 0, 0, 0))
        }
        mwebSettings = mWebView!!.settings
        mwebSettings!!.setSaveFormData(true)
        mwebSettings!!.setBuiltInZoomControls(false)
        mwebSettings!!.setSupportZoom(false)

        mwebSettings!!.setPluginState(PluginState.ON)
        mwebSettings!!.setRenderPriority(RenderPriority.HIGH)
        mwebSettings!!.setJavaScriptCanOpenWindowsAutomatically(true)
        mwebSettings!!.setDomStorageEnabled(true)
        mwebSettings!!.setDatabaseEnabled(true)
        mwebSettings!!.setDefaultTextEncodingName("utf-8")
        mwebSettings!!.setLoadsImagesAutomatically(true)
        mwebSettings!!.setAllowUniversalAccessFromFileURLs(true)
        mwebSettings!!.setAllowContentAccess(true)
        mwebSettings!!.setAllowFileAccessFromFileURLs(true)

        mWebView!!.settings.allowFileAccess = true

        mWebView!!.settings.javaScriptEnabled = true
        mWebView!!.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        if (mLoadUrl!!.endsWith(".pdf")) {
            mLoadUrl = "https://drive.google.com/viewerng/viewer?embedded=true&url=$mLoadUrl"
        }
        mErrorFlag = if (mLoadUrl == "") {
            true
        } else {
            false
        }
        if (mLoadUrl != null && !mErrorFlag) {
            println("BISAD load url $mLoadUrl")
            mWebView!!.loadUrl(mLoadUrl!!)
        } else {
            mProgressRelLayout!!.clearAnimation()
            mProgressRelLayout!!.visibility = View.GONE
            AppUtils.showAlertFinish(
                mContext!!, resources
                    .getString(R.string.common_error_loading_pdf), "",
                resources.getString(R.string.ok), false
            )
        }
        mWebView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                println("===Page LOADING1111===$url")
                if (mProgressRelLayout!!.visibility == View.GONE) mProgressRelLayout!!.visibility =
                    View.VISIBLE
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                mProgressRelLayout!!.clearAnimation()
                mProgressRelLayout!!.visibility = View.GONE
                println("===Page LOADING2222===")
                if (AppUtils.checkInternet(mContext!!) && loadingFlag) {
                    view.settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    view.loadUrl(url)
                    loadingFlag = false
                } else if (!AppUtils.checkInternet(mContext!!) && loadingFlag) {
                    view.settings.cacheMode = WebSettings.LOAD_CACHE_ONLY
                    view.loadUrl(url)
                    println("CACHE LOADING")
                    loadingFlag = false
                }
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
            }

            /*
                 * (non-Javadoc)
                 *
                 * @see
                 * android.webkit.WebViewClient#onReceivedError(android.webkit.WebView
                 * , int, java.lang.String, java.lang.String)
                 */
            override fun onReceivedError(
                view: WebView, errorCode: Int,
                description: String, failingUrl: String
            ) {
                mProgressRelLayout!!.clearAnimation()
                mProgressRelLayout!!.visibility = View.GONE
                if (AppUtils.checkInternet(mContext!!)) {
                    AppUtils.showAlertFinish(
                        mContext!!, resources
                            .getString(R.string.common_error), "",
                        resources.getString(R.string.ok), false
                    )
                }
                super.onReceivedError(view, errorCode, description, failingUrl)
            }
        }
        pdfDownloadImgView!!.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl)))
            } catch (e: Exception) {
                e.printStackTrace()
            }

           
        }
    }

    private fun initialiseUI() {
        mWebView = findViewById<View>(R.id.webView) as WebView
        mProgressRelLayout = findViewById<View>(R.id.progressDialog) as RelativeLayout
        backImageView = findViewById<View>(R.id.backImageView) as ImageView
        pdfDownloadImgView = findViewById<View>(R.id.pdfDownloadImgView) as ImageView
    }
}