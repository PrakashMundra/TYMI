package com.tymi.enums

import com.tymi.R

enum class DashBoardItem(var id: Int, var title: Int, var icon: Int) {
    ADD_PROFILE(R.id.add_profile, R.string.add_profile, R.drawable.ic_add_profile),
    VIEW_PROFILES(R.id.view_profiles, R.string.view_profiles, R.drawable.ic_view_profiles),
    ADD_INCIDENT(R.id.add_incident, R.string.add_incident, R.drawable.ic_add_incident),
    VIEW_INCIDENTS(R.id.view_incidents, R.string.view_incidents, R.drawable.ic_view_incidents),
    LOGOUT(R.id.logout, R.string.logout, R.drawable.ic_logout)
}