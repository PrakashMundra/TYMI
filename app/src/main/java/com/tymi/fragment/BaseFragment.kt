package com.tymi.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tymi.controllers.DataController
import com.tymi.entity.DataModel
import com.tymi.interfaces.ContextHolder
import com.tymi.interfaces.IDataCallback
import com.tymi.interfaces.ISaveDataCallback
import com.tymi.widget.SpinnerWidget


abstract class BaseFragment : Fragment(), ContextHolder {
    var mScrollView: ScrollView? = null
    var mErrorView: View? = null
    var mFireBaseAuth: FirebaseAuth? = null
    var mDataBase: DatabaseReference? = null

    override fun getContext(): Context {
        return activity
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(getContainerLayoutId(), container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFireBaseAuth = FirebaseAuth.getInstance()
        mDataBase = FirebaseDatabase.getInstance().reference
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

    fun loadDataWithoutUser(child: String, iDataCallback: IDataCallback) {
        mDataBase?.child(child)?.
                addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                    }

                    override fun onDataChange(data: DataSnapshot) {
                        iDataCallback.onDataCallback(null, data)
                    }
                })
    }

    fun loadData(child: String, iDataCallback: IDataCallback) {
        val user = mFireBaseAuth?.currentUser
        if (user != null) {
            mDataBase?.child(child)?.child(user.uid)?.
                    addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError?) {
                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataChange(data: DataSnapshot) {
                            iDataCallback.onDataCallback(user, data)
                        }
                    })
        }
    }

    fun saveArrayData(child: String, T: Any, iSaveDataCallback: ISaveDataCallback) {
        val user = mFireBaseAuth?.currentUser
        if (user != null) {
            mDataBase?.child(child)?.child(user.uid)?.push()?.setValue(T)?.
                    addOnSuccessListener {
                        iSaveDataCallback.onSaveDataCallback(user, true)
                    }?.
                    addOnFailureListener {
                        iSaveDataCallback.onSaveDataCallback(user, true)
                    }
        }
    }
}