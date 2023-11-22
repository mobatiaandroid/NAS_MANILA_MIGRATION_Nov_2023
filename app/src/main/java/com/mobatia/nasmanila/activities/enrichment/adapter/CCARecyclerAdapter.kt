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
import com.mobatia.nasmanila.activities.enrichment.CcaDataModel


class CCARecyclerAdapter(var mContext: Context,var mnNewsLetterModelArrayList: ArrayList<CcaDataModel>) :
    RecyclerView.Adapter<CCARecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.custom_pdf_adapter_row, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        holder.submenu.setText(mnNewsLetterModelArrayList.get(position).getSubmenu());
        holder.pdfTitle.setText(mnNewsLetterModelArrayList[position].name)

        if (mnNewsLetterModelArrayList[position].file.endsWith(".pdf")) {
            holder.imageIcon.setBackgroundResource(R.drawable.pdfdownloadbutton)
        } else {
            holder.imageIcon.setBackgroundResource(R.drawable.webcontentviewbutton)
        }

    }

    override fun getItemCount(): Int {
        return mnNewsLetterModelArrayList!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var imageIcon: ImageView
        var pdfTitle: TextView

        init {

            imageIcon = view.findViewById(R.id.imageIcon)
            pdfTitle = view.findViewById(R.id.pdfTitle)
        }
    }
}