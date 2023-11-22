package com.mobatia.nasmanila.activities.login.model

import com.google.gson.annotations.SerializedName

class LoginApiModel (
    @SerializedName("email")var email:String,
    @SerializedName("password")var password:String,
    @SerializedName("deviceid")var deviceid:String,
    @SerializedName("devicetype")var devicetype:String


)