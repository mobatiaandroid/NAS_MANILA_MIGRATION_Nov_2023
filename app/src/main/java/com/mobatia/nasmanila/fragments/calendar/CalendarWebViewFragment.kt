package com.mobatia.nasmanila.fragments.calendar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.calendar.model.CalendarResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CalendarWebViewFragment() : Fragment() {
    private var mRootView: View? = null
    private var mContext: Context? = null
    private var relMain: ConstraintLayout? = null
    var activity: Activity? = null
    var mTitle: String? = null
    var mTabId: String? = null
    private var mWebView: WebView? = null
    private lateinit var progressBar: ProgressBar
    var progressBarDialog: ProgressBarDialog? = null
    private var mwebSettings: WebSettings? = null
    private var loadingFlag = true
    private var mLoadUrl =""
        //"https://www.nordangliaeducation.com/our-schools/philippines/manila/international/news-and-insights/school-calendar"
    private var mErrorFlag = false
    var anim: RotateAnimation? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_calendar_web_view,
            container, false
        )
        mContext = getActivity()
        activity = getActivity()
        progressBarDialog = ProgressBarDialog(mContext!!)
        initialiseUI()
        if (AppUtils.checkInternet(requireContext())) {
            callCalendarApi()
            getWebViewSettings()
        }else{
            Toast.makeText(
                mContext,
                requireContext().resources.getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
        }

        return mRootView
    }

    private fun callCalendarApi() {

        val call: Call<CalendarResponseModel> = ApiClient.getClient.calender(
            "Bearer "+ PreferenceManager.getAccessToken(mContext)
        )
        progressBarDialog!!.show()
        call.enqueue(object : Callback<CalendarResponseModel> {
            override fun onResponse(call: Call<CalendarResponseModel>, response: Response<CalendarResponseModel>) {


                if (response.body()!!.responsecode.equals("200")){
                    progressBarDialog!!.dismiss()
                    var status_code=response.body()!!.response.statuscode
                    if (status_code.equals("303")){

                        mLoadUrl=response.body()!!.response.calendar_url
                        getWebViewSettings()
                    }
                    else if (status_code.equals("301")) {
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
                }else {
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

            override fun onFailure(call: Call<CalendarResponseModel>, t: Throwable) {
                progressBarDialog!!.dismiss()
            }

        })
    }

    private fun getWebViewSettings() {
progressBar.visibility=View.VISIBLE
        mWebView!!.settings.javaScriptEnabled = true
        mWebView!!.settings.javaScriptCanOpenWindowsAutomatically = true
        mWebView!!.settings.loadsImagesAutomatically = true
        mWebView!!.setBackgroundColor(Color.TRANSPARENT)
        mWebView!!.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)

        /*val userAgent =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
        mWebView!!.getSettings().setUserAgentString(userAgent)*/
        mWebView!!.webViewClient = MyWebViewClient(requireActivity())



        //   webView.getSettings().setUserAgentString("ur user agent");
        mWebView!!.loadUrl(mLoadUrl!!)



        mWebView!!.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBar.progress=newProgress
                //progressDialogAdd.progress = newProgress
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                    // back.visibility = View.VISIBLE

                }
            }
        }

    }

    private fun initialiseUI() {
        mLoadUrl =""
            //"https://www.nordangliaeducation.com/our-schools/philippines/manila/international/news-and-insights/school-calendar"
        var mTitleTextView: TextView? = null
        relMain = mRootView!!.findViewById<View>(R.id.relMain) as ConstraintLayout
        mWebView = mRootView!!.findViewById<View>(R.id.webView) as WebView
        mTitleTextView = mRootView!!.findViewById(R.id.titleTextView)
        mTitleTextView.text = NaisClassNameConstants.CALENDAR
        progressBar = mRootView!!.findViewById<View>(R.id.progressBar) as ProgressBar
        relMain!!.setOnClickListener(View.OnClickListener { })
    }
    class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString()
            view?.loadUrl(url)
            //progressDialogAdd.visibility= View.GONE
            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            var overrideUrlLoading = false
            if (url != null && url.contains("whatsapp")) {
                webView.getContext().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                overrideUrlLoading = true
            }
            else {
                webView.loadUrl(url)
            }

            // progressDialogAdd.visibility= View.GONE
            return overrideUrlLoading
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            //progressDialogAdd.visibility= View.GONE
            //Toast.makeText(activity, "Got Error! $error", Toast.LENGTH_SHORT).show()
        }
    }


}