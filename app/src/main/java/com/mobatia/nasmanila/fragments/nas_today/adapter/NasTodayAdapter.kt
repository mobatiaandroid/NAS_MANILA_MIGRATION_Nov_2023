package com.mobatia.nasmanila.fragments.nas_today.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.nas_today.NasTodayDetailWebViewActivityNew
import com.mobatia.nasmanila.fragments.nas_today.model.NasTodayModel
import java.util.regex.Pattern


class NasTodayAdapter : BaseAdapter{
    private var mContext: Context

    //	private ArrayList<AboutUsModel> mAboutusList;
    private var mNasTodayList: ArrayList<NasTodayModel>
    private val view: View? = null
    private val mTitleTxt: TextView? = null
    private val mImageView: ImageView? = null
    private var mTitle: String? = null
    private var mTabId: String? = null
    lateinit var mViewHolder: ViewHolder

    //	public CustomAboutUsAdapter(Context context,
    //								ArrayList<AboutUsModel> arrList, String title, String tabId) {
    //		this.mContext = context;
    //		this.mAboutusList = arrList;
    //		this.mTitle = title;
    //		this.mTabId = tabId;
    //	}
    constructor(
        context: Context,
        mNasTodayList: ArrayList<NasTodayModel>, title: String?, tabId: String?
    ) {
        mContext = context
        this.mNasTodayList = mNasTodayList
        mTitle = title
        mTabId = tabId
    }

    constructor(
        context: Context,
        arrList: ArrayList<NasTodayModel>
    ) {
        mContext = context
        mNasTodayList = arrList
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getCount()
     */
    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return mNasTodayList.size
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItem(int)
     */
    override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return mNasTodayList[position]
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItemId(int)
     */
    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflate = LayoutInflater.from(mContext)
            mViewHolder = ViewHolder()
            convertView = inflate.inflate(R.layout.custom_nastoday_list_adapter, null)
            mViewHolder!!.listTxtTitle =
                convertView.findViewById<View>(R.id.listTxtTitle) as TextView
            mViewHolder!!.listTxtDate = convertView.findViewById<View>(R.id.listTxtDate) as TextView
            mViewHolder!!.listTxtDescription =
                convertView.findViewById<View>(R.id.listTxtDescription) as TextView
            mViewHolder!!.readMoreTextView =
                convertView.findViewById<View>(R.id.readMoreTextView) as TextView
            convertView.tag = mViewHolder
        } else {
            mViewHolder = convertView.tag as ViewHolder
        }
        try {
            mViewHolder!!.listTxtTitle!!.text = mNasTodayList[position].title!!.trim { it <= ' ' }
            mViewHolder!!.listTxtDescription!!.text =
                mNasTodayList[position].description!!.trim { it <= ' ' }
            mViewHolder!!.listTxtDate!!.text =
                mNasTodayList[position].day + "-" + mNasTodayList[position].month + "-" + mNasTodayList[position].year + " " + mNasTodayList[position].time
            mViewHolder!!.readMoreTextView!!.setOnClickListener {
                var webViewComingUpDetail = "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head><meta name=\"viewport\" content=\"user-scalable=no, \n" +
                        "initial-scale=1.0, maximum-scale=1.0, width=device-width\">" +
                        "<style>\n" +
                        "\n" +
                        "@font-face {\n" +
                        "font-family: SourceSansPro-Semibold;" +
                        "src: url(SourceSansPro-Semibold.ttf);" +
                        "font-family: SourceSansPro-Regular;" +
                        "src: url(SourceSansPro-Regular.ttf);" +
                        "}" +
                        ".title {" +
                        "font-family: SourceSansPro-Regular;" +
                        "font-size:16px;" +
                        "text-align:left;" +
                        "color:	#46C1D0;" +
                        "}" +
                        "a {" +
                        "font-family: SourceSansPro-Regular;" +
                        "color:	#46C1D0;" +
                        "}" +
                        ".date {" +
                        "font-family: SourceSansPro-Regular;" +
                        "font-size:12px;" +
                        "text-align:left;" +
                        "color:	#46C1D0;" +
                        "}" +
                        ".description {" +
                        "font-family: SourceSansPro-Light;" +
                        "src: url(SourceSansPro-Light.otf);" +
                        "font-size:14px;" +
                        "word-wrap:break-word;" +
                        "color: #808080;" +  //black removed
                        "}" +
                        "</style>\n" + "</head>" +
                        "<body>" +
                        "<p class='title'>" + mNasTodayList[position].title + "</p>" +
                        "<p class='date'>" + mNasTodayList[position].day + "-" + mNasTodayList[position].month + "-" + mNasTodayList[position].year +
                        " " + mNasTodayList[position].time + "</p>"
                if (!mNasTodayList[position].image.equals("", ignoreCase = true)) {
                    webViewComingUpDetail =
                        webViewComingUpDetail + "<center><img src='" + mNasTodayList[position].image + "'width='100%', height='auto'></center>"
                }
                var desc = ""
                desc = mNasTodayList[position].description!!.replace(" ".toRegex(), "&nbsp;")
                var pullLinks: List<String> =
                    ArrayList()
                pullLinks = pullLinks(mNasTodayList[position].description)
                if (pullLinks.size > 0) {
                    for (i in pullLinks.indices) {
                        desc = desc.replace(
                            pullLinks[i].toRegex(),
                            "<a href=\"" + pullLinks[i] + "\">" + pullLinks[i] + " </a>"
                        )
                    }
                } else {
                    desc = mNasTodayList[position].description!!.replace(" ".toRegex(), "&nbsp;")
                }
                webViewComingUpDetail =
                    "$webViewComingUpDetail<p class='description'>" + desc.replace(
                        "\n".toRegex(),
                        "<br>"
                    ) + "</p>" + "</body>\n</html>"
                val mIntent = Intent(mContext, NasTodayDetailWebViewActivityNew::class.java)
                mIntent.putExtra("webViewComingDetail", webViewComingUpDetail)
                mIntent.putExtra("pdf", mNasTodayList[position].pdf)
                mContext.startActivity(mIntent)
            }
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return convertView!!
    }

    fun pullLinks(text: String?): List<String> {
//        String links ="";
        val links: MutableList<String> = ArrayList()

        //String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        val regex =
            "\\(?\\b(https?://|http?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]"
        val p = Pattern.compile(regex)
        val m = p.matcher(text)
        while (m.find()) {
            var urlStr = m.group()
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length - 1)
            }

//            links=urlStr;
            if (!links.contains(urlStr)) links.add(urlStr)
        }
        return links
    }

    inner class ViewHolder {
        var listTxtTitle: TextView? = null
        var listTxtDate: TextView? = null
        var listTxtDescription: TextView? = null
        var readMoreTextView: TextView? = null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
