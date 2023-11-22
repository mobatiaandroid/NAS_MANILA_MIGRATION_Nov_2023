package com.mobatia.nasmanila.activities.notification

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog

class TextAlertActivity : AppCompatActivity() {
    var txtmsg: TextView? = null
    var mDateTv: TextView? = null
    var img: ImageView? = null
    var home: ImageView? = null
    var extras: Bundle? = null
    var position = 0
    var context: Context? = null
    var mActivity: Activity? = null
    var header: RelativeLayout? = null
    var back: ImageView? = null
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    var progressBarDialog: ProgressBarDialog? = null
    var id = ""
    var title = ""
    var message = ""
    var url = ""
    var date = ""
    private var day = ""
    private var month = ""
    private var year = ""
    private var pushDate = ""
    private var pushfrom = ""
    private var type = ""
    private var pushID = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_alert)
        mActivity = this
        context = this
        extras = intent.extras
        if (extras != null) {
            position = extras!!.getInt("position")
            message = extras!!.getString("message")!!
            url = extras!!.getString("url")!!
            date = extras!!.getString("date")!!
            pushfrom = extras!!.getString("pushfrom")!!
            type = extras!!.getString("type")!!
        }
        date = AppUtils.dateParsingToDdMmmYyyy(date)!!
        initialiseUI()

    }


    private fun setDetails() {
        txtmsg!!.text =
            Html.fromHtml(
                message.replace("\n", "<br>")
                    .replace(" ", "&nbsp;")
            )
        mDateTv!!.text = date
    }

    private fun initialiseUI() {
//        img = findViewById<View>(R.id.image) as ImageView?
        txtmsg = findViewById<View>(R.id.txt) as TextView
        txtmsg!!.movementMethod = LinkMovementMethod.getInstance()
        progressBarDialog = ProgressBarDialog(context!!)
        mDateTv = findViewById<View>(R.id.mDateTv) as TextView
        relativeHeader = findViewById(R.id.relativeHeader)
        headermanager = HeaderManager(mActivity, "Notification")
        headermanager!!.getHeader(relativeHeader!!, 0)
        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(R.drawable.back,
            R.drawable.back)
        back!!.setOnClickListener { finish() }
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(mActivity, HomeListAppCompatActivity::class.java)
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        setDetails()
    }
}