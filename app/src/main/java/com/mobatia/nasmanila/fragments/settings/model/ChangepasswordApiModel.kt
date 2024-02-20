package com.mobatia.nasmanila.fragments.settings.model

import com.google.gson.annotations.SerializedName

class ChangepasswordApiModel (
    @SerializedName("current_password")var current_password:String,
    @SerializedName("new_password")var new_password:String,
    @SerializedName("email")var email:String,
    @SerializedName("deviceid")var deviceid:String,
    @SerializedName("devicetype")var devicetype: String
)
