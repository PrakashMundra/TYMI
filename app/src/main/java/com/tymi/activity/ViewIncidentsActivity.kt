package com.tymi.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import com.tymi.Constants
import com.tymi.R
import com.tymi.adapter.TabsAdapter
import com.tymi.fragment.ClosedIncidentsFragment
import com.tymi.fragment.OpenIncidentsFragment
import com.tymi.interfaces.IViewIncidentsActivity
import kotlinx.android.synthetic.main.activity_view_incidents.*
import kotlinx.android.synthetic.main.list_item_tab.view.*

class ViewIncidentsActivity : BaseNavigationActivity(), IViewIncidentsActivity,
        ViewPager.OnPageChangeListener {
    private var mTabsAdapter: TabsAdapter? = null
    private var isDataUpdated = false

    override fun getContainerLayoutId(): Int {
        return R.layout.activity_view_incidents
    }

    override fun getToolBarTitle(): Int {
        return R.string.view_incidents
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationMenu()
        initViews()
    }

    private fun initViews() {
        setupViewPager()
        incidents_tab_layout?.setupWithViewPager(incidents_viewpager)
        setupTabs()
    }

    private fun setupViewPager() {
        mTabsAdapter = TabsAdapter(supportFragmentManager)
        mTabsAdapter?.addFragment(OpenIncidentsFragment(), getString(R.string.open_incidents))
        mTabsAdapter?.addFragment(ClosedIncidentsFragment(), getString(R.string.closed_incidents))
        incidents_viewpager?.offscreenPageLimit = mTabsAdapter?.count as Int
        incidents_viewpager?.adapter = mTabsAdapter
        incidents_viewpager?.addOnPageChangeListener(this)
    }

    private fun setupTabs() {
        val titles = resources.getStringArray(R.array.incidents_tabs)
        for ((index, title) in titles.withIndex()) {
            val tab = incidents_tab_layout?.getTabAt(index)
            tab?.customView = prepareTabView(title)
        }
    }

    private fun prepareTabView(title: String): View {
        val view = View.inflate(getContext(), R.layout.list_item_tab, null)
        view.tab_title.text = title
        return view
    }

    override fun onIncidentSelection(position: Int, isEdit: Boolean) {
        val intent = Intent(this, AddIncidentActivity::class.java)
        intent.putExtra(Constants.Extras.POSITION, position)
        intent.putExtra(Constants.Extras.EDIT, isEdit)
        startActivityForResult(intent, Constants.RequestCodes.INCIDENT)
    }

    override fun onPageScrollStateChanged(p0: Int) {

    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

    }

    override fun onPageSelected(position: Int) {
        if (isDataUpdated) {
            val fragment = mTabsAdapter?.getItem(position)
            if (fragment is OpenIncidentsFragment)
                fragment.updateData()
            else if (fragment is ClosedIncidentsFragment)
                fragment.updateData()
            isDataUpdated = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.RequestCodes.INCIDENT -> {
                    isDataUpdated = true
                    val position = incidents_viewpager?.currentItem as Int
                    val fragment = mTabsAdapter?.getItem(position)
                    if (fragment is OpenIncidentsFragment)
                        fragment.updateData()
                    else if (fragment is ClosedIncidentsFragment)
                        fragment.updateData()
                }
            }
        }
    }
}