package com.example.profiscooterex.ui.scooter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Battery0Bar
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.BatteryUnknown
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.profiscooterex.navigation.ContentNavGraph
import com.example.profiscooterex.ui.scooter.components.Picker
import com.example.profiscooterex.ui.scooter.components.rememberPickerState
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.DarkGradient
import com.example.profiscooterex.ui.theme.LightColor
import com.example.profiscooterex.ui.theme.LightColor2
import com.example.profiscooterex.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScooterSettingsScreen(
    batteryAh: String,
    batteryVoltage: String,
    bottomCutOff: String,
    motorWatt: String,
    upperCutOff: String
) {

    val spacing = MaterialTheme.spacing

    val batteryAhValues = remember { (5..50).map { it.toString() } }
    val batteryAhPickerState = rememberPickerState()

    val batteryVoltageValues = remember { (12..92).map { it.toString() } }
    val batteryVoltagePickerState = rememberPickerState()

    val motorWattValues = remember { (150..1500 step 50).map { it.toString() } }
    val motorWattPickerState = rememberPickerState()

    var bottomCutOff by remember { mutableStateOf("") }
    var upperCutOff by remember { mutableStateOf("") }

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
            Surface(
                color = Color.Transparent
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {

                    Row(){
                        OutlinedTextField(
                            value = bottomCutOff,
                            onValueChange = { bottomCutOff = it },
                            placeholder = { Text(text = "Max Voltage") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(0.5f),
                            shape = RoundedCornerShape( 20.dp, 0.dp, 0.dp, 20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
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
                            leadingIcon = {
                                Icon(Icons.Default.Battery0Bar, "Minimum Voltage")
                            },
                        )
                        OutlinedTextField(
                            value = upperCutOff,
                            onValueChange = { upperCutOff = it },
                            placeholder = { Text(text = "Max Voltage") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(0.5f),
                            shape = RoundedCornerShape( 0.dp, 20.dp, 20.dp, 0.dp),
                            colors = OutlinedTextFieldDefaults.colors(
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
                            leadingIcon = {
                                Icon(Icons.Default.BatteryFull, "Minimum Voltage")
                            },
                        )

                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Row() {
                        Icon(
                            modifier = Modifier
                                .weight(0.33f),
                            imageVector = Icons.Default.BatteryUnknown,
                            contentDescription = "Battery Ah",
                            tint = Color.LightGray
                        )
                        Icon(
                            modifier = Modifier
                                .weight(0.33f),
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Voltage",
                            tint = Color.LightGray
                        )
                        Icon(
                            modifier = Modifier
                                .weight(0.33f),
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
                        Row() {
                                Picker(
                                    state = batteryAhPickerState,
                                    items = batteryAhValues,
                                    visibleItemsCount = 3,
                                    modifier = Modifier.weight(0.3f),
                                    textModifier = Modifier.padding(8.dp),
                                    textStyle = TextStyle(
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    ),
                                )
                                Picker(
                                    state = batteryVoltagePickerState,
                                    items = batteryVoltageValues,
                                    visibleItemsCount = 3,
                                    modifier = Modifier.weight(0.3f),
                                    textModifier = Modifier.padding(8.dp),
                                    textStyle = TextStyle(
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    ),
                                )
                                Picker(
                                    state = motorWattPickerState,
                                    items = motorWattValues,
                                    visibleItemsCount = 3,
                                    modifier = Modifier.weight(0.3f),
                                    textModifier = Modifier.padding(8.dp),
                                    textStyle = TextStyle(
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    ),
                                )
                            }
                        }

                    Text(
                        text = "Values: ${batteryAhPickerState.selectedItem}, ${batteryVoltagePickerState.selectedItem}, ${motorWattPickerState.selectedItem}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )



                }
            }

        }

    }
}



@RequiresApi(Build.VERSION_CODES.O)
@ContentNavGraph()
@Destination
@Composable
fun ScooterSettingsScreen(scooterSettingsViewModel : ScooterSettingsViewModel = hiltViewModel(), navigator: DestinationsNavigator) {
    ScooterSettingsScreen(
        batteryAh = scooterSettingsViewModel.batteryAh,
        batteryVoltage = scooterSettingsViewModel.batteryVoltage,
        bottomCutOff = scooterSettingsViewModel.bottomCutOff,
        motorWatt = scooterSettingsViewModel.motorWatt,
        upperCutOff = scooterSettingsViewModel.upperCutOff
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
                upperCutOff = "54.6"
            )
        }
    }
}

