package com.tymi.activity

import android.os.Bundle
import com.tymi.Constants
import com.tymi.R
import com.tymi.fragment.RegistrationFragment
import kotlinx.android.synthetic.main.actvity_navigation_base.*

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
        val registrationFragment: RegistrationFragment
        if (intent != null && intent.extras != null) {
            val isEdit = intent.extras.get(Constants.Extras.EDIT) as Boolean
            registrationFragment = RegistrationFragment.newInstance(isEdit)
            if (isEdit)
                toolbar.title = getString(R.string.edit_profile)
        } else
            registrationFragment = RegistrationFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_registration, registrationFragment, TAG)
                .commit()
    }
}