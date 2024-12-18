package com.mobatia.nasmanila.fragments.home

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
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnDragListener
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.mobatia.nasmanila.BuildConfig
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.activities.login.LoginActivity
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.DeviceRegistrtionmodel
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.common.constants.NaisTabConstants
import com.mobatia.nasmanila.fragments.about_us.AboutUsFragment
import com.mobatia.nasmanila.fragments.absence.AbsenceFragment
import com.mobatia.nasmanila.fragments.calendar.CalendarWebViewFragment
import com.mobatia.nasmanila.fragments.contact_us.ContactUsFragment
import com.mobatia.nasmanila.fragments.enrichment.CcaFragment
import com.mobatia.nasmanila.fragments.home.adapter.ImagePagerDrawableAdapter
import com.mobatia.nasmanila.fragments.home.model.HomeBannerApiModel
import com.mobatia.nasmanila.fragments.home.model.HomeBannerModel
import com.mobatia.nasmanila.fragments.nas_today.NasTodayFragment
import com.mobatia.nasmanila.fragments.notifications.NotificationsFragment
import com.mobatia.nasmanila.fragments.parent_essentials.ParentEssentialsFragment
import com.mobatia.nasmanila.fragments.parents_meeting.ParentsEveningFragment
import com.mobatia.nasmanila.fragments.social_media.SocialMediaFragment
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeScreenRegisteredUserFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
lateinit var rootView: View
private var mContext: Context? = null
lateinit var homeActivity: HomeListAppCompatActivity
var bannerImagePager: ViewPager? = null
var progressBarDialog: ProgressBarDialog? = null
private var isDraggable = false
var currentPage = 0
private var TAB_ID: String? = null
private var INTENT_TAB_ID: String? = null
var intentTabIdToProceed = ""
var versionName = ""
var versionCode = -1
private var viewTouched: View? = null
private lateinit var mSectionText: Array<String?>
private lateinit var homeBannerUrlImageArray: ArrayList<String>
private var mRelOne: RelativeLayout? = null
private var mRelTwo: RelativeLayout? = null
private var mRelThree: RelativeLayout? = null
private var mRelFour: RelativeLayout? = null
private var mRelFive: RelativeLayout? = null
private var mRelSix: RelativeLayout? = null
private var mRelSeven: RelativeLayout? = null
private var mRelEight: RelativeLayout? = null
private var mRelNine: RelativeLayout? = null

private var mTxtOne: TextView? = null
private var mTxtTwo: TextView? = null
private var mTxtThree: TextView? = null
private var mTxtFour: TextView? = null
private var mTxtFive: TextView? = null
private var mTxtSix: TextView? = null
private var mTxtSeven: TextView? = null
private var mTxtEight: TextView? = null
private var mTxtNine: TextView? = null
private var mImgOne: ImageView? = null
private var mImgTwo: ImageView? = null
private var mImgThree: ImageView? = null
private var mImgFour: ImageView? = null
private var mImgFive: ImageView? = null
private var mImgSix: ImageView? = null
private var mImgSeven: ImageView? = null
private var mImgEight: ImageView? = null
private var mImgNine: ImageView? = null
private var android_app_version: String? = null
private val PERMISSION_CALLBACK_CONSTANT_CALENDAR = 1
private val PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE = 2
private val PERMISSION_CALLBACK_CONSTANT_LOCATION = 3
private val REQUEST_PERMISSION_CALENDAR = 101
private val REQUEST_PERMISSION_EXTERNAL_STORAGE = 102
private val REQUEST_PERMISSION_LOCATION = 103
private val calendarToSettings = false
private val externalStorageToSettings = false
private var locationToSettings = false
lateinit var listItemArray: Array<String>
lateinit var listImageArray: TypedArray

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


