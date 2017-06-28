package com.tymi.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tymi.Constants
import com.tymi.R
import com.tymi.adapter.TabsAdapter
import com.tymi.controllers.DataController
import com.tymi.entity.Incident
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
        loadIncidents()
    }

    private fun loadIncidents() {
        val incidents = DataController.getInstance().dataModel?.incidents
        incidents?.clear()
        val fireBaseAuth = FirebaseAuth.getInstance()
        val dataBase = FirebaseDatabase.getInstance().reference
        val user = fireBaseAuth?.currentUser
        if (user != null) {
            dataBase?.child(Constants.DataBase.INCIDENT_REPORTS)?.child(user.uid)?.
                    addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError?) {
                            Toast.makeText(this@ViewIncidentsActivity, error.toString(), Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataChange(data: DataSnapshot) {
                            data.children.forEach { child ->
                                val incident = child.getValue(Incident::class.java)
                                incidents?.add(incident)
                            }
                            updateData(0)
                        }
                    })
        } else
            Toast.makeText(this, "Session has been Expired", Toast.LENGTH_SHORT).show()
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
            updateData(position)
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
                    updateData(position)
                }
            }
        }
    }

    private fun updateData(position: Int) {
        val fragment = mTabsAdapter?.getItem(position)
        if (fragment is OpenIncidentsFragment)
            fragment.updateData()
        else if (fragment is ClosedIncidentsFragment)
            fragment.updateData()
    }
}