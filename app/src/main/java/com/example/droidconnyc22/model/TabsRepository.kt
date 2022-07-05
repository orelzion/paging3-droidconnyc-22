package com.example.droidconnyc22.model

class TabsRepository {
    suspend fun getTabs(): List<TabData> {
        // TODO
        return listOf(
            TabData(id = "1", title = "LVO Suspected"),
            TabData(id = "2", title = "CTP Suspected")
        )
    }
}