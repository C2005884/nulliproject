package com.example.nulli.ui.map

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.nulli.R
import com.example.nulli.databinding.ActivityWriteBuildingBinding
import com.example.nulli.databinding.ActivityWriteObstacleBinding
import com.example.nulli.model.Obstacle
import com.example.nulli.util.ImageAnalysis
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import gun0912.tedimagepicker.builder.TedImagePicker
import java.text.SimpleDateFormat
import java.util.*

class WriteBuildingActivity : AppCompatActivity() {

    private val storage = Firebase.storage.reference
    private val db = Firebase.database.reference
    private val auth = Firebase.auth
    private val fUser = auth.currentUser

    private var mlatitude:Double = 0.0
    private var mlongitude:Double = 0.0
    private lateinit var maddress:String
    private var mtype: Int = -1
    private var mImageUri:Uri? = null

    private val binding:ActivityWriteBuildingBinding by lazy {
        ActivityWriteBuildingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mlatitude = intent.getDoubleExtra(LAT, 0.0)
        mlongitude = intent.getDoubleExtra(LNG, 0.0)
        maddress = intent.getStringExtra(ADR)?:""

        binding.tvAddress.text = maddress

        binding.ivObstacle.setOnClickListener {
            TedImagePicker.with(this)
                .start { uri -> showSingleImage(uri) }
        }
        binding.tvApply.setOnClickListener {
            makeObstacle()

        }
    }


    private fun makeObstacle() {
        if(mImageUri == null) {
            Toast.makeText(this, "이미지를 선택해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val ref = db.child("obstacle")

        // 유일함 보장 키
        val key = ref.push().key!!

        val obstacle = Obstacle().apply {
       id = key
       latitude = mlatitude.toString()
       longitude = mlongitude.toString()
       address = maddress
       type = mtype.toString()
       uid = fUser?.uid.toString()
       nickname = fUser?.displayName.toString()
       date = System.currentTimeMillis()
       dateText = SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.KOREAN).format(Date())
       imageUri = """https://firebasestorage.googleapis.com/v0/b/nulli-e491a.appspot.com/o/obstacle%2F$key?alt=media"""
        }

        storage.child("obstacle").child(key).putFile(mImageUri!!).addOnCompleteListener {
            ref.child(key).setValue(obstacle).addOnCompleteListener {
                Toast.makeText(this, "등록 완료되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun showSingleImage(uri: Uri) {

        mImageUri = uri
        mtype = Random().nextInt(4)

        Glide.with(this).load(mImageUri).into(binding.ivObstacle)
        when(mtype) {
            Obstacle.BLOCK -> {
                binding.tvType.text = "턱"
                Glide.with(this).load(R.drawable.block).into(binding.ivType)
            }
            Obstacle.SLOPE -> {
                binding.tvType.text = "경사"
                Glide.with(this).load(R.drawable.slope).into(binding.ivType)
            }
            Obstacle.STAIR -> {
                binding.tvType.text = "계단"
                Glide.with(this).load(R.drawable.stairs).into(binding.ivType)
            }
            Obstacle.OTHER -> {
                binding.tvType.text = "기타"
                Glide.with(this).load(R.drawable.warning).into(binding.ivType)
            }
        }

//        ImageAnalysis(this).apply {
//            classifyImage(uri.toString()) { classified, confidence ->
//                Toast.makeText(this@WriteObstacleActivity, "$classified $confidence", Toast.LENGTH_SHORT).show()
//                mtype = when(classified) {
//                    "경사" -> Obstacle.SLOPE
//                    "계단" -> Obstacle.STAIR
//                    "기타" -> Obstacle.OTHER
//                    "턱" -> Obstacle.BLOCK
//                    else -> Obstacle.OTHER
//                }
//            }
//        }
    }
    companion object {
        const val LAT = "latitude"
        const val LNG = "longitude"
        const val ADR = "address"
    }
}