package com.tymi.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tymi.Constants
import com.tymi.R
import com.tymi.entity.Incident
import com.tymi.interfaces.IViewIncidentsActivity
import com.tymi.utils.NumberUtils
import kotlinx.android.synthetic.main.list_item_incident.view.*

class IncidentsAdapter(val context: Context, private val mIncidents: ArrayList<Incident>) :
        RecyclerView.Adapter<IncidentsAdapter.ViewHolder>() {
    private var iViewIncidentsActivity: IViewIncidentsActivity? = null

    init {
        if (context is IViewIncidentsActivity)
            iViewIncidentsActivity = context
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindData(mIncidents[position], iViewIncidentsActivity)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, position: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.list_item_incident, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mIncidents.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(incident: Incident, iViewIncidentsActivity: IViewIncidentsActivity?) {
            with(incident) {
                itemView.setOnClickListener { v ->
                    val pos = v.item_edit.tag as String
                    iViewIncidentsActivity?.onIncidentSelection(pos, false)
                }
                itemView.item_profile.text = incident.profile.title
                itemView.item_incident.text = incident.incident.title
                if (incident.statusId.contentEquals(Constants.STATUS_OPEN)) {
                    itemView.item_start_date.text = incident.startDate
                    itemView.item_end_date_layout.visibility = View.GONE
                } else {
                    itemView.item_start_date.text = incident.startDate
                    itemView.item_end_date.text = incident.endDate
                    itemView.item_end_date_layout.visibility = View.VISIBLE
                }
                val expenses = incident.expenses
                if (expenses.isEmpty())
                    itemView.item_expenses.text = "0"
                else
                    itemView.item_expenses.text = NumberUtils.getFormattedNumber(expenses)
                itemView.item_edit.tag = incident.id
                itemView.item_edit.setOnClickListener { v ->
                    iViewIncidentsActivity?.onIncidentSelection(v.tag as String, true)
                }
            }
        }
    }

    fun updateData(incidents: ArrayList<Incident>) {
        mIncidents.clear()
        mIncidents.addAll(incidents)
        notifyDataSetChanged()
    }
}