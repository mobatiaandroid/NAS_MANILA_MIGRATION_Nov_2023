package com.mobatia.nasmanila.fragments.notifications.model

import com.google.gson.annotations.SerializedName

class NotificationsApiModel (

    @SerializedName("deviceid")var deviceid:String,
    @SerializedName("devicetype")var devicetype:String,
    @SerializedName("users_id")var users_id:String,
    @SerializedName("offset")var offset:String,
    @SerializedName("scroll_to")var scroll_to:String
)
