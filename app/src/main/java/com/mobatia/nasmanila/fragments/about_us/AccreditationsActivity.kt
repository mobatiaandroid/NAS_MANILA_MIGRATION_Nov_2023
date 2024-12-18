package com.mobatia.nasmanila.fragments.about_us

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListActivity
import com.mobatia.nasmanila.activities.pdf.PDFViewActivity
import com.mobatia.nasmanila.activities.web_view.LoadUrlWebViewActivity
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.about_us.adapter.AccreditationsRecyclerViewAdapter
import com.mobatia.nasmanila.fragments.about_us.model.AboutUsItemsModel

class AccreditationsActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var mContext: Context? = null
    var extras: Bundle? = null
    var tab_type: String? = null
    var bannner_img:kotlin.String? = null
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null
    var mAboutUsListArray: ArrayList<AboutUsItemsModel?>? = null
    var bannerImagePager: ImageView? = null
    var bannerUrlImageArray = ArrayList<String>()
    private var mTermsCalendarListView: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accreditations)
        mContext = this
        initialiseUI()
    }
    private fun initialiseUI() {
        extras = intent.extras
        if (extras != null) {
            /*mAboutUsListArray = extras!!
                .getSerializable("array") as ArrayList<AboutUsModel>?*/
            bannner_img = extras!!.getString("banner_image")
            if (bannner_img != "") {
                bannerUrlImageArray.add(bannner_img!!)
            }
            mAboutUsListArray=PreferenceManager.getaboutusarray(mContext!!)
        }
        mAboutUsListArray=PreferenceManager.getaboutusarray(mContext!!)
        relativeHeader = findViewById(R.id.relativeHeader)
        mTermsCalendarListView = findViewById(R.id.mTermsCalendarListView) as ListView
        bannerImagePager = findViewById(R.id.bannerImageViewPager) as ImageView

        bannerImagePager!!.visibility = View.GONE

        headermanager = HeaderManager(this@AccreditationsActivity, NaisClassNameConstants.ABOUT_US)
        headermanager!!.getHeader(relativeHeader!!, 1 )
        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
//        mTermsCalendarListView!!.onItemClickListener = AdapterView.OnItemClickListener()
        back!!.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
            finish()
        }
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val intent = Intent(mContext, HomeListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        Glide.with(mContext!!).load(bannner_img!!).centerCrop()
            .into(bannerImagePager!!)
        mTermsCalendarListView!!.onItemClickListener = this
        val recyclerViewAdapter = AccreditationsRecyclerViewAdapter(mContext!!, mAboutUsListArray!!)
        mTermsCalendarListView!!.adapter = recyclerViewAdapter
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if (mAboutUsListArray!![position]!!.url!!
                .endsWith(".pdf")
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                var intent:Intent =  Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(mAboutUsListArray!![position]!!.url), "application/pdf");

                startActivity(intent);
                /*startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(mAboutUsListArray!![position]!!.url)
                    )
                )*/
            } else {
                val intent = Intent(mContext, PDFViewActivity::class.java)
                intent.putExtra("pdf_url", mAboutUsListArray!![position]!!.url)
                startActivity(intent)
            }
//            Intent intent = new Intent(mContext, PDFViewActivity.class);
//            intent.putExtra("pdf_url",mAboutUsListArray.get(position).getItemPdfUrl());
//            startActivity(intent);
        } else {
            val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
            intent.putExtra("url", mAboutUsListArray!![position]!!.url)
            intent.putExtra("tab_type", NaisClassNameConstants.ABOUT_US)
            startActivity(intent)
        }
    }
}