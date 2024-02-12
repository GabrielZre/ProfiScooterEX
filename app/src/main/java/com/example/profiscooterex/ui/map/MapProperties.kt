package com.example.profiscooterex.ui.map

import com.utsman.osmandcompose.MapProperties
import com.utsman.osmandcompose.ZoomButtonVisibility

val mapProperties =
    MapProperties(
        mapOrientation = 0f,
        isMultiTouchControls = true,
        isAnimating = true,
        minZoomLevel = 6.0,
        maxZoomLevel = 21.0,
        isFlingEnable = true,
        isEnableRotationGesture = false,
        isUseDataConnection = true,
        isTilesScaledToDpi = false,
        tileSources = null,
        overlayManager = null,
        zoomButtonVisibility = ZoomButtonVisibility.ALWAYS
    )

val DefaultMapProperties = mapProperties
