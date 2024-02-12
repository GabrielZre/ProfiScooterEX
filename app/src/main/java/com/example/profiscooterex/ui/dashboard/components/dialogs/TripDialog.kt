package com.example.profiscooterex.ui.dashboard.components.dialogs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DisabledByDefault
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.profiscooterex.permissions.network.ConnectivityObserver
import com.example.profiscooterex.ui.dashboard.DashboardViewModel
import com.example.profiscooterex.ui.scooter.components.NetworkSnackBar
import com.example.profiscooterex.ui.theme.DarkColor2
import com.example.profiscooterex.ui.theme.LightColor
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SaveTripDialog(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    networkStatus: ConnectivityObserver.Status
) {
    val viewModel: DashboardViewModel = hiltViewModel()

    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    var tripName by remember { mutableStateOf("") }
    var isErrorState by remember { mutableStateOf(false) }

    AlertDialog(
        containerColor = LightColor,
        iconContentColor = Color.White,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        icon = {},
        title = { Text(text = dialogTitle) },
        text = {
            Column(modifier = Modifier.height(IntrinsicSize.Min)) {
                OutlinedTextField(
                    value = tripName,
                    onValueChange = {
                        tripName = it
                        isErrorState = it.isEmpty()
                    },
                    label = { Text("Trip name", color = Color.Gray) },
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DarkColor2,
                            unfocusedBorderColor = if (isErrorState) Color.Red else DarkColor2,
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White
                        )
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Distance", fontWeight = FontWeight.Bold, color = LightGray)
                        Text(
                            modifier = Modifier.padding(bottom = 5.dp),
                            text = "${"%.1f".format(viewModel.distanceTrip)}KM"
                        )
                    }
                    VerticalDivider(color = DarkColor2, thickness = 2.dp)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Time", fontWeight = FontWeight.Bold, color = LightGray)
                        Text(
                            modifier = Modifier.padding(bottom = 5.dp),
                            text = viewModel.stopWatch.formattedTime
                        )
                    }
                }
                HorizontalDivider(thickness = 2.dp, color = DarkColor2)
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 5.dp),
                            text = "Average speed",
                            fontWeight = FontWeight.Bold,
                            color = LightGray
                        )
                        Text(text = "${"%.1f".format(viewModel.averageSpeed)}Kmh")
                    }
                    VerticalDivider(color = DarkColor2, thickness = 2.dp)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 5.dp),
                            text = "Battery drain",
                            fontWeight = FontWeight.Bold,
                            color = LightGray
                        )
                        Text(text = "${"%.1f".format(viewModel.calculateBatteryDrain())}%")
                    }
                }
            }
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            Button(
                onClick = {
                    if (tripName.isNotEmpty()) {
                        if (networkStatus == ConnectivityObserver.Status.Available) {
                            onDismissRequest()
                            viewModel.saveTrip(tripName)
                        } else {
                            coroutineScope.launch {
                                snackBarHostState.showSnackbar(
                                    message = "Network is not available.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    } else {
                        isErrorState = true
                    }
                },
                shape = RoundedCornerShape(5.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = DarkColor2,
                    ),
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Confirm",
                    tint = Color.White
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismissRequest() },
                shape = RoundedCornerShape(5.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = DarkColor2,
                    ),
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DisabledByDefault,
                    contentDescription = "Dismiss",
                    tint = Color.White
                )
            }
        }
    )
    NetworkSnackBar(snackBarHostState, "Network is not available")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TripDialog(openAlertDialog: MutableState<Boolean>, networkStatus: ConnectivityObserver.Status) {
    when {
        openAlertDialog.value -> {
            SaveTripDialog(
                onDismissRequest = { openAlertDialog.value = false },
                dialogTitle = "Save trip",
                networkStatus
            )
        }
    }
}
