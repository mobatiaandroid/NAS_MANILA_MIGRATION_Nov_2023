package com.mobatia.nasmanila.activities.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.login.LoginActivity
import com.mobatia.nasmanila.activities.tutorial.adapter.TutorialViewPagerAdapter
import java.util.Arrays

class TutorialActivity : AppCompatActivity() {
    lateinit var mImgCircle: Array<ImageView?>
    lateinit var mLinearLayout: LinearLayout
    lateinit var mTutorialViewPager: ViewPager
    lateinit var mContext: Context
    lateinit var imageSkip: ImageView
    //var mTutorialViewPagerAdapter: TutorialViewPagerAdapter? = null
    var mPhotoList = ArrayList(
        Arrays.asList(
            R.drawable.tut_1,
            R.drawable.tut_2,
            R.drawable.tut_4,
            R.drawable.tut_6
        )
    )
    var dataType = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        mContext = this
        val bundle = intent.extras
        if (bundle != null) {
            dataType = bundle.getInt("type", 0)
        }
        initialiseViewPagerUI()
    }

    private fun initialiseViewPagerUI() {
        mTutorialViewPager = findViewById<View>(R.id.tutorialViewPager) as ViewPager
        imageSkip = findViewById<View>(R.id.imageSkip) as ImageView
        mLinearLayout = findViewById<View>(R.id.linear) as LinearLayout
        mImgCircle = arrayOfNulls(mPhotoList.size)
       var mTutorialViewPagerAdapter = TutorialViewPagerAdapter(mContext, mPhotoList)
        mTutorialViewPager!!.currentItem = 0
        mTutorialViewPager.adapter=mTutorialViewPagerAdapter
       // mTutorialViewPager!!.setAdapter(mTutorialViewPagerAdapter)
        addShowCountView(0)
        imageSkip.setOnClickListener{
            if (dataType == 0) {
                finish()
            } else {
                val loginIntent = Intent(
                    mContext,
                    LoginActivity::class.java
                )
                startActivity(loginIntent)
                finish()
            }

        }
        mTutorialViewPager.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                for (i in mPhotoList.indices) {
                    mImgCircle[i]!!.setBackgroundDrawable(
                        resources
                            .getDrawable(R.drawable.blackround)
                    )
                }
                if (position < mPhotoList.size) {
                    mImgCircle[position]!!.setBackgroundDrawable(
                        resources
                            .getDrawable(R.drawable.redround)
                    )
                    mLinearLayout.removeAllViews()
                    addShowCountView(position)
                } else {
                    mLinearLayout.removeAllViews()
                    if (dataType == 0) {
                        finish()
                    } else {
                        val loginIntent = Intent(
                            mContext,
                            LoginActivity::class.java
                        )
                        startActivity(loginIntent)
                        finish()
                    }
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })
        mTutorialViewPager.adapter!!.notifyDataSetChanged()
    }

    private fun addShowCountView(count: Int) {
        for (i in mPhotoList.indices) {
            mImgCircle[i] = ImageView(this)
            val layoutParams = LinearLayout.LayoutParams(
                resources
                    .getDimension(R.dimen.home_circle_width).toInt(), resources.getDimension(
                    R.dimen.home_circle_height
                ).toInt()
            )
            mImgCircle[i]!!.layoutParams = layoutParams
            if (i == count) {
                mImgCircle[i]!!.setBackgroundResource(R.drawable.redround)
            } else {
                mImgCircle[i]!!.setBackgroundResource(R.drawable.blackround)
            }
            mLinearLayout.addView(mImgCircle[i])
        }
    }
}