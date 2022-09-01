package com.example.nulli.ui.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nulli.R
import com.example.nulli.databinding.ActivityScrapDetailBinding

class ScrapDetailActivity : AppCompatActivity() {

    private val binding : ActivityScrapDetailBinding by lazy {
        ActivityScrapDetailBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}