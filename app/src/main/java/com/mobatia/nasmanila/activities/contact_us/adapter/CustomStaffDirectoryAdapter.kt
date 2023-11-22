package com.mobatia.nasmanila.activities.contact_us.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.contact_us.model.StaffModel

class CustomStaffDirectoryAdapter(
    var mcontext: Context, var mStaffList: ArrayList<StaffModel>

) :
    RecyclerView.Adapter<CustomStaffDirectoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.custom_aboutus_list_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mTitleTxt.setText(mStaffList[position].category_name)


    }

    override fun getItemCount(): Int {
        return mStaffList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mTitleTxt: TextView

        init {

            mTitleTxt = itemView.findViewById(R.id.listTxtTitle)
        }
    }
}
