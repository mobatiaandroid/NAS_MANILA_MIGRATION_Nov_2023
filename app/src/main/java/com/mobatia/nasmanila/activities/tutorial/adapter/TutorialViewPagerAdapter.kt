package com.mobatia.nasmanila.activities.tutorial.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.mobatia.nasmanila.R

class TutorialViewPagerAdapter : PagerAdapter {
    var context: Context
    var mImagesArrayListBg = ArrayList<Int>()
    lateinit var inflater: LayoutInflater


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View

    }

    override fun getCount(): Int {
        return mImagesArrayListBg.size
    }


    constructor(context: Context, mImagesArrayListBg: ArrayList<Int>) {
        this.context = context
        this.mImagesArrayListBg = mImagesArrayListBg
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var pageview: View? = null
        inflater = LayoutInflater.from(context)
        pageview = inflater.inflate(R.layout.layout_imagepager_adapter, null)

        val imageView = pageview.findViewById<View>(R.id.adImg) as ImageView
        if (position < mImagesArrayListBg.size) {
            imageView.setBackgroundResource(mImagesArrayListBg[position])
            (container as ViewPager).addView(pageview, 0)
        }


        return pageview
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}