package com.example.profiscooterex.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.profiscooterex.R
import com.example.profiscooterex.data.userDB.TripDetails
import com.example.profiscooterex.ui.auth.AuthViewModel
import com.example.profiscooterex.ui.destinations.DashboardScreenDestination
import com.example.profiscooterex.ui.destinations.DashboardTestScreenDestination
import com.example.profiscooterex.ui.destinations.LoginScreenDestination
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination
@Composable
fun HomeScreen(viewModel: AuthViewModel? = hiltViewModel(), userDataViewModel: DataViewModel? = hiltViewModel(), navigator: DestinationsNavigator) {
    val spacing = MaterialTheme.spacing
    val getUserData = userDataViewModel?.userDataState?.value
    val getTripData = userDataViewModel?.tripsDataState?.value ?: emptyList()

    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(
            space = 100.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        Text(
            text = stringResource(id = R.string.welcome_back),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp)) // Add spacing

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(spacing.medium)
                .padding(top = spacing.medium),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {

                Text(
                    text = getUserData?.nick ?: "",
                    //text = "",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(0.8f),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Column() {
                    Button(
                        onClick = {
                            viewModel?.logout()
                            /*navController.navigate(ROUTE_LOGIN) {
                                popUpTo(ROUTE_HOME) { inclusive = true }
                            }*/
                            navigator.navigate(LoginScreenDestination) {
                                popUpTo(LoginScreenDestination.route) {inclusive = true}
                            }
                        },
                        modifier = Modifier
                    ) {
                        Text(text = stringResource(id = R.string.logout))
                    }

                    Button(
                        onClick = {
                            navigator.navigate(DashboardScreenDestination) {
                                popUpTo(DashboardScreenDestination.route) {inclusive = true}
                            }
                        },
                        modifier = Modifier
                    ) {
                        Text(text = stringResource(id = R.string.dashboard))
                    }

                    Button(
                        onClick = {
                            navigator.navigate(DashboardTestScreenDestination) {
                                popUpTo(DashboardTestScreenDestination.route) {inclusive = true}
                            }
                        },
                        modifier = Modifier
                    ) {
                        Text(text = "DashboardTest")
                    }

                }

            }

        }
        /*Image(
            painter = painterResource(id = R.drawable.scooterruima),
            contentDescription = null,
            modifier = Modifier.height(306.dp)
        )*/

        Row()
        {
            HistoryTrips(getTripData)
        }
    }
}

@Composable
fun HistoryTrips(getTripData : List<TripDetails>) {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(16.dp) // Adjust the padding as needed
        ) {
            TextWithPadding(text = "Distance", modifier = Modifier.weight(1f) )
            TextWithPadding(text = "Time", modifier = Modifier.weight(1f) )
            TextWithPadding(text = "Speed", modifier = Modifier.weight(1f) )
            TextWithPadding(text = "Battery", modifier = Modifier.weight(1f) )
        }
        LazyColumn() {
            items(items = getTripData) { trip ->
                HistoryItem(trip)
            }
        }
    }
}

@Composable
private fun TextWithPadding(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodySmall,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

@Composable
fun HistoryItem(trip : TripDetails) {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .padding(top = 4.dp)

        ) {
            TextWithPadding(text = trip.tripName, modifier = Modifier.weight(1f) )
            TextWithPadding(text = trip.dateTime, modifier = Modifier.weight(1f) )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
        ) {
            TextWithPadding(text = trip.totalDistance, modifier = Modifier.weight(1f) )
            TextWithPadding(text = trip.distanceTime, modifier = Modifier.weight(1f) )
            TextWithPadding(text = trip.averageSpeed, modifier = Modifier.weight(1f) )
            TextWithPadding(text = trip.batteryDrain, modifier = Modifier.weight(1f) )

        }
    }
}

val sampleTrips = listOf(
    TripDetails(
        dateTime = "2023-08-21 10:00 AM",
        tripName = "Trip to Park",
        totalDistance = "10 km",
        distanceTime = "1 hour",
        averageSpeed = "10 km/h",
        batteryDrain = "30%"
    ),
    TripDetails(
        dateTime = "2023-08-22 2:30 PM",
        tripName = "City Exploration",
        totalDistance = "20 km",
        distanceTime = "2 hours",
        averageSpeed = "10 km/h",
        batteryDrain = "50%"
    ),
)

val sampleTrip =
    TripDetails(
        dateTime = "2023-08-21 10:00 AM",
        tripName = "Trip to Park",
        totalDistance = "10 km",
        distanceTime = "1 hour",
        averageSpeed = "10 km/h",
        batteryDrain = "30%"
    )


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun HomeScreenPreviewLight() {
    AppTheme {
        HomeScreen(null, navigator = EmptyDestinationsNavigator)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreviewDark() {
    AppTheme {
        HomeScreen(null, navigator = EmptyDestinationsNavigator)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun HistoryTripsPreview() {
    AppTheme {
        HistoryTrips(sampleTrips)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun HistoryItemPreview() {
    AppTheme {
        HistoryItem(sampleTrip)
    }
}
