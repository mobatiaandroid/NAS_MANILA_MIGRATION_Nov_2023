package com.mobatia.nasmanila.activities.absence

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.absence.model.LeavesubmitApiModel
import com.mobatia.nasmanila.activities.absence.model.LeavesubmitResponseModel
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.DividerItemDecoration
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.absence.adapter.StudentSpinnerAdapter
import com.mobatia.nasmanila.fragments.absence.model.StudentModel
import com.mobatia.nasmanila.fragments.absence.model.StudentlistApiModel
import com.mobatia.nasmanila.fragments.absence.model.StudentlistResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class LeaveRequestSubmissionActivity :AppCompatActivity(){
    lateinit var mContext: Context
    var progressBarDialog: ProgressBarDialog? = null
    var submitBtn: Button? = null
    var relativeHeader: RelativeLayout? = null

    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null

    var enterMessage: EditText? = null
    var enterStratDate: TextView? = null
    var enterEndDate:android.widget.TextView? = null

    //    EditText studentName,studentClass;
    var submitLayout: LinearLayout? = null
    var c: Calendar? = null
    var mYear = 0
    var mMonth = 0
    var mDay = 0
    var df: SimpleDateFormat? = null
    var formattedDate: String? = null
    var calendar: Calendar? = null
    var fromDate = ""
    var toDate:kotlin.String? = ""
    var tomorrowAsString: String? = null
    var sdate: Date? = null
    var edate:java.util.Date? = null
    var elapsedDays: Long = 0
    var studImg: ImageView? = null
    var stud_img = ""
    var outputFormats: SimpleDateFormat? = null

    //    SimpleDateFormat outputFormat;
    var extras: Bundle? = null
    var studentNameStr = ""
    var studentClassStr = ""
    var studentIdStr = ""
    var studentName: TextView? = null
    var mStudentSpinner: LinearLayout? = null

    var studentsModelArrayList: ArrayList<StudentModel> = ArrayList<StudentModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leave_request_submission_activity)
        mContext=this
        progressBarDialog = ProgressBarDialog(mContext!!)
        initUI()

    }

    private fun initUI() {
        extras = intent.extras
        if (extras != null) {
            studentNameStr = extras!!.getString("studentName")!!
            studentIdStr = extras!!.getString("studentId")!!
            //studentsModelArrayList = extras!!.getSerializable("StudentModelArray") as java.util.ArrayList<StudentModel?>?
            stud_img = extras!!.getString("studentImage")!!
        }

        calendar = Calendar.getInstance()
       outputFormats =
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout

        mStudentSpinner = findViewById<View>(R.id.studentSpinner) as LinearLayout
        studentName = findViewById<View>(R.id.studentName) as TextView

        enterMessage = findViewById(R.id.enterMessage)
        enterStratDate = findViewById(R.id.enterStratDate)

        enterEndDate = findViewById(R.id.enterEndDate)
        submitLayout = findViewById(R.id.submitLayout)
        submitBtn = findViewById(R.id.submitBtn)

        headermanager = HeaderManager(this@LeaveRequestSubmissionActivity, NaisClassNameConstants.ABSENCE)
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
        back!!.setOnClickListener {
            finish()
        }
        if (AppUtils.checkInternet(mContext)) {
            studentlistcall()
        }else{
            Toast.makeText(
                mContext,
                mContext.resources.getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
        }

        mStudentSpinner!!.setOnClickListener {
            if (studentsModelArrayList.size > 0) {
                showSocialmediaList(studentsModelArrayList)
            } else {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity,
                    "Alert",
                    getString(R.string.student_not_available),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        }
        studentName!!.text = studentNameStr
        studImg = findViewById<View>(R.id.studImg) as ImageView
        if (stud_img != "") {
            Glide.with(mContext).load(stud_img).placeholder(R.drawable.boy)
                .into(studImg!!)
        } else {
            studImg!!.setImageResource(R.drawable.boy)
        }
        PreferenceManager.setLeaveStudentId(mContext, studentIdStr)

        enterMessage!!.clearFocus()
        enterMessage!!.isFocusable = false

        enterMessage!!.setOnTouchListener { v, event ->
            enterMessage!!.isFocusableInTouchMode = true
            false
        }
        submitBtn!!.setOnClickListener {

           if (AppUtils.isEditTextFocused(this@LeaveRequestSubmissionActivity)) {
            AppUtils.hideKeyBoard(mContext)
        }
            if (enterStratDate!!.text.toString() == "") {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_startdate),
                    R.drawable.infoicon,
                    R.drawable.round
                )
            } else if (enterEndDate!!.text.toString() == "") {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_enddate),
                    R.drawable.infoicon,
                    R.drawable.round
                )
            } else if (enterMessage!!.text.toString().trim { it <= ' ' } == "") {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_reason),
                    R.drawable.infoicon,
                    R.drawable.round
                )
            } else {
                if (AppUtils.isNetworkConnected(mContext)) {
                    submitLeave()
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
        }
        enterStratDate!!.setOnClickListener {
            if (AppUtils.isEditTextFocused(this@LeaveRequestSubmissionActivity)) {
                AppUtils.hideKeyBoard(mContext)
            }
            enterMessage!!.isFocusable = false
//                studentName.clearFocus();
//                studentClass.clearFocus();
            //                studentName.clearFocus();
//                studentClass.clearFocus();
            enterMessage!!.clearFocus()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var strDate: Date? = null
            val items1: List<String> = AppUtils.getCurrentDateToday()!!.split("-")
            val date1 = items1[0]
            //if()
            //if()
            val month = items1[1]
            val year = items1[2]
            try {
                strDate = sdf.parse(AppUtils.getCurrentDateToday())
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val strDatePicker = DatePickerDialog(
                mContext, startDate, year.toInt(),
                month.toInt() - 1, date1.toInt()
            )
            strDatePicker.datePicker.minDate = strDate!!.time
            strDatePicker.show()
        }
        enterEndDate!!.setOnClickListener {
            if (AppUtils.isEditTextFocused(this@LeaveRequestSubmissionActivity)) {
                AppUtils.hideKeyBoard(mContext)
            }
            enterMessage!!.isFocusable = false
            enterMessage!!.clearFocus()
            if (enterStratDate!!.text.toString() == "") {
                Toast.makeText(mContext, "Please select first day of absence.", Toast.LENGTH_SHORT)
                    .show()
            } else {
//                    pickerIOSEndDate.setVisibility(View.VISIBLE);
//                    pickerIOS.setVisibility(View.GONE);
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                var strDate: Date? = null
                val items1: List<String> = AppUtils.getCurrentDateToday()!!.split("-")
                val date1 = items1[0]
                //if()
                val month = items1[1]
                val year = items1[2]
                try {
                    strDate = sdf.parse(AppUtils.getCurrentDateToday())
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val endDatePicker = DatePickerDialog(
                    mContext, endDate, year.toInt(),
                    month.toInt() - 1, date1.toInt()
                )
                endDatePicker.datePicker.minDate = strDate!!.time
                endDatePicker.show()
            }
        }


    }

    private fun submitLeave() {
        progressBarDialog!!.show()
        var student = LeavesubmitApiModel(
            studentIdStr,
            fromDate,
            toDate.toString(),
            enterMessage!!.text.toString().trim { it <= ' ' })
        val call: Call<LeavesubmitResponseModel> = ApiClient.getClient.requestLeave(
            "Bearer " + PreferenceManager.getAccessToken(mContext),
            student
        )
        call.enqueue(object : Callback<LeavesubmitResponseModel> {
            override fun onResponse(
                call: Call<LeavesubmitResponseModel>,
                response: Response<LeavesubmitResponseModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {

                        showDialogSuccess(
                            mContext as Activity,
                            "Success",
                            getString(R.string.succ_leave_submission),
                            R.drawable.tick,
                            R.drawable.round
                        )


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
                    } else if (status_code.equals("313")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.date_already_registered),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                    else {
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

            override fun onFailure(call: Call<LeavesubmitResponseModel>, t: Throwable) {

            }
        })
    }

    private fun showSocialmediaList(mStudentArray: ArrayList<StudentModel>) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_student_media_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<View>(R.id.btn_dismiss) as Button
        val iconImageView = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        iconImageView.setImageResource(R.drawable.boy)
        val socialMediaList =
            dialog.findViewById<View>(R.id.recycler_view_social_media) as RecyclerView
        //if(mSocialMediaArray.get())
        //if(mSocialMediaArray.get())
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            dialogDismiss.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.button_new))
        } else {
            dialogDismiss.background = mContext.resources.getDrawable(R.drawable.button_new)
        }
        socialMediaList.addItemDecoration(DividerItemDecoration(mContext.resources.getDrawable(R.drawable.list_divider)))

        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm

        val studentAdapter = StudentSpinnerAdapter(mContext, mStudentArray)
        socialMediaList.adapter = studentAdapter
        dialogDismiss.setOnClickListener {
            dialog.dismiss()
        }
        socialMediaList.addOnItemClickListener(object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                dialog.dismiss()
                studentName!!.setText(mStudentArray[position].name)
                studentIdStr = mStudentArray[position].id
                studentClassStr = mStudentArray[position].mClass
                stud_img = mStudentArray[position].photo
                if (stud_img != "") {
                    Glide.with(mContext).load(stud_img)
                        .placeholder(R.drawable.boy).into(studImg!!)
                } else {
                    studImg!!.setImageResource(R.drawable.boy)
                }
                PreferenceManager.setLeaveStudentId(mContext, studentIdStr)
                PreferenceManager.setLeaveStudentName(mContext, mStudentArray[position].name)
            }

        })
        dialog.show()
    }

    private fun studentlistcall() {
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
                        studentsModelArrayList= ArrayList()

                        if (responseData!!.response.data.size > 0) {
                            studentsModelArrayList.addAll(responseData!!.response.data)


                        } else {

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
    var startDate =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            fromDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
            if (toDate != "") {
                val dateFormatt = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                try {
                    sdate = dateFormatt.parse(fromDate)
                    edate = dateFormatt.parse(toDate)
                    printDifference(sdate!!, edate!!)
                } catch (e: Exception) {
                }
            }
            if (elapsedDays < 0 && toDate != "") {
                fromDate = AppUtils.dateConversionYToD(enterStratDate!!.text.toString()).toString()
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    "Choose first day of absence(date) less than or equal to selected return to school(date)",
                    R.drawable.infoicon,
                    R.drawable.round
                )
                //break;
            } else {
                enterStratDate!!.setText(AppUtils.dateConversionY(fromDate))
                try {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    val strDate = sdf.parse(fromDate)
                    c = Calendar.getInstance()
                    mYear = c!!.get(Calendar.YEAR)
                    mMonth = c!!.get(Calendar.MONTH)
                    mDay = c!!.get(Calendar.DAY_OF_MONTH)
                    df = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    formattedDate = df!!.format(c!!.getTime())
                    val endDate = sdf.parse(formattedDate)
                    val dateFormatt = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                    var convertedDate: Date? = Date()
                    try {
                        convertedDate = dateFormatt.parse(enterStratDate!!.text.toString())
                    } catch (e: ParseException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                    calendar!!.time = convertedDate
                    calendar!!.add(Calendar.DAY_OF_YEAR, 1)
                    val tomorrow = calendar!!.time
                    tomorrowAsString = dateFormatt.format(tomorrow)


                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
        }
    var endDate =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            toDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
            if (toDate != "") {
                val dateFormatt = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                try {
                    sdate = dateFormatt.parse(fromDate)
                    edate = dateFormatt.parse(toDate)
                    printDifference(sdate!!, edate!!)
                } catch (e: Exception) {
                }
                if (elapsedDays < 0) {
                    toDate = AppUtils.dateConversionYToD(enterEndDate!!.text.toString())
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity,
                        getString(R.string.alert_heading),
                        "Choose return to school(date) greater than or equal to first day of absence(date)",
                        R.drawable.infoicon,
                        R.drawable.round
                    )

                } else {
                    enterEndDate!!.setText(AppUtils.dateConversionY(toDate))
                }

            }
       try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val strDate = sdf.parse(toDate)
            c = Calendar.getInstance()
            mYear = c!!.get(Calendar.YEAR)
            mMonth = c!!.get(Calendar.MONTH)
            mDay = c!!.get(Calendar.DAY_OF_MONTH)
            df = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            formattedDate = df!!.format(c!!.getTime())
            val endDate = sdf.parse(formattedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        }

    fun printDifference(startDate: Date, endDate: Date) {

        //milliseconds
        var different = endDate.time - startDate.time

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        elapsedDays = different / daysInMilli
        different = different % daysInMilli
        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli
        val elapsedSeconds = different / secondsInMilli

    }
    fun showDialogSuccess(
        activity: Activity?,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int
    ) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)

        val text:TextView = dialog.findViewById(R.id.textDialog)
        val textHead:TextView = dialog.findViewById(R.id.alertHead)
        text.text = msg
        textHead.text = msgHead
        val dialogButton :Button= dialog.findViewById(R.id.btnOK)
        dialogButton.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }
}
