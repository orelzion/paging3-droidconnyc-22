package com.example.droidconnyc22.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.droidconnyc22.viewmodel.PatientViewModel

@Composable
fun PatientListView(viewModel: PatientViewModel) {
    val patientItems = viewModel.patientListFlow.collectAsLazyPagingItems()

    LazyColumn {
        items(patientItems) { patient ->
            patient?.let {
                PatientListItemView(patientEntity = it, onBookmarkClicked = {

                })
            }
        }
    }
}