package com.mobatia.nasmanila.activities.enrichment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.enrichment.adapter.CCAfinalReviewAdapter
import com.mobatia.nasmanila.activities.enrichment.model.CCADetailModel
import com.mobatia.nasmanila.activities.enrichment.model.Cca_submitApiModel
import com.mobatia.nasmanila.activities.enrichment.model.Cca_submitResponseModel
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.constants.AppController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CCAsReviewActivity :AppCompatActivity(){
    lateinit var mContext: Context
    var progressBarDialog: ProgressBarDialog? = null
    var recyclerViewLayoutManager: GridLayoutManager? = null
    var recycler_review: RecyclerView? = null
    var headermanager: HeaderManager? = null
    var relativeHeader: RelativeLayout? = null
    var back: ImageView? = null
    var submitBtn: Button? = null
    var home: ImageView? = null
    var tab_type = "CCAs"
    var extras: Bundle? = null
    var CCADetailModelArrayList: ArrayList<CCADetailModel>? = ArrayList()
    var mCCADetailModelArrayList: ArrayList<CCADetailModel>? = null
    var mCCAItemIdArray: ArrayList<String>? = null
    var textViewCCAaItem: TextView? = null
    var cca_details = ""
    var cca_detailsId = "["
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cca_review)
        mContext = this
        progressBarDialog = ProgressBarDialog(mContext!!)
        extras = intent.extras
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")!!
        }
        CCADetailModelArrayList= ArrayList()
        CCADetailModelArrayList=AppController.CCADetailModelArrayList
        relativeHeader = findViewById(R.id.relativeHeader) as RelativeLayout
        recycler_review = findViewById(R.id.recycler_view_cca) as RecyclerView
        textViewCCAaItem = findViewById(R.id.textViewCCAaItem) as TextView
        submitBtn = findViewById(R.id.submitBtn) as Button
        headermanager = HeaderManager(this@CCAsReviewActivity, "CCA Summary") //tab_type

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
        mCCADetailModelArrayList = ArrayList()
        mCCAItemIdArray = ArrayList()

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
        for (i in 0 until AppController.weekList!!.size) {
            for (j in 0 until CCADetailModelArrayList!!.size) {
                if (AppController.weekList!!.get(i).weekDay.equals(
                        CCADetailModelArrayList!!.get(j)!!.day
                    )
                ) {
                    val mCCADetailModel = CCADetailModel()
                    mCCADetailModel.day=
                        CCADetailModelArrayList!!.get(j)!!.day

                    mCCADetailModel.choicee1=
                        CCADetailModelArrayList!!.get(j)!!.choicee1

                    mCCADetailModel.choicee2=
                       CCADetailModelArrayList!!.get(j)!!.choicee2!!

                    mCCADetailModel.choice1Id=
                        CCADetailModelArrayList!!.get(j)!!.choice1Id
                    mCCADetailModel.choice2Id=
                        CCADetailModelArrayList!!.get(j)!!.choice2Id
                    mCCADetailModel.venue=""
                    for (k in 0 until CCADetailModelArrayList!!.get(j)!!
                        .choice1!!
                        .size) if (CCADetailModelArrayList!!.get(j)!!
                            .choicee1.equals(
                                CCADetailModelArrayList!!.get(j)!!.choice1!!
                                    .get(k)!!!!.cca_item_name
                            )
                    ) {
                        if (CCADetailModelArrayList!!.get(j)!!.choice1!!
                                .get(k)!!!!
                                .cca_item_start_time != null && CCADetailModelArrayList!!.get(
                                j
                            )!!.choice1!!.get(k)!!!!.cca_item_end_time != null
                        ) {
                            mCCADetailModel.cca_item_start_timechoice1=
                               CCADetailModelArrayList!!.get(
                                    j
                                )!!.choice1!!.get(k)!!!!.cca_item_start_time
                            
                            mCCADetailModel.cca_item_end_timechoice1=
                               CCADetailModelArrayList!!.get(
                                    j
                                )!!.choice1!!.get(k)!!!!.cca_item_end_time
                            mCCADetailModel.venue=
                               CCADetailModelArrayList!!.get(
                                    j
                                )!!.choice1!!.get(k)!!!!.venue
                            break
                        }
                    }

                    for (k in 0 until CCADetailModelArrayList!!.get(j)!!
                        .choice2!!
                        .size) if (CCADetailModelArrayList!!.get(j)!!
                            .choicee2.equals(
                               CCADetailModelArrayList!!.get(j)!!.choice2!!
                                    .get(k)!!!!.cca_item_name
                            )
                    ) {

                        if (CCADetailModelArrayList!!.get(j)!!.choice2!!
                                .get(k)!!
                                .cca_item_start_time != null &&CCADetailModelArrayList!!.get(
                                j
                            )!!.choice2!!.get(k)!!.cca_item_end_time != null
                        ) {
                            mCCADetailModel.cca_item_start_timechoice2=
                               CCADetailModelArrayList!!.get(
                                    j
                                )!!.choice2!!.get(k)!!.cca_item_start_time
                            
                            mCCADetailModel.cca_item_end_timechoice2=
                               CCADetailModelArrayList!!.get(
                                    j
                                )!!.choice2!!.get(k)!!.cca_item_end_time
                            
                            mCCADetailModel.venue=
                               CCADetailModelArrayList!!.get(
                                    j
                                )!!.choice2!!.get(k)!!.venue
                            break
                        }
                    }

                    mCCADetailModelArrayList!!.add(mCCADetailModel)


                    break

                }

            }
        }

        val mCCAsActivityAdapter = CCAfinalReviewAdapter(mContext, mCCADetailModelArrayList!!)
        recycler_review!!.adapter = mCCAsActivityAdapter

        for (j in mCCADetailModelArrayList!!.indices) {
            if (mCCADetailModelArrayList!![j].choicee1 != null && mCCADetailModelArrayList!![j].choicee2 != null) {
                if (!mCCADetailModelArrayList!![j].choice1Id
                        .equals("-541") && !mCCADetailModelArrayList!![j].choice2Id
                        .equals("-541")
                ) {
                    mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice1Id.toString())
                    mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice2Id.toString())
                } else if (!mCCADetailModelArrayList!![j].choice1Id.equals("-541")) {
                    mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice1Id.toString())
                } else if (!mCCADetailModelArrayList!![j].choice2Id.equals("-541")) {
                    mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice2Id.toString())
                }
            } else if (mCCADetailModelArrayList!![j].choicee1 != null) {
                if (!mCCADetailModelArrayList!![j].choice1Id.equals("-541")) {
                    mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice1Id.toString())
                }
            } else if (mCCADetailModelArrayList!![j].choicee2 != null) {
                if (!mCCADetailModelArrayList!![j].choice2Id.equals("-541")) {
                    mCCAItemIdArray!!.add(mCCADetailModelArrayList!![j].choice2Id.toString())
                }
            }
        }
        if (mCCAItemIdArray!!.size == 0) {
            cca_detailsId += "]}"
        }
        for (i in mCCAItemIdArray!!.indices) {
            cca_detailsId += if (mCCAItemIdArray!!.size - 1 == 0) {
                "\"" + mCCAItemIdArray!![i] + "\"]}"
            } else if (i == mCCAItemIdArray!!.size - 1) {
                mCCAItemIdArray!![i] + "\"]}"
            } else if (i == 0) {
                "\"" + mCCAItemIdArray!![i] + "\",\""
            } else {
                mCCAItemIdArray!![i] + "\",\""
            }
        }
        cca_details =
            "{\"cca_days_id\":\"" + PreferenceManager.getCCAItemId(mContext) + "\",\"student_id\":\"" + PreferenceManager.getStudIdForCCA(
                mContext
            ) + "\",\"users_id\":\"" + "userID" + "\",\"cca_days_details_id\":" + cca_detailsId

        submitBtn!!.setOnClickListener {
            showDialogReviewSubmit(
                mContext as Activity,
                "Confirm",
                "Final Submission",
                R.drawable.exclamationicon,
                R.drawable.round
            )
        }
    }

    private fun showDialogReviewSubmit(activity: Activity, msgHead: String, msg: String, ico: Int, bgIcon: Int) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        text.setText(msg)
        textHead.setText(msgHead)

        val dialogButton = dialog.findViewById<View>(R.id.btn_Ok) as Button
        dialogButton.setOnClickListener {
            dialog.dismiss()
            if (AppUtils.isNetworkConnected(mContext)) {
                ccaSubmitAPI()
            } else {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity,
                    "Network Error",
                    mContext.getString(R.string.no_internet),
                    R.drawable.nonetworkicon,
                    R.drawable.roundred
                )
            }
        }
        val dialogButtonCancel = dialog.findViewById<View>(R.id.btn_Cancel) as Button
        dialogButtonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun ccaSubmitAPI() {
        progressBarDialog!!.show()

        var student = Cca_submitApiModel(cca_details)
        val call: Call<Cca_submitResponseModel> = ApiClient.getClient.cca_submit(
            "Bearer " + PreferenceManager.getAccessToken(mContext),
            student
        )
        call.enqueue(object : Callback<Cca_submitResponseModel> {
            override fun onResponse(
                call: Call<Cca_submitResponseModel>,
                response: Response<Cca_submitResponseModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {
                        showDialogFinish(
                            mContext as Activity,
                            "Alert",
                            "CCA submitted successfully.",
                            R.drawable.tick,
                            R.drawable.round
                        )


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

            override fun onFailure(call: Call<Cca_submitResponseModel>, t: Throwable) {

            }
        })
    }

    private fun showDialogFinish(activity: Activity, msgHead: String, msg: String, ico: Int, bgIcon: Int) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<View>(R.id.textDialog) as TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        text.setText(msg)
        textHead.setText(msgHead)

        val dialogButton = dialog.findViewById<View>(R.id.btnOK) as Button
        dialogButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(mContext, CCA_Activity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        dialog.show()
    }

}
