package com.mobatia.nasmanila.activities.enrichment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.enrichment.adapter.CCAfinalReviewAfterSubmissionAdapter
import com.mobatia.nasmanila.activities.enrichment.model.CCAAttendanceModel
import com.mobatia.nasmanila.activities.enrichment.model.CCAReviewAfterSubmissionModel
import com.mobatia.nasmanila.activities.enrichment.model.Cca_reviewsApiModel
import com.mobatia.nasmanila.activities.enrichment.model.Cca_reviewsResponseModel
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class CCAsReviewAfterSubmissionActivity:AppCompatActivity() {
    lateinit var mContext: Context
    var progressBarDialog: ProgressBarDialog? = null
    var recyclerViewLayoutManager: GridLayoutManager? = null
    var recycler_review: RecyclerView? = null
    var headermanager: HeaderManager? = null
    var relativeHeader: RelativeLayout? = null
    var back: ImageView? = null
    var home: ImageView? = null
    var attendanceListIcon: ImageView? = null
    var tab_type = "CCAs"
    var extras: Bundle? = null
    var mCCADetailModelArrayList: ArrayList<CCAReviewAfterSubmissionModel>? = null
    var textViewCCAaItem: TextView? = null
    var weekList: ArrayList<String>? = null
    var absentDaysChoice2Array: ArrayList<String>? = null
    var presentDaysChoice2Array: ArrayList<String>? = null
    var upcomingDaysChoice2Array: ArrayList<String>? = null
    var absentDaysChoice1Array: ArrayList<String>? = null
    var presentDaysChoice1Array: ArrayList<String>? = null
    var upcomingDaysChoice1Array: ArrayList<String>? = null
    var datestringChoice1: ArrayList<CCAAttendanceModel?>? = null
    var datestringChoice2: ArrayList<CCAAttendanceModel?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cca_review_after_submit)
        mContext = this
        progressBarDialog = ProgressBarDialog(mContext!!)
        extras = intent.extras
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")!!
        }
        weekList = java.util.ArrayList()
        weekList!!.add("Sunday")
        weekList!!.add("Monday")
        weekList!!.add("Tuesday")
        weekList!!.add("Wednesday")
        weekList!!.add("Thursday")
        weekList!!.add("Friday")
        weekList!!.add("Saturday")
        absentDaysChoice2Array = java.util.ArrayList()
        presentDaysChoice2Array = java.util.ArrayList()
        upcomingDaysChoice2Array = java.util.ArrayList()
        absentDaysChoice1Array = java.util.ArrayList()
        presentDaysChoice1Array = java.util.ArrayList()
        upcomingDaysChoice1Array = java.util.ArrayList()
        datestringChoice1 = java.util.ArrayList()
        datestringChoice2 = java.util.ArrayList()
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        recycler_review = findViewById<View>(R.id.recycler_view_cca) as RecyclerView
        textViewCCAaItem = findViewById<View>(R.id.textViewCCAaItem) as TextView
        headermanager = HeaderManager(this@CCAsReviewAfterSubmissionActivity, tab_type)
        headermanager!!.getHeader(relativeHeader, 0)
        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        back!!.setOnClickListener {
            AppUtils.hideKeyBoard(mContext)
            finish()
        }
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(mContext, HomeListAppCompatActivity::class.java)
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        recycler_review!!.setHasFixedSize(true)
        recyclerViewLayoutManager = GridLayoutManager(mContext, 1)
        recycler_review!!.layoutManager = recyclerViewLayoutManager
        mCCADetailModelArrayList = java.util.ArrayList()
