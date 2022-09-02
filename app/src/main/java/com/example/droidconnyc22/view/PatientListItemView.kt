package com.example.droidconnyc22.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.droidconnyc22.BuildConfig
import com.example.droidconnyc22.R
import com.example.droidconnyc22.model.db.PatientEntity
import kotlinx.datetime.Instant


@Composable
fun PatientListItemView(
    patientEntity: PatientEntity,
    modifier: Modifier = Modifier,
    onBookmarkClicked: (PatientEntity) -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column {
            AsyncImage(
                model = BuildConfig.LOCAL_IP + patientEntity.photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                fallback = painterResource(id = R.drawable.no_brain),
                modifier = Modifier.fillMaxWidth().height(240.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = patientEntity.name, Modifier.weight(0.85f))
                BookmarkDetails(patientEntity, onBookmarkClicked, Modifier.weight(0.15f))
            }
        }
    }
}

@Composable
private fun BookmarkDetails(
    patientEntity: PatientEntity,
    onBookmarkClicked: (PatientEntity) -> Unit,
    modifier: Modifier
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(
                id =
                if (patientEntity.isBookmarked) R.drawable.ic_baseline_bookmark_24
                else R.drawable.ic_baseline_bookmark_border_24
            ),
            contentDescription = "bookmark",
            Modifier.clickable {
                onBookmarkClicked(patientEntity)
            }
        )
        Text(text = patientEntity.bookmarkCount.toString())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPatientListItemView() {
    return PatientListItemView(
        PatientEntity(
            12,
            "asdad",
            name = "Orel Zion",
            5,
            true,
            "",
            "filter",
            Instant.DISTANT_PAST
        )
    ) { entity ->

    }
}