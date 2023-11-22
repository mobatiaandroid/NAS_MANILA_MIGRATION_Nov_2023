package com.mobatia.nasmanila.activities.contact_us.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.contact_us.model.StaffModelDept
import com.mobatia.nasmanila.activities.parent_essential.model.SendemailApiModel
import com.mobatia.nasmanila.activities.parent_essential.model.SendemailResponseModel
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomStaffDeptRecyclerAdapter(
    var mContext: Context, var mStaffList: ArrayList<StaffModelDept>,var dept:String

) :
    RecyclerView.Adapter<CustomStaffDeptRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.custom_adapter_stafflist_item, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       var pos = position
        System.out.println("Inside list item adapter---" + mStaffList.get(position).name)
        System.out.println("Inside list item adapter---" + mStaffList.get(position).staffEmail)
        println("position per mail$position")

        if (dept == "") {
            holder.deptLayout.visibility = View.GONE
            holder.separator.visibility = View.GONE
        } else if (dept == "list") {
            //holder.departmentName.setText(mStaffList.get(position).getStaffDepartment());
            holder.departmentName.text = dept
            holder.separator.visibility = View.VISIBLE
            holder.deptLayout.visibility = View.GONE
        } else {
            //holder.departmentName.setText(dept);
            holder.deptLayout.visibility = View.GONE
            holder.separator.visibility = View.GONE
        }

        if (!mStaffList.get(position).staff_photo.equals("")) {
            Glide.with(mContext).load(mStaffList.get(position).staff_photo)

                .into(holder.staffImg)
        }
        holder.staffName.setText(mStaffList.get(position).name)
        holder.staffRole.setText(mStaffList.get(position).role)
        holder.mail.setOnClickListener {
            if (!PreferenceManager.getUserID(mContext).equals("")) {
               var dialog = Dialog(mContext)
   
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.alert_send_email_dialog)
                dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                var text_dialog = dialog.findViewById<View>(R.id.text_dialog) as EditText
                var text_content = dialog.findViewById<View>(R.id.text_content) as EditText
                //                    text_dialog.setFocusable(true);
                text_dialog.setFocusableInTouchMode(true)
                text_content.setFocusableInTouchMode(true)
                // text_dialog.setSelection(0);
                //text_content.setSelection(0);
                text_dialog.setOnFocusChangeListener(OnFocusChangeListener { v12: View?, hasFocus: Boolean ->
                    if (hasFocus) {
                        text_dialog.setHint("")
                        text_dialog.setGravity(Gravity.LEFT or Gravity.CENTER_VERTICAL)
                        text_dialog.setPadding(5, 5, 0, 0)
                    } else {
                        text_dialog.setHint("Enter your subject here...")
                        text_dialog.setGravity(Gravity.CENTER)
                    }
                })
                text_content.setOnFocusChangeListener(OnFocusChangeListener { v1: View?, hasFocus: Boolean ->
                    if (hasFocus) {
                        text_content.setGravity(Gravity.LEFT)
                    } else {
                        text_content.setGravity(Gravity.CENTER)
                    }
                })
                dialog.findViewById<View>(R.id.cancelButton).setOnClickListener { dialog.dismiss() }
                dialog.findViewById<View>(R.id.submitButton).setOnClickListener {
                    println("submit btn clicked")
                    /* if (AppUtils.isNetworkConnected(mContext)) {
                                                          if (text_content.equals("")) {
                                                              AppUtils.setErrorForEditText(text_content, mContext.getString(R.string.mandatory_field));
                                                          } else if (text_dialog.equals("")) {
                                                              AppUtils.setErrorForEditText(text_dialog, mContext.getString(R.string.mandatory_field));

                                                          } else {
                                                              sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);
                                                          }
                                                      } else {
                                                          AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                                      }*/if (text_dialog.getText()
                        .toString() == ""
                ) {
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity,
                        mContext.getString(R.string.alert_heading),
                        "Please enter subject",
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                } else if (text_content.getText().toString() == "") {
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity,
                        mContext.getString(R.string.alert_heading),
                        "Please enter content",
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                } else {
                    if (AppUtils.isNetworkConnected(mContext)) {
                        println("clicked position count$position")
                        System.out.println("Email id Passing" + mStaffList[position].staffEmail)
                        sendEmailToStaff(
                            
                            mStaffList[position].staffEmail,text_dialog.text.toString(),text_content.text.toString(),dialog
                        )
                    } else {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity,
                            "Network Error",
                            mContext.getString(R.string.no_internet),
                            R.drawable.nonetworkicon,
                            R.drawable.roundred
                        )
                    }
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

    private fun sendEmailToStaff(staffEmail: String?,text_dialog:String,text_content:String,dialog:Dialog) {
        var homebannerbody= SendemailApiModel(
            staffEmail.toString(),PreferenceManager.getUserID(mContext).toString(),
            text_dialog,text_content)
        val call: Call<SendemailResponseModel> = ApiClient.getClient.sendemailstaff("Bearer "+PreferenceManager.getAccessToken(mContext),
            homebannerbody)
       // progressBarDialog!!.show()
        call.enqueue(object : Callback<SendemailResponseModel> {
            override fun onResponse(call: Call<SendemailResponseModel>, response: Response<SendemailResponseModel>) {
               // progressBarDialog!!.dismiss()
                val responseData = response.body()
                if (response.body()!!.responsecode.equals("200")){
                    //progressBarDialog!!.dismiss()
                    var status_code=response.body()!!.response.statuscode
                    if (status_code.equals("303")){

                        dialog.dismiss()
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity,
                            "Success",
                            "Successfully sent email to staff",
                            R.drawable.tick,
                            R.drawable.round
                        )

                    } else if (status_code.equals("301")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext,
                            mContext.getString(R.string.error_heading),
                            mContext.getString(R.string.missing_parameter),
                            R.drawable.infoicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("304")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            mContext.getString(R.string.error_heading),
                            mContext.getString(R.string.email_exists),
                            R.drawable.infoicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("305")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            mContext.getString(R.string.error_heading),
                            mContext.getString(R.string.incrct_usernamepswd),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("306")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            mContext.getString(R.string.error_heading),
                            mContext.getString(R.string.invalid_email),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            mContext.getString(R.string.error_heading),
                            mContext.getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }

                } else {
                    //progressBarDialog!!.dismiss()
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
                Log.e("failed", t.toString())
                      }
        })
    }

    override fun getItemCount(): Int {
        return mStaffList!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var departmentName: TextView
        var staffName:TextView
        var staffRole:TextView
        var deptLayout: LinearLayout
        var separator: View
        var mail: ImageView
        var staffImg:ImageView

        init {

            departmentName = view.findViewById(R.id.departmentName) as TextView
            staffName = view.findViewById(R.id.staffName) as TextView
            deptLayout = view.findViewById(R.id.deptLayout) as LinearLayout
            separator = view.findViewById(R.id.separator) as View
            mail = view.findViewById(R.id.mailImage) as ImageView
            staffImg = view.findViewById(R.id.staffImg) as ImageView
            staffRole = view.findViewById(R.id.staffRole) as TextView
        }
    }
}