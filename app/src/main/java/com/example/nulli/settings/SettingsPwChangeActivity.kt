package com.example.nulli.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nulli.R
import com.example.nulli.databinding.ActivitySettingsPwChangeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsPwChangeActivity : AppCompatActivity() {

    val auth = Firebase.auth
    val fuser = auth.currentUser

    private val binding : ActivitySettingsPwChangeBinding by lazy {
        ActivitySettingsPwChangeBinding.inflate(layoutInflater)
    }

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}