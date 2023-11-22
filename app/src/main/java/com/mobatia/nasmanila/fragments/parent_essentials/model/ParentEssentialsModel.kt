package com.mobatia.nasmanila.fragments.parent_essentials.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ParentEssentialsModel (
   var name:String="",
    var banner_image:String="",
    var description:String="",
    var contact_email:String="",
    var link:String="",
    var submenu:ArrayList<SubmenuParentEssentials>?= null
    )

class SubmenuParentEssentials (
   var submenu:String="",
    var filename:String=""
)
