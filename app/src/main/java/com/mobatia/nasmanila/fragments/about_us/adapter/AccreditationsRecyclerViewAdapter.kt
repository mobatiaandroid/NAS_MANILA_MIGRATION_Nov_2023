package com.mobatia.nasmanila.fragments.about_us.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.fragments.about_us.model.AboutUsItemsModel

class AccreditationsRecyclerViewAdapter(
    mContext: Context,
    mAboutUsListArray: ArrayList<AboutUsItemsModel?>
) : BaseAdapter() {

    private val mContext: Context? = mContext
    private val mAboutusLists: java.util.ArrayList<String>? = null
    private val aboutusModelArrayList: java.util.ArrayList<AboutUsItemsModel?>? = mAboutUsListArray
    private var view: View? = null
    private var mTitleTxt: TextView? = null
    private val mImageView: ImageView? = null

    override fun getCount(): Int {
        return aboutusModelArrayList!!.size
    }

    override fun getItem(position: Int): String? {


        return aboutusModelArrayList!!.get(position).toString()
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        view = if (convertView == null) {
            val inflate = LayoutInflater.from(mContext)
            inflate.inflate(R.layout.custom_aboutus_list_adapter, null)
        } else {
            convertView
        }
        try {
            mTitleTxt = view!!.findViewById<View>(R.id.listTxtTitle) as TextView
            mTitleTxt!!.setText(aboutusModelArrayList!![position]!!.title)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view


    }
}