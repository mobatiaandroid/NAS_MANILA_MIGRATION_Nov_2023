package com.mobatia.nasmanila.activities.enrichment.model

import com.google.gson.annotations.SerializedName

class Cca_reviewsResponseModel (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response: Cca_reviewsResponse
)

class Cca_reviewsResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("data")var data:ArrayList<CCAReviewAfterSubmissionModel>
)
