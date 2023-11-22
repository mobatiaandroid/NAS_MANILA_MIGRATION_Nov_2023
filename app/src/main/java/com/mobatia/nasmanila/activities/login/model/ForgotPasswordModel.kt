package com.mobatia.nasmanila.activities.login.model

import com.google.gson.annotations.SerializedName

class ForgotPasswordModel (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:ForgotPasswordResponse
)

class ForgotPasswordResponse (
    @SerializedName("response")var response:String,
    @SerializedName("statuscode")var statuscode:String
)