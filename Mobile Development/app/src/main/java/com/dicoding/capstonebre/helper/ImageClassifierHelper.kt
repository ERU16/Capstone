package com.dicoding.capstonebre.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.dicoding.capstonebre.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.lang.IllegalStateException

class ImageClassifierHelper(
    val threshold: Float = 0.7f,
    var maxResults: Int = 3,
    val modelName: String = "FinalModel_Metadata.tflite",
    val context: Context,
    val listener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    interface ClassifierListener {
        fun onError(message: String)
        fun onClassifications(
            results: List<Pair<String, Float>>?,
            inferenceDuration: Long
        )
    }

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)

        val baseOptions = BaseOptions.builder()
            .setNumThreads(4)
            .build()

        optionsBuilder.setBaseOptions(baseOptions)

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            listener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        val bitmap = loadBitmap(imageUri)
        val tensorImage = processImage(bitmap)
        val results = runInference(tensorImage)
        notifyResults(results)
    }

    private fun loadBitmap(imageUri: Uri): Bitmap {
        return if (isAndroidPOrLater()) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun isAndroidPOrLater(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    private fun processImage(bitmap: Bitmap): TensorImage {
        val processor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .add(CastOp(DataType.UINT8))
            .build()

        return processor.process(TensorImage.fromBitmap(bitmap))
    }

    private fun runInference(tensorImage: TensorImage): List<Pair<String, Float>>? {
        val startTime = SystemClock.uptimeMillis()
        val classifications = imageClassifier?.classify(tensorImage)
        val duration = SystemClock.uptimeMillis() - startTime

        val results = classifications?.firstOrNull()?.categories?.map { category ->
            Pair(category.label, category.score)
        }

        if (results.isNullOrEmpty()) {
            listener?.onError(context.getString(R.string.no_classification_results_found))
        } else {
            listener?.onClassifications(results, duration)
        }

        return results
    }

    private fun notifyResults(results: List<Pair<String, Float>>?) {
        if (results.isNullOrEmpty()) {
            listener?.onError(context.getString(R.string.no_classification_results_found))
        }
    }



    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}
