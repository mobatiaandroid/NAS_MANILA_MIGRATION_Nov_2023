package com.mobatia.nasmanila.activities.enrichment

import com.google.gson.annotations.SerializedName

class CcaModel (
        @SerializedName("responsecode")var responsecode:String,
        @SerializedName("response")var response: CcaResponse
    )

    class CcaResponse (
        @SerializedName("statuscode")var statuscode:String,
        @SerializedName("description")var description:String,
        @SerializedName("contact_email")var contact_email:String,
        @SerializedName("banner_image")var banner_image:String,
        @SerializedName("data")var data:ArrayList<CcaDataModel>
    )

    class CcaDataModel(
        @SerializedName("id")var id:String,
        @SerializedName("name")var name:String,
        @SerializedName("file")var file:String,
        @SerializedName("title")var title:String,
        @SerializedName("description")var description:String,
    )

