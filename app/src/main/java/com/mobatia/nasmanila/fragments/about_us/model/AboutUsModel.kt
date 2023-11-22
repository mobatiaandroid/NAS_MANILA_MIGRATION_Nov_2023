package com.mobatia.nasmanila.fragments.about_us.model


import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList



class AboutUsModel(
    var id: String? = null,
    var tab_type: String? = null,
    var items: ArrayList<AboutUsItemsModel>? = null,
    var banner_image: String? = null,
    var description: String? = null,
    var url: String? = null,
    var image: String? = null




    /*var Url: String? = null
    var webUrl: String? = null
    var description: String? = null
    var email: String? = null
    var title: String? = null
    var TabType: String? = null
    var aboutusModelArrayList: ArrayList<AboutUsModel>? = null
    var mFacilitylListArray: ArrayList<AboutUsModel>? = null
    var itemDesc: String? = null
    var itemImageUrl: String? = null
    var itemPdfUrl: String? = null
    var itemTitle: String? = null*/
)
class AboutUsItemsModel(
    var id: String? = null,
    var url: String? = null,
    var title: String? = null,
    var image: String? = null
)