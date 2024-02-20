package com.mobatia.nasmanila.fragments.home.model

import com.google.gson.annotations.SerializedName

class HomeBannerApiModel (
    @SerializedName("app_version")var app_version:String,
    @SerializedName("devicetype")var devicetype:String
)