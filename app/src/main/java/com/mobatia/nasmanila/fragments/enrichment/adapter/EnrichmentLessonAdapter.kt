package com.mobatia.nasmanila.fragments.enrichment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.fragments.enrichment.model.EnrichmentLessonsDataModel

class EnrichmentLessonAdapter(var mContext: Context,var mnNewsLetterModelArrayList: ArrayList<EnrichmentLessonsDataModel>)
    : RecyclerView.Adapter<EnrichmentLessonAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var mTitleTxt: TextView


        init {

            //listName= (TextView) view.findViewById(R.id.listName);
            mTitleTxt = view.findViewById(R.id.listTxtTitle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_aboutus_list_adapter, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTitleTxt.setText(mnNewsLetterModelArrayList[position].name)
    }

    override fun getItemCount(): Int {
        return mnNewsLetterModelArrayList!!.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
