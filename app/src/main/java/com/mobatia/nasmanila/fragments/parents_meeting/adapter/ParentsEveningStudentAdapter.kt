package com.mobatia.nasmanila.fragments.parents_meeting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.fragments.absence.model.StudentModel

class ParentsEveningStudentAdapter(
    var mContext: Context?,
    var mSocialMediaModels: ArrayList<StudentModel>
) : RecyclerView.Adapter<ParentsEveningStudentAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgIcon: ImageView
        var listTxtView: TextView
        var listTxtClass:TextView


        init {

            imgIcon = view.findViewById(R.id.imagicon)
            listTxtView = view.findViewById(R.id.listTxtTitle)
            listTxtClass = view.findViewById(R.id.listTxtClass)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_student_list_adapter, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


//   holder.imgIcon.setVisibility(View.GONE);
//        holder.imgIcon.setBackgroundResource(R.drawable.roundfb);
        if (!mSocialMediaModels[position].photo.equals("")) {
            Glide.with(mContext!!)
                .load(mSocialMediaModels[position].photo)
                .placeholder(R.drawable.student).into(holder.imgIcon)
        } else {
            holder.imgIcon.setImageResource(R.drawable.student)
        }
        holder.listTxtView.setText(mSocialMediaModels[position].name)
        holder.listTxtClass.setText(mSocialMediaModels[position].mClass)


    }

    override fun getItemCount(): Int {
        return mSocialMediaModels!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}