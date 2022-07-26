package com.example.nulli.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nulli.R
import com.example.nulli.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val binding : ActivityJoinBinding by lazy {
            ActivityJoinBinding.inflate(layoutInflater)
        }
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}