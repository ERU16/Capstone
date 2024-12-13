package com.dicoding.capstonebre.ui.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstonebre.ui.home.HomeActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.capstonebre.api.ApiConfig
import com.dicoding.capstonebre.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dicoding.capstonebre.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val PICK_IMAGE_REQUEST = 1
    private val READ_EXTERNAL_STORAGE_PERMISSION = 100
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectProfileImage.setOnClickListener {
            if (checkPermission()) {
                openImagePicker()
            } else {
                requestPermission()
            }
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_EXTERNAL_STORAGE_PERMISSION
        )
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                Toast.makeText(
                    this,
                    "Permission is required to access the gallery",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let {
                Glide.with(this)
                    .load(it)
                    .into(binding.profileImageView)
            }
        }
    }

    private fun registerUser() {
        val username = binding.etUsername.text.toString().trim()
        val age = binding.etAge.text.toString().trim().toIntOrNull()

        if (username.isEmpty()) {
            binding.etUsername.error = getString(R.string.username_is_required)
            return
        }

        if (age == null) {
            binding.etAge.error = getString(R.string.valid_age_is_required)
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        val profilePicturePart = selectedImageUri?.let { uri ->
            val file = File(getRealPathFromURI(uri))
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profilePicture", file.name, requestFile)
        }

        sendRegisterRequest(username, age, profilePicturePart)
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val filePath = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return filePath ?: ""
    }

    private fun sendRegisterRequest(username: String, age: Int, profilePicturePart: MultipartBody.Part?) {
        val usernameRequestBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
        val ageRequestBody = age.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val apiService = ApiConfig.getApiService()

        lifecycleScope.launch {
            try {
                val response = apiService.registerUser(usernameRequestBody, ageRequestBody, profilePicturePart)
                Log.d("RegisterActivity", "Response: ${response.body()}")

                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body()?.status == 201) {
                    Toast.makeText(this@RegisterActivity,
                        getString(R.string.registration_successful), Toast.LENGTH_SHORT).show()
                    val userId = response.body()?.data?.id
                    val username = response.body()?.data?.username
                    if (userId != null && username != null) {
                        saveUserData(userId, username)
                        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("isRegistered", true)
                        editor.apply()

                        val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("RegisterActivity", "User ID or Username is null")
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, response.body()?.message ?: getString(R.string.registration_failed), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("RegisterActivity", "Error: ${e.message}", e)
                // Hide ProgressBar on error
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(userId: String, username: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_id", userId)
        editor.putString("username", username)
        editor.apply()
    }
}
