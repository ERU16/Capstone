package com.dicoding.capstonebre.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.capstonebre.R
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.dicoding.capstonebre.api.ApiConfig
import com.dicoding.capstonebre.database.AppDatabase
import com.dicoding.capstonebre.databinding.ActivityHomeBinding
import com.dicoding.capstonebre.helper.ImageClassifierHelper
import com.dicoding.capstonebre.ui.list.AnimalListActivity
import com.dicoding.capstonebre.ui.camera.CameraActivity
import com.dicoding.capstonebre.ui.profile.ProfileActivity
import com.dicoding.capstonebre.ui.detail.AnimalDetailActivity
import com.dicoding.capstonebre.ui.result.ResultActivity
import com.dicoding.capstonebre.ui.welcome.WelcomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var lottieView: LottieAnimationView
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var isNavigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lottieView = binding.lottieView
        lottieView.setAnimation(R.raw.panda)
        lottieView.repeatCount = LottieDrawable.INFINITE
        lottieView.playAnimation()

        // Initialize the ImageClassifierHelper
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            listener = this
        )

        val username = getUsername()
        if (username != null) {
            fetchUsernameFromAPI(username)
        } else {
            binding.usernameTextView.text = getString(R.string.helloUser)
            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show()
        }

        setupAction()

        val bottomNavigationView = binding.bottomNav
        bottomNavigationView.selectedItemId = R.id.home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> true
                R.id.camera -> {
                    startActivity(Intent(this, CameraActivity::class.java))
                    finish()
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.daftar -> {
                    startActivity(Intent(this, AnimalListActivity::class.java))
                    finish()
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                else -> false
            }
        }
    }

    companion object {
        private const val REQUEST_CAMERA = 1
        private const val REQUEST_ANIMAL_DETAIL = 2
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CAMERA -> binding.bottomNav.selectedItemId = R.id.home
            REQUEST_ANIMAL_DETAIL -> isNavigated = false // Reset navigation flag
        }
    }

    override fun onResume() {
        super.onResume()
        updateUsername()
    }

    private fun updateUsername() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "User")
        binding.usernameTextView.text = "Hello, $username"
    }

    private fun setupAction() {
        binding.settingImageView.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.pengaturan))

            val options = arrayOf(getString(R.string.bahasa), getString(R.string.profil))
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                        startActivity(intent)
                    }
                    1 -> {
                        startActivity(Intent(this, ProfileActivity::class.java))
                    }
                }
            }
            builder.create().show()
        }

        binding.actionLogout.setOnClickListener {
            lifecycleScope.launch {
                clearUserData()

                val intent = Intent(this@HomeActivity, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        binding.etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text.toString().trim()
                if (query.isNotEmpty()) {
                    navigateToAnimalDetail(query)
                } else {
                    Toast.makeText(this, getString(R.string.masukkan_nama), Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }
    }

    private fun getUsername(): String? {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }

    private fun fetchUsernameFromAPI(username: String) {
        val apiService = ApiConfig.getApiService()

        lifecycleScope.launch {
            try {
                val response = apiService.getUserProfile(username)
                if (response.isSuccessful && response.body()?.status == 200) {
                    val userData = response.body()?.data
                    if (userData != null) {
                        binding.usernameTextView.text = "Hello, ${userData.username}"
                    } else {
                        binding.usernameTextView.text = "Hello, User"
                        Toast.makeText(this@HomeActivity, "Data pengguna tidak tersedia", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    binding.usernameTextView.text = "Hello, User"
                    Toast.makeText(this@HomeActivity, response.body()?.message ?: "Gagal mengambil data profil", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.usernameTextView.text = "Hello, User"
                Log.e("HomeActivity", "Error: ${e.message}", e)
                Toast.makeText(this@HomeActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun clearUserData() {
        val database = AppDatabase.getDatabase(applicationContext)
        withContext(Dispatchers.IO) {
            database.classificationResultDao().clearAll()
        }

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    private fun navigateToAnimalDetail(animalName: String) {
        if (isNavigated) return
        isNavigated = true
        val intent = Intent(this, AnimalDetailActivity::class.java)
        intent.putExtra("ANIMAL_NAME", animalName)
        startActivityForResult(intent, REQUEST_ANIMAL_DETAIL) // Start activity for result
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun navigateToResultActivity() {
        if (isNavigated) return

        isNavigated = true

        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        isNavigated = false
    }

    override fun onError(message: String) {
        runOnUiThread {
            showAlert("Error", message)
        }
    }

    override fun onClassifications(results: List<Pair<String, Float>>?, inferenceDuration: Long) {
        if (isNavigated) {
            return
        }

        if (results.isNullOrEmpty()) {
            runOnUiThread {
                showAlert(getString(R.string.no_classification_found),
                    getString(R.string.the_image_did_not_yield_any_classifications))
            }
            isNavigated = true
            navigateToResultActivity()
        } else {
            val classification = results.firstOrNull()?.first
            if (classification != null) {
                Log.d("HomeActivity", "Classification found: $classification")
                isNavigated = true
                navigateToAnimalDetail(classification)
            } else {
                Log.d("HomeActivity", getString(R.string.no_valid_classification_result))
                isNavigated = true
                navigateToResultActivity()
            }
        }
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}
