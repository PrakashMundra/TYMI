package com.tymi.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.tymi.Constants
import com.tymi.receivers.AlarmReceiver

object AlarmUtils {
    fun startAlarm(context: Context) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + Constants.ADS_INTERVAL,
                Constants.ADS_INTERVAL, pendingIntent)
    }

    fun stopAlarm(context: Context) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
        manager.cancel(pendingIntent)
    }
}