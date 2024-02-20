package com.mobatia.nasmanila.activities.splash

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.nasmanila.R

import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.activities.login.LoginActivity
import com.mobatia.nasmanila.activities.tutorial.TutorialActivity
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.scottyab.rootbeer.RootBeer


class SplashActivity : AppCompatActivity() {
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        context = this

        if (AppUtils.checkInternet(context)) {
            val rootBeer = RootBeer(context)
            if (rootBeer.isRooted()) {
                showDeviceIsRootedPopUp(context)
            } else {
                Handler().postDelayed({
                    if (PreferenceManager.getIsFirstLaunch(context) &&
                        PreferenceManager.getAccessToken(context).toString().equals("")
                    ) {
                        var tutorialIntent: Intent = Intent(context, TutorialActivity::class.java)
                        tutorialIntent.putExtra("type", 1)
                        startActivity(tutorialIntent)
                        finish()
                    } else
                        if (PreferenceManager.getAccessToken(context).toString().equals("")) {
                            var loginIntent: Intent = Intent(context, LoginActivity::class.java)
                            startActivity(loginIntent)
                            finish()
                        } else {
                            ////AppUtils.getAccessToken(context)
                            var homeIntent: Intent =
                                Intent(context, HomeListAppCompatActivity::class.java)
                            startActivity(homeIntent)
                            finish()
                        }
                }, 5000)
            }
        } else {
            AppUtils.showDialogAlertDismiss(
                context ,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred)
        }
    }
    private fun showDeviceIsRootedPopUp(mContext: Context) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        val icon = dialog.findViewById(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(R.drawable.round)
        icon.setImageResource(R.drawable.exclamationicon)
        val text = dialog.findViewById(R.id.textDialog) as TextView
        val textHead = dialog.findViewById(R.id.alertHead) as TextView
        text.text = "This app does not support rooted devices for security reasons."
        textHead.text = "Rooted Device Detected"
        val dialogButton = dialog.findViewById(R.id.btnOK) as Button
        dialogButton.setOnClickListener { dialog.dismiss() }
        //		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
        dialog.show()
    }

}