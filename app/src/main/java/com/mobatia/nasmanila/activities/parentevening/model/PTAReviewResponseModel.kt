package com.mobatia.nasmanila.activities.parentevening.model

import com.google.gson.annotations.SerializedName

class PTAReviewResponseModel(
    @SerializedName("responsecode") var responseCode: String,
    @SerializedName("response") var response: PTAReviewResponse
) {
    class PTAReviewResponse(
        @SerializedName("statuscode") val statusCode: String,
        @SerializedName("response") val response: String,
        @SerializedName("data") val data: ArrayList<ReviewListModel>
    ) {
        class ReviewListModel(
            @SerializedName("id") val id: Int,
            @SerializedName("pta_time_slot_id") val ptaTimeSlotId: String,
            @SerializedName("student_id") val studentId: String,
            @SerializedName("student") val student: String,
            @SerializedName("student_photo") val studentPhoto: String,
            @SerializedName("staff") val staff: String,
            @SerializedName("staff_id") val staffId: String,
            @SerializedName("date") val date: String,
            @SerializedName("start_time") val startTime: String,
            @SerializedName("end_time") val endTime: String,
            @SerializedName("room") val room: String,
            @SerializedName("vpml") val vpml: String,
            @SerializedName("student_class") val studentClass: String,
            @SerializedName("status") val status: Int,
            @SerializedName("book_end_date") val bookEndDate: String,
            @SerializedName("booking_open") val bookingOpen: String
        )
    }
}
