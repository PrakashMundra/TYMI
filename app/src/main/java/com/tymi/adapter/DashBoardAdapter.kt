package com.tymi.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tymi.R
import com.tymi.enums.DashBoardItem
import com.tymi.interfaces.IDashBoardActivity
import com.tymi.utils.DrawableUtils
import kotlinx.android.synthetic.main.list_item_dashboard.view.*

class DashBoardAdapter(val context: Context, val items: Array<DashBoardItem>) :
        RecyclerView.Adapter<DashBoardAdapter.ViewHolder>() {
    private var iDashBoardActivity: IDashBoardActivity? = null

    init {
        if (this.context is IDashBoardActivity)
            this.iDashBoardActivity = context
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val dashBoardItem = items[position]
        holder?.bindData(context, dashBoardItem, iDashBoardActivity)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, position: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.list_item_dashboard, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(context: Context, item: DashBoardItem, iDashBoardActivity: IDashBoardActivity?) {
            with(item) {
                itemView.dashboard_title?.text = context.getString(item.title)
                itemView.dashboard_icon?.setImageDrawable(DrawableUtils.getDrawable(context, item.icon))
                itemView.setOnClickListener {
                    iDashBoardActivity?.showDashBordItem(item.id)
                }
            }
        }
    }
}