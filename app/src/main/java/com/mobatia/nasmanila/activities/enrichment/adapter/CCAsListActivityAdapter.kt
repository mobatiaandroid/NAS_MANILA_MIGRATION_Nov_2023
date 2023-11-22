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
import com.mobatia.nasmanila.activities.enrichment.model.CCAModel
import com.mobatia.nasmanila.common.common_classes.AppUtils

class CCAsListActivityAdapter(var mContext: Context,var mCCAmodelArrayList: ArrayList<CCAModel>)  :
    RecyclerView.Adapter<CCAsListActivityAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_cca_first_list, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


//    holder.listTxtView.setText(mSocialMediaModels.get(position).toString());
        holder.listTxtView.setText(mCCAmodelArrayList[position].title)
        holder.listTxtViewDate.setText(
            AppUtils.dateParsingTodd_MMM_yyyy(mCCAmodelArrayList[position].from_date) + " to " + AppUtils.dateParsingTodd_MMM_yyyy(
                mCCAmodelArrayList[position].to_date
            )
        )
        if (mCCAmodelArrayList[position].isAttendee.equals("0")) {
            if (mCCAmodelArrayList[position].isSubmissiondateOver.equals("1")) {
                //closed
                holder.statusImageView.setImageResource(R.drawable.closed)
            } else {
                holder.statusImageView.setImageResource(R.drawable.edit) //edit
            }
        } else if (mCCAmodelArrayList[position].isAttendee.equals("1")) {
            //approved
            holder.statusImageView.setImageResource(R.drawable.approve_new)
        } else if (mCCAmodelArrayList[position].isAttendee.equals("2")) {
            //pending
            holder.statusImageView.setImageResource(R.drawable.pending)
        }

    }

    override fun getItemCount(): Int {
        return mCCAmodelArrayList!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var listTxtView: TextView
        var listTxtViewDate: TextView
        var statusImageView: ImageView

        init {

            listTxtView = view.findViewById(R.id.textViewCCAaItem) as TextView
            listTxtViewDate = view.findViewById(R.id.textViewCCAaDateItem) as TextView
            statusImageView = view.findViewById(R.id.statusImageView) as ImageView
        }
    }
}