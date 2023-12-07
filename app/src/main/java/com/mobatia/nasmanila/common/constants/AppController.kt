package com.mobatia.nasmanila.common.constants

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.mobatia.nasmanila.activities.enrichment.model.CCADetailModel
import com.mobatia.nasmanila.activities.enrichment.model.WeekListModel

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
}