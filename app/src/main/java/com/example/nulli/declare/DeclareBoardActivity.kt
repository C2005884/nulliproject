package com.example.nulli.declare

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class DeclareBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.nulli.R.layout.activity_declare_board)
        val email = findViewById<View>(com.example.nulli.R.id.btnEmail) as TextView
        email.setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)
            email.type = "plain/text"
            val address = arrayOf("olivesession@gmail.com")
            email.putExtra(Intent.EXTRA_EMAIL, address)
            email.putExtra(Intent.EXTRA_SUBJECT, "[널리/게시물 권리침해 및 유해정보 신고]")
            email.putExtra(Intent.EXTRA_TEXT, "아래 양식에 맞추어 신고글을 작성해주시기 바랍니다. \n\n 해당 게시물 게시 날짜: \n 해당 게시물 신고 이유:")
            startActivity(email)
        }
    }
}