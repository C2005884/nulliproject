package com.example.nulli.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nulli.R
import com.example.nulli.databinding.ActivityBoardListBinding
import com.example.nulli.databinding.ActivitySettingsDeclareBinding
import com.example.nulli.declare.DeclareBoardActivity
import com.example.nulli.declare.DeclareObstacleActivity
import com.example.nulli.declare.DeclareReviewActivity


class SettingsDeclareActivity : AppCompatActivity() {

    private val binding: ActivitySettingsDeclareBinding by lazy {
        ActivitySettingsDeclareBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getSupportActionBar()?.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setCustomView(R.layout.custom_toolbar);

        binding.tvBoardDeclare.setOnClickListener {
            val intent = Intent(this, DeclareBoardActivity::class.java)
            startActivity(intent)
        }

        binding.tvObstacleDeclare.setOnClickListener {
            val intent = Intent(this, DeclareObstacleActivity::class.java)
            startActivity(intent)
        }

        binding.tvReviewDeclare.setOnClickListener {
            val intent = Intent(this, DeclareReviewActivity::class.java)
            startActivity(intent)
        }

        binding.tvBoardDeclare2.setOnClickListener {
            val intent = Intent(this, DeclareBoardActivity::class.java)
            startActivity(intent)
        }

        binding.tvObstacleDeclare2.setOnClickListener {
            val intent = Intent(this, DeclareObstacleActivity::class.java)
            startActivity(intent)
        }

        binding.tvReviewDeclare2.setOnClickListener {
            val intent = Intent(this, DeclareReviewActivity::class.java)
            startActivity(intent)
        }
    }
}