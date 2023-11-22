package com.mobatia.nasmanila.common.constants

import androidx.multidex.MultiDexApplication
import com.mobatia.nasmanila.activities.enrichment.model.WeekListModel

class AppController : MultiDexApplication() {
    var weekList: ArrayList<WeekListModel>? = null
    var weekListWithData: ArrayList<Int>? = null
}