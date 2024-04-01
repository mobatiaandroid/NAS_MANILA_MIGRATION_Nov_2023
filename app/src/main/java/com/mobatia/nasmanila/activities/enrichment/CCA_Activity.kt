package com.mobatia.nasmanila.activities.enrichment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.enrichment.adapter.CCAsListActivityAdapter
import com.mobatia.nasmanila.activities.enrichment.model.CCADetailModel
import com.mobatia.nasmanila.activities.enrichment.model.CCAModel

import com.mobatia.nasmanila.activities.enrichment.model.CCAchoiceModel
import com.mobatia.nasmanila.activities.enrichment.model.Cca_detailsApiModel
import com.mobatia.nasmanila.activities.enrichment.model.Cca_detailsResponseModel
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.DividerItemDecoration
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import com.mobatia.nasmanila.fragments.absence.adapter.StudentSpinnerAdapter
import com.mobatia.nasmanila.fragments.absence.model.StudentModel
import com.mobatia.nasmanila.fragments.absence.model.StudentlistApiModel
import com.mobatia.nasmanila.fragments.absence.model.StudentlistResponseModel
import com.mobatia.nasmanila.manager.recyclermanager.ItemOffsetDecoration
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CCA_Activity:AppCompatActivity() {
    lateinit var mContext: Context
    var progressBarDialog: ProgressBarDialog? = null
    var studentsModelArrayList: ArrayList<StudentModel>? = null
   var mCCAmodelArrayList: ArrayList<CCAModel>? = null
    var CCADetailModelArrayList: ArrayList<CCADetailModel?>? = null
    var CCAchoiceModelArrayList: ArrayList<CCAchoiceModel?>? = null
    var CCAchoiceModelArrayList2: ArrayList<CCAchoiceModel?>? = null
    var studentName: TextView? = null
    var textViewYear: TextView? = null
    var stud_id = ""
    var stud_class = ""
    var stud_name = ""
    var stud_img = ""
    var mStudentSpinner: LinearLayout? = null
    var headermanager: HeaderManager? = null
    var relativeHeader: RelativeLayout? = null
    var back: ImageView? = null
    var studImg: ImageView? = null
    var home: ImageView? = null
    var tab_type = "CCAs"
    var extras: Bundle? = null

    //    ArrayList<String> mCcaArrayList;
    var recycler_review: RecyclerView? = null
    var recyclerViewLayoutManager: GridLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cca)
        mContext = this
        progressBarDialog = ProgressBarDialog(mContext!!)
        initUI()
        mStudentSpinner!!.setOnClickListener { showSocialmediaList(studentsModelArrayList) }
        if (AppUtils.isNetworkConnected(mContext)) {
            getStudentsListAPI()
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

    private fun showSocialmediaList(mStudentArray: ArrayList<StudentModel>?) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialogue_student_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<View>(R.id.btn_dismiss) as Button
        val iconImageView = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        iconImageView.setImageResource(R.drawable.boy)
        val socialMediaList =
            dialog.findViewById<View>(R.id.recycler_view_social_media) as RecyclerView
        //if(mSocialMediaArray.get())
        //if(mSocialMediaArray.get())
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            dialogDismiss.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.button_new))
        } else {
            dialogDismiss.background = mContext.resources.getDrawable(R.drawable.button_new)
        }
        socialMediaList.addItemDecoration(DividerItemDecoration(mContext.resources.getDrawable(R.drawable.list_divider)))

        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm

        val studentAdapter = StudentSpinnerAdapter(mContext, mStudentArray!!)
        socialMediaList.adapter = studentAdapter
        dialogDismiss.setOnClickListener { dialog.dismiss() }
        socialMediaList!!.addOnItemClickListener(object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                dialog.dismiss()
                studentName!!.setText(mStudentArray.get(position).name)
                stud_id = mStudentArray.get(position).id
                stud_name = mStudentArray.get(position).name
                stud_class = mStudentArray.get(position).mClass
                stud_img = mStudentArray.get(position).photo
                if (stud_img != "") {
                    Glide.with(mContext).load(stud_img)
                        .placeholder(R.drawable.student).into(studImg!!)
                } else {
                    studImg!!.setImageResource(R.drawable.student)
                }
                textViewYear!!.text = "Class : " + mStudentArray.get(position).mClass
                PreferenceManager.setCCAStudentIdPosition(
                    mContext,
                    position.toString() + ""
                )
                if (AppUtils.checkInternet(mContext!!)) {
                    getCCAListAPI(stud_id)
                }else{
                    Toast.makeText(
                        mContext,
                        mContext!!.resources.getString(R.string.no_internet),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        })

      /*  socialMediaList.addOnItemTouchListener(
            RecyclerItemListener(mContext, socialMediaList,
                object : RecyclerTouchListener() {
                    fun onClickItem(v: View?, position: Int) {
                        dialog.dismiss()
                        studentName.setText(mStudentArray.get(position).getmName())
                        stud_id = mStudentArray.get(position).getmId()
                        stud_name = mStudentArray.get(position).getmName()
                        stud_class = mStudentArray.get(position).getmClass()
                        stud_img = mStudentArray.get(position).getmPhoto()
                        if (stud_img != "") {
                            Picasso.with(mContext).load(AppUtils.replace(stud_img))
                                .placeholder(R.drawable.student).fit().into(studImg)
                        } else {
                            studImg!!.setImageResource(R.drawable.student)
                        }
                        textViewYear!!.text = "Class : " + mStudentArray.get(position).getmClass()
                        PreferenceManager.setCCAStudentIdPosition(
                            mContext,
                            position.toString() + ""
                        )
                        getCCAListAPI(stud_id)
                    }

                    fun onLongClickItem(v: View?, position: Int) {
                        println("On Long Click Item interface")
                    }
                })
        )*/
        dialog.show()
    }

    private fun getStudentsListAPI() {
        progressBarDialog!!.show()
        var student = StudentlistApiModel()
        val call: Call<StudentlistResponseModel> = ApiClient.getClient.studentlist(
            "Bearer " + PreferenceManager.getAccessToken(mContext),
            student
        )
        call.enqueue(object : Callback<StudentlistResponseModel> {
            override fun onResponse(
                call: Call<StudentlistResponseModel>,
                response: Response<StudentlistResponseModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {
                        studentsModelArrayList= ArrayList()
                        if (responseData!!.response.data.size > 0) {
                          /*  for (i in 0 until apiResponse.getResponse().getData().size()) {
                                val item: StudentModel = apiResponse.getResponse().getData().get(i)
                                val gson = Gson()
                                val eventJson = gson.toJson(item)
                                try {
                                    val jsonObject = JSONObject(eventJson)
                                    studentsModelArrayList!!.add(addStudentDetails(jsonObject))
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }*/
                            studentsModelArrayList!!.addAll(responseData!!.response.data)
                            if (PreferenceManager.getStudIdForCCA(mContext).equals("")) {
                                studentName!!.setText(studentsModelArrayList!![0].name)
                                stud_id = studentsModelArrayList!![0].id
                                stud_name = studentsModelArrayList!![0].name
                                stud_class = studentsModelArrayList!![0].mClass
                                stud_img = studentsModelArrayList!![0].photo
                                if (stud_img != "") {
                                    Glide.with(mContext).load(stud_img)
                                        .placeholder(R.drawable.student).into(studImg!!)
                                } else {
                                    studImg!!.setImageResource(R.drawable.student)
                                }
                                textViewYear!!.text =
                                    "Class : " + studentsModelArrayList!![0].mClass
                                PreferenceManager.setCCAStudentIdPosition(mContext, "0")
                            } else {
                                val studentSelectPosition = Integer.valueOf(
                                    PreferenceManager.getCCAStudentIdPosition(mContext)
                                )
                                studentName!!.setText(studentsModelArrayList!![studentSelectPosition].name)
                                stud_id = studentsModelArrayList!![studentSelectPosition].id
                                stud_name =
                                    studentsModelArrayList!![studentSelectPosition].name
                                stud_class =
                                    studentsModelArrayList!![studentSelectPosition].mClass
                                stud_img = studentsModelArrayList!![0].photo
                                if (stud_img != "") {
                                    Glide.with(mContext).load(stud_img)
                                        .placeholder(R.drawable.student).into(studImg!!)
                                } else {
                                    studImg!!.setImageResource(R.drawable.student)
                                }
                                textViewYear!!.text =
                                    "Class : " + studentsModelArrayList!![studentSelectPosition].mClass
                                
                            }
                            if (AppUtils.checkInternet(mContext!!)) {
                                getCCAListAPI(stud_id)
                            }else{
                                Toast.makeText(
                                    mContext,
                                    mContext!!.resources.getString(R.string.no_internet),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            //CustomStatusDialog();
                            Toast.makeText(mContext, "No Student Found.", Toast.LENGTH_SHORT).show()
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
                        mContext as Activity?,
                        getString(R.string.error_heading),
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<StudentlistResponseModel>, t: Throwable) {

            }
        })
    }

    private fun getCCAListAPI(studId: String) {
        progressBarDialog!!.show()
var ccadetailbody=Cca_detailsApiModel(studId)

        val call: Call<Cca_detailsResponseModel> = ApiClient.getClient.cca_details(
            "Bearer "+ PreferenceManager.getAccessToken(mContext),ccadetailbody)
        call.enqueue(object : Callback<Cca_detailsResponseModel> {
            override fun onResponse(
                call: Call<Cca_detailsResponseModel>,
                response: Response<Cca_detailsResponseModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {
                        mCCAmodelArrayList= ArrayList()
                        if (responseData!!.response.data.size > 0) {
                            for (i in 0 until responseData!!.response.data.size) {
                                val item: CCAModel = responseData!!.response.data.get(i)
                                val gson = Gson()
                                val eventJson = gson.toJson(item)
                                try {
                                    val jsonObject = JSONObject(eventJson)
                                    mCCAmodelArrayList!!.add(addCCAlist(jsonObject))
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                            if (mCCAmodelArrayList!!.size > 0) {
                                val mCCAsActivityAdapter =
                                    CCAsListActivityAdapter(mContext, mCCAmodelArrayList!!)
                                recycler_review!!.adapter = mCCAsActivityAdapter
                            }
                        } else {
                            val mCCAsActivityAdapter =
                                CCAsListActivityAdapter(mContext, mCCAmodelArrayList!!)
                            recycler_review!!.adapter = mCCAsActivityAdapter
                            Toast.makeText(
                                this@CCA_Activity,
                                "No EL available.",
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
                        mContext as Activity?,
                        getString(R.string.error_heading),
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<Cca_detailsResponseModel>, t: Throwable) {

            }
        })
    }

    private fun addCCAlist(dataObject: JSONObject): CCAModel {
        val mCCAModel = CCAModel()
        mCCAModel.cca_days_id=dataObject.optString("cca_days_id")
        mCCAModel.title=dataObject.optString("title")
        mCCAModel.from_date=dataObject.optString("from_date")
        mCCAModel.to_date=dataObject.optString("to_date")
        mCCAModel.isAttendee=dataObject.optString("isAttendee")
        mCCAModel.submission_dateTime=dataObject.optString("submission_dateTime")
        mCCAModel.isSubmissiondateOver=dataObject.optString("isSubmissiondateOver")
        val jsonCCADetailArray: JSONArray = dataObject.optJSONArray("details")
        CCADetailModelArrayList = java.util.ArrayList()
        if (jsonCCADetailArray.length() > 0) {
            for (i in 0 until jsonCCADetailArray.length()) {
                val objectCCA = jsonCCADetailArray.optJSONObject(i)
                val mCCADetailModel = CCADetailModel()
                mCCADetailModel.day=objectCCA.optString("day")
                val jsonCCAChoiceArray = objectCCA.optJSONArray("choice1")
                val jsonCCAChoiceArray2 = objectCCA.optJSONArray("choice2")
                CCAchoiceModelArrayList = java.util.ArrayList()
                if (jsonCCAChoiceArray.length() > 0) {
                    for (j in 0..jsonCCAChoiceArray.length()) {
                        val mCCADetailModelchoice = CCAchoiceModel()
                        if (jsonCCAChoiceArray.length() != j) {
                            val objectCCAchoice = jsonCCAChoiceArray.optJSONObject(j)
                            mCCADetailModelchoice.cca_item_name=objectCCAchoice.optString("cca_item_name")
                            mCCADetailModelchoice.cca_details_id=objectCCAchoice.optString("cca_details_id")
                            mCCADetailModelchoice.isattending=objectCCAchoice.optString("isAttendee")
                            mCCADetailModelchoice.cca_item_start_time=objectCCAchoice.optString("cca_item_start_time")
                            mCCADetailModelchoice.cca_item_description=
                                objectCCAchoice.optString(
                                    "cca_item_description"
                                )

                            mCCADetailModelchoice.venue=objectCCAchoice.optString("venue")
                            mCCADetailModelchoice.cca_item_end_time=objectCCAchoice.optString("cca_item_end_time")
                            mCCADetailModelchoice.status="0"
                            mCCADetailModelchoice.dayChoice=objectCCAchoice.optString("day")
                        } else {
                            mCCADetailModelchoice.cca_item_name="None"
                            mCCADetailModelchoice.cca_details_id="-541"
                            mCCADetailModelchoice.isattending="0"
                            mCCADetailModelchoice.status="0"
                            mCCADetailModelchoice.cca_item_description="0"
                            mCCADetailModelchoice.venue="0"
                            mCCADetailModelchoice.dayChoice=objectCCA.optString("day")
                        }
                        CCAchoiceModelArrayList!!.add(mCCADetailModelchoice)
                    }
                }
                mCCADetailModel.choice1=CCAchoiceModelArrayList
                CCAchoiceModelArrayList2 = ArrayList()
                if (jsonCCAChoiceArray2.length() > 0) {
                    for (j in 0..jsonCCAChoiceArray2.length()) {
                        val mCCADetailModelchoice = CCAchoiceModel()
                        if (jsonCCAChoiceArray2.length() != j) {
                            val objectCCAchoice = jsonCCAChoiceArray2.optJSONObject(j)
                            mCCADetailModelchoice.cca_item_name=objectCCAchoice.optString("cca_item_name")
                            mCCADetailModelchoice.cca_details_id=objectCCAchoice.optString("cca_details_id")
                            mCCADetailModelchoice.isattending=objectCCAchoice.optString("isAttendee")
                            mCCADetailModelchoice.cca_item_start_time=objectCCAchoice.optString("cca_item_start_time")
                            mCCADetailModelchoice.cca_item_end_time=objectCCAchoice.optString("cca_item_end_time")
                            mCCADetailModelchoice.cca_item_description=
                                objectCCAchoice.optString(
                                    "cca_item_description"
                                )

                            mCCADetailModelchoice.venue=objectCCAchoice.optString("venue")
                            mCCADetailModelchoice.dayChoice=objectCCAchoice.optString("day")
                            mCCADetailModelchoice.status="0"
                        } else {
                            mCCADetailModelchoice.cca_item_name="None"
                            mCCADetailModelchoice.cca_details_id="-541"
                            mCCADetailModelchoice.isattending="0"
                            mCCADetailModelchoice.status="0"
                            mCCADetailModelchoice.dayChoice=objectCCA.optString("day")
                            mCCADetailModelchoice.cca_item_description="0"
                            mCCADetailModelchoice.venue="0"
                        }
                        CCAchoiceModelArrayList2!!.add(mCCADetailModelchoice)
                    }
                }
                mCCADetailModel.choice2=CCAchoiceModelArrayList2
                CCADetailModelArrayList!!.add(mCCADetailModel)
            }
        }
        mCCAModel.details=CCADetailModelArrayList
        return mCCAModel
    }

    private fun initUI() {

        extras = intent.extras
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")!!
        }
        //        mCcaArrayList = new ArrayList<>();
//        mCcaArrayList.add("Sunday CCA");
//        mCcaArrayList.add("Monday CCA");
//        mCcaArrayList.add("Tuesday CCA");
//        mCcaArrayList.add("Wednesday CCA");
//        mCcaArrayList.add("Thursday CCA");
//        mCcaArrayList.add("Friday CCA");
//        mCcaArrayList.add("Saturday CCA");
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        recycler_review = findViewById<View>(R.id.recycler_view_cca) as RecyclerView

        recycler_review!!.setHasFixedSize(true)
        recyclerViewLayoutManager = GridLayoutManager(mContext, 1)
        val spacing = 5 // 50px

        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
        recycler_review!!.addItemDecoration(
            DividerItemDecoration(mContext.resources.getDrawable(R.drawable.list_divider))
        )
        recycler_review!!.addItemDecoration(itemDecoration)
        recycler_review!!.layoutManager = recyclerViewLayoutManager
        headermanager = HeaderManager(this@CCA_Activity, tab_type)
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
        mStudentSpinner = findViewById<View>(R.id.studentSpinner) as LinearLayout
        studentName = findViewById<View>(R.id.studentName) as TextView
        studImg = findViewById<View>(R.id.studImg) as ImageView
        textViewYear = findViewById<View>(R.id.textViewYear) as TextView
        recycler_review!!.addOnItemClickListener(object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {

                if (mCCAmodelArrayList!!.get(position).isAttendee.equals("0")) {
                    if (mCCAmodelArrayList!!.get(position).isSubmissiondateOver
                            .equals("0")
                    ) {
                        if (mCCAmodelArrayList!!.get(position).details!!.size > 0) {
                            PreferenceManager.setccadetailarray(mCCAmodelArrayList!!.get(position).details,mContext)
                            val intent = Intent(mContext, CCASelectionActivity::class.java)
                            /*intent.putExtra(
                                "CCA_Detail",
                                mCCAmodelArrayList!!.get(position).details
                            )*/
                            intent.putExtra("tab_type", tab_type)
                            PreferenceManager.setStudIdForCCA(mContext, stud_id)
                            PreferenceManager.setStudNameForCCA(mContext, stud_name)
                            PreferenceManager.setStudClassForCCA(mContext, stud_class)
                            PreferenceManager.setCCATitle(
                                mContext,
                                mCCAmodelArrayList!!.get(position).title.toString()
                            )
                            PreferenceManager.setCCAItemId(
                                mContext,
                                mCCAmodelArrayList!!.get(position).cca_days_id.toString()
                            )
                            startActivity(intent)
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity,
                                "Alert",
                                "No data found.",
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
                        }
                    } else {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity,
                            "Alert",
                            "EL Sign-Up Closed.",
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                        //                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Date of submission expired.", R.drawable.exclamationicon, R.drawable.round);
                    }
                } else if (mCCAmodelArrayList!!.get(position).isAttendee.equals("2")) {
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity,
                        "Alert",
                        "Your EL choices have been submitted and are currently being processed",
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
//                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Already submitted your choices for this CCA term. You can view the details, once they are approved by BISAD.", R.drawable.exclamationicon, R.drawable.round);
                } else {
                    val intent = Intent(mContext, CCAsReviewAfterSubmissionActivity::class.java)
                    intent.putExtra("tab_type", tab_type)
                    PreferenceManager.setStudIdForCCA(mContext, stud_id)
                    PreferenceManager.setStudNameForCCA(mContext, stud_name)
                    PreferenceManager.setStudClassForCCA(mContext, stud_class)
                    PreferenceManager.setCCATitle(
                        mContext,
                        mCCAmodelArrayList!!.get(position).title.toString()
                    )
                    PreferenceManager.setCCAItemId(
                        mContext,
                        mCCAmodelArrayList!!.get(position).cca_days_id.toString()
                    )
                    startActivity(intent)
                }
            }

        })
    }
}