lateinit var textMessage:TextView
lateinit var textHead:TextView
class HomeScreenRegisteredUserFragment2( s: String,
                                         mDrawerLayout: DrawerLayout,
                                         mHomeListView: ListView,
                                         mLinearLayout: LinearLayout,
                                         mListItemArray: Array<String>,
                                         mListImgArray: TypedArray) : Fragment() {
    var title: String = s
    var drawerLayout: DrawerLayout? = mDrawerLayout
    var listView: ListView? = mHomeListView
   // var listItemArray: Array<String> = mListItemArray
    var linearLayout: LinearLayout? = mLinearLayout
    //var listImageArray: TypedArray = mListImgArray
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home_screen_registered_user, container, false)
        homeActivity =  activity as HomeListAppCompatActivity
        mContext = activity
        calendarPermissionStatus =requireActivity().getSharedPreferences(
            "calendarPermissionStatus",
            Context.MODE_PRIVATE
        )
        externalStoragePermissionStatus = requireActivity().getSharedPreferences(
            "externalStoragePermissionStatus",
            Context.MODE_PRIVATE
        )
        locationPermissionStatus = requireActivity().getSharedPreferences(
            "locationPermissionStatus",
            Context.MODE_PRIVATE
        )
        listItemArray = mContext!!.resources.getStringArray(
            R.array.home_list_content_reg_items
        )
        listImageArray = mContext!!.resources.obtainTypedArray(
            R.array.home_list_reg_icons
        )
        initialiseUI()
        setListeners()
        setDragListenersForButtons()
        getButtonBgAndTextImages()
        return rootView
    }

    private fun setDragListenersForButtons() {
        mRelOne!!.setOnDragListener(DropListener())
        mRelTwo!!.setOnDragListener(DropListener())
        mRelThree!!.setOnDragListener(DropListener())
        mRelFour!!.setOnDragListener(DropListener())
        mRelFive!!.setOnDragListener(DropListener())
        mRelSix!!.setOnDragListener(DropListener())
        mRelSeven!!.setOnDragListener(DropListener())
        mRelEight!!.setOnDragListener(DropListener())
        mRelNine!!.setOnDragListener(DropListener())
    }
    private class DropListener : OnDragListener {
        /*
		 * (non-Javadoc)
		 *
		 * @see android.view.View.OnDragListener#onDrag(android.view.View,
		 * android.view.DragEvent)
		 */
        override fun onDrag(view: View, event: DragEvent): Boolean {
            PreferenceManager.setIfHomeItemClickEnabled(mContext!!, true)
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {}
                DragEvent.ACTION_DRAG_ENTERED -> {}
                DragEvent.ACTION_DRAG_EXITED -> {}
                DragEvent.ACTION_DROP -> {
                    //				AppController.mDrawerLayouts.closeDrawer(AppController.mListViews);
                    //mDrawerLayout.closeDrawer(mLinearLayouts)
                    val arr = IntArray(2)
                    view.getLocationInWindow(arr)
                    val x = arr[0]
                    val y = arr[1]
                    getButtonViewTouched(x.toFloat(), y.toFloat())
                    mSectionText[0] =
                        mTxtOne!!.getText().toString().uppercase(Locale.getDefault())
                    mSectionText[1] =
                        mTxtTwo!!.getText().toString().uppercase(Locale.getDefault())
                    mSectionText[2] =
                        mTxtThree!!.getText().toString().uppercase(Locale.getDefault())
                    mSectionText[3] =
                        mTxtFour!!.getText().toString().uppercase(Locale.getDefault())
                    mSectionText[4] =
                        mTxtFive!!.getText().toString().uppercase(Locale.getDefault())
                    mSectionText[5] =
                        mTxtSix!!.getText().toString().uppercase(Locale.getDefault())
                    mSectionText[6] =
                        mTxtSeven!!.getText().toString().uppercase(Locale.getDefault())
                    mSectionText[7] =
                        mTxtEight!!.getText().toString().uppercase(Locale.getDefault())
                    mSectionText[8] =
                        mTxtNine!!.getText().toString().uppercase(Locale.getDefault())
                    var i = 0
                    while (i < mSectionText.size) {
                        isDraggable = true
                        if (mSectionText.get(i)
                                .equals(
                                    listItemArray.get(HomeListAppCompatActivity().sPosition),
                                    ignoreCase = true
                                )
                        ) {
                            isDraggable = false
                            break
                        }
                        i++
                    }
                    if (isDraggable) {
                        getButtonDrawablesAndText(
                            viewTouched!!,
                            HomeListAppCompatActivity().sPosition
                        )
                    } else {
                        AppUtils.showAlert(
                            (mContext as Activity?)!!, mContext!!
                                .getResources().getString(R.string.drag_duplicate),
                            "",
                            mContext!!.getResources().getString(R.string.ok),
                            false
                        )
                    }
                }

                DragEvent.ACTION_DRAG_ENDED -> {}
                else -> {}
            }
            return true
        }

        private fun getButtonDrawablesAndText(v:View, position: Int) {
            if (position != 0) {
                if (v === mRelOne) {
                    val sdk = Build.VERSION.SDK_INT
                    if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                        mImgOne!!.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.news))
                    } else {
                        mImgOne!!.background = mContext!!.resources.getDrawable(R.drawable.news)
                    }
                    mTxtOne!!.text = "NAIS TODAY"
                    getTabId(NaisTabConstants.TAB_NAS_TODAY)
                    PreferenceManager.setButtonOneTabId(mContext!!, TAB_ID)
                    setBackgroundColorForView(listItemArray.get(position), mRelOne!!)
                    PreferenceManager.setButtonOneTextImage(
                        mContext!!,
                        Integer.toString(position)
                    )
                    AppUtils.showAlert(
                        (mContext as Activity?)!!,
                        mContext!!.getResources().getString(R.string.drag_impossible), "",
                        mContext!!.getResources().getString(R.string.ok), false
                    )
                } else if (v === mRelTwo) {
                    mImgTwo!!.setImageDrawable(listImageArray.getDrawable(position))
                    var relTwoStr: String? = ""
                    if (listItemArray.get(position).toString()
                            .equals(NaisClassNameConstants.CCAS)
                    ) {
                        relTwoStr = NaisClassNameConstants.CCAS
                    } else {
                        relTwoStr = listItemArray.get(position).toUpperCase()
                    }
                    mTxtTwo!!.text = relTwoStr
                    getTabId(listItemArray.get(position))
                    PreferenceManager.setButtonTwoTabId(mContext!!, TAB_ID)
                    setBackgroundColorForView(listItemArray.get(position), mRelTwo!!)
                    PreferenceManager.setButtonTwoTextImage(
                        mContext!!,
                        Integer.toString(position)
                    )
                } else if (v === mRelThree) {
                    mImgThree!!.setImageDrawable(listImageArray.getDrawable(position))
                    var relTwoStr: String? = ""
                    if (listItemArray.get(position).toString()
                            .equals(NaisClassNameConstants.CCAS)
                    ) {
                        relTwoStr = NaisClassNameConstants.CCAS
                    } else {
                        relTwoStr = listItemArray.get(position).toUpperCase()
                    }
                    mTxtThree!!.text = relTwoStr
                    getTabId(listItemArray.get(position))
                    PreferenceManager.setButtonThreeTabId(mContext!!, TAB_ID)
                    setBackgroundColorForView(listItemArray.get(position), mRelThree!!)
                    PreferenceManager.setButtonThreeTextImage(
                        mContext!!,
                        Integer.toString(position)
                    )
                } else if (v === mRelFour) {
                    mImgFour!!.setImageDrawable(listImageArray.getDrawable(position))
                    var relTwoStr: String? = ""
                    if (listItemArray.get(position).toString()
                            .equals(NaisClassNameConstants.CCAS)
                    ) {
                        relTwoStr = NaisClassNameConstants.CCAS
                    } else {
                        relTwoStr = listItemArray.get(position).toUpperCase()
                    }
                    mTxtFour!!.text = relTwoStr
                    getTabId(listItemArray.get(position))
                    PreferenceManager.setButtonFourTabId(mContext!!, TAB_ID)
                    setBackgroundColorForView(listItemArray.get(position), mRelFour!!)
                    PreferenceManager.setButtonFourTextImage(
                        mContext!!,
                        Integer.toString(position)
                    )
                } else if (v === mRelFive) {
                    mImgFive!!.setImageDrawable(listImageArray.getDrawable(position))
                    var relTwoStr: String? = ""
                    if (listItemArray.get(position).toString()
                            .equals(NaisClassNameConstants.CCAS)
                    ) {
                        relTwoStr = NaisClassNameConstants.CCAS
                    } else {
                        relTwoStr = listItemArray.get(position).toUpperCase()
                    }
                    mTxtFive!!.text = relTwoStr
                    getTabId(listItemArray.get(position))
                    PreferenceManager.setButtonFiveTabId(mContext!!, TAB_ID)
                    setBackgroundColorForView(listItemArray.get(position), mRelFive!!)
                    PreferenceManager.setButtonFiveTextImage(
                        mContext!!,
                        Integer.toString(position)
                    )
                } else if (v === mRelSix) {
                    mImgSix!!.setImageDrawable(listImageArray.getDrawable(position))
                    var relTwoStr: String? = ""
                    if (listItemArray.get(position).toString()
                            .equals(NaisClassNameConstants.CCAS)
                    ) {
                        relTwoStr = NaisClassNameConstants.CCAS
                    } else {
                        relTwoStr = listItemArray.get(position).toUpperCase()
                    }
                    mTxtSix!!.text = relTwoStr
                    getTabId(listItemArray.get(position))
                    PreferenceManager.setButtonSixTabId(
                        mContext!!,TAB_ID)
                    setBackgroundColorForView(listItemArray.get(position), mRelSix!!)
                    PreferenceManager.setButtonSixTextImage(
                        mContext!!,
                        Integer.toString(position)
                    )
                } else if (v === mRelSeven) {
                    mImgSeven!!.setImageDrawable(listImageArray.getDrawable(position))
                    var relTwoStr: String? = ""
                    if (listItemArray.get(position).toString()
                            .equals(NaisClassNameConstants.CCAS)
                    ) {
                        relTwoStr = NaisClassNameConstants.CCAS
                    } else {
                        relTwoStr = listItemArray.get(position).toUpperCase()
                    }
                    mTxtSeven!!.text = relTwoStr
                    getTabId(listItemArray.get(position))
                    PreferenceManager.setButtonSevenTabId(mContext!!, TAB_ID)
                    setBackgroundColorForView(listItemArray.get(position), mRelSeven!!)
                    PreferenceManager.setButtonSevenTextImage(
                        mContext!!,
                        Integer.toString(position)
                    )
                } else if (v === mRelEight) {
                    mImgEight!!.setImageDrawable(listImageArray.getDrawable(position))
                    var relTwoStr: String? = ""
                    if (listItemArray.get(position).toString()
                            .equals(NaisClassNameConstants.CCAS)
                    ) {
                        relTwoStr = NaisClassNameConstants.CCAS
                    } else {
                        relTwoStr = listItemArray.get(position).toUpperCase()
                    }
                    mTxtEight!!.text = relTwoStr
                    getTabId(listItemArray.get(position))
                    PreferenceManager.setButtonEightTabId(mContext!!, TAB_ID)
                    setBackgroundColorForView(listItemArray.get(position), mRelEight!!)
                    PreferenceManager.setButtonEightTextImage(
                        mContext!!,
                        Integer.toString(position)
                    )
                } else if (v === mRelNine) {
                    mImgNine!!.setImageDrawable(listImageArray.getDrawable(position))
                    var relTwoStr: String? = ""
                    if (listItemArray.get(position).toString()
                            .equals(NaisClassNameConstants.CCAS)
                    ) {
                        relTwoStr = NaisClassNameConstants.CCAS
                    } else {
                        relTwoStr = listItemArray.get(position).toUpperCase()
                    }
                    mTxtNine!!.text = relTwoStr
                    getTabId(listItemArray.get(position))
                    PreferenceManager.setButtonNineTabId(mContext!!, TAB_ID)
                    setBackgroundColorForView(listItemArray.get(position), mRelNine!!)
                    PreferenceManager.setButtonNineTextImage(
                        mContext!!,
                        Integer.toString(position)
                    )
                }
            }
            //v=null
           // v = null
            viewTouched = null
        }

        private fun setBackgroundColorForView(buttonText: String, v: View) {
            if (v === mRelOne) {
                v.setBackgroundColor(
                    mContext!!.resources.getColor(
                        R.color.transparent
                    )
                )
                saveButtonBgColor(
                    v,
                    mContext!!.resources.getColor(R.color.transparent)
                )
                return
            } else if (v === mRelTwo) {
                v.setBackgroundColor(mContext!!.resources.getColor(R.color.transparent))
                saveButtonBgColor(v, mContext!!.resources.getColor(R.color.transparent))
                return
            } else if (v === mRelThree) {
                v.setBackgroundColor(
                    mContext!!.resources.getColor(
                        R.color.transparent
                    )
                )
                saveButtonBgColor(
                    v,
                    mContext!!.resources.getColor(R.color.transparent)
                )
                return
            } else if (v === mRelFour) {
                v.setBackgroundColor(
                    mContext!!.resources.getColor(
                        R.color.transparent
                    )
                )
                saveButtonBgColor(
                    v,
                    mContext!!.resources.getColor(R.color.transparent)
                )
                return
            } else if (v === mRelFive) {
                v.setBackgroundColor(
                    mContext!!.resources.getColor(
                        R.color.transparent
                    )
                )
                saveButtonBgColor(
                    v,
                    mContext!!.resources.getColor(R.color.transparent)
                )
                return
            } else if (v === mRelSix) {
                v.setBackgroundColor(mContext!!.resources.getColor(R.color.transparent))
                saveButtonBgColor(v, mContext!!.resources.getColor(R.color.transparent))
                return
            } else if (v === mRelSeven) {
                v.setBackgroundColor(
                    mContext!!.resources.getColor(
                        R.color.transparent
                    )
                )
                saveButtonBgColor(
                    v,
                    mContext!!.resources.getColor(R.color.transparent)
                )
                return
            } else if (v === mRelEight) {
                v.setBackgroundColor(
                    mContext!!.resources.getColor(
                        R.color.transparent
                    )
                )
                saveButtonBgColor(
                    v,
                    mContext!!.resources.getColor(R.color.transparent)
                )
                return
            } else if (v === mRelNine) {
                v.setBackgroundColor(
                    mContext!!.resources
                        .getColor(R.color.transparent)
                )
                saveButtonBgColor(
                    v, mContext!!.resources
                        .getColor(R.color.transparent)
                )
                return
            }
        }

        private fun saveButtonBgColor(v: View, color: Int) {
            if (v != null) {
                if (v === mRelOne) {
                    PreferenceManager.setButtonOneBg(mContext!!, color)
                } else if (v === mRelTwo) {
                    PreferenceManager.setButtonTwoBg(mContext!!, color)
                } else if (v === mRelThree) {
                    PreferenceManager.setButtonThreeBg(mContext!!, color)
                } else if (v === mRelFour) {
                    PreferenceManager.setButtonFourBg(mContext!!, color)
                } else if (v === mRelFive) {
                    PreferenceManager.setButtonFiveBg(mContext!!, color)
                } else if (v === mRelSix) {
                    PreferenceManager.setButtonSixBg(mContext!!, color)
                } else if (v === mRelSeven) {
                    PreferenceManager.setButtonSevenBg(mContext!!, color)
                } else if (v === mRelEight) {
                    PreferenceManager.setButtonEightBg(mContext!!, color)
                } else if (v === mRelNine) {
                    PreferenceManager.setButtonNineBg(mContext!!, color)
                }
                //v = null
            }
        }

        private fun getTabId(text: String) {
            if (text.equals(NaisClassNameConstants.ABOUT_US)) {
                TAB_ID = NaisTabConstants.TAB_ABOUT_US_REG
            } else if (text.equals(NaisClassNameConstants.NOTIFICATIONS)) {
                TAB_ID = NaisTabConstants.TAB_NOTIFICATIONS_REG
            } else if (text.equals(NaisClassNameConstants.COMMUNICATIONS)) {
                TAB_ID = NaisTabConstants.TAB_COMMUNICATIONS_REG
            } else if (text.equals(NaisClassNameConstants.CALENDAR)) {
                TAB_ID = NaisTabConstants.TAB_CALENDAR_REG
            } else if (text.equals(NaisClassNameConstants.CONTACT_US)) {
                TAB_ID = NaisTabConstants.TAB_CONTACT_US_REG
            } else if (text.equals(NaisClassNameConstants.PARENT_ESSENTIALS)) {
                TAB_ID = NaisTabConstants.TAB_PARENT_ESSENTIALS_REG
            } /*else if (text.equalsIgnoreCase(EARLY_YEARS)) {
			TAB_ID = TAB_EARLYYEARS;
		} */
            /*else if (text.equalsIgnoreCase(SPORTS)) {
			TAB_ID = TAB_SPORTS;
		} else if (text.equalsIgnoreCase(IB_PROGRAMME)) {
			TAB_ID = TAB_IB_PROGRAMME;
		} else if (text.equalsIgnoreCase(PERFORMING_ARTS)) {
			TAB_ID = TAB_PERFORMING_ARTS;
		}*/ else if (text.equals(NaisClassNameConstants.GALLERY)) {
                TAB_ID = NaisTabConstants.TAB_GALLERY_REG
            } /* else if (text.equalsIgnoreCase(CCAS)) {
			TAB_ID = TAB_CCAS;
		} else if (text.equalsIgnoreCase(NAE_PROGRAMMES)) {
			TAB_ID = TAB_NAE_PROGRAMMES;
		}*/
            /*else if (text.equalsIgnoreCase(PRIMARY)) {
			TAB_ID = TAB_PRIMARY;
		} else if (text.equalsIgnoreCase(SECONDARY)) {
			TAB_ID = TAB_SECONDARY;
		}*/ else if (text.equals(NaisClassNameConstants.SOCIAL_MEDIA)) {
                TAB_ID = NaisTabConstants.TAB_SOCIAL_MEDIA_REG
            } else if (text.equals(NaisClassNameConstants.PROGRAMMES)) {
                TAB_ID = NaisTabConstants.TAB_PROGRAMMES_REG
            } else if (text.equals(NaisClassNameConstants.NAS_TODAY)) {
                TAB_ID = NaisTabConstants.TAB_NAS_TODAY
            } else if (text.equals(NaisClassNameConstants.PARENT_EVENING)) {
                TAB_ID = NaisTabConstants.TAB_PARENTS_MEETING_REG
            } else if (text.equals(NaisClassNameConstants.ABSENCE)) {
                TAB_ID = NaisTabConstants.TAB_ABSENCES_REG
            } else if (text.equals(NaisClassNameConstants.PARENTS_ASSOCIATION)) {
                TAB_ID = NaisTabConstants.TAB_PARENTS_ASSOCIATION_REG
            }

        }

        private fun getButtonViewTouched(centerX: Float, centerY: Float) {

            // button one
            val arr1 = IntArray(2)
            mRelOne!!.getLocationInWindow(arr1)
            val x1 = arr1[0]
            val x2 = x1 + mRelOne!!.width
            val y1 = arr1[1]
            val y2 = y1 + mRelOne!!.height

            // button two

            // button two
            val arr2 = IntArray(2)
            mRelTwo!!.getLocationInWindow(arr2)
            val x3 = arr2[0]
            val x4 = x3 + mRelTwo!!.width
            val y3 = arr2[1]
            val y4 = y3 + mRelTwo!!.height

            // button three

            // button three
            val arr3 = IntArray(2)
            mRelThree!!.getLocationInWindow(arr3)
            val x5 = arr3[0]
            val x6 = x5 + mRelThree!!.width
            val y5 = arr3[1]
            val y6 = y5 + mRelFour!!.height

            // button four

            // button four
            val arr4 = IntArray(2)
            mRelFour!!.getLocationInWindow(arr4)
            val x7 = arr4[0]
            val x8 = x7 + mRelFour!!.width
            val y7 = arr4[1]
            val y8 = y7 + mRelFour!!.height

            // button five

            // button five
            val arr5 = IntArray(2)
            mRelFive!!.getLocationInWindow(arr5)
            val x9 = arr5[0]
            val x10 = x9 + mRelFive!!.width
            val y9 = arr5[1]
            val y10 = y9 + mRelFive!!.height

            // button six

            // button six
            val arr6 = IntArray(2)
            mRelSix!!.getLocationInWindow(arr6)
            val x11 = arr6[0]
            val x12 = x11 + mRelSix!!.width
            val y11 = arr6[1]
            val y12 = y11 + mRelSix!!.height

            // button seven

            // button seven
            val arr7 = IntArray(2)
            mRelSeven!!.getLocationInWindow(arr7)
            val x13 = arr7[0]
            val x14 = x13 + mRelSeven!!.width
            val y13 = arr7[1]
            val y14 = y13 + mRelSeven!!.height

            // button eight

            // button eight
            val arr8 = IntArray(2)
            mRelEight!!.getLocationInWindow(arr8)
            val x15 = arr8[0]
            val x16 = x15 + mRelEight!!.width
            val y15 = arr8[1]
            val y16 = y15 + mRelEight!!.height

            // button nine

            // button nine
            val arr9 = IntArray(2)
            mRelNine!!.getLocationInWindow(arr9)
            val x17 = arr9[0]
            val x18 = x17 + mRelNine!!.width
            val y17 = arr9[1]
            val y18 = y17 + mRelNine!!.height

            if (x1 <= centerX && centerX <= x2 && y1 <= centerY && centerY <= y2) {
                viewTouched = mRelOne
            } else if (x3 <= centerX && centerX <= x4 && y3 <= centerY && centerY <= y4) {
                viewTouched = mRelTwo
            } else if (x5 <= centerX && centerX <= x6 && y5 <= centerY && centerY <= y6) {
                viewTouched = mRelThree
            } else if (x7 <= centerX && centerX <= x8 && y7 <= centerY && centerY <= y8) {
                viewTouched = mRelFour
            } else if (x9 <= centerX && centerX <= x10 && y9 <= centerY && centerY <= y10) {
                viewTouched = mRelFive
            } else if (x11 <= centerX && centerX <= x12 && y11 <= centerY && centerY <= y12) {
                viewTouched = mRelSix
            } else if (x13 <= centerX && centerX <= x14 && y13 <= centerY && centerY <= y14) {
                viewTouched = mRelSeven
            } else if (x15 <= centerX && centerX <= x16 && y15 <= centerY && centerY <= y16) {
                viewTouched = mRelEight
            } else if (x17 <= centerX && centerX <= x18 && y17 <= centerY && centerY <= y18) {
                viewTouched = mRelNine
            } else {
                viewTouched = null
            }
        }
    }

    private fun getButtonBgAndTextImages() {
        if (PreferenceManager.getButtonOneTextImage(mContext!!)!!.toInt() != 0) {
            val sdk = Build.VERSION.SDK_INT
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                mImgOne!!.background = mContext!!.resources.getDrawable(R.drawable.news)
            } else {
                mImgOne!!.background = mContext!!.resources.getDrawable(R.drawable.news)
            }
            mTxtOne!!.text = "NAIS MANILA TODAY"
            mRelOne!!.setBackgroundColor(PreferenceManager.getButtonOneBg(mContext!!))
        }
        if (PreferenceManager.getButtonTwoTextImage(mContext!!)!!.toInt() != 0) {
            mImgTwo!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager.getButtonTwoTextImage(
                        mContext!!
                    )!!.toInt()
                )
            )
            var relTwoStr = ""
            relTwoStr = if (listItemArray[PreferenceManager.getButtonTwoTextImage(mContext!!)!!
                    .toInt()].equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray[PreferenceManager.getButtonTwoTextImage(mContext!!)!!.toInt()].toUpperCase()
            }
            mTxtTwo!!.text = relTwoStr
            mRelTwo!!.setBackgroundColor(PreferenceManager.getButtonTwoBg(mContext!!))
        }
        if (PreferenceManager.getButtonThreeTextImage(mContext!!)!!.toInt() != 0) {
            mImgThree!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager.getButtonThreeTextImage(
                        mContext!!
                    )!!.toInt()
                )
            )
            var relTwoStr: String = if (listItemArray.get(
                    PreferenceManager.getButtonThreeTextImage(
                        mContext!!
                    )!!.toInt()
                ).equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray[PreferenceManager.getButtonThreeTextImage(mContext!!)!!.toInt()].toUpperCase(
                    Locale.ROOT
                )
            }
            mTxtThree!!.text = relTwoStr
            mRelThree!!.setBackgroundColor(PreferenceManager.getButtonThreeBg(mContext!!))
        }
        if (PreferenceManager.getButtonFourTextImage(mContext!!)!!.toInt() != 0) {
            mImgFour!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager.getButtonFourTextImage(
                        mContext!!
                    )!!.toInt()
                )
            )
            var relTwoStr: String = if (listItemArray[PreferenceManager.getButtonFourTextImage(
                    mContext!!
                )!!.toInt()].equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray[PreferenceManager.getButtonFourTextImage(mContext!!)!!.toInt()].toUpperCase(
                    Locale.ROOT
                )
            }
            mTxtFour!!.text = relTwoStr
            mRelFour!!.setBackgroundColor(PreferenceManager.getButtonFourBg(mContext!!))
        }
        if (PreferenceManager.getButtonFiveTextImage(mContext!!)!!.toInt() != 0) {
            mImgFive!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager.getButtonFiveTextImage(
                        mContext!!
                    )!!.toInt()
                )
            )
            var relTwoStr = ""
            relTwoStr = if (listItemArray.get(
                    PreferenceManager.getButtonFiveTextImage(mContext!!)!!.toInt()
                ).equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray.get(PreferenceManager.getButtonFiveTextImage(mContext!!)!!.toInt()).toUpperCase()
            }
            mTxtFive!!.text = relTwoStr
            mRelFive!!.setBackgroundColor(
                PreferenceManager
                    .getButtonFiveBg(mContext!!)
            )
        }
        if (PreferenceManager
                .getButtonSixTextImage(mContext!!)!!.toInt() != 0) {
            mImgSix!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager
                        .getButtonSixTextImage(mContext!!)!!.toInt()
                )
            )
            var relTwoStr = ""
            relTwoStr = if (listItemArray.get(
                    PreferenceManager
                        .getButtonSixTextImage(mContext!!)!!.toInt()
                ).equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray.get(
                    PreferenceManager
                        .getButtonSixTextImage(mContext!!)!!.toInt()
                ).toUpperCase()
            }
            mTxtSix!!.text = relTwoStr
            mRelSix!!.setBackgroundColor(
                PreferenceManager
                    .getButtonSixBg(mContext!!)
            )
        }
        if (PreferenceManager
                .getButtonSevenTextImage(mContext!!)!!.toInt() != 0) {
            mImgSeven!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager
                        .getButtonSevenTextImage(mContext!!)!!.toInt()
                )
            )
            var relTwoStr = ""
            relTwoStr = if (listItemArray.get(
                    PreferenceManager
                        .getButtonSevenTextImage(mContext!!)!!.toInt()
                ).equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray.get(
                    PreferenceManager
                        .getButtonSevenTextImage(mContext!!)!!.toInt()
                ).toUpperCase()
            }
            mTxtSeven!!.text = relTwoStr
            mRelSeven!!.setBackgroundColor(
                PreferenceManager
                    .getButtonSevenBg(mContext!!)
            )
        }
        if (PreferenceManager
                .getButtonEightTextImage(mContext!!)!!.toInt() != 0) {
            mImgEight!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager
                        .getButtonEightTextImage(mContext!!)!!.toInt()
                )
            )
            var relTwoStr = ""
            relTwoStr = if (listItemArray.get(
                    PreferenceManager
                        .getButtonEightTextImage(mContext!!)!!.toInt()
                ).equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray.get(
                    PreferenceManager
                        .getButtonEightTextImage(mContext!!)!!.toInt()
                ).toUpperCase()
            }
            mTxtEight!!.text = relTwoStr
            mRelEight!!.setBackgroundColor(
                PreferenceManager
                    .getButtonEightBg(mContext!!)
            )
        }
        if (PreferenceManager
                .getButtonNineTextImage(mContext!!)!!.toInt() != 0) {
            mImgNine!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager
                        .getButtonNineTextImage(mContext!!)!!.toInt()
                )
            )
            var relTwoStr = ""
            relTwoStr = if (listItemArray.get(
                    PreferenceManager
                        .getButtonNineTextImage(mContext!!)!!.toInt()
                ).equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray.get(
                    PreferenceManager
                        .getButtonNineTextImage(mContext!!)!!.toInt()
                ).toUpperCase()
            }
            mTxtNine!!.text = relTwoStr
            mRelNine!!.setBackgroundColor(
                PreferenceManager
                    .getButtonNineBg(mContext!!)
            )
        }
    }

    private fun setListeners() {
        mRelOne!!.setOnClickListener{
            INTENT_TAB_ID = PreferenceManager.getButtonOneTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelTwo!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonTwoTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelThree!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonThreeTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelFour!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonFourTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelFive!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonFiveTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelSix!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonSixTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelSeven!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonSevenTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelEight!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonEightTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelNine!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonNineTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
    }

    private fun checkIntent(intentTabId: String?) {
        var mFragment: Fragment? = null
        if (intentTabId.equals(NaisTabConstants.TAB_CALENDAR_REG, ignoreCase = true)) {
            mFragment = CalendarWebViewFragment()
            fragmentIntent(mFragment)
        } else if (intentTabId.equals(NaisTabConstants.TAB_NOTIFICATIONS_REG, ignoreCase = true)) {
            mFragment = NotificationsFragment()
            fragmentIntent(mFragment)
        } else if ( intentTabId.equals(
                NaisTabConstants.TAB_PARENT_ESSENTIALS_REG,
                ignoreCase = true
            )) {
            mFragment = ParentEssentialsFragment()
            fragmentIntent(mFragment)
        } else if (intentTabId.equals(NaisTabConstants.TAB_PROGRAMMES_REG, ignoreCase = true)) {
            mFragment = CcaFragment()
            fragmentIntent(mFragment!!)
        } else if (intentTabId.equals(NaisTabConstants.TAB_SOCIAL_MEDIA_REG, ignoreCase = true)) {
            mFragment = SocialMediaFragment()
            fragmentIntent(mFragment)
        } else if (intentTabId.equals(NaisTabConstants.TAB_ABOUT_US_REG, ignoreCase = true)) {
            mFragment = AboutUsFragment()
            fragmentIntent(mFragment)
        } else if (intentTabId.equals(NaisTabConstants.TAB_CONTACT_US_REG, ignoreCase = true)) {
            if (ActivityCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                checkpermission()


            } else {
                mFragment = ContactUsFragment()
                fragmentIntent(mFragment)
            }
          /*  mFragment = ContactUsFragment()
            if (Build.VERSION.SDK_INT < 23) {
                fragmentIntent(mFragment)
            } else {
                if (ActivityCompat.checkSelfPermission(requireActivity(), permissionsRequiredLocation[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        permissionsRequiredLocation[1]
                    ) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            permissionsRequiredLocation[0]
                        )
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            permissionsRequiredLocation[1]
                        )) {
                        val builder = AlertDialog.Builder(requireActivity())
                        builder.setTitle("Need Location Permission")
                        builder.setMessage("This module needs location permissions.")
                        builder.setPositiveButton("Grant") { dialog, which ->
                            dialog.cancel()
                            ActivityCompat.requestPermissions(
                                requireActivity(),
                                permissionsRequiredLocation,
                                PERMISSION_CALLBACK_CONSTANT_LOCATION
                            )
                        }
                        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                        builder.show()
                    } else if (locationPermissionStatus!!.getBoolean(
                            permissionsRequiredLocation[0],
                            false
                        )) {
                        val builder = AlertDialog.Builder(requireActivity())
                        builder.setTitle("Need Location Permission")
                        builder.setMessage("This module needs location permissions.")
                        builder.setPositiveButton("Grant") { dialog, which ->
                            dialog.cancel()
                            locationToSettings = true
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", requireActivity().packageName, null)
                            intent.data = uri
                            startActivityForResult(intent, REQUEST_PERMISSION_LOCATION)
                            Toast.makeText(
                                mContext,
                                "Go to settings and grant access to location",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        builder.setNegativeButton("Cancel") { dialog, which ->
                            dialog.cancel()
                            locationToSettings = false
                        }
                        builder.show()
                    } else if (locationPermissionStatus!!.getBoolean(
                            permissionsRequiredLocation[1],
                            false
                        )) {
                        val builder = AlertDialog.Builder(requireActivity())
                        builder.setTitle("Need Location Permission")
                        builder.setMessage("This module needs location permissions.")
                        builder.setPositiveButton("Grant") { dialog, which ->
                            dialog.cancel()
                            locationToSettings = true
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", requireActivity().packageName, null)
                            intent.data = uri
                            startActivityForResult(intent, REQUEST_PERMISSION_LOCATION)
                            Toast.makeText(
                                mContext,
                                "Go to settings and grant access to location",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        builder.setNegativeButton("Cancel") { dialog, which ->
                            dialog.cancel()
                            locationToSettings = false
                        }
                        builder.show()
                    } else {
                        requestPermissions(
                            permissionsRequiredLocation,
                            PERMISSION_CALLBACK_CONSTANT_LOCATION
                        )
                    }
                    val editor = locationPermissionStatus!!.edit()
                    editor.putBoolean(permissionsRequiredLocation[0], true)
                    editor.apply()
                } else {
                    fragmentIntent(mFragment)
                }
            }*/
        } else if (intentTabId.equals(NaisTabConstants.TAB_NAS_TODAY, ignoreCase = true)) {
            mFragment = NasTodayFragment(
                NaisClassNameConstants.NAS_TODAY,
                NaisTabConstants.TAB_NAS_TODAY
            )
            fragmentIntent(mFragment)
        } else if (intentTabId.equals(NaisTabConstants.TAB_PARENTS_MEETING_REG, ignoreCase = true)) {
            mFragment = ParentsEveningFragment( )
            fragmentIntent(mFragment)
        } else if (intentTabId.equals(NaisTabConstants.TAB_ABSENCES_REG, ignoreCase = true)) {
            mFragment = AbsenceFragment()
            fragmentIntent(mFragment)
        } /*else if (intentTabId.equals(
                NaisTabConstants.TAB_PARENTS_ASSOCIATION_REG,
                ignoreCase = true
            )) {
            mFragment = ParentAssociationsFragment(
                NaisClassNameConstants.PARENTS_ASSOCIATION,
                NaisTabConstants.TAB_PARENTS_ASSOCIATION_REG
            )
            fragmentIntent(mFragment)
        }*/
    }

    private fun fragmentIntent(mFragment: Fragment?) {
        if (mFragment != null) {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .add(R.id.frame_container, mFragment, title)
                .addToBackStack(title).commitAllowingStateLoss()
        }
    }

    private fun initialiseUI() {
        bannerImagePager = rootView.findViewById<View>(R.id.bannerImagePager) as ViewPager
        mRelOne = rootView.findViewById<View>(R.id.relOne) as RelativeLayout?
        mRelTwo = rootView.findViewById<View>(R.id.relTwo) as RelativeLayout
        mRelThree = rootView.findViewById<View>(R.id.relThree) as RelativeLayout
        mRelFour = rootView.findViewById<View>(R.id.relFour) as RelativeLayout
        mRelFive = rootView.findViewById<View>(R.id.relFive) as RelativeLayout
        mRelSix = rootView.findViewById<View>(R.id.relSix) as RelativeLayout
        mRelSeven = rootView.findViewById<View>(R.id.relSeven) as RelativeLayout
        mRelEight = rootView.findViewById<View>(R.id.relEight) as RelativeLayout
        mRelNine = rootView.findViewById<View>(R.id.relNine) as RelativeLayout
        mTxtOne = rootView.findViewById<View>(R.id.relTxtOne) as TextView?
        mImgOne = rootView.findViewById<View>(R.id.relImgOne) as ImageView?
        mTxtTwo = rootView.findViewById<View>(R.id.relTxtTwo) as TextView
        mImgTwo = rootView.findViewById<View>(R.id.relImgTwo) as ImageView
        mTxtThree = rootView.findViewById<View>(R.id.relTxtThree) as TextView
        mImgThree = rootView.findViewById<View>(R.id.relImgThree) as ImageView
        mTxtFour = rootView.findViewById<View>(R.id.relTxtFour) as TextView
        mImgFour = rootView.findViewById<View>(R.id.relImgFour) as ImageView
        mTxtFive = rootView.findViewById<View>(R.id.relTxtFive) as TextView
        mImgFive = rootView.findViewById<View>(R.id.relImgFive) as ImageView
        mTxtSix = rootView.findViewById<View>(R.id.relTxtSix) as TextView
        mImgSix = rootView.findViewById<View>(R.id.relImgSix) as ImageView
        mTxtSeven = rootView.findViewById<View>(R.id.relTxtSeven) as TextView
        mImgSeven = rootView.findViewById<View>(R.id.relImgSeven) as ImageView
        mTxtEight = rootView.findViewById<View>(R.id.relTxtEight) as TextView
        mImgEight = rootView.findViewById<View>(R.id.relImgEight) as ImageView
        mTxtNine = rootView.findViewById<View>(R.id.relTxtNine) as TextView
        mImgNine = rootView.findViewById<View>(R.id.relImgNine) as ImageView

        progressBarDialog = ProgressBarDialog(mContext!!)
        homeBannerUrlImageArray = ArrayList<String>()
//        getVersionInfo()
        if (AppUtils.checkInternet(mContext!!)) {
            getBanner()
            callDeviceReg()

        } else {

            homeBannerUrlImageArray.add("")
            bannerImagePager!!.adapter = ImagePagerDrawableAdapter(
                mContext!!,
                homeBannerUrlImageArray
            )
            Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show()
        }

        if (homeBannerUrlImageArray != null) {
            val handler = Handler()
            val update = Runnable {
                if (currentPage == homeBannerUrlImageArray.size) {
                    currentPage = 0
                    bannerImagePager!!.setCurrentItem(
                        currentPage,
                        false
                    )
                } else {
                    bannerImagePager!!
                        .setCurrentItem(currentPage++, true)
                }
            }
            val swipeTimer = Timer()
            swipeTimer.schedule(object : TimerTask() {
                override fun run() {
                    handler.post(update)
                }
            }, 100, 3000)
        }
        mSectionText = arrayOfNulls(9)
    }
    private fun callDeviceReg() {

        progressBarDialog!!.show()
        val fToken = arrayOf("")
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        var device = manufacturer + model
        FirebaseApp.initializeApp(mContext!!)
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                fToken[0] = token
                PreferenceManager.setFCMID(mContext!!, token)
            } else {
            }
        }
        val versionName: String = BuildConfig.VERSION_NAME

        var androidID = Settings.Secure.getString(
            mContext!!.contentResolver,
            Settings.Secure.ANDROID_ID)
        var loginbody = DeviceRegistrtionmodel(
            "2", PreferenceManager.getFCMID(mContext), device, versionName, androidID
        )

        val call: Call<ResponseBody> = ApiClient.getClient.deviceregistration("Bearer "+PreferenceManager.getAccessToken(
            mContext),loginbody)

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
    private fun getBanner() {

        var homebannerbody = HomeBannerApiModel(versionName, "2")
        val call: Call<HomeBannerModel> = ApiClient.getClient.homebanner(
            "Bearer " + PreferenceManager.getAccessToken(mContext),
            homebannerbody
        )
        progressBarDialog!!.show()
        call.enqueue(object : Callback<HomeBannerModel> {
            override fun onResponse(call: Call<HomeBannerModel>, response: Response<HomeBannerModel>) {
                if (response.isSuccessful) {
                    progressBarDialog!!.dismiss()
                    val responseData = response.body()
                    if (response.body()!!.responsecode.equals("200")) {
                        progressBarDialog!!.dismiss()
                        var status_code = response.body()!!.response.statuscode
                        if (status_code.equals("303")) {

                            if (responseData!!.response.data.size > 0) {
                                homeBannerUrlImageArray.addAll(responseData!!.response.data)

                                bannerImagePager!!.adapter =
                                    ImagePagerDrawableAdapter(mContext, homeBannerUrlImageArray)
                            } else {
                                bannerImagePager!!.setBackgroundResource(R.drawable.default_bannerr)
//
                            }


                        } else if (status_code.equals("301")) {
                            AppUtils.showDialogAlertDismiss(
                                context,
                                getString(R.string.error_heading),
                                getString(R.string.missing_parameter),
                                R.drawable.infoicon,
                                R.drawable.round
                            )
                        } else if (status_code.equals("304")) {
                            AppUtils.showDialogAlertDismiss(
                                context as Activity?,
                                getString(R.string.error_heading),
                                getString(R.string.email_exists),
                                R.drawable.infoicon,
                                R.drawable.round
                            )
                        } else if (status_code.equals("305")) {
                            AppUtils.showDialogAlertDismiss(
                                context as Activity?,
                                getString(R.string.error_heading),
                                getString(R.string.incrct_usernamepswd),
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
                        } else if (status_code.equals("306")) {
                            AppUtils.showDialogAlertDismiss(
                                context as Activity?,
                                getString(R.string.error_heading),
                                getString(R.string.invalid_email),
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                context as Activity?,
                                getString(R.string.error_heading),
                                getString(R.string.common_error),
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
                        }

                    }  else {
                        progressBarDialog!!.dismiss()
                        AppUtils.showDialogAlertDismiss(
                            context,
                            "Alert",
                            mContext!!.getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                }
              else
                {
                    AppUtils.showSessionExpired(
                        context,
                        "Session Expired",
                        "You will now be logged out.",
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                   //  showSessionExpiredPopUp()
                }
            }

            override fun onFailure(call: Call<HomeBannerModel>, t: Throwable) {
                progressBarDialog!!.dismiss()            }
        })
    }
    private fun checkpermission() {
        if (ContextCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                homeActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CALL_PHONE
                ),
                123
            )
        }
    }

    private fun showSessionExpiredPopUp() {
        val dialog = Dialog(mContext!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        var icon = dialog.findViewById<ImageView>(R.id.iconImageView)
        icon.setBackgroundResource(R.drawable.round)
        icon.setImageResource(R.drawable.exclamationicon)
        textMessage = dialog.findViewById(R.id.text_dialog)!!
        textHead  = dialog.findViewById(R.id.alertHead)!!
        textMessage.text = "You will now be logged out."
        textHead.text = "Session Expired"
        var dialogButton = dialog.findViewById<Button>(R.id.btn_Ok)
        dialogButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(mContext, LoginActivity::class.java)
           // PreferenceManager.setbackpresskey(mContext, "0")
            PreferenceManager.setAccessToken(mContext!!, "")
            PreferenceManager.setUserEmail(mContext!!,"")
            mContext!!.startActivity(intent)
            (context as Activity).finish()
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
}