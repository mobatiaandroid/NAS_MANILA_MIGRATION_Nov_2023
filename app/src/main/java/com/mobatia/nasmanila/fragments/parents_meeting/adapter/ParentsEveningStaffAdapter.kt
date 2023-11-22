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
import com.mobatia.nasmanila.activities.contact_us.model.StaffModel
import com.mobatia.nasmanila.fragments.parents_meeting.model.StaffPtaModel

class ParentsEveningStaffAdapter(
    var mContext: Context?,
    var mSocialMediaModels: ArrayList<StaffPtaModel>
) : RecyclerView.Adapter<ParentsEveningStaffAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgIcon: ImageView
        var listTxtView: TextView

        init {

            imgIcon = view.findViewById(R.id.imagicon)
            listTxtView = view.findViewById(R.id.listTxtTitle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_staff_list_adapter, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


//   holder.imgIcon.setVisibility(View.GONE);
//   holder.imgIcon.setBackgroundResource(R.drawable.roundfb);
        holder.listTxtView.setText(mSocialMediaModels[position].name)
        if (!mSocialMediaModels[position].staff_photo.equals("")) {
            Glide.with(mContext!!)
                .load(mSocialMediaModels[position].staff_photo.toString())
                .placeholder(R.drawable.staff).into(holder.imgIcon)
        } else {
            holder.imgIcon.setImageResource(R.drawable.staff)
        }
    }

    override fun getItemCount(): Int {
        return mSocialMediaModels!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}