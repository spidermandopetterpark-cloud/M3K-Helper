package com.remtrik.m3khelper

import com.topjohnwu.superuser.Shell

object RebootManager {

    fun reboot(): Boolean {

        return try {

            Shell.cmd(
                "reboot"
            ).exec().isSuccess

        } catch (_: Throwable) {
            false
        }
    }
}
