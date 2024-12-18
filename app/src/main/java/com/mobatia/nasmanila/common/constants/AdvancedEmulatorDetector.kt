package com.mobatia.nasmanila.common.constants

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.Locale

class AdvancedEmulatorDetector {
    fun isEmulator(): Boolean {
        // Check for QEMU environment
        val knownFiles = arrayOf(
            "/dev/qemu_pipe",
            "/dev/socket/qemud",
            "/system/lib/libc_malloc_debug_qemu.so",
            "/sys/qemu_trace",
            "/system/bin/qemu-props"
        )

        for (file in knownFiles) {
            if (File(file).exists()) {
                return true
            }
        }

        // Check for special CPU info
        return readCpuInfo().lowercase(Locale.getDefault())
            .contains("intel") || readCpuInfo().lowercase(
            Locale.getDefault()
        ).contains("amd")
    }

    private fun readCpuInfo(): String {
        val cpuInfo = StringBuilder()
        try {
            BufferedReader(FileReader("/proc/cpuinfo")).use { br ->
                var line: String?
                while ((br.readLine().also { line = it }) != null) {
                    cpuInfo.append(line)
                }
            }
        } catch (e: IOException) {
            // Ignore exceptions
        }
        return cpuInfo.toString()
    }
}