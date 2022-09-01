package com.example.nulli.ui.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nulli.R
import com.example.nulli.databinding.ActivityMyContentDetailBinding

class MyContentDetailActivity : AppCompatActivity() {

    private val binding : ActivityMyContentDetailBinding by lazy {
        ActivityMyContentDetailBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}