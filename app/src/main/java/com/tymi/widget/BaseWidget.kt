package com.tymi.widget

import android.content.Context
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

abstract class BaseWidget : FrameLayout {
    private var inflatedView: View? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        preInitAttributes(context, attrs, -1)
        init()
        processAttributes(context, attrs, -1)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        preInitAttributes(context, attrs, defStyleAttr)
        init()
        processAttributes(context, attrs, defStyleAttr)
    }

    open fun preInitAttributes(context: Context?, attrs: AttributeSet?, defStyle: Int?) {

    }

    open fun processAttributes(context: Context?, attrs: AttributeSet?, defStyle: Int?) {

    }

    open fun init() {
        val context = context
        if (context == null || getLayoutId() == 0)
            return
        inflatedView = LayoutInflater.from(context).inflate(getLayoutId(), this)
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int
}