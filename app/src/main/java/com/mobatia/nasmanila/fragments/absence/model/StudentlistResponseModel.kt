package com.mobatia.nasmanila.fragments.absence.model

import com.google.gson.annotations.SerializedName
import com.mobatia.nasmanila.activities.contact_us.model.StaffDeptData

class StudentlistResponseModel (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:StudentlistResponse
)

class StudentlistResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("data")var data:ArrayList<StudentModel>
)

class StudentModel(
    @SerializedName("id")var id:String,
    @SerializedName("name")var name:String,
    @SerializedName("class")var mClass:String,
    @SerializedName("section")var section:String,
    @SerializedName("house")var house:String,
    @SerializedName("photo")var photo:String
)
