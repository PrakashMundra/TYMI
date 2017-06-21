package com.tymi

import android.app.Application
import com.tymi.controllers.DataController

class TYMIApp : Application() {
    companion object {
        var menuId: Int = R.id.dashboard
            set(value) {
                field = value
            }
            get() = field
    }

    override fun onCreate() {
        super.onCreate()
        DataController.getInstance().initialize()
    }
}