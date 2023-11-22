package com.mobatia.nasmanila.activities.enrichment.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.enrichment.model.WeekListModel
import com.mobatia.nasmanila.common.constants.AppController

class CCAsWeekListAdapter(var mContext: Context, var mWeekListModelArrayList: ArrayList<WeekListModel>,var weekPosition:Int) :
    RecyclerView.Adapter<CCAsWeekListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_weeklist_cca, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


//    holder.listTxtView.setText(mSocialMediaModels.get(position).toString());
        holder.listTxtView.setText(mWeekListModelArrayList[position].weekDayMMM)
        if (AppController().weekList!!.get(position).choiceStatus
                .equals("0") || AppController().weekList!!.get(position).choiceStatus1
                .equals("0")
        ) {
            holder.selectionCompletedView.setBackgroundResource(R.drawable.curve_filled_cca_pending)
            holder.linearBg.setBackgroundResource(R.color.white)
            holder.linearChoice.setBackgroundResource(R.color.white)
        } else if (AppController().weekList!!.get(position).choiceStatus
                .equals("2") && AppController().weekList!!.get(position).choiceStatus1
                .equals("2")
        ) {
            holder.selectionCompletedView.setBackgroundResource(R.drawable.curve_filled_cca_not_available)
            holder.linearBg.setBackgroundResource(R.color.light_grey)
            holder.linearChoice.setBackgroundResource(R.color.light_grey)
        } else {
            holder.selectionCompletedView.setBackgroundResource(R.drawable.curve_filled_cca_completed)
            holder.linearBg.setBackgroundResource(R.color.white)
            holder.linearChoice.setBackgroundResource(R.color.white)
        }
        if (weekPosition == position) {
            holder.weekSelectedImageView.visibility = View.VISIBLE
        } else {
            holder.weekSelectedImageView.visibility = View.INVISIBLE
        }

    }

    override fun getItemCount(): Int {
        return mWeekListModelArrayList!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var listTxtView: TextView
        var selectionCompletedView: View
        var weekSelectedImageView: ImageView
        var linearBg: LinearLayout
        var linearChoice: LinearLayout

        init {

            listTxtView = view.findViewById(R.id.textViewCCAaItem) as TextView
            selectionCompletedView = view.findViewById(R.id.selectionCompletedView) as View
            weekSelectedImageView = view.findViewById(R.id.weekSelectedImageView) as ImageView
            linearBg = view.findViewById(R.id.linearBg) as LinearLayout
            linearChoice = view.findViewById(R.id.linearChoice) as LinearLayout
        }
    }
}