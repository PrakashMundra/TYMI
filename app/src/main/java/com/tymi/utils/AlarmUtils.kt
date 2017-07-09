package com.tymi.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.Toast
import com.tymi.BuildConfig
import com.tymi.Constants
import com.tymi.receivers.AlarmReceiver

object AlarmUtils {
    fun startAlarm(context: Context) {
        if (BuildConfig.DEBUG)
            Toast.makeText(context, "Ads Alarm has been Set", Toast.LENGTH_LONG).show()
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
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