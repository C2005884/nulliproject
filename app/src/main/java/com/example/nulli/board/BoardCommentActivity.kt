package com.example.nulli.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nulli.R
import com.example.nulli.databinding.ActivityBoardCommentBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class BoardCommentActivity : AppCompatActivity() {

    val auth = Firebase.auth
    val fuser = auth.currentUser

    private val binding : ActivityBoardCommentBinding by lazy {
        ActivityBoardCommentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}