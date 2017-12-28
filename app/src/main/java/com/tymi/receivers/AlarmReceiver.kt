package com.tymi.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.tymi.AppPreferences
import com.tymi.BuildConfig
import com.tymi.Constants
import com.tymi.utils.AdUtils
import com.tymi.utils.AlarmUtils
import java.util.*


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (BuildConfig.DEBUG)
            Toast.makeText(context, "Ads Alarm has been Received", Toast.LENGTH_LONG).show()
        if (context != null) {
            val profile = AppPreferences.getInstance(context).getString(AppPreferences.USER_PROFILE)
            if (!profile.isEmpty()) {
                if (intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                    AlarmUtils.startAlarm(context)
                } else {
                    val random = Random()
                    val i = random.nextInt(2) + 1
                    if (BuildConfig.DEBUG)
                        Toast.makeText(context, "Showing Ad: " + i, Toast.LENGTH_LONG).show()
                    when (i) {
                        1 -> AdUtils.showInterstitialAd(context)
                        2 -> AdUtils.showRewardedVideoAd(context)
                    }
                    val time = System.currentTimeMillis().plus(Constants.ADS_INTERVAL)
                    AppPreferences.getInstance(context).putLong(AppPreferences.TRIGGER_TIME, time)
                }
            }
        }
    }
}