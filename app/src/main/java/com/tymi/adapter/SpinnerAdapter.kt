package com.tymi.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.tymi.R
import com.tymi.utils.DrawableUtils

class SpinnerAdapter<E> : ArrayAdapter<E> {
    constructor(context: Context, resource: Int, objects: List<E>) : super(context, resource, objects)

    constructor(context: Context, resource: Int) : super(context, resource)

    constructor(context: Context, resource: Int, textViewResourceId: Int) : super(context, resource, textViewResourceId)

    constructor(context: Context, resource: Int, objects: Array<E>) : super(context, resource, objects)

    constructor(context: Context, resource: Int, textViewResourceId: Int, objects: Array<E>) : super(context, resource, textViewResourceId, objects)

    constructor(context: Context, resource: Int, textViewResourceId: Int, objects: List<E>) : super(context, resource, textViewResourceId, objects)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view = super.getView(position, convertView, parent)
        if (view is TextView) {
            DrawableUtils.setCompoundDrawableLocaleInverse(view, R.drawable.ic_spinner_dropdown)
        }
        return view
    }
}
