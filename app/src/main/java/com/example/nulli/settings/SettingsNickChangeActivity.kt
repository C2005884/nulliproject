package com.example.nulli.settings

import android.app.ActionBar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.nulli.R
import com.example.nulli.databinding.ActivitySettingsNickChangeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SettingsNickChangeActivity : AppCompatActivity() {

    val auth = Firebase.auth
    val fuser = auth.currentUser
    val db = Firebase.database.reference

    private val binding : ActivitySettingsNickChangeBinding by lazy {
        ActivitySettingsNickChangeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        getSupportActionBar()?.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setCustomView(R.layout.custom_toolbar);

        binding.etNicknameChange.setText(fuser?.displayName?: "")
        binding.btnChange.setOnClickListener {
            val profileUpdates = userProfileChangeRequest {
                displayName = binding.etNicknameChange.text.toString()
            }

            fuser!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        db.child("user").child(fuser.uid).child("nickname").setValue(
                            binding.etNicknameChange.text.toString()
                        ).addOnCompleteListener {
                            Toast.makeText(this, "닉네임 변경이 완료되었습니다", Toast.LENGTH_LONG).show()
                            finish()
                        }
                       // Log.d(TAG, "User profile updated.")
                    }
                }
        }
    }
}