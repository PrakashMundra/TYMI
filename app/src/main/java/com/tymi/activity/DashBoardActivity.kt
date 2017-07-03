package com.tymi.activity

import android.os.Bundle
import com.tymi.R
import com.tymi.fragment.DashBoardFragment
import com.tymi.interfaces.IDashBoardActivity
import com.tymi.utils.AlarmUtils

class DashBoardActivity : BaseNavigationActivity(), IDashBoardActivity {
    private val TAG = DashBoardActivity::class.java.simpleName

    override fun getContainerLayoutId(): Int {
        return R.layout.activity_dashboard
    }

    override fun getToolBarTitle(): Int {
        return R.string.dashboard
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationMenu()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_home, DashBoardFragment(), TAG)
                .commit()
        AlarmUtils.startAlarm(this)
    }

    override fun showDashBordItem(id: Int?) {
        onMenuItemSelected(id)
    }
}