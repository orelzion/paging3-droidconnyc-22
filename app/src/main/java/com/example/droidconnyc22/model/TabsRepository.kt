package com.example.droidconnyc22.model

class TabsRepository {
    fun getTabs(): List<TabData> {
        return listOf(
            TabData(id = "1", title = "Bookmarks"),
            TabData(id = "2", title = "All Cases"),
            TabData(id = "LVO", title = "LVO Suspected"),
            TabData(id = "ICH", title = "CTP Suspected")
        )
    }
}