package com.mobatia.nasmanila.fragments.enrichment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.enrichment.CcaActivity
import com.mobatia.nasmanila.activities.parent_essential.model.SendemailApiModel
import com.mobatia.nasmanila.activities.parent_essential.model.SendemailResponseModel
import com.mobatia.nasmanila.activities.web_view.LoadUrlWebViewActivity
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.DividerItemDecoration
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import com.mobatia.nasmanila.fragments.enrichment.adapter.EnrichmentLessonAdapter
import com.mobatia.nasmanila.fragments.enrichment.model.EnrichmentLessonsDataModel
import com.mobatia.nasmanila.fragments.enrichment.model.Enrichment_lessonsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CcaFragment:Fragment() {
    private var mRootView: View? = null
    lateinit var mContext: Context
    var progressBarDialog: ProgressBarDialog? = null
    var mTitleTextView: TextView? = null
    private var mListView: RecyclerView? = null
    private val mTitle: String? = null
    private val mTabId: String? = null

    //	 ViewPager bannerImagePager;
    var bannerImagePager: ImageView? = null
    private val mBannerImage: ImageView? = null
    var bannerUrlImageArray: ArrayList<String>? = null
    lateinit var newsLetterModelArrayList: ArrayList<EnrichmentLessonsDataModel>
    var relMain: RelativeLayout? = null
    var mtitle: RelativeLayout? = null
    var text_content: TextView? = null
    var text_dialog: TextView? = null
    var description = ""
    var contactEmail = ""
    var descriptionTV: TextView? = null
    var descriptionTitle: TextView? = null
    var sendEmail: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_parent_essentials_list, container,
            false
        )

        mContext = requireActivity()
        progressBarDialog = ProgressBarDialog(mContext!!)
        //		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        initialiseUI()
        return mRootView
    }

    private fun initialiseUI() {
        mtitle = mRootView!!.findViewById<View>(R.id.title) as RelativeLayout
        descriptionTV = mRootView!!.findViewById<View>(R.id.descriptionTV) as TextView
        descriptionTitle = mRootView!!.findViewById<View>(R.id.descriptionTitle) as TextView
        sendEmail = mRootView!!.findViewById<View>(R.id.sendEmail) as ImageView
        mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
        mTitleTextView!!.text = "Enrichment Lessons"
        mListView = mRootView!!.findViewById<View>(R.id.mListView) as RecyclerView
        mListView!!.setNestedScrollingEnabled(false)
        bannerImagePager = mRootView!!.findViewById<View>(R.id.bannerImagePager) as ImageView
        relMain = mRootView!!.findViewById<View>(R.id.relMain) as RelativeLayout
        val llm = LinearLayoutManager(mContext)

        llm.orientation = LinearLayoutManager.VERTICAL
        mListView!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.list_divider)))

        mListView!!.layoutManager = llm
        if (AppUtils.isNetworkConnected(mContext)) {
            getNewslettercategory()
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
        mListView!!.addOnItemClickListener(object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {

//						Intent intent = new Intent(mContext, ParentEssentialActivity.class);
                if (newsLetterModelArrayList.get(position).name
                        .equals("Enrichment Lessons") || newsLetterModelArrayList.get(
                        position
                    ).name.equals("Enrichment Lesson")
                ) {
                    val intent = Intent(mContext, CcaActivity::class.java)
                    intent.putExtra("tab_type", "Enrichment Lessons")
                    mContext.startActivity(intent)
                } else if (newsLetterModelArrayList.get(position).name
                        .equals("Edmodo")
                ) {
                    val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                    intent.putExtra("url", "https://www.edmodo.com/?go2url=%2Fhome")
                    intent.putExtra("tab_type", "Edmodo")
                    startActivity(intent)
                }
            }

        })
