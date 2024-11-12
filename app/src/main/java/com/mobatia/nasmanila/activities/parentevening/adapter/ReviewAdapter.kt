package com.mobatia.nasmanila.activities.parentevening.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.gson.JsonObject
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.parentevening.ReviewAppointmentsRecyclerViewActivity
import com.mobatia.nasmanila.activities.parentevening.model.GeneralSubmitResponseModel
import com.mobatia.nasmanila.activities.parentevening.model.PTAReviewResponseModel
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

internal class ReviewAdapter(
    private val mContext: Context,
    private var reviewList: ArrayList<PTAReviewResponseModel.PTAReviewResponse.ReviewListModel>,
    private val reviewActivity: ReviewAppointmentsRecyclerViewActivity,
    private val progressDialog: ProgressBarDialog,
    private val reviewRecyclerView: RecyclerView,
) : RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {

    private val idList = ArrayList<Int>()
    private var confirmVisibility = false

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val studentName: TextView = view.findViewById(R.id.studNameTV)
        val studentClass: TextView = view.findViewById(R.id.classNameTV)
        val staffName: TextView = view.findViewById(R.id.staffNameTV)
        val startDate: TextView = view.findViewById(R.id.reserveDateTimeTextView)
        val endDate: TextView = view.findViewById(R.id.expireDateTimeTextView)
        val studentImage: ImageView = view.findViewById(R.id.imgView)
        val cancelImage: ImageView = view.findViewById(R.id.cancelAppointment)
        val confirmImage: ImageView = view.findViewById(R.id.confirmAppointment)
        val confirmIcon: ImageView = view.findViewById(R.id.confirmationImageview)
        val addToCalendarImage: ImageView = view.findViewById(R.id.addTocalendar)
        val vpmlButton: TextView = view.findViewById(R.id.vpml)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_review_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        progressDialog.dismiss()
        idList.clear()

        val review = reviewList[position]
        holder.apply {
            studentName.text = review.student
            studentClass.text = review.studentClass
            staffName.text = review.staff

            if (review.status == 2 && review.bookingOpen == "y") {
                idList.add(review.id.toInt())
                confirmVisibility = true
            }

            setDateTimeFields(review, startDate, endDate)
            setImageView(mContext, review.studentPhoto, studentImage)

            setupActionIcons(review, this)
            setupListeners(this, review)
        }
    }

    override fun getItemCount(): Int = reviewList.size

    private fun setDateTimeFields(
        review: PTAReviewResponseModel.PTAReviewResponse.ReviewListModel,
        startDateText: TextView, endDateText: TextView
    ) {
        try {
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            val timeFormat = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)

            val formattedDate =
                dateFormat.format(SimpleDateFormat("yyyy-MM-dd").parse(review.date)!!)
            val formattedStartTime =
                timeFormat.format(SimpleDateFormat("hh:mm:ss").parse(review.startTime)!!)
            val formattedEndTime =
                timeFormat.format(SimpleDateFormat("hh:mm:ss").parse(review.endTime)!!)

            startDateText.text = "$formattedDate $formattedStartTime - $formattedEndTime"

            if (review.bookEndDate.isNotEmpty()) {
                endDateText.text =
                    "Confirm/Cancellation closes at ${formatDateTime(review.bookEndDate)}"
            } else {
                endDateText.text = "No cancellation date available"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun formatDateTime(dateStr: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.ENGLISH)
            outputFormat.format(inputFormat.parse(dateStr)!!)
        } catch (e: Exception) {
            e.printStackTrace()
            "Invalid date"
        }
    }

    private fun setImageView(context: Context, photoUrl: String, imageView: ImageView) {
        if (photoUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.student)
        } else {
            Glide.with(context)
                .load(photoUrl)
                .placeholder(R.drawable.student)
                .error(R.drawable.student)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .transform(CircleCrop())
                .into(imageView)
        }
    }

    private fun setupActionIcons(
        review: PTAReviewResponseModel.PTAReviewResponse.ReviewListModel,
        holder: MyViewHolder
    ) {
        with(holder) {
            // Confirm Image Visibility
            confirmImage.visibility =
                if (review.status == 2 && review.bookingOpen == "y") View.VISIBLE else View.GONE

            // Cancel Image Visibility and behavior
            if (review.status == 3 && review.bookingOpen == "y") {
                cancelImage.visibility = View.VISIBLE
                cancelImage.alpha = 1.0f // fully visible
                cancelImage.isClickable = true
            } else {
                // If cancellation date is over, make it semi-transparent and unclickable
                cancelImage.alpha = 0.5f // 50% transparency
                cancelImage.isClickable = false
            }

            // Add to Calendar Image Visibility
            addToCalendarImage.visibility =
                if (review.status != 2 || review.bookingOpen == "n") View.VISIBLE else View.GONE

            // VPML button visibility
            vpmlButton.visibility = if (review.vpml.isEmpty()) View.GONE else View.VISIBLE

            // Confirmation Icon Background
            confirmIcon.setBackgroundResource(
                if (review.status == 2) R.drawable.doubtinparticipatingsmallicon else R.drawable.tick_icon
            )
        }
    }


    private fun setupListeners(
        holder: MyViewHolder,
        review: PTAReviewResponseModel.PTAReviewResponse.ReviewListModel
    ) {

        holder.cancelImage.setOnClickListener {
            if (review.bookingOpen == "y") {
                idList.clear()
                idList.add(review.id.toInt())
                showCancelDialog(
                    mContext, "Do you want to cancel appointment?", review.ptaTimeSlotId,
                    review.studentId, review.staffId
                )
            } else {
                showAlert(mContext, "Alert", "Booking and cancellation date is over")
            }
        }

        holder.vpmlButton.setOnClickListener {
            if (review.vpml.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(review.vpml))
                mContext.startActivity(intent)
            }
        }

        holder.addToCalendarImage.setOnClickListener {
            addToCalendar(review)
        }
    }

    private fun addToCalendar(review: PTAReviewResponseModel.PTAReviewResponse.ReviewListModel) {
        val startTime = formatToMillis("${review.date} ${review.startTime}")
        val endTime = formatToMillis("${review.date} ${review.endTime}")
        val intent = Intent(Intent.ACTION_EDIT).apply {
            type = "vnd.android.cursor.item/event"
            putExtra("beginTime", startTime)
            putExtra("endTime", endTime)
            putExtra("allDay", true)
            putExtra("title", "PARENT MEETING")
            putExtra("description", review.vpml)
        }
        mContext.startActivity(intent)
    }

    private fun formatToMillis(dateTime: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        return format.parse(dateTime)?.time ?: 0L
    }


    private fun showCancelDialog(
        context: Context,
        message: String,
        id: String,
        studentId: String,
        staffId: String
    ) {
        showDialog(context, message, R.drawable.questionmark_icon) {
            postSelectedSlot(
                id,
                studentId,
                staffId
            )
        }
    }

    private fun showAlert(context: Context, title: String, message: String) {
        AppUtils.showDialogAlertDismiss(
            context,
            title,
            message,
            R.drawable.exclamationicon,
            R.drawable.roundred
        )
    }

    private fun showDialog(
        context: Context,
        message: String,
        iconRes: Int,
        onOkClicked: () -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.alert_dialogue_layout)
