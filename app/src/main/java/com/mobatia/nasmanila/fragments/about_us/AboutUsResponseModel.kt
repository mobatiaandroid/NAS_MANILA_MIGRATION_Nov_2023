package com.mobatia.nasmanila.fragments.about_us

import com.google.gson.annotations.SerializedName
import com.mobatia.nasmanila.fragments.about_us.model.AboutUsModel

class AboutUsResponseModel  (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response: LeavesubmitResponse
)

class LeavesubmitResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("banner_image")var banner_image:String,
    @SerializedName("description")var description:String,
    @SerializedName("contact_email")var contact_email:String,
    @SerializedName("website_link")var website_link:String,
    @SerializedName("data")var data:ArrayList<AboutUsModel>,
)
