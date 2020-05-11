package com.mayowa.android.rxjavatraining.utils

import android.text.format.DateFormat
import java.util.*

fun getDate(time: Long): String {
    val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = time
    return DateFormat.format("yyyy-MM-dd HH:mm:ss", cal).toString()
}
