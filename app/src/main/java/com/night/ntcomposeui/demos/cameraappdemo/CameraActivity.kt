package com.night.ntcomposeui.demos.cameraappdemo

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.google.mlkit.vision.barcode.common.Barcode
import com.night.ntcomposeui.NTCApplication
import com.night.ntcomposeui.config.IS_DARK_MODE
import com.night.ntcomposeui.ui.theme.NTComposeUITheme
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CameraActivity : ComponentActivity() {

    companion object {
        const val CAMERA_APP_MODE = "CAMERA_APP_MODE"
        const val BARCODE_DETECTOR = "BARCODE_DETECTOR"
        const val FACE_DETECTOR = "FACE_DETECTOR"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val darkMode = (application as NTCApplication).dataStore.data.map {
            it[booleanPreferencesKey(IS_DARK_MODE)]
                ?: (application.resources.configuration.uiMode == Configuration.UI_MODE_NIGHT_MASK)
        }
        val mode = intent.extras?.getString(CAMERA_APP_MODE)

        setContent {

            val isDarkMode = darkMode.collectAsState(initial = isSystemInDarkTheme())

            NTComposeUITheme(darkTheme = isDarkMode.value) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when (mode) {
                        BARCODE_DETECTOR -> BarcodeDetectorView()
                        FACE_DETECTOR -> {}
                        else -> BarcodeDetectorView()
                    }
                }
            }
        }

        registerPermission.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_MEDIA_LOCATION
            )
        )
    }

    //取得使用者權限
    private val registerPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.containsValue(false)) {
                Toast.makeText(this, "Permission died", Toast.LENGTH_SHORT).show()
                finish()
                return@registerForActivityResult
            }
        }

}

@Composable
fun BarcodeDetectorView() {

    var barcodeContentValue by remember {
        mutableStateOf("RESULT")
    }


    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (cameraRef,textRef) = createRefs()


        Box(
            modifier = Modifier
                .fillMaxHeight(0.87f)
                .constrainAs(cameraRef){
                    top.linkTo(this.parent.top)
                    start.linkTo(this.parent.start)
                    end.linkTo(this.parent.end)
                    bottom.linkTo(textRef.top)

                }
        ) {
            CameraPreview(onBarCodeDetect = {
//                Log.d("Barcode", it.displayValue ?: "TT")
                barcodeContentValue = it.displayValue ?: ""
            })
        }

        Box(
            modifier = Modifier.constrainAs(textRef){
//                top.linkTo(cameraRef.bottom)
                start.linkTo(this.parent.start)
                end.linkTo(this.parent.end)
                bottom.linkTo(this.parent.bottom)
            },
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = barcodeContentValue,
                textAlign = TextAlign.Center,
                fontSize = 26.sp
            )
        }





    }

}


@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onBarCodeDetect: (barcode: Barcode) -> Unit = { }
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            // CameraX Preview UseCase
            val previewUseCase = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val detectorInterface = object : DetectorInterface {
                override fun onBarCode(barcode: Barcode) {
                    onBarCodeDetect(barcode)
                }
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(context.executor, QrCodeAnalyzer(detectorInterface))
                }

            coroutineScope.launch {
                val cameraProvider = context.getCameraProvider()
                try {
                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, previewUseCase, imageAnalysis
                    )
                } catch (e: Exception) {
                    Log.e("CameraPreview", "Use case binding failed", e)
                }
            }
            previewView
        }
    )
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener({
            continuation.resume(future.get())
        }, executor)
    }
}

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)

