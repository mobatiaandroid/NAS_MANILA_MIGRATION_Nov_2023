package com.mobatia.nasmanila.fragments.notifications.model

import com.google.gson.annotations.SerializedName

class NotificationsClearResponseModel (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:NotificationsClearResponse
    )
class NotificationsClearResponse (
    @SerializedName("statuscode")var statuscode:String
)