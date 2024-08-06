package com.mobatia.nasmanila.activities.enrichment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.Window
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.enrichment.adapter.CCARecyclerAdapter

import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.activities.parent_essential.model.SendemailApiModel
import com.mobatia.nasmanila.activities.parent_essential.model.SendemailResponseModel
import com.mobatia.nasmanila.activities.pdf.PDFViewActivity
import com.mobatia.nasmanila.activities.web_view.LoadUrlWebViewActivity
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import com.mobatia.nasmanila.manager.recyclermanager.ItemOffsetDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CcaActivity:AppCompatActivity(), AdapterView.OnItemClickListener {
    lateinit var mContext: Context
    var progressBarDialog: ProgressBarDialog? = null
    var relativeHeader: RelativeLayout? = null

    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null
    private val mTitle: String? = null
    private val mTabId: String? = null
    private var description = ""
    private var relMain: RelativeLayout? = null
    var descriptionTV: TextView? = null
    var descriptionTitle: TextView? = null
    var mtitleRel: RelativeLayout? = null
    var CCAFRegisterRel: RelativeLayout? = null
    private var mListViewArray: ArrayList<CcaDataModel>? = null
    var bannerImagePager: ImageView? = null
    var bannerUrlImageArray: ArrayList<String>? = null
    private var mListView: RecyclerView? = null
    var contactEmail = ""
    var text_dialog: EditText? = null
    var text_content: EditText? = null
    var sendEmail: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_ccas_list)
        mContext = this
        progressBarDialog = ProgressBarDialog(mContext!!)
        initialiseUI()

    }

    private fun initialiseUI() {
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        headermanager = HeaderManager(this@CcaActivity, "Enrichment Lessons")
        headermanager!!.getHeader(relativeHeader, 0)
        back = headermanager!!.getLeftButton()
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(mContext, HomeListAppCompatActivity::class.java)
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )

        back!!.setOnClickListener { v: View? -> finish() }
