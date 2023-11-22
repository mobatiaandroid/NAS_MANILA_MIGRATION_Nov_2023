package com.mobatia.nasmanila.fragments.contact_us.model

import com.google.gson.annotations.SerializedName

class ContacUsModel (
    @SerializedName("description")var description:String,
    @SerializedName("latitude")var latitude:String,
    @SerializedName("longitude")var longitude:String,
    @SerializedName("contacts")var contacts:ArrayList<ContactUsListModel>
)
