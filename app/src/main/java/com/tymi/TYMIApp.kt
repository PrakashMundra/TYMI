package com.tymi

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tymi.controllers.DataController

class TYMIApp : Application() {
    companion object {
        var menuId: Int = R.id.dashboard
            set(value) {
                field = value
            }
            get() = field
        var mFireBaseAuth: FirebaseAuth? = null
        var mDataBase: DatabaseReference? = null
    }

    override fun onCreate() {
        super.onCreate()
        mFireBaseAuth = FirebaseAuth.getInstance()
        mDataBase = FirebaseDatabase.getInstance().reference
        DataController.getInstance().initialize()
    }
}