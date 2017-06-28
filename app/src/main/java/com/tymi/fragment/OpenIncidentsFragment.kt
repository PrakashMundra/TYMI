package com.tymi.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tymi.Constants
import com.tymi.R
import com.tymi.adapter.IncidentsAdapter
import kotlinx.android.synthetic.main.fragment_open_incidents.*

class OpenIncidentsFragment : BaseFragment() {
    override fun getContainerLayoutId(): Int {
        return R.layout.fragment_open_incidents
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        incidents_recyclerView?.hasFixedSize()
        incidents_recyclerView?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider_child_profile))
        incidents_recyclerView?.addItemDecoration(itemDecoration)
        incidents_recyclerView?.adapter = IncidentsAdapter(context,
                getDataModel().getFilteredIncidents(Constants.STATUS_OPEN))
    }

    fun updateData() {
        if (incidents_recyclerView != null && incidents_recyclerView?.adapter != null) {
            val adapter = incidents_recyclerView?.adapter as IncidentsAdapter
            adapter.updateData(getDataModel().getFilteredIncidents(Constants.STATUS_OPEN))
        }
    }
}