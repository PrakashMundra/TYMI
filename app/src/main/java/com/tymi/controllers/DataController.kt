package com.tymi.controllers

import com.tymi.entity.DataModel
import com.tymi.entity.LookUp


object DataController {
    private var instance: DataController? = null
    var dataModel: DataModel? = null
        get() = field
    private var lookUpsCache: MutableMap<String, List<LookUp>>? = null

    fun getInstance(): DataController {
        if (instance == null)
            instance = DataController
        return DataController.instance!!
    }

    fun initialize(): DataController {
        dataModel = DataModel()
        lookUpsCache = HashMap()
        return this
    }

    fun putLookUp(name: String, lookUps: ArrayList<LookUp>) {
        if (lookUpsCache != null)
            lookUpsCache?.put(name, lookUps)
    }

    fun getLookUp(name: String): ArrayList<LookUp> {
        var values = ArrayList<LookUp>()
        if (lookUpsCache != null && lookUpsCache?.containsKey(name) as Boolean)
            values = lookUpsCache?.get(name) as ArrayList<LookUp>
        return values
    }
}