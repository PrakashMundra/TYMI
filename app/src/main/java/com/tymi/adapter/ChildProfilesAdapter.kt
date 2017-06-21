package com.tymi.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tymi.Constants
import com.tymi.R
import com.tymi.entity.Profile
import com.tymi.interfaces.IViewProfileActivity
import kotlinx.android.synthetic.main.list_item_child_profile.view.*

class ChildProfilesAdapter(val context: Context) :
        RecyclerView.Adapter<ChildProfilesAdapter.ViewHolder>() {
    private val mProfiles: ArrayList<Profile> = ArrayList()
    private var iViewProfileActivity: IViewProfileActivity? = null

    init {
        if (context is IViewProfileActivity)
            iViewProfileActivity = context
    }

    fun setData(profiles: ArrayList<Profile>) {
        mProfiles.clear()
        mProfiles.addAll(profiles)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val childProfile = mProfiles[position]
        holder?.bindData(position, childProfile, iViewProfileActivity)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, position: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.list_item_child_profile, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mProfiles.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(position: Int, profile: Profile, iViewProfileActivity: IViewProfileActivity?) {
            with(profile) {
                itemView.child_full_name?.text = profile.fullName
                itemView.child_role?.text = profile.role.toString()
                itemView.child_dob?.text = profile.dateOfBirth
                itemView.child_edit?.tag = position
                itemView.child_edit?.setOnClickListener { v ->
                    iViewProfileActivity?.onProfileSelection(Constants.RequestCodes.CHILD_PROFILE, v.tag as Int)
                }
            }
        }
    }
}