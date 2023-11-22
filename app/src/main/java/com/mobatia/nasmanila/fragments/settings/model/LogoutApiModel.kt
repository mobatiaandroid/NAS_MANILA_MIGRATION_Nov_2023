package com.mobatia.nasmanila.fragments.settings.model

import com.google.gson.annotations.SerializedName

class LogoutApiModel (
    @SerializedName("users_id")var users_id:String,
    @SerializedName("deviceid")var deviceid:String,
    @SerializedName("devicetype")var devicetype: String
)