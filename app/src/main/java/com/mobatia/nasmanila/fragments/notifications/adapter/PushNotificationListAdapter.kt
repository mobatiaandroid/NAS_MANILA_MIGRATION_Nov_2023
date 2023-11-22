package com.mobatia.nasmanila.fragments.notifications.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.fragments.notifications.OnBottomReachedListener
import com.mobatia.nasmanila.fragments.notifications.model.NotificationsListModel

class PushNotificationListAdapter(
    var mContext: Context,
    var pushNotificationList: ArrayList<NotificationsListModel>
): RecyclerView.Adapter<PushNotificationListAdapter.ViewHolder>(){

    var onBottomReachedListener: OnBottomReachedListener? = null
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView? = null
        var title: TextView? = null
        init {
            image = itemView.findViewById<ImageView?>(R.id.Img)
            title = itemView.findViewById<TextView?>(R.id.title)

        }

    }

   /* fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener?) {
        this.onBottomReachedListener = onBottomReachedListener
    }*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_adapter_pushlist_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       /* if (position == pushNotificationList.size - 1) {
            onBottomReachedListener!!.onBottomReached(position)
        }*/

        if (pushNotificationList[position].type.equals("Video", ignoreCase = true)) {
            holder.image!!.setImageResource(R.drawable.alerticon_video)
        } else if (pushNotificationList[position].type.equals("Text", ignoreCase = true)) {
            holder.image!!.setImageResource(R.drawable.alerticon_text)
        } else if (pushNotificationList[position].type.equals("image", ignoreCase = true)) {
            holder.image!!.setImageResource(R.drawable.alerticon_image)
        } else if (pushNotificationList[position].type.equals("Voice", ignoreCase = true)) {
            holder.image!!.setImageResource(R.drawable.alerticon_audio)
        }
        holder.title!!.text = pushNotificationList[position].message
        holder.itemView.setOnClickListener { }
    }

    override fun getItemCount(): Int {
        return pushNotificationList.size
    }

}