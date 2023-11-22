package com.mobatia.nasmanila.fragments.notifications.model

import com.google.gson.annotations.SerializedName

class NotificationsListModel (
    @SerializedName("message")var message:String,
    @SerializedName("url")var url:String,
    @SerializedName("date")var date:String,
    @SerializedName("push_from")var push_from:String,
    @SerializedName("type")var type:String
)