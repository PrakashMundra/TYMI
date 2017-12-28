package com.tymi.activity

import android.os.Bundle
import com.tymi.Constants
import com.tymi.R
import com.tymi.fragment.AddIncidentFragment
import kotlinx.android.synthetic.main.actvity_navigation_base.*

class AddIncidentActivity : BaseNavigationActivity() {
    private val TAG = AddIncidentActivity::class.java.simpleName

    override fun getContainerLayoutId(): Int {
        return R.layout.activity_add_incident
    }

    override fun getToolBarTitle(): Int {
        return R.string.add_incident
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val addIncidentFragment: AddIncidentFragment
        if (intent != null && intent.extras != null) {
            setNavigationBack()
            val id = intent.extras.get(Constants.Extras.ID) as String
            val isEdit = intent.extras.get(Constants.Extras.EDIT) as Boolean
            addIncidentFragment = AddIncidentFragment.newInstance(id, isEdit)
            toolbar.title = if (isEdit) getString(R.string.edit_incident)
            else getString(R.string.view_incident)
        } else {
            setNavigationMenu()
            addIncidentFragment = AddIncidentFragment()
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_add_incident, addIncidentFragment, TAG)
                .commit()
    }
}