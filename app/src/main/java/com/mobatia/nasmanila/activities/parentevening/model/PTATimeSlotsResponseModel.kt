package com.mobatia.nasmanila.activities.parentevening.model

import com.google.gson.annotations.SerializedName


class PTATimeSlotsResponseModel(
    @SerializedName("responsecode")
    val responseCode: String,
    @SerializedName("response")
    val response: PTAResponseData

) {
    data class PTAResponseData(
        @SerializedName("response")
        val responseMessage: String,
        @SerializedName("statuscode")
        val statusCode: String,
        @SerializedName("data")
        val availableDates: ArrayList<Slot>,
        @SerializedName("data_count")
        val dataCount: String
    ) {
        data class Slot(
            @SerializedName("parent_evening_id")
            val parentEveningId: String,
            @SerializedName("meeting_date")
            val meetingDate: String,
            @SerializedName("booking_end_date")
            val bookingEndDate: String,
            @SerializedName("slot_id")
            val slotId: String,
            @SerializedName("slot_start_time")
            val slotStartTime: String,
            @SerializedName("slot_end_time")
            val slotEndTime: String,
            @SerializedName("room")
            val room: String?,
            @SerializedName("vpml")
            val vpml: String?,
            @SerializedName("status")
            val status: String,
            @SerializedName("status_slot_message")
            val statusSlotMessage: String,
            @SerializedName("booking_status")
            val bookingStatus: String
        )
    }
}