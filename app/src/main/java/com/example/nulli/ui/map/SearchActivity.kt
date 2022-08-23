package com.example.nulli.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nulli.R
import com.example.nulli.api.CallApi
import com.example.nulli.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private val binding : ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        setRv()

        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            search(textView.text.toString())
            false
        }
    }

    private fun setRv() {

        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = SearchAdapter().apply {
                itemClick = {
                    Toast.makeText(this@SearchActivity, it, Toast.LENGTH_SHORT).show()
                    CallApi().getLatLng(it){
                        Toast.makeText(this@SearchActivity, "${it?.x} ${it?.y}", Toast.LENGTH_LONG).show()

                        //Toast.makeText(this@SearchActivity, it.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
//        binding.rvSearch.layoutManager = LinearLayoutManager(this)
//        binding.rvSearch.adapter = SearchAdapter()
    }

    private fun search(text: String) {
        //Toast.makeText(this,"$text", Toast.LENGTH_SHORT).show()
        CallApi().search(text) {
            (binding.rvSearch.adapter as SearchAdapter).setDatas(ArrayList(it))
        }

    }
}