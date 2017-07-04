package com.tymi.activity

import android.os.Bundle
import com.tymi.R
import com.tymi.fragment.AboutUsFragment

class AboutUsActivity : BaseNavigationActivity() {
    private val TAG = AboutUsActivity::class.java.simpleName

    override fun getContainerLayoutId(): Int {
        return R.layout.activity_about_us
    }

    override fun getToolBarTitle(): Int {
        return R.string.about_us
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationMenu()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_about_us, AboutUsFragment(), TAG)
                .commit()
    }
}