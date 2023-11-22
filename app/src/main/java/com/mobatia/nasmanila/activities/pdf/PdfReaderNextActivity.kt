package com.mobatia.nasmanila.activities.pdf

import android.app.Activity
import android.content.Context
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
import com.mobatia.nasmanila.activities.web_view.LoadUrlWebViewActivity
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.fragments.parent_essentials.model.SubmenuParentEssentials

class PdfReaderNextActivity : AppCompatActivity() {
    private var mContext: Context? = null
    private var activity: Activity? = null
    private var mWebView: WebView? = null
    private var mProgressRelLayout: RelativeLayout? = null
    private var mwebSettings: WebSettings? = null
    private var loadingFlag = true
    private var mLoadUrl: String? = null
    private  var pdfUrl:String? = ""
    private var mErrorFlag = false
    var extras: Bundle? = null
    var backImageView: ImageView? =
        null
    var next:android.widget.ImageView? = null
    var prev:android.widget.ImageView? = null
    var pdfDownloadImgView:android.widget.ImageView? = null
    var anim: RotateAnimation? = null
    var pos = 0
    var list: ArrayList<SubmenuParentEssentials>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pdf_with_next_view_layout);
        mContext = this
        activity=this
        extras = intent.extras
        if (extras != null) {
//            mLoadUrl = extras.getString("pdf_url");
            pos = extras!!.getInt("position")
            list =
                extras!!.getSerializable("submenuArray") as ArrayList<SubmenuParentEssentials>?
            mLoadUrl = AppUtils.replace(list!![pos].filename.replace("&", "%26"))
            pdfUrl = AppUtils.replace(list!![pos].filename.replace("&", "%26"))
        }
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        //		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        initialiseUI()
        getWebViewSettings()
    }

    private fun getWebViewSettings() {
        mProgressRelLayout!!.visibility = View.VISIBLE
        anim = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim!!.setInterpolator(mContext, android.R.interpolator.linear)
        anim!!.repeatCount = Animation.INFINITE
        anim!!.duration = 1000
        mProgressRelLayout!!.animation = anim
        mProgressRelLayout!!.startAnimation(anim)
        mWebView!!.isFocusable = true
        mWebView!!.isFocusableInTouchMode = true
        mWebView!!.setBackgroundColor(-0x1)
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

//        mWebView.getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 5MB
//        mWebView.getSettings().setAppCachePath(
//                mContext.getCacheDir().getAbsolutePath());

//        mWebView.getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 5MB
//        mWebView.getSettings().setAppCachePath(
//                mContext.getCacheDir().getAbsolutePath());
        mWebView!!.settings.allowFileAccess = true
//        mWebView.getSettings().setAppCacheEnabled(true);
        //        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView!!.settings.javaScriptEnabled = true
        mWebView!!.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setUseWideViewPort(true);

//		refreshBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				GetWebUrlAsyncTask getWebUrl = new GetWebUrlAsyncTask(WEB_CONTENT_URL
//						+ mType, WEB_CONTENT + "/" + mType, 1, mTAB_ID);
//				getWebUrl.execute();
//			}
//		});

        //        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setUseWideViewPort(true);

//		refreshBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				GetWebUrlAsyncTask getWebUrl = new GetWebUrlAsyncTask(WEB_CONTENT_URL
//						+ mType, WEB_CONTENT + "/" + mType, 1, mTAB_ID);
//				getWebUrl.execute();
//			}
//		});
        mWebView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                mProgressRelLayout!!.clearAnimation()
                mProgressRelLayout!!.visibility = View.GONE
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
                        activity!!, resources
                            .getString(R.string.common_error), "",
                        resources.getString(R.string.ok), false
                    )
                }
                super.onReceivedError(view, errorCode, description, failingUrl)
            }
        }
        if (mLoadUrl!!.endsWith(".pdf")) {
            mLoadUrl = "http://docs.google.com/gview?embedded=true&url=$mLoadUrl"
//            mLoadUrl = "http://docs.google.com/viewerng/viewer?url=" + mLoadUrl;
//             mLoadUrl="<iframe src="+"'http://docs.google.com/gview?embedded=true&url="+AppUtils.replace(mLoadUrl)+"'width='100%' height='100%'style='border: none;'></iframe>";
        }
        mErrorFlag = if (mLoadUrl == "") {
            true
        } else {
            false
        }
        if (mLoadUrl != null && !mErrorFlag) {
            println("NAIS load url $mLoadUrl")
            mWebView!!.loadUrl(mLoadUrl!!)
        } else {
            mProgressRelLayout!!.clearAnimation()
            mProgressRelLayout!!.visibility = View.GONE
            AppUtils.showAlertFinish(
                activity!!, resources
                    .getString(R.string.common_error_loading_pdf), "",
                resources.getString(R.string.ok), false
            )
        }
    }

    private fun initialiseUI() {
        mWebView = findViewById<View>(R.id.webView) as WebView
        mProgressRelLayout = findViewById<View>(R.id.progressDialog) as RelativeLayout
        backImageView = findViewById<View>(R.id.backImageView) as ImageView
        pdfDownloadImgView = findViewById<View>(R.id.pdfDownloadImgView) as ImageView

        prev = findViewById<View>(R.id.prev) as ImageView
        next = findViewById<View>(R.id.next) as ImageView
        if (list!!.size <= 1) {
            prev!!.visibility = View.GONE
            next!!.visibility = View.GONE
        } else {
            prev!!.visibility = View.VISIBLE
            next!!.visibility = View.VISIBLE
        }
        backImageView!!.setOnClickListener { finish() }
        prev!!.setOnClickListener {
            pos = pos - 1
            if (pos < 0) {
                pos = list!!.size - 1
            }
            mLoadUrl = AppUtils.replace(list!![pos].filename.replace("&", "%26"))
            pdfUrl = AppUtils.replace(list!![pos].filename.replace("&", "%26"))
            if (mLoadUrl!!.endsWith(".pdf")) {
                getWebViewSettings()
            } else {
                val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                intent.putExtra("url", list!![pos].filename)
                intent.putExtra("tab_type", list!![pos].submenu)
                startActivity(intent)
                finish()
            }
        }
        next!!.setOnClickListener {
            pos = pos + 1
            if (pos >= list!!.size) {
                pos = 0
            }
            mLoadUrl = AppUtils.replace(list!![pos].filename.replace("&", "%26"))
            pdfUrl = AppUtils.replace(list!![pos].filename.replace("&", "%26"))
            if (mLoadUrl!!.endsWith(".pdf")) {
                getWebViewSettings()
            } else {
                val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                intent.putExtra("url", list!![pos].filename)
                intent.putExtra("tab_type", list!![pos].submenu)
                startActivity(intent)
                finish()
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
}
