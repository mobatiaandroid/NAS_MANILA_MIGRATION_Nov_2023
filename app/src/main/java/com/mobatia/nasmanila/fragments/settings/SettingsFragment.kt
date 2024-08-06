package com.mobatia.nasmanila.fragments.settings

import android.app.Activity
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mobatia.nasmanila.BuildConfig
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.login.LoginActivity
import com.mobatia.nasmanila.activities.terms_of_service.TermsOfServiceActivity
import com.mobatia.nasmanila.activities.tutorial.TutorialActivity
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.settings.adapter.CustomSettingsAdapter
import com.mobatia.nasmanila.fragments.settings.model.ChangePasswordResponseModel
import com.mobatia.nasmanila.fragments.settings.model.ChangepasswordApiModel
import com.mobatia.nasmanila.fragments.settings.model.LogoutApiModel
import com.mobatia.nasmanila.fragments.settings.model.LogoutResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SettingsFragment() : Fragment() {

    private var mRootView: View? = null
    private var mContext: Context? = null
    var progressBarDialog: ProgressBarDialog? = null
    private var mSettingsList: ListView? = null
    private val mTitle: String? = null
    private val mTabId: String? = null
    private var relMain: RelativeLayout? = null
    private val mBannerImage: ImageView? = null
    private var versionText: TextView? = null
    var mTitleTextView: TextView? = null
    var text_currentpswd: EditText? = null
    var newpassword: EditText? = null
    var confirmpassword: EditText? = null
    var isRegUser = false
    var dialog: Dialog? = null
    private val PASSWORD_PATTERN = "^" +
            "(?=.*[@#$%!^&+=])" +  // at least 1 special character
            "(?=\\S+$)" +  // no white spaces
            ".{8,}" +  // at least 8 characters
            "$"
    var PASSWORD_PATTERN3="^" +
            ".{8,}"
    var mSettingsListArray: ArrayList<String?> = object : ArrayList<String?>() {
        init {
            add("Change App Settings")
            add("Terms of Service")
            add("Email Us")
            add("Tutorial")
            add("Logout")
        }
    }
    var mSettingsListArrayRegistered: ArrayList<String?> = object : ArrayList<String?>() {
        init {
            add("Change App Settings")
            add("Terms of Service")
            add("Email Us")
            add("Tutorial")
            add("Change Password")
            add("Delete My Account")
            add("Logout")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_settings_list, container,
            false
        )
        mContext = activity
        progressBarDialog = ProgressBarDialog(mContext!!)
        dialog = Dialog(mContext!!, R.style.NewDialog)
        initialiseUI()
        return mRootView
    }

    private fun initialiseUI() {
        mTitleTextView = mRootView!!.findViewById(R.id.titleTextView) as TextView
        versionText = mRootView!!.findViewById(R.id.versionText) as TextView
        mSettingsList = mRootView!!.findViewById(R.id.mSettingsListView) as ListView
        relMain = mRootView!!.findViewById(R.id.relMain) as RelativeLayout
        relMain!!.setOnClickListener { }
        mTitleTextView!!.text = NaisClassNameConstants.SETTINGS
        val versionName: String = BuildConfig.VERSION_NAME
        if (ApiClient.BASE_URL.contains("mobatia")) {
            versionText!!.text = "DEV v$versionName"
        } else {
            versionText!!.text = "v" + versionName
        }

        if (PreferenceManager.getAccessToken(mContext!!) == "") {
            isRegUser = false
            mSettingsList!!.adapter = CustomSettingsAdapter(requireActivity(), mSettingsListArray)
        } else {
            isRegUser = true
            mSettingsList!!.adapter =
                CustomSettingsAdapter(requireActivity(), mSettingsListArrayRegistered)
        }
        mSettingsList!!.setOnItemClickListener { parent, view, position, id ->
            if (isRegUser) {
                when(position) {
                    0 -> {
                        PreferenceManager.setGoToSettings(mContext!!, "1")

                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        mContext!!.startActivity(intent)
                    }
                    1 -> {
                        val mIntent = Intent(mContext, TermsOfServiceActivity::class.java)
                        mIntent.putExtra("tab_type", mSettingsListArray[position].toString())
                        mContext!!.startActivity(mIntent)
                    }
                    2 -> {
                        val to = "appsupport@naismanila.edu.ph"
                        val email = Intent(Intent.ACTION_SEND)
                        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
                        email.type = "message/rfc822"
                        startActivity(Intent.createChooser(email, "Choose an Email client :"))
                    }
                    3 -> {
                        val mIntent = Intent(mContext, TutorialActivity::class.java)
                        mIntent.putExtra("type", 0)
                        mContext!!.startActivity(mIntent)
                    }
                    4 -> {
                        if (AppUtils.checkInternet(mContext!!)) {
                            showChangePasswordAlert()
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?,
                                "Network Error",
                                getString(R.string.no_internet),
                                R.drawable.nonetworkicon,
                                R.drawable.roundred
                            )
                        }
                    }
                    5 -> {
                        if (AppUtils.isNetworkConnected(mContext!!)) {
                           showDialogAlertDeleteaccount(
                               mContext!!,
                                "Confirm?",
                                "Do you want to delete your account?",
                                R.drawable.questionmark_icon,
                                R.drawable.round
                            )
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?,
                                "Network Error",
                                getString(R.string.no_internet),
                                R.drawable.nonetworkicon,
                                R.drawable.roundred
                            )
                        }
                    }
                    6 -> {
                        if (AppUtils.checkInternet(mContext!!)) {
                            showDialogAlertLogout(
                                mContext!!,
                                "Confirm?",
                                "Do you want to Logout?",
                                R.drawable.questionmark_icon,
                                R.drawable.round
                            )
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?,
                                "Network Error",
                                getString(R.string.no_internet),
                                R.drawable.nonetworkicon,
                                R.drawable.roundred
                            )
                        }
                    }
                }
            } else {
                when (position) {
                    0 -> {
                        PreferenceManager.setGoToSettings(mContext!!, "1")
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        mContext!!.startActivity(intent)
                    }
                    1 -> {
                        Toast.makeText(mContext, "This feature is not available for Guest", Toast.LENGTH_SHORT).show()

                        /* val mIntent = Intent(mContext, TermsOfServiceActivity::class.java)
                         mIntent.putExtra("tab_type", mSettingsListArray[position].toString())
                         mContext!!.startActivity(mIntent)*/
                    }
                    2 -> {
                        val emailIntent = Intent(
                            Intent.ACTION_SEND_MULTIPLE
                        )
                        val deliveryAddress = arrayOf("appsupport@naismanila.edu.ph")
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress)
                        emailIntent.type = "text/plain"
                        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        val pm = mContext!!.packageManager
                        val activityList = pm.queryIntentActivities(
                            emailIntent, 0
                        )
                        println("packge size" + activityList.size)
                        for (app in activityList) {
                            println("packge name" + app.activityInfo.name)
                            if (app.activityInfo.name.contains("com.google.android.gm")) {
                                val activity = app.activityInfo
                                val name = ComponentName(
                                    activity.applicationInfo.packageName, activity.name
                                )
                                emailIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                                emailIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                                        or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                emailIntent.component = name
                                mContext!!.startActivity(emailIntent)
                                break
                            }
                        }
                    }
                    3 -> {
                        val mIntent = Intent(mContext, TutorialActivity::class.java)
                        mIntent.putExtra("type", 0)

                        mContext!!.startActivity(mIntent)
                    }
                    4 -> {
                        showDialogAlertLogout(
                            mContext!!,
                            "Confirm?",
                            "Do you want to Logout?",
                            R.drawable.questionmark_icon,
                            R.drawable.round
                        )
                    }

                }
            }
        }
    }
    fun String.onlyLetters() = all { it.isLetter() }
    private fun showChangePasswordAlert() {
        dialog = Dialog(mContext!!, R.style.NewDialog)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.custom_dialog_changepassword)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(true)
        val sdk = Build.VERSION.SDK_INT
        text_currentpswd = dialog!!.findViewById(R.id.text_currentpassword)
        newpassword = dialog!!.findViewById(R.id.text_currentnewpassword)  as EditText
        confirmpassword = dialog!!.findViewById(R.id.text_confirmpassword)  as EditText

        val dialogSubmitButton = dialog!!.findViewById(R.id.btn_changepassword) as Button
        text_currentpswd!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, m: MotionEvent): Boolean {
                // Perform tasks here
                text_currentpswd!!.isFocusable =true
                text_currentpswd!!.isFocusableInTouchMode =true
                return false
            }
        })
        newpassword!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, m: MotionEvent): Boolean {
                // Perform tasks here
                newpassword!!.isFocusable =true
                newpassword!!.isFocusableInTouchMode =true
                return false
            }
        })
        confirmpassword!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, m: MotionEvent): Boolean {
                // Perform tasks here
                confirmpassword!!.isFocusable =true
                confirmpassword!!.isFocusableInTouchMode =true
                return false
            }
        })
        dialogSubmitButton.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
