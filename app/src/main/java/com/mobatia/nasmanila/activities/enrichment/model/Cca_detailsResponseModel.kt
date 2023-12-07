package com.mobatia.nasmanila.activities.enrichment.model

import com.google.gson.annotations.SerializedName

class Cca_detailsResponseModel (
    @SerializedName("responsecode")var responsecode:String,
    @SerializedName("response")var response: Cca_detailsResponse
)

class Cca_detailsResponse (
    @SerializedName("statuscode")var statuscode:String,
    @SerializedName("data")var data:ArrayList<CCAModel>
)

class CCAModel (
    var cca_days_id: String? = null,
    var from_date: String? = null,
    var to_date: String? = null,
    var title: String? = null,
    var submission_dateTime: String? = null,
    var isAttendee: String? = null,
    var isSubmissiondateOver: String? = null,
    var details: ArrayList<CCADetailModel?>? = null
   /* @SerializedName("cca_days_id")var cca_days_id:Int,
    @SerializedName("from_date")var from_date:String,
    @SerializedName("to_date")var to_date:String,
    @SerializedName("title")var title:String,
    @SerializedName("isAttendee")var isAttendee:String,
    @SerializedName("submission_dateTime")var submission_dateTime:String,
    @SerializedName("isSubmissiondateOver")var isSubmissiondateOver:String,
    @SerializedName("details")var details:ArrayList<CCADetailModel>*/
)

class CCADetailModel(
    var day: String? = null,
    var choicee1: String? = null,
    var choicee2: String? = null,
    var choice1: ArrayList<CCAchoiceModel?>? = null,
    var choice1Id: String? = null,
    var choice2Id: String? = null,
    var cca_item_start_timechoice1: String? = null,
    var cca_item_end_timechoice1: String? = null,
    var cca_item_start_timechoice2: String? = null,
    var cca_item_end_timechoice2: String? = null,
    var cca_item_description: String? = null,
    var venue: String? = null,
    var choice2: ArrayList<CCAchoiceModel?>? = null
)
class CCAchoiceModel(
    var cca_details_id: String? = null,
    var cca_item_name: String? = null,
    @SerializedName("attending_status")
    var status: String? = null,
    var dayChoice: String? = null,
    var choice2Empty: String? = null,
    var choice1Empty: String? = null,
    var cca_item_start_time: String? = null,
    var cca_item_end_time: String? = null,
    var cca_item_description: String? = null,
    var venue: String? = null,
    @SerializedName("isAttendee")
    var isattending: String? = null
)