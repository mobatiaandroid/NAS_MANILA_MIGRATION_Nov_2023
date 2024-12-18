package com.mobatia.nasmanila.common.constants

import android.os.Build
import android.provider.Settings
import java.util.Locale

class EmulatorDetector {
    fun isEmulator(): Boolean {
        // Check for typical emulator characteristics
        return (Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.lowercase(Locale.getDefault())
                    .contains("vbox") ||
                Build.FINGERPRINT.lowercase(Locale.getDefault())
                    .contains("test-keys") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86") ||
                Build.MANUFACTURER.contains("Genymotion") ||
                (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith(
                    "generic"
                )) || "google_sdk" == Build.PRODUCT || System.getProperty("ro.kernel.qemu") != null ||
                isProbablyAnEmulatorBasedOnDeviceProperties())
    }

    private fun isProbablyAnEmulatorBasedOnDeviceProperties(): Boolean {
        val androidId = Settings.Secure.ANDROID_ID
        return androidId == null || androidId == "android_id"
    }
}