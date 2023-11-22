package com.mobatia.nasmanila.activities.login.model

import com.google.gson.annotations.SerializedName

class ForgotPasswordApiModel (
    @SerializedName("email")var email:String,
    @SerializedName("deviceid")var deviceid:String,
    @SerializedName("devicetype")var devicetype:String


)