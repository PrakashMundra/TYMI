package com.tymi.interfaces

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot

interface IDataCallback {
    fun onDataCallback(user: FirebaseUser?, data: DataSnapshot)
}