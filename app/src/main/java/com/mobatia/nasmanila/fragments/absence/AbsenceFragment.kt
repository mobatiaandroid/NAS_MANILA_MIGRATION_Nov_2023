package com.mobatia.nasmanila.fragments.absence
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.absence.LeaveRequestSubmissionActivity
import com.mobatia.nasmanila.activities.absence.LeavesDetailActivity
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.DividerItemDecoration
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.absence.adapter.AbsenceRecyclerAdapter
import com.mobatia.nasmanila.fragments.absence.adapter.StudentSpinnerAdapter
import com.mobatia.nasmanila.fragments.absence.model.LeaveRequestsApiModel
import com.mobatia.nasmanila.fragments.absence.model.LeaveRequestsResponseModel
import com.mobatia.nasmanila.fragments.absence.model.LeavesModel
import com.mobatia.nasmanila.fragments.absence.model.StudentModel
import com.mobatia.nasmanila.fragments.absence.model.StudentlistApiModel
import com.mobatia.nasmanila.fragments.absence.model.StudentlistResponseModel
import com.mobatia.nasmanila.manager.recyclermanager.ItemOffsetDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AbsenceFragment : Fragment() {
    private var mRootView: View? = null
    lateinit var mContext: Context
    private var mAbsenceListView: RecyclerView? = null
    private val mTitle: String? = null
    private val mTabId: String? = null
    var mTitleTextView: TextView? = null
    var studentName: TextView? = null
    var newRequest: TextView? = null
    private var relMain: RelativeLayout? = null
    private var belowViewRelative: RelativeLayout? = null
    var studentsModelArrayList: ArrayList<StudentModel> = ArrayList<StudentModel>()
    private var mAbsenceListViewArray: ArrayList<LeavesModel>? = null
    var mStudentSpinner: LinearLayout? = null
    var stud_id = ""
    var studClass = ""
    var stud_img = ""
    var studImg: ImageView? = null
    var studentList = ArrayList<String>()
    var firstVisit = false
    var progressBarDialog: ProgressBarDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_absence, container,
            false
        )

        mContext = requireActivity()
        progressBarDialog = ProgressBarDialog(mContext!!)
        firstVisit = true
        initialiseUI()
        if (AppUtils.checkInternet(mContext)) {
            getStudentsListFirstAPI()
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

    private fun initialiseUI() {
        mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
        mAbsenceListView = mRootView!!.findViewById<View>(R.id.mAbsenceListView) as RecyclerView
        relMain = mRootView!!.findViewById<View>(R.id.relMain) as RelativeLayout
        studImg = mRootView!!.findViewById<View>(R.id.studImg) as ImageView
        mStudentSpinner = mRootView!!.findViewById<View>(R.id.studentSpinner) as LinearLayout
        belowViewRelative = mRootView!!.findViewById<View>(R.id.belowViewRelative) as RelativeLayout

        studentName = mRootView!!.findViewById<View>(R.id.studentName) as TextView
        newRequest = mRootView!!.findViewById<View>(R.id.newRequest) as TextView

        mTitleTextView!!.setText(NaisClassNameConstants.ABSENCE)
        mAbsenceListView!!.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        mAbsenceListView!!.setLayoutManager(llm)
        val itemDecoration = ItemOffsetDecoration(mContext, 2)
        mAbsenceListView!!.addItemDecoration(itemDecoration)
        //mAbsenceListView.setLayoutManager(recyclerViewLayoutManager);
        //mAbsenceListView.setLayoutManager(recyclerViewLayoutManager);
        mAbsenceListView!!.addItemDecoration(
            DividerItemDecoration(mContext.resources.getDrawable(R.drawable.list_divider))
        )
        mAbsenceListView!!.addOnItemClickListener(object :OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                if (mAbsenceListViewArray!!.size > 0) {
                    val mIntent = Intent(mContext, LeavesDetailActivity::class.java)
                    mIntent.putExtra("studentName", studentName!!.text.toString())
                    mIntent.putExtra("studentClass", studClass)
                    mIntent.putExtra("studentImage", stud_img)
                    mIntent.putExtra("fromDate", mAbsenceListViewArray!![position].from_date)
                    mIntent.putExtra("toDate", mAbsenceListViewArray!![position].to_date)
                    mIntent.putExtra("reasonForAbsence", mAbsenceListViewArray!![position].reason)
                    mContext.startActivity(mIntent)
                }
            }

        })
        mStudentSpinner!!.setOnClickListener {
            if (studentsModelArrayList.size > 0) {
                showSocialmediaList(studentsModelArrayList)
            } else {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity,
                    "Alert",
                    getString(R.string.student_not_available),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        }
        newRequest!!.setOnClickListener {
            val mIntent = Intent(mContext, LeaveRequestSubmissionActivity::class.java)
            mIntent.putExtra("studentName", studentName!!.text.toString())
            mIntent.putExtra("studentId", PreferenceManager.getLeaveStudentId(mContext))
            mIntent.putExtra("studentImage", stud_img)
            mContext.startActivity(mIntent)
        }

    }

    private fun showSocialmediaList(mStudentArray: ArrayList<StudentModel>) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_student_media_list)
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

        val studentAdapter = StudentSpinnerAdapter(mContext, mStudentArray)
        socialMediaList.adapter = studentAdapter
        dialogDismiss.setOnClickListener { dialog.dismiss() }
