package com.example.profiscooterex.ui.scooter.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NetworkSnackBar(snackBarHostState: SnackbarHostState, text: String) {
    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
        ) {
            Snackbar(modifier = Modifier.wrapContentHeight().padding(0.dp, 0.dp, 0.dp, 80.dp)) {
                Text(text, color = Color.White)
            }
        }
    }
}
