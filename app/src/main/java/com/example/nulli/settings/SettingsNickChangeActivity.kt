package com.example.nulli.settings

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.nulli.R
import com.example.nulli.databinding.ActivitySettingsNickChangeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class SettingsNickChangeActivity : AppCompatActivity() {

    val auth = Firebase.auth
    val fuser = auth.currentUser

    private val binding : ActivitySettingsNickChangeBinding by lazy {
        ActivitySettingsNickChangeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.stnick.setText(fuser?.displayName?: "")
        binding.btnChange.setOnClickListener {
            val profileUpdates = userProfileChangeRequest {
                displayName = binding.stnick.text.toString()
            }

            fuser!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "아이디 삭제가 완료되었습니다", Toast.LENGTH_LONG).show()
                        FirebaseAuth.getInstance().signOut()
                        finish()
                       // Log.d(TAG, "User profile updated.")
                    }
                }
        }
    }
}