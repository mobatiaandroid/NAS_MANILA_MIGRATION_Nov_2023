package com.mobatia.nasmanila.activities.enrichment.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.enrichment.model.CCAReviewAfterSubmissionModel
import com.mobatia.nasmanila.common.common_classes.AppUtils

class CCAfinalReviewAfterSubmissionAdapter(var mContext: Context,var mCCADetailModelArrayList: ArrayList<CCAReviewAfterSubmissionModel>)  :
    RecyclerView.Adapter<CCAfinalReviewAfterSubmissionAdapter.ViewHolder>() {

    var dialog: Dialog? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_cca_review_after_submit, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
Log.e("adapter","CCAfinalReviewAfterSubmissionAdapter")

//    holder.listTxtView.setText(mSocialMediaModels.get(position).toString());
        holder.textViewCCADay.setText(mCCADetailModelArrayList[position].day)
        holder.attendanceListIcon.setOnClickListener {
            if (!mCCADetailModelArrayList[position].choicee1
                    .equals("0") || !mCCADetailModelArrayList[position].choicee1
                    .equals("-1") || !mCCADetailModelArrayList[position].choicee2
                    .equals("0") || !mCCADetailModelArrayList[position].choicee2
                    .equals("-1")
            ) {
                showAttendanceList(position)
            }
        }
        Log.e("choice1",mCCADetailModelArrayList[position].choicee1.toString())
        if (mCCADetailModelArrayList[position].choicee1.equals("0")) {
            holder.linearChoice1.visibility = View.GONE
            holder.textViewCCAChoice1.text = "Choice 1 : None"
        } else if (mCCADetailModelArrayList[position].choicee1.equals("-1")) {
            holder.linearChoice1.visibility = View.GONE
            //            holder.textViewCCAChoice1.setVisibility(View.INVISIBLE);
            holder.textViewCCAChoice1.text = "Choice 1 : Nil"
        } else {
            holder.linearChoice1.visibility = View.VISIBLE
            //            holder.textViewCCAChoice1.setVisibility(View.VISIBLE);
//            holder.textViewCCAChoice1.setText("Choice 1 : " + mCCADetailModelArrayList.get(position).choicee1);
            Log.e("Choice 1", mCCADetailModelArrayList[position].choicee1.toString())
            holder.textViewCCAChoice1.setText(mCCADetailModelArrayList[position].choicee1)
            if (mCCADetailModelArrayList[position].venue
                    .equals("0") || mCCADetailModelArrayList[position].venue
                    .equals("")
            ) {
                holder.textViewCCAVenue.visibility = View.GONE
            } else {
                holder.textViewCCAVenue.setText(mCCADetailModelArrayList[position].venue)
                holder.textViewCCAVenue.visibility = View.VISIBLE
            }
            if (mCCADetailModelArrayList[position].cca_item_start_time != null && mCCADetailModelArrayList[position].cca_item_end_time != null) {
                holder.textViewCCAaDateItemChoice1.visibility = View.VISIBLE
                holder.textViewCCAaDateItemChoice1.text =
                    "(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList[position].cca_item_start_time) + " - " + AppUtils.convertTimeToAMPM(
                        mCCADetailModelArrayList[position].cca_item_end_time
                    ) + ")"
            } else if (mCCADetailModelArrayList[position].cca_item_start_time != null) {
                holder.textViewCCAaDateItemChoice1.visibility = View.VISIBLE
                holder.textViewCCAaDateItemChoice1.text =
                    "(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList[position].cca_item_start_time) + ")"
            } else if (mCCADetailModelArrayList[position].cca_item_end_time != null) {
                holder.textViewCCAaDateItemChoice1.visibility = View.VISIBLE
                holder.textViewCCAaDateItemChoice1.text =
                    "(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList[position].cca_item_end_time) + ")"
            } else {
                holder.textViewCCAaDateItemChoice1.visibility = View.GONE
            }
        }
        if (mCCADetailModelArrayList[position].choicee2.equals("0")) {
            holder.linearChoice2.visibility = View.GONE
            holder.textViewCCAChoice2.text = "Choice 2 : None"
        } else if (mCCADetailModelArrayList[position].choicee2.equals("-1")) {
            holder.linearChoice2.visibility = View.GONE
            //            holder.textViewCCAChoice2.setVisibility(View.INVISIBLE);
            holder.textViewCCAChoice2.text = "Choice 2 : Nil"
        } else {
            holder.linearChoice2.visibility = View.VISIBLE
            //            holder.textViewCCAChoice2.setVisibility(View.VISIBLE);
            holder.textViewCCAChoice2.setText(mCCADetailModelArrayList[position].choicee2)
            //            holder.textViewCCAChoice2.setText("Choice 2 : " + mCCADetailModelArrayList.get(position).choicee2);
            if (mCCADetailModelArrayList[position].venue
                    .equals("0") || mCCADetailModelArrayList[position].venue
                    .equals("")
            ) {
                holder.textViewCCAVenueChoice2.visibility = View.GONE
            } else {
                holder.textViewCCAVenueChoice2.setText(mCCADetailModelArrayList[position].venue)
                holder.textViewCCAVenueChoice2.visibility = View.VISIBLE
            }
            if (mCCADetailModelArrayList[position].cca_item_start_time != null && mCCADetailModelArrayList[position].cca_item_end_time != null) {
                holder.textViewCCAaDateItemChoice2.visibility = View.VISIBLE
                holder.textViewCCAaDateItemChoice2.text =
                    "(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList[position].cca_item_start_time) + " - " + AppUtils.convertTimeToAMPM(
                        mCCADetailModelArrayList[position].cca_item_end_time
                    ) + ")"
            } else if (mCCADetailModelArrayList[position].cca_item_start_time != null) {
                holder.textViewCCAaDateItemChoice2.visibility = View.VISIBLE
                holder.textViewCCAaDateItemChoice2.text =
                    "(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList[position].cca_item_start_time) + ")"
            } else if (mCCADetailModelArrayList[position].cca_item_end_time != null) {
                holder.textViewCCAaDateItemChoice2.visibility = View.VISIBLE
                holder.textViewCCAaDateItemChoice2.text =
                    "(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList[position].cca_item_end_time) + ")"
            } else {
                holder.textViewCCAaDateItemChoice2.visibility = View.GONE
            }
        }
        if ((mCCADetailModelArrayList[position].choicee1
                .equals("0") || mCCADetailModelArrayList[position].choicee1
                .equals("-1")) && (mCCADetailModelArrayList[position].choicee2
                .equals("0") || mCCADetailModelArrayList[position].choicee2
                .equals("-1"))
        ) {
            holder.attendanceListIcon.visibility = View.INVISIBLE
        } else {
            holder.attendanceListIcon.visibility = View.VISIBLE
        }

    }

    private fun showAttendanceList(mPosition: Int) {
        dialog = Dialog(mContext)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_attendance_list)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (dialog!!.isShowing()) {
            dialog!!.dismiss()
        }
        val dialogDismiss = dialog!!.findViewById<View>(R.id.btn_dismiss) as Button
        val linearChoice3 :LinearLayout= dialog!!.findViewById(R.id.linearChoice1)
        val alertHead:TextView = dialog!!.findViewById(R.id.alertHead) as TextView
        val linearChoice4:LinearLayout = dialog!!.findViewById(R.id.linearChoice2)
        val socialMediaList :RecyclerView= dialog!!.findViewById(R.id.recycler_view_social_media)
        val textViewCCAChoiceFirst =
            dialog!!.findViewById(R.id.textViewCCAChoice1) as TextView
        val textViewCCAChoiceSecond =
            dialog!!.findViewById(R.id.textViewCCAChoice2) as TextView
        val scrollViewMain = dialog!!.findViewById(R.id.scrollViewMain) as ScrollView
        val recycler_view_social_mediaChoice2:RecyclerView =
            dialog!!.findViewById(R.id.recycler_view_social_mediaChoice2)
        alertHead.setText("Attendance report of " + mCCADetailModelArrayList[mPosition].day)

        Log.e("text", mCCADetailModelArrayList[mPosition].choicee1.toString())
        Log.e("text33", mCCADetailModelArrayList[mPosition].day.toString())
