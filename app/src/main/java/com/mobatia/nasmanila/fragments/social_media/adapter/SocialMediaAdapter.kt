package com.mobatia.nasmanila.fragments.social_media.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.fragments.social_media.model.SocialMediaModel
import com.mobatia.nasmanila.fragments.social_media.model.SocialmediaDataModel

class SocialMediaAdapter(var mContext: Context,var mSocialMediaModels: ArrayList<SocialmediaDataModel>) :
    RecyclerView.Adapter<SocialMediaAdapter.MyViewHolder>(){



    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){
        var imgIcon: ImageView? = null
        var listTxtView: TextView? = null
        init {
            imgIcon = view.findViewById<View>(R.id.imagicon) as ImageView
            listTxtView = view.findViewById<View>(R.id.listTxtTitle) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_social_media_adapter, parent, false)

        return SocialMediaAdapter.MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (mSocialMediaModels!![position].tab_type!!.startsWith("Facebook")) {
            holder.imgIcon!!.setImageResource(R.drawable.facebookiconmedia)
            val sdk = Build.VERSION.SDK_INT
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                holder.imgIcon!!.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.roundfb))
            } else {
                holder.imgIcon!!.background = mContext!!.resources.getDrawable(R.drawable.roundfb)
            }
            holder.listTxtView!!.text =
                mSocialMediaModels!![position].tab_type!!.replace("Facebook:", " ")

        } else if (mSocialMediaModels!![position].tab_type!!.startsWith("Instagram")) {
            holder.imgIcon!!.setImageResource(R.drawable.instagramicon)
            val sdk = Build.VERSION.SDK_INT
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                holder.imgIcon!!.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.roundins))
            } else {
                holder.imgIcon!!.background = mContext!!.resources.getDrawable(R.drawable.roundins)
            }
            holder.listTxtView!!.text =
                mSocialMediaModels!![position].tab_type!!.replace("Instagram:", " ")
        } else if(mSocialMediaModels!![position].tab_type!!.startsWith("Twitter")) {
            holder.imgIcon!!.setImageResource(R.drawable.twittericon);
            val sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                holder.imgIcon!!.setBackgroundDrawable(
                    mContext!!.getResources().getDrawable(R.drawable.roundtw)
                );
            } else {
                holder.imgIcon!!.background =
                    mContext!!.getResources().getDrawable(R.drawable.roundtw);
            }
            holder.listTxtView!!.text =
                mSocialMediaModels!![position].tab_type!!.replace(
                    "Twitter:",
                    " "
                )
        }
    }
    override fun getItemCount(): Int {
        return mSocialMediaModels!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}