package com.tymi.enums

import android.content.Context
import com.tymi.Constants
import com.tymi.R
import com.tymi.entity.LookUp

enum class IncidentStatus(var id: Int, var title: Int) {
    OPEN(Constants.STATUS_OPEN, R.string.open),
    CLOSE(Constants.STATUS_CLOSE, R.string.close);

    companion object {
        fun getStatusLookUp(context: Context): ArrayList<LookUp> {
            val values = ArrayList<LookUp>()
            val statuses = IncidentStatus.values()
            statuses.forEach { status ->
                values.add(LookUp(status.id, context.getString(status.title)))
            }
            return values
        }
    }
}