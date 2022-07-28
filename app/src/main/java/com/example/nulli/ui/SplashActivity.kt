package com.example.nulli.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nulli.MainActivity
import com.example.nulli.databinding.ActivitySplashBinding
import com.example.nulli.ui.auth.AuthMailActivity
import com.example.nulli.ui.auth.JoinActivity
import com.example.nulli.ui.auth.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission


class SplashActivity : AppCompatActivity() {

    val binding : ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Handler(Looper.myLooper()!!).postDelayed({
                val user = Firebase.auth.currentUser
                if (user != null) {
                    user.reload()

                    val intent = if (user.isEmailVerified) {
                        Intent(this@SplashActivity, MainActivity::class.java)
                    } else {
                        Intent(this@SplashActivity, AuthMailActivity::class.java)
                    }

                    startActivity(intent)
                    finish()
                } else {
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.btnJoin.visibility = View.VISIBLE
                }

            },2000)
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
        )
            .check();



        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}