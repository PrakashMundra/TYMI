package com.tymi.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class HomeItemDecoration(spanCount: Int, spacing: Int, includeEdge: Boolean) : RecyclerView.ItemDecoration() {
    private var spanCount: Int = 0
    private var spacing: Int = 0
    private var includeEdge: Boolean = false

    init {
        this.spanCount = spanCount
        this.spacing = spacing
        this.includeEdge = includeEdge
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent?.getChildAdapterPosition(view) // item position
        val column = position?.rem(spanCount) // item column
        if (includeEdge) {
            outRect?.left = spacing.minus((column?.times(spacing))?.div(spanCount) as Int)
            outRect?.right = ((column.plus(1)).times(spacing)).div(spanCount)
            if (position < spanCount) // top edge
                outRect?.top = spacing
            outRect?.bottom = spacing // item bottom
        } else {
            outRect?.left = (column?.times(spacing))?.div(spanCount)
            outRect?.right = spacing.minus((((column?.plus(1))?.times(spacing))?.div(spanCount)) as Int)
            if (position >= spanCount)
                outRect?.top = spacing
        }
    }
}