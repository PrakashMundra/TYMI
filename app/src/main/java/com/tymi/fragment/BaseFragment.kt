package com.tymi.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ScrollView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tymi.AppPreferences
import com.tymi.R
import com.tymi.TYMIApp
import com.tymi.controllers.DataController
import com.tymi.entity.DataModel
import com.tymi.interfaces.ContextHolder
import com.tymi.interfaces.IDataCallback
import com.tymi.interfaces.ISaveDataCallback
import com.tymi.utils.DialogUtils
import com.tymi.widget.SpinnerWidget


abstract class BaseFragment : Fragment(), ContextHolder {
    var mScrollView: ScrollView? = null
    var mErrorView: View? = null

    override fun getContext(): Context {
        return activity
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(getContainerLayoutId(), container, false)
        return view
    }

    protected abstract fun getContainerLayoutId(): Int

    fun setErrorView(view: View) {
        if (mErrorView == null)
            mErrorView = view
    }

    fun scrollToErrorView() {
        if (mScrollView == null || mErrorView == null)
            return
        mScrollView?.post {
            mErrorView?.requestFocus()
            mScrollView?.smoothScrollTo(0, mErrorView?.top as Int)
        }
    }

    fun disableAllFields(layout: ViewGroup) {
        val count = layout.childCount
        (0 until count).forEach { i ->
            val view = layout.getChildAt(i)
            if (view != null) {
                if (view is SpinnerWidget)
                    view.isEnabled = false
                else if (view is EditText)
                    view.isEnabled = false
                else if (view is ViewGroup)
                    disableAllFields(view)
            }
        }
    }

    fun getDataModel(): DataModel {
        return DataController.getInstance().dataModel!!
    }

    fun getAppPreferences(): AppPreferences {
        return AppPreferences.getInstance(context)
    }

    fun loadDataWithoutUser(child: String, iDataCallback: IDataCallback) {
        TYMIApp.mDataBase?.child(child)?.
                addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {
                        DialogUtils.showAlertDialog(context, getString(R.string.app_name), error?.message!!)
                    }

                    override fun onDataChange(data: DataSnapshot) {
                        iDataCallback.onDataCallback(null, data)
                    }
                })
    }

    fun loadData(child: String, iDataCallback: IDataCallback) {
        val user = TYMIApp.mFireBaseAuth?.currentUser
        if (user != null) {
            TYMIApp.mDataBase?.child(child)?.child(user.uid)?.
                    addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError?) {
                            DialogUtils.showAlertDialog(context, getString(R.string.app_name), error?.message!!)
                        }

                        override fun onDataChange(data: DataSnapshot) {
                            iDataCallback.onDataCallback(user, data)
                        }
                    })
        } else
            DialogUtils.showSessionExpireDialog(context)
    }

    fun saveArrayData(child: String, key: String, T: Any, iSaveDataCallback: ISaveDataCallback) {
        val user = TYMIApp.mFireBaseAuth?.currentUser
        if (user != null) {
            TYMIApp.mDataBase?.child(child)?.child(user.uid)?.child(key)?.setValue(T)?.
                    addOnSuccessListener {
                        iSaveDataCallback.onSaveDataCallback(user)
                    }?.
                    addOnFailureListener { e ->
                        DialogUtils.showAlertDialog(context, getString(R.string.app_name), e.message!!)
                    }
        } else
            DialogUtils.showSessionExpireDialog(context)
    }

    fun updateDataUserLevel(child: String, T: Any, iSaveDataCallback: ISaveDataCallback) {
        val user = TYMIApp.mFireBaseAuth?.currentUser
        if (user != null) {
            TYMIApp.mDataBase?.child(child)?.child(user.uid)?.setValue(T)?.
                    addOnSuccessListener {
                        iSaveDataCallback.onSaveDataCallback(user)
                    }?.
                    addOnFailureListener { e ->
                        DialogUtils.showAlertDialog(context, getString(R.string.app_name), e.message!!)
                    }
        } else
            DialogUtils.showSessionExpireDialog(context)
    }

    fun updateData(child: String, key: String, T: Any, iSaveDataCallback: ISaveDataCallback) {
        val user = TYMIApp.mFireBaseAuth?.currentUser
        if (user != null) {
            TYMIApp.mDataBase?.child(child)?.child(user.uid)?.child(key)?.
                    setValue(T)?.
                    addOnSuccessListener {
                        iSaveDataCallback.onSaveDataCallback(user)
                    }?.
                    addOnFailureListener { e ->
                        DialogUtils.showAlertDialog(context, getString(R.string.app_name), e.message!!)
                    }
        } else
            DialogUtils.showSessionExpireDialog(context)
    }
}