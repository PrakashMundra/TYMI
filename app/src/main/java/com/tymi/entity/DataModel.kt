package com.tymi.entity

class DataModel {
    var roles = ArrayList<LookUp>()
        get() = field

    var profile: Profile? = null
        get() = field

    var childProfiles = ArrayList<Profile>()
        get() = field

    var profileLookUps = ArrayList<LookUp>()
        get() {
            val lookUps = ArrayList<LookUp>()
            if (profile != null)
                lookUps.add(LookUp(profile?.id!!, profile?.fullName!!))
            childProfiles.forEach { (id, name) ->
                lookUps.add(LookUp(id, name))
            }
            return lookUps
        }

    var incidentLookUps = ArrayList<LookUp>()
        get() = field

    var incidents = ArrayList<Incident>()
        get() = field

    fun getFilteredIncidents(id: Int): ArrayList<Incident> {
        return incidents.filter { it.statusId == id } as ArrayList<Incident>
    }
}