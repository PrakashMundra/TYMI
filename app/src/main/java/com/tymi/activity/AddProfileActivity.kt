package com.tymi.activity

import android.os.Bundle
import com.tymi.Constants
import com.tymi.R
import com.tymi.fragment.AddProfileFragment

class AddProfileActivity : BaseNavigationActivity() {
    private val TAG = AddProfileActivity::class.java.simpleName

    override fun getContainerLayoutId(): Int {
        return R.layout.activity_add_profile
    }

    override fun getToolBarTitle(): Int {
        return R.string.add_profile
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val addProfileFragment: AddProfileFragment
        if (intent != null && intent.extras != null) {
            setNavigationBack()
            val position = intent.extras.get(Constants.Extras.POSITION) as Int
            val isEdit = intent.extras.getBoolean(Constants.Extras.EDIT)
            addProfileFragment = AddProfileFragment.newInstance(position, isEdit)
        } else {
            setNavigationMenu()
            addProfileFragment = AddProfileFragment()
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_add_profile, addProfileFragment, TAG)
                .commit()
    }
}