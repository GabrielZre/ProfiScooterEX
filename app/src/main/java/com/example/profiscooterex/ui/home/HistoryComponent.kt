/*
package com.example.profiscooterex.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.profiscooterex.data.Trip
import com.example.profiscooterex.ui.theme.AppTheme

@Composable
fun HistoryComponent(viewModel: HomeViewModel) {
    val historyItems = viewModel.historyItems

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(historyItems) { trip ->
            HistoryItem(trip)
        }
    }
}

@Composable
fun HistoryItem(trip: Trip) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = trip.tripName,
                )
                Text(
                    text = trip.dateTime,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = trip.totalDistance,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = trip.distanceTime,
                    modifier = Modifier.weight(0.9f)
                )
                Text(
                    text = trip.averageSpeed,
                    modifier = Modifier.weight(1.2f)
                )
                Text(
                    text = trip.batteryDrain,
                    modifier = Modifier.weight(0.9f)
                )
            }
        }
    }
}

*/
/*@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun HistoryItemPreview() {
    val viewModel = HomeViewModel()
    CompositionLocalProvider(LocalViewModel provides viewModel) {
        AppTheme {
            HistoryItem()
        }
    }
}

@Composable
fun HistoryItem() {
    val viewModel = LocalViewModel.current
    val historyItems = viewModel.historyItems

    // reszta kodu HistoryItem
    // ...
}
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun HistoryItemPreview() {
    AppTheme {
        HistoryItem()
    }
}*/

