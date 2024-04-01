package com.mobatia.nasmanila.fragments.contact_us.adapter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.fragments.contact_us.model.ContactUsListModel

class ContactUsAdapter(
    var mContext: Context?,
    var mStaffList: ArrayList<ContactUsListModel>
) : RecyclerView.Adapter<ContactUsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var mTitleTxt: TextView
        var arrowImg: ImageView
        var subTitle: TextView
        var cotactEmail:TextView
        init {
            mTitleTxt = view.findViewById<View>(R.id.contactName) as TextView
            subTitle = view.findViewById<View>(R.id.cotactNumber) as TextView
            cotactEmail = view.findViewById<View>(R.id.cotactEmail) as TextView
            arrowImg = view.findViewById<View>(R.id.arrowImg) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_adapter_contact_us, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTitleTxt.setText(mStaffList[position].name)
        if (!mStaffList[position].phone.equals("")) {
            holder.subTitle.setText(mStaffList[position].phone)
        } else {
            holder.subTitle.visibility = View.GONE
        }
        if (position == mStaffList.size - 1) {
            holder.arrowImg.visibility = View.VISIBLE
        } else {
            holder.arrowImg.visibility = View.GONE
        }
        if (mStaffList[position].email
                .equals("") && mStaffList[position].phone
                .equals("")
            && !mStaffList[position].name.equals("")
        ) {
            val lp = holder.mTitleTxt.layoutParams as RelativeLayout.LayoutParams
            lp.addRule(RelativeLayout.CENTER_VERTICAL)
            holder.mTitleTxt.layoutParams = lp
        }
        //forGotpasswordText.setText(getString(R.string.forgot_password));
        //forGotpasswordText.setText(getString(R.string.forgot_password));
        holder.cotactEmail.setText(mStaffList[position].email)
        if (!mStaffList[position].email.equals("")) {
            holder.cotactEmail.paintFlags = holder.cotactEmail.paintFlags
        } else {
            holder.cotactEmail.visibility = View.INVISIBLE
        }
        holder.subTitle.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_CALL)
            dialIntent.data = Uri.parse("tel:" + mStaffList[position].phone)
            mContext!!.startActivity(dialIntent)
        }
        holder.cotactEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf(holder.cotactEmail.text.toString())
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.type = "text/html"
            intent.setPackage("com.google.android.gm")
            mContext!!.startActivity(Intent.createChooser(intent, "Send mail"))
        }
    }

    override fun getItemCount(): Int {
        return mStaffList!!.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}