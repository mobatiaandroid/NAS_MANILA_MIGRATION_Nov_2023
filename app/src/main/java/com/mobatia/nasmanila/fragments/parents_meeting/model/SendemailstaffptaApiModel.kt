package com.mobatia.nasmanila.fragments.parents_meeting.model

import com.google.gson.annotations.SerializedName

class SendemailstaffptaApiModel (
    @SerializedName("studentid")var studentid:String,
    @SerializedName("staffid")var staffid:String,
    @SerializedName("title")var title:String,
    @SerializedName("description")var description:String
)