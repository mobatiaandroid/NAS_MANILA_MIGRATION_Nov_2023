package com.mobatia.nasmanila.activities.notification

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity

import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager

import com.mobatia.nasmanila.fragments.notifications.model.PushNotificationModel

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AudioAlertActivity : AppCompatActivity() {
    private var seekBarProgress: SeekBar? = null
    private var linearLayoutMediaController: LinearLayout? = null

    var btnplay: TextView? = null
    var position = 0
    private var isReset = false
    var context: Context = this
    var player: MediaPlayer? = null
    var id = ""
    var title = ""
    var message = ""
    var date = ""
    var day = ""
    var month = ""
    var year = ""
    var pushDate = ""
    private var pushID = ""
    private var textViewPlayed: TextView? = null
    private var textViewLength: TextView? = null
    private var textcontent: TextView? = null
    private var updateTimer: Timer? = null
    var playerIamge: ImageView? = null
    var alertlist: ArrayList<PushNotificationModel?>? = null
    private var progressBarWait: ProgressBar? = null
    private var url = ""
    private var pushfrom = ""
    private var type = ""
    private var isplayclicked = false
    var backImg: ImageView? = null
    var home: ImageView? = null
    var mContext: Context? = null
    var relativeHeader: RelativeLayout? = null
    var headerManager: HeaderManager? = null
    var mActivity: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_alert)
        mContext = this
        mActivity = this
        val extras = intent.extras
        if (extras != null) {
            position = extras!!.getInt("position")
            message = extras!!.getString("message")!!
            url = extras!!.getString("url")!!
            date = extras!!.getString("date")!!
            pushfrom = extras!!.getString("pushfrom")!!
            type = extras!!.getString("type")!!
        }
        initialiseUI()

    }

    private fun initialiseUI() {
        relativeHeader = findViewById(R.id.relativeHeader)
        headerManager = HeaderManager(mActivity, "Notification")
        headerManager!!.getHeader(relativeHeader!!, 0)
        backImg = headerManager!!.getLeftButton()
        headerManager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        backImg!!.setOnClickListener {
            finish()
        }

        home = headerManager!!.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(mContext, HomeListAppCompatActivity::class.java)
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        linearLayoutMediaController =
            findViewById<View>(R.id.linearLayoutMediaController) as LinearLayout
        btnplay = findViewById<View>(R.id.btn_play) as TextView
        textViewPlayed = findViewById<View>(R.id.textViewPlayed) as TextView
        textViewLength = findViewById<View>(R.id.textViewLength) as TextView
        textcontent = findViewById<View>(R.id.txt) as TextView
        seekBarProgress = findViewById<View>(R.id.seekBarProgress) as SeekBar
        playerIamge = findViewById<View>(R.id.imageViewPauseIndicator) as ImageView
//        url = alertlist!![position]!!.pushURL!!
//        textcontent!!.text = alertlist!![position]!!.pushTitle
//        println("check url$url")
        seekBarProgress!!.progress = 0

        seekBarProgress!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    textViewPlayed!!.text = (AppUtils.durationInSecondsToString(progress))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (player!!.isPlaying) {
                    progressBarWait!!.visibility = View.GONE
                    player!!.seekTo(seekBar!!.progress * 1000)
                }
            }

        })
        player = MediaPlayer()
        player!!.setOnPreparedListener {

        }
        player!!.setOnCompletionListener {
            player!!.stop()
            btnplay!!.text = resources.getString(R.string.play)
            updateTimer?.cancel()
            player!!.reset()
            isReset = true
        }
        player!!.setOnSeekCompleteListener {
            progressBarWait!!.visibility = View.GONE
            btnplay!!.visibility = View.VISIBLE
        }
        progressBarWait = findViewById<View>(R.id.progressBarWait) as ProgressBar

        btnplay!!.setOnClickListener {

            if (!isplayclicked) {
                if (player!!.isPlaying) {
                    println("is come click second")
                    player!!.pause()
                    playerIamge!!.background = resources
                        .getDrawable(R.drawable.mic)
                    btnplay!!.text = resources.getString(R.string.play)
                }
                isplayclicked = true
            } else {
                if (player == null || isReset) {
                    if (AppUtils.checkInternet(context)) {
                        player!!.start()
                        playerIamge
                            ?.setBackgroundResource(R.drawable.michover)
                        isReset = false
                    } else {
                        Toast.makeText(
                            context,
                            resources.getString(R.string.no_internet),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (AppUtils.checkInternet(context)) {
                        player!!.start()
                        playerIamge
                            ?.setBackgroundResource(R.drawable.michover)
                        btnplay!!.text = resources
                            .getString(R.string.pause)
                    } else {
                        Toast.makeText(
                            context,
                            resources.getString(R.string.no_internet),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                isplayclicked = false
            }

        }
    }

    private fun updateMediaProgress() {
        updateTimer = Timer("progress Updater")
        updateTimer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (player != null) {
                        seekBarProgress!!.progress = player!!
                            .currentPosition / 1000
                    }
                }
            }
        }, 0, 1000)

    }
}




