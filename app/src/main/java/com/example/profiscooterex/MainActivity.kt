package com.example.profiscooterex
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.profiscooterex.navigation.Parent
import com.example.profiscooterex.permissions.service.RequestServiceListener
import com.example.profiscooterex.permissions.service.RequestServicesListener
import com.example.profiscooterex.permissions.service.ServiceViewModel
//import com.example.profiscooterex.data.userDB.DataViewModel
//import com.example.profiscooterex.navigation.AppNavHost
import com.example.profiscooterex.ui.theme.AppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity(), RequestServicesListener {

    @Inject lateinit var bluetoothAdapter: BluetoothAdapter
    @Inject lateinit var locationManager: LocationManager

    @Inject lateinit var requestServiceListener: RequestServiceListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContent {
            AppTheme {
                Parent()
            }
        }
        requestServiceListener.addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        requestServiceListener.removeListener(this)
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun startLocation() {
        showLocationDialog()
    }
    private fun startBluetooth() {
        showBluetoothDialog()
    }

    private fun showBluetoothDialog() {
        if(!bluetoothAdapter.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startBluetoothIntentForResult.launch(enableBluetoothIntent)
        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun showLocationDialog() {
        if(!locationManager.isLocationEnabled) {
            val enableLocationIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startLocationIntentForResult.launch(enableLocationIntent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val startLocationIntentForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            if(result.resultCode != Activity.RESULT_OK) {
                showLocationDialog()
            }
        }

    private val startBluetoothIntentForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            if(result.resultCode != Activity.RESULT_OK) {
                showBluetoothDialog()
            }
        }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun requestLocation() {
        startLocation()
    }
    override fun requestBluetooth() {
        startBluetooth()
    }

}