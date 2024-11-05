package com.mobatia.nasmanila.fragments.parents_meeting

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.parentevening.ParentsEveningCalendarActivity
import com.mobatia.nasmanila.activities.parentevening.ReviewAppointmentsRecyclerViewActivity
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.DividerItemDecoration
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.absence.model.StudentModel
import com.mobatia.nasmanila.fragments.absence.model.StudentlistApiModel
import com.mobatia.nasmanila.fragments.absence.model.StudentlistResponseModel
import com.mobatia.nasmanila.fragments.parents_meeting.adapter.ParentsEveningStaffAdapter
import com.mobatia.nasmanila.fragments.parents_meeting.adapter.ParentsEveningStudentAdapter
import com.mobatia.nasmanila.fragments.parents_meeting.model.SendemailstaffptaApiModel
import com.mobatia.nasmanila.fragments.parents_meeting.model.SendemailstaffptaResponseModel
import com.mobatia.nasmanila.fragments.parents_meeting.model.StaffPtaModel
import com.mobatia.nasmanila.fragments.parents_meeting.model.StafflistApiModel
import com.mobatia.nasmanila.fragments.parents_meeting.model.StafflistResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParentsEveningFragment:Fragment() {
    private var mRootView: View? = null
    lateinit var mContext: Context
    var progressBarDialog: ProgressBarDialog? = null
    private val mAboutUsList: ListView? = null
    private val mTitle: String? = null
    private val mTabId: String? = null
    private var mStaffId = ""
    private var mStudentId = ""
    private var mStudentName = ""
    private var mStaffName = ""
    private var mClass = ""
    private var staffEmail = ""
    private var staffRelative: RelativeLayout? =
        null
    private  var studentRelative: RelativeLayout? = null
    private  var relMain: RelativeLayout? = null
    private var selectStaffImgView: ImageView? =
        null
    private  var selectStudentImgView: ImageView? = null
    private  var next: ImageView? = null
    private  var infoImg: ImageView? = null
    private  var reviewImageView: ImageView? = null
    var mTitleTextView: TextView? =
        null
    var studentNameTV: TextView? = null
    var staffNameTV: TextView? = null
//    var contactTeacher: TextView? = null

    //	private CustomAboutUsAdapter mAdapter;
    //	private ArrayList<AboutUsModel> mAboutUsListArray;
    var mListViewArray: ArrayList<StudentModel>? = null
    var mListViewStaffArray: ArrayList<StaffPtaModel>? = null
    var dialog: Dialog? = null
    var text_dialog: EditText? = null
    var text_content: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_parents_meeting, container,
            false
        )

        mContext = requireActivity()
        progressBarDialog = ProgressBarDialog(mContext!!)
        initialiseUI()
        if (AppUtils.isNetworkConnected(mContext)) {
            getStudentList()
        } else {
            AppUtils.showDialogAlertDismiss(
                activity,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
        return mRootView
    }
    private fun getStudentList() {
        mListViewArray= ArrayList()
        progressBarDialog!!.show()
        var student = StudentlistApiModel()
        val call: Call<StudentlistResponseModel> = ApiClient.getClient.studentlist(
            "Bearer " + PreferenceManager.getAccessToken(mContext),
            student
        )
        call.enqueue(object : Callback<StudentlistResponseModel> {
            override fun onResponse(
                call: Call<StudentlistResponseModel>,
                response: Response<StudentlistResponseModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {
                        if (responseData!!.response.data.size > 0) {
                            mListViewArray!!.addAll(responseData!!.response.data)
                            /* for (i in 0 until responseData!!.response.data.size) {
                                 val item: StudentModel =responseData!!.response.data.get(i)
                                 val gson = Gson()
                                 val eventJson = gson.toJson(item)
                                 try {
                                     val jsonObject = JSONObject(eventJson)
                                     mListViewArray!!.add(getSearchValues(jsonObject))
                                 } catch (e: JSONException) {
                                     e.printStackTrace()
                                 }
                             }*/
                        } else {
                            //CustomStatusDialog();
                            Toast.makeText(mContext, "No Student Found.", Toast.LENGTH_SHORT).show()
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

            override fun onFailure(call: Call<StudentlistResponseModel>, t: Throwable) {

            }
        })
    }

    private fun initialiseUI() {
        Log.e("Parents Evening Fragment", "Here")

        mTitleTextView = mRootView!!.findViewById(R.id.titleTextView)
        studentNameTV = mRootView!!.findViewById(R.id.studentNameTV)
        staffNameTV = mRootView!!.findViewById(R.id.staffNameTV)
        selectStaffImgView = mRootView!!.findViewById(R.id.selectStaffImgView)
        next = mRootView!!.findViewById(R.id.next)
        next!!.visibility = View.VISIBLE
//        contactTeacher = mRootView!!.findViewById(R.id.contactTeacher)
        selectStudentImgView = mRootView!!.findViewById(R.id.selectStudentImgView)
        studentRelative = mRootView!!.findViewById(R.id.studentRelative)
        staffRelative = mRootView!!.findViewById(R.id.staffRelative)
        mTitleTextView!!.setText(NaisClassNameConstants.PARENT_EVENING)
        relMain = mRootView!!.findViewById(R.id.relMain)
        reviewImageView = mRootView!!.findViewById(R.id.reviewImageView)
        infoImg = mRootView!!.findViewById(R.id.infoImg)

        selectStaffImgView!!.setOnClickListener {
            if (mListViewStaffArray!!.size > 0) {
                showStaffList()
            } else {
                Toast.makeText(mContext, "No Staff Found.", Toast.LENGTH_SHORT).show()
            }
        }
        selectStudentImgView!!.setOnClickListener {
            if (mListViewArray!!.size > 0) {
                showStudentList()
            } else {
                Toast.makeText(mContext, "No Student Found.", Toast.LENGTH_SHORT).show()
            }
        }

        next!!.setOnClickListener {
            val mIntent = Intent(activity, ParentsEveningCalendarActivity::class.java)
            mIntent.putExtra("staff_id", mStaffId)
            mIntent.putExtra("student_id", mStudentId)
            mIntent.putExtra("studentName", mStudentName)
            mIntent.putExtra("staffName", mStaffName)
            mIntent.putExtra("studentClass", mClass)
            mContext.startActivity(mIntent)
        }
        reviewImageView!!.setOnClickListener {
            val mIntent = Intent(activity, ReviewAppointmentsRecyclerViewActivity::class.java)

            mContext.startActivity(mIntent)
        }

    }

    private fun showStaffList() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_student_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<View>(R.id.btn_dismiss) as Button
        val iconImageView = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        val socialMediaList =
            dialog.findViewById<View>(R.id.recycler_view_social_media) as RecyclerView
        iconImageView.setImageResource(R.drawable.girl)
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            iconImageView.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.girl))
            dialogDismiss.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.buttonsubmit_new))
        } else {
            iconImageView.background = mContext.resources.getDrawable(R.drawable.girl)
            dialogDismiss.background = mContext.resources.getDrawable(R.drawable.buttonsubmit_new)
        }

        socialMediaList.addItemDecoration(DividerItemDecoration(mContext.resources.getDrawable(R.drawable.list_divider)))

        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm

        val socialMediaAdapter = ParentsEveningStaffAdapter(mContext, mListViewStaffArray!!)
        socialMediaList.adapter = socialMediaAdapter

        dialogDismiss.setOnClickListener {
            dialog.dismiss()
        }
        socialMediaList!!.addOnItemClickListener(object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                selectStaffImgView!!.setImageResource(R.drawable.staff)
                staffNameTV!!.setText(mListViewStaffArray!![position].name)
                mStaffId = mListViewStaffArray!![position].id
                mStaffName = mListViewStaffArray!![position].name
                mStaffName = mListViewStaffArray!![position].name
                staffEmail = mListViewStaffArray!![position].staff_email
                if (!mListViewStaffArray!![position].staff_photo.equals("")) {
                    Glide.with(mContext).load(
                        AppUtils.replace(
                            mListViewStaffArray!![position].staff_photo.toString()
                        )
                    ).placeholder(R.drawable.staff).error(R.drawable.staff)
                        .into(selectStaffImgView!!)
                } else {
                    selectStaffImgView!!.setImageResource(R.drawable.staff)
                }
                dialog.dismiss()
            }

        })
        dialog.show()
    }

    private fun showStudentList() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_student_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<View>(R.id.btn_dismiss) as Button
        val iconImageView = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        val socialMediaList =
            dialog.findViewById<View>(R.id.recycler_view_social_media) as RecyclerView
        iconImageView.setImageResource(R.drawable.boy)


        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            iconImageView.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.boy))
            dialogDismiss.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.buttonsubmit_new))
        } else {
            iconImageView.background = mContext.resources.getDrawable(R.drawable.boy)
            dialogDismiss.background = mContext.resources.getDrawable(R.drawable.buttonsubmit_new)
        }

        socialMediaList.addItemDecoration(DividerItemDecoration(mContext.resources.getDrawable(R.drawable.list_divider)))

        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm

        val socialMediaAdapter = ParentsEveningStudentAdapter(mContext, mListViewArray!!)
        socialMediaList.adapter = socialMediaAdapter

        dialogDismiss.setOnClickListener {
            dialog.dismiss()
        }
        socialMediaList.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                selectStudentImgView!!.setImageResource(R.drawable.student)
                studentNameTV!!.setText(mListViewArray!![position].name)
                mStudentId = mListViewArray!![position].id
                mStudentName = mListViewArray!![position].name
                mClass = mListViewArray!![position].mClass
                staffRelative!!.visibility = View.INVISIBLE
                selectStaffImgView!!.setImageResource(R.drawable.addiconinparentsevng)
                staffNameTV!!.text = "Staff Name:-"
                if (!mListViewArray!![position].photo.equals("")) {
                    System.out.println("the result are::" + mListViewArray!![position].photo)
                    Glide.with(mContext)
                        .load(mListViewArray!![position].photo.toString())
                        .placeholder(R.drawable.student).error(R.drawable.student)
                        .into(selectStudentImgView!!)
                } else {
                    selectStudentImgView!!.setImageResource(R.drawable.student)
                }
                if (AppUtils.isNetworkConnected(mContext)) {
                    getStaffList(mListViewArray!![position].id)
                } else {
                    AppUtils.showDialogAlertDismiss(
                        activity,
                        "Network Error",
                        getString(R.string.no_internet),
                        R.drawable.nonetworkicon,
                        R.drawable.roundred
                    )
                }
                dialog.dismiss()
            }

        })
        dialog.show()
    }

    private fun getStaffList(student_id: String) {
        progressBarDialog!!.show()
        mListViewStaffArray=ArrayList()
        var student= StafflistApiModel(student_id)
        val call: Call<StafflistResponseModel> = ApiClient.getClient.stafflist("Bearer "+ PreferenceManager.getAccessToken(mContext),student)
        call.enqueue(object : Callback<StafflistResponseModel> {
            override fun onResponse(
                call: Call<StafflistResponseModel>,
                response: Response<StafflistResponseModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {

                        if (responseData!!.response.data.size > 0) {
                            mListViewStaffArray!!.addAll(responseData!!.response.data)
                            staffRelative!!.visibility = View.VISIBLE
                        } else {
                            //CustomStatusDialog();
                            staffRelative!!.visibility = View.INVISIBLE
                            Toast.makeText(mContext, "No Staff Found", Toast.LENGTH_SHORT).show()
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

            override fun onFailure(call: Call<StafflistResponseModel>, t: Throwable) {

            }
        })
    }

    private fun sendEmailToStaff() {
        var homebannerbody = SendemailstaffptaApiModel(
            mStudentId, mStaffId,
            text_dialog!!.text.toString(), text_content!!.text.toString()
        )
        val call: Call<SendemailstaffptaResponseModel> = ApiClient.getClient.sendemailstaffpta(
            "Bearer " + PreferenceManager.getAccessToken(mContext),
            homebannerbody
        )
        progressBarDialog!!.show()
        call.enqueue(object : Callback<SendemailstaffptaResponseModel> {
            override fun onResponse(call: Call<SendemailstaffptaResponseModel>, response: Response<SendemailstaffptaResponseModel>) {
                progressBarDialog!!.dismiss()
                val responseData = response.body()
                if (response.body()!!.responsecode.equals("200")){
                    progressBarDialog!!.dismiss()
                    var status_code=response.body()!!.response.statuscode
                    if (status_code.equals("303")){

                        dialog!!.dismiss()
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity,
                            "Success",
                            "Successfully sent email to staff",
                            R.drawable.tick,
                            R.drawable.round
                        )

                    } else {
                        val toast = Toast.makeText(mContext, "Email not sent", Toast.LENGTH_SHORT)
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

            override fun onFailure(call: Call<SendemailstaffptaResponseModel>, t: Throwable) {
                progressBarDialog!!.dismiss()
            }
        })
    }
}

//        contactTeacher!!.setOnClickListener {
//            if (!PreferenceManager.getAccessToken(mContext).equals("")) {
//                dialog = Dialog(mContext)
//                dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//                dialog!!.setContentView(R.layout.alert_send_email_dialog)
//                dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
//                val dialogCancelButton = dialog!!.findViewById<View>(R.id.cancelButton) as Button
//                val submitButton = dialog!!.findViewById<View>(R.id.submitButton) as Button
//                text_dialog = dialog!!.findViewById<View>(R.id.text_dialog) as EditText
//                text_content = dialog!!.findViewById<View>(R.id.text_content) as EditText
//                // text_dialog.setSelection(0);
//                //text_content.setSelection(0);
//                text_dialog!!.onFocusChangeListener =
//                    View.OnFocusChangeListener { v, hasFocus ->
//                        if (hasFocus) {
//                            text_dialog!!.hint = ""
//                            text_dialog!!.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
//                            text_dialog!!.setPadding(5, 5, 0, 0)
//                        } else {
//                            text_dialog!!.hint = "Enter your subject here..."
//                            text_dialog!!.gravity = Gravity.CENTER
//                        }
//                    }
//                text_content!!.onFocusChangeListener =
//                    View.OnFocusChangeListener { v, hasFocus ->
//                        if (hasFocus) {
//                            text_content!!.gravity = Gravity.LEFT
//                        } else {
//                            text_content!!.gravity = Gravity.CENTER
//                        }
//                    }
//                dialogCancelButton.setOnClickListener { dialog!!.dismiss() }
//                submitButton.setOnClickListener {
//                    println("submit btn clicked")
//                    //                            if (AppUtils.isNetworkConnected(mContext)) {
//                    //                                if (text_content.equals("")) {
//                    //                                    AppUtils.setErrorForEditText(text_content, mContext.getString(R.string.mandatory_field));
//                    //                                } else if (text_dialog.equals("")) {
//                    //                                    AppUtils.setErrorForEditText(text_dialog, mContext.getString(R.string.mandatory_field));
//                    //
//                    //                                } else {
//                    //                                    sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF_PTA);
//                    //                                }
//                    //                            } else {
//                    //                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//                    //
//                    //                            }
//                    if (text_dialog!!.text.toString() == "") {
//                        AppUtils.showDialogAlertDismiss(
//                            mContext as Activity,
//                            mContext.getString(R.string.alert_heading),
//                            "Please enter subject",
//                            R.drawable.exclamationicon,
//                            R.drawable.round
//                        )
//                    } else if (text_content!!.text.toString() == "") {
//                        AppUtils.showDialogAlertDismiss(
//                            mContext as Activity,
//                            mContext.getString(R.string.alert_heading),
//                            "Please enter content",
//                            R.drawable.exclamationicon,
//                            R.drawable.round
//                        )
//                    } else {
//                        emailvalidationcheck( text_dialog!!.text.toString(),text_content!!.text.toString(),
//                            dialog!!
//                        )
//                       /* if (AppUtils.isNetworkConnected(mContext)) {
//                            println("student id" + mStudentId + "staff id" + mStaffId)
//                            sendEmailToStaff()
//                        } else {
//                            AppUtils.showDialogAlertDismiss(
//                                mContext as Activity,
//                                "Network Error",
//                                mContext.getString(R.string.no_internet),
//                                R.drawable.nonetworkicon,
//                                R.drawable.roundred
//                            )
//                        }*/
//                    }
//                }
//                dialog!!.show()
//            } else {
//                AppUtils.showDialogAlertDismiss(
//                    mContext as Activity,
//                    mContext.getString(R.string.alert_heading),
//                    "This feature is available only for registered users. Login/register to see contents.",
//                    R.drawable.exclamationicon,
//                    R.drawable.round
//                )
//            }
//        }

//fun emailvalidationcheck( title: String,
//                          message: String,
//                          dialog: Dialog){
//    val EMAIL_PATTERN :String=
//        "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$"
//    val pattern :String= "^([a-zA-Z ]*)$"
//
//    if (title.equals("")) {
//        val toast: Toast = Toast.makeText(
//            mContext, mContext.getResources().getString(
//                R.string.enter_subjects
//            ), Toast.LENGTH_SHORT
//        )
//        toast.show()
//    } else {
//        if (message.equals("")) {
//            val toast: Toast = Toast.makeText(
//                mContext, mContext.getResources().getString(
//                    R.string.enter_contents
//                ), Toast.LENGTH_SHORT
//            )
//            toast.show()
//        } else if (staffEmail.matches(EMAIL_PATTERN.toRegex())) {
//            if (title.toString().trim().matches(pattern.toRegex())) {
//                if (title.toString().length>=500){
//                    Toast.makeText(mContext, "Subject is too long", Toast.LENGTH_SHORT).show()
//
//                }else{
//                    if (message.toString().trim().matches(pattern.toRegex())) {
//
//                        if (message.length <= 500) {
//                            if (AppUtils.checkInternet(mContext!!)) {
//                                Log.e("success","Success")
//                                sendEmailToStaff()
//                            }else{
//                                Toast.makeText(
//                                    mContext,
//                                    mContext!!.resources.getString(R.string.no_internet),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        } else {
//                            Toast.makeText(mContext, "Message is too long", Toast.LENGTH_SHORT)
//                                .show()
//
//                        }
//                    }
//                    else {
//                        val toast: Toast = Toast.makeText(
//                            mContext, mContext.getResources().getString(
//                                R.string.enter_valid_contents
//                            ), Toast.LENGTH_SHORT
//                        )
//                        toast.show()
//                    }
//                }
//
//
//            } else {
//                val toast: Toast = Toast.makeText(
//                    mContext, mContext.getResources().getString(
//                        R.string.enter_valid_subjects
//                    ), Toast.LENGTH_SHORT
//                )
//                toast.show()
//            }
//        } else {
//            val toast: Toast = Toast.makeText(
//                mContext, mContext.getResources().getString(
//                    R.string.enter_valid_mail
//                ), Toast.LENGTH_SHORT
//            )
//            toast.show()
//        }
//    }
//}