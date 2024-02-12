package com.example.profiscooterex.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.profiscooterex.R
import com.example.profiscooterex.data.DataViewModel
import com.example.profiscooterex.data.userDB.Scooter
import com.example.profiscooterex.data.userDB.TripDetails
import com.example.profiscooterex.data.userDB.User
import com.example.profiscooterex.navigation.ContentNavGraph
import com.example.profiscooterex.ui.auth.AuthViewModel
import com.example.profiscooterex.ui.destinations.LoginScreenDestination
import com.example.profiscooterex.ui.destinations.ScooterSettingsScreenDestination
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.DarkColor
import com.example.profiscooterex.ui.theme.DarkColor2
import com.example.profiscooterex.ui.theme.DarkGradient
import com.example.profiscooterex.ui.theme.LightColor
import com.example.profiscooterex.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    logout: () -> Unit,
    goToLoginScreen: () -> Unit,
    goToScooterSettingsScreen: () -> Unit,
    userData: User?,
    tripData: List<TripDetails>,
    scooterData: Scooter?,
    removeTrip: (String) -> Unit
) {

    val spacing = MaterialTheme.spacing

    Column(modifier = Modifier.fillMaxSize().background(DarkGradient)) {
        Column(
            modifier =
                Modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .padding(spacing.medium)
                    .padding(top = spacing.medium),
        ) {
            Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                Text(
                    text = stringResource(id = R.string.welcome),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                    OutlinedButton(
                        onClick = {
                            logout()
                            goToLoginScreen()
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = DarkColor,
                                contentColor = Color.White
                            )
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = "logout")
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                Text(
                    text = userData!!.nick,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                    OutlinedButton(
                        onClick = { goToScooterSettingsScreen() },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = DarkColor,
                                contentColor = Color.White
                            )
                    ) {
                        Icon(Icons.Default.ElectricScooter, contentDescription = "logout")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp)) // Add spacing

        Row() { HistoryTrips(tripData) { tripDate: String -> removeTrip(tripDate) } }
    }
}

@Composable
fun HistoryTrips(getTripData: List<TripDetails>, removeTrip: (String) -> Unit) {
    Column {
        HorizontalDivider()
        Row(
            modifier =
                Modifier.fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .background(DarkColor2)
                    .padding(start = 16.dp, top = 5.dp, end = 16.dp, bottom = 5.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextTripTitle(text = "Distance", modifier = Modifier)
                TextTripUnit(text = "[Km]", modifier = Modifier)
            }
            VerticalDivider()
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextTripTitle(text = "Time", modifier = Modifier)
                TextTripUnit(text = "[min]", modifier = Modifier)
            }
            VerticalDivider()
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextTripTitle(text = "Speed", modifier = Modifier)
                TextTripUnit(text = "[Kmh]", modifier = Modifier)
            }
            VerticalDivider()
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextTripTitle(text = "Battery", modifier = Modifier)
                TextTripUnit(text = "[%]", modifier = Modifier)
            }
        }
        HorizontalDivider()
        if (getTripData.isEmpty()) {
            val rotationState by
                rememberInfiniteTransition(label = "")
                    .animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec =
                            infiniteRepeatable(animation = tween(1000, easing = LinearEasing)),
                        label = ""
                    )

            Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.profiscooter_logo),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp).rotate(rotationState).align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(modifier = Modifier.padding(bottom = 55.dp)) {
                items(items = getTripData) { trip ->
                    HistoryItem(trip) { tripDate -> removeTrip(tripDate) }
                }
            }
        }
    }
}

@Composable
private fun TextTrip(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodySmall,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun TextTripTitle(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyMedium,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun TextTripUnit(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodySmall,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

@Composable
fun HistoryItem(trip: TripDetails, removeTrip: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(10.dp, 10.dp, 0.dp, 0.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth().background(LightColor).padding(top = 5.dp, bottom = 5.dp)
        ) {
            Box(modifier = Modifier.weight(0.93f)) {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 5.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(start = 10.dp, bottom = 5.dp)) {
                        TextTrip(text = trip.tripName, modifier = Modifier.weight(1f))
                        TextTrip(text = trip.dateTime, modifier = Modifier.weight(1f))
                    }
                    Row(
                        modifier =
                            Modifier.fillMaxWidth()
                                .padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
                    ) {
                        TextTrip(text = trip.totalDistance, modifier = Modifier.weight(1f))
                        TextTrip(text = trip.distanceTime, modifier = Modifier.weight(1f))
                        TextTrip(text = trip.averageSpeed, modifier = Modifier.weight(1f))
                        TextTrip(text = trip.batteryDrain, modifier = Modifier.weight(1f))
                    }
                }
            }
            Box(
                modifier = Modifier.weight(0.07f).align(Alignment.CenterVertically).fillMaxHeight()
            ) {
                Icon(
                    modifier = Modifier.fillMaxHeight().clickable { removeTrip(trip.dateTime) },
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "logout",
                    tint = DarkColor2
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ContentNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    viewModel: AuthViewModel? = hiltViewModel(),
    dataViewModel: DataViewModel? = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    HomeScreen(
        logout = { viewModel?.logout() },
        goToLoginScreen = {
            navigator.popBackStack()
            navigator.navigate(LoginScreenDestination) {
                popUpTo(LoginScreenDestination.route) { inclusive = true }
            }
        },
        goToScooterSettingsScreen = {
            navigator.navigate(ScooterSettingsScreenDestination) {
                popUpTo(ScooterSettingsScreenDestination.route) { inclusive = false }
            }
        },
        userData = dataViewModel?.userDataState?.value,
        tripData = dataViewModel?.tripsDataState?.value ?: emptyList(),
        scooterData = dataViewModel?.scooterDataState?.value,
        removeTrip = { tripDate -> dataViewModel?.removeTripData(tripDate) }
    )
}

val sampleTrips =
    listOf(
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
val sampleUser = User(nick = "User", age = "26", email = "example@gmail.com")

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        Surface {
            HomeScreen(
                logout = {},
                goToLoginScreen = {},
                goToScooterSettingsScreen = {},
                userData = sampleUser,
                tripData = sampleTrips,
                scooterData = null,
                removeTrip = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryTripsPreview() {
    AppTheme { HistoryTrips(sampleTrips) {} }
}

@Preview(showBackground = true)
@Composable
fun HistoryItemPreview() {
    AppTheme { HistoryItem(sampleTrip) {} }
}
