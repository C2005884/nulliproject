package com.example.nulli.ui.mypage

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.nulli.databinding.ActivityBoardReadBinding
import com.example.nulli.databinding.ActivityMypageBinding
import com.example.nulli.databinding.FragmentMypageBinding
import com.example.nulli.settings.SettingsDeclareActivity
import com.example.nulli.settings.SettingsImgChangeActivity
import com.example.nulli.settings.SettingsNickChangeActivity
import com.example.nulli.settings.SettingsWithdrawalActivity
import com.example.nulli.ui.SplashActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MypageActivity : AppCompatActivity(){

    private val binding: ActivityMypageBinding by lazy {
        ActivityMypageBinding.inflate(layoutInflater)
    }


    val auth = Firebase.auth
    val fuser = auth.currentUser
    var b4ClickTime = 0L


    override fun onBackPressed() {
        if(System.currentTimeMillis() - b4ClickTime < 3000){
            finish()
        }else{
            b4ClickTime = System.currentTimeMillis()
            Toast.makeText(this,"한번 더 누르시면 앱이 꺼집니다.",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        Glide.with(binding.root).load(fuser?.photoUrl).into(binding.ivProfile)
        binding.tvNickname.text = fuser?.displayName
        binding.tvProfile.text = """
            ${fuser?.email}
        """.trimMargin("|")



        binding.tvWithhdrawal.setOnClickListener {
            val intent = Intent(this,SettingsWithdrawalActivity::class.java)
            startActivity(intent)
        }

        binding.tvNicnameChange.setOnClickListener {
            val intent = Intent(this,SettingsNickChangeActivity::class.java)
            startActivity(intent)
        }

        binding.tvProfileImageChange.setOnClickListener {
            val intent = Intent(this,SettingsImgChangeActivity::class.java)
            startActivity(intent)
        }

        binding.tvPasswordChange.setOnClickListener {
            val emailAddress = fuser?.email
            // val dialog = PwchangeConfirmDialog().apply {  }.dialog.show(supportFragmentManager, null)

            val builder = AlertDialog.Builder(this)
            builder
                .setTitle("비밀번호 변경")
                .setMessage("이메일이 전송되었습니다. 이메일을 확인해주세요")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Start 버튼 선택시 수행
                    })
            builder.create()
            builder.show()

            Firebase.auth.sendPasswordResetEmail(emailAddress!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    }
                }
        }

        binding.tvDeclare.setOnClickListener {
            val intent = Intent(this,SettingsDeclareActivity::class.java)
            startActivity(intent)
        }

        binding.tvLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this,SplashActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    fun clear(){

    }

}