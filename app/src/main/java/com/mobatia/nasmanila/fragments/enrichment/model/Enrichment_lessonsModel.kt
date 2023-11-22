package com.mobatia.nasmanila.fragments.enrichment.model

import com.google.gson.annotations.SerializedName
import com.mobatia.nasmanila.fragments.parents_meeting.model.StafflistResponse

class Enrichment_lessonsModel (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response: Enrichment_lessonsResponse
)

class Enrichment_lessonsResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("description")var description:String,
    @SerializedName("contact_email")var contact_email:String,
    @SerializedName("banner_image")var banner_image:String,
    @SerializedName("data")var data:ArrayList<EnrichmentLessonsDataModel>
)

class EnrichmentLessonsDataModel  (
    @SerializedName("name")var name:String,
    @SerializedName("banner_image")var banner_image:String,
    @SerializedName("description")var description:String,
    @SerializedName("contact_email")var contact_email:String
)