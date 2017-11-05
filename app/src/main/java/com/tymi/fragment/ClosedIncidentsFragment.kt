package com.tymi.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tymi.Constants
import com.tymi.R
import com.tymi.adapter.IncidentsAdapter
import com.tymi.entity.Incident
import kotlinx.android.synthetic.main.inc_incidents.*
import kotlinx.android.synthetic.main.inc_no_data_view.*

class ClosedIncidentsFragment : BaseFragment() {
    override fun getContainerLayoutId(): Int {
        return R.layout.fragment_closed_incidents
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        incidents_recyclerView?.hasFixedSize()
        incidents_recyclerView?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        val drawable = ContextCompat.getDrawable(context, R.drawable.divider_child_profile) as Drawable
        itemDecoration.setDrawable(drawable)
        incidents_recyclerView?.addItemDecoration(itemDecoration)
        val incidents = getDataModel().getFilteredIncidents(Constants.STATUS_CLOSE)
        incidents_recyclerView?.adapter = IncidentsAdapter(context, incidents)
        showNoData(incidents)
    }

    fun updateData() {
        if (incidents_recyclerView != null && incidents_recyclerView?.adapter != null) {
            val adapter = incidents_recyclerView?.adapter as IncidentsAdapter
            val incidents = getDataModel().getFilteredIncidents(Constants.STATUS_CLOSE)
            adapter.updateData(incidents)
            showNoData(incidents)
        }
    }

    private fun showNoData(list: ArrayList<Incident>) {
        if (list.size > 0)
            no_data?.visibility = View.GONE
        else
            no_data?.visibility = View.VISIBLE
    }
}