//		mListView = (ListView) mRootView.findViewById(R.id.mListView);
        //		mListView = (ListView) mRootView.findViewById(R.id.mListView);
        bannerImagePager = findViewById<View>(R.id.bannerImagePager) as ImageView
        mListView = findViewById<View>(R.id.mListView) as RecyclerView
        descriptionTV = findViewById<View>(R.id.descriptionTV) as TextView
        descriptionTitle = findViewById<View>(R.id.descriptionTitle) as TextView
        sendEmail = findViewById<View>(R.id.sendEmail) as ImageView
        mtitleRel = findViewById<View>(R.id.title) as RelativeLayout
        CCAFRegisterRel = findViewById<View>(R.id.CCAFRegisterRel) as RelativeLayout
        relMain = findViewById<View>(R.id.relMain) as RelativeLayout
        CCAFRegisterRel!!.setOnClickListener{
            if (PreferenceManager.getAccessToken(mContext).equals("")) {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity,
                    "Alert",
                    "This feature is available only for registered users. Login/register to see contents.",
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            } else {
                val intent = Intent(mContext, CCA_Activity::class.java)
                intent.putExtra("tab_type", "Enrichment Lessons")
                mContext.startActivity(intent)
            }
        }
        //////////////////////////////
        //mListView.setOnItemClickListener(this);
        if (AppUtils.checkInternet(mContext)) {
            getList()
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
        /////////////////////////
        /////////////////////////
        mListView!!.setHasFixedSize(true)
//        mNewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        //        mNewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        val recyclerViewLayout = GridLayoutManager(mContext, 4)
        val spacing = 5 // 50px

        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
        mListView!!.addItemDecoration(itemDecoration)
        mListView!!.layoutManager = recyclerViewLayout
        mListView!!.addOnItemClickListener(object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                if (mListViewArray!!.get(position).file.endsWith(".pdf")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        var intent:Intent =  Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(mListViewArray!!.get(position).file), "application/pdf");

                        startActivity(intent);
                       /* startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(mListViewArray!!.get(position).file)
                            )
                        )*/
                    } else {
                        val intent = Intent(mContext, PDFViewActivity::class.java)
                        intent.putExtra("pdf_url", mListViewArray!!.get(position).file)
                        startActivity(intent)
                    }
//                            Intent intent = new Intent(mContext, PDFViewActivity.class);
//                            intent.putExtra("pdf_url", mListViewArray.get(position).getmFile());
//                            startActivity(intent);
                } else {
                    val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                    intent.putExtra("url", mListViewArray!!.get(position).file)
                    intent.putExtra("tab_type", "Enrichment Lessons")
                    mContext.startActivity(intent)
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
                text_dialog!!.onFocusChangeListener =
                    OnFocusChangeListener { v, hasFocus ->
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
                    OnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            text_content!!.gravity = Gravity.LEFT
                        } else {
                            text_content!!.gravity = Gravity.CENTER
                        }
                    }
                dialogCancelButton.setOnClickListener { dialog.dismiss() }
                submitButton.setOnClickListener {
                    println("submit btn clicked")
                    //						sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);
                    if (text_dialog!!.text.toString() == "") {
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
                        emailvalidationcheck( text_dialog!!.getText().toString(),text_content!!.text.toString(),dialog)
                       /* if (AppUtils.isNetworkConnected(mContext)) {
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
        PreferenceManager.setStudIdForCCA(mContext, "")
        PreferenceManager.setCCAStudentIdPosition(mContext, "0")
        PreferenceManager.setStudNameForCCA(mContext, "")
        PreferenceManager.setStudClassForCCA(mContext, "")
        PreferenceManager.setCCATitle(mContext, "")
        PreferenceManager.setCCAItemId(mContext, "")
    }

    private fun getList() {
        progressBarDialog!!.show()

        mListViewArray= ArrayList()
        val call: Call<CcaModel> = ApiClient.getClient.cca("Bearer "+ PreferenceManager.getAccessToken(mContext))
        call.enqueue(object : Callback<CcaModel> {
            override fun onResponse(
                call: Call<CcaModel>,
                response: Response<CcaModel>
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
                                .into(bannerImagePager!!)

//											bannerUrlImageArray = new ArrayList<>();
//											bannerUrlImageArray.add(bannerImage);
//											bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray, getActivity()));
                        } else {
                            bannerImagePager!!.setBackgroundResource(R.drawable.default_bannerr)
//											bannerImagePager.setBackgroundResource(R.drawable.ccas_banner);
                        }

                        if (description.equals("") && contactEmail.equals(
                                ""
                            )
                        ) {
                            mtitleRel!!.visibility = View.GONE
                        } else {
                            mtitleRel!!.visibility = View.VISIBLE
                        }
                        if (description.equals("")) {
                            descriptionTV!!.visibility = View.GONE
                            descriptionTitle!!.visibility = View.GONE
                        } else {
                            descriptionTV!!.text = description
                            descriptionTitle!!.visibility = View.GONE
                            //											descriptionTitle.setVisibility(View.VISIBLE);
                            descriptionTV!!.visibility = View.VISIBLE
                            mtitleRel!!.visibility = View.VISIBLE
                        }
                        if (contactEmail.equals("")) {
                            sendEmail!!.visibility = View.GONE
                        } else {
                            mtitleRel!!.visibility = View.VISIBLE
                            sendEmail!!.visibility = View.VISIBLE
                        }
                        CCAFRegisterRel!!.visibility = View.VISIBLE
                        //JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                        //JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                        if (responseData!!.response.data.size > 0) {
                            /*for (i in 0 until apiResponse.getResponse().getData().size()) {
                                //JSONObject dataObject = dataArray.getJSONObject(i);
                                val item: SecondaryModel =
                                    apiResponse.getResponse().getData().get(i)
                                val gson = Gson()
                                val eventJson = gson.toJson(item)
                                try {
                                    val jsonObject = JSONObject(eventJson)
                                    mListViewArray!!.add(getSearchValues(jsonObject))
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }*/
                            mListViewArray!!.addAll(responseData!!.response.data)

//											mListView.setAdapter(new CustomSecondaryAdapter(getActivity(), mListViewArray));
                            mListView!!.adapter = CCARecyclerAdapter(mContext, mListViewArray!!)
                        } else {
                            //CustomStatusDialog();
                            //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity,
                                "Alert",
                                getString(R.string.nodatafound),
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
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

            override fun onFailure(call: Call<CcaModel>, t: Throwable) {

            }
        })
    }
    fun emailvalidationcheck(text_dialog:String,text_content:String,dialog:Dialog){
        val EMAIL_PATTERN :String=
            "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$"
        val pattern :String= "^([a-zA-Z ]*)$"

        if (text_dialog.equals("")) {
            val toast: Toast = Toast.makeText(
                mContext, mContext.getResources().getString(
                    R.string.enter_subjects
                ), Toast.LENGTH_SHORT
            )
            toast.show()
        } else {
            if (text_content.equals("")) {
                val toast: Toast = Toast.makeText(
                    mContext, mContext.getResources().getString(
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
                                if (AppUtils.checkInternet(mContext)) {
                                    sendEmailToStaff(
                                       dialog
                                    )
                                }else{
                                    Toast.makeText(
                                        mContext,
                                        mContext.resources.getString(R.string.no_internet),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }else{
                                Toast.makeText(mContext, "Message is too long", Toast.LENGTH_SHORT).show()

                            }

                        } else {
                            val toast: Toast = Toast.makeText(
                                mContext, mContext.getResources().getString(
                                    R.string.enter_valid_contents
                                ), Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    }
                } else {
                    val toast: Toast = Toast.makeText(
                        mContext, mContext.getResources().getString(
                            R.string.enter_valid_subjects
                        ), Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            } else {
                val toast: Toast = Toast.makeText(
                    mContext, mContext.getResources().getString(
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

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (mListViewArray!!.get(position).file.endsWith(".pdf")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(mListViewArray!!.get(position).file)
                    )
                )
            } else {
                val intent = Intent(mContext, PDFViewActivity::class.java)
                intent.putExtra("pdf_url", mListViewArray!!.get(position).file)
                startActivity(intent)
            }
//            Intent intent = new Intent(mContext, PDFViewActivity.class);
//            intent.putExtra("pdf_url", mListViewArray.get(position).getmFile());
//            startActivity(intent);
        } else {
            val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
            intent.putExtra("url", mListViewArray!!.get(position).file)
            intent.putExtra("tab_type", mListViewArray!!.get(position).name)
            mContext.startActivity(intent)
        }
    }
}
