package com.dicoding.capstonebre.ui.welcome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstonebre.R
import android.content.Intent
import androidx.activity.viewModels
import android.widget.Button
import com.dicoding.capstonebre.ui.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private val welcomeViewModel: WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val btnDaftar: Button = findViewById(R.id.btnDaftar)
        btnDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
