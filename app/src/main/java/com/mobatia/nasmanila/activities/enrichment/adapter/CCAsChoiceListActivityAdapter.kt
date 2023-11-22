package com.mobatia.nasmanila.activities.enrichment.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.enrichment.CCASelectionActivity
import com.mobatia.nasmanila.activities.enrichment.model.CCAchoiceModel
import com.mobatia.nasmanila.activities.enrichment.model.WeekListModel
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.constants.AppController

class CCAsChoiceListActivityAdapter(
    var mContext: Context, var mCCAmodelArrayList: ArrayList<CCAchoiceModel?>, var dayPosition: Int,
    var weekList: ArrayList<WeekListModel>?, var choicePosition: Int, var recyclerWeek: RecyclerView?) :
    RecyclerView.Adapter<CCAsChoiceListActivityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_ccalist_activity, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.confirmationImageview.visibility = View.VISIBLE
        if (mCCAmodelArrayList.get(position)!!.venue
                .equals("0") || mCCAmodelArrayList.get(position)!!.venue
                .equals("")
        ) {
            holder.textViewCCAVenue.visibility = View.GONE
        } else {
            holder.textViewCCAVenue.setText(mCCAmodelArrayList.get(position)!!.venue)
            holder.textViewCCAVenue.visibility = View.VISIBLE
        }
        if (choicePosition == 0) {
            if (mCCAmodelArrayList.get(position)!!.status.equals("0")) {
                holder.confirmationImageview.setBackgroundResource(R.drawable.close_icon_with_white)
                //                AppController.weekList.get(dayPosition).setChoiceStatus("0");
            } else {
                holder.confirmationImageview.setBackgroundResource(R.drawable.participatingsmallicon)
                AppController().weekList!!.get(dayPosition).choiceStatus="1"
                CCASelectionActivity().CCADetailModelArrayList!!.get(CCASelectionActivity().ccaDetailpos)!!.choicee1=mCCAmodelArrayList.get(position)!!.cca_item_name
                CCASelectionActivity().CCADetailModelArrayList!!.get(CCASelectionActivity().ccaDetailpos)!!.choice1Id=mCCAmodelArrayList.get(position)!!.cca_details_id
                val mCCAsWeekListAdapter =
                    CCAsWeekListAdapter(mContext, AppController().weekList!!, dayPosition)
                mCCAsWeekListAdapter.notifyDataSetChanged()
                recyclerWeek!!.adapter = mCCAsWeekListAdapter
            }
        } else {
            if (mCCAmodelArrayList.get(position)!!.status.equals("0")) {
                holder.confirmationImageview.setBackgroundResource(R.drawable.close_icon_with_white)
                //                AppController.weekList.get(dayPosition).setChoiceStatus1("0");
            } else {
                holder.confirmationImageview.setBackgroundResource(R.drawable.participatingsmallicon)
                AppController().weekList!!.get(dayPosition).choiceStatus1="1"
                CCASelectionActivity().CCADetailModelArrayList!!.get(CCASelectionActivity().ccaDetailpos)!!
                    .choicee2=mCCAmodelArrayList.get(position)!!.cca_item_name
                CCASelectionActivity().CCADetailModelArrayList!!.get(CCASelectionActivity().ccaDetailpos)!!
                    .choice2Id=mCCAmodelArrayList.get(position)!!.cca_details_id
                val mCCAsWeekListAdapter =
                    CCAsWeekListAdapter(mContext, AppController().weekList!!, dayPosition)
                mCCAsWeekListAdapter.notifyDataSetChanged()
                recyclerWeek!!.adapter = mCCAsWeekListAdapter
            }
        }
        for (j in 0 until AppController().weekList!!.size) {
            if (AppController().weekList!!.get(j).choiceStatus
                    .equals("0") || AppController().weekList!!.get(j).choiceStatus1
                    .equals("0")
            ) {
                CCASelectionActivity().filled = false
                break
            } else {
                CCASelectionActivity().filled = true
            }
            if (!CCASelectionActivity().filled) {
                break
            }
        }
        if (CCASelectionActivity().filled) {
            CCASelectionActivity().submitBtn!!.getBackground().setAlpha(255)
            CCASelectionActivity().submitBtn!!.setVisibility(View.VISIBLE)
            CCASelectionActivity().nextBtn!!.getBackground().setAlpha(255)
            CCASelectionActivity().nextBtn!!.setVisibility(View.GONE)
        } else {
            CCASelectionActivity().submitBtn!!.getBackground().setAlpha(150)
            CCASelectionActivity().submitBtn!!.setVisibility(View.INVISIBLE)
            CCASelectionActivity().nextBtn!!.getBackground().setAlpha(255)
            CCASelectionActivity().nextBtn!!.setVisibility(View.VISIBLE)
        }
//    holder.listTxtView.setText(mSocialMediaModels.get(position).toString());
        //    holder.listTxtView.setText(mSocialMediaModels.get(position).toString());
        holder.listTxtView.setText(mCCAmodelArrayList.get(position)!!.cca_item_name)
        if (mCCAmodelArrayList.get(position)!!
                .cca_item_start_time != null && mCCAmodelArrayList.get(position)!!
                .cca_item_end_time != null
        ) {
            holder.textViewCCAaDateItem.visibility = View.VISIBLE
            holder.textViewCCAaDateItem.text =
                "(" + AppUtils.convertTimeToAMPM(
                    mCCAmodelArrayList.get(position)!!.cca_item_start_time
                ) + " - " + AppUtils.convertTimeToAMPM(
                    mCCAmodelArrayList.get(position)!!.cca_item_end_time
                ) + ")"
        } else if (mCCAmodelArrayList.get(position)!!.cca_item_start_time != null) {
            holder.textViewCCAaDateItem.visibility = View.VISIBLE
            holder.textViewCCAaDateItem.text =
                "(" + AppUtils.convertTimeToAMPM(
                    mCCAmodelArrayList.get(position)!!.cca_item_start_time
                ) + ")"
        } else if (mCCAmodelArrayList.get(position)!!.cca_item_end_time != null) {
            holder.textViewCCAaDateItem.visibility = View.VISIBLE
            holder.textViewCCAaDateItem.text =
                "(" + AppUtils.convertTimeToAMPM(
                    mCCAmodelArrayList.get(position)!!.cca_item_end_time
                ) + ")"
        } else {
            holder.textViewCCAaDateItem.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return mCCAmodelArrayList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var listTxtView: TextView
        var textViewCCAaDateItem: TextView
        var textViewCCAVenue: TextView
        var confirmationImageview: ImageView

        init {

            textViewCCAaDateItem = view.findViewById(R.id.textViewCCAaDateItem) as TextView
            textViewCCAVenue = view.findViewById(R.id.textViewCCAVenue) as TextView
            listTxtView = view.findViewById(R.id.textViewCCAaItem) as TextView
            confirmationImageview = view.findViewById(R.id.confirmationImageview) as ImageView
        }
    }
}