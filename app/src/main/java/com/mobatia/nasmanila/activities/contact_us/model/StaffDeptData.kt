package com.mobatia.nasmanila.activities.contact_us.model

import com.google.gson.annotations.SerializedName

class StaffDeptData (
    @SerializedName("departments")var departments:ArrayList<String>,
    @SerializedName("staffs")var staffs: ArrayList<Map<String, ArrayList<StaffModelDept>>>,
)

class StaffModelDept (
    var id:String?=null,
    var category_name:String?=null,
    var staffId:String?=null,
    var staffDepartment:String?=null,
    var name:String?=null,
    var staffPhoneNo:String?=null,
    var staffEmail:String?=null,
    var staffContactNo:String?=null,
    var staffAbout:String?=null,
    var staff_photo:String?=null,
    var role:String?=null,
    var Principal: ArrayList<StaffModel?>? = null,
    var Admissions: ArrayList<StaffModel?>? = null,
    var Marketing: ArrayList<StaffModel?>? = null,
    var Finance: ArrayList<StaffModel?>? = null,
    @SerializedName("Human Resource")
    var HumanResource: ArrayList<StaffModel?>? = null,
    var ICT: ArrayList<StaffModel?>? = null,
    var Operations: ArrayList<StaffModel?>? = null,
    var Administration: ArrayList<StaffModel?>? = null,
)