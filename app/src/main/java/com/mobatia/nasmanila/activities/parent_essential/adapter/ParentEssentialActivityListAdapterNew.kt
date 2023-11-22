package com.mobatia.nasmanila.activities.parent_essential.adapter

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
import com.mobatia.nasmanila.fragments.parent_essentials.model.SubmenuParentEssentials

class ParentEssentialActivityListAdapterNew(
    var mcontext: Context, var mnNewsLetterModelArrayList: ArrayList<SubmenuParentEssentials?>?

) :
    RecyclerView.Adapter<ParentEssentialActivityListAdapterNew.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.custom_pdf_adapter_row, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        holder.submenu.setText(mnNewsLetterModelArrayList.get(position).getSubmenu());
        holder.pdfTitle.setText(mnNewsLetterModelArrayList!![position]!!.submenu)

        if (mnNewsLetterModelArrayList!![position]!!.filename.endsWith(".pdf")) {
            holder.imageIcon.setBackgroundResource(R.drawable.pdfdownloadbutton)
        } else {
            holder.imageIcon.setBackgroundResource(R.drawable.webcontentviewbutton)
        }

    }

    override fun getItemCount(): Int {
        return mnNewsLetterModelArrayList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageIcon: ImageView
        var pdfTitle: TextView

        init {

            imageIcon = itemView.findViewById(R.id.imageIcon)
            pdfTitle = itemView.findViewById(R.id.pdfTitle)
        }
    }
}