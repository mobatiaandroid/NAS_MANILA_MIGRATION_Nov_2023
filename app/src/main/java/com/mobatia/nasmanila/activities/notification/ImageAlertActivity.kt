package com.mobatia.nasmanila.activities.notification

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog


class ImageAlertActivity : AppCompatActivity() {
    private var mContext: Context? = null
    private var mWebView: WebView? = null
    private var progressBar: ProgressBar? = null
    private var mwebSettings: WebSettings? = null
    private var loadingFlag = true
    private var mLoadUrl: String? = null
    private var mErrorFlag = false
    private var extras: Bundle? = null
    private var relativeHeader: RelativeLayout? = null
    private var headermanager: HeaderManager? = null
    private var back: ImageView? = null
    var home: ImageView? = null
    var anim: RotateAnimation? = null
    private var pushID = ""
    var id = ""
    var title = ""
    var message = ""
    var url = ""
    var date = ""
    var type = ""
    var day = ""
    var month = ""
    var year = ""
    var pushDate = ""
    var position =0
    var pushfrom = ""
    var mMessage = ""
    var mDate = ""
    var progressBarDialog: ProgressBarDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_alert)
        mContext = this
        extras = intent.extras
        if (extras != null) {
            position = extras!!.getInt("position")
            message = extras!!.getString("message")!!
            url = extras!!.getString("url")!!
            date = extras!!.getString("date")!!
            pushfrom = extras!!.getString("pushfrom")!!
            type = extras!!.getString("type")!!
        }
        mDate = AppUtils.dateParsingToDdMmmYyyy(date).toString()
        mMessage = message
            .replace("\n".toRegex(), "<br>")
            .replace(" ".toRegex(), "&nbsp;");
        initialiseUI()
        if (AppUtils.checkInternet(mContext as ImageAlertActivity)) {
            callPushNotification(pushID)
        } else {
            AppUtils.showDialogAlertDismiss(mContext as Activity?, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred)
        }
        getWebViewSettings()
    }

    private fun callPushNotification(pushID: String) {
        var pushNotificationDetail = """
            <!DOCTYPE html>
            <html>
            <head>
            <style>
            
            @font-face {
            font-family: SourceSansPro-Semibold;src: url(SourceSansPro-Semibold.otf);font-family: SourceSansPro-Regular;src: url(SourceSansPro-Regular.otf);}.title {font-family: SourceSansPro-Regular;font-size:15px;text-align:left;color:	#46C1D0;text-align: ####TEXT_ALIGN####;}.description {font-family: SourceSansPro-Semibold;text-align:justify;font-size:14px;color: #000000;text-align: ####TEXT_ALIGN####;}</style>
            </head><body><p class='title'>$mMessage
            """.trimIndent()
        pushNotificationDetail="$pushNotificationDetail<p class='description'>$mDate</p>"
        if (!url.equals(""))
        {
            pushNotificationDetail=pushNotificationDetail+"<center><img src='" + url + "'width='100%', height='auto'>"
        }
        pushNotificationDetail=pushNotificationDetail+"</body>\n</html>"
        var htmlData=pushNotificationDetail
        //  webView.loadData(htmlData,"text/html; charset=utf-8","utf-8")
        mWebView!!.loadDataWithBaseURL("file:///android_asset/fonts/",htmlData,"text/html; charset=utf-8", "utf-8", "about:blank")



    }
    private fun initialiseUI() {
        relativeHeader = findViewById<View>(R.id.relativeHeader) as? RelativeLayout
        mWebView = findViewById<View>(R.id.webView) as? WebView
        progressBar = findViewById<View>(R.id.progressDialog) as? ProgressBar
        progressBarDialog = ProgressBarDialog(mContext!!)
        headermanager = HeaderManager(this@ImageAlertActivity, "Notification")
        headermanager!!.getHeader(relativeHeader!!, 0)
        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(R.drawable.back,
            R.drawable.back)
        back!!.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
            finish()
        }
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val intent = Intent(mContext, HomeListAppCompatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun getWebViewSettings() {
       // progressBarDialog!!.show()
        mWebView!!.settings.javaScriptEnabled = true
        mWebView!!.settings.setSupportZoom(false)
        mWebView!!.settings.cacheMode=WebSettings.LOAD_NO_CACHE
        mWebView!!.settings.javaScriptCanOpenWindowsAutomatically = true
        mWebView!!.settings.domStorageEnabled = true
        mWebView!!.settings.databaseEnabled = true
        mWebView!!.settings.defaultTextEncodingName = "utf-8"
        mWebView!!.settings.loadsImagesAutomatically = true
        mWebView!!.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        mWebView!!.settings.allowFileAccess = true
        mWebView!!.setBackgroundColor(Color.TRANSPARENT)
        mWebView!!.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)


        mWebView!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBarDialog!!.show()
                if (newProgress == 100)
                {
                    progressBarDialog!!.dismiss()

                }
            }
        }

    }

}