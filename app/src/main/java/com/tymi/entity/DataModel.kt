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

    fun getFilteredIncidents(id: String): ArrayList<Incident> {
        return incidents.filter { it.statusId.contentEquals(id) } as ArrayList<Incident>
    }

    fun getIncident(id: String): Incident {
        return incidents.filter { it.id.contentEquals(id) }.single()
    }

    fun updateIncident(updatedIncident: Incident) {
        incidents.forEachIndexed { index, incident ->
            if (incident.id.contentEquals(updatedIncident.id)) {
                incidents[index] = updatedIncident
                return
            }
        }
    }
}