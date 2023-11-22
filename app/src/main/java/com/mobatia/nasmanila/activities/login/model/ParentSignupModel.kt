package com.mobatia.nasmanila.activities.login.model

import com.google.gson.annotations.SerializedName

class ParentSignupModel  (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response:ParentSignupResponse
)

class ParentSignupResponse (
    @SerializedName("response")var response:String,
    @SerializedName("statuscode")var statuscode:String
)