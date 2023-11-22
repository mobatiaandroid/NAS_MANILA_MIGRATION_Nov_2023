package com.mobatia.nasmanila.fragments.parent_essentials

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.parent_essential.ParentEssentialActivity
import com.mobatia.nasmanila.activities.parent_essential.ParentEssentialActivityNew
import com.mobatia.nasmanila.activities.web_view.LoadUrlWebViewActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.*
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.parent_essentials.adapter.ParentEssentialsListAdapter
import com.mobatia.nasmanila.fragments.parent_essentials.model.ParentEssentialsModel
import com.mobatia.nasmanila.fragments.parent_essentials.model.ParentessentialsResponseModel
import com.mobatia.nasmanila.fragments.parent_essentials.model.SubmenuParentEssentials
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ParentEssentialsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParentEssentialsFragment() : Fragment() {

    var mTitleTextView: TextView? = null
    private var mRootView: View? = null
    private var mContext: Context? = null
    private var mListView: RecyclerView? = null
    var progressBarDialog: ProgressBarDialog? = null
    var bannerImagePager: ImageView? = null
    private val mBannerImage: ImageView? = null
    var bannerUrlImageArray: ArrayList<String>? = null
    private val newsLetterModelArrayList = ArrayList<ParentEssentialsModel>()
    var relMain: RelativeLayout? = null
    var mtitle: RelativeLayout? = null
    var text_content: TextView? = null
    var text_dialog: TextView? = null
    var description = ""
    var contactEmail = ""
    var descriptionTV: TextView? = null
    var descriptionTitle: TextView? = null
    var sendEmail: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mRootView =
            inflater.inflate(
                R.layout.fragment_parent_essentials, container,
                false
            )
        mContext = activity
        progressBarDialog = ProgressBarDialog(mContext!!)
        initialiseUI()
        return mRootView
        return inflater.inflate(R.layout.fragment_parent_essentials, container, false)
    }

    private fun initialiseUI() {
        mtitle = mRootView!!.findViewById<View>(R.id.title) as RelativeLayout
        descriptionTV = mRootView!!.findViewById<View>(R.id.descriptionTV) as TextView
        descriptionTitle = mRootView!!.findViewById<View>(R.id.descriptionTitle) as TextView
        sendEmail = mRootView!!.findViewById<View>(R.id.sendEmail) as ImageView
        mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
        mTitleTextView!!.text = NaisClassNameConstants.PARENT_ESSENTIALS
        mListView = mRootView!!.findViewById<View>(R.id.mListView) as RecyclerView
        mListView!!.isNestedScrollingEnabled = false
        bannerImagePager = mRootView!!.findViewById<View>(R.id.bannerImagePager) as ImageView
        relMain = mRootView!!.findViewById<View>(R.id.relMain) as RelativeLayout
        relMain!!.setOnClickListener {

        }
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        mListView!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.list_divider)))

        mListView!!.layoutManager = llm
        if (AppUtils.checkInternet(mContext!!)) {
            getNewslettercategory()
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity?,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
        mListView!!.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (newsLetterModelArrayList[position].name.equals(
                        "Bus Service"
                    ) || newsLetterModelArrayList[position].name.equals(
                        "Lunch Menu"
                    )
                ) {
                    Log.e("view","new")
                    val intent = Intent(mContext, ParentEssentialActivityNew::class.java)
                    PreferenceManager.setparentEssentials(
                        newsLetterModelArrayList[position].submenu,
                        mContext!!
                    )

                   /* intent.putExtra(
                        "submenuArray",
                        newsLetterModelArrayList[position].submenu
                    )*/
                    intent.putExtra("tab_type", NaisClassNameConstants.PARENT_ESSENTIALS)
                    intent.putExtra("tab_typeName", newsLetterModelArrayList[position].name)
                    intent.putExtra("bannerImage", newsLetterModelArrayList[position].banner_image)
                    intent.putExtra("contactEmail", newsLetterModelArrayList[position].contact_email)
                    intent.putExtra("description", newsLetterModelArrayList[position].description)
                    mContext!!.startActivity(intent)
                } else if (newsLetterModelArrayList[position].name.equals(
                        "Parent Portal",
                        ignoreCase = true
                    )
                ) {
                    Log.e("view","web")
                    val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                    intent.putExtra("tab_type", NaisClassNameConstants.PARENT_ESSENTIALS)
                    intent.putExtra("url", newsLetterModelArrayList[position].link)
                    (mContext as Activity).startActivity(intent)
                } else {
                    Log.e("view","act")
                    val intent = Intent(mContext, ParentEssentialActivity::class.java)
                    PreferenceManager.setparentEssentials(
                        newsLetterModelArrayList[position].submenu,
                        mContext!!
                    )
                   /* intent.putExtra(
                        "submenuArray",
                        newsLetterModelArrayList[position].submenu
                    )*/
                    intent.putExtra("tab_type", NaisClassNameConstants.PARENT_ESSENTIALS)
                    intent.putExtra("tab_typeName", newsLetterModelArrayList[position].name)
                    intent.putExtra("bannerImage", newsLetterModelArrayList[position].banner_image)
                    (mContext as Activity).startActivity(intent)
                }
            }

        })
        sendEmail!!.setOnClickListener {
            if (!PreferenceManager.getUserID(mContext as Activity).equals("", ignoreCase = true)) {
                val dialog = Dialog(mContext as Activity)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.alert_send_email_dialog)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val dialogCancelButton = dialog.findViewById<View>(R.id.cancelButton) as Button
                val submitButton = dialog.findViewById<View>(R.id.submitButton) as Button
                text_dialog = dialog.findViewById<View>(R.id.text_dialog) as EditText
                text_content = dialog.findViewById<View>(R.id.text_content) as EditText
                (text_dialog as EditText).setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        (text_dialog as EditText).hint = ""
                        (text_dialog as EditText).gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                        (text_dialog as EditText).setPadding(5, 5, 0, 0)
                    } else {
                        (text_dialog as EditText).hint = "Enter your subject here..."
                        (text_dialog as EditText).gravity = Gravity.CENTER
                    }
                })
                (text_content as EditText).onFocusChangeListener =
                    View.OnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            (text_content as EditText).gravity = Gravity.LEFT
                        } else {
                            (text_content as EditText).gravity = Gravity.CENTER
                        }
                    }
                dialogCancelButton.setOnClickListener { dialog.dismiss() }
                submitButton.setOnClickListener {
                    if ((text_dialog as EditText).text.equals("")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            (mContext as Activity).getString(R.string.alert_heading),
                            "Please enter  subject",
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else if ((text_content as EditText).text.toString() == "") {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            (mContext as Activity).getString(R.string.alert_heading),
                            "Please enter  content",
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else {
                        if (AppUtils.checkInternet(mContext as Activity)) {
                            sendEmailToStaff()
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?,
                                "Network Error",
                                (mContext as Activity).getString(R.string.no_internet),
                                R.drawable.nonetworkicon,
                                R.drawable.roundred
                            )
                        }
                    }
                }
                dialog.show()
            } else {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    (mContext as Activity).getString(R.string.alert_heading),
                    "This feature is available only for registered users. Login/register to see contents.",
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        }
    }

    private fun getNewslettercategory() {
        progressBarDialog!!.show()
        val call: Call<ParentessentialsResponseModel> = ApiClient.getClient.parentessentials("Bearer "+PreferenceManager.getAccessToken(mContext))
        call.enqueue(object : Callback<ParentessentialsResponseModel> {
            override fun onResponse(call: Call<ParentessentialsResponseModel>, response: Response<ParentessentialsResponseModel>) {
                if (response.body()!!.responsecode.equals("200")){
                    progressBarDialog!!.dismiss()
                    var status_code=response.body()!!.response.statuscode
                    if (status_code.equals("303")){

                        val bannerImage: String = response.body()!!.response.banner_image
                        description = response.body()!!.response.description
                        contactEmail = response.body()!!.response.contact_email
                        if (!bannerImage.equals("")) {
                            Log.e("banner","banner")
                            Glide.with(mContext!!).load(bannerImage).centerCrop().into(bannerImagePager!!)
                           /* Glide.with(mContext!!).load(AppUtils.replace(bannerImage)).centerCrop()
                                .placeholder(R.drawable.default_bannerr)
                                .error(R.drawable.default_bannerr).into(
                                    bannerImagePager!!
                                )*/
                } else {
                            Log.e("banner","default")
                            Glide.with(mContext!!).load(R.drawable.default_bannerr).centerCrop().into(bannerImagePager!!)
                            //bannerImagePager!!.setImageResource(R.drawable.default_bannerr)
                        }
                        if (description.equals("") && contactEmail.equals(
                                ""
                            )
                        ) {
                            mtitle!!.visibility = View.GONE
                        } else {
                            mtitle!!.visibility = View.VISIBLE
                        }
                        if (description.equals("")) {
                            descriptionTV!!.visibility = View.GONE
                            descriptionTitle!!.visibility = View.GONE
                        } else {
                            descriptionTV!!.text = description
                            descriptionTitle!!.visibility = View.GONE
                            //			descriptionTitle.setVisibility(View.VISIBLE);
                            descriptionTV!!.visibility = View.VISIBLE
                            mtitle!!.visibility = View.VISIBLE
                        }
                        if (contactEmail.equals("")) {
                            sendEmail!!.visibility = View.GONE
                        } else {
                            sendEmail!!.visibility = View.VISIBLE
                            mtitle!!.visibility = View.VISIBLE
                        }

                        if (response.body()!!.response.data.size > 0) {
                            for (i in response.body()!!.response.data.indices) {
                                val item: ParentEssentialsModel =
                                    response.body()!!.response.data.get(i)
                                val gson = Gson()
                                val eventJson = gson.toJson(item)
                                try {
                                    val jsonObject = JSONObject(eventJson)
                                    Log.e("json", jsonObject.toString())
                                    newsLetterModelArrayList.add(addNewsLetterDetails(jsonObject))
                                    Log.e("Parentessentialsq", newsLetterModelArrayList.toString())
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                            val newsLetterAdapter =
                                ParentEssentialsListAdapter(mContext, newsLetterModelArrayList)
                            mListView!!.adapter = newsLetterAdapter
                        } else {
                            Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show()
                        }
                    }else if (status_code.equals("301")) {
                        AppUtils.showDialogAlertDismiss(
                            context,
                            getString(R.string.error_heading),
                            getString(R.string.missing_parameter),
                            R.drawable.infoicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("304")) {
                        AppUtils.showDialogAlertDismiss(
                            context as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.email_exists),
                            R.drawable.infoicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("305")) {
                        AppUtils.showDialogAlertDismiss(
                            context as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.incrct_usernamepswd),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("306")) {
                        AppUtils.showDialogAlertDismiss(
                            context as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.invalid_email),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else {
                        AppUtils.showDialogAlertDismiss(
                            context as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }

                }else {
                    progressBarDialog!!.dismiss()
                    AppUtils.showDialogAlertDismiss(
                        context as Activity?,
                        getString(R.string.error_heading),
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }


            }

            override fun onFailure(call: Call<ParentessentialsResponseModel>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addNewsLetterDetails(obj: JSONObject?): ParentEssentialsModel {
        val model = ParentEssentialsModel()
        try {
            model.name= obj!!.optString("name")
            if (obj.has("banner_image")) {
                model.banner_image=obj.optString("banner_image")
            } else {
                model.banner_image=""
            }
            if (obj.has("description")) {
                model.description = obj.optString("description")
            } else {
                model.description = ""
            }
            if (obj.has("contact_email")) {
                model.contact_email=obj.optString("contact_email")
            } else {
                model.contact_email=""
            }
            if (obj.has("link")) {
                model.link = obj.optString("link")
            } else {
                model.link = ""
            }
            val submenuArray: JSONArray = obj.optJSONArray("submenu")
            Log.e("submenu", submenuArray.toString())
            val subMenNewsLetterModels: ArrayList<SubmenuParentEssentials> =
                ArrayList<SubmenuParentEssentials>()
            for (i in 0 until submenuArray.length()) {
                val subObj = submenuArray.getJSONObject(i)
                val newsModel = SubmenuParentEssentials()
                //				newsModel.setNewLetterSubId(subObj.optString("id"));
                newsModel.filename=subObj.optString("filename")
                newsModel.submenu=subObj.optString("submenu")

                subMenNewsLetterModels.add(newsModel)
                Log.e("sub", subMenNewsLetterModels.toString())
            }
            model.submenu=subMenNewsLetterModels
        } catch (ex: Exception) {
            println("Exception is$ex")
        }

        return model
    }

    private fun sendEmailToStaff() {
        val call: Call<ResponseBody> = ApiClient.getClient.sendEmailToStaffCall(
            contactEmail!!,
            text_dialog!!.text.toString(),
            text_content!!.text.toString()
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseData = response.body()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData.string())
                    if (jsonObject.has("status")) {
                        val status = jsonObject.optInt("status")
                        if (status == 100) {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?,
                                "Success",
                                "Successfully sent email to staff",
                                R.drawable.tick,
                                R.drawable.round
                            )
                        } else {
                            val toast = Toast.makeText(mContext, "Email not sent", Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                } else {
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        "Alert",
                        (mContext as Activity).getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}