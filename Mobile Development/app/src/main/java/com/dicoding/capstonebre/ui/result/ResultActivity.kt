package com.dicoding.capstonebre.ui.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstonebre.database.AppDatabase
import com.dicoding.capstonebre.databinding.ActivityResultBinding
import com.dicoding.capstonebre.helper.ImageClassifierHelper
import com.dicoding.capstonebre.ui.detail.AnimalDetailActivity

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var database: AppDatabase

    companion object {
        const val IMAGE_URI = "image_uri"
        const val TAG = "ResultActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(applicationContext)
        val imageUriString = intent.getStringExtra(IMAGE_URI)
        if (imageUriString != null) {
            processImage(Uri.parse(imageUriString))
        } else {
            Log.e(TAG, "No image URI provided")
            finish()
        }
    }

    private fun processImage(imageUri: Uri) {
        showImage(imageUri)

        val classifierHelper = ImageClassifierHelper(
            context = this,
            listener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(message: String) {
                    Log.e(TAG, "Classification error: $message")
                }

                override fun onClassifications(results: List<Pair<String, Float>>?, inferenceDuration: Long) {
                    results?.let { navigateToAnimalDetail(it[0].first.replace("[#*]".toRegex(), "")) }
                }
            }
        )

        classifierHelper.classifyStaticImage(imageUri)
    }

    private fun showImage(uri: Uri) {
        try {
            binding.resultImage.setImageURI(uri)
        } catch (e: Exception) {
            Log.e(TAG, "Error showing image: ${e.message}")
        }
    }

    private fun navigateToAnimalDetail(animalName: String) {
        val intent = Intent(this, AnimalDetailActivity::class.java)
        intent.putExtra("ANIMAL_NAME", animalName)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
