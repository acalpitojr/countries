package com.calpito.walmart.presentation.views

import com.calpito.walmart.databinding.ActivitySplashBinding



import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 2000 // 2 seconds
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Using a Handler to delay the opening of the main activity
        Handler().postDelayed({
            // Start the main activity after the splash time out
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close the splash activity to prevent going back to it
        }, SPLASH_TIME_OUT)
    }
}
