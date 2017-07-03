package com.tymi

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tymi.controllers.DataController

class TYMIApp : Application(), Application.ActivityLifecycleCallbacks {
    companion object {
        var menuId: Int = R.id.dashboard
            set(value) {
                field = value
            }
            get() = field
        var mFireBaseAuth: FirebaseAuth? = null
        var mDataBase: DatabaseReference? = null
        var isActivityShowing = true
    }

    override fun onCreate() {
        super.onCreate()
        mFireBaseAuth = FirebaseAuth.getInstance()
        mDataBase = FirebaseDatabase.getInstance().reference
        DataController.getInstance().initialize()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {

    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {

    }

    override fun onActivityStarted(p0: Activity?) {

    }

    override fun onActivityResumed(p0: Activity?) {
        isActivityShowing = true
    }

    override fun onActivityPaused(p0: Activity?) {
        isActivityShowing = false
    }

    override fun onActivityStopped(p0: Activity?) {

    }

    override fun onActivityDestroyed(p0: Activity?) {

    }
}