package com.nilezia.myweather.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Map<String, String>?.getLocalName(locale: Locale = Locale.getDefault()): String {
    return this?.get(locale.language) ?: this?.get("en") ?: "Unknown"
}

fun Long.toTimeString(pattern: String = "HH:mm"): String? {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(Date(this * 1000)) // เพราะ timestamp จาก API เป็น seconds
}