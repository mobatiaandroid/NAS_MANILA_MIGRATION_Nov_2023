package com.mobatia.nasmanila.fragments.social_media

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.web_view.LoadUrlWebViewActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.DividerItemDecoration
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.social_media.adapter.SocialMediaAdapter
import com.mobatia.nasmanila.fragments.social_media.model.SocialmediaDataModel
import com.mobatia.nasmanila.fragments.social_media.model.SocialmediaResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SocialMediaFragment() : Fragment() {

    var mTitleTextView: TextView? = null
    var progressBarDialog: ProgressBarDialog? = null
    private var mRootView: View? = null
    private var mContext: Context? = null
    private val mAboutUsList: ListView? = null
    private val mTitle: String? = null
    private val mTabId: String? = null
    private val mProgressDialog: RelativeLayout? = null
    private val mBannerImage: ImageView? = null
    private var facebook: ImageView? = null
    private  var twitter: ImageView? = null
    private  var instagram: ImageView? = null
    var type: String? = null
    private val mSocialMediaArraylistFacebook = ArrayList<SocialmediaDataModel>()
    private val mSocialMediaArraylistTwitter = ArrayList<SocialmediaDataModel>()
    private val mSocialMediaArraylistInstagram = ArrayList<SocialmediaDataModel>()
    private val mSocialMediaArray = ArrayList<SocialmediaDataModel>()

    var bannerImagePager: ImageView? = null
    var bannerUrlImageArray: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_social_media, container,
            false
        )

        mContext = activity
        progressBarDialog = ProgressBarDialog(mContext!!)
        initialiseUI()
        if (AppUtils.checkInternet(mContext!!)) {
            callSocialMediaListAPI()
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

    private fun callSocialMediaListAPI() {
        progressBarDialog!!.show()
        val call: Call<SocialmediaResponseModel> = ApiClient.getClient.social_media("Bearer "+PreferenceManager.getAccessToken(mContext))
        call.enqueue(object : Callback<SocialmediaResponseModel> {
            override fun onResponse(call: Call<SocialmediaResponseModel>, response: Response<SocialmediaResponseModel>) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")){
                    progressBarDialog!!.dismiss()
                    var status_code=responseData!!.response.statuscode
                    if (status_code.equals("303")){
                        val bannerImage: String = responseData!!.response.banner_image
                        if (!bannerImage.equals("", ignoreCase = true)) {
                            Glide.with(mContext!!).load(bannerImage)
                                .into(bannerImagePager!!)
                        } else {
                            Glide.with(mContext!!).load(R.drawable.socialbanner)
                                .into(bannerImagePager!!)
                            //bannerImagePager!!.setBackgroundResource(R.drawable.socialbanner)
                        }
                        mSocialMediaArraylistInstagram.clear()
                        mSocialMediaArraylistFacebook.clear()
                        mSocialMediaArraylistTwitter.clear()
                        if (responseData!!.response.data.size>0) {
                            for (i in responseData!!.response.data.indices) {

                                // JSONObject dataObject = data.getJSONObject(i);
                                val dataObject: SocialmediaDataModel =
                                    responseData!!.response.data.get(i)
                                val socialMediaModel = SocialmediaDataModel()
                                socialMediaModel.id = dataObject.id
                                socialMediaModel.url = dataObject.url
                                socialMediaModel.tab_type=dataObject.tab_type
                                socialMediaModel.image = dataObject.image
                                if (dataObject.tab_type!!.contains("Facebook")) {
                                    mSocialMediaArraylistFacebook.add(socialMediaModel)
                                } else if (dataObject.tab_type!!.contains("Twitter")) {
                                    mSocialMediaArraylistTwitter.add(socialMediaModel)
                                    //mSocialMediaArray=mSocialMediaArraylistTwitter;
                                } else if (dataObject.tab_type!!.contains("Instagram")) {
                                    mSocialMediaArraylistInstagram.add(socialMediaModel)
                                }
                            }
                        }else{
                            Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show()
                        }

                    }
                    else if (status_code.equals("301")) {
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
            /*    if (responseData != null) {
                    val jsonObject = JSONObject(responseData.string())
                    if (jsonObject.has("status")) {
                        val status = jsonObject.optInt("status")
                        if (status == 100) {
                            val responseArray: JSONObject = jsonObject.optJSONObject("responseArray")
                            val banner: String = responseArray.optString("banner_images")
                            if (banner != "") {
                                Glide.with(mContext!!).load(AppUtils.replace(banner)).centerCrop()
                                    .into(mBannerImage!!)
                            } else {
                                mBannerImage!!.setBackgroundResource(R.drawable.default_bannerr)
                            }
                            val data: JSONArray = responseArray.getJSONArray("data")
                            mSocialMediaArraylistInstagram.clear()
                            mSocialMediaArraylistFacebook.clear()
                            mSocialMediaArraylistTwitter.clear()
                            if (data.length() > 0) {
                                for (i in 0 until data.length()) {
                                    val dataObject = data.getJSONObject(i)
                                    val socialMediaModel = SocialMediaModel()
                                    socialMediaModel.id = dataObject.optString("id")
                                    socialMediaModel.url = dataObject.optString("url")
                                    socialMediaModel.tabType = (dataObject.optString("tab_type"))
                                    socialMediaModel.image =
                                        dataObject.optString("image")
                                    if (dataObject.optString("tab_type")
                                            .contains("Facebook")
                                    ) {
                                        mSocialMediaArraylistFacebook.add(socialMediaModel)
                                    } else if (dataObject.optString("tab_type")
                                            .contains("Twitter")
                                    ) {
                                        mSocialMediaArraylistTwitter.add(socialMediaModel)
                                    } else if (dataObject.optString("tab_type")
                                            .contains("Instagram")
                                    ) {
                                        mSocialMediaArraylistInstagram.add(socialMediaModel)
                                    }
                                }
                            } else {
                                Toast.makeText(activity, "No data found", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?,
                                "Alert",
                                getString(R.string.common_error),
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
                        }
                    }
                }*/
            }

            override fun onFailure(call: Call<SocialmediaResponseModel>, t: Throwable) {

            }
        })
    }

    private fun initialiseUI() {
        mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
        mTitleTextView!!.text = NaisClassNameConstants.SOCIAL_MEDIA
        facebook = mRootView!!.findViewById<View>(R.id.facebookButton) as ImageView
        twitter = mRootView!!.findViewById<View>(R.id.twitterButton) as ImageView
        instagram = mRootView!!.findViewById<View>(R.id.instagramButton) as ImageView
        bannerImagePager = mRootView!!.findViewById<View>(R.id.bannerImageViewPager) as ImageView

        facebook!!.setOnClickListener {
            type = "facebook"
            if (mSocialMediaArraylistFacebook.size <= 0) {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    mContext!!.getString(R.string.alert_heading),
                    "Link not available.",
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            } else if (mSocialMediaArraylistFacebook.size == 1) {


                val facebookAppIntent: Intent
                try {
                    facebookAppIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("fb://page/${mSocialMediaArraylistFacebook[0].id}")
                    )
                    startActivity(facebookAppIntent)
                } catch (e: ActivityNotFoundException) {

                    val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                    intent.putExtra("url", mSocialMediaArraylistFacebook[0].url)
                    intent.putExtra("tab_type", "Facebook")
                    startActivity(intent)

                }


//                val intent = Intent(mContext, FullscreenWebViewActivityNoHeader::class.java)

            } else {
                showSocialMediaList(mSocialMediaArraylistFacebook, type)
            }
        }
        twitter!!.setOnClickListener {
            type = "twitter"
            if (mSocialMediaArraylistTwitter.size == 1) {
//                val intent = Intent(mContext, FullscreenWebViewActivityNoHeader::class.java)
                val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                intent.putExtra("url", mSocialMediaArraylistTwitter[0].url)
                intent.putExtra("tab_type", "Twitter")
                startActivity(intent)
            } else {
                showSocialMediaList(mSocialMediaArraylistTwitter, type)
            }

        }
        instagram!!.setOnClickListener {
            type = "instagram"
            if (mSocialMediaArraylistInstagram.size <= 0) {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    mContext!!.getString(R.string.alert_heading),
                    "Link not available.",
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            } else if (mSocialMediaArraylistInstagram.size == 1) {
//                val intent = Intent(mContext, FullscreenWebViewActivityNoHeader::class.java)
                val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                intent.putExtra("url", mSocialMediaArraylistInstagram[0].url)
                intent.putExtra("tab_type", "Instagram")
                startActivity(intent)
            } else {
                showSocialMediaList(mSocialMediaArraylistInstagram, type)
            }
        }
    }

    private fun showSocialMediaList(mSocialMediaArray: ArrayList<SocialmediaDataModel>, type: String?) {
        val dialog = Dialog(mContext!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_social_media_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<View>(R.id.btn_dismiss) as Button
        val iconImageView = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        val socialMediaList = dialog.findViewById<View>(R.id.recycler_view_social_media) as RecyclerView

        if (type == "facebook") {
            iconImageView.setImageResource(R.drawable.facebookiconmedia)
            val sdk = Build.VERSION.SDK_INT
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                iconImageView.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.roundfb))
                dialogDismiss.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.buttonfb))
            } else {
                iconImageView.background = mContext!!.resources.getDrawable(R.drawable.roundfb)
                dialogDismiss.background = mContext!!.resources.getDrawable(R.drawable.buttonfb)
            }
        } else if (type == "twitter") {
            iconImageView.setImageResource(R.drawable.twittericon)
            val sdk = Build.VERSION.SDK_INT
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                iconImageView.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.roundtw))
                dialogDismiss.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.buttontwi))
            } else {
                iconImageView.background = mContext!!.resources.getDrawable(R.drawable.roundtw)
                dialogDismiss.background = mContext!!.resources.getDrawable(R.drawable.buttontwi)
            }
        } else {
            iconImageView.setImageResource(R.drawable.instagramicon)
            val sdk = Build.VERSION.SDK_INT
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                iconImageView.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.roundins))
                dialogDismiss.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.buttonins))
            } else {
                iconImageView.background = mContext!!.resources.getDrawable(R.drawable.roundins)
                dialogDismiss.background = mContext!!.resources.getDrawable(R.drawable.buttonins)
            }
        }
        socialMediaList.addItemDecoration(DividerItemDecoration(mContext!!.resources.getDrawable(R.drawable.list_divider)))

        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm

        val socialMediaAdapter = SocialMediaAdapter(mContext!!, mSocialMediaArray)
        socialMediaList.adapter = socialMediaAdapter
        dialogDismiss.setOnClickListener {
            dialog.dismiss()
        }
        socialMediaList.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (type == "facebook") {
                    val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
//                    val intent = Intent(mContext, FullscreenWebViewActivityNoHeader::class.java)
                    intent.putExtra("url", mSocialMediaArraylistFacebook[position].url)
                    intent.putExtra("tab_type", mSocialMediaArraylistFacebook[position].tab_type)
                    startActivity(intent)
                } else if (type == "twitter") {
                    val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
//                    val intent = Intent(mContext, FullscreenWebViewActivityNoHeader::class.java)
                    intent.putExtra("url", mSocialMediaArraylistTwitter[position].url)
                    intent.putExtra("tab_type", mSocialMediaArraylistTwitter[position].tab_type)
                    startActivity(intent)
                } else if (type == "instagram") {
                    val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
//                    val intent = Intent(mContext, FullscreenWebViewActivityNoHeader::class.java)
                    intent.putExtra("url", mSocialMediaArraylistInstagram[position].url)
                    intent.putExtra("tab_type", mSocialMediaArraylistInstagram[position].tab_type)
                    startActivity(intent)
                }
            }

        })
        dialog.show()
    }



}