socialMediaList.addOnItemClickListener(object :OnItemClickListener{
    override fun onItemClicked(position: Int, view: View) {
        dialog.dismiss()
        studentName!!.setText(mStudentArray[position].name)
        stud_id = mStudentArray[position].id
        studClass = mStudentArray[position].mClass
        PreferenceManager.setLeaveStudentId(mContext, stud_id)
        PreferenceManager.setLeaveStudentName(
            mContext,
            mStudentArray[position].name
        )
        stud_img = mStudentArray[position].photo
        if (stud_img != "") {
            Glide.with(mContext).load(stud_img)
                .placeholder(R.drawable.student).into(studImg!!)
        } else {
            studImg!!.setImageResource(R.drawable.student)
        }
        if (AppUtils.isNetworkConnected(mContext)) {
            getList( mStudentArray[position].id)
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

})
       
        dialog.show()
    }

    private fun getList(id: String) {
        mAbsenceListViewArray = ArrayList()
        progressBarDialog!!.show()
        var student = LeaveRequestsApiModel(id)
        val call: Call<LeaveRequestsResponseModel> = ApiClient.getClient.leaveRequests(
            "Bearer " + PreferenceManager.getAccessToken(mContext),
            student
        )
        call.enqueue(object : Callback<LeaveRequestsResponseModel> {
            override fun onResponse(
                call: Call<LeaveRequestsResponseModel>,
                response: Response<LeaveRequestsResponseModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {

                        if (responseData!!.response.requests.size > 0) {
                            mAbsenceListViewArray!!.addAll(responseData!!.response.requests)
                            /*for (i in 0 until responseData!!.response.requests.size) {
                                // JSONObject dataObject = dataArray.optJSONObject(i);
                                val dataObject: LeavesModel =
                                    apiResponse.getResponse().getData().get(i)
                                val mLeavesModel = LeavesModel()
                                mLeavesModel.to_date = dataObject.to_date
                                mLeavesModel.from_date = dataObject.from_date
                                mLeavesModel.reason = dataObject.reason
                                mLeavesModel.status = dataObject.status
                                mAbsenceListViewArray!!.add(mLeavesModel)
                            }*/
                            val mAbsenceRecyclerAdapter = AbsenceRecyclerAdapter(
                                mContext,
                                mAbsenceListViewArray!!
                            )
                            mAbsenceListView!!.adapter = mAbsenceRecyclerAdapter
                        } else {
                            val mAbsenceRecyclerAdapter = AbsenceRecyclerAdapter(
                                mContext,
                                mAbsenceListViewArray!!
                            )
                            mAbsenceListView!!.adapter = mAbsenceRecyclerAdapter
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity,
                                "Alert",
                                "No data available.",
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
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

            override fun onFailure(call: Call<LeaveRequestsResponseModel>, t: Throwable) {

            }
        })
    }

    private fun getStudentsListFirstAPI() {
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
                        studentList= ArrayList()
                        if (responseData!!.response.data.size > 0) {
                            studentsModelArrayList.addAll(responseData!!.response.data)
                            for (i in studentsModelArrayList.indices){
                                studentList.add(studentsModelArrayList[i].name)
                            }
                          /*  for (i in 0 until apiResponse.getResponse().getData().size()) {
                                val item: StudentModel = apiResponse.getResponse().getData().get(i)
                                val gson = Gson()
                                val eventJson = gson.toJson(item)
                                try {
                                    val jsonObject = JSONObject(eventJson)
                                    studentsModelArrayList.add(addStudentDetails(jsonObject))
                                    studentList.add(studentsModelArrayList[i].getmName())

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }*/
                            studentName!!.setText(studentsModelArrayList[0].name)
                            stud_id = studentsModelArrayList[0].id
                            stud_img = studentsModelArrayList[0].photo
                            if (stud_img != "") {
                                Glide.with(mContext).load(stud_img)
                                    .placeholder(R.drawable.student).into(studImg!!)
                            } else {
                                studImg!!.setImageResource(R.drawable.student)
                            }
                            PreferenceManager.setLeaveStudentId(mContext,stud_id)

                            PreferenceManager.setLeaveStudentName(
                                mContext,
                                studentsModelArrayList[0].name
                            )
                            studClass = studentsModelArrayList[0].mClass
                            belowViewRelative!!.visibility = View.VISIBLE
                            newRequest!!.visibility = View.VISIBLE
                            if (AppUtils.isNetworkConnected(mContext)) {
                                getListFirst( stud_id)
                            } else {
                                AppUtils.showDialogAlertDismiss(
                                    mContext as Activity,
                                    "Network Error",
                                    getString(R.string.no_internet),
                                    R.drawable.nonetworkicon,
                                    R.drawable.roundred
                                )
                            }

                            // studentList.add("Select a child");

                            /*CustomSpinnerAdapter dataAdapter = new CustomSpinnerAdapter(mContext,
                                        R.layout.spinnertextwithoutbg, studentList,-1);
                                mStudentSpinner.setAdapter(dataAdapter);*/
                        } else {
                            belowViewRelative!!.visibility = View.INVISIBLE
                            newRequest!!.visibility = View.INVISIBLE
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity,
                                "Alert",
                                getString(R.string.student_not_available),
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
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

    private fun getListFirst(studId: String) {
        mAbsenceListViewArray = ArrayList()
        progressBarDialog!!.show()
        var student = LeaveRequestsApiModel(studId)
        val call: Call<LeaveRequestsResponseModel> = ApiClient.getClient.leaveRequests(
            "Bearer " + PreferenceManager.getAccessToken(mContext),
            student
        )
        call.enqueue(object : Callback<LeaveRequestsResponseModel> {
            override fun onResponse(
                call: Call<LeaveRequestsResponseModel>,
                response: Response<LeaveRequestsResponseModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {

                        if (responseData!!.response.requests.size > 0) {
                            mAbsenceListViewArray!!.addAll(responseData!!.response.requests)
                           /* for (i in 0 until responseData!!.response.requests.size) {
                                // JSONObject dataObject = dataArray.optJSONObject(i);
                                val dataObject: LeavesModel =
                                    responseData!!.response.requests
                                val mLeavesModel = LeavesModel()
                                mLeavesModel.to_date = dataObject.to_date
                                mLeavesModel.from_date = dataObject.from_date
                                mLeavesModel.reason = dataObject.reason
                                mLeavesModel.status = dataObject.status
                                mAbsenceListViewArray!!.add(mLeavesModel)
                            }*/
                            val mAbsenceRecyclerAdapter =
                                AbsenceRecyclerAdapter(mContext, mAbsenceListViewArray!!)
                            mAbsenceListView!!.adapter = mAbsenceRecyclerAdapter
                        } else {
                            val mAbsenceRecyclerAdapter =
                                AbsenceRecyclerAdapter(mContext, mAbsenceListViewArray!!)
                            mAbsenceListView!!.adapter = mAbsenceRecyclerAdapter
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity,
                                "Alert",
                                "No data available.",
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
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

            override fun onFailure(call: Call<LeaveRequestsResponseModel>, t: Throwable) {

            }
        })

    }
    override fun onResume() {
        super.onResume()
        //other stuff
        if (firstVisit) {
            //do stuff for first visit only
            firstVisit = false
        } else {
            if (AppUtils.isNetworkConnected(mContext)) {
                studentName!!.text = PreferenceManager.getLeaveStudentName(mContext)
                getList(PreferenceManager.getLeaveStudentId(mContext).toString())
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
    }
}
