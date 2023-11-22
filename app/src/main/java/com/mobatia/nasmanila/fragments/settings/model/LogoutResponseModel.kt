package com.mobatia.nasmanila.fragments.settings.model

import com.google.gson.annotations.SerializedName

class LogoutResponseModel  (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response: LogoutResponse
)

class LogoutResponse (
    @SerializedName("statuscode")var statuscode:String
)
