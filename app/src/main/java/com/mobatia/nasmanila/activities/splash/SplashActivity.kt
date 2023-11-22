package com.mobatia.nasmanila.activities.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.nasmanila.R

import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.activities.login.LoginActivity
import com.mobatia.nasmanila.activities.tutorial.TutorialActivity
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.PreferenceManager


class SplashActivity : AppCompatActivity() {
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        context = this

        if (AppUtils.checkInternet(context)) {

            Handler().postDelayed({
                if (PreferenceManager.getIsFirstLaunch(context) &&
                    PreferenceManager.getUserID(context).toString().equals("")
                ) {
                    var tutorialIntent: Intent = Intent(context, TutorialActivity::class.java)
                    tutorialIntent.putExtra("type", 1)
                    startActivity(tutorialIntent)
                    finish()
                } else
                if (PreferenceManager.getUserID(context).toString().equals("")) {
                    var loginIntent: Intent = Intent(context, LoginActivity::class.java)
                    startActivity(loginIntent)
                    finish()
                } else {
                    ////AppUtils.getAccessToken(context)
                    var homeIntent: Intent = Intent(context, HomeListAppCompatActivity::class.java)
                    startActivity(homeIntent)
                    finish()
                }
            }, 5000)
        } else {
            AppUtils.showDialogAlertDismiss(
                context ,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred)
        }
    }


}