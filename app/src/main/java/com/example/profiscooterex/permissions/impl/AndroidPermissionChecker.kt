package com.example.profiscooterex.permissions.impl

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.profiscooterex.permissions.PermissionChecker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidPermissionChecker @Inject constructor(@ApplicationContext val context: Context) :
    PermissionChecker {

    override fun hasPermission(permissionName: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permissionName) ==
            PackageManager.PERMISSION_GRANTED
    }
}
