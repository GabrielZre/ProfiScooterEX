package com.example.profiscooterex.ui.dashboard.components.dialogs

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.profiscooterex.ui.dashboard.DashboardViewModel
import com.example.profiscooterex.ui.dashboard.UiState
import com.example.profiscooterex.ui.dashboard.components.DashboardSpeedIndicator
import com.example.profiscooterex.ui.theme.AppTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveTripDialog(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    icon: ImageVector,
) {
    val viewModel: DashboardViewModel = hiltViewModel()
    var tripName by remember { mutableStateOf("") }

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
                    value = tripName,
                    onValueChange = { tripName = it },
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
                        Text(text = "Distance")
                        Text(
                            modifier = Modifier.padding(bottom = 5.dp),
                            text = "${"%.1f".format(viewModel.distanceTrip)}KM"
                        )
                    }
                    VerticalDivider()
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Time")
                        Text(
                            modifier = Modifier.padding(bottom = 5.dp),
                            text = viewModel.stopWatch.formattedTime
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
                        Text(
                            modifier = Modifier.padding(top = 5.dp),
                            text = "Average speed"
                        )
                        Text(
                            text = "${"%.1f".format(viewModel.averageSpeed)}Kmh"
                        )
                    }
                    VerticalDivider()
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 5.dp),
                            text = "Battery drain"
                        )
                        Text(
                            text = "${"%.1f".format(viewModel.calculateBatteryDrain())}%"
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
                    onDismissRequest()
                    viewModel.saveTrip(tripName)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TripDialog(openAlertDialog: MutableState<Boolean>) {
    when {
        openAlertDialog.value -> {
            SaveTripDialog(
                onDismissRequest = { openAlertDialog.value = false },
                dialogTitle = "Alert dialog example",
                icon = Icons.Default.Info,
            )
        }
    }
}