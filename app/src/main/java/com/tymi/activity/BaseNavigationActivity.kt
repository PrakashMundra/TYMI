package com.tymi.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.tymi.AppPreferences
import com.tymi.R
import com.tymi.TYMIApp
import com.tymi.utils.DialogUtils
import com.tymi.utils.DrawableUtils
import kotlinx.android.synthetic.main.actvity_navigation_base.*


abstract class BaseNavigationActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val TAG = BaseNavigationActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_navigation_base)
        initViews()
        if (getContainerLayoutId() != 0) {
            try {
                activity_container?.addView(View.inflate(this, getContainerLayoutId(), null))
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }
    }

    private fun initViews() {
        navigationView?.menu?.findItem(TYMIApp.menuId)?.isChecked = true
        navigationView?.setNavigationItemSelectedListener(this)
    }

    protected fun setNavigationMenu() {
        if (getToolBarTitle() != 0)
            toolbar.title = getString(getToolBarTitle())
        setSupportActionBar(toolbar)
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawer_layout, toolbar, 0, 0) {
            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View?) {
                super.onDrawerOpened(drawerView)
            }
        }
        drawer_layout?.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    protected fun setNavigationBack() {
        if (getToolBarTitle() != 0)
            toolbar.title = getString(getToolBarTitle())
        toolbar.navigationIcon = DrawableUtils.getDrawable(getContext(), R.drawable.ic_arrow_back)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        drawer_layout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    protected abstract fun getToolBarTitle(): Int

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        onMenuItemSelected(itemId)
        return true
    }

    fun onMenuItemSelected(itemId: Int?) {
        val id = itemId as Int
        if (id != R.id.logout)
            TYMIApp.menuId = id
        when (itemId) {
            R.id.dashboard -> openActivity(this, DashBoardActivity::class.java)
            R.id.add_profile -> openActivity(this, AddProfileActivity::class.java)
            R.id.view_profiles -> openActivity(this, ViewProfilesActivity::class.java)
            R.id.add_incident -> openActivity(this, AddIncidentActivity::class.java)
            R.id.view_incidents -> openActivity(this, ViewIncidentsActivity::class.java)
            R.id.logout -> DialogUtils.showAlertDialog(this, R.string.logout, R.string.msg_logout,
                    R.string.yes, Runnable { logout() }, R.string.no)
        }
        drawer_layout?.closeDrawers()
    }

    fun openActivity(source: Activity, destination: Class<out Activity>) {
        if (source.javaClass != destination) {
            val intent = Intent(source, destination)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            source.startActivity(intent)
            if (source !is DashBoardActivity)
                source.finish()
        }
    }

    fun logout() {
        TYMIApp.mFireBaseAuth?.signOut()
        AppPreferences.getInstance(this).clearData()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}