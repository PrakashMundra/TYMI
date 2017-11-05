package com.tymi.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.tymi.AppPreferences
import com.tymi.R
import com.tymi.TYMIApp
import com.tymi.controllers.DataController
import com.tymi.utils.AlarmUtils
import com.tymi.utils.DialogUtils
import com.tymi.utils.DrawableUtils
import com.tymi.utils.NetworkUtils
import kotlinx.android.synthetic.main.actvity_navigation_base.*
import kotlinx.android.synthetic.main.header_view_navigation.view.*


abstract class BaseNavigationActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val TAG = BaseNavigationActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_navigation_base)
        if (getContainerLayoutId() != 0) {
            try {
                activity_container?.addView(View.inflate(this, getContainerLayoutId(), null))
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }
    }

    protected fun setNavigationMenu() {
        initNavigationViews()
        if (getToolBarTitle() != 0)
            toolbar.title = getString(getToolBarTitle())
        setSupportActionBar(toolbar)
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawer_layout, toolbar, 0, 0) {

        }
        drawer_layout?.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun initNavigationViews() {
        updateNavigationSelection()
        navigationView?.setNavigationItemSelectedListener(this)
        setHeaderData()
    }

    protected fun updateNavigationSelection() {
        navigationView?.menu?.findItem(TYMIApp.menuId)?.isChecked = true
    }

    private fun setHeaderData() {
        val dataModel = DataController.getInstance().dataModel
        if (dataModel != null) {
            val userProfile = dataModel.profile
            if (userProfile != null) {
                val headerView = navigationView?.inflateHeaderView(R.layout.header_view_navigation) as View
                headerView.nav_header_name?.text = SpannableStringBuilder(userProfile.fullName)
                headerView.nav_header_email?.text = SpannableStringBuilder(userProfile.email)
            }
        }
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
        when (itemId) {
            R.id.dashboard -> openActivity(itemId, this, DashBoardActivity::class.java)
            R.id.add_profile -> checkNetworkAndOpenActivity(itemId, this, AddProfileActivity::class.java)
            R.id.view_profiles -> checkNetworkAndOpenActivity(itemId, this, ViewProfilesActivity::class.java)
            R.id.add_incident -> checkNetworkAndOpenActivity(itemId, this, AddIncidentActivity::class.java)
            R.id.view_incidents -> checkNetworkAndOpenActivity(itemId, this, ViewIncidentsActivity::class.java)
            R.id.about_us -> openActivity(itemId, this, AboutUsActivity::class.java)
            R.id.contact_us -> openActivity(itemId, this, ContactUsActivity::class.java)
            R.id.logout -> {
                if (NetworkUtils.isNetworkAvailable(this))
                    DialogUtils.showAlertDialog(this, R.string.logout, R.string.msg_logout,
                            R.string.yes, Runnable { logout() }, R.string.no)
            }
        }
        drawer_layout?.closeDrawers()
    }

    fun checkNetworkAndOpenActivity(itemId: Int?, source: Activity, destination: Class<out Activity>) {
        if (NetworkUtils.isNetworkAvailable(this))
            openActivity(itemId, source, destination)
        else
            Handler().postDelayed({ updateNavigationSelection() }, 1000)
    }

    private fun openActivity(itemId: Int?, source: Activity, destination: Class<out Activity>) {
        if (source.javaClass != destination) {
            TYMIApp.menuId = itemId as Int
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
        AlarmUtils.stopAlarm(this)
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (drawer_layout?.isDrawerOpen(navigationView)!!)
            drawer_layout?.closeDrawers()
        else
            super.onBackPressed()
    }
}