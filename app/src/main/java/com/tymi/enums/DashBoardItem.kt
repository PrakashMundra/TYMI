package com.tymi.enums

import com.tymi.R

enum class DashBoardItem(var id: Int, var title: Int, var icon: Int) {
    ADD_PROFILE(R.id.add_profile, R.string.add_profile, R.drawable.ic_add_profile),
    VIEW_PROFILES(R.id.view_profiles, R.string.view_profiles, R.drawable.ic_view_profiles),
    ADD_INCIDENT(R.id.add_incident, R.string.add_incident, R.drawable.ic_add_incident),
    VIEW_INCIDENTS(R.id.view_incidents, R.string.view_incidents, R.drawable.ic_view_incidents),
    ABOUT_US(R.id.about_us, R.string.about_us, R.drawable.ic_about_us),
    CONTACT_US(R.id.contact_us, R.string.contact_us, R.drawable.ic_contact_us),
    LOGOUT(R.id.logout, R.string.logout, R.drawable.ic_logout)
}