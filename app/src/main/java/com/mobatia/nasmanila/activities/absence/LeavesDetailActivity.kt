package com.mobatia.nasmanila.activities.absence

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants

class LeavesDetailActivity :AppCompatActivity(){
    lateinit var mContext:Context
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null

    var fromlayout: LinearLayout? = null
    var reasonlayout:LinearLayout? = null
    var stnameValue: TextView? =
        null
    var leaveDateFromValue:android.widget.TextView? = null
    var leaveDateToValue:android.widget.TextView? = null
    var reasonValue:android.widget.TextView? = null
    var studClassValue:android.widget.TextView? = null
    var extras: Bundle? = null
    var position = 0
    var studentNameStr = ""
    var studentClassStr = ""
    var fromDate = ""
    var toDate = ""
    var reasonForAbsence = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_detailpage)
        mContext=this
        initUI()
        setValues()
    }

    private fun setValues() {
        stnameValue!!.text = studentNameStr
        studClassValue!!.text = studentClassStr
        leaveDateFromValue!!.setText(AppUtils.dateParsingToDdMmYyyy(fromDate))
        leaveDateToValue!!.setText(AppUtils.dateParsingToDdMmYyyy(toDate))
        reasonValue!!.text = reasonForAbsence
    }

    private fun initUI() {
        extras = intent.extras
        if (extras != null) {
            studentNameStr = extras!!.getString("studentName")!!
            studentClassStr = extras!!.getString("studentClass")!!
            fromDate = extras!!.getString("fromDate")!!
            toDate = extras!!.getString("toDate")!!
            reasonForAbsence = extras!!.getString("reasonForAbsence")!!
        }
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        fromlayout = findViewById<View>(R.id.fromlayout) as LinearLayout
        reasonlayout = findViewById<View>(R.id.reasonlayout) as LinearLayout
        stnameValue = findViewById<View>(R.id.stnameValue) as TextView
        studClassValue = findViewById<View>(R.id.studClassValue) as TextView
        leaveDateFromValue = findViewById<View>(R.id.leaveDateFromValue) as TextView
        leaveDateToValue = findViewById<View>(R.id.leaveDateToValue) as TextView
        reasonValue = findViewById<View>(R.id.reasonValue) as TextView

        headermanager = HeaderManager(this@LeavesDetailActivity, NaisClassNameConstants.ABSENCE)
        headermanager!!.getHeader(relativeHeader, 0)

        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(mContext, HomeListAppCompatActivity::class.java)
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        back!!.setOnClickListener {
            finish()
        }


    }

}
