package com.dicoding.capstonebre.ui.detail

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstonebre.R
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.capstonebre.api.ApiConfig
import com.dicoding.capstonebre.api.ApiService
import com.dicoding.capstonebre.response.DetailData
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstonebre.question.QuestionAdapter
import com.dicoding.capstonebre.response.QuestionData
import com.dicoding.capstonebre.response.RequestQBody
import java.lang.Exception

class AnimalDetailActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var nameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var scientificNameTextView: TextView
    private lateinit var animalImageView: ImageView
    private lateinit var playSoundButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var questionsRecyclerView: RecyclerView
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var applausePlayer: MediaPlayer
    private lateinit var wrongPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_detail)

        apiService = ApiConfig.getApiService()

        nameTextView = findViewById(R.id.tvAnimalName)
        descriptionTextView = findViewById(R.id.tvAnimalDescription)
        scientificNameTextView = findViewById(R.id.tvAnimalScientificName)
        animalImageView = findViewById(R.id.ivAnimalImage)
        playSoundButton = findViewById(R.id.btnPlaySound)
        progressBar = findViewById(R.id.loadingProgressBar)
        questionsRecyclerView = findViewById(R.id.rvAnimalQuestions)
        questionsRecyclerView.layoutManager = LinearLayoutManager(this)
        applausePlayer = MediaPlayer.create(this, R.raw.applause)
        wrongPlayer = MediaPlayer.create(this, R.raw.wrong)

        val animalName = intent.getStringExtra("ANIMAL_NAME")
        if (animalName != null) {
            loadAnimalDetail(animalName)
        } else {
            showToast(getString(R.string.uknown_animal))
        }
    }
    private fun loadAnimalQuestions(animalName: String) {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = apiService.getAnimalQuestions(animalName)
                if (response.isSuccessful) {
                    val questionData = response.body()?.data
                    if (questionData != null) {
                        displayQuestions(questionData)
                    } else {
                        showToast("Pertanyaan tidak ditemukan.")
                    }
                } else {
                    showToast("Gagal memuat pertanyaan.")
                }
            } catch (e: Exception) {
                Log.e("AnimalDetailActivity", "Error loading questions: ${e.message}")
                showToast("Terjadi kesalahan: ${e.message}")
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
    private fun displayQuestions(questionData: QuestionData) {
        val questionAdapter = QuestionAdapter(questionData) { selectedAnswer ->
            verifyAnswer(selectedAnswer)
        }
        questionsRecyclerView.adapter = questionAdapter
    }

    private fun loadAnimalDetail(animalName: String) {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = apiService.getAnimalDetail(animalName)
                if (response.isSuccessful) {
                    val animalDetail = response.body()?.data
                    if (animalDetail != null) {
                        displayAnimalDetail(animalDetail)
                    } else {
                        showAnimalNotFoundDialog(animalName)
                    }
                } else {
                    showAnimalNotFoundDialog(animalName)
                }
            } catch (e: Exception) {
                Log.e("AnimalDetailActivity", "Error loading details: ${e.message}")
                showToast("Terjadi kesalahan: ${e.message}")
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun verifyAnswer(selectedAnswer: String) {
        val requestQBody = RequestQBody(answer = selectedAnswer)
        val animalName = nameTextView.text.toString()

        lifecycleScope.launch {
            try {
                val response = apiService.verifyAnswer(animalName, requestQBody)
                if (response.isSuccessful) {
                    val verifyData = response.body()?.data
                    if (verifyData != null) {
                        if (verifyData.correct == true) {
                            showResultDialog("Jawaban benar! ${verifyData.funFact}", true)
                            applausePlayer.start()
                        } else {
                            showResultDialog(getString(R.string.jawaban_salah_coba_lagi), false)
                            wrongPlayer.start()
                        }
                    } else {
                        showToast("Data verifikasi tidak ditemukan.")
                    }
                } else {
                    showToast("Gagal memverifikasi jawaban.")
                }
            } catch (e: Exception) {
                Log.e("AnimalDetailActivity", "Error verifying answer: ${e.message}")
                showToast("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    private fun displayAnimalDetail(animalDetail: DetailData) {
        nameTextView.text = (animalDetail.name ?: "Nama tidak tersedia").replace("[#*]".toRegex(), "")
        descriptionTextView.text = (animalDetail.description ?: "Deskripsi tidak tersedia").replace("[#*]".toRegex(), "")
        scientificNameTextView.text = (animalDetail.scientificName ?: "Nama ilmiah tidak tersedia").replace("[#*]".toRegex(), "")

        animalDetail.imageUrl?.let { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.baseline_image_24)
                .error(R.drawable.baseline_broken_image_24)
                .into(animalImageView)
        } ?: run {
            animalImageView.setImageResource(R.drawable.baseline_image_24)
        }

        loadAnimalQuestions(animalDetail.name ?: "")

        animalDetail.soundUrl?.let { soundUrl ->
            playSoundButton.visibility = View.VISIBLE
            playSoundButton.setOnClickListener {
                playAnimalSound(soundUrl)
            }
        } ?: run {
            playSoundButton.visibility = View.GONE
        }
    }
    private fun showResultDialog(message: String, isCorrect: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(if (isCorrect) "Jawaban Benar!" else "Jawaban Salah")
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setBackgroundColor(if (isCorrect) Color.GREEN else Color.RED)
            button.setTextColor(Color.WHITE)
        }

        dialog.show()
    }

    private fun playAnimalSound(soundUrl: String) {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(soundUrl)
                prepare()
                start()
            }
            showToast(getString(R.string.memutar_suara_hewan))

            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
                showToast(getString(R.string.suara_selesai_diputar))
            }
        } catch (e: Exception) {
            Log.e("AnimalDetailActivity", "Error playing sound: ${e.message}")
            showToast("Gagal memutar suara: ${e.message}")
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
    }

    private fun showAnimalNotFoundDialog(animalName: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.hewan_tidak_ditemukan))
        builder.setMessage(getString(R.string.tidak_ditemukan, animalName))
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        builder.create().show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
