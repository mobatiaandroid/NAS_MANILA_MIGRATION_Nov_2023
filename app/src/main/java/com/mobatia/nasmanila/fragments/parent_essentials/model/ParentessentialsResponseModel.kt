package com.mobatia.nasmanila.fragments.parent_essentials.model

import com.google.gson.annotations.SerializedName

class ParentessentialsResponseModel  (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:ParentessentialsResponse
)

class ParentessentialsResponse (
    @SerializedName("response")var response:String,
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("description")var description:String,
    @SerializedName("contact_email")var contact_email:String,
    @SerializedName("banner_image")var banner_image:String,
    @SerializedName("data")var data:ArrayList<ParentEssentialsModel>
)