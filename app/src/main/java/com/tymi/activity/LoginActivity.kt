package com.tymi.activity

import android.content.Intent
import android.os.Bundle
import com.tymi.R
import com.tymi.fragment.LoginFragment
import com.tymi.interfaces.ILoginActivity

class LoginActivity : BaseActivity(), ILoginActivity {
    private val TAG = LoginActivity::class.java.simpleName

    override fun getContainerLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_login, LoginFragment(), TAG)
                .commit()
    }

    override fun showHome() {
        val homeIntent = Intent(this, DashBoardActivity::class.java)
        startActivity(homeIntent)
        finish()
    }

    override fun showForgotPassword(email: String) {

    }

    override fun showRegistration() {
        val registrationIntent = Intent(this, RegistrationActivity::class.java)
        startActivity(registrationIntent)
    }
}