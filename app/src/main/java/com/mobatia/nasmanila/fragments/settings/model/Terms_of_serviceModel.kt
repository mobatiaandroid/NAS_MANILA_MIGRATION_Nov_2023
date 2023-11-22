package com.mobatia.nasmanila.fragments.settings.model

import com.google.gson.annotations.SerializedName

class Terms_of_serviceModel  (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response: Terms_of_serviceResponse
)

class Terms_of_serviceResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("data")var data:Terms_of_serviceDataModel
)

class Terms_of_serviceDataModel (
    @SerializedName("title")var title:String,
    @SerializedName("description")var description:String
)
