package com.mobatia.nasmanila.fragments.calendar.model

import com.google.gson.annotations.SerializedName
import com.mobatia.nasmanila.fragments.notifications.model.NotificationsListModel

class CalendarResponseModel (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:CalendarResponse
)

class CalendarResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("calendar_url")var calendar_url:String
)