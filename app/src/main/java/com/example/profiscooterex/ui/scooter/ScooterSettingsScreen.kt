package com.example.profiscooterex.ui.scooter

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Battery0Bar
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.BatteryUnknown
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.profiscooterex.data.userDB.Scooter
import com.example.profiscooterex.navigation.ContentNavGraph
import com.example.profiscooterex.permissions.network.ConnectivityObserver
import com.example.profiscooterex.ui.scooter.components.NetworkSnackBar
import com.example.profiscooterex.ui.scooter.components.Picker
import com.example.profiscooterex.ui.scooter.components.rememberPickerState
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.DarkColor
import com.example.profiscooterex.ui.theme.DarkGradient
import com.example.profiscooterex.ui.theme.LightColor
import com.example.profiscooterex.ui.theme.LightColor2
import com.example.profiscooterex.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScooterSettingsScreen(
    batteryAh: String,
    batteryVoltage: String,
    bottomCutOff: String,
    motorWatt: String,
    upperCutOff: String,
    calculateStartIndex: (String, String, String, Int) -> Int,
    saveScooterSettings: (Scooter) -> Unit,
    backToHomeScreen: () -> Unit,
    networkStatus: ConnectivityObserver.Status
) {
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val spacing = MaterialTheme.spacing

    var isPopupVisible by remember { mutableStateOf(false) }

    val batteryAhValues = remember { (5..50).map { it.toString() } }
    val batteryAhPickerState = rememberPickerState()
    batteryAhPickerState.selectedItem = batteryAh

    val batteryVoltageValues = remember { (12..92).map { it.toString() } }
    val batteryVoltagePickerState = rememberPickerState()
    batteryVoltagePickerState.selectedItem = batteryVoltage

    val motorWattValues = remember { (150..1500 step 50).map { it.toString() } }
    val motorWattPickerState = rememberPickerState()
    motorWattPickerState.selectedItem = motorWatt

    var bottomCutOffValue by remember { mutableStateOf("") }
    bottomCutOffValue = bottomCutOff
    var upperCutOffValue by remember { mutableStateOf(upperCutOff) }
    upperCutOffValue = upperCutOff

    Column(modifier = Modifier.fillMaxSize().background(DarkGradient)) {
        Column(
            modifier =
                Modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .padding(spacing.medium)
                    .padding(top = spacing.extraLarge),
        ) {
            Surface(color = Color.Transparent) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Row {
                        OutlinedTextField(
                            value = bottomCutOffValue,
                            onValueChange = { bottomCutOffValue = it },
                            placeholder = {
                                Text(
                                    text = "Min Voltage",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(0.5f),
                            shape = RoundedCornerShape(20.dp, 0.dp, 0.dp, 20.dp),
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = LightColor,
                                    focusedContainerColor = LightColor2,
                                    focusedPlaceholderColor = LightColor,
                                    unfocusedPlaceholderColor = Color.LightGray,
                                    focusedLeadingIconColor = LightColor,
                                    unfocusedLeadingIconColor = Color.LightGray,
                                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                    cursorColor = MaterialTheme.colorScheme.onPrimary
                                ),
                            textStyle = MaterialTheme.typography.bodyLarge,
                            leadingIcon = { Icon(Icons.Default.Battery0Bar, "Minimum Voltage") },
                        )
                        OutlinedTextField(
                            value = upperCutOffValue,
                            onValueChange = { upperCutOffValue = it },
                            placeholder = {
                                Text(
                                    text = "Max Voltage",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(0.5f),
                            shape = RoundedCornerShape(0.dp, 20.dp, 20.dp, 0.dp),
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = LightColor,
                                    focusedContainerColor = LightColor2,
                                    focusedPlaceholderColor = LightColor,
                                    unfocusedPlaceholderColor = Color.LightGray,
                                    focusedLeadingIconColor = LightColor,
                                    unfocusedLeadingIconColor = Color.LightGray,
                                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                    cursorColor = MaterialTheme.colorScheme.onPrimary
                                ),
                            textStyle = MaterialTheme.typography.bodyLarge,
                            leadingIcon = { Icon(Icons.Default.BatteryFull, "Minimum Voltage") },
                        )
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Row {
                        Icon(
                            modifier = Modifier.weight(0.33f).clickable { isPopupVisible = true },
                            imageVector = Icons.Default.BatteryUnknown,
                            contentDescription = "Battery Ah",
                            tint = Color.LightGray
                        )

                        Icon(
                            modifier = Modifier.weight(0.33f),
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Voltage",
                            tint = Color.LightGray
                        )
                        Icon(
                            modifier = Modifier.weight(0.33f),
                            imageVector = Icons.Default.ElectricScooter,
                            contentDescription = "Watt Power",
                            tint = Color.LightGray
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = LightColor,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    ) {
                        Row {
                            Picker(
                                state = batteryAhPickerState,
                                items = batteryAhValues,
                                visibleItemsCount = 3,
                                modifier = Modifier.weight(0.3f),
                                textModifier = Modifier.padding(8.dp),
                                textStyle =
                                    TextStyle(
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    ),
                                textMetric = "Ah",
                                startIndex =
                                    calculateStartIndex(
                                        batteryAh,
                                        batteryAhValues.first(),
                                        batteryAhValues.last(),
                                        1
                                    )
                            )

                            Picker(
                                state = batteryVoltagePickerState,
                                items = batteryVoltageValues,
                                visibleItemsCount = 3,
                                modifier = Modifier.weight(0.3f),
                                textModifier = Modifier.padding(8.dp),
                                textStyle =
                                    TextStyle(
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    ),
                                textMetric = "V",
                                startIndex =
                                    calculateStartIndex(
                                        batteryVoltage,
                                        batteryVoltageValues.first(),
                                        batteryVoltageValues.last(),
                                        1
                                    )
                            )
                            Picker(
                                state = motorWattPickerState,
                                items = motorWattValues,
                                visibleItemsCount = 3,
                                modifier = Modifier.weight(0.3f),
                                textModifier = Modifier.padding(8.dp),
                                textStyle =
                                    TextStyle(
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    ),
                                textMetric = "W",
                                startIndex =
                                    calculateStartIndex(
                                        motorWatt,
                                        motorWattValues.first(),
                                        motorWattValues.last(),
                                        50
                                    )
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    if (networkStatus == ConnectivityObserver.Status.Available) {
                        saveScooterSettings(
                            Scooter(
                                batteryAh = batteryAhPickerState.selectedItem,
                                batteryVoltage = batteryVoltagePickerState.selectedItem,
                                bottomCutOff = bottomCutOffValue,
                                motorWatt = motorWattPickerState.selectedItem,
                                upperCutOff = upperCutOffValue
                            )
                        )
                        backToHomeScreen()
                    } else {
                        coroutineScope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Network is not available.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = DarkColor,
                        contentColor = Color.White
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save",
                    tint = Color.LightGray
                )
            }
        }
        NetworkSnackBar(snackBarHostState, "Network is not available")
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@ContentNavGraph
@Destination
@Composable
fun ScooterSettingsScreen(
    scooterSettingsViewModel: ScooterSettingsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    ScooterSettingsScreen(
        batteryAh = scooterSettingsViewModel.scooterData.batteryAh,
        batteryVoltage = scooterSettingsViewModel.scooterData.batteryVoltage,
        bottomCutOff = scooterSettingsViewModel.scooterData.bottomCutOff,
        motorWatt = scooterSettingsViewModel.scooterData.motorWatt,
        upperCutOff = scooterSettingsViewModel.scooterData.upperCutOff,
        calculateStartIndex = scooterSettingsViewModel::calculateStartIndex,
        saveScooterSettings = scooterSettingsViewModel::saveScooterSettings,
        backToHomeScreen = navigator::popBackStack,
        networkStatus =
            scooterSettingsViewModel.connectivityObserver
                .observe()
                .collectAsState(initial = ConnectivityObserver.Status.Unavailable)
                .value
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun ScooterSettingsScreenPreview() {
    AppTheme {
        Surface {
            ScooterSettingsScreen(
                batteryAh = "16",
                batteryVoltage = "48",
                bottomCutOff = "41.5",
                motorWatt = "1000",
                upperCutOff = "54.6",
                calculateStartIndex = { _, _, _, _ -> 1 },
                saveScooterSettings = {},
                backToHomeScreen = {},
                networkStatus = ConnectivityObserver.Status.Available
            )
        }
    }
}
