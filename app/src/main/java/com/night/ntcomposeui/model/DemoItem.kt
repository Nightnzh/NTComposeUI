package com.night.ntcomposeui.model

import androidx.compose.runtime.Composable

data class DemoItem(
    val title : String,
    val routeName : String,
    val ComposeView : @Composable () -> Unit,
    val mdFileName : String? = null,
)
