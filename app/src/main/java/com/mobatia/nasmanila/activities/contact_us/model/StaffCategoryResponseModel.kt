package com.mobatia.nasmanila.activities.contact_us.model

import com.google.gson.annotations.SerializedName
import com.mobatia.nasmanila.fragments.contact_us.model.ContacUsModel

class StaffCategoryResponseModel  (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:StaffCategoryResponse
)

class StaffCategoryResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("data")var data:ArrayList<StaffModel>,
    @SerializedName("banner_image")var banner_image:String
)