package com.remtrik.m3khelper

import android.content.pm.PackageManager
import rikka.shizuku.Shizuku

object ShizukuManager {

    const val REQUEST_CODE = 1001

    fun isRunning(): Boolean {
        return try {
            Shizuku.pingBinder()
        } catch (_: Throwable) {
            false
        }
    }

    fun hasPermission(): Boolean {
        if (!isRunning()) return false

        return try {
            Shizuku.checkSelfPermission() ==
                    PackageManager.PERMISSION_GRANTED
        } catch (_: Throwable) {
            false
        }
    }

    fun requestPermission() {
        if (!isRunning()) return

        Shizuku.requestPermission(
            REQUEST_CODE
        )
    }

    fun uid(): Int {
        return try {
            Shizuku.getUid()
        } catch (_: Throwable) {
            -1
        }
    }

    fun backend(): String {
        return when (uid()) {
            0 -> "ROOT"
            2000 -> "ADB"
            else -> "DESCONHECIDO"
        }
    }
}
