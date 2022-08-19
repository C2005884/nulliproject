package com.example.nulli.board

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.nulli.R
import com.example.nulli.databinding.ActivityBoardWriteBinding
import com.example.nulli.databinding.ActivityReviewBuildingBinding
import com.example.nulli.databinding.ActivityWriteObstacleBinding
import com.example.nulli.model.Building
import com.example.nulli.model.Content
import com.example.nulli.model.Obstacle
import com.example.nulli.model.Review
import com.example.nulli.ui.map.WriteBuildingActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import gun0912.tedimagepicker.builder.TedImagePicker
import java.text.SimpleDateFormat
import java.util.*

class BoardWriteActivity: AppCompatActivity() {
    private val storage = Firebase.storage.reference
    private val db = Firebase.database.reference
    private val auth = Firebase.auth
    private val fUser = auth.currentUser

    private var mId: String = ""

    private var mImageUri: Uri? = null

    private val binding: ActivityBoardWriteBinding by lazy {
        ActivityBoardWriteBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.etContent.setHint(
            Html.fromHtml("<h6>" + "내용을 입력해주세요" + "</h6>" + "<small>" + "" +
                    "<br>" +
                    "널리는 쾌적한 커뮤니티를 만들기 위해 커뮤니티 이용규칙을 제정하여 운영하고 있습니다. 위반 시 게시물이 삭제되고 서비스 이용이 제한될 수 있습니다."+
                    "<br>" +
                    "※ 규칙 위반"+
                    "<br>" +
                    "- 타인의 권리를 침해하거나 불쾌감을 주는 행위"+
                    "<br>" +
                    "- 범죄, 불법 행위 등 법령을 위반하는 행위"+
                    "<br>" +
                    "- 욕설, 비하, 차별, 혐오, 자살, 폭력 관련 내용을 포함한 게시물 작성 행위"+
                    "<br>" +
                    "- 음란물, 성적 수치심을 유발하는 행위"+
                    "<br>" +
                    "- 스포일러, 공포, 속임, 놀라게 하는 행위"+
                    "<br>" +
                    "</small>"))

        mId = intent.getStringExtra(WriteBuildingActivity.ID)?:""



        binding.ivPhoto.setOnClickListener {
            TedImagePicker.with(this)
                .start { uri -> showSingleImage(uri) }
        }

        binding.btnPost.setOnClickListener {
            if(binding.etContent.toString().length<10) {
                Toast.makeText(this, "내용을 10자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                makeContent()
            }
        }
    }


    private fun makeContent() {
        val contentRef = db.child("content")
        val ref = db.child("content")

        Log.e("mid","${mId}")
        var key = ref.push().key!!
        val content = Content().apply {
            id = key
            uid = fUser?.uid.toString()
            title = binding.etTitle.text.toString()
            content = binding.etContent.text.toString()
            nickname = fUser?.displayName.toString()
            date = System.currentTimeMillis()
            dateText = SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.KOREAN).format(Date())
            profileImageUri = fUser?.photoUrl.toString()
            imageUri =
                if(mImageUri == null) "" else
                    """https://firebasestorage.googleapis.com/v0/b/nulli-e491a.appspot.com/o/content%2F$key?alt=media"""
        }


        ref.child(key).setValue(content).addOnCompleteListener {
            contentRef.child(mId).child("content").child(key)
                .setValue(System.currentTimeMillis()).addOnCompleteListener {

                        Toast.makeText(this, "등록 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()

                }
        }
    }

    private fun showSingleImage(uri: Uri) {

        mImageUri = uri
        Glide.with(this).load(mImageUri).into(binding.ivPhoto)
    }


    companion object {
        const val ID = "id"
    }

}