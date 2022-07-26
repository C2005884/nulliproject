package com.example.nulli.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.nulli.MainActivity
import com.example.nulli.R
import com.example.nulli.databinding.ActivitySplashBinding
import com.example.nulli.ui.auth.AuthMailActivity
import com.example.nulli.ui.auth.JoinActivity
import com.example.nulli.ui.auth.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    val binding : ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Handler(Looper.myLooper()!!).postDelayed({
            val user = Firebase.auth.currentUser
            if (user != null) {
                user.reload()

                    val intent = if (user.isEmailVerified) {
                        Intent(this, MainActivity::class.java)
                    } else {
                        Intent(this, AuthMailActivity::class.java)
                    }

                    startActivity(intent)
                    finish()
            } else {
                binding.btnLogin.visibility = View.VISIBLE
                binding.btnJoin.visibility = View.VISIBLE
            }

        },2000)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}