package com.tymi.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.tymi.Constants
import com.tymi.R
import com.tymi.adapter.DashBoardAdapter
import com.tymi.enums.DashBoardItem
import com.tymi.widget.HomeItemDecoration
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashBoardFragment : BaseFragment() {
    override fun getContainerLayoutId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        home_recyclerView?.setHasFixedSize(true)
        home_recyclerView?.layoutManager = GridLayoutManager(context, Constants.HOME_SPANS)
        val spacing = resources.getDimensionPixelSize(R.dimen.margin)
        home_recyclerView?.addItemDecoration(HomeItemDecoration(Constants.HOME_SPANS, spacing, false))
        home_recyclerView?.adapter = DashBoardAdapter(context, DashBoardItem.values())
    }
}