package com.example.nulli.settings

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.nulli.R
import com.example.nulli.databinding.ActivitySettingsImgChangeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import gun0912.tedimagepicker.builder.TedImagePicker

class SettingsImgChangeActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private val fuser = auth.currentUser

    private val binding : ActivitySettingsImgChangeBinding by lazy {
        ActivitySettingsImgChangeBinding.inflate(layoutInflater)
    }

    private var imageUri:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imgChange.setOnClickListener {
            TedImagePicker.with(this).start {
                showSingleImage(it)
            }
        }

        binding.btnChange.setOnClickListener {

            val profileUpdates = userProfileChangeRequest {
                photoUri = imageUri
            }

            fuser!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                       finish()
                    }
                }
        }
    }

    private fun showSingleImage(it: Uri) {
        Glide.with(this).load(it).into(binding.imgChange)
        imageUri = it
    }

}