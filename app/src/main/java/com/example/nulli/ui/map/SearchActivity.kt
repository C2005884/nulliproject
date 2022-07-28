package com.example.nulli.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nulli.R
import com.example.nulli.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private val binding : ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            search(textView.text.toString())
            false
        }
    }

    private fun search(text: String) {
        Toast.makeText(this,"$text", Toast.LENGTH_SHORT).show()

    }
}