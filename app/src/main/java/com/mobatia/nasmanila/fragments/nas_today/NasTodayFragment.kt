package com.mobatia.nasmanila.fragments.nas_today

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.nas_today.adapter.NasTodayAdapter
import com.mobatia.nasmanila.fragments.nas_today.model.NasTodayModel
import com.mobatia.nasmanila.fragments.nas_today.model.NasTodayResponseModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NasTodayFragment(nasToday: String, tabNasToday: String) : Fragment() {
    var mTitleTextView: TextView? = null
    private var mRootView: View? = null
    private var mContext: Context? = null
    private var mNasTodayListView: ListView? = null
    private val mTitle: String? = null
    private val mTabId: String? = null
    private var relMain: RelativeLayout? = null
    private val mBannerImage: ImageView? = null
    lateinit var  mListViewArray: ArrayList<NasTodayModel>
    var bannerImagePager: ImageView? = null
    var progressBarDialog: ProgressBarDialog? = null

    var bannerUrlImageArray: ArrayList<String>? = null
    var myFormatCalender = "yyyy-MM-dd"
    var sdfcalender: SimpleDateFormat? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_nas_today, container, false)

        mContext = activity
        myFormatCalender = "yyyy-MM-dd hh:mm:ss"
        sdfcalender = SimpleDateFormat(myFormatCalender, Locale.ENGLISH)
        initialiseUI()
        return mRootView

    }

    private fun initialiseUI() {
        progressBarDialog = ProgressBarDialog(mContext!!)

        mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
        mNasTodayListView = mRootView!!.findViewById<View>(R.id.mNasTodayListView) as ListView
        mTitleTextView!!.text = NaisClassNameConstants.NAS_TODAY
        bannerImagePager = mRootView!!.findViewById<View>(R.id.bannerImageViewPager) as ImageView
        relMain = mRootView!!.findViewById<View>(R.id.relMain) as RelativeLayout
        relMain!!.setOnClickListener(View.OnClickListener { })
        if (AppUtils.checkInternet(mContext!!)) {
            getList()
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity?,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
    }

    private fun getList() {
        progressBarDialog!!.show()

        //comment for conflict
        mListViewArray = ArrayList()
        val call: Call<NasTodayResponseModel> = ApiClient.getClient.nasTodayListCall("Bearer "+ PreferenceManager.getAccessToken(mContext))
        call.enqueue(object : Callback<NasTodayResponseModel> {
            override fun onResponse(call: Call<NasTodayResponseModel>, response: Response<NasTodayResponseModel>) {
                progressBarDialog!!.dismiss()

                val responseData = response.body()!!

                            if (responseData.responsecode.equals("200")) {

                                if (responseData.response.statuscode.equals("303")) {
                                    val bannerImage = responseData.response.banner_image
                                    if (!bannerImage.equals("", ignoreCase = true)) {
                                        Glide.with(mContext!!).load(AppUtils.replace(bannerImage))
                                            .centerCrop().into(
                                                bannerImagePager!!
                                            )

//											bannerUrlImageArray = new ArrayList<>();
//											bannerUrlImageArray.add(bannerImage);
//											bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray, getActivity()));
                                    } else {
                                        bannerImagePager!!.setBackgroundResource(R.drawable.nastodaybanner)
                                    }

                                    if (responseData.response.data.size > 0) {
                                        for (i in 0 until responseData.response.data.size) {

                                            var mNoticeDate: Date? = Date()
                                            val mDate: String? = responseData.response.data.get(i).date
                                            try {
                                                mNoticeDate = sdfcalender!!.parse(mDate)
                                            } catch (ex: ParseException) {
                                                Log.e("Date", "Parsing error")
                                            }
                                            val mDateTime: String? =responseData.response.data.get(i).date
                                            var mTime: Date? = Date()

                                            try {
                                                mTime = sdfcalender!!.parse(mDateTime)
                                                val format2 =
                                                    SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                                                val startTime = format2.format(mTime)
                                                responseData.response.data.get(i).time = startTime
                                            } catch (ex: ParseException) {
                                                Log.e("Date", "Parsing error")
                                            }
//			String dayOfTheWeek = (String) DateFormat.format("EEEE", mNoticeDate); // Thursday
                                            //			String dayOfTheWeek = (String) DateFormat.format("EEEE", mNoticeDate); // Thursday
                                            val day =
                                                DateFormat.format("dd", mNoticeDate) as String // 20

                                            val monthString = DateFormat.format(
                                                "MMM",
                                                mNoticeDate
                                            ) as String // Jun

//			String monthNumber  = (String) DateFormat.format("MM",   mNoticeDate); // 06
                                            //			String monthNumber  = (String) DateFormat.format("MM",   mNoticeDate); // 06
                                            val year = DateFormat.format(
                                                "yyyy",
                                                mNoticeDate
                                            ) as String // 2013

                                            responseData.response.data.get(i).day = day
                                            responseData.response.data.get(i).month = monthString.uppercase(Locale.getDefault())
                                            responseData.response.data.get(i).year = year
                                            mListViewArray!!.add((responseData.response.data.get(i)))
                                        }
                                        mNasTodayListView!!.adapter =
                                            NasTodayAdapter(mContext!!, mListViewArray)
                                    } else {
                                        //CustomStatusDialog();
                                        //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                        AppUtils.showDialogAlertDismiss(
                                            mContext as Activity?,
                                            "Alert",
                                            getString(R.string.nodatafound),
                                            R.drawable.exclamationicon,
                                            R.drawable.round
                                        )
                                    }
                                } else {
//										CustomStatusDialog(RESPONSE_FAILURE);
                                    //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                    AppUtils.showDialogAlertDismiss(
                                        mContext as Activity?,
                                        "Alert",
                                        getString(R.string.common_error),
                                        R.drawable.exclamationicon,
                                        R.drawable.round
                                    )
                                }
                            }else  {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                //Toast.makeText(mContext,"Failure", Toast.LENGTH_SHORT).show();
                                AppUtils.showDialogAlertDismiss(
                                    mContext as Activity?,
                                    "Alert",
                                    getString(R.string.common_error),
                                    R.drawable.exclamationicon,
                                    R.drawable.round
                                )
                            }
                         /*else {
//								CustomStatusDialog(RESPONSE_FAILURE);
                            //Toast.makeText(mContext,"Failure", Toast.LENGTH_SHORT).show();
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?,
                                "Alert",
                                getString(R.string.common_error),
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
                        }*/


            }

            override fun onFailure(call: Call<NasTodayResponseModel>, t: Throwable) {
                progressBarDialog!!.dismiss()

            }
        })
    }

    private fun getSearchValues(dataObject: JSONObject?): NasTodayModel {
        val model = NasTodayModel()
//        model.id = (dataObject!!.getString(JSONConstants.JTAG_ID))
//        model.image = (dataObject.getString(JSONConstants.JTAG_IMAGE))
//        model.title = (dataObject.getString(JSONConstants.JTAG_TITLE))
//        model.description = (dataObject.getString(JSONConstants.JTAG_DESCRIPTION))
//        model.date = (dataObject.getString(JSONConstants.JTAG_DATE))
//        model.pdf = (dataObject.getString(JSONConstants.JTAG_PDF))
//        var mNoticeDate: Date? = Date()
//        val mDate: String = dataObject.optString(JSONConstants.JTAG_DATE)
//        try {
//            mNoticeDate = sdfcalender!!.parse(mDate)
//        } catch (ex: ParseException) {
//
//        }
//        val mDateTime: String = dataObject.getString(JSONConstants.JTAG_DATE)
//        var mTime: Date? = Date()
//
//        try {
//            mTime = sdfcalender!!.parse(mDateTime)
//            val format2 = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
//            val startTime = format2.format(mTime)
//            model.time = (startTime)
//        } catch (ex: ParseException) {
//
//        }
//
//        val day = DateFormat.format("dd", mNoticeDate) as String
//
//        val monthString = DateFormat.format("MMM", mNoticeDate) as String
//
//        val year = DateFormat.format("yyyy", mNoticeDate) as String
//
//        model.day = (day)
//        model.month = (monthString.toUpperCase())
//        model.year = (year)
        return model
    }
}