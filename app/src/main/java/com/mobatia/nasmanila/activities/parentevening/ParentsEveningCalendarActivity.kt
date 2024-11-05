package com.mobatia.nasmanila.activities.parentevening

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.activities.parentevening.model.PTADatesResponseModel
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.fragments.home.progressBarDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.YearMonth
import java.util.Calendar
import java.util.Locale

class ParentsEveningCalendarActivity : AppCompatActivity() {
    private lateinit var mContext: Context
    private lateinit var monthTextView: TextView
    private lateinit var progressBarDialog: ProgressBarDialog
    private lateinit var arrowPrev: ImageView
    private lateinit var arrowNext: ImageView
    private lateinit var monthList: Array<String>
    private lateinit var weekDays: Array<String>
    private lateinit var dateTextView: Array<TextView?>
    private val numsArray = ArrayList<String>()
    private val datesToPlot = ArrayList<String>()
    private var studentId: String = ""
    private var staffId: String = ""
    private var studentName: String = ""
    private var staffName: String = ""
    private var studentClass: String = ""
    private var countMonth: Int = 0
    private var countYear: Int = 0
    private var monthTotalDays: Int = 0
    var headermanager: HeaderManager? = null
    var relativeHeader: RelativeLayout? = null
    var back: ImageView? = null
    var home: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.parent_meetings_calendar_activity)
        mContext = this
        initialize()
        setupHeader()
        setupCalendarDays()
        setupOnClickListeners()
    }

    private fun initialize() {
        progressBarDialog = ProgressBarDialog(mContext)
        studentId = intent.getStringExtra("student_id").orEmpty()
        staffId = intent.getStringExtra("staff_id").orEmpty()
        studentName = intent.getStringExtra("studentName").orEmpty()
        staffName = intent.getStringExtra("staffName").orEmpty()
        studentClass = intent.getStringExtra("studentClass").orEmpty()
        PreferenceManager.setStaffID(mContext, staffId)
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout

        headermanager = HeaderManager(this@ParentsEveningCalendarActivity, "Parents' Meeting")
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
        arrowPrev = findViewById(R.id.arrow_back)
        arrowNext = findViewById(R.id.arrow_nxt)
        monthTextView = findViewById(R.id.monthTextView)
        monthList = resources.getStringArray(R.array.Months)
        weekDays = resources.getStringArray(R.array.Weeks)


        dateTextView = Array(42) {
            findViewById<TextView?>(
                resources.getIdentifier(
                    "textview_$it",
                    "id",
                    packageName
                )
            )
        }

//        resetCalendarDays()

        if (AppUtils.isNetworkConnected(mContext)) fetchAllottedDates() else AppUtils.showDialogAlertDismiss(
            mContext as Activity?,
            "Network Error",
            getString(R.string.no_internet),
            R.drawable.nonetworkicon,
            R.drawable.roundred
        )


    }

    private fun resetCalendarDays() {
        dateTextView.forEach {
            it?.apply {
                setBackgroundColor(Color.WHITE)
                setTextColor(Color.BLACK)
                visibility = View.VISIBLE
            }
        }
    }

    private fun setupHeader() {
        val calendar = Calendar.getInstance()
        val currentMonth = SimpleDateFormat("MM").format(calendar.time).toInt()
        countMonth = currentMonth - 1
        countYear = SimpleDateFormat("yyyy").format(calendar.time).toInt()

        updateHeader()
        arrowPrev.setOnClickListener { navigateMonth(isNext = false) }
        arrowNext.setOnClickListener { navigateMonth(isNext = true) }
    }

    private fun updateHeader() {
        monthTextView.text = "${monthList[countMonth]} $countYear"
    }

    private fun navigateMonth(isNext: Boolean) {
        if (isNext) {
            if (countMonth == 11) {
                countMonth = 0
                countYear++
            } else countMonth++
        } else {
            if (countMonth == 0) {
                countMonth = 11
                countYear--
            } else countMonth--
        }
        updateHeader()
        setupCalendarDays()
        resetCalendarDays()
        if (AppUtils.isNetworkConnected(mContext)) fetchAllottedDates() else AppUtils.showDialogAlertDismiss(
            mContext as Activity?,
            "Network Error",
            getString(R.string.no_internet),
            R.drawable.nonetworkicon,
            R.drawable.roundred
        )
    }

    private fun setupCalendarDays() {
        numsArray.clear()
        val yearMonthObject = YearMonth.of(countYear, countMonth + 1)
        monthTotalDays = yearMonthObject.lengthOfMonth()
        val startDayOffset = when (yearMonthObject.atDay(1).dayOfWeek) {
            DayOfWeek.MONDAY -> 0
            DayOfWeek.TUESDAY -> 1
            DayOfWeek.WEDNESDAY -> 2
            DayOfWeek.THURSDAY -> 3
            DayOfWeek.FRIDAY -> 4
            DayOfWeek.SATURDAY -> 5
            DayOfWeek.SUNDAY -> 6
        }

        numsArray.addAll(List(startDayOffset) { "" })
        numsArray.addAll(List(monthTotalDays) { (it + 1).toString() })

        dateTextView.forEachIndexed { index, textView ->
            textView?.apply {
                visibility = if (index < numsArray.size) View.VISIBLE else View.INVISIBLE
                text = numsArray.getOrNull(index) ?: ""
                setBackgroundColor(Color.WHITE)
            }
        }
    }

    private fun setupOnClickListeners() {
        dateTextView.forEachIndexed { index, textView ->
            textView?.setOnClickListener {
                val day = numsArray[index]
                if (day != "") {
                    val dateStr = "$day/${countMonth + 1}/$countYear"
                    if (dateStr in datesToPlot) openMeetingDetailActivity(dateStr)
                }
            }
        }
    }

    private fun openMeetingDetailActivity(date: String) {
        startActivity(Intent(mContext, MeetingSlotListingActivity::class.java).apply {
            putExtra("date", date)
            putExtra("studentId", studentId)
            putExtra("studentName", studentName)
            putExtra("studentClass", studentClass)
            putExtra("staffName", staffName)
            putExtra("staffId", staffId)
        })
    }

    private fun fetchAllottedDates() {
        progressBarDialog!!.show()
        val token = PreferenceManager.getAccessToken(mContext)
        var paramObject: JsonObject = JsonObject()
        paramObject.addProperty("student_id", studentId)
        paramObject.addProperty("staff_id", staffId)
        val call: Call<PTADatesResponseModel> =
            ApiClient.getClient.pta_allotted_dates(paramObject, "Bearer $token")

        call.enqueue(object : Callback<PTADatesResponseModel> {
            override fun onFailure(call: Call<PTADatesResponseModel>, t: Throwable) {
                progressBarDialog!!.dismiss()
            }

            override fun onResponse(
                call: Call<PTADatesResponseModel>,
                response: Response<PTADatesResponseModel>
            ) {
                progressBarDialog!!.dismiss()
                if (response.isSuccessful && response.body()?.response!!.statuscode == "303") {
                    populateDatesToPlot(response.body()?.response!!.availableDates.orEmpty())
                    highlightAllottedDates()
                } else {
                    Toast.makeText(mContext, "No available dates.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun populateDatesToPlot(dates: List<String>) {
        datesToPlot.clear()
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        datesToPlot.addAll(dates.mapNotNull {
            inputFormat.parse(it)?.let { date -> outputFormat.format(date) }
        })
    }

    private fun highlightAllottedDates() {
        datesToPlot.forEach { date ->
            val (day, month, year) = date.split("/").map { it.toInt() }
            if (month - 1 == countMonth && year == countYear) {
                val index = numsArray.indexOf(day.toString())
                if (index >= 0) {
                    dateTextView[index]?.apply {
                        setBackgroundResource(R.drawable.roundred)
                        setTextColor(Color.WHITE)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        progressBarDialog!!.dismiss()
    }
}
