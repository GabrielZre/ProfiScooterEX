package com.example.profiscooterex.ui.map

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import com.example.profiscooterex.ui.scooter.ScooterSettingsViewModel


import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.profiscooterex.R
import com.example.profiscooterex.navigation.ContentNavGraph
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.DarkGradient
import com.example.profiscooterex.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import com.utsman.osmandcompose.rememberOverlayManagerState
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.CopyrightOverlay


@SuppressLint("UseCompatLoadingForDrawables")
@ContentNavGraph()
@Destination
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapScreen(
) {
    val context = LocalContext.current

    val spacing = MaterialTheme.spacing

    val cameraState = rememberCameraState {
        geoPoint = Coordinates.depok
        zoom = 16.0
    }

    val depokMarkerState = rememberMarkerState(
        geoPoint = Coordinates.depok,
        rotation = 90f
    )

    val depokIcon: Drawable? by remember {
        mutableStateOf(context.getDrawable(R.drawable.ic_electric_scooter))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGradient)
    ) {

        Surface(
            color = Color.Transparent
        ) {


            OpenStreetMap(
                modifier = Modifier.fillMaxSize(),
                cameraState = cameraState
            ) {
                Marker(
                    state = depokMarkerState,
                    icon = depokIcon,
                    title = "Scooter",
                    snippet = "Last seen"
                ) {
                    Column(
                        modifier = Modifier
                            .size(70.dp)
                            .background(color = Color.LightGray, shape = RoundedCornerShape(7.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = it.title)
                        Text(text = it.snippet, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}


object Coordinates {

    private val depokJakartaRoutesPolyline = "BGvjumMyq93rGkCO8L4DwMjSwMjSkIrO4D7GoLoG0KkI4IkI0F0FkNwMwM4NkNsOgKgK8GwHsEsE4IkIgPsO8LsJwC8BsJ0F0F4DkN8G8LoG8QsJ0FkDgFwC4IsEsEwCgKgFoG4DwH4DoGkD0FwCwWoLsE8B4D8BwH4DwHsE0FkDsEwC4csO4SsJ0KsEgK4DgFwCoG4DgFwC0FwC8GkDgFwC4I4D0PkI0U8LoL0F0K8GgU4NsEkDwbkX0UsTwHwHkSsOsOoL8L0KwH8G0KkIwM4IwM8G0K0FgKsE8L0FgU8G0PoGsO0FouBwRgoBoQsO0FsiBkN0F8BsJ4D8gD8kB0P0F0FwC0K4D8LsEsnBgP4IkDkIkD4hB4N0UkIgKkDkN4DoLkDgKwCgZoGgKwCoG8B8G8BwWgFgK8B8GoBoLwCoGoBgFoBoV4DoL8BwHoBwHoBkwBsJsO4D0PsE0PgF0P0F4IwCwgBsJkI8BsJ8B8QkD0ZoG4NkD8L4DwlB4NsYwHkIkDge4I0Z8GsiBsJoLwC8GoB8GUsJAsOA0yBjD4wBvCssB3DwMnB4mB_EkXjDgKTgKnBwHTgFTwMnB8VvC0jBrEwlB3DkNnBoGT8GTkSvC4InB0KvCkI7BgF7BsTzFsJvCsOrE4N3DoG7BgKjDgUnGgPrEwR_EwRrE0UrE0FnB0FAgKT0KTkIUkI8BoGwCokB0UwM8G4DwC0FwCwH8BoGA0FT8G7B8GrEgFrE4D3DgFnG4DjIwC_JvC3SnB3IT_EnBjN7B3NnB3IvC3SvCnLvCvHjDnG3DzFrE_EnGzF_EjDjInBnGTjIoBzF8B3DwCjDwCzFoG7B4D7BsETsEA8G8BwH4DoG8GwHwHsEoLgFgKkDgjBoGoGoB46B0KgPwCsOwCs2B4I8L8BkIoBgKAg3BU0PoBsOwC0PkDkNkDsd4I0Z8GoL8BsOwCoLoBkXwCofwCsOUsOA4IA8GoBkI8B4IkDgKkDgP4DoL8BgKUw0BwH0esEoQkDgZwCgZwC4IU0KoBwRAoawCsdwC4ckDoBAoiCoL8sC0UgjB4I4mB0FgewH0yBwMokBsJwCUk9C8V8uBoL4NwCkcoG8Q8B8GAoGAoVoBoLT4N7BoBAgP7B8LvCwRrE0P_EoVvH8VrJ0PnGgjBrO41BzZ0tBnV8BTge3NoQ7G8GnBwHnBgFA8GAwRAkNA8pBnBkhBAwMoB4I8BgKoBsJ4D4IgFkIgFkN4I0FkD8pBkckS8L8GgF8QsJgFsJ0FsJwC0F4DgFsEsE0FsEwb4SkN4IkDwC0FwC0F8B4I8BgF8B4D8B8QgK0K8GsEoBsEAwMwHsO8GgyB8Q4DoB4NkD4IwC0Z0F8LoB4N8BwMoB0KoB4XsEo0DoVw_D4XsOkDgmCwMwM8B4hB0F4N8BsOwCsOwCoQUwHAkS7Bg3BrJ0P7B4NToLUkNoBoLwCoGwC4DoB0PkIwlBgUsd4NkXwHsYsE4hB8GsgCoL8pBoGgoBoG4X4DoGoB4DU0FoBwHoBsEUkI8BoGUgFoB8L8B0ZgFwMwCk1B8G8LoBgU8BkNoBk6B4DgPoBoGUouBkD4IUoL8Bof4D03BkIsdoGgPkDofwHoL4DwM4D8Q0F4_BgZwboL0U4Io9Gg1C0tBwR0jB4N4NgFwCTsEAwRgF8uBoLsJwCoQ4Dw0B8L8G8BsJwC4DUkSwC8V4Ds2B0KsOkDw0BgK0U4DkwBkIsJoB0mCkNgmC0PoVsEgFoBsd8GgFoBsJ8BgKwC4IwCgFoBkDUsTgFsEUsnB8G0Z0F0e8GgU4D8VsE8pBwHkI8BgP8BgKoB0UwCkDAsJA8fUwRUgFA4IUsOUsJAsJAsiBUkXT4NnB8arEwR3DoQrEkS7GoV3IgK_EgK_E8QrJ8L7GsJnG4SvMwWvRwb_TkI_EgKzFgFvCgKzFsY3NwRjIgKzF4D7BsEvC0KzFwH3DwMnG8arOwHvCkN3DwR7BgFTsJTgjB7BsOnBwRnBgPTgUnBkhBnBg6C4D4rB0F4SkDkIoBwM8BgFU8L8B8GoB8GoB8GAgKTgP7BsJnBkInBsT3D4NjDoBA4hB7GoGnB0KvC4I7B8LvC8GnB4I7BoQ3Doa3IsiB_JwMjD0KnB8QnBgFTkNA8LA8LU0Z8Bkc8B8LU0U8BkNoB8LU8QUsJUgPUwgB8B0KUwHUoaU8GAsOUoGU0UA8LAkDAkDA4IAsEAsOAwMA4IAoGA8LUoVUoQU4NUoL8B0FU4I8BkwBkIoGoBkNwCk1BkN8LoBkSoBoGT8GAoG8B8GkDwMwH8GsE0KkI4XsToQoL8pBkc0oBwW4NwHsTkI0KgFwHwCkNwC8GUkIAkI7B0F7BkI3DoL7GkI7G0KvHwRnLsEjD4IrEwMnGwRvH8QzF8GvCgPrE8GnBwHTgKnBoLTsOAgFA8LAwMAwHA0eAkNA0jBA0UUkDAsT4DsOkD8G8BgP0FgKgF8G4D4IsE0K8GkI4IsEsEkIoGkDwCgKkIgK0K4I0KkDkDgK8L0PsT8LwMsT0U4IsJsE4DoG4DoG4DkI4DwHkDkI8B8LoB4cgFoLwC4IUsJUoLoBsJoBkc4D8GUsEU0FUsEU8fUsOTgKU8QTkITgZnB4NA0U7BwHA0PTwgB8BkNoBoQkD8GwCwWoB0FAoGnBoG7BoGvC8G3D0FjD0evRsEvCsJ_E4I_EwqBzZ8VjNgFjDgKnG4I_E4I3DsJ3D0enLkIjDgUvH0K3D8LrE4DnBoGvCwHjDsE7B8GjDsOnGsE7BwCnB8VzKsJrEsJ_E8Q3I4IrEsY7L8GjDsOnGsiBvRwH_EwH_EoGrEsEvC8Q7GsJ3DsEnB0FTsJwCsEUgFAkI7B4DTsETgFUsEoBs1E8zB4rBkSgzDgtB8QoGkDoBkNvC0F7BgFjDgKjIwMriBkNrsBgtB_mEkSjwB8G7VgF_O8G_T0FrTwWr7B0FrOoG4DkD8BgFkD0FoBsEoBsEAkNwC0FAkDT4NjDsd3IgoBjNwRzFwHvC4I7B4c7Gk6BnLwvBnLoa7GwH7B8anGsd7GoLvC89BrJ8arE4uC3NwWjD0F_YwH7f4DzP8GrnBwHrnB0K36B4DvWoBzFwCAgFA0FU0jBAkNA8B3DwCvCsEvCgFTsEU4D8BkDkD0F8B8GoBwHoBoVAwvBAk_BUoQAonCUgyBUoLA4D7GwC_EniCzmC7BU7BTnBnBT7BU7BoBnB8BT8BU"

    val depok = GeoPoint(-6.3970066, 106.8224316)
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun MapScreenPreview() {
    AppTheme {
        Surface {
            MapScreen()
        }
    }
}

