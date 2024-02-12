package com.example.profiscooterex.permissions

interface PermissionChecker {
    fun hasPermission(permissionName: String): Boolean
}
