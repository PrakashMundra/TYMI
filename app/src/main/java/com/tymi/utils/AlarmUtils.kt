package com.tymi.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.tymi.AppPreferences
import com.tymi.BuildConfig
import com.tymi.Constants
import com.tymi.receivers.AlarmReceiver

object AlarmUtils {
    fun startAlarm(context: Context) {
        if (BuildConfig.DEBUG)
            Toast.makeText(context, "Ads Alarm has been Set", Toast.LENGTH_SHORT).show()
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val preferences = AppPreferences.getInstance(context)
        val savedTime = preferences.getLong(AppPreferences.TRIGGER_TIME)
        if (savedTime == 0L) {
            val time = System.currentTimeMillis().plus(Constants.ADS_INTERVAL)
            preferences.putLong(AppPreferences.TRIGGER_TIME, time)
            manager.setInexactRepeating(AlarmManager.RTC, time,
                    Constants.ADS_INTERVAL, pendingIntent)
        } else {
            var time = savedTime.minus(System.currentTimeMillis())
            if (time <= 0) {
                time = System.currentTimeMillis()
                preferences.putLong(AppPreferences.TRIGGER_TIME, time.plus(Constants.ADS_INTERVAL))
            } else {
                time = System.currentTimeMillis().plus(time)
                preferences.putLong(AppPreferences.TRIGGER_TIME, time)
            }
            manager.setInexactRepeating(AlarmManager.RTC, time, Constants.ADS_INTERVAL, pendingIntent)
        }
    }

    fun stopAlarm(context: Context) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
        manager.cancel(pendingIntent)
    }
}