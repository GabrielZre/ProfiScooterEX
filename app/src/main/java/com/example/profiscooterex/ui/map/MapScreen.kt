package com.example.profiscooterex.ui.map

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.profiscooterex.R
import com.example.profiscooterex.data.DataViewModel
import com.example.profiscooterex.data.userDB.GpsData
import com.example.profiscooterex.navigation.ContentNavGraph
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.DarkGradient
import com.example.profiscooterex.ui.theme.LightColor2
import com.ramcosta.composedestinations.annotation.Destination
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import kotlinx.coroutines.delay
import org.osmdroid.util.GeoPoint

@SuppressLint("UseCompatLoadingForDrawables")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapScreen(gpsData: GpsData?) {
    val depot = GeoPoint(gpsData?.latitude ?: 0.0, gpsData?.longitude ?: 0.0)
    val date = remember { mutableStateOf(gpsData!!.date) }

    val shouldBeReady = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val cameraState = rememberCameraState {
        geoPoint = depot
        zoom = 16.0
    }
    val depotMarkerState = rememberMarkerState(geoPoint = depot, rotation = 90f)
    val depotIcon: Drawable? by remember {
        mutableStateOf(context.getDrawable(R.drawable.ic_electric_scooter))
    }

    LaunchedEffect(gpsData, shouldBeReady) {
        delay(250)
        shouldBeReady.value = true
    }

    if (shouldBeReady.value) {
        date.value = gpsData!!.date
        cameraState.geoPoint = GeoPoint(gpsData.latitude, gpsData.longitude)
        depotMarkerState.geoPoint = GeoPoint(gpsData.latitude, gpsData.longitude)
    }

    Column(modifier = Modifier.fillMaxSize().background(DarkGradient)) {
        Surface(color = Color.Transparent) {
            if (shouldBeReady.value) {
                OpenStreetMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraState = cameraState,
                    properties = DefaultMapProperties
                ) {
                    Marker(
                        state = depotMarkerState,
                        icon = depotIcon,
                        title = "Scooter",
                        snippet = date.value
                    ) {
                        Column(
                            modifier =
                                Modifier.size(70.dp)
                                    .background(
                                        color = LightColor2,
                                        shape = RoundedCornerShape(7.dp)
                                    ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = it.title, fontSize = 12.sp, color = Color.White)
                            Text(text = it.snippet, fontSize = 10.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@ContentNavGraph
@Destination
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapScreen(dataViewModel: DataViewModel? = hiltViewModel()) {
    MapScreen(gpsData = dataViewModel?.gpsDataState?.value)
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun MapScreenPreview() {
    AppTheme { Surface { MapScreen() } }
}
