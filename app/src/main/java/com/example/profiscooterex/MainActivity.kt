package com.example.profiscooterex
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.RawContacts.Data
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph
import com.example.profiscooterex.permissions.PermissionsViewModel
//import com.example.profiscooterex.data.userDB.DataViewModel
//import com.example.profiscooterex.navigation.AppNavHost
import com.example.profiscooterex.ui.auth.AuthViewModel
import com.example.profiscooterex.ui.NavGraphs
import com.example.profiscooterex.ui.dashboard.DashboardViewModel
import com.example.profiscooterex.ui.theme.AppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @Inject lateinit var bluetoothAdapter: BluetoothAdapter

    private val viewModel: PermissionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }

    }
    fun startBLE() {
        showBluetoothDialog()
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


}