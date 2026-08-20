package com.remtrik.m3khelper

object WindowsManager {

    fun start(
        config: WindowsConfig
    ): Boolean {

        val ramMb = config.ramMb
        val vCpu = config.vCpu

        println(
            "Windows RAM: ${ramMb} MB"
        )

        println(
            "Windows vCPU: $vCpu"
        )

        /*
         * Depois vamos ligar aqui ao
         * Quick Boot to Windows existente
         * no M3K-Helper.
         */

        return true
    }
}
