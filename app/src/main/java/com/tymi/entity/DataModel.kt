package com.tymi.entity

class DataModel {
    var roles = ArrayList<LookUp>()
    var profile: Profile? = null
    var childProfiles = ArrayList<Profile>()
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
    var incidents = ArrayList<Incident>()

    fun getFilteredIncidents(id: String): ArrayList<Incident> {
        return incidents.filter { it.statusId.contentEquals(id) } as ArrayList<Incident>
    }

    fun getIncident(id: String): Incident {
        return incidents.find { it.id.contentEquals(id) } as Incident
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