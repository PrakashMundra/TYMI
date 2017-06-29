package com.tymi.activity

import android.os.Bundle
import com.tymi.R
import com.tymi.fragment.RegistrationFragment

class RegistrationActivity : BaseNavigationActivity() {
    private val TAG = RegistrationActivity::class.java.simpleName

    override fun getContainerLayoutId(): Int {
        return R.layout.activity_registration
    }

    override fun getToolBarTitle(): Int {
        return R.string.registration
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationBack()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_registration, RegistrationFragment(), TAG)
                .commit()
    }
}