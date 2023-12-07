package com.mobatia.nasmanila.activities.parent_essential

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.activities.parent_essential.adapter.ParentEssentialActivityListAdapter
import com.mobatia.nasmanila.activities.pdf.PDFViewActivity
import com.mobatia.nasmanila.activities.pdf.PdfReaderActivity
import com.mobatia.nasmanila.activities.pdf.PdfReaderNextActivity
import com.mobatia.nasmanila.activities.web_view.LoadUrlWebViewActivity
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.DividerItemDecoration
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import com.mobatia.nasmanila.fragments.parent_essentials.model.SubmenuParentEssentials

class ParentEssentialActivity :AppCompatActivity(){
    var extras: Bundle? = null
    var list: ArrayList<SubmenuParentEssentials?>? = null
    var tab_type: String? = null
    var tab_typeName: String? = null
    var bannerImage = ""
    var mContext: Context = this
    var mNewsLetterListView: RecyclerView? = null
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null

    //    ViewPager bannerImagePager;
    var bannerImagePager: ImageView? = null
    var bannerUrlImageArray: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_essential)
        initUI()
        setListAdapter()

    }

    private fun setListAdapter() {
        val newsDetailListAdapter = ParentEssentialActivityListAdapter(mContext, list)
        mNewsLetterListView!!.adapter = newsDetailListAdapter
    }

    private fun initUI() {
        extras = intent.extras
        if (extras != null) {
            list = PreferenceManager.getparentEssentials(mContext)
            tab_type = extras!!.getString("tab_type")
            bannerImage = extras!!.getString("bannerImage")!!
            tab_typeName = extras!!.getString("tab_typeName")
        }
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        bannerImagePager = findViewById<View>(R.id.bannerImagePager) as ImageView
//        bannerImagePager= (ViewPager) findViewById(R.id.bannerImagePager);
        //        bannerImagePager= (ViewPager) findViewById(R.id.bannerImagePager);
        if (!bannerImage.equals("")) {

            Glide.with(mContext).load(bannerImage)
                .into(bannerImagePager!!)
        } else {
            Glide.with(mContext!!).load(R.drawable.default_bannerr).centerCrop().into(bannerImagePager!!)

            //bannerImagePager!!.setImageResource(R.drawable.default_bannerr)
        }
        mNewsLetterListView = findViewById<View>(R.id.mListView) as RecyclerView
        mNewsLetterListView!!.setHasFixedSize(true)
        mNewsLetterListView!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.list_divider)))
        headermanager = HeaderManager(this@ParentEssentialActivity, tab_type)
        headermanager!!.getHeader(relativeHeader, 0)
        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        back!!.setOnClickListener {
            AppUtils.hideKeyBoard(mContext)
            finish()
        }
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(mContext, HomeListAppCompatActivity::class.java)
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        mNewsLetterListView!!.layoutManager = llm
        mNewsLetterListView!!.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (list!!.size <= 1) {
                    if (list!![position]!!.filename.endsWith(".pdf")) {

                        val intent = Intent(mContext, PdfReaderActivity::class.java)
                        intent.putExtra("pdf_url", list!![position]!!.filename)
                        startActivity(intent)
                    } else {

                        val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                        intent.putExtra("url", list!![position]!!.filename)
                        intent.putExtra("tab_type", tab_type)
                        startActivity(intent)
                    }
                } else {
                    if (list!![position]!!.filename.endsWith(".pdf") && tab_typeName.equals(
                            "Lunch Menu",
                            ignoreCase = true
                        )
                    ) {
                        val intent = Intent(mContext, PdfReaderNextActivity::class.java)
                        intent.putExtra("position", position)
                        intent.putExtra("submenuArray", list)
                        startActivity(intent)
                    } else if (list!![position]!!.filename.endsWith(".pdf")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                             var intent:Intent =  Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(list!![position]!!.filename), "application/pdf");

                            startActivity(intent);
                            /*startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(list!![position]!!.filename)
                                )
                            )*/
                        } else {
                            val intent = Intent(mContext, PDFViewActivity::class.java)
                            intent.putExtra("pdf_url", list!![position]!!.filename)
                            startActivity(intent)
                        }
                        //                                Intent intent = new Intent(mContext, PDFViewActivity.class);
//                                intent.putExtra("pdf_url", list.get(position).getFilename());
//                                startActivity(intent);
                    } else {
                        val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                        intent.putExtra("url", list!![position]!!.filename)
                        intent.putExtra("tab_type", tab_type)
                        startActivity(intent)
                    }
                }
            }

        })
    }

}
