package com.mobatia.nasmanila.fragments.social_media.model

import com.google.gson.annotations.SerializedName

class SocialmediaResponseModel (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:CalendarResponse
)

class CalendarResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("banner_image")var banner_image:String,
    @SerializedName("data")var data:ArrayList<SocialmediaDataModel>
)