package com.mobatia.nasmanila.activities.parent_essential.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.fragments.parent_essentials.model.SubmenuParentEssentials

class ParentEssentialActivityListAdapter(
    var mcontext: Context, var mnNewsLetterModelArrayList: ArrayList<SubmenuParentEssentials?>?

) :
    RecyclerView.Adapter<ParentEssentialActivityListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.custom_aboutus_list_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.submenu.setText(mnNewsLetterModelArrayList!![position]!!.submenu)


    }

    override fun getItemCount(): Int {
        return mnNewsLetterModelArrayList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var submenu: TextView

        init {

            submenu = itemView.findViewById(R.id.listTxtTitle)
        }
    }
}