package com.tymi.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tymi.interfaces.ContextHolder

abstract class BaseActivity : AppCompatActivity(), ContextHolder {
    override fun getContext(): Context {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContainerLayoutId())
    }

    protected abstract fun getContainerLayoutId(): Int
}