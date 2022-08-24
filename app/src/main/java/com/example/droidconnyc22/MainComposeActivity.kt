package com.example.droidconnyc22

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.droidconnyc22.ui.theme.DroidconNYC22Theme
import com.example.droidconnyc22.view.PatientListView
import com.example.droidconnyc22.viewmodel.PatientViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainComposeActivity : ComponentActivity() {

    private val patientViewModel by viewModel<PatientViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DroidconNYC22Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Tabs(viewModel = patientViewModel)
                        PatientListView(viewModel = patientViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Tabs(viewModel: PatientViewModel) {
    var tabIndex by remember { mutableStateOf(1) }
    val tabTitles = viewModel.viewState.collectAsState().value.tabs.map { it.title }

    Column {
        TabRow(selectedTabIndex = tabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(selected = tabIndex == index,
                    onClick = {
                        tabIndex = index
                        viewModel.onTabSelected(tabIndex)
                    },
                    text = { Text(text = title) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DroidconNYC22Theme {
    }
}