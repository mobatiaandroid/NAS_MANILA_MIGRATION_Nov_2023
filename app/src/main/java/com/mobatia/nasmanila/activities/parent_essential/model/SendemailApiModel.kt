package com.mobatia.nasmanila.activities.parent_essential.model

import com.google.gson.annotations.SerializedName

class SendemailApiModel (
    @SerializedName("email")var email:String,
    @SerializedName("title")var title:String,
    @SerializedName("message")var message:String
)