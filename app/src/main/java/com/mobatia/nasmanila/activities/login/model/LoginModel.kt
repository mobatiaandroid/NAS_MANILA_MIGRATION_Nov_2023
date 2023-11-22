package com.mobatia.nasmanila.activities.login.model

import com.google.gson.annotations.SerializedName

class LoginModel (
@SerializedName("responsecode")var responsecode:String,
@SerializedName("response")var response:LoginResponse
)

class LoginResponse (
    @SerializedName("response")var response:String,
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("responseArray")var responseArray:LoginResponseArray
)