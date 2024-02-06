package com.example.profiscooterex.ui.home

import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.profiscooterex.R
import com.example.profiscooterex.data.DataViewModel
import com.example.profiscooterex.data.userDB.Scooter
import com.example.profiscooterex.data.userDB.TripDetails
import com.example.profiscooterex.data.userDB.User
import com.example.profiscooterex.navigation.BottomBar
import com.example.profiscooterex.navigation.BottomBarDestination
import com.example.profiscooterex.navigation.ContentNavGraph
import com.example.profiscooterex.navigation.checkForDestinations
import com.example.profiscooterex.ui.NavGraphs
import com.example.profiscooterex.ui.appCurrentDestinationAsState
import com.example.profiscooterex.ui.auth.AuthViewModel
import com.example.profiscooterex.ui.dashboard.components.dialogs.TripDialog
import com.example.profiscooterex.ui.destinations.DashboardTestScreenDestination
import com.example.profiscooterex.ui.destinations.LoginScreenDestination
import com.example.profiscooterex.ui.destinations.ScooterSettingsScreenDestination
import com.example.profiscooterex.ui.startAppDestination
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.DarkColor
import com.example.profiscooterex.ui.theme.DarkColor2
import com.example.profiscooterex.ui.theme.DarkGradient
import com.example.profiscooterex.ui.theme.LightColor
import com.example.profiscooterex.ui.theme.LightColor2
import com.example.profiscooterex.ui.theme.Spacing
import com.example.profiscooterex.ui.theme.md_theme_light_primary
import com.example.profiscooterex.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    logout: () -> Unit,
    goToLoginScreen: () -> Unit,
    goToScooterSettingsScreen: () -> Unit,
    userData: User?,
    tripData: List<TripDetails>,
    scooterData: Scooter?
) {

    val spacing = MaterialTheme.spacing
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGradient)
    ) {
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
                    text = stringResource(id = R.string.welcome),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    OutlinedButton(
                        onClick = {
                            logout()
                            goToLoginScreen()
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSecondaryContainer)
                    ) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = "logout"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = userData?.nick ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    OutlinedButton(
                        onClick = {
                            goToScooterSettingsScreen()
                            Log.d("tag", "scooterDataState: ${scooterData?.bottomCutOff}")
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSecondaryContainer)
                    ) {
                        Icon(
                            Icons.Default.ElectricScooter,
                            contentDescription = "logout"
                        )
                    }
                }

            }

        }

        /*Image(
            painter = painterResource(id = R.drawable.scooterruima),
            contentDescription = null,
            modifier = Modifier.height(306.dp)
        )*/

        Spacer(modifier = Modifier.height(20.dp)) // Add spacing

        Row()
        {
            HistoryTrips(tripData)
        }
    }
}

@Composable
fun HistoryTrips(getTripData : List<TripDetails>) {
    Column {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(DarkColor2)
                .padding(16.dp)
        ) {
            TextTripTitle(text = "Distance", modifier = Modifier.weight(1f) )
            VerticalDivider()
            TextTripTitle(text = "Time", modifier = Modifier.weight(1f) )
            VerticalDivider()
            TextTripTitle(text = "Speed", modifier = Modifier.weight(1f) )
            VerticalDivider()
            TextTripTitle(text = "Battery", modifier = Modifier.weight(1f) )
        }
        HorizontalDivider()
        LazyColumn() {
            items(items = getTripData) { trip ->
                HistoryItem(trip)
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
fun Modifier.borderTopAndBottom(size: Dp, color: Color): Modifier {
    return this.then(
        Modifier.drawWithContent {
            drawContent()
            drawRect(
                color = color,
                topLeft = Offset(0f, 0f),
                size = Size(size.toPx(), size.toPx())
            )
            drawRect(
                color = color,
                topLeft = Offset(0f, size.toPx() + 2f), // +2f to uwzględnia odstęp między górnym a dolnym obramowaniem
                size = Size(size.toPx(), size.toPx())
            )
        }
    )
}

@Composable
fun HistoryItem(trip : TripDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 10.dp, 10.dp, 0.dp),
        shape = RoundedCornerShape(20.dp),
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightColor)
                .padding(top = 5.dp)
        ) {
            TextTrip(text = trip.tripName, modifier = Modifier.weight(1f))
            TextTrip(text = trip.dateTime, modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightColor)
                .padding(top = 5.dp, bottom = 5.dp)
        ) {
            TextTrip(text = trip.totalDistance, modifier = Modifier.weight(1f))
            TextTrip(text = trip.distanceTime, modifier = Modifier.weight(1f))
            TextTrip(text = trip.averageSpeed, modifier = Modifier.weight(1f))
            TextTrip(text = trip.batteryDrain, modifier = Modifier.weight(1f))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ContentNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(viewModel: AuthViewModel? = hiltViewModel(), dataViewModel: DataViewModel? = hiltViewModel(), navigator: DestinationsNavigator) {
    HomeScreen(
        logout = { viewModel?.logout() },
        goToLoginScreen = {
            navigator.popBackStack()
            navigator.navigate(LoginScreenDestination) {
            popUpTo(LoginScreenDestination.route) {inclusive = true}
        } },
        goToScooterSettingsScreen = { navigator.navigate(ScooterSettingsScreenDestination) {
            popUpTo(ScooterSettingsScreenDestination.route) {inclusive = false}
        } },
        userData = dataViewModel?.userDataState?.value,
        tripData = dataViewModel?.tripsDataState?.value ?: emptyList(),
        scooterData = dataViewModel?.scooterDataState?.value
    )
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
val sampleUser =
    User(
        nick = "Doublementi",
        age = "26",
        email = "example@gmail.com"
    )


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
                scooterData = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryTripsPreview() {
    AppTheme {
        HistoryTrips(sampleTrips)
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryItemPreview() {
    AppTheme {
        HistoryItem(sampleTrip)
    }
}
