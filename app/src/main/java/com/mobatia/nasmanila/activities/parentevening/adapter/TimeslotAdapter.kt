package com.mobatia.nasmanila.activities.parentevening.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.parentevening.model.PTATimeSlotsResponseModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

internal class TimeslotAdapter(
    var context: Context,
    var timeSlotList: ArrayList<PTATimeSlotsResponseModel.PTAResponseData.Slot>,
    var cancel: TextView
) :
    RecyclerView.Adapter<TimeslotAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var time_from: TextView = view.findViewById(R.id.timeFrom)
        var time_to: TextView = view.findViewById(R.id.timeTo)
        var gridClickRelative: LinearLayout = view.findViewById(R.id.gridClickRelative)
        var card_view: CardView = view.findViewById(R.id.card_view)
        var txt_colon: TextView = view.findViewById(R.id.textViewTo)

        var startTimeAm = true
        var select_bool = true
        var startTime = ""
        var alreadyslotBookedByUser: Boolean = false
        var confirmedslotBookedByUser: Boolean = false

    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.time_slot_adapter, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (timeSlotList.get(position).status.equals("0")) {
            holder.gridClickRelative.setBackgroundResource(R.drawable.time_curved_rel_layout)
            holder.time_from.setTextColor(context.resources.getColor(R.color.black))
            holder.txt_colon.setTextColor(context.resources.getColor(R.color.black))
            holder.time_to.setTextColor(context.resources.getColor(R.color.black))
        } else if (timeSlotList.get(position).status.equals("1")) {
            holder.gridClickRelative.setBackgroundResource(R.drawable.slotbooked_curved_rel_layout)
            holder.time_from.setTextColor(context.resources.getColor(R.color.black))
            holder.txt_colon.setTextColor(context.resources.getColor(R.color.black))
            holder.time_to.setTextColor(context.resources.getColor(R.color.black))
        } else if (timeSlotList.get(position).status.equals("2")) {

            holder.gridClickRelative.background =
                context.resources.getDrawable(R.drawable.slotbookedbyuser_curved_rel_layout)
            holder.time_from.setTextColor(context.resources.getColor(R.color.black))
            holder.txt_colon.setTextColor(context.resources.getColor(R.color.black))
            holder.time_to.setTextColor(context.resources.getColor(R.color.black))

        } else if (timeSlotList.get(position).status.equals("3")) {
            holder.gridClickRelative.setBackgroundResource(R.drawable.parent_slot_new)
            holder.time_from.setTextColor(context.resources.getColor(R.color.white))
            holder.txt_colon.setTextColor(context.resources.getColor(R.color.white))
            holder.time_to.setTextColor(context.resources.getColor(R.color.white))

        }
        var from_time = timeSlotList[position].slotStartTime
        val inputFormat: DateFormat = SimpleDateFormat("hh:mm:ss")
        val outputFormat: DateFormat = SimpleDateFormat("hh:mm aa")
        val inputDateStr = from_time
        val date: Date = inputFormat.parse(inputDateStr)
        val formt_fromtime = outputFormat.format(date)
        var to_time = timeSlotList[position].slotEndTime
        val inputFormat2: DateFormat = SimpleDateFormat("hh:mm:ss")
        val outputFormat2: DateFormat = SimpleDateFormat("hh:mm aa")
        val inputDateStr2 = to_time
        val date2: Date = inputFormat2.parse(inputDateStr2)
        val formt_totime: String = outputFormat2.format(date2)

        holder.time_from.text = formt_fromtime
        holder.time_to.text = formt_totime

    }

    override fun getItemCount(): Int {

        return timeSlotList.size

    }
}