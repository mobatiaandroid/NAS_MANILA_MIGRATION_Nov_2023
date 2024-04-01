package com.mobatia.nasmanila.activities.enrichment.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.enrichment.model.CCADetailModel
import com.mobatia.nasmanila.common.common_classes.AppUtils

class CCAfinalReviewAdapter(var mContext: Context,var mCCADetailModelArrayList: ArrayList<CCADetailModel>)  :
    RecyclerView.Adapter<CCAfinalReviewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_cca_final_review, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


//    holder.listTxtView.setText(mSocialMediaModels.get(position).toString());
        holder.textViewCCADay.setText(mCCADetailModelArrayList[position].day)
        if (mCCADetailModelArrayList[position].choicee1 == null) {
            holder.linearChoice1.visibility = View.GONE
            //            holder.textViewCCAChoice1.setVisibility(View.INVISIBLE);
            holder.textViewCCAChoice1.text = "Choice 1 : Nil"
        } else {
            holder.linearChoice1.visibility = View.VISIBLE
            holder.textViewCCAChoice1.text =
                "Choice 1 : " + mCCADetailModelArrayList[position].choicee1
            if (mCCADetailModelArrayList[position].venue
                    .equals("0") || mCCADetailModelArrayList[position].venue
                    .equals("")
            ) {
                holder.textViewCCAVenue.visibility = View.GONE
            } else {
                holder.textViewCCAVenue.visibility = View.VISIBLE
                holder.textViewCCAVenue.setText(mCCADetailModelArrayList[position].venue)

            }
            if (mCCADetailModelArrayList[position].cca_item_start_timechoice1 != null && mCCADetailModelArrayList[position].cca_item_end_timechoice1 != null) {
                holder.textViewCCAaDateItemChoice1.visibility = View.VISIBLE
                holder.textViewCCAaDateItemChoice1.text =
                    "(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList[position].cca_item_start_timechoice1) + " - " + AppUtils.convertTimeToAMPM(
                        mCCADetailModelArrayList[position].cca_item_end_timechoice1
                    ) + ")"
            } else if (mCCADetailModelArrayList[position].cca_item_start_timechoice1 != null) {
                holder.textViewCCAaDateItemChoice1.visibility = View.VISIBLE
                holder.textViewCCAaDateItemChoice1.text =
                    "(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList[position].cca_item_start_timechoice1) + ")"
            } else if (mCCADetailModelArrayList[position].cca_item_end_timechoice1 != null) {
                holder.textViewCCAaDateItemChoice1.visibility = View.VISIBLE
                holder.textViewCCAaDateItemChoice1.text =
                    "(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList[position].cca_item_end_timechoice1) + ")"
            } else {
                holder.textViewCCAaDateItemChoice1.visibility = View.GONE
            }
        }
        if (mCCADetailModelArrayList[position].choicee2 == null) {
            holder.linearChoice2.visibility = View.GONE
            //            holder.textViewCCAChoice2.setVisibility(View.INVISIBLE);
            holder.textViewCCAChoice2.text = "Choice 2 : Nil"
        } else {
            holder.linearChoice2.visibility = View.VISIBLE
            if (mCCADetailModelArrayList[position].venue
                    .equals("0") || mCCADetailModelArrayList[position].venue
                    .equals("")
            ) {
                holder.textViewCCAVenueChoice2.visibility = View.GONE
            } else {
                holder.textViewCCAVenueChoice2.setText(mCCADetailModelArrayList[position].venue)
                holder.textViewCCAVenueChoice2.visibility = View.VISIBLE
            }
            holder.textViewCCAChoice2.text =
                "Choice 2 : " + mCCADetailModelArrayList[position].choicee2
            if (mCCADetailModelArrayList[position].cca_item_start_timechoice2 != null && mCCADetailModelArrayList[position].cca_item_end_timechoice2 != null) {
                holder.textViewCCAaDateItemChoice2.visibility = View.VISIBLE
                holder.textViewCCAaDateItemChoice2.text =
                    "(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList[position].cca_item_start_timechoice2) + " - " + AppUtils.convertTimeToAMPM(
                        mCCADetailModelArrayList[position].cca_item_end_timechoice2
                    ) + ")"
            } else if (mCCADetailModelArrayList[position].cca_item_start_timechoice2 != null) {
                holder.textViewCCAaDateItemChoice2.visibility = View.VISIBLE
                holder.textViewCCAaDateItemChoice2.text =
                    "(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList[position].cca_item_start_timechoice2) + ")"
            } else if (mCCADetailModelArrayList[position].cca_item_end_timechoice2 != null) {
                holder.textViewCCAaDateItemChoice2.visibility = View.VISIBLE
                holder.textViewCCAaDateItemChoice2.text =
                    "(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList[position].cca_item_end_timechoice2) + ")"
            } else {
                holder.textViewCCAaDateItemChoice2.visibility = View.GONE
            }
        }

    }

    override fun getItemCount(): Int {
        return mCCADetailModelArrayList!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var textViewCCAaDateItemChoice1: TextView
        var textViewCCAaDateItemChoice2: TextView

        var textViewCCAVenue: TextView
        var textViewCCAVenueChoice2: TextView
        var textViewCCADay: TextView
        var textViewCCAChoice1: TextView
        var textViewCCAChoice2: TextView
        var linearChoice1: LinearLayout
        var linearChoice2:LinearLayout

        init {

            textViewCCAaDateItemChoice1 =
                view.findViewById(R.id.textViewCCAaDateItemChoice1) as TextView
            textViewCCAaDateItemChoice2 =
                view.findViewById(R.id.textViewCCAaDateItemChoice2) as TextView
            textViewCCADay = view.findViewById(R.id.textViewCCADay) as TextView
            textViewCCAVenue = view.findViewById(R.id.textViewCCAVenue) as TextView
            textViewCCAVenueChoice2 =
                view.findViewById(R.id.textViewCCAVenueChoice2) as TextView
            textViewCCAChoice1 = view.findViewById(R.id.textViewCCAChoice1) as TextView
            textViewCCAChoice2 = view.findViewById(R.id.textViewCCAChoice2) as TextView
            linearChoice1 = view.findViewById(R.id.linearChoice1) as LinearLayout
            linearChoice2 = view.findViewById(R.id.linearChoice2) as LinearLayout
        }
    }
}