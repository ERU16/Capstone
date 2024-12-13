package com.dicoding.capstonebre.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.capstonebre.R
import com.dicoding.capstonebre.database.ClassificationResult
import com.dicoding.capstonebre.databinding.ActivityCameraBinding
import com.dicoding.capstonebre.getImageUri
import com.dicoding.capstonebre.helper.ImageClassifierHelper
import com.dicoding.capstonebre.history.ClassificationResultViewModel
import com.dicoding.capstonebre.ui.history.HistoryActivity
import com.dicoding.capstonebre.ui.home.HomeActivity
import com.dicoding.capstonebre.ui.list.AnimalListActivity
import com.dicoding.capstonebre.ui.result.ResultActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var currentImageUri: Uri? = null
    private lateinit var bottomNavigationView: BottomNavigationView
    private val viewModel: ClassificationResultViewModel by viewModels()

    companion object {
        private const val TAG = "CameraActivity"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showToast(getString(R.string.permission_request_granted))
            } else {
                showToast(getString(R.string.permission_request_denied))
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        bottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigationView.selectedItemId = R.id.camera

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.daftar -> {
                    val intent = Intent(this, AnimalListActivity::class.java)
                    startActivity(intent)
                    finish()
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.camera -> true
                else -> false
            }
        }
    }

    private fun setupButtons() {
        binding.apply {
            cameraButton.setOnClickListener { startCamera() }
            historyImageView.setOnClickListener {
                navigateToHistoryActivity()
            }
        }
    }

    private fun startCamera() {
        try {
            val imageUri = getImageUri(this)
            currentImageUri = imageUri
            launcherIntentCamera.launch(imageUri)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting camera: ${e.message}")
            showToast(getString(R.string.failed_to_start_camera))
        }
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            currentImageUri?.let { uri ->
                showImage()
                startCrop(uri)
            }
        } else {
            showToast(getString(R.string.failed_to_capture_image))
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            try {
                Log.d(TAG, "Displaying image URI: $it")
                binding.previewImageView.setImageURI(it)
            } catch (e: Exception) {
                Log.e(TAG, "Error displaying image: ${e.message}")
                showToast("Failed to display image")
            }
        } ?: Log.w(TAG, "No image URI to display")
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))
        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(800, 800)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            if (resultUri != null) {
                currentImageUri = resultUri
                binding.previewImageView.setImageURI(resultUri)
                showToast(getString(R.string.image_cropped_successfully))
                analyzeImage(resultUri)
            } else {
                showToast(getString(R.string.failed_to_crop_image))
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.let {
                Log.e(TAG, "UCrop Error: ${it.message}")
                showToast("Crop failed: ${it.message}")
            }
        }
    }

    private fun analyzeImage(uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

            val savedImageUri = saveImageToInternalStorage(bitmap)

            val imageClassifierHelper = ImageClassifierHelper(
                context = this,
                listener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(message: String) {
                        showErrorDialog(message)
                    }

                    override fun onClassifications(
                        results: List<Pair<String, Float>>?,
                        inferenceDuration: Long
                    ) {
                        if (results.isNullOrEmpty()) {
                            return
                        }

                        val classificationLabel = results.firstOrNull()?.first ?: "Unknown"
                        val classificationScore = results.firstOrNull()?.second ?: 0f

                        val classificationResult = ClassificationResult(
                            labels = classificationLabel,
                            score = classificationScore,
                            imageUri = savedImageUri.toString()
                        )

                        lifecycleScope.launch {
                            viewModel.addNewResult(classificationResult)
                            showToast(getString(R.string.classification_saved_to_database))
                        }

                        val intent = Intent(this@CameraActivity, ResultActivity::class.java).apply {
                            putExtra(ResultActivity.IMAGE_URI, savedImageUri.toString())
                        }
                        startActivity(intent)
                    }
                })

            imageClassifierHelper.classifyStaticImage(uri)

        } catch (e: Exception) {
            Log.e(TAG, "Error starting image analysis: ${e.message}")
            showErrorDialog(getString(R.string.error_starting_image_analysis))
        }

    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.error))
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }


    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val filename = "image_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, filename)
        try {
            FileOutputStream(file).use { outStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                outStream.flush()
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error saving image to internal storage: ${e.message}")
        }
        return Uri.fromFile(file)
    }

    private fun navigateToHistoryActivity() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
