package com.mobatia.nasmanila.activities.parentevening

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.activities.parentevening.adapter.ParentsEveningRoomListAdapter
import com.mobatia.nasmanila.activities.parentevening.adapter.TimeslotAdapter
import com.mobatia.nasmanila.activities.parentevening.model.GeneralSubmitResponseModel
import com.mobatia.nasmanila.activities.parentevening.model.PTATimeSlotsResponseModel
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class MeetingSlotListingActivity : AppCompatActivity() {
    private lateinit var mContext: Context
    private lateinit var progressBarDialog: ProgressBarDialog
    private lateinit var heading: TextView
    private lateinit var studentName: TextView
    private lateinit var studentClass: TextView
    private lateinit var staffName: TextView
    private lateinit var dateHeader: TextView

    //    private lateinit var confirmButton: TextView
    private lateinit var cancelButton: TextView
    private lateinit var videoLinkButton: Button
    private lateinit var recyclerView: RecyclerView
    var alreadyslotBookedByUser: Boolean = false
    var confirmedslotBookedByUser: Boolean = false


    var headermanager: HeaderManager? = null
    var relativeHeader: RelativeLayout? = null
    var back: ImageView? = null
    var home: ImageView? = null
    private var firstVisit = true
    private var dateString = ""
    private var dateSelected = ""
    private var studentNameText = ""
    private var studentId = ""
    private var studentClassText = ""
    private var staffNameText = ""
    private var staffId = ""
    private var confirmedLink = ""
    var isSlotConfirmed = false
    var isSlotBookedByUser = false
    lateinit var info: ImageView
    private val timeSlotList = ArrayList<PTATimeSlotsResponseModel.PTAResponseData.Slot>()
    private var timeSlotListPost = ArrayList<PTATimeSlotsResponseModel.PTAResponseData.Slot>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_slot_listing)
        mContext = this
        initViews()
        fetchIntentData()
        setupRecyclerView()
        setupButtons()
        if (AppUtils.isNetworkConnected(mContext)) fetchTimeSlotList() else AppUtils.showDialogAlertDismiss(
            mContext as Activity?,
            "Network Error",
            getString(R.string.no_internet),
            R.drawable.nonetworkicon,
            R.drawable.roundred
        )
    }

    private fun initViews() {
        progressBarDialog = ProgressBarDialog(mContext)
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout

        headermanager = HeaderManager(this@MeetingSlotListingActivity, "Parents' Meeting")
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
        heading = findViewById(R.id.heading)
        heading.text = "Parents' Meeting"
        dateHeader = findViewById(R.id.dateTextView)
        studentName = findViewById(R.id.studentNameTV)
        studentClass = findViewById(R.id.studentClassTV)
        staffName = findViewById(R.id.staffTV)
//        confirmButton = findViewById(R.id.reviewConfirmTextView)
        cancelButton = findViewById(R.id.cancelTextView)
        videoLinkButton = findViewById(R.id.vpmlBtn)
        recyclerView = findViewById(R.id.recycler_view_alloted_time)
    }

    private fun fetchIntentData() {
        dateString = intent.getStringExtra("date").orEmpty()
        studentNameText = intent.getStringExtra("studentName").orEmpty()
        studentId = intent.getStringExtra("studentId").orEmpty()
        studentClassText = intent.getStringExtra("studentClass").orEmpty()
        staffNameText = intent.getStringExtra("staffName").orEmpty()
        staffId = intent.getStringExtra("staffId").orEmpty()

        studentName.text = studentNameText
        studentClass.text = studentClassText
        staffName.text = staffNameText
        dateSelected = formatDateString(dateString)
        dateHeader.text = AppUtils.dateConversionY(dateSelected).toString()
    }

    private fun formatDateString(inputDateStr: String): String {
        val inputFormat: DateFormat = SimpleDateFormat("d/M/yyyy")
        val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = inputFormat.parse(inputDateStr) ?: Date()
        return outputFormat.format(date)
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(mContext, 3)
        }
        recyclerView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                Log.e("confirm", confirmedslotBookedByUser.toString())
                if (timeSlotList[position].status == "1") {
                    AppUtils.showDialogAlertDismiss(
                        mContext,
                        "Alert",
                        "This slot is not available.",
                        R.drawable.exclamationicon,
                        R.drawable.roundred
                    )

                } else if (!confirmedslotBookedByUser) {
                    timeSlotListPost = java.util.ArrayList()
                    timeSlotListPost.add(timeSlotList[position])
                    if (timeSlotListPost.get(0).bookingStatus
                            .equals("y")
                    ) {
                        val inputFormat: DateFormat = SimpleDateFormat("hh:mm:ss")
                        val outputFormat: DateFormat = SimpleDateFormat("hh:mm aa")
                        val inputDateStr = timeSlotListPost[0].slotStartTime
                        val date: Date = inputFormat.parse(inputDateStr)
                        val formt_fromtime = outputFormat.format(date)


                        val inputFormat2: DateFormat = SimpleDateFormat("hh:mm:ss")
                        val outputFormat2: DateFormat = SimpleDateFormat("hh:mm aa")
                        val inputDateStr2 = timeSlotListPost[0].slotEndTime
                        val date2: Date = inputFormat2.parse(inputDateStr2)
                        val formt_totime: String = outputFormat2.format(date2)

                        showApiAlert(
                            mContext,
                            "Do you want to reserve your appointment on " + dateSelected + " , " +
                                    formt_fromtime + " - " + formt_totime,
                            "Alert",
                            1
                        )

                    } else {
                        AppUtils.showDialogAlertDismiss(
                            mContext,
                            "Alert",
                            "Booking and cancellation date is over.",
                            R.drawable.exclamationicon,
                            R.drawable.roundred
                        )

                    }
                } else {
                    if (timeSlotList.get(position).status.equals("3")
                    ) {
                        AppUtils.showDialogAlertDismiss(
                            mContext,
                            "Alert",
                            "This slot is booked by you for the Parents' Meeting. Click 'Cancel' option to cancel this appointment.",
                            R.drawable.exclamationicon,
                            R.drawable.roundred
                        )


                    } else {
                        AppUtils.showDialogAlertDismiss(
                            mContext,
                            "Alert",
                            "Another Slot is already booked by you. If you want to take appointment on this time, please cancel earlier appointment and try.",
                            R.drawable.exclamationicon,
                            R.drawable.roundred
                        )


                    }
                }
            }

        })

    }

    private fun setupButtons() {

        info = findViewById<ImageView>(R.id.infoRoomImg)
        info.setOnClickListener { showRoomListDialog() }
//        confirmButton.setOnClickListener { showReviewAlertDialog() }
        cancelButton.setOnClickListener {
//            if (timeSlotListPost.firstOrNull()?.bookingStatus == "y") {
            showApiAlert(mContext, "Do you want to cancel this appointment?", "Confirm", 2)
//            } else {
//                AppUtils.showDialogAlertDismiss(
//                    mContext as Activity?,
//                    "Alert",
//                    "Booking and cancellation date is over.",
//                    R.drawable.exclamationicon,
//                    R.drawable.roundred
//                )
//            }
        }
        videoLinkButton.setOnClickListener {
            confirmedLink.takeIf { it.isNotEmpty() }?.let {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
            }
        }
    }

    private fun showApiAlert(
        context: Context,
        message: String,
        msgHead: String,
        bookOrCancel: Int
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        val icon = dialog.findViewById(R.id.iconImageView) as ImageView
        /* icon.setBackgroundResource(bgIcon)
         icon.setImageResource(ico)*/
        val text = dialog.findViewById(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById(R.id.alertHead) as TextView
        text.text = message
        textHead.text = msgHead
        val dialogButton = dialog.findViewById(R.id.btn_Ok) as Button
        dialogButton.setOnClickListener {
            if (AppUtils.isNetworkConnected(mContext)) {
                postSelectedSlots(bookOrCancel)
            } else {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    "Network Error",
                    getString(R.string.no_internet),
                    R.drawable.nonetworkicon,
                    R.drawable.roundred
                )
            }
            dialog.dismiss()
        }
        val dialogButtonCancel = dialog.findViewById(R.id.btn_Cancel) as Button
        dialogButtonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    private fun fetchTimeSlotList() {
        progressBarDialog.show()
        timeSlotList.clear()
        isSlotConfirmed = false
        isSlotBookedByUser = false
        val token = PreferenceManager.getAccessToken(mContext)
        Log.e("date", dateSelected)
//        val formattedDate = formatDateStringForApi(dateSelected)
        val formattedDate = dateSelected
        var paramObject: JsonObject = JsonObject()
        paramObject.addProperty("student_id", studentId)
        paramObject.addProperty("staff_id", staffId)
        paramObject.addProperty("date", formattedDate)

        ApiClient.getClient.pta_list(paramObject, "Bearer $token")
            .enqueue(object : Callback<PTATimeSlotsResponseModel> {
                override fun onFailure(call: Call<PTATimeSlotsResponseModel>, t: Throwable) {
                    progressBarDialog.dismiss()
                }

                override fun onResponse(
                    call: Call<PTATimeSlotsResponseModel>,
                    response: Response<PTATimeSlotsResponseModel>
                ) {
                    progressBarDialog.dismiss()
                    response.body()?.let { handleTimeSlotResponse(it) }
                        ?: AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            "Failed",
                            getString(R.string.cannot_continue),
                            R.drawable.nonetworkicon,
                            R.drawable.roundred
                        )
                }
            })
    }

    private fun formatDateStringForApi(inputDateStr: String): String {
        val inputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
        val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = inputFormat.parse(inputDateStr) ?: Date()
        return outputFormat.format(date)
    }

    private fun handleTimeSlotResponse(response: PTATimeSlotsResponseModel) {
        if (response.response.statusCode != "303" || response.response.availableDates.isEmpty()) {
            Toast.makeText(mContext, "No data available!", Toast.LENGTH_SHORT).show()
            return
        }
        timeSlotList.clear()
        timeSlotList.addAll(response.response.availableDates)



        videoLinkButton.visibility =
            if (isSlotConfirmed && confirmedLink.isNotEmpty()) View.VISIBLE else View.GONE
//        confirmButton.visibility = if (isSlotBookedByUser) View.VISIBLE else View.GONE
        cancelButton.visibility = if (isSlotConfirmed) View.VISIBLE else View.GONE

        for (i in timeSlotList.indices) {
            if (timeSlotList[i].status.equals("2")) {

                timeSlotListPost.add(timeSlotList[i])
                alreadyslotBookedByUser = true

//                confirmButton.visibility = View.GONE
                cancelButton.visibility = View.GONE
            }
        }
        for (i in timeSlotList.indices) {
            if (timeSlotList[i].status.equals("3")) {
                Log.e("timeslot",timeSlotList[i].slotStartTime.toString())
                confirmedslotBookedByUser = true
                confirmedLink = timeSlotList[i].vpml.toString()
//                confirmButton.visibility = View.GONE
                cancelButton.visibility = View.VISIBLE
            }
        }
        if (confirmedslotBookedByUser) {
            if (confirmedLink.equals("")) {
                videoLinkButton.visibility = View.GONE
            } else {
                videoLinkButton.visibility = View.VISIBLE
            }
            cancelButton.visibility = View.VISIBLE
//            confirmButton.visibility = View.INVISIBLE
        } else if (alreadyslotBookedByUser) {

            cancelButton.visibility = View.VISIBLE
//            confirmButton.visibility = View.VISIBLE
        } else {
            cancelButton.visibility = View.INVISIBLE
//            confirmButton.visibility = View.INVISIBLE
        }
        recyclerView.adapter = TimeslotAdapter(mContext, timeSlotList, cancelButton)
    }

    private fun showReviewAlertDialog() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_review_parentmeeting)
        var btn_maybelater = dialog.findViewById(R.id.btn_Cancel) as Button
        var btn_ok = dialog.findViewById(R.id.btn_Ok) as Button
        btn_maybelater.setOnClickListener()
        {
            dialog.dismiss()
        }
        btn_ok.setOnClickListener {
            val intent = Intent(mContext, ReviewAppointmentsRecyclerViewActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showRoomListDialog() {

        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_room_slot_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<View>(R.id.btn_dismiss) as Button
        val iconImageView = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        val socialMediaList =
            dialog.findViewById<View>(R.id.recycler_view_social_media) as RecyclerView
        val divider = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.list_divider)!!)
        socialMediaList.addItemDecoration(divider)
        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm

        val socialMediaAdapter = ParentsEveningRoomListAdapter(mContext, timeSlotList)
        socialMediaList.adapter = socialMediaAdapter
        dialogDismiss.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun postSelectedSlots(bookOrCancel: Int) {
        progressBarDialog.show()
        val token = PreferenceManager.getAccessToken(mContext)
        var paramObject: JsonObject = JsonObject()
        paramObject.addProperty("student_id", studentId)
        paramObject.addProperty("staff_id", staffId)
        paramObject.addProperty("date", dateSelected)
        paramObject.addProperty("pta_time_slot_id", timeSlotListPost[0].slotId)

        ApiClient.getClient.pta_insert(paramObject, "Bearer $token")
            .enqueue(object : Callback<GeneralSubmitResponseModel> {
                override fun onResponse(
                    call: Call<GeneralSubmitResponseModel>,
                    response: Response<GeneralSubmitResponseModel>
                ) {
                    progressBarDialog.dismiss()
                    if (response.isSuccessful && response.body()?.response!!.statuscode == "303") {
                        if (bookOrCancel == 1) {
                            showToast("Appointment confirmed successfully!")
                        } else {
                            showToast("Appointment cancelled successfully!")
                            cancelButton.visibility = View.GONE
                        }

                        fetchTimeSlotList()
                    } else {
                        showToast("Failed to confirm appointment")
                    }
                }

                override fun onFailure(call: Call<GeneralSubmitResponseModel>, t: Throwable) {
                    progressBarDialog.dismiss()
                    showToast("Error: ${t.message}")
                }
            })
    }

    private fun showToast(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }


}