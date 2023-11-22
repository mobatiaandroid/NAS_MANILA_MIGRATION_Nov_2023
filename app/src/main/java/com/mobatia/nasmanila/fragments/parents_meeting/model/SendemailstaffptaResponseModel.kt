package com.mobatia.nasmanila.fragments.parents_meeting.model

import com.google.gson.annotations.SerializedName

class SendemailstaffptaResponseModel(
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response: SendemailstaffptaResponse
)

class SendemailstaffptaResponse (
    @SerializedName("statuscode")var statuscode:String,
)