package com.mobatia.nasmanila.fragments.notifications.model

import com.google.gson.annotations.SerializedName

class NotificationsResponseModel (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:NotificationsResponse
)

class NotificationsResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("data")var data:ArrayList<NotificationsListModel>
)