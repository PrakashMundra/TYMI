package com.tymi.activity

import android.os.Bundle
import com.tymi.Constants
import com.tymi.R
import com.tymi.fragment.ForgotPasswordFragment

class ForgotPasswordActivity : BaseNavigationActivity() {
    private val TAG = ForgotPasswordActivity::class.java.simpleName

    override fun getContainerLayoutId(): Int {
        return R.layout.activity_forgot_password
    }

    override fun getToolBarTitle(): Int {
        return R.string.forgot_password
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationBack()
        var email = ""
        if (intent != null && intent.extras != null)
            email = intent.extras.getString(Constants.Extras.EMAIL, "")
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_forgot_password, ForgotPasswordFragment.newInstance(email), TAG)
                .commit()
    }
}