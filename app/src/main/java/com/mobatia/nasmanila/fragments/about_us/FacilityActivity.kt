package com.mobatia.nasmanila.fragments.about_us

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListActivity
import com.mobatia.nasmanila.activities.parent_essential.model.SendemailApiModel
import com.mobatia.nasmanila.activities.parent_essential.model.SendemailResponseModel
import com.mobatia.nasmanila.activities.pdf.PDFViewActivity
import com.mobatia.nasmanila.activities.web_view.LoadUrlWebViewActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.*
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.about_us.adapter.FacilityRecyclerAdapter
import com.mobatia.nasmanila.fragments.about_us.model.AboutUsItemsModel
import com.mobatia.nasmanila.fragments.about_us.model.AboutUsModel
import com.mobatia.nasmanila.manager.recyclermanager.ItemOffsetDecoration
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FacilityActivity : AppCompatActivity() {
    var extras: Bundle? = null
    var aboutusModelArrayList: ArrayList<AboutUsModel> = ArrayList<AboutUsModel>()
    private var mContext: Context = this
    var progressBarDialog: ProgressBarDialog? = null
    private var mAboutUsList: RecyclerView? = null
    private val mBannerImage: ImageView? = null
    private  var sendEmail: ImageView? = null
    var mTitleTextView: TextView? = null
    var weburl: TextView? = null
    var description: TextView? = null
    var descriptionTitle:TextView? = null

    private var mAboutUsListArray: ArrayList<AboutUsItemsModel?>? = null
    var relMain: RelativeLayout? = null
    var desc: String? = null
    var title:kotlin.String? = null
    var bannerimg:kotlin.String? = null
    var bannerImagePager: ImageView? = null

    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null
    var bannerUrlImageArray = ArrayList<String>()
    var contactEmail: String? = null
    var text_dialog: EditText? = null
    var text_content: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facility)
        mContext = this
        progressBarDialog = ProgressBarDialog(mContext!!)
        initialiseUI()
    }

    private fun initialiseUI() {
        if (extras != null) {
           /* mAboutUsListArray = extras!!
                .getSerializable("array") as ArrayList<AboutUsModel>?*/
            desc = extras!!.getString("desc")
            title = extras!!.getString("title")
            bannerimg = extras!!.getString("banner_image")
            println("Image url--$bannerimg")
            if (bannerimg != "") {
                bannerUrlImageArray.add(bannerimg!!)
            }
        }
        mAboutUsListArray=PreferenceManager.getaboutusarray(mContext!!)
        relativeHeader = findViewById(R.id.relativeHeader)
        bannerImagePager = findViewById<View>(R.id.bannerImagePager) as ImageView

        headermanager = HeaderManager(this@FacilityActivity, NaisClassNameConstants.ABOUT_US)
        headermanager!!.getHeader(relativeHeader!!, 0)
        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        back!!.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
            finish()
        }
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val intent = Intent(mContext, HomeListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        mAboutUsList = findViewById<View>(R.id.mAboutUsListView) as RecyclerView
        description = findViewById<View>(R.id.descriptionTV) as TextView
        descriptionTitle = findViewById<View>(R.id.descriptionTitle) as TextView
        if (desc.equals("", ignoreCase = true)) {
            description!!.visibility = View.GONE
            descriptionTitle!!.visibility = View.GONE
        } else {
            description!!.text = desc
            descriptionTitle!!.visibility = View.GONE
            //			descriptionTitle.setVisibility(View.VISIBLE);
            description!!.visibility = View.VISIBLE
        }
        weburl = findViewById<View>(R.id.weburl) as TextView
        sendEmail = findViewById<View>(R.id.sendEmail) as ImageView
        sendEmail!!.visibility = View.GONE




        mAboutUsList!!.setHasFixedSize(true)
        val recyclerViewLayout = GridLayoutManager(mContext, 4)
        val spacing = 5 // 50px

        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
        mAboutUsList!!.addItemDecoration(itemDecoration)
        mAboutUsList!!.layoutManager = recyclerViewLayout
        if (bannerimg != "") {
            Glide.with(mContext).load(AppUtils.replace(bannerimg!!)).centerCrop()
                .into(bannerImagePager!!)
        }
        val mFacilityRecyclerAdapter = FacilityRecyclerAdapter(mContext, mAboutUsListArray)
        mAboutUsList!!.adapter = mFacilityRecyclerAdapter
        mAboutUsList!!.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (mAboutUsListArray!!.size <= 1) {
                    if (mAboutUsListArray!![position]!!.url!!.endsWith(".pdf")) {
                        val intent = Intent(mContext, PDFViewActivity::class.java)
                        intent.putExtra("pdf_url", NaisClassNameConstants.ABOUT_US)
                        startActivity(intent)
                    } else {
                        val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                        intent.putExtra(
                            "url",
                            mAboutUsListArray!![position]!!.url
                        )
                        intent.putExtra("tab_type", NaisClassNameConstants.ABOUT_US)
                        startActivity(intent)
                    }
                } else {
                    if (mAboutUsListArray!![position]!!.url!!.endsWith(".pdf")) {
                        val intent = Intent(mContext, PDFViewActivity::class.java)
                        intent.putExtra(
                            "pdf_url",
                            mAboutUsListArray!![position]!!.url
                        )
                        startActivity(intent)
                    } else {
                        val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                        intent.putExtra(
                            "url",
                            mAboutUsListArray!![position]!!.url
                        )
                        intent.putExtra("tab_type", NaisClassNameConstants.ABOUT_US)
                        startActivity(intent)
                    }
                }
            }

        })


        sendEmail!!.setOnClickListener {
            if (!PreferenceManager.getUserID(mContext)
                    .equals("", ignoreCase = true)
            ) {
                val dialog = Dialog(mContext)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.alert_send_email_dialog)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val dialogCancelButton =
                    dialog.findViewById<View>(R.id.cancelButton) as Button
                val submitButton =
                    dialog.findViewById<View>(R.id.submitButton) as Button
                text_dialog = dialog.findViewById<View>(R.id.text_dialog) as EditText
                text_content = dialog.findViewById<View>(R.id.text_content) as EditText
                text_dialog!!.onFocusChangeListener =
                    View.OnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            text_dialog!!.hint = ""
                            text_dialog!!.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                            text_dialog!!.setPadding(5, 5, 0, 0)
                        } else {
                            text_dialog!!.hint = "Enter your subject here..."
                            text_dialog!!.gravity = Gravity.CENTER
                        }
                    }
                text_content!!.onFocusChangeListener =
                    View.OnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            text_content!!.gravity = Gravity.LEFT
                        } else {
                            text_content!!.gravity = Gravity.CENTER
                        }
                    }
                dialogCancelButton.setOnClickListener { dialog.dismiss() }
                submitButton.setOnClickListener {
                    if (text_dialog!!.text.equals("") ) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity,
                            mContext.getString(R.string.alert_heading),
                            "Please enter subject",
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else if (text_content!!.text.toString() == "") {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity,
                            mContext.getString(R.string.alert_heading),
                            "Please enter content",
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else {
                        emailvalidationcheck( text_dialog!!.text.toString(),
                            text_content!!.text.toString(),dialog)
                       /* if (AppUtils.checkInternet(mContext)) {
                            sendEmailToStaff(dialog)
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity,
                                "Network Error",
                                mContext.getString(R.string.no_internet),
                                R.drawable.nonetworkicon,
                                R.drawable.roundred
                            )
                        }*/
                    }
                }
                dialog.show()
            } else {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity,
                    mContext.getString(R.string.alert_heading),
                    "This feature is available only for registered users. Login/register to see contents.",
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        }
    }
    fun emailvalidationcheck(text_dialog:String,text_content:String,dialog:Dialog){
        val EMAIL_PATTERN :String=
            "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$"
        val pattern :String= "^([a-zA-Z ]*)$"

        if (text_dialog.equals("")) {
            val toast: Toast = Toast.makeText(
                mContext, mContext!!.getResources().getString(
                    R.string.enter_subjects
                ), Toast.LENGTH_SHORT
            )
            toast.show()
        } else {
            if (text_content.equals("")) {
                val toast: Toast = Toast.makeText(
                    mContext, mContext!!.getResources().getString(
                        R.string.enter_contents
                    ), Toast.LENGTH_SHORT
                )
                toast.show()
            } else if (contactEmail!!.matches(EMAIL_PATTERN.toRegex())) {
                if (text_dialog.toString().trim().matches(pattern.toRegex())) {
                    if (text_dialog.toString().length>=500){
                        Toast.makeText(mContext, "Subject is too long", Toast.LENGTH_SHORT).show()

                    }else{
                        if (text_content.toString().trim().matches(pattern.toRegex())) {
                            if (text_content.length<=500) {
                                if (AppUtils.checkInternet(mContext!!)) {
                                    sendEmailToStaff(dialog
                                    )
                                }else{
                                    Toast.makeText(
                                        mContext,
                                        mContext!!.resources.getString(R.string.no_internet),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }else{
                                Toast.makeText(mContext, "Message is too long", Toast.LENGTH_SHORT).show()

                            }

                        } else {
                            val toast: Toast = Toast.makeText(
                                mContext, mContext!!.getResources().getString(
                                    R.string.enter_valid_contents
                                ), Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    }
                } else {
                    val toast: Toast = Toast.makeText(
                        mContext, mContext!!.getResources().getString(
                            R.string.enter_valid_subjects
                        ), Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            } else {
                val toast: Toast = Toast.makeText(
                    mContext, mContext!!.getResources().getString(
                        R.string.enter_valid_mail
                    ), Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }


    }
    private fun sendEmailToStaff(dialog:Dialog) {
        var homebannerbody= SendemailApiModel(contactEmail!!,PreferenceManager.getUserID(mContext),
            text_dialog!!.text.toString(),
            text_content!!.text.toString())
        val call: Call<SendemailResponseModel> = ApiClient.getClient.sendemailstaff("Bearer "+PreferenceManager.getAccessToken(mContext),
            homebannerbody)
        progressBarDialog!!.show()
        call.enqueue(object : Callback<SendemailResponseModel> {
            override fun onResponse(call: Call<SendemailResponseModel>, response: Response<SendemailResponseModel>) {
                progressBarDialog!!.dismiss()
                val responseData = response.body()
                if (response.body()!!.responsecode.equals("200")){
                    progressBarDialog!!.dismiss()
                    var status_code=response.body()!!.response.statuscode

                    if (status_code == "303") {
                        dialog!!.dismiss()
                        val toast = Toast.makeText(
                            mContext,
                            "Successfully sent email to staff",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else {
                        val toast =
                            Toast.makeText(mContext, "Email not sent", Toast.LENGTH_SHORT)
                        toast.show()
                    }

                } else {
                    progressBarDialog!!.dismiss()
                    AppUtils.showDialogAlertDismiss(
                        mContext,
                        "Alert",
                        mContext!!.getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<SendemailResponseModel>, t: Throwable) {
                progressBarDialog!!.dismiss()            }
        })
       /* val call: Call<ResponseBody> = ApiClient.getClient.sendEmailToStaffCall(
            contactEmail!!,
            text_dialog!!.text.toString(),
            text_content!!.text.toString()
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseData = response.body()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData.string())
                    if (jsonObject.has("status")) {
                        val status = jsonObject.optInt("status")
                        if (status == 100) {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?,
                                "Success",
                                "Successfully sent email to staff",
                                R.drawable.tick,
                                R.drawable.round
                            )
                        } else {
                            val toast = Toast.makeText(mContext, "Email not sent", Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                } else {
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        "Alert",
                        (mContext as Activity).getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })*/
    }

}