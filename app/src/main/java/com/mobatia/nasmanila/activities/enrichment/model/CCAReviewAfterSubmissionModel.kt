package com.mobatia.nasmanila.activities.enrichment.model

class CCAReviewAfterSubmissionModel (
    var day: String? = null,
    var choice1: Choice1Obj? = null,
    var choice2: Choice1Obj? = null,
    var choicee1: String? = null,
    var choicee2: String? = null,
    var calendarDaysChoice1: ArrayList<CCAAttendanceModel?>? = null,
    var calendarDaysChoice2: ArrayList<CCAAttendanceModel?>? = null,
    var cca_item_start_time: String? = null,
    var cca_item_end_time: String? = null,
    var cca_item_description: String? = null,
    var venue: String? = null
)

class Choice1Obj(
    var day:String?=null,
    var cca_details_id:String?=null,
    var cca_item_name:String?=null,
    var cca_item_start_time:String?=null,
    var cca_item_end_time:String?=null,
    var isAttendee:String?=null,
    var absentDays:ArrayList<String>?=null,
    var presentDays:ArrayList<String>?=null,
    var upcomingDays:ArrayList<String>?=null,
    var attending_status:String?=null
)
