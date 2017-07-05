package com.tymi

object Constants {
    val SPLASH_TIME_OUT: Long = 2000
    val DATE_FORMAT = "dd/MM/yyyy"
    val DEFAULT_POSITION = -1
    val DEFAULT_LOOK_ID = "-1"
    val HOME_SPANS = 2
    val STATUS_OPEN = "1"
    val STATUS_CLOSE = "2"
    val ADS_INTERVAL = 3000000L
    val UPDATE_INTERVAL = (2.times(1000)).toLong()
    val FASTEST_INTERVAL: Long = 2000
    val NETWORK_TIME_OUT: Long = 45000

    object Extras {
        val POSITION = "POSITION"
        val EDIT = "EDIT"
        val EMAIL = "EMAIL"
    }

    object RequestCodes {
        val REGISTRATION = 0x100
        val FORGOT_PASSWORD = 0x101
        val PROFILE = 0x102
        val CHILD_PROFILE = 0x103
        val INCIDENT = 0x104
        val PERMISSIONS = 0x105
    }

    object DataBase {
        val ROLES = "Roles"
        val INCIDENTS = "Incidents"
        val USER_PROFILE = "UserProfile"
        val CHILD_PROFILES = "ChildProfiles"
        val INCIDENT_REPORTS = "IncidentReports"
    }
}