package com.night.ntcomposeui.modal

import androidx.compose.runtime.Composable

data class MyTabView(
    val viewTitle : String,
    val ContentView : @Composable () -> Unit
)
