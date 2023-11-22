package com.mobatia.nasmanila.common.constants

import android.app.Activity
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.fragments.absence.model.StudentlistApiModel
import com.mobatia.nasmanila.fragments.absence.model.StudentlistResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NaisClassNameConstants {
    companion object {
        var HOME = "Home"
        var CALENDAR = "Calendar"
        var NOTIFICATIONS = "Notifications"
        var COMMUNICATIONS = "Communications"
        var PARENT_ESSENTIALS = "Parent Essentials"
        var PARENTS_ASSOCIATION = "Parents' Association"
        var PARENT_EVENING = "Parent Meeting" //Evening

        var EARLY_YEARS = "Early Years"
        var PRIMARY = "Primary"
        var SECONDARY = "Secondary"
        var IB_PROGRAMME = "IGCSE Programme"
        var SPORTS = "Sports"
        var PERFORMING_ARTS = "Performing Arts"
        var CCAS = "ELs"
        var DETAILS = "Details"
        var NAE_PROGRAMMES = "NAE Programmes"
        var SOCIAL_MEDIA = "Social Media"
        var CATEGORY1 = "Category 1"
        var CATEGORY2 = "Category 2"
        var GALLERY = "Gallery"
        var ABOUT_US = "About Us"
        var ABSENCE = "Absences"
        var SETTINGS = "Settings"
        var LOGOUT = "Logout"
        var CONTACT_US = "Contact Us"
        var WISSUP = "WISS UP"
        var NAS_TODAY = "NAIS Manila Today"
        var PROGRAMMES = "Enrichment Lessons"
        var COMING_UP = "Coming Up"
        var PRIMARY_COMING_UP = "$PRIMARY: $COMING_UP"
        var SECONDARY_COMING_UP = "$SECONDARY: $COMING_UP"
        var EARLY_YEARS_COMING_UP = "$EARLY_YEARS: $COMING_UP"
        var IB_PROGRAMME_COMING_UP = "$IB_PROGRAMME: $COMING_UP"

    }
}