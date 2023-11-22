package com.mobatia.nasmanila.fragments.absence.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.fragments.absence.model.LeavesModel

class AbsenceRecyclerAdapter(
    var mContext: Context?,
    var mLeavesList: ArrayList<LeavesModel>
) : RecyclerView.Adapter<AbsenceRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var listDate: TextView
        var listStatus: TextView
        var listBackGround: RelativeLayout

        init {

            //listName= (TextView) view.findViewById(R.id.listName);
            listDate = view.findViewById(R.id.listDate) as TextView
            listStatus = view.findViewById(R.id.listStatus) as TextView


            listBackGround = view.findViewById(R.id.listBackGround) as RelativeLayout
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.absence_recycler_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (mLeavesList[position].to_date == mLeavesList[position].from_date) {
            holder.listDate.setText(AppUtils.dateParsingToDdMmYyyy(mLeavesList[position].from_date))
        } else {
            holder.listDate.text = Html.fromHtml(
                AppUtils.dateParsingToDdMmYyyy(mLeavesList[position].from_date) + " to " +
                        AppUtils.dateParsingToDdMmYyyy(mLeavesList[position].to_date)
            )
        }
        //holder.listName.setText(AppPreferenceManager.getStudentName(mContext));
        //holder.listName.setText(AppPreferenceManager.getStudentName(mContext));
        if (mLeavesList[position].status.equals("1")) {
            holder.listStatus.text = "Approved"
            holder.listStatus.setTextColor(mContext!!.resources.getColor(R.color.nas_green))
        } else if (mLeavesList[position].status.equals("2")) {
            holder.listStatus.text = "Pending"
            holder.listStatus.setTextColor(mContext!!.resources.getColor(R.color.rel_six))
        } else if (mLeavesList[position].status.equals("3")) {
            holder.listStatus.text = "Rejected"
            holder.listStatus.setTextColor(mContext!!.resources.getColor(R.color.rel_nine))
        }
    }

    override fun getItemCount(): Int {
        return mLeavesList!!.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}