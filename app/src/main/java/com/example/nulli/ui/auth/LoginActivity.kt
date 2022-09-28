package com.example.nulli.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.nulli.LoginJoinActivity
import com.example.nulli.MainActivity
import com.example.nulli.R
import com.example.nulli.databinding.ActivityLoginBinding
import com.example.nulli.model.UserData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private val TAG: String = "LoginActivity"

    val binding : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    fun login(){
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"로그인에 성공했습니다.",Toast.LENGTH_LONG).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            user.reload()
            val database = Firebase.database.reference
            database.child("user").child(user.uid).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(UserData::class.java)!!
                    if (userData.activation.isNullOrBlank() || userData.activation == "true") {
                        if (user.isEmailVerified) {
                            val intent = if (user.isAnonymous) {
                                Intent(this@LoginActivity, MainActivity::class.java)
                            } else {
                                Intent(this@LoginActivity, AuthMailActivity::class.java)
                            }
                            startActivity(intent)
                            finishAffinity()
                        }
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "접근 차단된 유저 입니다.",Toast.LENGTH_SHORT).show()
                        auth.signOut()

                        val intent = Intent(this@LoginActivity, LoginJoinActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        }
    }

}