package com.mobatia.nasmanila.activities.contact_us.model

import com.google.gson.annotations.SerializedName

class StaffCDeptResponseModel  (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:StaffCDeptResponse
)

class StaffCDeptResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("data")var data:StaffDeptData
)