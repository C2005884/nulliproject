package com.example.nulli.ui.map

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.nulli.R
import com.example.nulli.databinding.ActivityReviewBuildingBinding
import com.example.nulli.databinding.ActivityWriteObstacleBinding
import com.example.nulli.model.Building
import com.example.nulli.model.Obstacle
import com.example.nulli.model.Review
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

class ReviewBuildingActivity : AppCompatActivity() {
    private val storage = Firebase.storage.reference
    private val db = Firebase.database.reference
    private val auth = Firebase.auth
    private val fUser = auth.currentUser

    private var mId: String = ""

    private lateinit var maddress:String
    private var mtype: Int = -1
    private var mImageUri: Uri? = null

    private val binding: ActivityReviewBuildingBinding by lazy {
        ActivityReviewBuildingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.etContent.setHint(
            Html.fromHtml("<h6>" + "내용을 입력해주세요" + "</h6>" + "<small>" + "" +
                    "<br>" +
                    "사용자들의 원활한 지도 사용을 위해 부적절한 게시를 금합니다. 위반 시 게시물이 삭제되고 서비스 이용이 제한될 수 있습니다."+
                    "</small>"))

        mtype = intent.getIntExtra(WriteBuildingActivity.TYPE, 1)
        maddress = intent.getStringExtra(WriteBuildingActivity.ADR) ?:""
        mId = intent.getStringExtra(WriteBuildingActivity.ID)?:""
        binding.tvAddress.text = maddress

        when(mtype) {
            Building.HOSPITAL -> {
                Glide.with(this@ReviewBuildingActivity).load(R.drawable.hospital).into(binding.ivType)
                binding.tvType.text = "병원"
            }
            Building.DRUG -> {
                Glide.with(this@ReviewBuildingActivity).load(R.drawable.drug).into(binding.ivType)
                binding.tvType.text = "약국"
            }
            Building.REHABILITATION -> {
                Glide.with(this@ReviewBuildingActivity).load(R.drawable.rehabilitation).into(binding.ivType)
                binding.tvType.text = "재활센터"
            }
            Building.VRIOUS -> {
                Glide.with(this@ReviewBuildingActivity).load(R.drawable.vrious).into(binding.ivType)
                binding.tvType.text = "기타"
            }
        }


        binding.ivReview.setOnClickListener {
            TedImagePicker.with(this)
                .start { uri -> showSingleImage(uri) }
        }
        binding.tvApply.setOnClickListener {
            if(binding.etContent.toString().length<10) {
                Toast.makeText(this, "내용을 10자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                makeReview()
            }
        }
    }


    private fun makeReview() {
        val buildingRef = db.child("building")
        val ref = db.child("review")

        Log.e("mid","${mId}")
        var key = ref.push().key!!
        val review = Review().apply {
            id = key
            uid = fUser?.uid.toString()
            buildingId = mId
            content = binding.etContent.text.toString()
            nickname = fUser?.displayName.toString()
            date = System.currentTimeMillis()
            dateText = SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.KOREAN).format(Date())
            profileImageUri = fUser?.photoUrl.toString()
            imageUri =
                if(mImageUri == null) "" else
                """https://firebasestorage.googleapis.com/v0/b/nulli-e491a.appspot.com/o/review%2F$key?alt=media"""
        }


            ref.child(key).setValue(review).addOnCompleteListener {
                buildingRef.child(mId).child("review").child(key)
                    .setValue(System.currentTimeMillis()).addOnCompleteListener {
                        if(mImageUri != null){
                        storage.child("review").child(key).putFile(mImageUri!!)
                            .addOnCompleteListener {
                            Toast.makeText(this, "등록 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        }else{
                                Toast.makeText(this, "등록 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
            }
        }
    }

    private fun showSingleImage(uri: Uri) {
        mImageUri = uri
        Glide.with(this).load(mImageUri).into(binding.ivReview)
    }


    companion object {
        const val ID = "id"
        const val LAT = "latitude"
        const val LNG = "longitude"
        const val ADR = "address"
    }

}