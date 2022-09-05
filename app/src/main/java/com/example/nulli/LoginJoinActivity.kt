package com.example.nulli

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.nulli.databinding.ActivitySplashBinding
import com.example.nulli.model.UserData
import com.example.nulli.ui.auth.AuthMailActivity
import com.example.nulli.ui.auth.JoinActivity
import com.example.nulli.ui.auth.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import java.io.ByteArrayOutputStream


class LoginJoinActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 9001
    private val db = Firebase.database.reference
    private val storage = Firebase.storage.reference

    val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Handler(Looper.myLooper()!!).postDelayed({
                val user = Firebase.auth.currentUser
                if (user != null) {
                    user.reload()

                    val intent = if (user.isEmailVerified) {
                        Intent(this@LoginJoinActivity, MainActivity::class.java)
                    } else {
                        Intent(this@LoginJoinActivity, AuthMailActivity::class.java)
                    }

                    startActivity(intent)
                    finish()
                } else {
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.btnJoin.visibility = View.VISIBLE
                    binding.btnGoogleSignIn.visibility = View.VISIBLE
                }

            },2000)
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

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

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        var googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInGoogleBtn:SignInButton = findViewById(R.id.btn_googleSignIn)

        signInGoogleBtn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this,"로그인 실패", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val fuser = auth.currentUser!!
                    Log.d("xxxx ", fuser.toString())
                    val user = UserData(
                        uid = fuser.uid,
                        email = fuser.email,
                        nickname = fuser.displayName,
                        profileImageUri = "https://firebasestorage.googleapis.com/v0/b/nulli-e491a.appspot.com/o/user%2F${fuser?.uid}?alt=media",
                        scrapMap = hashMapOf(),
                        myContentMap = hashMapOf(),
                    )

                    db.child("user").child(fuser.uid).setValue(user).addOnCompleteListener {
                        if (fuser.photoUrl == null) {
                            moveMainPage(task.result?.user)
                        } else {
                            Glide.with(this)
                                .asBitmap()
                                .load(fuser.photoUrl)
                                .into(object : CustomTarget<Bitmap>(){
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        val baos = ByteArrayOutputStream()
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                                        val data = baos.toByteArray()

                                        val profileImageRef = storage.child("user").child(fuser?.uid!!)
                                        var uploadTask = profileImageRef.putBytes(data)
                                        uploadTask.addOnFailureListener {
                                            // Handle unsuccessful uploads
                                        }.addOnCompleteListener { taskSnapshot ->
                                            moveMainPage(task.result?.user)
                                        }

                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {

                                    }

                                })
                        }
                    }
                } else {
                    Log.d("xxxx ", "signInWithCredential:failure", task.exception)
                }

            }
    }

    fun moveMainPage(user: FirebaseUser?) {
        if(user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}