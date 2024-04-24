package com.mobatia.nasmanila.common.common_classes

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.login.LoginActivity
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AppUtils {
    var progressBarDialog: ProgressBarDialog? = null

    companion object {
      /*  fun getAccessToken(context: Context) {
            val call: Call<ResponseBody> = ApiClient.getClient.accessToken(
                PreferenceManager.getUserCode(context)!!
            )
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val responseData = response.body()
                    if (responseData != null) {
                        val jsonObject = JSONObject(responseData.string())
                        if (jsonObject != null) {
                            val accessToken: String = jsonObject.optString("authorization-user")
                            PreferenceManager.setAccessToken(context, accessToken)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }*/


      open fun showAlert(
          activity: Activity, message: String?,
          okBtnTitle: String?, cancelBtnTitle: String?, okBtnVisibility: Boolean
      ) {
          // custom dialog
          val dialog = Dialog(activity, R.style.NewDialog)
          dialog.setContentView(R.layout.custom_alert_dialog)
          dialog.setCancelable(false)

          // set the custom dialog components - text, image, button
          val text = dialog.findViewById<View>(R.id.text) as TextView
          text.text = message
          val sdk = Build.VERSION.SDK_INT
          val dialogCancelButton = dialog
              .findViewById<View>(R.id.dialogButtonCancel) as Button
          dialogCancelButton.text = cancelBtnTitle
          //		if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//			dialogCancelButton.setBackgroundDrawable(AppUtils
//					.getButtonDrawableByScreenCathegory(activity,
//							R.color.split_bg, R.color.list_selector));
//		} else {
//			dialogCancelButton.setBackground(AppUtils
//					.getButtonDrawableByScreenCathegory(activity,
//							R.color.split_bg, R.color.list_selector));
//		}
          // if button is clicked, close the custom dialog
          dialogCancelButton.setOnClickListener { dialog.dismiss() }
          val dialogOkButton = dialog
              .findViewById<View>(R.id.dialogButtonOK) as Button
          dialogOkButton.visibility = View.GONE
          dialogOkButton.text = okBtnTitle
          if (okBtnVisibility) {
              dialogOkButton.visibility = View.VISIBLE
              //			if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//				dialogOkButton.setBackgroundDrawable(AppUtils
//						.getButtonDrawableByScreenCathegory(activity,
//								R.color.split_bg, R.color.list_selector));
//			} else {
//				dialogOkButton.setBackground(AppUtils
//						.getButtonDrawableByScreenCathegory(activity,
//								R.color.split_bg, R.color.list_selector));
//			}
              // if button is clicked, close the custom dialog
              dialogOkButton.setOnClickListener {
                  dialog.dismiss()
                  activity.finish()
              }
          }
          dialog.show()
      }
        fun dateParsingToDdMmYyyy(date: String?): String? {
            var strCurrentDate = ""
            var format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var newDate: Date? = null
            try {
                newDate = format.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            format = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            strCurrentDate = format.format(newDate)
            return strCurrentDate
        }
        fun dateParsingToDdMmmYyyy(date: String?): String? {
            var strCurrentDate = ""
            var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            var newDate: Date? = null
            try {
                newDate = format.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            format = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH)
            strCurrentDate = format.format(newDate)
            return strCurrentDate
        }
        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = cm.activeNetworkInfo
            return if (ni == null) {
                // There are no active networks.
                false
            } else true
        }
        fun hideKeyBoard(context: Context) {
            val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                imm.hideSoftInputFromWindow(
                    (context as Activity).currentFocus!!.getWindowToken(), 0
                )
            }
        }
        fun getCurrentDateToday(): String? {
            val dateFormat: DateFormat =
                SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val calendar = Calendar.getInstance()
            return dateFormat.format(calendar.time)
        }
        fun dateConversionYToD(inputDate: String?): String? {
            var mDate = ""
            try {
                val date: Date
                val formatter: DateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
                date = formatter.parse(inputDate)
                //Subtracting 6 hours from selected time
                val time = date.time

                //SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
                val formatterFullDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                mDate = formatterFullDate.format(time)
            } catch (e: java.lang.Exception) {
            }
            return mDate
        }
        fun dateConversionY(inputDate: String?): String? {
            var mDate = ""
            try {
                val date: Date
                val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                date = formatter.parse(inputDate)
                //Subtracting 6 hours from selected time
                val time = date.time

                //SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
                val formatterFullDate = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
                mDate = formatterFullDate.format(time)
            } catch (e: Exception) {
            }
            return mDate
        }
        fun convertTimeToAMPM(date: String?): String? {
            var strCurrentDate = ""
            var format = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            var newDate: Date? = null
            try {
                newDate = format.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            format = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            strCurrentDate = format.format(newDate)
            return strCurrentDate
        }
        fun isEditTextFocused(context: Activity): Boolean {
            val inputManager = context.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            val focusedView = context.currentFocus
            /*
     * If no view is focused, an NPE will be thrown
     *
     * Maxim Dmitriev
     */return if (focusedView != null) {
                inputManager.hideSoftInputFromWindow(
                    focusedView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
                true
            } else {
                false
            }


            /*InputMethodManager imm = (InputMethodManager) context

				.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (imm.isAcceptingText()) {

			return true;


		}
		else
			return false;*/
        }
        fun showDialogAlertFinish(
            activity: Activity,
            msgHead: String?,
            msg: String?,
            ico: Int,
            bgIcon: Int
        ) {
            val dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_dialogue_ok_layout)
            val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
            icon.setBackgroundResource(bgIcon)
            icon.setImageResource(ico)
            val text = dialog.findViewById<View>(R.id.textDialog) as TextView
            val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
            text.text = msg
            textHead.text = msgHead
            val dialogButton = dialog.findViewById<View>(R.id.btnOK) as Button
            dialogButton.setOnClickListener {
                dialog.dismiss()
                activity.finish()
            }
            //		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
            dialog.show()
        }
        fun dateParsingTodd_MMM_yyyy(date: String?): String? {
            var strCurrentDate = ""
            var format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var newDate: Date? = null
            try {
                newDate = format.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            format = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            strCurrentDate = format.format(newDate)
            return strCurrentDate
        }
        fun showDialogAlertDismiss(
            context: Context?,
            msgHead: String?,
            msg: String?,
            ico: Int,
            bgIcon: Int
        ) {
            val dialog = Dialog(context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_dialogue_ok_layout)
            val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
            icon.setBackgroundResource(bgIcon)
            icon.setImageResource(ico)
            val text = dialog.findViewById<View>(R.id.textDialog) as TextView
            val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
            text.text = msg
            textHead.text = msgHead
            val dialogButton = dialog.findViewById<View>(R.id.btnOK) as Button
            dialogButton.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
        fun showSessionExpired(
            context: Context?,
            msgHead: String?,
            msg: String?,
            ico: Int,
            bgIcon: Int
        ) {
            val dialog = Dialog(context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_dialogue_ok_layout)
            val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
            icon.setBackgroundResource(bgIcon)
            icon.setImageResource(ico)
            val text = dialog.findViewById<View>(R.id.textDialog) as TextView
            val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
            text.text = msg
            textHead.text = msgHead
            val dialogButton = dialog.findViewById<View>(R.id.btnOK) as Button
            dialogButton.setOnClickListener {
                val intent = Intent(context, LoginActivity::class.java)
                // PreferenceManager.setbackpresskey(mContext, "0")
                PreferenceManager.setAccessToken(context!!, "")
                PreferenceManager.setUserEmail(context!!,"")
                context!!.startActivity(intent)
                (context as Activity).finish()
            }
            dialog.show()
        }

        fun checkInternet(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }

        fun getButtonDrawableByScreenCategory(
            context: Context?,
            normalStateResID: Int,
            pressedStateResID: Int
        ): Drawable {
            val stateNormal: Drawable = context?.resources?.getDrawable(normalStateResID)!!.mutate()
            val statePressed: Drawable =
                context?.resources?.getDrawable(pressedStateResID)!!.mutate()
            val drawable = StateListDrawable()
            drawable.addState(intArrayOf(android.R.attr.state_pressed), statePressed)
            drawable.addState(intArrayOf(android.R.attr.state_enabled), stateNormal)
            return drawable
        }

        fun hideKeyboard(context: Context?) {
//        if (editText != null) {
//            val imm = context
//                    ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(editText.windowToken, 0)
//        }
            val imm = context
                ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            if (imm.isAcceptingText) {

                imm.hideSoftInputFromWindow(
                    (context as Activity).currentFocus
                        ?.windowToken, 0
                )
            }
        }

        fun isValidEmail(string: String): Boolean {
            return return Patterns.EMAIL_ADDRESS.matcher(string).matches()
        }

        fun replace(s: String): String {
            return s.replace(" ".toRegex(), "%20")
        }


        fun durationInSecondsToString(sec: Int): CharSequence? {
            val hours: Int = sec / 3600
            val minutes: Int = sec / 60 - hours * 60
            val seconds: Int = sec - hours * 3600 - minutes * 60
            return String.format("%d:%02d:%02d", hours, minutes, seconds)
        }

        var progressBarDialog: ProgressBarDialog? = null
      /*  fun callLogoutApi(activity: Activity, dialog: Dialog) {
            val call: Call<ResponseBody> = ApiClient.getClient.logOut()
            progressBarDialog!!.show()
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressBarDialog!!.dismiss()
                    val responseData = response.body()
                    if (responseData != null) {
                        val jsonObject = JSONObject(responseData.string())
                        if (jsonObject.has("status")) {
                            val status = jsonObject.optInt("status")
                            if (status == 100) {
                                dialog.dismiss()
                                PreferenceManager.setUserID(activity,"")
                                val mIntent = Intent(activity, LoginActivity::class.java)
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                activity.startActivity(mIntent)
                            } else {
                                dialog.dismiss()
                                showDialogAlertDismiss(
                                    activity,
                                    "Alert",
                                    activity.getString(R.string.common_error),
                                    R.drawable.exclamationicon,
                                    R.drawable.round
                                )
                            }

                        }

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressBarDialog!!.dismiss()
                }

            })

        }*/

        fun showAlertFinish(
            activity: Activity, message: String?,
            okBtnTitle: String?, cancelBtnTitle: String?, okBtnVisibility: Boolean
        ) {
            // custom dialog
            val dialog = Dialog(activity, R.style.NewDialog)
            dialog.setContentView(R.layout.custom_alert_dialog)
            dialog.setCancelable(false)

            // set the custom dialog components - text, image, button
            val text = dialog.findViewById<View>(R.id.text) as TextView
            text.text = message
            val sdk = Build.VERSION.SDK_INT
            val dialogCancelButton = dialog
                .findViewById<View>(R.id.dialogButtonCancel) as Button
            dialogCancelButton.text = cancelBtnTitle
            //		if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//			dialogCancelButton.setBackgroundDrawable(AppUtils
//					.getButtonDrawableByScreenCathegory(activity,
//							R.color.split_bg, R.color.list_selector));
//		} else {
//			dialogCancelButton.setBackground(AppUtils
//					.getButtonDrawableByScreenCathegory(activity,
//							R.color.split_bg, R.color.list_selector));
//		}
            // if button is clicked, close the custom dialog
            dialogCancelButton.setOnClickListener {
                dialog.dismiss()
                activity.finish()
            }
            val dialogOkButton = dialog
                .findViewById<View>(R.id.dialogButtonOK) as Button
            dialogOkButton.visibility = View.GONE
            dialogOkButton.text = okBtnTitle
            if (okBtnVisibility) {
                dialogOkButton.visibility = View.VISIBLE
                //			if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//				dialogOkButton.setBackgroundDrawable(AppUtils
//						.getButtonDrawableByScreenCathegory(activity,
//								R.color.split_bg, R.color.list_selector));
//			} else {
//				dialogOkButton.setBackground(AppUtils
//						.getButtonDrawableByScreenCathegory(activity,
//								R.color.split_bg, R.color.list_selector));
//			}
                // if button is clicked, close the custom dialog
                dialogOkButton.setOnClickListener {
                    dialog.dismiss()
                    activity.finish()
                }
            }
            dialog.show()
        }
    }
}