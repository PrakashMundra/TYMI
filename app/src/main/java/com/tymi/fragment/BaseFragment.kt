package com.tymi.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ScrollView
import com.tymi.controllers.DataController
import com.tymi.entity.DataModel
import com.tymi.interfaces.ContextHolder
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
}