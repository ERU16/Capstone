package com.dicoding.capstonebre.ui.edit

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.capstonebre.R
import com.dicoding.capstonebre.api.ApiConfig
import com.dicoding.capstonebre.api.ApiService
import com.dicoding.capstonebre.databinding.ActivityUpdateBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var apiService: ApiService
    private var profileImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.profileImage.setImageURI(it)
            profileImageUri = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiConfig.getApiService()

        loadUserData()

        binding.buttonSave.setOnClickListener {
            saveData()
        }

        binding.buttonPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun loadUserData() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        val age = sharedPreferences.getInt("age", -1)

        if (username != null) {
            binding.textViewOldUsername.text = "Old Username: $username"
        }
        if (age != -1) {
            binding.textViewOldAge.text = "Old Age: $age"
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val oldUsername = sharedPreferences.getString("username", null)
        val newUsername = binding.editTextName.text.toString().trim()
        val age = binding.editTextAge.text.toString().trim()

        if (oldUsername.isNullOrEmpty()) {
            Toast.makeText(this, "Old username not found in SharedPreferences", Toast.LENGTH_SHORT).show()
            return
        }

        if (newUsername.isEmpty()) {
            binding.editTextName.error = "New username is required"
            return
        }

        if (age.isEmpty() || age.toIntOrNull() == null || age.toInt() < 0) {
            binding.editTextAge.error = "Valid age is required"
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        val usernameUpdate = RequestBody.create("text/plain".toMediaTypeOrNull(), newUsername)
        val ageUpdate = RequestBody.create("text/plain".toMediaTypeOrNull(), age)


        val multipartBody: MultipartBody.Part? = if (profileImageUri != null) {
            val file = File(getRealPathFromURI(profileImageUri))
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profilePicture", file.name, requestBody)
        } else {
            null
        }

        updateUserProfile(oldUsername, usernameUpdate, ageUpdate, multipartBody)
    }

    private fun updateUserProfile(
        userId: String,
        usernameUpdate: RequestBody,
        ageUpdate: RequestBody,
        profilePicture: MultipartBody.Part? = null
    ) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)

        lifecycleScope.launch {
            try {
                val response = apiService.updateUserProfile(
                    username = userId,
                    usernameUpdate = usernameUpdate,
                    ageUpdate = ageUpdate,
                    profilePicture = profilePicture
                )
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body()?.status == 200) {
                    val updatedUser = response.body()?.data
                    if (updatedUser != null) {
                        val editor = sharedPreferences.edit()
                        editor.putString("username", updatedUser.username)
                        editor.putInt("age", updatedUser.age?.toInt() ?: -1)

                        updatedUser.profilePicture?.let {
                            editor.putString("profile_picture", it)
                        }
                        editor.apply()

                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                } else {
                    Toast.makeText(this@UpdateActivity, response.body()?.message ?: getString(R.string.update_failed), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("UpdateActivity", "Error: ${e.message}", e)
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@UpdateActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getRealPathFromURI(contentUri: Uri?): String? {
        var path: String? = null
        contentUri?.let {
            val cursor = contentResolver.query(it, null, null, null, null)
            cursor?.use { c ->
                if (c.moveToFirst()) {
                    val index = c.getColumnIndex(MediaStore.Images.Media.DATA)
                    path = c.getString(index)
                }
            }
        }
        return path
    }

}