//        dialog.findViewById<ImageView>(R.id.dialogIcon).setImageResource(iconRes)
        dialog.findViewById<TextView>(R.id.text_dialog).text = message
        dialog.findViewById<Button>(R.id.btn_Ok).setOnClickListener {
            onOkClicked()
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.btn_Cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun postSelectedSlot(id: String, studentId: String, staffId: String) {
        val token = PreferenceManager.getAccessToken(mContext)
        var paramObject: JsonObject = JsonObject()
        paramObject.addProperty("student_id", studentId)
        paramObject.addProperty("staff_id", staffId)
        paramObject.addProperty("pta_time_slot_id", id)

        ApiClient.getClient.pta_insert(
            paramObject, "Bearer " + token
        ).enqueue(object : Callback<GeneralSubmitResponseModel> {
            override fun onResponse(
                call: Call<GeneralSubmitResponseModel>,
                response: Response<GeneralSubmitResponseModel>
            ) {
                if (response.isSuccessful) {
                    showDialogAlertDismiss(
                        mContext,
                        "Alert",
                        "Appointment cancelled successfully",
                        R.drawable.exclamationicon,
                        R.drawable.roundred
                    )
                } else {
                    AppUtils.showDialogAlertDismiss(
                        mContext,
                        "Alert",
                        "Appointment cancellation failed.",
                        R.drawable.exclamationicon,
                        R.drawable.roundred
                    )
                }
            }

            override fun onFailure(call: Call<GeneralSubmitResponseModel>, t: Throwable) {
                AppUtils.showDialogAlertDismiss(
                    mContext,
                    "Alert",
                    "Something went wrong, please try again",
                    R.drawable.exclamationicon,
                    R.drawable.roundred
                )
            }
        })
    }

    private fun refreshList() {
        reviewRecyclerView.layoutManager = LinearLayoutManager(mContext)
        reviewActivity.reviewlistcall(progressDialog, mContext, reviewRecyclerView)
    }
    fun showDialogAlertDismiss(
        context: Context?,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int
    ) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<View>(R.id.textDialog) as TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        text.text = msg
        textHead.text = msgHead
        val dialogButton = dialog.findViewById<View>(R.id.btnOK) as Button
        dialogButton.setOnClickListener {
            refreshList()

            dialog.dismiss() }

        dialog.show()
    }
}