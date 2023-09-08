package com.example.profiscooterex
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.profiscooterex.permissions.service.RequestServiceListener
import com.example.profiscooterex.permissions.service.RequestServicesListener
import com.example.profiscooterex.permissions.service.ServiceViewModel
//import com.example.profiscooterex.data.userDB.DataViewModel
//import com.example.profiscooterex.navigation.AppNavHost
import com.example.profiscooterex.ui.NavGraphs
import com.example.profiscooterex.ui.theme.AppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity(), RequestServicesListener {

    @Inject lateinit var bluetoothAdapter: BluetoothAdapter

    @Inject lateinit var requestServiceListener: RequestServiceListener
    //private val viewModel: PermissionsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
        requestServiceListener.addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        requestServiceListener.removeListener(this)
        Log.d("tag","onDESTROY")
    }
    private fun startBLE() {
        showBluetoothDialog()
        Log.d("tag","Called from MAIN RFB2")

    }

    private fun showBluetoothDialog() {
        if(!bluetoothAdapter.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startBluetoothIntentForResult.launch(enableBluetoothIntent)
        }
    }

    private val startBluetoothIntentForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            if(result.resultCode != Activity.RESULT_OK) {
                showBluetoothDialog()
            }
        }

    override fun requestBluetooth() {
        startBLE()
        Log.d("tag","Called from MAIN RFB1")
    }


}