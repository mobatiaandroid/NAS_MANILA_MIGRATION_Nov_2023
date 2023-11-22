package com.mobatia.nasmanila.fragments.absence.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.fragments.absence.model.LeavesModel
import com.mobatia.nasmanila.fragments.absence.model.StudentModel

class StudentSpinnerAdapter(
    var mContext: Context?,
    var mStudentList: ArrayList<StudentModel>
) : RecyclerView.Adapter<StudentSpinnerAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var mTitleTxt: TextView
        var listTxtClass: TextView
        var imgIcon: ImageView

        init {

            mTitleTxt = view.findViewById(R.id.listTxtTitle) as TextView
            listTxtClass = view.findViewById(R.id.listTxtClass) as TextView
            imgIcon = view.findViewById(R.id.imagicon) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_student_list_adapter, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTitleTxt.setText(mStudentList.get(position).name)
        holder.imgIcon.visibility = View.VISIBLE
        holder.listTxtClass.setText(mStudentList.get(position).mClass)
        if (!mStudentList.get(position).photo.equals("")) {
            Glide.with(mContext!!)
                .load(mStudentList.get(position).photo.toString())
                .placeholder(R.drawable.student).into(holder.imgIcon)
        } else {
            holder.imgIcon.setImageResource(R.drawable.student)
        }
    }

    override fun getItemCount(): Int {
        return mStudentList!!.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}