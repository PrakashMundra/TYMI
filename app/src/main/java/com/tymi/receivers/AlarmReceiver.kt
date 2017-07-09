package com.tymi.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.Toast
import com.tymi.AppPreferences
import com.tymi.BuildConfig
import com.tymi.Constants
import com.tymi.utils.AdUtils
import java.util.*


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (BuildConfig.DEBUG)
            Toast.makeText(context, "Ads Alarm has been Received", Toast.LENGTH_LONG).show()
        if (context != null) {
            val profile = AppPreferences.getInstance(context).getString(AppPreferences.USER_PROFILE)
            if (!profile.isNullOrEmpty()) {
                if (intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                    val alarmIntent = Intent(context, AlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT)
                    val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + Constants.ADS_INTERVAL,
                            Constants.ADS_INTERVAL, pendingIntent)
                } else {
                    val random = Random()
                    val i = random.nextInt(2) + 1
                    if (BuildConfig.DEBUG)
                        Toast.makeText(context, "Showing Ad: " + i, Toast.LENGTH_LONG).show()
                    when (i) {
                        1 -> AdUtils.showInterstitialAd(context)
                        2 -> AdUtils.showRewardedVideoAd(context)
                    }
                }
            }
        }
    }
}