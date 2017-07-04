package com.tymi.utils

import android.content.Context
import android.net.ConnectivityManager
import com.tymi.R


object NetworkUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connection = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo = connection.activeNetworkInfo
        val isAvailable = nInfo != null &&
                (nInfo.isConnectedOrConnecting || nInfo.isConnected) &&
                (nInfo.type == ConnectivityManager.TYPE_MOBILE || nInfo.type == ConnectivityManager.TYPE_WIFI)
        if (!isAvailable)
            DialogUtils.showAlertDialog(context, R.string.no_internet)
        return isAvailable
    }
}