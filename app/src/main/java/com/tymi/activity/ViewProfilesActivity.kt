package com.tymi.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tymi.Constants
import com.tymi.R
import com.tymi.fragment.ViewProfilesFragment
import com.tymi.interfaces.IViewProfileActivity

class ViewProfilesActivity : BaseNavigationActivity(), IViewProfileActivity {
    private val TAG = ViewProfilesActivity::class.java.simpleName
    private var viewProfilesFragment: ViewProfilesFragment? = null

    override fun getContainerLayoutId(): Int {
        return R.layout.activity_view_profiles
    }

    override fun getToolBarTitle(): Int {
        return R.string.view_profiles
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationMenu()
        viewProfilesFragment = ViewProfilesFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_view_profiles, viewProfilesFragment, TAG)
                .commit()
    }

    override fun onProfileSelection(profileType: Int, position: Int) {
        when (profileType) {
            Constants.RequestCodes.PROFILE -> {
                val intent = Intent(this, RegistrationActivity::class.java)
                intent.putExtra(Constants.Extras.EDIT, true)
                startActivityForResult(intent, Constants.RequestCodes.PROFILE)
            }
            Constants.RequestCodes.CHILD_PROFILE -> {
                val intent = Intent(this, AddProfileActivity::class.java)
                intent.putExtra(Constants.Extras.POSITION, position)
                startActivityForResult(intent, Constants.RequestCodes.CHILD_PROFILE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.RequestCodes.PROFILE ->
                    viewProfilesFragment?.setProfile()
                Constants.RequestCodes.CHILD_PROFILE ->
                    viewProfilesFragment?.updateChildProfile()
            }
        }
    }
}