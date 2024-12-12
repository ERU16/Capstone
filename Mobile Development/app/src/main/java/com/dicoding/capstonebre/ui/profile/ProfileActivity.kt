package com.dicoding.capstonebre.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstonebre.R
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.capstonebre.api.ApiConfig
import com.dicoding.capstonebre.databinding.ActivityProfileBinding
import com.dicoding.capstonebre.response.Data
import com.dicoding.capstonebre.ui.edit.UpdateActivity
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)

        if (username != null) {
            getUserProfile(username)
        } else {
            Toast.makeText(this, "Username tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

        binding.editProfileButton.setOnClickListener {
            val intent = Intent(this, UpdateActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_UPDATE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE && resultCode == Activity.RESULT_OK) {
            val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
            val username = sharedPreferences.getString("username", null)

            Log.d("ProfileActivity", "Reloading profile for username: $username")

            username?.let {
                getUserProfile(it)
            }
        }
    }

    private fun getUserProfile(username: String) {
        val apiService = ApiConfig.getApiService()

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = apiService.getUserProfile(username)
                Log.d("ProfileActivity", "Response: ${response.body()}")

                if (response.isSuccessful && response.body()?.status == 200) {
                    val userProfile = response.body()?.data
                    if (userProfile != null) {
                        Log.d("ProfileActivity", "Profile Picture URL from API: ${userProfile.profilePicture}")
                        displayUserProfile(userProfile)
                    } else {
                        Toast.makeText(
                            this@ProfileActivity,
                            "Data pengguna tidak tersedia",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        response.body()?.message ?: "Failed to fetch profile",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("ProfileActivity", "Error: ${e.message}", e)
                Toast.makeText(this@ProfileActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun displayUserProfile(userProfile: Data) {
        binding.nameTextView.text = userProfile.username ?: "Username tidak tersedia"
        binding.ageTextView.text = userProfile.age?.let { "Umur: $it" } ?: "Umur: Tidak tersedia"

        Log.d("ProfileActivity", "Response Profile Picture URL: ${userProfile.profilePicture}")

        val urlToLoad = userProfile.profilePicture

        Log.d("ProfileActivity", "Profile Picture URL to load: $urlToLoad")

        urlToLoad?.let { originalUrl ->
            Glide.with(this)
                .load(originalUrl.replace(" ", "%20"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.baseline_person_24)
                .into(binding.profileImage)
        } ?: run {

        }
    }

    companion object {
        private const val REQUEST_CODE_UPDATE = 1001
    }
}

