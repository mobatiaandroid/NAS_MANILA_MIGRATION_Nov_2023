package com.mobatia.nasmanila.fragments.absence.model

import com.google.gson.annotations.SerializedName

class LeaveRequestsApiModel  (
    @SerializedName("student_id")var student_id:String,
    @SerializedName("users_id")var users_id:String
)