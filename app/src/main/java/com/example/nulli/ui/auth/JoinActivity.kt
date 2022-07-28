package com.example.nulli.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.nulli.MainActivity
import com.example.nulli.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {
    private val TAG: String = "JoinActivity"
    val binding: ActivityJoinBinding by lazy {
        ActivityJoinBinding.inflate(layoutInflater)
    }

    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnJoin.setOnClickListener {
            join()
        }

    }

    fun join() {
        var isJoin = true
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val name = binding.etName.text.toString()
        val password2 = binding.etPassword2.text.toString()

        if(email.isEmpty()) {
            Toast.makeText(this,"이메일을 입력해주세요", Toast.LENGTH_LONG).show()
            isJoin = false
        }
        if(name.isEmpty()) {
            Toast.makeText(this,"닉네임을 입력해주세요",Toast.LENGTH_LONG).show()
            isJoin = false
        }
        if(password.isEmpty()) {
            Toast.makeText(this,"패스워드를 입력해주세요",Toast.LENGTH_LONG).show()
            isJoin = false
        }
        //비밀번호 자리
        if(password.length < 8) {
            Toast.makeText(this,"비밀번호를 8자리 이상으로 입력해주세요",Toast.LENGTH_LONG).show()
            isJoin = false
        }
        if(password != password2) {
            Toast.makeText(this,"같은 비밀번호를 입력해주세요",Toast.LENGTH_LONG).show()
            isJoin = false
        }
        if(isJoin){

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"인증 이메일이 전송됐습니다. 확인해주세요",Toast.LENGTH_LONG).show()
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            sendAuthMail()
        }

    }

    fun sendAuthMail(){
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    val intent = Intent(this, AuthMailActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
    }
}