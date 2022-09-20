package com.example.nulli.settings

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nulli.R
import com.example.nulli.databinding.ActivitySettingsImgChangeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import gun0912.tedimagepicker.builder.TedImagePicker

class SettingsImgChangeActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private val fuser = auth.currentUser
    private val db = Firebase.database.reference
    private val storage = Firebase.storage.reference

    private val binding: ActivitySettingsImgChangeBinding by lazy {
        ActivitySettingsImgChangeBinding.inflate(layoutInflater)
    }

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        getSupportActionBar()?.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setCustomView(R.layout.custom_toolbar);

        Glide.with(this).load(fuser?.photoUrl).into(binding.imgChange)

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
                        db.child("user").child(auth.uid!!).child("profileImageUri").setValue(
                            """https://firebasestorage.googleapis.com/v0/b/nulli-e491a.appspot.com/o/user%2F${auth.uid!!}?alt=media"""
                        ).addOnCompleteListener {
                            storage.child("user").child(auth.uid!!).putFile(imageUri!!)
                                .addOnCompleteListener {
                                    Toast.makeText(this, "프로필 이미지 변경이 완료되었습니다", Toast.LENGTH_SHORT)
                                        .show()
                                    finish()
                                }
                        }
                    }
                }
        }
    }

    private fun showSingleImage(it: Uri) {
        Glide.with(this).load(it).into(binding.imgChange)
        imageUri = it
    }

}