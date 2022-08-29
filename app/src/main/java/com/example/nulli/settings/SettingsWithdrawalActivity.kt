package com.example.nulli.settings

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.nulli.R
import com.example.nulli.databinding.ActivitySettingsWithdrawalBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class SettingsWithdrawalActivity : AppCompatActivity() {

    val auth = Firebase.auth
    val fuser = auth.currentUser

    private val binding : ActivitySettingsWithdrawalBinding by lazy {
        ActivitySettingsWithdrawalBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnWithhdrawal.setOnClickListener {
            val user = Firebase.auth.currentUser!!
            val password = binding.etPassword.text.toString()
            val credential = EmailAuthProvider.getCredential(user.email.toString(), password)

// Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                .addOnCompleteListener {
                    user.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "탈퇴가 완료되었습니다")
                            }
                        }
                }
        }
    }
}