package com.mobatia.nasmanila.activities.enrichment.model

import com.google.gson.annotations.SerializedName

class Cca_submitResponseModel  (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response: Cca_submitResponse
)

class Cca_submitResponse (
    @SerializedName("statuscode")var statuscode:String
)
