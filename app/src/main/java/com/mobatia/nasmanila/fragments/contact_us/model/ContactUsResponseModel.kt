package com.mobatia.nasmanila.fragments.contact_us.model

import com.google.gson.annotations.SerializedName
import com.mobatia.nasmanila.fragments.social_media.model.SocialmediaDataModel

class ContactUsResponseModel  (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:ContactUsResponse
)

class ContactUsResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("data")var data:ContacUsModel
)