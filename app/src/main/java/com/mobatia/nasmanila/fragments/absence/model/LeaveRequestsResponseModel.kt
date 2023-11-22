package com.mobatia.nasmanila.fragments.absence.model

import com.google.gson.annotations.SerializedName

class LeaveRequestsResponseModel (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:LeaveRequestsResponse
)

class LeaveRequestsResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("requests")var requests:ArrayList<LeavesModel>
)

class LeavesModel(
    @SerializedName("id")var id:String,
    @SerializedName("from_date")var from_date:String,
    @SerializedName("to_date")var to_date:String,
    @SerializedName("reason")var reason:String,
    @SerializedName("status")var status:Int,
    @SerializedName("created_at")var created_at:String,
    @SerializedName("updated_at")var updated_at:String,
    @SerializedName("requestedParent")var requestedParent:String,
    @SerializedName("requestedParentId")var requestedParentId:Int
)