package com.tymi.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View

class GenericTextWatcher(view: View, eventHandler: TextWatcherHandler) : TextWatcher {
    private var mEventHandler: TextWatcherHandler? = null
    private var mView: View? = null

    init {
        this.mView = view
        this.mEventHandler = eventHandler
    }

    override fun afterTextChanged(p0: Editable?) {
        mEventHandler?.afterTextChanged(mView)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    interface TextWatcherHandler {
        fun beforeTextChanged()
        fun onTextChanged()
        fun afterTextChanged(view: View?)
    }
}