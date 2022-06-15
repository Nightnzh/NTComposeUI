package com.night.ntcomposeui.model

import androidx.compose.runtime.Composable

data class MyTabView(
    val viewTitle : String,
    val ContentView : @Composable () -> Unit
)
