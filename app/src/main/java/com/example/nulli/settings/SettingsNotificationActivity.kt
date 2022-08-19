package com.example.nulli.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nulli.R
import com.example.nulli.databinding.ActivitySettingsNotificationBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsNotificationActivity : AppCompatActivity() {

    val auth = Firebase.auth
    val fuser = auth.currentUser

    private val binding : ActivitySettingsNotificationBinding by lazy {
        ActivitySettingsNotificationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}