//    override fun onCompletion(mp: MediaPlayer?) {
//        mp!!.stop()
//        btnplay!!.text = resources.getString(R.string.play)
//        updateTimer?.cancel()
//        player!!.reset()
//        isReset = true
//    }
//
//    override fun onSeekComplete(mp: MediaPlayer?) {
//        progressBarWait!!.visibility = View.GONE
//        btnplay!!.visibility = View.VISIBLE
//    }
//
//    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//
//        if (!fromUser) {
//            textViewPlayed!!.text = (AppUtils.durationInSecondsToString(progress))
//        }
//    }
//
//    override fun onStartTrackingTouch(seekBar: SeekBar?) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onStopTrackingTouch(seekBar: SeekBar?) {
//
//        // TODO Auto-generated method stub
//        if (player!!.isPlaying) {
//            progressBarWait!!.visibility = View.GONE
//            player!!.seekTo(seekBar!!.progress * 1000)
//        }
//    }
//
//    override fun onPrepared(mp: MediaPlayer?) {
//
//
//        val duration = mp!!.duration / 1000
//
//
//        seekBarProgress!!.max = duration
//        textViewLength!!.text = (AppUtils.durationInSecondsToString(duration))
//        progressBarWait!!.visibility = View.GONE
//
//        if (!mp.isPlaying) {
//            playerIamge!!.setBackgroundResource(R.drawable.mic)
//            btnplay!!.visibility = View.VISIBLE
//            btnplay!!.text = resources.getString(R.string.pause)
//            mp.start()
//            updateMediaProgress()
//            linearLayoutMediaController!!.visibility = View.VISIBLE
//        }
//    }
//
//    override fun onClick(v: View?) {
//
//        // TODO Auto-generated method stub
//        if (v === btnplay) {
//            if (!isplayclicked) {
//                if (player!!.isPlaying) {
//                    println("is come click second")
//                    player!!.pause()
//                    playerIamge!!.background = resources
//                        .getDrawable(R.drawable.mic)
//                    btnplay!!.text = resources.getString(R.string.play)
//                }
//                isplayclicked = true
//            } else {
//                if (player == null || isReset) {
//                    if (AppUtils.checkInternet(context)) {
//                        player!!.start()
//                        playerIamge
//                            ?.setBackgroundResource(R.drawable.michover)
//                        isReset = false
//                    } else {
//                        Toast.makeText(context, resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    if (AppUtils.checkInternet(context)) {
//                        player!!.start()
//                        playerIamge
//                            ?.setBackgroundResource(R.drawable.michover)
//                        btnplay!!.text = resources
//                            .getString(R.string.pause)
//                    } else {
//                        Toast.makeText(context, resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
//                    }
//                }
//                isplayclicked = false
//            }
//        } else if (v === backImg) {
//            finish()
//        }
//    }
