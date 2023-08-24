package com.example.profiscooterex
import android.os.Bundle
import android.provider.ContactsContract.RawContacts.Data
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavGraph
//import com.example.profiscooterex.data.userDB.DataViewModel
//import com.example.profiscooterex.navigation.AppNavHost
import com.example.profiscooterex.ui.auth.AuthViewModel
import com.example.profiscooterex.ui.NavGraphs
import com.example.profiscooterex.ui.theme.AppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthViewModel>()
    //private val dataViewModel by viewModels<DataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                //AppNavHost(viewModel)
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}