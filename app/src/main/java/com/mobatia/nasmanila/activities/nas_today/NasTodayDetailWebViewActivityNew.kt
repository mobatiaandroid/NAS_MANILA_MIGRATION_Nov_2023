package com.mobatia.nasmanila.activities.nas_today

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog


class NasTodayDetailWebViewActivityNew : AppCompatActivity() {
    private var mContext: Context? = null
    private var mWebView: WebView? = null
    var progressBarDialog: ProgressBarDialog? = null

    private var mwebSettings: WebSettings? = null
    private val loadingFlag = true
    private var mLoadUrl: String? = null
    private var mErrorFlag = false
    var extras: Bundle? = null
    var relativeHeader: RelativeLayout? = null
    lateinit var headermanager: HeaderManager
    var back: ImageView? = null
    var home: ImageView? = null
    var infoImg: ImageView? = null
    var anim: RotateAnimation? = null
    var pdf: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nas_today_detail_web_view_new)
        mContext = this
        extras = intent.extras
        if (extras != null) {
            mLoadUrl = extras!!.getString("webViewComingDetail")
            pdf = extras!!.getString("pdf")
            println("webViewComingUpDetail$mLoadUrl")
            Log.e("Webview", mLoadUrl!!)
        }
        //		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        initialiseUI()
        webViewSettings
    }

    private fun initialiseUI() {
        progressBarDialog = ProgressBarDialog(mContext!!)
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        mWebView = findViewById<View>(R.id.webView) as WebView
        progressBarDialog!!.show()
        //mProgressRelLayout = findViewById<View>(R.id.progressDialog) as RelativeLayout
        headermanager = HeaderManager(this@NasTodayDetailWebViewActivityNew, "NAIS Manila Today")
        headermanager.getHeader(relativeHeader, 0)
        back = headermanager.getLeftButton()
        headermanager.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        back!!.setOnClickListener {

            finish()
        }
        home = headermanager.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(mContext, HomeListAppCompatActivity::class.java)
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
    }

    private val webViewSettings: Unit
        private get() {
            progressBarDialog!!.hide()

            /* anim = RotateAnimation(
                 0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
                 Animation.RELATIVE_TO_SELF, 0.5f
             )*/
            //anim!!.setInterpolator(mContext, R.interpolator.linear)
            //anim!!.repeatCount = Animation.INFINITE
           // anim!!.duration = 1000
           // mProgressRelLayout!!.animation = anim
           // mProgressRelLayout!!.startAnimation(anim)
            mWebView!!.isFocusable = true
            mWebView!!.isFocusableInTouchMode = true
            mWebView!!.setBackgroundColor(0X00000000)
            mWebView!!.isVerticalScrollBarEnabled = false
            mWebView!!.isHorizontalScrollBarEnabled = false
            mWebView!!.webChromeClient = WebChromeClient()

            //        int sdk = Build.VERSION.SDK_INT;
            //        if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
            //            mWebView.setBackgroundColor(Color.argb(1, 0, 0, 0));
            //        }
            mwebSettings = mWebView!!.settings
            mwebSettings!!.saveFormData = true
            mwebSettings!!.builtInZoomControls = false
            mwebSettings!!.setSupportZoom(false)
            mwebSettings!!.pluginState = WebSettings.PluginState.ON
            mwebSettings!!.setRenderPriority(WebSettings.RenderPriority.HIGH)
            mwebSettings!!.javaScriptCanOpenWindowsAutomatically = true
            mwebSettings!!.domStorageEnabled = true
            mwebSettings!!.databaseEnabled = true
            mwebSettings!!.defaultTextEncodingName = "utf-8"
            mwebSettings!!.loadsImagesAutomatically = true
           /* mWebView!!.settings.setAppCacheMaxSize(10 * 1024 * 1024) // 5MB
            mWebView!!.settings.setAppCachePath(
                mContext!!.cacheDir.absolutePath
            )*/
            mWebView!!.settings.allowFileAccess = true
            //mWebView!!.settings.setAppCacheEnabled(true)
            mWebView!!.settings.javaScriptEnabled = true
            mWebView!!.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK


            mWebView!!.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                    return if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith(
                            "www."
                        )
                    ) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        true
                    } else {
                        println("NAS load url $mLoadUrl")
                        view.loadDataWithBaseURL(
                            "file:///android_asset/fonts/",
                            mLoadUrl!!, "text/html; charset=utf-8", "utf-8", "about:blank"
                        )
                        true
                    }
                }


                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }


                override fun onReceivedError(
                    view: WebView, errorCode: Int,
                    description: String, failingUrl: String
                ) {
                    progressBarDialog!!.hide()

                    if (AppUtils.checkInternet(mContext!!)) {
                        AppUtils.showAlertFinish(
                            (mContext as Activity?)!!, resources
                                .getString(R.string.common_error), "",
                            resources.getString(R.string.ok), false
                        )
                    }

                    super.onReceivedError(view, errorCode, description, failingUrl)
                }
            }
            mErrorFlag = if (mLoadUrl == "") {
                true
            } else {
                false
            }
            if (mLoadUrl != null && !mErrorFlag) {
                println("NAS load url $mLoadUrl")
                //mWebView.loadData(mLoadUrl, "text/html", "UTF-8");
                mWebView!!.loadDataWithBaseURL(
                    "file:///android_asset/",
                    mLoadUrl!!, "text/html; charset=utf-8", "utf-8", "about:blank"
                )
                //mWebView.loadData(mLoadUrl, "text/html; charset=utf-8", "utf-8");
            } else {
                progressBarDialog!!.hide()
                AppUtils.showAlertFinish(
                    (mContext as Activity?)!!, resources
                        .getString(R.string.common_error_loading_page), "",
                    resources.getString(R.string.ok), false
                )
            }

        }
}
