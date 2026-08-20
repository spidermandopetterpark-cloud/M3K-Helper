package com.remtrik.m3khelper

data class WindowsConfig(
    val ramGb: Int = 4,
    val vCpu: Int = 4
) {
    val ramMb: Int
        get() = ramGb * 1024
}
