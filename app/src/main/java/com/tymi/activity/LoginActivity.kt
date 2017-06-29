package com.tymi.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tymi.Constants
import com.tymi.R
import com.tymi.fragment.LoginFragment
import com.tymi.interfaces.ILoginActivity

class LoginActivity : BaseActivity(), ILoginActivity {
    private val TAG = LoginActivity::class.java.simpleName
    private var mLoginFragment: LoginFragment? = null

    override fun getContainerLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLoginFragment = LoginFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_login, mLoginFragment, TAG)
                .commit()
    }

    override fun showHome() {
        val homeIntent = Intent(this, DashBoardActivity::class.java)
        startActivity(homeIntent)
        finish()
    }

    override fun showForgotPassword(email: String) {
        val registrationIntent = Intent(this, ForgotPasswordActivity::class.java)
        registrationIntent.putExtra(Constants.Extras.EMAIL, email)
        startActivityForResult(registrationIntent, Constants.RequestCodes.FORGOT_PASSWORD)
    }

    override fun showRegistration() {
        val registrationIntent = Intent(this, RegistrationActivity::class.java)
        startActivityForResult(registrationIntent, Constants.RequestCodes.REGISTRATION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.RequestCodes.REGISTRATION ||
                    requestCode == Constants.RequestCodes.FORGOT_PASSWORD)
                mLoginFragment?.clearFields()
        }
    }
}