package com.mobatia.nasmanila.activities.contact_us

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.contact_us.adapter.CustomStaffDeptRecyclerAdapter
import com.mobatia.nasmanila.activities.contact_us.adapter.StaffAdapterAdapterNew
import com.mobatia.nasmanila.activities.contact_us.model.Categori
import com.mobatia.nasmanila.activities.contact_us.model.StaffCDeptApiModel
import com.mobatia.nasmanila.activities.contact_us.model.StaffCDeptResponseModel
import com.mobatia.nasmanila.activities.contact_us.model.StaffModel
import com.mobatia.nasmanila.activities.contact_us.model.StaffModelDept
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StaffListActivity:AppCompatActivity() {
    lateinit var mContext: Context
    var progressBarDialog: ProgressBarDialog? = null
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null

    //ListView mStaffListView;
    private var mStaffListView: RecyclerView? = null

    var extras: Bundle? = null
    var category_id: String? = null
    var title:kotlin.String? = null
    lateinit var mStaffDeptList: ArrayList<StaffModelDept>
    var list: ArrayList<Map<String, ArrayList<StaffModel>>> =
        ArrayList<Map<String, ArrayList<StaffModel>>>()
    var hashmap: HashMap<String, ArrayList<StaffModelDept>> = HashMap<String, ArrayList<StaffModelDept>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_list)
        mContext = this
        progressBarDialog = ProgressBarDialog(mContext!!)
        initialiseUI()
        if (AppUtils.isNetworkConnected(mContext)) {
            callStaffListAPI()
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

    private fun callStaffListAPI() {
        var staffdept=StaffCDeptApiModel(category_id.toString())

        val call: Call<StaffCDeptResponseModel> = ApiClient.getClient.getstaffdeptlist("Bearer "+ PreferenceManager.getAccessToken(mContext),
            staffdept)
        progressBarDialog!!.show()
        call.enqueue(object : Callback<StaffCDeptResponseModel> {
            override fun onResponse(call: Call<StaffCDeptResponseModel>, response: Response<StaffCDeptResponseModel>) {
                progressBarDialog!!.dismiss()
                val responseData = response.body()
                if (response.body()!!.responsecode.equals("200")){
                    progressBarDialog!!.dismiss()
                    var status_code=response.body()!!.response.statuscode
                    if (status_code.equals("303")){
                        val deptArrayList = ArrayList<String>()
                        if (response.body()!!.response.data.departments.size > 0) {
                            var depObject = ""
                            for (i in 0 until response.body()!!.response.data.departments
                                .size) {
                                depObject =
                                    response.body()!!.response.data.departments.get(i)
                                deptArrayList.add(depObject)
                            }
                            for (j in deptArrayList.indices) {
                                val listt: ArrayList<Categori> =
                                    ArrayList<Categori>()


                                // System.out.println("staffdept----name--1--" + j + "---" + keyArray);
                                mStaffDeptList = ArrayList()
                                for (departmentMap in response.body()!!.response.data.staffs) {

                                    for ((key, value)in departmentMap) {
                                        val directory = Categori(key, value)
                                        listt.add(directory)

                                    }
                                }
                                val customStaffDeptAdapter = StaffAdapterAdapterNew(mContext, listt)
                                mStaffListView!!.adapter = customStaffDeptAdapter
                             hashmap[deptArrayList[j]] = mStaffDeptList
                            }
                            println("hashmap size--" + hashmap.size + "--" + mStaffDeptList!!.size)

                            /* StaffAdapterAdapterNew customStaffDeptAdapter = new StaffAdapterAdapterNew(mContext, list);
                                mStaffListView.setAdapter(customStaffDeptAdapter);*/if (hashmap.size == 1 && mStaffDeptList!!.size == 0) {
                                AppUtils.showDialogAlertFinish(
                                    mContext as Activity,
                                    mContext.getString(R.string.alert_heading),
                                    mContext.getString(R.string.no_details_available),
                                    R.drawable.exclamationicon,
                                    R.drawable.round
                                )
                            } else {
                                /*StaffAdapterAdapterNew customStaffDeptAdapter = new StaffAdapterAdapterNew(mContext, deptArrayList, hashmap);
                                    mStaffListView.setAdapter(customStaffDeptAdapter);*/
                            }
                        } else {
                            mStaffDeptList = java.util.ArrayList()
                            for (j in 0 until response.body()!!.response.data.staffs
                                .size) {
                                //  JSONObject staffObject = staffArray.getJSONObject(j);
                                /*  StaffModel item = apiResponse.getResponse().getData().getStaffs().get(j);
                                    Gson gson = new Gson();
                                    String eventJson = gson.toJson(item);
                                    try {
                                        JSONObject jsonObject = new JSONObject(eventJson);

                                        mStaffDeptList.add(addStaffDeptDetails(jsonObject));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }*/

                                //System.out.println("staffdept----name--"+currentKey);
                            }
                            println("staffdept----name--" + mStaffDeptList!!.size)
                            val customStaffDeptAdapter =
                                CustomStaffDeptRecyclerAdapter(mContext, mStaffDeptList!!, "")
                            mStaffListView!!.adapter = customStaffDeptAdapter
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

            override fun onFailure(call: Call<StaffCDeptResponseModel>, t: Throwable) {
                progressBarDialog!!.dismiss()            }
        })
    }

    private fun initialiseUI() {
        mStaffDeptList= ArrayList()
        extras = intent.extras
        if (extras != null) {
            category_id = extras!!.getString("category_id")
            title = extras!!.getString("title")
        }
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        mStaffListView = findViewById<View>(R.id.mStaffListView) as RecyclerView
        mStaffListView!!.setHasFixedSize(true)
        headermanager = HeaderManager(this@StaffListActivity, title)
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
        mStaffListView!!.layoutManager = llm

    }
}