//        scrollViewMain.pageScroll(View.FOCUS_DOWN);
        //        scrollViewMain.pageScroll(View.FOCUS_DOWN);
        scrollViewMain.smoothScrollTo(0, 0)
        if (!mCCADetailModelArrayList[mPosition].choicee1
                .equals("0") && !mCCADetailModelArrayList[mPosition].choicee1
                .equals("-1")
        ) {

            textViewCCAChoiceFirst.setText(mCCADetailModelArrayList[mPosition].choicee1)
            Log.e("text", mCCADetailModelArrayList[mPosition].choicee1.toString())
            linearChoice3.visibility = View.VISIBLE
            socialMediaList.visibility = View.VISIBLE
            //socialMediaList.setHasFixedSize(true)
            val llm = LinearLayoutManager(mContext)
            llm.orientation = LinearLayoutManager.VERTICAL
            socialMediaList.layoutManager = llm
            val socialMediaAdapter = CCAAttendenceListAdapter(
                mContext,
                mCCADetailModelArrayList[mPosition].calendarDaysChoice1
            )
            socialMediaList.adapter = socialMediaAdapter
        } else {
            linearChoice3.visibility = View.GONE
            socialMediaList.visibility = View.GONE
        }


        if (!mCCADetailModelArrayList[mPosition].choicee2
                .equals("0") && !mCCADetailModelArrayList[mPosition].choicee2
                .equals("-1")
        ) {
            textViewCCAChoiceSecond
                .setText(mCCADetailModelArrayList[mPosition].choicee2)
            linearChoice4.visibility = View.VISIBLE
            recycler_view_social_mediaChoice2.visibility = View.VISIBLE
            recycler_view_social_mediaChoice2.setHasFixedSize(true)
            val llmrecycler_view_social_mediaChoice2 = LinearLayoutManager(mContext)
            llmrecycler_view_social_mediaChoice2.orientation = LinearLayoutManager.VERTICAL
            recycler_view_social_mediaChoice2.layoutManager = llmrecycler_view_social_mediaChoice2
            val socialMediaAdapterChoice2 = CCAAttendenceListAdapter(
                mContext,
                mCCADetailModelArrayList[mPosition].calendarDaysChoice2
            )
            recycler_view_social_mediaChoice2.adapter = socialMediaAdapterChoice2
        } else {
            linearChoice4.visibility = View.GONE
            recycler_view_social_mediaChoice2.visibility = View.GONE
        }
        dialogDismiss.setOnClickListener { dialog!!.dismiss() }

        dialog!!.show()

    }

    override fun getItemCount(): Int {
        return mCCADetailModelArrayList!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var textViewCCADay: TextView
        var textViewCCAChoice1: TextView
        var textViewCCAChoice2: TextView
        var attendanceListIcon: ImageView
        var linearChoice1: LinearLayout
        var linearChoice2:LinearLayout
        var textViewCCAaDateItemChoice1: TextView
        var textViewCCAaDateItemChoice2: TextView
        var textViewCCAVenue: TextView
        var textViewCCAVenueChoice2: TextView

        init {

            textViewCCAaDateItemChoice1 =
                view.findViewById(R.id.textViewCCAaDateItemChoice1) as TextView
            textViewCCAaDateItemChoice2 =
                view.findViewById(R.id.textViewCCAaDateItemChoice2) as TextView
            textViewCCADay = view.findViewById(R.id.textViewCCADay) as TextView
            textViewCCAChoice1 = view.findViewById(R.id.textViewCCAChoice1) as TextView
            textViewCCAChoice2 = view.findViewById(R.id.textViewCCAChoice2) as TextView
            attendanceListIcon = view.findViewById(R.id.attendanceListIcon) as ImageView
            linearChoice1 = view.findViewById(R.id.linearChoice1) as LinearLayout
            linearChoice2 = view.findViewById(R.id.linearChoice2) as LinearLayout
            textViewCCAVenue = view.findViewById(R.id.textViewCCAVenue) as TextView
            textViewCCAVenueChoice2 =
                view.findViewById(R.id.textViewCCAVenueChoice2) as TextView
        }
    }
}