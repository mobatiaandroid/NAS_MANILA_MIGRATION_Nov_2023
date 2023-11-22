package com.mobatia.nasmanila.activities.login.model

import com.google.gson.annotations.SerializedName

class LoginResponseArray(
    @SerializedName("userid")var userid:LoginUserModel,
    @SerializedName("token")var token:String
)

class LoginUserModel (
    @SerializedName("id")var id:Int,
    @SerializedName("firstname")var firstname:String,
    @SerializedName("mobile")var mobile:String
)