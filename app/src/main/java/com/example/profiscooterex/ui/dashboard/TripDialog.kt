package com.example.profiscooterex.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.profiscooterex.data.userDB.TripDetails
import com.example.profiscooterex.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveTripDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    icon: ImageVector,
    tripName: String,
    totalDistance: String,
    averageSpeed: String,
    batteryDrain: String,
    distanceTime: String,
    dateTime: String
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Trip name") }
                )
                Spacer(
                    modifier = Modifier.height(20.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Battery")
                        Text(
                            text = "1"
                        )
                    }
                    VerticalDivider()
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Battery")
                        Text(
                            text = "1"
                        )
                    }
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Battery")
                        Text(
                            text = "1"
                        )
                    }
                    VerticalDivider()
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Battery")
                        Text(
                            text = "1"
                        )
                    }
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun TripDialog(openAlertDialog: MutableState<Boolean>, tripDetails: TripDetails, saveTrip: Unit) {
    when {
        openAlertDialog.value -> {
            SaveTripDialog(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                    saveTrip
                },
                dialogTitle = "Alert dialog example",
                icon = Icons.Default.Info,
                tripName = "",
                totalDistance = tripDetails.totalDistance,
                averageSpeed = tripDetails.averageSpeed,
                batteryDrain = tripDetails.batteryDrain,
                distanceTime = tripDetails.distanceTime,
                dateTime = tripDetails.dateTime
            )
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun SaveTripDialogPreview() {
    AppTheme {
        Surface {
            SaveTripDialog(
                onDismissRequest = {},
                onConfirmation = {},
                dialogTitle = "Dialog preview",
                icon = Icons.Default.Info,
                tripName = "Preview example name",
                totalDistance = "",
                averageSpeed = "",
                batteryDrain = "",
                distanceTime = "0:15:23",
                dateTime = "14.09.2023 15:33:12"
            )
        }
    }
}