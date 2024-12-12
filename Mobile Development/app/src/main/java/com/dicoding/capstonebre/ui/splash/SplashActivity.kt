package com.dicoding.capstonebre.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.capstonebre.R
import com.dicoding.capstonebre.ui.home.HomeActivity
import com.dicoding.capstonebre.ui.welcome.WelcomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(2000)

            val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
            val isRegistered = sharedPreferences.getBoolean("isRegistered", false)

            val intent = if (isRegistered) {
                Intent(this@SplashActivity, HomeActivity::class.java)
            } else {
                Intent(this@SplashActivity, WelcomeActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }
}
