package com.tymi.utils

import android.util.Log
import com.tymi.Constants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        private val TAG = DateUtils::class.java.simpleName

        fun compareDates(startDate: String, endDate: String): Boolean {
            val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.US)
            try {
                val start = dateFormat.parse(startDate)
                val end = dateFormat.parse(endDate)
                return (start.after(end))
            } catch (e: ParseException) {
                Log.e(TAG, "Dates Comparison", e)
            }
            return false
        }
    }
}