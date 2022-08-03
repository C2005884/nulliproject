package com.example.nulli.ui.map

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nulli.databinding.ActivityWriteObstacleBinding
import com.example.nulli.util.ImageAnalysis
import gun0912.tedimagepicker.builder.TedImagePicker

class WriteObstacleActivity : AppCompatActivity() {
    private val binding:ActivityWriteObstacleBinding by lazy {
        ActivityWriteObstacleBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.ivObstacle.setOnClickListener {
            TedImagePicker.with(this)
                .start { uri -> showSingleImage(uri) }
        }
    }

    private fun showSingleImage(uri: Uri) {
        ImageAnalysis(this).apply {
            classifyImage(uri.toString()) { classified, confidence ->
                Toast.makeText(this@WriteObstacleActivity, "$classified $confidence", Toast.LENGTH_SHORT).show()
            }
        }
    }
}