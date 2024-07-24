package com.mobatia.nasmanila.common.common_classes

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

class DeviceRegistrtionmodel
    (
    @SerializedName("deviceType") var device_type: String,
    @SerializedName("deviceId") var deviceId: String,
    @SerializedName("device_name") var device_name: String,
    @SerializedName("app_version") var app_version: String,
    @SerializedName("device_identifier") var device_identifier: String

            )