//            appUtils.hideKeyboard(mContext, text_currentpswd)
//            appUtils.hideKeyboard(mContext, confirmpassword)
            if (text_currentpswd!!.text.toString().trim { it <= ' ' }.isEmpty()) {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_current_password),
                    R.drawable.infoicon,
                    R.drawable.round
                )
            } else if (newpassword!!.text.toString().trim { it <= ' ' }.isEmpty()) {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_new_password),
                    R.drawable.infoicon,
                    R.drawable.round
                )

            } else if (confirmpassword!!.text.toString().trim { it <= ' ' }.isEmpty()) {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_confirm_password),
                    R.drawable.infoicon,
                    R.drawable.round
                )

            } else if (newpassword!!.text.toString()
                    .trim { it <= ' ' } != confirmpassword!!.text.toString()
                    .trim { it <= ' ' }
            ) {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    getString(R.string.alert_heading),
                    getString(R.string.password_mismatch),
                    R.drawable.infoicon,
                    R.drawable.round
                )
            } else {
                if (newpassword!!.text.toString().trim().equals("")) {
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        getString(R.string.alert_heading),
                        "Please enter New Password",
                        R.drawable.infoicon,
                        R.drawable.round
                    )

                } else {
                    if (confirmpassword!!.text.toString().trim().equals("")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.alert_heading),
                            "Please enter Confirm Password",
                            R.drawable.infoicon,
                            R.drawable.round
                        )

                    } else {
                        if (newpassword!!.getText().toString().trim { it <= ' ' }
                                .matches(PASSWORD_PATTERN.toRegex()) && confirmpassword!!.getText()
                                .toString().trim { it <= ' ' }
                                .matches(PASSWORD_PATTERN.toRegex())
                        ) {

                            if (AppUtils.checkInternet(mContext!!)) {
                                callChangePasswordAPI()
                            } else {
                                AppUtils.showDialogAlertDismiss(
                                    mContext as Activity?,
                                    "Network Error",
                                    getString(R.string.no_internet),
                                    R.drawable.nonetworkicon,
                                    R.drawable.roundred
                                )
                            }

                        } else {
                            if (!newpassword!!.getText().toString().onlyLetters() &&
                                !confirmpassword!!.getText()
                                    .toString().onlyLetters()
                            ) {

                                if (!newpassword!!.text.toString()
                                        .contains(" ") &&
                                    !confirmpassword!!.getText()
                                        .toString()
                                        .contains(" ")
                                ) {

                                    if (newpassword!!.text.toString().trim()
                                            .matches(PASSWORD_PATTERN3.toRegex()) &&
                                        confirmpassword!!.getText()
                                            .toString().trim { it <= ' ' }
                                            .matches(PASSWORD_PATTERN3.toRegex())
                                    ) {
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Password must contain atleast 8 characters",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Password must not contain white spaces",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                            } else {
                                Toast.makeText(
                                    context,
                                    "Password must contain atleast 1 special character",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                        /*  if (AppUtils.checkInternet(mContext!!)) {
                    callChangePasswordAPI()
                } else {
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        "Network Error",
                        getString(R.string.no_internet),
                        R.drawable.nonetworkicon,
                        R.drawable.roundred
                    )
                }*/
                    }
                }
            }
        }
                val dialogCancel = dialog!!.findViewById(R.id.btn_cancel) as Button
                dialogCancel.setOnClickListener {
                    AppUtils.hideKeyboard(mContext)

                    dialog!!.dismiss()
                }
                dialog!!.show()

        }

    private fun callChangePasswordAPI() {
        progressBarDialog!!.show()
        var student = ChangepasswordApiModel(
            text_currentpswd!!.getText().toString(),
            newpassword!!.getText().toString(),
            PreferenceManager.getUserEmail(mContext!!).toString(),
            PreferenceManager.getFCMID(mContext),
            "2"
        )
        val call: Call<ChangePasswordResponseModel> = ApiClient.getClient.changepassword(
            "Bearer " + PreferenceManager.getAccessToken(mContext),
            student
        )
        call.enqueue(object : Callback<ChangePasswordResponseModel> {
            override fun onResponse(
                call: Call<ChangePasswordResponseModel>,
                response: Response<ChangePasswordResponseModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {

                        dialog!!.dismiss()

                        showDialogSignUpAlert(
                            mContext!!,
                            "Success",
                            getString(R.string.succ_pswd),
                            R.drawable.tick,
                            R.drawable.round
                        )


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

            override fun onFailure(call: Call<ChangePasswordResponseModel>, t: Throwable) {

            }
        })
    }

    private fun showDialogSignUpAlert(
        activity: Context,
        msgHead: String,
        msg: String,
        ico: Int,
        bgIcon: Int
    ) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        val icon = dialog.findViewById(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text:TextView = dialog.findViewById(R.id.textDialog)
        val textHead = dialog.findViewById(R.id.alertHead) as TextView
        text.text = msg
        textHead.text = msgHead

        val dialogButton = dialog.findViewById(R.id.btnOK) as Button
        dialogButton.setOnClickListener {
            PreferenceManager.setAccessToken(mContext!!, "")
            dialog.dismiss()
            /*val mIntent = Intent(activity, LoginActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mContext!!.startActivity(mIntent)*/
        }
        dialog.show()
    }
    fun showDialogAlertLogout(
        activity: Context,
        s: String,
        s1: String,
        questionMarkIcon: Int,
        round: Int
    ) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        val icon = dialog.findViewById(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(round)
        icon.setImageResource(questionMarkIcon)
        val text = dialog.findViewById(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById(R.id.alertHead) as TextView
        text.text = s1
        textHead.text = s

        val dialogButton = dialog.findViewById(R.id.btn_Ok) as Button
        dialogButton.setOnClickListener {
            if (PreferenceManager.getAccessToken(activity)
                    .equals("", ignoreCase = true)
            ) {
                PreferenceManager.setAccessToken(activity, "")
                dialog.dismiss()
                val mIntent = Intent(activity, LoginActivity::class.java)
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                activity.startActivity(mIntent)
            } else {
                if (AppUtils.checkInternet(mContext!!)) {
                    callLogoutApi(activity, dialog)
                }else{
                    Toast.makeText(
                        mContext,
                        mContext!!.resources.getString(R.string.no_internet),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
        val dialogButtonCancel = dialog.findViewById(R.id.btn_Cancel) as Button
        dialogButtonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
fun showDialogAlertDeleteaccount(
    activity: Context,
    s: String,
    s1: String,
    questionMarkIcon: Int,
    round: Int
) {
    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.alert_dialogue_layout)
    val icon = dialog.findViewById(R.id.iconImageView) as ImageView
    icon.setBackgroundResource(round)
    icon.setImageResource(questionMarkIcon)
    val text = dialog.findViewById(R.id.text_dialog) as TextView
    val textHead = dialog.findViewById(R.id.alertHead) as TextView
    text.text = s1
    textHead.text = s

    val dialogButton = dialog.findViewById(R.id.btn_Ok) as Button
    dialogButton.setOnClickListener {
        if (PreferenceManager.getAccessToken(activity)
                .equals("")
        ) {
            PreferenceManager.setAccessToken(activity, "")
            dialog.dismiss()
            val mIntent = Intent(activity, LoginActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(mIntent)
        } else {
            if (AppUtils.checkInternet(mContext!!)) {
                calldeleteaccountapi(activity, dialog)
            }else{
                Toast.makeText(
                    mContext,
                    mContext!!.resources.getString(R.string.no_internet),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
    val dialogButtonCancel = dialog.findViewById(R.id.btn_Cancel) as Button
    dialogButtonCancel.setOnClickListener { dialog.dismiss() }
    dialog.show()

}
    fun calldeleteaccountapi(activity: Context, dialog: Dialog) {
        progressBarDialog!!.show()

        //VolleyWrapper volleyWrapper = new VolleyWrapper(URL_LOGOUT);
        val fToken = arrayOf("")
        /*  FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
              if (!TextUtils.isEmpty(token)) {
                  fToken[0] = token
                  PreferenceManager.setFCMID(mContext!!, token)
              } else {
              }
          }*/
        var logoutmodel = LogoutApiModel("", "2")
        val call: Call<LogoutResponseModel> = ApiClient.getClient.delete_account(
            "Bearer " + PreferenceManager.getAccessToken(mContext),
            logoutmodel
        )
        call.enqueue(object : Callback<LogoutResponseModel> {
            override fun onResponse(
                call: Call<LogoutResponseModel>,
                response: Response<LogoutResponseModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {

                        dialog.dismiss()
                        PreferenceManager.setAccessToken(activity, "")
                        val mIntent = Intent(activity, LoginActivity::class.java)
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        activity.startActivity(mIntent)

                    }  else {
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

            override fun onFailure(call: Call<LogoutResponseModel>, t: Throwable) {

            }
        })
    }
    private fun callLogoutApi(activity: Context, dialog: Dialog) {
        progressBarDialog!!.show()

        //VolleyWrapper volleyWrapper = new VolleyWrapper(URL_LOGOUT);
        val fToken = arrayOf("")
      /*  FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                fToken[0] = token
                PreferenceManager.setFCMID(mContext!!, token)
            } else {
            }
        }*/
        var logoutmodel = LogoutApiModel("", "2")
        val call: Call<LogoutResponseModel> = ApiClient.getClient.logout(
            "Bearer " + PreferenceManager.getAccessToken(mContext),
            logoutmodel
        )
        call.enqueue(object : Callback<LogoutResponseModel> {
            override fun onResponse(
                call: Call<LogoutResponseModel>,
                response: Response<LogoutResponseModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {

                        dialog.dismiss()
                        PreferenceManager.setAccessToken(activity, "")
                        val mIntent = Intent(activity, LoginActivity::class.java)
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        activity.startActivity(mIntent)

                    }  else {
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

            override fun onFailure(call: Call<LogoutResponseModel>, t: Throwable) {

            }
        })
    }
}