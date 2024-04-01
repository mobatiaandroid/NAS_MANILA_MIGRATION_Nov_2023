package com.mobatia.nasmanila.activities.notification

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaPlayer.OnSeekCompleteListener
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.fragments.notifications.model.PushNotificationModel
import java.io.IOException
import java.util.Timer
import java.util.TimerTask

class AudioAlertActivity : AppCompatActivity(), OnSeekCompleteListener ,
    OnCompletionListener{
    private var seekBarProgress: SeekBar? = null
    private var linearLayoutMediaController: LinearLayout? = null
    private var handler2 = Handler()

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
    var flag:Boolean = true

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
            Log.e("url",url)
            date = extras!!.getString("date")!!
            pushfrom = extras!!.getString("pushfrom")!!
            type = extras!!.getString("type")!!
        }
        initialiseUI()
        /*if (AppUtils.isNetworkConnected(context)) {
            Play3()
        } else {
            Toast.makeText(context, resources.getString(R.string.no_internet), Toast.LENGTH_SHORT)
                .show()
        }*/
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

      /*  seekBarProgress!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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


        })*/
        player = MediaPlayer()
        player!!.setOnPreparedListener {

        }

        progressBarWait = findViewById<View>(R.id.progressBarWait) as ProgressBar
        if (player!!.isPlaying()) {
            handler2.removeCallbacks(updater)
            player!!.pause()
            playerIamge!!
                .setBackgroundResource(R.drawable.mic)
            player!!.start()
            //playbutton_audio.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
        } else {

            player!!.start()
            playerIamge!!
                .setBackgroundResource(R.drawable.michover)
            player!!.start()
           // playbutton_audio.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
            try {
                updateseekbar()
            }catch (e:Exception)
            {

            }
        }
        setUpMediaPlayer(url)
        progressBarWait!!.visibility = View.GONE
        playerIamge!!.visibility = View.VISIBLE

        btnplay!!.setOnClickListener {
            /*if(flag)
            {
                progressBarWait!!.visibility = View.GONE
                playerIamge!!
                    .setBackgroundResource(R.drawable.michover)
                player!!.start()
                btnplay!!.text = resources.getString(R.string.pause)
            }
            else
            {
                progressBarWait!!.visibility = View.GONE
                playerIamge!!
                    .setBackgroundResource(R.drawable.michover)
                player!!.pause()
                btnplay!!.text = resources.getString(R.string.pause)

            }
            flag = !flag*/
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
                        //Play3()
                        setUpMediaPlayer(url)
                        playerIamge!!
                            .setBackgroundResource(R.drawable.michover)

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
    val updater = Runnable {
        try {
            updateseekbar()
            val currentduration: Long = player!!.getCurrentPosition().toLong()
            textViewPlayed!!.text=(milliseconds(currentduration).toString())
        }catch (e:Exception)
        {

        }


    }
    fun milliseconds(milliscnd: Long): String? {
        var timer = ""
        val secondString: String
        val hour = (milliscnd / (1000 * 60 * 60)).toInt()
        val min = (milliscnd % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val sec = (milliscnd % (1000 * 60 * 60)).toInt() % (1000 * 60) / 1000
        if (hour > 0) {
            timer = "$hour;"
        }
        secondString = if (sec < 10) {
            "0$sec"
        } else {
            "" + sec
        }
        timer = "$timer$min:$secondString"
        return timer
    }
    fun updateseekbar() {

        try {
            seekBarProgress!!.setProgress((player!!.getCurrentPosition().toFloat() / player!!.getDuration() * 100).toInt())
            System.out.print("seekbar"+seekBarProgress)
            handler2.postDelayed(updater, 1000)
        }catch (e:InterruptedException)
        {
            e.printStackTrace();
        }



    }
private fun Play3()
{
    if (url == "") {
        // showToast("Please, set the video URI in HelloAndroidActivity.java in onClick(View v) method");
    } else {
        try {
            player!!.setDataSource(url)
            player!!.setOnPreparedListener(MediaPlayer.OnPreparedListener { mp -> mp.start() })
            player!!.prepare()
        } catch (e: IllegalArgumentException) {
            // showToast("Error while playing video");
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            // showToast("Error while playing video");
            e.printStackTrace()
        } catch (e: IOException) {
           // showToast(resources.getString(R.string.no_internet))
            e.printStackTrace()
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



    override fun onSeekComplete(mp: MediaPlayer?) {
        progressBarWait!!.visibility = View.GONE
        btnplay!!.visibility = View.VISIBLE
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mp!!.stop()
       btnplay!!.text = resources.getString(R.string.play)
        updateTimer?.cancel()
        player!!.reset()
       isReset = true
    }
    fun setUpMediaPlayer( filename: String) {

        val mydir = mContext!!.getDir("audioCheck", Context.MODE_PRIVATE) //Creating an internal dir;


        try {

            player!!.setDataSource(filename)
            player!!.setOnPreparedListener(MediaPlayer.OnPreparedListener { mp -> mp.start() })
            player!!.prepare()

        } catch (exception: Exception) {
            println("failed for load" + exception.message)
        }


    }
    override fun onPause() {
        super.onPause()
        player!!.pause()
    }


    override fun onDestroy() {
        super.onDestroy()
        player!!.stop()
        player!!.release()
    }



    override fun onBackPressed() {
        super.onBackPressed()

        finish()

    }
}





