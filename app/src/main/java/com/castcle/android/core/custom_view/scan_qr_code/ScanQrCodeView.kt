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