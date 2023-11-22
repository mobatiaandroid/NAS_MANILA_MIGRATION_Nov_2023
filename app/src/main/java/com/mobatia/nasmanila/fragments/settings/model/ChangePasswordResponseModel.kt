package com.mobatia.nasmanila.fragments.settings.model

import com.google.gson.annotations.SerializedName
import com.mobatia.nasmanila.fragments.about_us.LeavesubmitResponse

class ChangePasswordResponseModel(
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response: ChangePasswordResponse
)

class ChangePasswordResponse (
    @SerializedName("statuscode")var statuscode:String,
)