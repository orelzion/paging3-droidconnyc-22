package com.example.droidconnyc22.model

import com.example.droidconnyc22.model.remote.PatientType

class TabsRepository {
    fun getTabs(): List<TabData> {
        return listOf(
            TabData(id = "1", title = "Bookmarks", PatientFilter.Bookmarks),
            TabData(id = "2", title = "All Cases", PatientFilter.All),
            TabData(id = "LVO", title = "LVO Suspected", PatientFilter.TypeFilter(PatientType.LVO)),
            TabData(id = "ICH", title = "ICH Suspected", PatientFilter.TypeFilter(PatientType.ICH)),
            TabData(id = "CTP", title = "CTP Suspected", PatientFilter.TypeFilter(PatientType.CTP))
        )
    }
}