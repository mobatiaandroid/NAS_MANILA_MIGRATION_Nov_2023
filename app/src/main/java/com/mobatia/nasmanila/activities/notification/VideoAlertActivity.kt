package com.mobatia.nasmanila.activities.notification

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings.PluginState
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
var proWebView: ProgressBar? = null
private var textcontent: TextView? = null
class VideoAlertActivity : AppCompatActivity() {

    var webView: WebView? = null
    var position = 0

    private var back: ImageView? = null
    var home: ImageView? = null
    lateinit var mContext:Context
    var mActivity: Activity? = null

    private var relativeHeader: RelativeLayout? = null
    var progressBarDialog: ProgressBarDialog? = null
    private var headermanager: HeaderManager? = null
    var id = ""
    var title = ""
    var message = ""
    var url = ""
    var date = ""
    var day = ""
    var month = ""
    var year = ""
    var pushDate = ""
    var pushfrom = ""
    var type = ""
    private var pushID = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_alert)
        mActivity = this

        val extra = intent.extras
        if (extra != null) {
            position = extra!!.getInt("position")
            message = extra!!.getString("message")!!
            url = extra!!.getString("url")!!
            date = extra!!.getString("date")!!
            pushfrom = extra!!.getString("pushfrom")!!
            type = extra!!.getString("type")!!
        }
        initialiseUI()
    }

    private fun initialiseUI() {
        mContext=this
        progressBarDialog = ProgressBarDialog(mContext!!)
        webView = findViewById<View>(R.id.webView) as WebView

        proWebView = findViewById<View>(R.id.proWebView) as ProgressBar
        textcontent = findViewById(R.id.txtContent)
        textcontent!!.visibility = View.INVISIBLE

        relativeHeader = findViewById(R.id.relativeHeader)
        headermanager = HeaderManager(mActivity, "Notification")
        headermanager!!.getHeader(relativeHeader!!, 0)
        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(R.drawable.back,
            R.drawable.back)
        back!!.setOnClickListener { finish() }
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(mActivity, HomeListAppCompatActivity::class.java)
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }

        webView!!.webViewClient = HelloWebViewClient()
        webView!!.settings.javaScriptEnabled = true
        webView!!.settings.pluginState = PluginState.ON
        webView!!.settings.builtInZoomControls = false
        webView!!.settings.displayZoomControls = true
//		DisplayMetrics displaymetrics = new DisplayMetrics();
//	    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//	    int height = displaymetrics.heightPixels;
        //		DisplayMetrics displaymetrics = new DisplayMetrics();
//	    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//	    int height = displaymetrics.heightPixels;
        val frameVideo = "<html>" + "<br><iframe width=\"320\" height=\"250\" src=\""
        val url_Video = frameVideo + url + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>"
        val urlYoutube = url_Video.replace("watch?v=", "embed/")
        println("urlYoutube:$urlYoutube")
        webView!!.loadData(urlYoutube, "text/html", "utf-8")

//		webView.loadUrl(videolist.get(position).getUrl());

//		webView.loadUrl(videolist.get(position).getUrl());
        textcontent!!.setText(message)
        proWebView!!.visibility = View.VISIBLE
    }



    class HelloWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            proWebView!!.setVisibility(View.GONE)
            textcontent!!.setVisibility(View.VISIBLE)

        }
    }
}