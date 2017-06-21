package com.tymi.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tymi.Constants
import com.tymi.R
import com.tymi.adapter.ChildProfilesAdapter
import com.tymi.interfaces.IViewProfileActivity
import kotlinx.android.synthetic.main.fragment_view_profiles.*


class ViewProfilesFragment : BaseFragment() {
    private var mChildProfilesAdapter: ChildProfilesAdapter? = null
    private var iViewProfileActivity: IViewProfileActivity? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IViewProfileActivity)
            iViewProfileActivity = context
    }

    override fun onDetach() {
        super.onDetach()
        iViewProfileActivity = null
    }

    override fun getContainerLayoutId(): Int {
        return R.layout.fragment_view_profiles
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        setProfile()
        child_profiles_recyclerView?.setHasFixedSize(true)
        child_profiles_recyclerView?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider_child_profile))
        child_profiles_recyclerView?.addItemDecoration(itemDecoration)
        mChildProfilesAdapter = ChildProfilesAdapter(context)
        mChildProfilesAdapter?.setData(getDataModel().childProfiles)
        child_profiles_recyclerView?.adapter = mChildProfilesAdapter
    }

    fun setProfile() {
        val profile = getDataModel().profile
        my_full_name?.text = profile?.fullName
        my_email?.text = profile?.email
        my_role?.text = profile?.role.toString()
        my_dob?.text = profile?.dateOfBirth
        my_profile_edit?.setOnClickListener {
            if (iViewProfileActivity != null)
                iViewProfileActivity?.onProfileSelection(Constants.RequestCodes.PROFILE, Constants.DEFAULT_ID)
        }
    }

    fun updateChildProfile() {
        mChildProfilesAdapter?.setData(getDataModel().childProfiles)
        mChildProfilesAdapter?.notifyDataSetChanged()
    }
}