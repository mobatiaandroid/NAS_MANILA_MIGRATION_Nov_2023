package com.mobatia.nasmanila.activities.parentevening

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.activities.parentevening.adapter.ReviewAdapter
import com.mobatia.nasmanila.activities.parentevening.model.PTAReviewResponseModel
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewAppointmentsRecyclerViewActivity : AppCompatActivity() {
    lateinit var mContext: Context

    //    lateinit var backRelative: RelativeLayout
//    lateinit var heading: TextView
//    lateinit var logoClickImgView: ImageView
    lateinit var progressDialogAdd: ProgressBarDialog
    lateinit var review_rec: RecyclerView
    lateinit var review_list: ArrayList<PTAReviewResponseModel.PTAReviewResponse.ReviewListModel>
    lateinit var home_icon: ImageView
    lateinit var headermanager: HeaderManager
    lateinit var relativeHeader: RelativeLayout
    lateinit var back: ImageView
    lateinit var home: ImageView
    lateinit var heading: TextView

    //    lateinit var idList: ArrayList<Int>
//    lateinit var confirm_tv: TextView
    var confimVisibility: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_meeting_review)

        mContext = this
        initfn()

    }

    private fun initfn() {
        progressDialogAdd = ProgressBarDialog(mContext)
//        heading = findViewById(R.id.heading)
//        backRelative = findViewById(R.id.backRelative)
//        logoClickImgView = findViewById(R.id.logoClickImgView)
//        confirm_tv = findViewById(R.id.confirmTV)
        review_list = ArrayList()
        review_rec = findViewById(R.id.recycler_review)
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout

        headermanager =
            HeaderManager(this@ReviewAppointmentsRecyclerViewActivity, "Parents' Meeting")
        headermanager!!.getHeader(relativeHeader, 0)
        back = headermanager!!.getLeftButton()!!
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        back!!.setOnClickListener {
            AppUtils.hideKeyBoard(mContext)
            finish()
        }
        home = headermanager!!.getLogoButton()!!
        home!!.setOnClickListener {
            val `in` = Intent(mContext, HomeListAppCompatActivity::class.java)
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        heading = findViewById(R.id.heading)
        heading.text = "Parents' Meeting"
        if (AppUtils.isNetworkConnected(mContext)) {
            reviewlistcall(progressDialogAdd, mContext, review_rec)

        } else {
            AppUtils.showDialogAlertDismiss(
                mContext,
                "Alert",
                "Cannot continue. Try again later.",
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }

//        backRelative.setOnClickListener(View.OnClickListener {
//            finish()
        /* val intent = Intent(mContext, ParentMeetingDetailActivity::class.java)
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
         startActivity(intent)*/
//        })
//        heading.text = "Review Appointments"
//        logoClickImgView.setOnClickListener(View.OnClickListener {
//            val intent = Intent(mContext, HomeListAppCompatActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(intent)
//        })


    }

    fun reviewlistcall(
        progressDialog: ProgressBarDialog,
        mContext: Context,
        review_rec: RecyclerView
    ) {
        review_list = ArrayList()

        progressDialog.show()
        val token = PreferenceManager.getAccessToken(mContext)

        val call: Call<PTAReviewResponseModel> =
            ApiClient.getClient.ptaReviewList("Bearer " + token)
        call.enqueue(object : Callback<PTAReviewResponseModel> {
            override fun onFailure(call: Call<PTAReviewResponseModel>, t: Throwable) {
                progressDialog.dismiss()
            }

            override fun onResponse(
                call: Call<PTAReviewResponseModel>,
                response: Response<PTAReviewResponseModel>
            ) {
                progressDialog.dismiss()
                //val arraySize :Int = response.body()!!.responseArray.studentList.size
                if (response.body()!!.response.statusCode == "303") {
                    review_list.addAll(response.body()!!.response.data)
                    if (review_list.size > 0) {
                        progressDialog.dismiss()
                        review_rec.layoutManager = LinearLayoutManager(mContext)

                        var review_adapter = ReviewAdapter(
                           mContext, review_list, ReviewAppointmentsRecyclerViewActivity(),
                            progressDialog, review_rec
                        )
                        review_rec.adapter = review_adapter
                    } else {
                        var review_adapter = ReviewAdapter(
                            mContext, ArrayList(), ReviewAppointmentsRecyclerViewActivity(),
                            progressDialog, review_rec
                        )
                       review_rec.adapter = review_adapter
                        AppUtils.showDialogAlertDismiss(
                            mContext,
                            "Alert",
                            "No Appointments Available.",
                            R.drawable.exclamationicon,
                            R.drawable.roundred
                        )

                    }

//                    for (i in review_list.indices) {
//                        if (review_list[i].status == 2 && review_list[i].booking_open.equals("y")) {
//                            idList.add(review_list[i].id.toInt())
//
//                            // confirm_tv.visibility=View.VISIBLE
//                            confimVisibility = true
//
//                        } else {
//                            // confimVisibility=false
//                            // confirm_tv.visibility=View.GONE
//                        }
//                    }


                } else {
                    AppUtils.showDialogAlertDismiss(
                        mContext,
                        "Alert",
                        "Cannot continue. Please try again later.",
                        R.drawable.exclamationicon,
                        R.drawable.roundred
                    )

                }


            }

        })

    }

    private fun showerrorConfirm(context: Context, message: String, msgHead: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        iconImageView.setImageResource(R.drawable.questionmark_icon)
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()
//            callConfirmPta()

        }
        btn_Cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}
