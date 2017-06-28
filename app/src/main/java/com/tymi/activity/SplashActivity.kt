package com.tymi.activity

import android.content.Intent
import android.os.Bundle
import com.tymi.R
import com.tymi.fragment.SplashFragment
import com.tymi.interfaces.ISplashActivity

class SplashActivity : BaseActivity(), ISplashActivity {
    private val TAG = SplashActivity::class.java.simpleName

    override fun getContainerLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_splash, SplashFragment(), TAG)
                .commit()
    }

    override fun showLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    override fun showDashBoard() {
        val dashBoardIntent = Intent(this, DashBoardActivity::class.java)
        startActivity(dashBoardIntent)
        finish()
    }
}