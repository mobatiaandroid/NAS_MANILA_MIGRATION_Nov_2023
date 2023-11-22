package com.mobatia.nasmanila.activities.absence.model

import com.google.gson.annotations.SerializedName
import com.mobatia.nasmanila.fragments.absence.model.LeaveRequestsResponse

class LeavesubmitResponseModel  (
@SerializedName("responsecode")var responsecode:String,
@SerializedName("response")var response: LeavesubmitResponse
)

class LeavesubmitResponse (
    @SerializedName("statuscode")var statuscode:String
)