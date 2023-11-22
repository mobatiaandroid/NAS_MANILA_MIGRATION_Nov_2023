package com.mobatia.nasmanila.fragments.home.model

import com.google.gson.annotations.SerializedName

class HomeBannerModel  (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:HomeBannerResponse
)

class HomeBannerResponse (
    @SerializedName("response")var response:String,
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("data")var data:ArrayList<String>
)