package com.mobatia.nasmanila.activities.parentevening.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.parentevening.model.PTATimeSlotsResponseModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

internal class ParentsEveningRoomListAdapter(
    var context: Context,
    var timeSlotList: ArrayList<PTATimeSlotsResponseModel.PTAResponseData.Slot>
) :
    RecyclerView.Adapter<ParentsEveningRoomListAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var time: TextView = view.findViewById(R.id.listTxtTitle)
        var room: TextView = view.findViewById(R.id.listTxtClass)

    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.room_list_adapter_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var from_time = timeSlotList[position].slotStartTime
        val inputFormat: DateFormat = SimpleDateFormat("hh:mm:ss")
        val outputFormat: DateFormat = SimpleDateFormat("hh:mm aa")
        val inputDateStr = from_time
        val date: Date = inputFormat.parse(inputDateStr)
        val formt_fromtime: String = outputFormat.format(date)
        var to_time = timeSlotList[position].slotEndTime
        val inputFormat2: DateFormat = SimpleDateFormat("hh:mm:ss")
        val outputFormat2: DateFormat = SimpleDateFormat("hh:mm aa")
        val inputDateStr2 = from_time
        val date2: Date = inputFormat2.parse(inputDateStr2)
        val formt_totime: String = outputFormat2.format(date2)

        var room = timeSlotList[position].room
        holder.time.text = from_time + "-" + to_time
        holder.room.text = "Room : " + room

    }

    override fun getItemCount(): Int {

        return timeSlotList.size

    }
}