package com.mobatia.nasmanila.activities.parentevening.model

import com.google.gson.annotations.SerializedName

class GeneralSubmitResponseModel(
    @SerializedName("responsecode") val responsecode: String,
    @SerializedName("response") val response: Response,

    ) {
    class Response(
        @SerializedName("response") val response: String,
        @SerializedName("statuscode") val statuscode: String
    )
}
