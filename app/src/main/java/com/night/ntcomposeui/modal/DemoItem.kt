package com.night.ntcomposeui.modal

import androidx.compose.runtime.Composable

data class DemoItem(
    val title : String,
    val routeName : String,
    val DemoView : @Composable () -> Unit,
    val CodeView : @Composable () -> Unit,
    val mdFileName : String?,
)
