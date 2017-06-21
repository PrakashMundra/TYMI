package com.tymi.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View


class RecyclerItemClickListener(context: Context, listener: ItemClickListener) : RecyclerView.OnItemTouchListener {
    private var mListener: ItemClickListener? = null
    private var mGestureDetector: GestureDetector? = null
    private var mView: View? = null
    private var mPosition: Int = 0

    init {
        this.mListener = listener
        mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                if (mView != null)
                    mListener?.onItemClick(mView, mPosition)
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                if (mView != null)
                    mListener?.onItemLongClick(mView, mPosition)
            }
        })
    }

    override fun onTouchEvent(p0: RecyclerView?, p1: MotionEvent?) {

    }

    override fun onInterceptTouchEvent(view: RecyclerView?, e: MotionEvent?): Boolean {
        mView = view?.findChildViewUnder(e?.x as Float, e.y)
        mPosition = if (mView != null) view?.getChildAdapterPosition(mView) as Int else -1
        return mView != null && mGestureDetector?.onTouchEvent(e) as Boolean
    }

    override fun onRequestDisallowInterceptTouchEvent(p0: Boolean) {

    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)

        fun onItemLongClick(view: View?, position: Int)
    }
}