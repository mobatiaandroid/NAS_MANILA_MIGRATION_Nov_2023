package com.mobatia.nasmanila.activities.contact_us

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.contact_us.adapter.CustomStaffDirectoryAdapter
import com.mobatia.nasmanila.activities.contact_us.model.StaffCategoryResponseModel
import com.mobatia.nasmanila.activities.contact_us.model.StaffModel
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.DividerItemDecoration
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StaffDirectoryActivity :AppCompatActivity(){
lateinit var mContext:Context
    var progressBarDialog: ProgressBarDialog? = null
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null
    var mStaffDirectoryListArray: ArrayList<StaffModel> = ArrayList<StaffModel>()

    //private ListView mStaffDirectoryList;
    var mStaffDirectoryListView: RecyclerView? = null
    var bannerUrlImageArray: ArrayList<String>? = null
    var bannerImagePager: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staffdirectory_list)
      mContext=this
        initialiseUI()
        if (AppUtils.isNetworkConnected(mContext)) {
            callStaffDirectoryListAPI()
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }

    }

    private fun callStaffDirectoryListAPI() {
        val call: Call<StaffCategoryResponseModel> = ApiClient.getClient.getstaffcategorylist("Bearer "+ PreferenceManager.getAccessToken(mContext))
        progressBarDialog!!.show()
        call.enqueue(object : Callback<StaffCategoryResponseModel> {
            override fun onResponse(call: Call<StaffCategoryResponseModel>, response: Response<StaffCategoryResponseModel>) {
                progressBarDialog!!.dismiss()
                val responseData = response.body()
                if (response.body()!!.responsecode.equals("200")){
                    progressBarDialog!!.dismiss()
                    var status_code=response.body()!!.response.statuscode
                    if (status_code.equals("303")){
                        val bannerImage: String = response.body()!!.response.banner_image

                        if (!bannerImage.equals("")) {
                            Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop()
                                .into(bannerImagePager!!)


                        } else {
                            bannerImagePager!!.setBackgroundResource(R.drawable.staffdirectory)
                        }
                        if (response.body()!!.response.data.size > 0) {
                            mStaffDirectoryListArray.addAll(response.body()!!.response.data)
                           /* for (i in 0 until response.body()!!.response.data.size) {
                                val item: StaffModel = response.body()!!.response.data[i]
                                val gson = Gson()
                                val eventJson = gson.toJson(item)
                                try {
                                    val jsonObject = JSONObject(eventJson)
                                    mStaffDirectoryListArray.add(addStaffDetails(jsonObject))
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }*/
                            val customStaffDirectoryAdapter =
                                CustomStaffDirectoryAdapter(mContext, mStaffDirectoryListArray)
                            mStaffDirectoryListView!!.adapter = customStaffDirectoryAdapter
                        } else {
                            Toast.makeText(
                                this@StaffDirectoryActivity,
                                "No data found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else if (status_code.equals("301")) {
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

                } else {
                    progressBarDialog!!.dismiss()
                    AppUtils.showDialogAlertDismiss(
                        mContext,
                        "Alert",
                        mContext!!.getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<StaffCategoryResponseModel>, t: Throwable) {
                progressBarDialog!!.dismiss()            }
        })
    }

    private fun initialiseUI() {
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        progressBarDialog = ProgressBarDialog(mContext!!)
//		bannerImagePager= (ViewPager) findViewById(R.id.bannerImageViewPager);
        //		bannerImagePager= (ViewPager) findViewById(R.id.bannerImageViewPager);
        bannerImagePager = findViewById<View>(R.id.bannerImageViewPager) as ImageView
        mStaffDirectoryListView = findViewById<View>(R.id.mStaffDirectoryListView) as RecyclerView
        mStaffDirectoryListView!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.list_divider)))
        mStaffDirectoryListView!!.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        mStaffDirectoryListView!!.layoutManager = llm
        headermanager = HeaderManager(this@StaffDirectoryActivity, "Staff Directory")
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
        mStaffDirectoryListView!!.addOnItemClickListener(object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                val intent = Intent(this@StaffDirectoryActivity, StaffListActivity::class.java)
                intent.putExtra(
                    "category_id",
                    mStaffDirectoryListArray[position].id

                )
                intent.putExtra("title", "Staff Directory")
                startActivity(intent)
            }

        })

    }

}
