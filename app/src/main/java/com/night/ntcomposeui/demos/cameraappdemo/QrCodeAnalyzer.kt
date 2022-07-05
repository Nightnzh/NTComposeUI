package com.night.ntcomposeui.demos.cameraappdemo

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class QrCodeAnalyzer(
    private val detectorInterface: DetectorInterface
) : ImageAnalysis.Analyzer {

    val TAG = QrCodeAnalyzer::class.java.simpleName

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val img = image.image
        if (img != null) {
            val inputImage = InputImage.fromMediaImage(img, image.imageInfo.rotationDegrees)

            // Process image searching for barcodes
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            val scanner = BarcodeScanning.getClient(options)

            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
//                    for (barcode in barcodes) {
//                        // Handle received barcodes...
//                    }
                    if(barcodes.size > 0){
                        Log.d(TAG, barcodes.size.toString())
                        detectorInterface.onBarCode(barcodes[0])
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "ERR: ${it.message}")
                }
                .addOnCompleteListener {
                    image.close()
                }
        }

    }

}