//        textViewCCAaItem.setText(Html.fromHtml(PreferenceManager.getCCATitle(mContext) + "<br/>" + PreferenceManager.getStudNameForCCA(mContext)));
        //        textViewCCAaItem.setText(Html.fromHtml(PreferenceManager.getCCATitle(mContext) + "<br/>" + PreferenceManager.getStudNameForCCA(mContext)));
        if (PreferenceManager.getStudClassForCCA(mContext).equals("")) {
            textViewCCAaItem!!.text = Html.fromHtml(
                PreferenceManager.getCCATitle(mContext) + "<br/>" + PreferenceManager.getStudNameForCCA(
                    mContext
                )
            )
        } else {
            textViewCCAaItem!!.text = Html.fromHtml(
                (PreferenceManager.getCCATitle(mContext) + "<br/>" + PreferenceManager.getStudNameForCCA(
                    mContext
                )).toString() + "<br/>Year Group : " + PreferenceManager.getStudClassForCCA(mContext)
            )
        }
        if (AppUtils.isNetworkConnected(mContext)) {
            ccaReviewListAPI()
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
    }

    private fun ccaReviewListAPI() {
        progressBarDialog!!.show()

var reviewbody=Cca_reviewsApiModel(PreferenceManager.getStudIdForCCA(mContext),PreferenceManager.getCCAItemId(mContext))
        val call: Call<Cca_reviewsResponseModel> = ApiClient.getClient.cca_reviews("Bearer "+ PreferenceManager.getAccessToken(mContext),reviewbody)
        call.enqueue(object : Callback<Cca_reviewsResponseModel> {
            override fun onResponse(
                call: Call<Cca_reviewsResponseModel>,
                response: Response<Cca_reviewsResponseModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {

                        if (responseData!!.response.data.size > 0) {
                            for (j in weekList!!.indices) {
                                for (i in 0 until responseData!!.response.data.size) {
                                    val item: CCAReviewAfterSubmissionModel =
                                        responseData!!.response.data.get(i)
                                    val gson = Gson()
                                    val eventJson = gson.toJson(item)
                                    try {
                                        val jsonObject = JSONObject(eventJson)
                                        if (jsonObject.optString("day")
                                                .equals(weekList!![j])
                                        ) {
                                            mCCADetailModelArrayList!!.add(
                                                addCCAReviewlist(
                                                    jsonObject
                                                )
                                            )
                                        }
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                            if (mCCADetailModelArrayList!!.size > 0) {
                                val mCCAsActivityAdapter = CCAfinalReviewAfterSubmissionAdapter(
                                    mContext,
                                    mCCADetailModelArrayList!!
                                )
                                recycler_review!!.adapter = mCCAsActivityAdapter
                            }
                        } else {
                            Toast.makeText(
                                this@CCAsReviewAfterSubmissionActivity,
                                "No CCAs available.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else if (status_code.equals("301")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext,
                            getString(R.string.error_heading),
                            getString(R.string.missing_parameter),
                            R.drawable.infoicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("304")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.email_exists),
                            R.drawable.infoicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("305")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.incrct_usernamepswd),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("306")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.invalid_email),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                } else {
                    progressBarDialog!!.dismiss()
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        getString(R.string.error_heading),
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<Cca_reviewsResponseModel>, t: Throwable) {

            }
        })
    }

    private fun addCCAReviewlist(dataObject: JSONObject): CCAReviewAfterSubmissionModel {
        val mCCAModel = CCAReviewAfterSubmissionModel()
        if (dataObject.has("day")){
        }
        mCCAModel.day=dataObject.optString("day")
        datestringChoice1 = ArrayList()
        datestringChoice2 = ArrayList()
        if (dataObject.has("choice1")) {
            val choice1: JSONObject = dataObject.optJSONObject("choice1")
            if (choice1 != null) {
                if (choice1.has("cca_item_name")) {
                    mCCAModel.choicee1=choice1.optString("cca_item_name")
                    mCCAModel.cca_item_start_time=choice1.optString("cca_item_start_time")
                    mCCAModel.cca_item_end_time=choice1.optString("cca_item_end_time")
                    val absentDaysChoice1 = choice1.optJSONArray("absentDays")
                    mCCAModel.venue=choice1.optString("venue")
                    mCCAModel.cca_item_description=choice1.optString("cca_item_description")
                    absentDaysChoice1Array = ArrayList()
                    if (choice1.has("absentDays")) {
                        for (i in 0 until absentDaysChoice1.length()) {
                            absentDaysChoice1Array!!.add(absentDaysChoice1.optString(i))
                        }
                    }
                    presentDaysChoice1Array = ArrayList()
                    if (choice1.has("presentDays")) {
                        val presentDaysChoice1 = choice1.optJSONArray("presentDays")
                        for (i in 0 until presentDaysChoice1.length()) {
                            presentDaysChoice1Array!!.add(presentDaysChoice1.optString(i))
                        }
                    }
                    upcomingDaysChoice1Array = java.util.ArrayList()
                    if (choice1.has("upcomingDays")) {
                        val upcomingDaysChoice1 = choice1.optJSONArray("upcomingDays")
                        for (i in 0 until upcomingDaysChoice1.length()) {
                            upcomingDaysChoice1Array!!.add(upcomingDaysChoice1.optString(i))
                        }
                    }
                } else {
                    mCCAModel.choicee1="0"
                }
            } else {
                mCCAModel.choicee1="0"
            }
        } else {
            mCCAModel.choicee1="-1"
        }
        if (dataObject.has("choice2")) {
            val choice2: JSONObject = dataObject.optJSONObject("choice2")
            if (choice2 != null) {
                if (choice2.has("cca_item_name")) {
                    mCCAModel.choicee2=choice2.optString("cca_item_name")
                    mCCAModel.cca_item_start_time=choice2.optString("cca_item_start_time")
                    mCCAModel.cca_item_end_time=choice2.optString("cca_item_end_time")
                    mCCAModel.cca_item_description=choice2.optString("cca_item_description")
                    mCCAModel.venue=choice2.optString("venue")
                    val absentDaysChoice2 = choice2.optJSONArray("absentDays")
                    if (choice2.has("absentDays")) {
                        absentDaysChoice2Array = java.util.ArrayList()
                        for (i in 0 until absentDaysChoice2.length()) {
                            absentDaysChoice2Array!!.add(absentDaysChoice2.optString(i))
                        }
                    }
                    presentDaysChoice2Array = java.util.ArrayList()
                    val presentDaysChoice2 = choice2.optJSONArray("presentDays")
                    if (choice2.has("presentDays")) {
                        for (i in 0 until presentDaysChoice2.length()) {
                            presentDaysChoice2Array!!.add(presentDaysChoice2.optString(i))
                        }
                    }
                    upcomingDaysChoice2Array = java.util.ArrayList()
                    val upcomingDaysChoice2 = choice2.optJSONArray("upcomingDays")
                    if (choice2.has("upcomingDays")) {
                        for (i in 0 until upcomingDaysChoice2.length()) {
                            upcomingDaysChoice2Array!!.add(upcomingDaysChoice2.optString(i))
                        }
                    }
                } else {
                    mCCAModel.choicee2="0"
                }
            } else {
                mCCAModel.choicee2="0"
            }
        } else {
            mCCAModel.choicee2="-1"
        }

        if (absentDaysChoice1Array!!.size > 0) {
            for (i in absentDaysChoice1Array!!.indices) {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend=absentDaysChoice1Array!![i]
                mCCAAttendanceModel.statusCCA="a"
                datestringChoice1!!.add(mCCAAttendanceModel)
            }
        }

        if (upcomingDaysChoice1Array!!.size > 0) {
            for (i in upcomingDaysChoice1Array!!.indices)  //                datestringChoice1.add(upcomingDaysChoice1Array.get(i).toString());
            {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend=upcomingDaysChoice1Array!![i]
                mCCAAttendanceModel.statusCCA="u"
                datestringChoice1!!.add(mCCAAttendanceModel)
            }
        }

        if (presentDaysChoice1Array!!.size > 0) {
            for (i in presentDaysChoice1Array!!.indices)  //                datestringChoice1.add(presentDaysChoice1Array.get(i).toString());
            {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend=presentDaysChoice1Array!![i]
                mCCAAttendanceModel.statusCCA="p"
                datestringChoice1!!.add(mCCAAttendanceModel)
            }
        }
        if (absentDaysChoice2Array!!.size > 0) {
            for (i in absentDaysChoice2Array!!.indices)  //                datestringChoice2.add(absentDaysChoice2Array.get(i).toString());
            {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend=absentDaysChoice2Array!![i]
                mCCAAttendanceModel.statusCCA="a"
                datestringChoice2!!.add(mCCAAttendanceModel)
            }
        }
        if (upcomingDaysChoice2Array!!.size > 0) {
            for (i in upcomingDaysChoice2Array!!.indices)  //                datestringChoice2.add(upcomingDaysChoice2Array.get(i).toString());
            {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend=upcomingDaysChoice2Array!![i]
                mCCAAttendanceModel.statusCCA="u"
                datestringChoice2!!.add(mCCAAttendanceModel)
            }
        }
        if (presentDaysChoice2Array!!.size > 0) {
            for (i in presentDaysChoice2Array!!.indices)  //                datestringChoice2.add(presentDaysChoice2Array.get(i).toString());
            {
                val mCCAAttendanceModel = CCAAttendanceModel()
                mCCAAttendanceModel.dateAttend=presentDaysChoice2Array!![i]
                mCCAAttendanceModel.statusCCA="p"
                datestringChoice2!!.add(mCCAAttendanceModel)
            }
        }
        if (datestringChoice1!!.size > 0) {
//            Collections.sort(datestringChoice1, new Comparator<String>() {
//                DateFormat f = new SimpleDateFormat("yyyy-mm-dd");
//
//                @Override
//                public int compare(String o1, String o2) {
//                    try {
//                        return f.parse(o1).compareTo(f.parse(o2));
//                    } catch (ParseException e) {
//                        throw new IllegalArgumentException(e);
//                    }
//                }
//            });
            Collections.sort(datestringChoice1, object : Comparator<CCAAttendanceModel?> {
              

                override fun compare(s1: CCAAttendanceModel?, s2: CCAAttendanceModel?): Int {
                    return s1!!.dateAttend!!.compareTo(s2!!.dateAttend.toString())
                }
            })
        }
        if (datestringChoice2!!.size > 0) {
//            Collections.sort(datestringChoice2, new Comparator<String>() {
//                DateFormat f = new SimpleDateFormat("yyyy-mm-dd");
//
//                @Override
//                public int compare(String o1, String o2) {
//                    try {
//                        return f.parse(o1).compareTo(f.parse(o2));
//                    } catch (ParseException e) {
//                        throw new IllegalArgumentException(e);
//                    }
//                }
//            });
            Collections.sort(datestringChoice1, object : Comparator<CCAAttendanceModel?> {
            

                override fun compare(s1: CCAAttendanceModel?, s2: CCAAttendanceModel?): Int {
                    return s1!!.dateAttend!!.compareTo(s2!!.dateAttend.toString())
                }
            })
        }
        mCCAModel.calendarDaysChoice1=datestringChoice1
        mCCAModel.calendarDaysChoice2=datestringChoice2
        return mCCAModel
    }

}
