package com.mobatia.nasmanila.activities.parent_essential.model

import com.google.gson.annotations.SerializedName
import com.mobatia.nasmanila.fragments.parent_essentials.model.ParentEssentialsModel

class SendemailResponseModel (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:SendemailResponse
)

class SendemailResponse (
    @SerializedName("response")var response:String,
    @SerializedName("statuscode")var statuscode:String
)