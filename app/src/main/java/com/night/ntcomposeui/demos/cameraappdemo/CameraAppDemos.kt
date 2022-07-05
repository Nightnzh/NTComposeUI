package com.night.ntcomposeui.demos.cameraappdemo

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions


@Composable
fun CameraAppDemos() {

    val ctx = LocalContext.current
    var result by remember { mutableStateOf("") }

    val zxingScannerLauncher = rememberLauncherForActivityResult(contract = ScanContract(), onResult = {
        result = it.contents
    })


    Column {
        Text(text = result)
        Button(onClick = { zxingScannerLauncher.launch(ScanOptions()) }) {
            Text("SCAN(ZXING)")
        }
        Button(onClick = {
            ctx.startActivity(Intent(ctx,CameraActivity::class.java))
        }) {
            Text("SCAN(Google ML)")
        }
    }

}


