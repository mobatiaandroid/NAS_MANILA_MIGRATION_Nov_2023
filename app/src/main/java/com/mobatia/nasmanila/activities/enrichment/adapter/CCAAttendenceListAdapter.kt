package com.mobatia.nasmanila.activities.enrichment.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.enrichment.model.CCAAttendanceModel
import com.mobatia.nasmanila.common.common_classes.AppUtils

class CCAAttendenceListAdapter(var mContext: Context, var mSocialMediaModels: ArrayList<CCAAttendanceModel?>?) :
    RecyclerView.Adapter<CCAAttendenceListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_cca_review_attendancelist, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.ccaDate.setText(AppUtils.dateParsingTodd_MMM_yyyy(mSocialMediaModels!![position]!!.dateAttend))
        if (mSocialMediaModels!![position]!!.statusCCA.equals("u")) {
            holder.ccaDateStatus.text = "Upcoming"
            holder.ccaDateStatus.setTextColor(mContext.resources.getColor(R.color.rel_six))
        } else if (mSocialMediaModels!![position]!!.statusCCA.equals("p")) {
            holder.ccaDateStatus.text = "Present"
            holder.ccaDateStatus.setTextColor(mContext.resources.getColor(R.color.nas_green))
        } else if (mSocialMediaModels!![position]!!.statusCCA.equals("a")) {
            holder.ccaDateStatus.text = "Absent"
            holder.ccaDateStatus.setTextColor(mContext.resources.getColor(R.color.rel_nine))
        }

    }

    override fun getItemCount(): Int {
        return mSocialMediaModels!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var ccaDate: TextView
        var ccaDateStatus: TextView

        init {

            ccaDate = view.findViewById(R.id.ccaDate) as TextView
            ccaDateStatus = view.findViewById(R.id.ccaDateStatus) as TextView
        }
    }
}