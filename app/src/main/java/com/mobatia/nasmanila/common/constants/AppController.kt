package com.mobatia.nasmanila.common.constants

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.mobatia.nasmanila.activities.enrichment.model.CCADetailModel
import com.mobatia.nasmanila.activities.enrichment.model.WeekListModel

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager

class AppController : Application() {

    companion object {
        var instance: AppController? = null
        var weekList: ArrayList<WeekListModel>? = null
        var weekListWithData: ArrayList<Int>? = null
        var filledFlag = 0
        var filled = false
        var CCADetailModelArrayList: ArrayList<CCADetailModel> = ArrayList()

        fun applicationContext(): AppController {
            return instance as AppController
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        // Register ActivityLifecycleCallbacks
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // Apply FLAG_SECURE to prevent screenshots and screen recordings
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}
