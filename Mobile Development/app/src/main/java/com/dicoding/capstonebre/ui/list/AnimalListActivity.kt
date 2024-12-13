package com.dicoding.capstonebre.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstonebre.R
import com.dicoding.capstonebre.api.AnimalAdapter
import com.dicoding.capstonebre.api.ApiConfig
import com.dicoding.capstonebre.api.ApiService
import com.dicoding.capstonebre.ui.camera.CameraActivity
import com.dicoding.capstonebre.ui.detail.AnimalDetailActivity
import com.dicoding.capstonebre.ui.home.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class AnimalListActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var animalAdapter: AnimalAdapter
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_list)

        apiService = ApiConfig.getApiService()
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recycle_api)
        bottomNavigationView = findViewById(R.id.bottomNav)

        animalAdapter = AnimalAdapter { animalName ->
            val intent = Intent(this, AnimalDetailActivity::class.java)
            intent.putExtra("ANIMAL_NAME", animalName)
            startActivity(intent)

        }

        setupRecyclerView()
        loadAnimals()

        bottomNavigationView.selectedItemId = R.id.daftar

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.camera -> {
                    startActivityForResult(Intent(this, CameraActivity::class.java), REQUEST_CAMERA)
                    finish()
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.daftar -> true
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = animalAdapter
    }

    private fun loadAnimals() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val response = apiService.getAnimals()
                if (response.isSuccessful) {
                    response.body()?.data?.let { animals ->
                        animalAdapter.submitList(animals)
                    } ?: showToast("Data hewan tidak ditemukan.")
                } else {
                    showToast("Gagal memuat data: ${response.message()}")
                }
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            bottomNavigationView.selectedItemId = R.id.home
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    companion object {
        private const val REQUEST_CAMERA = 1
    }
}

