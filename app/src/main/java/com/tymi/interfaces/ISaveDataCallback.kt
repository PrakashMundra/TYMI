package com.tymi.interfaces

import com.google.firebase.auth.FirebaseUser

interface ISaveDataCallback {
    fun onSaveDataCallback(user: FirebaseUser?)
}