sendEmail!!.setOnClickListener {
    if (!PreferenceManager.getAccessToken(mContext).equals("")) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.alert_send_email_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogCancelButton = dialog.findViewById<View>(R.id.cancelButton) as Button
        val submitButton = dialog.findViewById<View>(R.id.submitButton) as Button
        text_dialog = dialog.findViewById<View>(R.id.text_dialog) as EditText
        text_content = dialog.findViewById<View>(R.id.text_content) as EditText
        text_dialog!!.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                text_dialog!!.setHint("")
                text_dialog!!.setGravity(Gravity.LEFT or Gravity.CENTER_VERTICAL)
                text_dialog!!.setPadding(5, 5, 0, 0)
            } else {
                text_dialog!!.setHint("Enter your subject here...")
                text_dialog!!.setGravity(Gravity.CENTER)
            }
        })
        text_content!!.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                text_content!!.setGravity(Gravity.LEFT)
            } else {
                text_content!!.setGravity(Gravity.CENTER)
            }
        })
        dialogCancelButton.setOnClickListener { dialog.dismiss() }
        submitButton.setOnClickListener { //						sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);
            if (text_dialog!!.text.equals("")) {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity,
                    mContext.getString(R.string.alert_heading),
                    "Please enter  subject",
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            } else if (text_content!!.text.equals("")) {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity,
                    mContext.getString(R.string.alert_heading),
                    "Please enter  content",
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            } else {
                emailvalidationcheck( text_dialog!!.text.toString(),
                    text_content!!.text.toString(),dialog)
              /*  if (AppUtils.isNetworkConnected(mContext)) {
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
    private fun sendEmailToStaff(dialog: Dialog) {
        var homebannerbody = SendemailApiModel(
            contactEmail,
            text_dialog!!.getText().toString(), text_content!!.text.toString()
        )
        val call: Call<SendemailResponseModel> = ApiClient.getClient.sendemailstaff(
            "Bearer " + PreferenceManager.getAccessToken(mContext),
            homebannerbody
        )
        progressBarDialog!!.show()
        call.enqueue(object : Callback<SendemailResponseModel> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: Call<SendemailResponseModel>, response: Response<SendemailResponseModel>) {
                progressBarDialog!!.dismiss()
                val responseData = response.body()
                if (response.body()!!.responsecode.equals("200")){
                    progressBarDialog!!.dismiss()
                    var status_code=response.body()!!.response.statuscode

                        if (status_code == "303") {
                            dialog.dismiss()
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
    }

    private fun getNewslettercategory() {
        progressBarDialog!!.show()
        newsLetterModelArrayList= ArrayList()
       
        val call: Call<Enrichment_lessonsModel> = ApiClient.getClient.enrichment_lessons("Bearer "+ PreferenceManager.getAccessToken(mContext))
        call.enqueue(object : Callback<Enrichment_lessonsModel> {
            override fun onResponse(
                call: Call<Enrichment_lessonsModel>,
                response: Response<Enrichment_lessonsModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {

                        val bannerImage: String = responseData!!.response.banner_image
                        description = responseData!!.response.description
                        contactEmail = responseData!!.response.contact_email
                        if (!bannerImage.equals("")) {
                            Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop()
                                .placeholder(R.drawable.default_bannerr)
                                .error(R.drawable.default_bannerr).into(
                                    bannerImagePager!!
                                )

                        } else {
                            bannerImagePager!!.setImageResource(R.drawable.default_bannerr)

                        }
                        if (description.equals("") && contactEmail.equals(
                                ""
                            )
                        ) {
                            mtitle!!.visibility = View.GONE
                        } else {
                            mtitle!!.visibility = View.VISIBLE
                        }
                        if (description.equals("")) {
                            descriptionTV!!.visibility = View.GONE
                            descriptionTitle!!.visibility = View.GONE
                        } else {
                            descriptionTV!!.text = description
                            descriptionTitle!!.visibility = View.GONE
                            //			descriptionTitle.setVisibility(View.VISIBLE);
                            descriptionTV!!.visibility = View.VISIBLE
                            mtitle!!.visibility = View.VISIBLE
                        }
                        if (contactEmail.equals("")) {
                            sendEmail!!.visibility = View.GONE
                        } else {
                            sendEmail!!.visibility = View.VISIBLE
                            mtitle!!.visibility = View.VISIBLE
                        }
                        if (responseData!!.response.data.size > 0) {
                            newsLetterModelArrayList.addAll(responseData!!.response.data)
                           /* for (i in 0 until responseData!!.response.data.size) {
                                val item: EnrichmentEssentialsModel =
                                    apiResponse.getResponse().getData().get(i)
                                val gson = Gson()
                                val eventJson = gson.toJson(item)
                                try {
                                    val jsonObject = JSONObject(eventJson)
                                    newsLetterModelArrayList.add(addNewsLetterDetails(jsonObject))

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }*/
                            val newsLetterAdapter =
                                EnrichmentLessonAdapter(mContext, newsLetterModelArrayList)
                            mListView!!.adapter = newsLetterAdapter
                        } else {
                            Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show()
                        }

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

            override fun onFailure(call: Call<Enrichment_lessonsModel>, t: Throwable) {

            }
        })
    }
}