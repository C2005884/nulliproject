package com.example.nulli.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.nulli.R
import com.example.nulli.databinding.ActivitySettingsWithdrawalBinding
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
            val deleteEmails =  {

            }
        }
        fuser!!.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    finish()
                }
            }
    }
}