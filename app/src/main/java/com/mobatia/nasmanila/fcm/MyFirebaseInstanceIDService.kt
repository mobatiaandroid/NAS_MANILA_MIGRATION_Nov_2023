package com.mobatia.nasmanila.fcm

import com.mobatia.nasmanila.common.common_classes.PreferenceManager

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
public class MyFirebaseInstanceIDService {
    //var mContext: Context
    var sharedprefs: PreferenceManager = PreferenceManager()


    /* override fun onTokenRefresh() {

         //val refreshedToken = FirebaseInstanceId.getInstance().token

         val refreshedToken = FirebaseInstanceId.getInstance().token.toString()

         sendRegistrationToServer(refreshedToken)
         super.onTokenRefresh()
     }*/

    private fun sendRegistrationToServer(refreshedToken: String) {
        /* if (refreshedToken != null) {
             sharedprefs.setFcmID(mContext, refreshedToken)
         }*/

    }
}