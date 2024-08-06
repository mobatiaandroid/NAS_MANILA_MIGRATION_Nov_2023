package com.mobatia.nasmanila.activities.home

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.GestureDetector
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.mobatia.nasmanila.BuildConfig
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.adapter.HomeListAdapter
import com.mobatia.nasmanila.activities.login.LoginActivity
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.DeviceRegistrtionmodel
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants.Companion.SETTINGS
import com.mobatia.nasmanila.fragments.about_us.AboutUsFragment
import com.mobatia.nasmanila.fragments.absence.AbsenceFragment
import com.mobatia.nasmanila.fragments.calendar.CalendarWebViewFragment
import com.mobatia.nasmanila.fragments.calendar.model.CalendarResponseModel
import com.mobatia.nasmanila.fragments.contact_us.ContactUsFragment
import com.mobatia.nasmanila.fragments.enrichment.CcaFragment
import com.mobatia.nasmanila.fragments.home.HomeScreenGuestUserFragment
import com.mobatia.nasmanila.fragments.home.HomeScreenRegisteredUserFragment2
import com.mobatia.nasmanila.fragments.notifications.NotificationsFragment
import com.mobatia.nasmanila.fragments.parent_essentials.ParentEssentialsFragment
import com.mobatia.nasmanila.fragments.parents_meeting.ParentsEveningFragment
import com.mobatia.nasmanila.fragments.settings.SettingsFragment
import com.mobatia.nasmanila.fragments.social_media.SocialMediaFragment
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeListAppCompatActivity:AppCompatActivity(), AdapterView.OnItemClickListener,
     OnItemLongClickListener,
     View.OnClickListener,
     GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {
    lateinit var mContext:Context
    val manager = supportFragmentManager
    var progressBarDialog: ProgressBarDialog? = null
    var linearLayout: LinearLayout? = null
    private var mHomeListView: ListView? = null
    private var mListAdapter: HomeListAdapter? = null
    private var mActivity: Activity? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var mDrawerLayout: DrawerLayout? = null
    lateinit var mListItemArray: Array<String>
    private var mListImgArray: TypedArray? = null
    private var mDetector: GestureDetector? = null
    private var mFragment: Fragment? = null
    lateinit var navigation_menu: ImageView
    var sPosition = 0
    var downarrow: ImageView? = null
    private val preLast = 0
    var notificationRecieved = 0
    var extras: Bundle? = null
    var imageButton: ImageView? = null
    var imageButton2: ImageView? = null
    private val PERMISSION_CALLBACK_CONSTANT_CALENDAR = 1
    private val PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE = 2
    private val PERMISSION_CALLBACK_CONSTANT_LOCATION = 3
    private val REQUEST_PERMISSION_CALENDAR = 101
    private val REQUEST_PERMISSION_EXTERNAL_STORAGE = 102
    private val REQUEST_PERMISSION_LOCATION = 103
    var permissionsRequiredCalendar = arrayOf(
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.WRITE_CALENDAR
    )
    var permissionsRequiredExternalStorage = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    var permissionsRequiredLocation = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private var calendarPermissionStatus: SharedPreferences? = null
    private var externalStoragePermissionStatus: SharedPreferences? = null
    private var locationPermissionStatus: SharedPreferences? = null
    private val calendarToSettings = false
    private val externalStorageToSettings = false
    private var locationToSettings = false
    var tabPositionProceed = 0
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_list)
        mContext=this
        mActivity = this
        progressBarDialog = ProgressBarDialog(mContext!!)
        calendarPermissionStatus = getSharedPreferences("calendarPermissionStatus", MODE_PRIVATE)
        externalStoragePermissionStatus =
            getSharedPreferences("externalStoragePermissionStatus", MODE_PRIVATE)
        locationPermissionStatus = getSharedPreferences("locationPermissionStatus", MODE_PRIVATE)

        extras = intent.extras
        if (AppUtils.checkInternet(mContext)) {
            if (PreferenceManager.getAccessToken(mContext).equals(""))
            {

            }else
            {
                callCalendarAPI()
              //  callDeviceReg()
            }

        }else{
            Toast.makeText(
                mContext,
                mContext.resources.getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (extras != null) {
            notificationRecieved = extras!!.getInt("Notification_Recieved", 0)
        } /*Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this,
                HomeListActivity.class));*/
        initialiseUI()
//showfragmenthome()
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            ActivityResultCallback<Boolean> { result ->
                if (result) {
                    // PERMISSION GRANTED
                    // Toast.makeText(mContext, String.valueOf(result), Toast.LENGTH_SHORT).show();
                } else {
                    // PERMISSION NOT GRANTED
                    val snackbar = Snackbar.make(
                        mDrawerLayout!!,
                        "Notification Permission Denied",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Settings") {
                            val intent = Intent()
                            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.putExtra("app_package", mContext.packageName)
                            intent.putExtra("app_uid", mContext.applicationInfo.uid)
                            intent.putExtra(
                                "android.provider.extra.APP_PACKAGE",
                                mContext.packageName
                            )
                            startActivity(intent)
                        }
                    snackbar.setActionTextColor(Color.RED)
                    val view = snackbar.view
                    val tv = view
                        .findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
                    tv.setTextColor(Color.WHITE)
                    snackbar.show()


                    // Toast.makeText(mContext, String.valueOf(result), Toast.LENGTH_SHORT).show();
                }
            }
        )
        askForNotificationPermission()
       /* FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                PreferenceManager.setFCMID(mContext, token)
            } else {
            }
        }*/
        initialSettings()

        if (notificationRecieved == 1) {
            displayView(0)
            displayView(2)

        } else {
            displayView(0)
        }
        if (AppUtils.checkInternet(mContext)) {

        }

    }

    private fun callDeviceReg() {

            progressBarDialog!!.show()
        val fToken = arrayOf("")
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        var device = manufacturer + model
        FirebaseApp.initializeApp(mContext)
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                fToken[0] = token
                Log.e("token", token)
                PreferenceManager.setFCMID(mContext!!, token)
            } else {
            }
        }
        val versionName: String = BuildConfig.VERSION_NAME

        var androidID = Settings.Secure.getString(this.contentResolver,
            Settings.Secure.ANDROID_ID)
        var loginbody = DeviceRegistrtionmodel(
            "2", PreferenceManager.getFCMID(mContext), device, versionName, androidID
        )

            val call: Call<ResponseBody> = ApiClient.getClient.deviceregistration("Bearer "+PreferenceManager.getAccessToken(mContext),loginbody)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val responseData = response.body()

                }




                override fun onFailure(call: Call<ResponseBody
                        >, t: Throwable) {
                    progressBarDialog!!.dismiss()
                }
            })
                }




    private fun callCalendarAPI() {
        val call: Call<CalendarResponseModel> = ApiClient.getClient.calender(
            "Bearer "+ PreferenceManager.getAccessToken(mContext)
        )
        progressBarDialog!!.show()
        call.enqueue(object : Callback<CalendarResponseModel> {
            override fun onResponse(call: Call<CalendarResponseModel>, response: Response<CalendarResponseModel>) {
                if (response.isSuccessful) {
                    if (response.body()!!.responsecode.equals("200")) {
                        progressBarDialog!!.dismiss()
                        var status_code = response.body()!!.response.statuscode
                        if (status_code.equals("303")) {

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
                    }else {
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
                else{
                    showSessionExpiredPopUp()
                }
            }

            override fun onFailure(call: Call<CalendarResponseModel>, t: Throwable) {
                progressBarDialog!!.dismiss()
            }

        })
    }
    private fun showSessionExpiredPopUp() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        val icon = dialog.findViewById<ImageView>(R.id.iconImageView)
        icon.setBackgroundResource(R.drawable.round)
        icon.setImageResource(R.drawable.exclamationicon)
        val text = dialog.findViewById<TextView>(R.id.textDialog)
        val textHead = dialog.findViewById<TextView>(R.id.alertHead)
        text.text = "You will now be logged out."
        textHead.text = "Session Expired"
        val dialogButton = dialog.findViewById<Button>(R.id.btnOK)
        dialogButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(mContext, LoginActivity::class.java)
            // PreferenceManager.setbackpresskey(mContext, "0")
            PreferenceManager.setAccessToken(mContext, "")
            PreferenceManager.setUserEmail(mContext,"")
            mContext.startActivity(intent)
            (mContext as Activity).finish()
        }
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
    private fun initialSettings() {

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_action_view_home)
        supportActionBar!!.elevation = 0f

        val view = supportActionBar!!.customView
        val toolbar = view.parent as Toolbar
        toolbar.setContentInsetsAbsolute(0, 0)

        //imageButton = view.findViewById<View>(R.id.action_bar_back) as ImageView
        navigation_menu = view.findViewById(R.id.action_bar_back)
        /*imageButton!!.setOnClickListener {
            val fm = supportFragmentManager
            val currentFragment = fm.findFragmentById(R.id.frame_container)
            println(
                "nas current fragment "
                        + currentFragment!!.javaClass.toString()
            )
            if (mDrawerLayout!!.isDrawerOpen(linearLayout!!)) {
                mDrawerLayout!!.closeDrawer(linearLayout!!)
            } else {
                mDrawerLayout!!.openDrawer(linearLayout!!)
            }
        }*/

        imageButton2 =
            view.findViewById<View>(R.id.action_bar_forward) as ImageView
        val logoClickImgView = view.findViewById<View>(R.id.logoClickImgView) as ImageView
        navigation_menu.setOnClickListener {
            if (mDrawerLayout!!.isDrawerOpen(linearLayout!!)) {
                mDrawerLayout!!.closeDrawer(linearLayout!!)
            } else {
                mDrawerLayout!!.openDrawer(linearLayout!!)
            }
        }
        logoClickImgView.setOnClickListener {
           /* if (mDrawerLayout!!.isDrawerOpen(linearLayout!!)) {
                mDrawerLayout!!.closeDrawer(linearLayout!!)
            }
            closeKeyBoard(mContext)
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            //mFragment = HomescreenFragment()
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)*/
            val fm = supportFragmentManager
            val currentFragment = fm.findFragmentById(R.id.frame_container)

            if (currentFragment!!.javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.about_us.AboutUsFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.absence.AbsenceFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.calendar.CalendarFragment",
                        ignoreCase = true
                    ) || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.calendar.CalendarWebViewFragment",
                        ignoreCase = true
                    ) || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.category1.CategoryMainFragment",
                        ignoreCase = true
                    ) || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.category2.CategoryFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.parentassociation.ParentAssociationsFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.cca.CcaFragment", ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.communications.CommunicationsFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.contact_us.ContactUsFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.gallery.GalleryFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.early_years.EarlyYearsFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.social_media.SocialMediaFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.ib_programme.IbProgrammeFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.nae_programmes.NaeProgrammesFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.notifications.NotificationsFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.notifications_new.NotificationsFragmentNew",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.parent_essentials.ParentEssentialsFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.performing_arts.PerformingArtsFragment",
                        ignoreCase = true
                    )
                || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.primary.PrimaryFragment",
                        ignoreCase = true
                    ) || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.secondary.SecondaryFragment",
                        ignoreCase = true
                    ) || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.sports.SportsFragment",
                        ignoreCase = true
                    ) || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.contact_us.ContactUsFragment",
                        ignoreCase = true
                    ) || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.nas_today.NasTodayFragment",
                        ignoreCase = true
                    ) || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.parents_evening.ParentsEveningFragment",
                        ignoreCase = true
                    ) || currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.settings.SettingsFragment",
                        ignoreCase = true
                    )||currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.enrichment.CcaFragment",
                        ignoreCase = true
                    )
                ||currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.parents_meeting.ParentsEveningFragment",
                        ignoreCase = true
                    )
            ) {
                //                    onBackPressed();
                displayView(0)
                if (PreferenceManager.getAccessToken(mContext).equals("")) {
                    imageButton2!!.setVisibility(View.GONE)
                } else {
                    imageButton2!!.setVisibility(View.VISIBLE)
                }
            }
        }
        if (PreferenceManager.getAccessToken(mContext).equals("")) {
            imageButton2!!.setVisibility(View.GONE)
        } else {
            imageButton2!!.setVisibility(View.VISIBLE)
        }
        imageButton2!!.setOnClickListener(View.OnClickListener {
            if (mDrawerLayout!!.isDrawerOpen(linearLayout!!)) {
                mDrawerLayout!!.closeDrawer(linearLayout!!)
            }
            val fm = supportFragmentManager
            val currentFragment = fm.findFragmentById(R.id.frame_container)
            println(
                "nas current fragment "
                        + currentFragment!!.javaClass.toString()
            )
            if (!(currentFragment
                    .javaClass
                    .toString()
                    .equals(
                        "class com.mobatia.nasmanila.fragments.settings.SettingsFragment",
                        ignoreCase = true
                    ))
            ) {
                mFragment = SettingsFragment()
                if (mFragment != null) {
                    val fragmentManager =
                        supportFragmentManager
                    fragmentManager.beginTransaction()
                        .add(R.id.frame_container, mFragment!!, "Settings")
                        .addToBackStack("Settings").commit()

                    mDrawerLayout!!.closeDrawer((linearLayout)!!)
                    supportActionBar!!.setTitle(R.string.null_value)

                }
                if (PreferenceManager.getAccessToken(mContext).equals("")) {
                    imageButton2!!.setVisibility(View.GONE)
                } else {
                    imageButton2!!.setVisibility(View.VISIBLE)
                }
            }
        })

       // mDrawerToggle!!.syncState()

    }

    private fun displayView(position: Int) {
        mFragment = null
        tabPositionProceed = position
        if (!PreferenceManager.getAccessToken(mContext).equals("")) {
            imageButton2!!.setVisibility(View.VISIBLE)
        } else {
        }

        if (PreferenceManager.getAccessToken(mContext).equals("")) {
            when (position) {
                0 -> {
                    // home
//                    mFragment = new HomeScreenGuestUserFragment(
//                            mListItemArray[position], mDrawerLayout, mHomeListView,
//                            mListItemArray, mListImgArray);
                    imageButton2!!.setVisibility(View.GONE)
                    mFragment = HomeScreenGuestUserFragment(
                        mListItemArray[position],
                        mListItemArray, mListImgArray!!
                    )
                    replaceFragmentsSelected(position)
                }

                1 -> {
                    // Notifications
                    Toast.makeText(mContext, "This feature is not available for Guest", Toast.LENGTH_SHORT).show()
                   /* imageButton2!!.setVisibility(View.VISIBLE)
                    mFragment = NotificationsFragment()
                    replaceFragmentsSelected(position)*/
                }

                2 -> {
                    //parent essentials
                    Toast.makeText(mContext, "This feature is not available for Guest", Toast.LENGTH_SHORT).show()

                    /* imageButton2!!.setVisibility(View.VISIBLE)
                     mFragment = ParentEssentialsFragment()
                     replaceFragmentsSelected(position)*/
                }

                3 -> {
                    //category 1
                    Toast.makeText(mContext, "This feature is not available for Guest", Toast.LENGTH_SHORT).show()

                    /*imageButton2!!.setVisibility(View.VISIBLE)
                    mFragment = CcaFragment()
                    replaceFragmentsSelected(position)*/
                }

                4 -> {
                    // social media
                    Toast.makeText(mContext, "This feature is not available for Guest", Toast.LENGTH_SHORT).show()

                    /* imageButton2!!.setVisibility(View.VISIBLE)
                     mFragment = SocialMediaFragment()
                     replaceFragmentsSelected(position)*/
                }

                5 -> {
                    // about us
                    Toast.makeText(mContext, "This feature is not available for Guest", Toast.LENGTH_SHORT).show()

                    /* imageButton2!!.setVisibility(View.VISIBLE)
                      mFragment = AboutUsFragment( )
                      replaceFragmentsSelected(position)*/
                }

                6 -> {
                    // contact us
                    Toast.makeText(mContext, "This feature is not available for Guest", Toast.LENGTH_SHORT).show()

                   /* imageButton2!!.setVisibility(View.VISIBLE)
                    if (ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        checkPermission()


                    }
                    else
                    {
                        mFragment = ContactUsFragment()
                        replaceFragmentsSelected(position)
                    }*/



            /*        mFragment = ContactUsFragment()
                    if (Build.VERSION.SDK_INT < 23) {
                        //Do not need to check the permission
                        replaceFragmentsSelected(position)
                    } else {
                        if (ActivityCompat.checkSelfPermission(
                                mActivity!!,
                                permissionsRequiredLocation[0]
                            ) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(
                                mActivity!!,
                                permissionsRequiredLocation[1]
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    mActivity!!,
                                    permissionsRequiredLocation[0]
                                )
                                || ActivityCompat.shouldShowRequestPermissionRationale(
                                    mActivity!!,
                                    permissionsRequiredLocation[1]
                                )
                            ) {
                                //Show Information about why you need the permission
                                val builder = AlertDialog.Builder(
                                    mActivity!!
                                )
                                builder.setTitle("Need Location Permission")
                                builder.setMessage("This module needs location permissions.")
                                builder.setPositiveButton(
                                    "Grant"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    ActivityCompat.requestPermissions(
                                        mActivity!!,
                                        permissionsRequiredLocation,
                                       PERMISSION_CALLBACK_CONSTANT_LOCATION
                                    )
                                }
                                builder.setNegativeButton(
                                    "Cancel"
                                ) { dialog, which -> dialog.cancel() }
                                builder.show()
                            } else if (locationPermissionStatus!!.getBoolean(
                                    permissionsRequiredLocation[0],
                                    false
                                )
                            ) {
                                //Previously Permission Request was cancelled with 'Dont Ask Again',
                                // Redirect to Settings after showing Information about why you need the permission
                                val builder = AlertDialog.Builder(
                                    mActivity!!
                                )
                                builder.setTitle("Need Location Permission")
                                builder.setMessage("This module needs location permissions.")
                                builder.setPositiveButton(
                                    "Grant"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = true

                                    //                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    //                                        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                                    //                                        intent.setData(uri);
                                    //                                        startActivityForResult(intent, REQUEST_PERMISSION_LOCATION);
                                    Toast.makeText(
                                        mContext,
                                        "Go to settings and grant access to location",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                builder.setNegativeButton(
                                    "Cancel"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = false
                                }
                                builder.show()
                            } else if (locationPermissionStatus!!.getBoolean(
                                    permissionsRequiredLocation[1],
                                    false
                                )
                            ) {
                                //Previously Permission Request was cancelled with 'Dont Ask Again',
                                // Redirect to Settings after showing Information about why you need the permission
                                val builder = AlertDialog.Builder(
                                    mActivity!!
                                )
                                builder.setTitle("Need Location Permission")
                                builder.setMessage("This module needs location permissions.")
                                builder.setPositiveButton(
                                    "Grant"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = true
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri =
                                        Uri.fromParts("package", mActivity!!.packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        REQUEST_PERMISSION_LOCATION
                                    )
                                    Toast.makeText(
                                        mContext,
                                        "Go to settings and grant access to location",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                builder.setNegativeButton(
                                    "Cancel"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = false
                                }
                                builder.show()
                            } else {

                                //just request the permission
//                        ActivityCompat.requestPermissions(mActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                                requestPermissions(
                                    permissionsRequiredLocation,
                                    PERMISSION_CALLBACK_CONSTANT_LOCATION
                                )
                            }
                            val editor = locationPermissionStatus!!.edit()
                            editor.putBoolean(permissionsRequiredLocation[0], true)
                            editor.commit()
                        } else {
                            replaceFragmentsSelected(position)
                        }
                    }*/
                }

                else -> {}
            }
        } else {
            when (position) {
                0 -> {

                    mFragment = HomeScreenRegisteredUserFragment2(
                        mListItemArray[position],
                        mDrawerLayout!!, mHomeListView!!, linearLayout!!,
                        mListItemArray, mListImgArray!!
                    )
                    replaceFragmentsSelected(position)
                }

                1 -> {
                    // Calendar
                    mFragment = CalendarWebViewFragment()
                    replaceFragmentsSelected(position)
                }

                2 -> {
                    // Notifications
                    mFragment = NotificationsFragment()
                    replaceFragmentsSelected(position)
                }

                3 -> {
                    // absence
                    mFragment = AbsenceFragment()
                    replaceFragmentsSelected(position)
                }

                4 -> {
                    //parent essentials
                    mFragment = ParentEssentialsFragment()
                    replaceFragmentsSelected(position)
                }

                5 -> {
                    // communications
                    mFragment = CcaFragment()
                    replaceFragmentsSelected(position)
                }

                6 -> {
                    // parents' evening
                    mFragment = ParentsEveningFragment()
                    replaceFragmentsSelected(position)
                }

                7 -> {
                    // social media
                    mFragment = SocialMediaFragment()
                    replaceFragmentsSelected(position)
                }

                8 -> {
                    // about us
                    mFragment = AboutUsFragment()
                    replaceFragmentsSelected(position)
                }

                9 -> {

                    if (ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                        checkPermission()


                    }
                    else
                    {
                        mFragment = ContactUsFragment()
                        replaceFragmentsSelected(position)
                    }
                    // contact us
                   /* mFragment = ContactUsFragment()
                    if (Build.VERSION.SDK_INT < 23) {
                        //Do not need to check the permission
                        replaceFragmentsSelected(position)
                    } else {
                        if (ActivityCompat.checkSelfPermission(
                                mActivity!!,
                                permissionsRequiredLocation[0]
                            ) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(
                                mActivity!!,
                                permissionsRequiredLocation[1]
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    mActivity!!,
                                    permissionsRequiredLocation[0]
                                )
                                || ActivityCompat.shouldShowRequestPermissionRationale(
                                    mActivity!!,
                                    permissionsRequiredLocation[1]
                                )
                            ) {
                                //Show Information about why you need the permission
                                val builder = AlertDialog.Builder(
                                    mActivity!!
                                )
                                builder.setTitle("Need Location Permission")
                                builder.setMessage("This module needs location permissions.")
                                builder.setPositiveButton(
                                    "Grant"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    ActivityCompat.requestPermissions(
                                        mActivity!!,
                                        permissionsRequiredLocation,
                                        PERMISSION_CALLBACK_CONSTANT_LOCATION
                                    )
                                }
                                builder.setNegativeButton(
                                    "Cancel"
                                ) { dialog, which -> dialog.cancel() }
                                builder.show()
                            } else if (locationPermissionStatus!!.getBoolean(
                                    permissionsRequiredLocation[0],
                                    false
                                )
                            ) {
                                //Previously Permission Request was cancelled with 'Dont Ask Again',
                                // Redirect to Settings after showing Information about why you need the permission
                                val builder = AlertDialog.Builder(
                                    mActivity!!
                                )
                                builder.setTitle("Need Location Permission")
                                builder.setMessage("This module needs location permissions.")
                                builder.setPositiveButton(
                                    "Grant"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = true
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri =
                                        Uri.fromParts("package", mActivity!!.packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        REQUEST_PERMISSION_LOCATION
                                    )
                                    Toast.makeText(
                                        mContext,
                                        "Go to settings and grant access to location",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                builder.setNegativeButton(
                                    "Cancel"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = false
                                }
                                builder.show()
                            } else if (locationPermissionStatus!!.getBoolean(
                                    permissionsRequiredLocation[1],
                                    false
                                )
                            ) {
                                //Previously Permission Request was cancelled with 'Dont Ask Again',
                                // Redirect to Settings after showing Information about why you need the permission
                                val builder = AlertDialog.Builder(
                                    mActivity!!
                                )
                                builder.setTitle("Need Location Permission")
                                builder.setMessage("This module needs location permissions.")
                                builder.setPositiveButton(
                                    "Grant"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = true
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri =
                                        Uri.fromParts("package", mActivity!!.packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        REQUEST_PERMISSION_LOCATION
                                    )
                                    Toast.makeText(
                                        mContext,
                                        "Go to settings and grant access to location",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                builder.setNegativeButton(
                                    "Cancel"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = false
                                }
                                builder.show()
                            } else {

                                //just request the permission
//                        ActivityCompat.requestPermissions(mActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                                ActivityCompat.requestPermissions(
                                    mActivity!!,
                                    permissionsRequiredLocation,
                                    PERMISSION_CALLBACK_CONSTANT_LOCATION
                                )
                            }
                            val editor = locationPermissionStatus!!.edit()
                            editor.putBoolean(permissionsRequiredLocation[0], true)
                            editor.commit()
                        } else {
                            replaceFragmentsSelected(position)
                        }
                    }*/
                }

                else -> {}
            }
        }
    }

    private fun initialiseUI() {
        mHomeListView = findViewById<View>(R.id.homeList) as ListView
        downarrow = findViewById<View>(R.id.downarrow) as ImageView
        linearLayout = findViewById<View>(R.id.linearLayout) as LinearLayout

        if (!PreferenceManager.getAccessToken(mContext).equals("")) {
            // registered user
            mListItemArray = mContext.resources.getStringArray(
                R.array.home_list_content_reg_items
            )
            mListImgArray = mContext.resources.obtainTypedArray(
                R.array.home_list_reg_icons
            )
        } else {
            // guest user
            mListItemArray = mContext.resources.getStringArray(
                R.array.home_list_content_guest_items
            )
            mListImgArray = mContext.resources.obtainTypedArray(
                R.array.home_list_guest_icons
            )
        }
        mListAdapter = HomeListAdapter(
            mContext, mListItemArray,
            mListImgArray!!, R.layout.custom_list_adapter, true
        )
        mHomeListView!!.setAdapter(mListAdapter)
        mHomeListView!!.setBackgroundColor(
            resources.getColor(
                R.color.split_bg
            )
        )
        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        /** change home list width */
        /** change home list width  */
        val width = (resources.displayMetrics.widthPixels / 1.8).toInt()
        val params = linearLayout!!
            .layoutParams as DrawerLayout.LayoutParams
        params.width = width
        linearLayout!!.layoutParams = params
        mHomeListView!!.setOnItemClickListener(this)
//        mHomeListView.setOnItemLongClickListener(this);
               mHomeListView!!.setOnItemLongClickListener(this);
        val firebaseID = PreferenceManager.getFCMID(mContext)
        //mDetector = GestureDetector(this)
     /*   mDrawerToggle = object : ActionBarDrawerToggle(
            mContext as Activity,
            mDrawerLayout, R.drawable.hamburgerbtn, R.string.null_value,
            R.string.null_value
        ) {
            fun onDrawerClosed(view: View?) {
                // int width = getResources().getDisplayMetrics().widthPixels /
                // 8;
                // DrawerLayout.LayoutParams params =
                // (android.support.v4.widget.DrawerLayout.LayoutParams)
                // mHomeListView
                // .getLayoutParams();
                // params.width = width;
                // mHomeListView.setLayoutParams(params);
                mDrawerLayout!!.setOnTouchListener(OnTouchListener { v, event ->
                    mDetector!!.onTouchEvent(
                        event
                    )
                })
                supportInvalidateOptionsMenu()
            }

            fun onDrawerOpened(drawerView: View?) {
                mDrawerLayout!!.setOnTouchListener(OnTouchListener { v, event ->
                    mDetector!!.onTouchEvent(
                        event
                    )
                })
                supportInvalidateOptionsMenu()
            }
        }*/

        //mDrawerLayout!!.setDrawerListener(mDrawerToggle)
       // mDrawerLayout!!.setOnTouchListener(OnTouchListener { v, event -> mDetector!!.onTouchEvent(event) })

        mHomeListView!!.setOnScrollListener(object : AbsListView.OnScrollListener {
            var mLastFirstVisibleItem = 0
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
            override fun onScroll(
                view: AbsListView,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if (view.id == mHomeListView!!.getId()) {
                    val currentFirstVisibleItem = mHomeListView!!.getLastVisiblePosition()
                    if (currentFirstVisibleItem == totalItemCount - 1) {
                        downarrow!!.visibility = View.INVISIBLE
                    } else {
                        downarrow!!.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (PreferenceManager.getIfHomeItemClickEnabled(mContext)) {
            println("Position homelist:$position")
            if (PreferenceManager.getAccessToken(mContext).equals("")) {
                imageButton2!!.setVisibility(View.GONE)
            } else {
                imageButton2!!.setVisibility(View.VISIBLE)
            }
            displayView(position)
        }
    }

  /*  override fun onDown(e: MotionEvent): Boolean {
        TODO("Not yet implemented")
        return false
    }

    override fun onShowPress(e: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        TODO("Not yet implemented")
        return false
    }*/

/*    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        TODO("Not yet implemented")
        return false
    }

    override fun onLongPress(e: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        TODO("Not yet implemented")
    }*/

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
      /*   if (position != 0) {
            // drag list view item
            PreferenceManager.setIfHomeItemClickEnabled(mContext, true)
            sPosition = position
            view!!.setBackgroundColor(Color.parseColor("#47C2D1"))
            val shadowBuilder = DragShadowBuilder(view)
            val data = ClipData.newPlainText("", "")
            view!!.startDrag(data, shadowBuilder, view, 0)
            view!!.visibility = View.VISIBLE
            mDrawerLayout!!.closeDrawer(linearLayout!!)
        } else {
            // if home in list view is selected
            AppUtils.showAlert(
                mContext as Activity,
                resources.getString(R.string.drag_impossible), "",
                resources.getString(R.string.ok), false
            )
            PreferenceManager.setIfHomeItemClickEnabled(mContext, true)
        }*/
        return false
    }

    override fun onClick(v: View?) {

        // settings
        mFragment = SettingsFragment()
        if (mFragment != null) {
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.frame_container, mFragment!!, SETTINGS)
                .addToBackStack(SETTINGS).commit()
            mDrawerLayout!!.closeDrawer(linearLayout!!)
            supportActionBar!!.setTitle(R.string.null_value)
            imageButton2!!.setVisibility(View.GONE)
        }
    }

    override fun onConnected(p0: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }
    private fun replaceFragmentsSelected(position: Int) {

        if (mFragment != null) {
            val fragmentManager = supportFragmentManager
            fragmentManager
                .beginTransaction()
                .replace(
                    R.id.frame_container, mFragment!!,
                    mListItemArray[position]
                )
                .addToBackStack(mListItemArray[position]).commitAllowingStateLoss()

            mHomeListView!!.setItemChecked(position, true)
            mHomeListView!!.setSelection(position)
            mDrawerLayout!!.closeDrawer(linearLayout!!)
            supportActionBar!!.setTitle(R.string.null_value)
          /*  if (drawer_layout.isDrawerOpen(linear_layout)) {
                drawer_layout.closeDrawer(linear_layout)
            }*/

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
     //   callDeviceReg()
        if (mDrawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout!!.closeDrawer(linearLayout!!)
        } else {
            if (supportFragmentManager.backStackEntryCount > 1) {
                val fm = supportFragmentManager
                val currentFragment = fm
                    .findFragmentById(R.id.frame_container)
                println(
                    "nas current fragment "
                            + currentFragment!!.javaClass.toString()
                )
                if ((currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.settings.SettingsFragment",
                            ignoreCase = true
                        ))
                ) {
                    if (PreferenceManager.getAccessToken(mContext).equals("")) {
                        imageButton2!!.setVisibility(View.GONE)
                    } else {
                        imageButton2!!.setVisibility(View.VISIBLE)
                    }
                } else {
                    if (PreferenceManager.getAccessToken(mContext).equals("")) {
                        imageButton2!!.setVisibility(View.GONE)
                    } else {
                        imageButton2!!.setVisibility(View.VISIBLE)
                    }
                }
                if ((currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.home.HomeScreenRegisteredUserFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.home.HomeScreenGuestUserFragment",
                            ignoreCase = true
                        ))
                ) {
                    if (PreferenceManager.getAccessToken(mContext).equals("")) {
                        imageButton2!!.setVisibility(View.GONE)
                    } else {
                        imageButton2!!.setVisibility(View.VISIBLE)
                    }
                    AppUtils.showAlert(
                        (mContext as Activity)!!, resources
                            .getString(R.string.exit_alert), resources
                            .getString(R.string.ok), resources
                            .getString(R.string.cancel), true
                    )
                } else if ((currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.aboutus.AboutUsFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.absence.AbsenceFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.calendar.CalendarFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.cca.CcaFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.parentassociation.ParentAssociationsFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.communications.CommunicationsFragment",
                            ignoreCase = true
                        ) || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.calendar.CalendarWebViewFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.contact_us.ContactUsFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.category1.CategoryMainFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.category2.CategoryFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.gallery.GalleryFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.early_years.EarlyYearsFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.social_media.SocialMediaFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.ib_programme.IbProgrammeFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.nae_programmes.NaeProgrammesFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.notifications.NotificationsFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.parent_essentials.ParentEssentialsFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.performing_arts.PerformingArtsFragment",
                            ignoreCase = true
                        )
                            || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.primary.PrimaryFragment",
                            ignoreCase = true
                        ) || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.secondary.SecondaryFragment",
                            ignoreCase = true
                        ) || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.sports.SportsFragment",
                            ignoreCase = true
                        ) || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.contact_us.ContactUsFragment",
                            ignoreCase = true
                        ) || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.nas_today.NasTodayFragment",
                            ignoreCase = true
                        ) || currentFragment
                        .javaClass
                        .toString()
                        .equals(
                            "class com.mobatia.nasmanila.fragments.parents_evening.ParentsEveningFragment",
                            ignoreCase = true
                        ))
                ) {
//                    imageButton.setImageResource(R.drawable.hamburgerbtn);
                    displayView(0)
                    if (PreferenceManager.getAccessToken(mContext).equals("")) {
                        imageButton2!!.setVisibility(View.GONE)
                    } else {
                        imageButton2!!.setVisibility(View.VISIBLE)
                    }
                    /*|| currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.nasmanila.fragments.settings.SettingsFragment")*/
                } else {
                    if (PreferenceManager.getAccessToken(mContext).equals("")) {
                        imageButton2!!.setVisibility(View.GONE)
                    } else {
                        imageButton2!!.setVisibility(View.VISIBLE)
                    }
                    supportFragmentManager.popBackStack()

                }
            } else {
                AppUtils.showAlert(
                    (mContext as Activity)!!, resources
                        .getString(R.string.exit_alert), resources
                        .getString(R.string.ok),
                    resources.getString(R.string.cancel), true
                )
            }
        }
    }
    private fun askForNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    fun showfragmenthome() {
        val transaction = manager.beginTransaction()
        val fragment = HomeScreenGuestUserFragment(
                        mListItemArray[0],
                        mListItemArray, mListImgArray!!
                    )
        transaction.replace(R.id.frameLayoutRoot, fragment)
        transaction.commit()
    }
    private fun closeKeyBoard(mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText){
            imm.hideSoftInputFromWindow((mContext as Activity).currentFocus!!.windowToken,0)
        }
    }
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
//            || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CALL_PHONE
//                    ,
//                    Manifest.permission.ACCESS_NOTIFICATION_POLICY
                ),
                123
            )
        }
    }
}