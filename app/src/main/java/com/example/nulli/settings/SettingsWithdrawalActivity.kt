package com.example.nulli.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nulli.databinding.ActivitySettingsWithdrawalBinding
import com.example.nulli.LoginJoinActivity
import com.example.nulli.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsWithdrawalActivity : AppCompatActivity() {

    val auth = Firebase.auth
    val fuser = auth.currentUser

    private val binding : ActivitySettingsWithdrawalBinding by lazy {
        ActivitySettingsWithdrawalBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)

        getSupportActionBar()?.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setCustomView(R.layout.custom_toolbar);

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
                                val intent = Intent(this, LoginJoinActivity::class.java)
                                startActivity(intent)
                            }
                        }
                }
        }
    }
}