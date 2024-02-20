package com.mobatia.nasmanila.fragments.parents_meeting.model

import com.google.gson.annotations.SerializedName

class StafflistResponseModel (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response: StafflistResponse
)

class StafflistResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("data")var data:ArrayList<StaffPtaModel>,
)

class StaffPtaModel (
    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("staff_photo") var staff_photo: String,
    @SerializedName("staff_email") var staff_email: String
)