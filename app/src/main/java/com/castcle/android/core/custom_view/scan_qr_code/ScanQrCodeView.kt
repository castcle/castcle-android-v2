/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.core.custom_view.scan_qr_code

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Size
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.castcle.android.databinding.CustomViewScanQrCodeBinding
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber
import java.util.concurrent.*

class ScanQrCodeView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    @ExperimentalGetImage
    private val analyzer = object : ImageAnalysis.Analyzer {

        private val barcodeScanner = BarcodeScanning.getClient(
            BarcodeScannerOptions
                .Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
        )

        override fun analyze(imageProxy: ImageProxy) {
            val image = imageProxy.image ?: return
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            process(inputImage, imageProxy)
        }

        fun analyze(context: Context, uri: Uri) {
            process(InputImage.fromFilePath(context, uri))
        }

        private fun process(image: InputImage, imageProxy: ImageProxy? = null) {
            barcodeScanner.process(image).addOnCompleteListener {
                imageProxy?.close()
            }.addOnSuccessListener { result ->
                result.filterNotNull().firstOrNull()
                    ?.rawValue
                    ?.ifBlank { null }
                    ?.let { updateResult(it) }
            }
        }

        private fun updateResult(text: String) {
            CoroutineScope(Dispatchers.IO).launch {
                result.emit(text)
            }
        }

    }

    private val binding by lazy {
        CustomViewScanQrCodeBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    private val cameraProvider = cameraProviderFuture.get()

    private val executor = Executors.newSingleThreadExecutor()

    private val result = MutableSharedFlow<String>()

    fun getResultFromUri(uri: Uri) {
        try {
            analyzer.analyze(context, uri)
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    fun init(lifecycleOwner: LifecycleOwner) {
        try {
            cameraProviderFuture.addListener({
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    Preview.Builder()
                        .build()
                        .apply { setSurfaceProvider(binding.previewView.surfaceProvider) },
                    ImageAnalysis.Builder()
                        .setTargetResolution(Size(1280, 720))
                        .build()
                        .apply { setAnalyzer(executor, analyzer) },
                )
            }, ContextCompat.getMainExecutor(context))
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    fun onGetResult() = result.distinctUntilChanged()

}