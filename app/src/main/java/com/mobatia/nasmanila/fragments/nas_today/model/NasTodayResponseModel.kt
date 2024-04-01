package com.mobatia.nasmanila.fragments.nas_today.model

import com.google.gson.annotations.SerializedName


class NasTodayResponseModel (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response: NastodayResponse
)
class NastodayResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("response")var response:String,
    @SerializedName("data")var data:ArrayList<NasTodayModel>,
    @SerializedName("banner_image")var banner_image:String

)