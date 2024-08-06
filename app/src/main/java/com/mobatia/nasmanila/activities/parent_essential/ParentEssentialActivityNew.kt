package com.mobatia.nasmanila.activities.parent_essential

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
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
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.activities.parent_essential.adapter.ParentEssentialActivityListAdapterNew
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
import com.mobatia.nasmanila.fragments.parent_essentials.model.SubmenuParentEssentials
import com.mobatia.nasmanila.manager.recyclermanager.ItemOffsetDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class ParentEssentialActivityNew : AppCompatActivity() {
    var extras: Bundle? = null
    var list: ArrayList<SubmenuParentEssentials?>? = null
    var tab_type: String? = null
    var tab_typeName: String? = null
    var mContext: Context = this
    var mNewsLetterListView: RecyclerView? = null
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null
    var mailImageView: ImageView? = null
    var descriptionTV: TextView? = null
    var descriptionTitle: TextView? = null
    var bannerImagePager: ImageView? = null
    var progressBarDialog: ProgressBarDialog? = null
    //    ViewPager bannerImagePager;
    var bannerUrlImageArray: ArrayList<String>? = null
    var bannerImage = ""
    var contactEmail = ""
    var description = ""
    var text_content: EditText? = null
    var text_dialog: EditText? = null
    var downloadURL: URL? = null
    var downloadFileName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_essential)
        initUI()
        setListAdapter()
    }

    private fun setListAdapter() {
        val newsDetailListAdapter = ParentEssentialActivityListAdapterNew(mContext, list)
        mNewsLetterListView!!.adapter = newsDetailListAdapter
    }

    private fun initUI() {
        extras = intent.extras
        if (extras != null) {
            list = PreferenceManager.getparentEssentials(mContext!!)
            tab_type = extras!!.getString("tab_type")
            bannerImage = extras!!.getString("bannerImage")!!
            contactEmail = extras!!.getString("contactEmail")!!
            description = extras!!.getString("description")!!
            tab_typeName = extras!!.getString("tab_typeName")
        }
        progressBarDialog = ProgressBarDialog(mContext!!)
        relativeHeader = findViewById<RelativeLayout>(R.id.relativeHeader) as RelativeLayout
        descriptionTV = findViewById<View>(R.id.descriptionTV) as TextView
        descriptionTitle = findViewById<View>(R.id.descriptionTitle) as TextView
        mailImageView = findViewById<View>(R.id.mailImageView) as ImageView
        bannerImagePager = findViewById<View>(R.id.bannerImagePager) as ImageView
//        bannerImagePager= (ViewPager) findViewById(R.id.bannerImagePager);
        //        bannerImagePager= (ViewPager) findViewById(R.id.bannerImagePager);
        mNewsLetterListView = findViewById<View>(R.id.mListView) as RecyclerView
        headermanager = HeaderManager(this, tab_type)
        headermanager!!.getHeader(relativeHeader, 0)
        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        back!!.setOnClickListener {
            AppUtils.hideKeyBoard(mContext)
            finish()
        }
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(mContext, HomeListAppCompatActivity::class.java)
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }

        if (!bannerImage.equals("")) {
            Glide.with(mContext) //1
                .load(bannerImage)
                .into(bannerImagePager!!)

           /* Glide.with(mContext).load(AppUtils.replace(bannerImage))
                .placeholder(R.drawable.default_bannerr).error(R.drawable.default_bannerr)
                .into(bannerImagePager!!)*/
        } else {
            Glide.with(mContext!!).load(R.drawable.default_bannerr).centerCrop().into(bannerImagePager!!)

            //  bannerImagePager!!.setBackgroundResource(R.drawable.default_bannerr)
//            bannerImagePager.setBackgroundResource(R.drawable.demo);
        }
        if (!description.equals("", ignoreCase = true)) {
            descriptionTV!!.visibility = View.VISIBLE
            descriptionTV!!.text = description
            descriptionTitle!!.visibility = View.GONE
//			descriptionTitle.setVisibility(View.VISIBLE);
        } else {
            descriptionTV!!.visibility = View.GONE
            descriptionTitle!!.visibility = View.GONE
        }
        if (!contactEmail.equals("", ignoreCase = true) && !PreferenceManager.getAccessToken(
                mContext
            )
                .equals("")
        ) {
            mailImageView!!.visibility = View.VISIBLE
        } else {
            mailImageView!!.visibility = View.INVISIBLE
        }
        mailImageView!!.setOnClickListener {
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
            dialogCancelButton.setOnClickListener { dialog.dismiss() }
            submitButton.setOnClickListener {
                println("submit btn clicked")
                //                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);
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
                    emailvalidationcheck(text_dialog!!.getText().toString(),text_content!!.text.toString(),dialog)
                   /* if (AppUtils.isNetworkConnected(mContext)) {
                        sendEmailToStaff( dialog)
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
        }

        mNewsLetterListView!!.setHasFixedSize(true)
//        mNewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        //        mNewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        val recyclerViewLayout = GridLayoutManager(mContext, 4)
        val spacing = 5 // 50px

        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
        mNewsLetterListView!!.addItemDecoration(itemDecoration)
        mNewsLetterListView!!.layoutManager = recyclerViewLayout
        mNewsLetterListView!!.addOnItemClickListener(object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                if (list!!.size <= 1) {
                    if (list!![position]!!.filename.endsWith(".pdf")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            var intent:Intent =  Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(list!![position]!!.filename), "application/pdf");

                            startActivity(intent);
                           /* startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(list!![position]!!.filename)
                                )
                            )*/
                        } else {
                            val intent = Intent(mContext, PDFViewActivity::class.java)
                            intent.putExtra("pdf_url", list!![position]!!.filename)
                            startActivity(intent)
                        }
                    } else {
                        val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                        intent.putExtra("url", list!![position]!!.filename)
                        intent.putExtra("tab_type", tab_type)
                        startActivity(intent)
                    }
                } else {
                    if (list!![position]!!.filename.endsWith(".pdf") && tab_typeName.equals(
                            "Lunch Menu",
                            ignoreCase = true
                        )
                    ) {

                        /*intent.putExtra("position", position);*/
//                                downloadFromUrl(list.get(position).getFilename());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            var intent:Intent =  Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(list!![position]!!.filename), "application/pdf");

                            startActivity(intent);
                           /* startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(list!![position]!!.filename)
                                )
                            )*/
                        } else {
                            val intent = Intent(mContext, PDFViewActivity::class.java)
                            intent.putExtra("pdf_url", list!![position]!!.filename)
                            startActivity(intent)
                        }
                    } else if (list!![position]!!.filename.endsWith(".pdf")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            var intent:Intent =  Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(list!![position]!!.filename), "application/pdf");

                            startActivity(intent);
                          /*  startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(list!![position]!!.filename)
                                )
                            )*/
                        } else {
                            val intent = Intent(mContext, PDFViewActivity::class.java)
                            intent.putExtra("pdf_url", list!![position]!!.filename)
                            startActivity(intent)
                        }
                    } else {
                        val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                        intent.putExtra("url", list!![position]!!.filename)
                        intent.putExtra("tab_type", tab_type)
                        startActivity(intent)
                    }
                }
            }

        }
        )

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
                    if (status_code.equals("303")){
                        dialog.dismiss()
                        val toast = Toast.makeText(
                            mContext,
                            "Successfully sent email to staff",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()

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
}


