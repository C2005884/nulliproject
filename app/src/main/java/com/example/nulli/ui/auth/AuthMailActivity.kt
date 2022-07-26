package com.example.nulli.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.nulli.MainActivity
import com.example.nulli.R
import com.example.nulli.databinding.ActivityAuthMailBinding
import com.example.nulli.ui.SplashActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthMailActivity : AppCompatActivity() {
    val binding : ActivityAuthMailBinding by lazy {
        ActivityAuthMailBinding.inflate(layoutInflater)
    }
    val auth = Firebase.auth
    val fuser = auth.currentUser

    override fun onResume() {
        super.onResume()
        checkAuthMail()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnReAuth.setOnClickListener {
            sendAuthMail()
        }
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this,SplashActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun sendAuthMail() {
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "인증 메일을 다시 보내드렸습니다.", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun checkAuthMail(){
        fuser?.reload()
        if (true == fuser?.isEmailVerified){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}