package com.mobatia.nasmanila.activities.terms_of_service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.fragments.settings.model.Terms_of_serviceModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermsOfServiceActivity : AppCompatActivity() {
    var mContext: Context = this
    var relativeHeader: RelativeLayout? = null
    //var mProgressRelLayout:RelativeLayout? = null
    var progressBarDialog: ProgressBarDialog? = null
    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null
    var extras: Bundle? = null
    var tab_type: String? = null
    var web: WebView? = null
    var anim: RotateAnimation? = null
    private var mwebSettings: WebSettings? = null
    private var loadingFlag = true
    private var mErrorFlag = false
    var termsTitle: String? = null
    var termsDescription: String? = null
    var mLoadData: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_service)
        progressBarDialog = ProgressBarDialog(mContext!!)
        initUI()
        if (AppUtils.isNetworkConnected(mContext)) {
            callAboutUsApi()
            getWebViewSettings()
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
    }

    private fun callAboutUsApi() {
        progressBarDialog!!.show()


        val call: Call<Terms_of_serviceModel> = ApiClient.getClient.terms_of_service("Bearer "+ PreferenceManager.getAccessToken(mContext))
        call.enqueue(object : Callback<Terms_of_serviceModel> {
            override fun onResponse(
                call: Call<Terms_of_serviceModel>,
                response: Response<Terms_of_serviceModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {

                        termsTitle = response.body()!!.response.data.title
                        termsDescription = response.body()!!.response.data.description
                        mLoadData = """
                            <!DOCTYPE html>
                            <html>
                            <head>
                            <style>
                            
                            @font-face {
                            font-family: SourceSansPro-Semibold;src: url(SourceSansPro-Semibold.ttf);font-family: SourceSansPro-Regular;src: url(SourceSansPro-Regular.ttf);font-family: SourceSansPro-Light;src: url(SourceSansPro-Light.otf);}.title {font-family: SourceSansPro-Regular;src: url(SourceSansPro-Regular.ttf);font-size:16px;text-align:left;color:	#46C1D0;text-align: ####TEXT_ALIGN####;}.description {font-family: SourceSansPro-Light;src: url(SourceSansPro-Light.otf);text-align:justify;font-size:14px;color: #808080;text-align: ####TEXT_ALIGN####;}</style>
                            </head><body><p class='title'>$termsTitle</p><p class='description'>$termsDescription</p></body>
                            </html>
                            """.trimIndent()
                        web!!.loadDataWithBaseURL("file:///android_asset/fonts/",
                            mLoadData!!, "text/html; charset=utf-8", "utf-8", "about:blank")

                    } else if (status_code.equals("301")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext,
                            getString(R.string.error_heading),
                            getString(R.string.missing_parameter),
                            R.drawable.infoicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("304")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.email_exists),
                            R.drawable.infoicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("305")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.incrct_usernamepswd),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("306")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.invalid_email),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                } else {
                    progressBarDialog!!.dismiss()
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        getString(R.string.error_heading),
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<Terms_of_serviceModel>, t: Throwable) {

            }
        })
    }

    private fun getWebViewSettings() {

        mwebSettings = web!!.settings
        mwebSettings!!.javaScriptEnabled = true
        mwebSettings!!.setSupportZoom(false)
        mwebSettings!!.cacheMode=WebSettings.LOAD_NO_CACHE
        mwebSettings!!.javaScriptCanOpenWindowsAutomatically = true
        mwebSettings!!.domStorageEnabled = true
        mwebSettings!!.databaseEnabled = true
        mwebSettings!!.defaultTextEncodingName = "utf-8"
        mwebSettings!!.loadsImagesAutomatically = true
        mwebSettings!!.cacheMode = WebSettings.LOAD_NO_CACHE
        mwebSettings!!.allowFileAccess = true
        web!!.setBackgroundColor(Color.TRANSPARENT)
        web!!.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)


        web!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBarDialog!!.show()
                if (newProgress == 100)
                {
                   progressBarDialog!!.dismiss()

                }
            }
        }
    }

    private fun initUI() {
        extras = intent.extras
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")
        }
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        web = findViewById<View>(R.id.webView) as WebView
        //mProgressRelLayout = findViewById<View>(R.id.progressDialog) as RelativeLayout
        //web.setWebViewClient(new myWebClient());
        //web.setWebViewClient(new myWebClient());
        web!!.settings.javaScriptEnabled = true
        headermanager = HeaderManager(this@TermsOfServiceActivity, tab_type)
        headermanager!!.getHeader(relativeHeader, 0)
        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        back!!.setOnClickListener {
            finish()
        }
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(mContext, HomeListAppCompatActivity::class.java)
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        progressBarDialog!!.show()
        anim = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim!!.setInterpolator(mContext, android.R.interpolator.linear)
        anim!!.repeatCount = Animation.INFINITE
        anim!!.duration = 1000
       /* mProgressRelLayout!!.animation = anim
        mProgressRelLayout!!.startAnimation(anim)*/
    }
}