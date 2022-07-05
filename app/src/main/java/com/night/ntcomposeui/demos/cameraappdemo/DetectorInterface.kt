package com.night.ntcomposeui.demos.cameraappdemo

import com.google.mlkit.vision.barcode.common.Barcode

interface DetectorInterface {
    fun onBarCode(barcode: Barcode)
}