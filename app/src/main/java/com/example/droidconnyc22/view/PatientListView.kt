package com.example.droidconnyc22.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.droidconnyc22.viewmodel.PatientViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun PatientListView(viewModel: PatientViewModel) {
    val patientItems = viewModel.patientListFlow.collectAsLazyPagingItems()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = false),
            onRefresh = { patientItems.refresh() }
        ) {
            LazyColumn {
                items(patientItems) { patient ->
                    patient?.let {
                        PatientListItemView(patientEntity = it, onBookmarkClicked = {
                            viewModel.onUserTappedOnBookmark(it)
                        })
                    }
                }

                when (patientItems.loadState.append) {
                    is LoadState.Error -> Unit
                    LoadState.Loading -> item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is LoadState.NotLoading -> Unit
                }
            }
        }

        if (patientItems.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator()
        }
    }
}