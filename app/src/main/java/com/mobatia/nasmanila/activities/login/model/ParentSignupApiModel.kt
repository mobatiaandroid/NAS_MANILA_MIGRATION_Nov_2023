package com.mobatia.nasmanila.activities.login.model

import com.google.gson.annotations.SerializedName

class ParentSignupApiModel (
    @SerializedName("email")var email:String,
    @SerializedName("deviceid")var deviceid:String,
    @SerializedName("devicetype")var devicetype:String


)