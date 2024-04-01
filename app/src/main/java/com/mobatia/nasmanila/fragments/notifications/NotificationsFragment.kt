package com.mobatia.nasmanila.fragments.notifications

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.notification.AudioAlertActivity
import com.mobatia.nasmanila.activities.notification.ImageAlertActivity
import com.mobatia.nasmanila.activities.notification.TextAlertActivity
import com.mobatia.nasmanila.activities.notification.VideoAlertActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.DividerItemDecoration
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.home.progressBarDialog
import com.mobatia.nasmanila.fragments.notifications.adapter.PushNotificationListAdapter
import com.mobatia.nasmanila.fragments.notifications.model.NotificationsApiModel
import com.mobatia.nasmanila.fragments.notifications.model.NotificationsClearResponseModel
import com.mobatia.nasmanila.fragments.notifications.model.NotificationsListModel
import com.mobatia.nasmanila.fragments.notifications.model.NotificationsResponseModel
import com.mobatia.nasmanila.fragments.notifications.model.PushNotificationModel
import com.mobatia.nasmanila.manager.recyclermanager.ItemOffsetDecoration
import me.leolin.shortcutbadger.ShortcutBadger
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationsFragment() : Fragment() {

    lateinit var mRootView: View
    lateinit var mContext: Context
    lateinit var notificationRecycler: RecyclerView
    lateinit var swipeNotification: SwipeRefreshLayout

    private lateinit var constraintMain: ConstraintLayout
    private lateinit var mBannerImage: ImageView
    lateinit var progressBar: ProgressBar
    lateinit var mTitleTextView: TextView
    lateinit var pushNotificationArrayList: ArrayList<NotificationsListModel>
    lateinit var mPushNotificationListAdapter: PushNotificationListAdapter
    var mIntent: Intent? = null
    var myFormatCalender = "yyyy-MM-dd HH:mm:ss"
    var sdfcalendar: SimpleDateFormat? = null
    var isFromBottom = false
    var swipeRefresh = false
    var pageFrom = ""
    var scrollTo = ""
    var notificationSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_notifications, container,
            false
        )

        mContext = requireActivity()
        myFormatCalender = "yyyy-MM-dd HH:mm:ss"
        sdfcalendar = SimpleDateFormat(myFormatCalender, Locale.ENGLISH)
        ShortcutBadger.applyCount(mContext, 0) //badge

        initialiseUI()
        if (AppUtils.checkInternet(mContext!!)) {
           clearBadge()
            val isFromBottom = false
            callPushNotification(
                pageFrom,
                scrollTo
            )
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity?,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
        return mRootView
    }

    private fun clearBadge()
    {
        val call: Call<NotificationsClearResponseModel> = ApiClient.getClient.clear_badge(
            "Bearer " + PreferenceManager.getAccessToken(mContext))
        progressBarDialog!!.show()
        call.enqueue(object : Callback<NotificationsClearResponseModel> {
            override fun onResponse(call: Call<NotificationsClearResponseModel>, response: Response<NotificationsClearResponseModel>) {


                if (response.body()!!.responsecode.equals("200")){
                    progressBarDialog!!.dismiss()
                    var status_code=response.body()!!.response.statuscode
                    if (status_code.equals("303")){


                        } else {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                }else {
                    progressBarDialog!!.dismiss()
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        getString(R.string.error_heading),
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }

            }

            override fun onFailure(call: Call<NotificationsClearResponseModel>, t: Throwable) {
                progressBarDialog!!.dismiss()
            }

        })
    }

    private fun callPushNotification(
        page_from: String,
        scroll_to: String
    ) {
        var jtagDevicetype = "2"
        notificationSize = 0
        var notifbody = NotificationsApiModel(
            PreferenceManager.getFCMID(mContext),
            jtagDevicetype,
            page_from,
            scroll_to
        )
        val call: Call<NotificationsResponseModel> = ApiClient.getClient.notifications(
            "Bearer " + PreferenceManager.getAccessToken(mContext), notifbody
        )
        progressBarDialog!!.show()
        call.enqueue(object : Callback<NotificationsResponseModel> {
            override fun onResponse(call: Call<NotificationsResponseModel>, response: Response<NotificationsResponseModel>) {


                if (response.body()!!.responsecode.equals("200")){
                    progressBarDialog!!.dismiss()
                    var status_code=response.body()!!.response.statuscode
                    if (status_code.equals("303")){
                        if (response.body()!!.response.data.size > 0) {
                            notificationSize = response.body()!!.response.data.size
                            pushNotificationArrayList.addAll(response.body()!!.response.data)
                            /* for (i in response.body()!!.response.data.indices) {

                                 val item: NotificationsListModel =
                                     response.body()!!.response.data[i]
                                 val gson = Gson()
                                 val eventJson = gson.toJson(item)
                                 try {
                                     val jsonObject = JSONObject(eventJson)
                                     pushNotificationArrayList.add(getSearchValues(jsonObject))
                                 } catch (e: JSONException) {
                                     e.printStackTrace()
                                 }


                             }*/
                            mPushNotificationListAdapter =
                                PushNotificationListAdapter(mContext, pushNotificationArrayList)
                            notificationRecycler.setAdapter(mPushNotificationListAdapter)
                        } else {
                        }
                    }else if (status_code.equals("301")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext,
                            getString(R.string.error_heading),
                            getString(R.string.missing_parameter),
                            R.drawable.infoicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("304")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.email_exists),
                            R.drawable.infoicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("305")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.incrct_usernamepswd),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("306")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.invalid_email),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                }else {
                    progressBarDialog!!.dismiss()
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        getString(R.string.error_heading),
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }

            }

            override fun onFailure(call: Call<NotificationsResponseModel>, t: Throwable) {
                progressBarDialog!!.dismiss()
            }

        })
        notificationRecycler!!.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                if (pushNotificationArrayList!![position].type.equals(
                        "Text"
                    )
                ) {
                    mIntent = Intent(context, TextAlertActivity::class.java)
                    mIntent!!.putExtra("position", position)
                    mIntent!!.putExtra("message", pushNotificationArrayList!![position].message)
                    mIntent!!.putExtra("url", pushNotificationArrayList!![position].url)
                    mIntent!!.putExtra("date", pushNotificationArrayList!![position].date)
                    mIntent!!.putExtra("pushfrom", pushNotificationArrayList!![position].push_from)
                    mIntent!!.putExtra("type", pushNotificationArrayList!![position].type)


                    context!!.startActivity(mIntent)
                }
                if (pushNotificationArrayList!![position].type.equals("Image"))  {
                    mIntent = Intent(context, ImageAlertActivity::class.java)
                    mIntent!!.putExtra("position", position)
                    mIntent!!.putExtra("message", pushNotificationArrayList!![position].message)
                    mIntent!!.putExtra("url", pushNotificationArrayList!![position].url)
                    mIntent!!.putExtra("date", pushNotificationArrayList!![position].date)
                    mIntent!!.putExtra("pushfrom", pushNotificationArrayList!![position].push_from)
                    mIntent!!.putExtra("type", pushNotificationArrayList!![position].type)
                    context!!.startActivity(mIntent)
                }
                if (pushNotificationArrayList!![position].type.equals(
                        "Audio"
                    )
                ) {
                    mIntent = Intent(context, AudioAlertActivity::class.java)
                    mIntent!!.putExtra("position", position)
                    mIntent!!.putExtra("message", pushNotificationArrayList!![position].message)
                    mIntent!!.putExtra("url", pushNotificationArrayList!![position].url)
                    mIntent!!.putExtra("date", pushNotificationArrayList!![position].date)
                    mIntent!!.putExtra("pushfrom", pushNotificationArrayList!![position].push_from)
                    mIntent!!.putExtra("type", pushNotificationArrayList!![position].type)
                    context!!.startActivity(mIntent)
                }
                if (pushNotificationArrayList!![position].type.equals(
                        "Video"
                    )
                ) {
                    mIntent = Intent(context, VideoAlertActivity::class.java)
                    mIntent!!.putExtra("position", position)
                    mIntent!!.putExtra("message", pushNotificationArrayList!![position].message)
                    mIntent!!.putExtra("url", pushNotificationArrayList!![position].url)
                    mIntent!!.putExtra("date", pushNotificationArrayList!![position].date)
                    mIntent!!.putExtra("pushfrom", pushNotificationArrayList!![position].push_from)
                    mIntent!!.putExtra("type", pushNotificationArrayList!![position].type)
                    context!!.startActivity(mIntent)
                }
            }

        })
    }

    private fun getSearchValues(dataObject: JSONObject?): PushNotificationModel {
        val mPushNotificationModel = PushNotificationModel()
        mPushNotificationModel.pushType = dataObject!!.optString("alert_type")
        mPushNotificationModel.id = dataObject.optString("id")
        mPushNotificationModel.headTitle = dataObject.optString("title")
        mPushNotificationModel.pushDate = dataObject.optString("time_Stamp")
        val mDate: String? = mPushNotificationModel.pushDate
        var mEventDate = Date()
        mEventDate = sdfcalendar!!.parse(mDate)
        val format2 = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        val startTime = format2.format(mEventDate)
        mPushNotificationModel.pushTime = startTime
        val dayOfTheWeek = DateFormat.format("EEEE", mEventDate) as String // Thursday
        val day = DateFormat.format("dd", mEventDate) as String // 20
        val monthString = DateFormat.format("MMM", mEventDate) as String // June
        val monthNumber = DateFormat.format("MM", mEventDate) as String // 06
        val year = DateFormat.format("yyyy", mEventDate) as String // 2013
        mPushNotificationModel.dayOfTheWeek = dayOfTheWeek
        mPushNotificationModel.day = day
        mPushNotificationModel.monthString = monthString
        mPushNotificationModel.monthNumber = monthNumber
        mPushNotificationModel.year = year
        return mPushNotificationModel
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initialiseUI() {
        mTitleTextView = mRootView.findViewById<View>(R.id.titleTextView) as TextView
        mTitleTextView.text = NaisClassNameConstants.NOTIFICATIONS
        isFromBottom = false
        swipeRefresh = false
        pushNotificationArrayList = ArrayList()
        notificationRecycler = mRootView.findViewById(R.id.notification_recycler)
//        swipeNotification = mRootView.findViewById(R.id.swipe_notification)
        constraintMain = mRootView.findViewById<View>(R.id.relMain) as ConstraintLayout
        progressBar = mRootView.findViewById(R.id.progressBar)
        constraintMain.setOnClickListener {  }
        notificationRecycler.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(mContext)
        val spacing = 10
        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
        notificationRecycler.addItemDecoration(
            DividerItemDecoration(mContext.resources.getDrawable(R.drawable.list_divider))
        )
        notificationRecycler.addItemDecoration(itemDecoration)

        notificationRecycler.layoutManager = linearLayoutManager

    }


}