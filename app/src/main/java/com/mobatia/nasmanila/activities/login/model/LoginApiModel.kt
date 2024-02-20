package com.mobatia.nasmanila.activities.login.model

import com.google.gson.annotations.SerializedName

class LoginApiModel (
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("deviceid") var deviceid: String,
    @SerializedName("devicetype") var devicetype: String,
    @SerializedName("device_name") var device_name: String,
    @SerializedName("app_version") var app_version: String


)