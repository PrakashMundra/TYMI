package com.tymi.activity

import android.os.Bundle
import com.tymi.R
import com.tymi.fragment.ContactUsFragment

class ContactUsActivity : BaseNavigationActivity() {
    private val TAG = ContactUsActivity::class.java.simpleName

    override fun getContainerLayoutId(): Int {
        return R.layout.activity_contact_us
    }

    override fun getToolBarTitle(): Int {
        return R.string.contact_us
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationMenu()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_contact_us, ContactUsFragment(), TAG)
                .commit()
    }
}