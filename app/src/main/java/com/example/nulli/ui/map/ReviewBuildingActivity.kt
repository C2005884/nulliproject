package com.example.nulli.ui.map

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.nulli.R
import com.example.nulli.databinding.ActivityReviewBuildingBinding
import com.example.nulli.databinding.ActivityWriteObstacleBinding
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

    private var mBuilding: Review = Review()
    private var mId: String = ""
    private var mlatitude:Double = 0.0
    private var mlongitude:Double = 0.0
    private lateinit var maddress:String
    private var mtype: Int = -1
    private var mImageUri: Uri? = null

    private val binding: ActivityReviewBuildingBinding by lazy {
        ActivityReviewBuildingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.llType.setOnClickListener {
            val dialog = BuildingTypeSelectorDialog().apply {
                clickEvent = {
                    when(it) {
                        1 -> {
                            Glide.with(this@ReviewBuildingActivity).load(R.drawable.hospital).into(binding.ivType)
                            binding.tvType.text = "병원"
                        }
                        2 -> {
                            Glide.with(this@ReviewBuildingActivity).load(R.drawable.drug).into(binding.ivType)
                            binding.tvType.text = "약국"
                        }
                        3 -> {
                            Glide.with(this@ReviewBuildingActivity).load(R.drawable.rehabilitation).into(binding.ivType)
                            binding.tvType.text = "재활"
                        }
                        4 -> {
                            Glide.with(this@ReviewBuildingActivity).load(R.drawable.vrious).into(binding.ivType)
                            binding.tvType.text = "기타"
                        }
                    }
                    //Toast.makeText(this@WriteBuildingActivity,it.toString(),Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show(supportFragmentManager, null)
        }
        mId = intent.getStringExtra(ID)?: ""

        if(!mId.isBlank()) {
            initView()
        }else {
            mlatitude = intent.getDoubleExtra(LAT, 0.0)
            mlongitude = intent.getDoubleExtra(LNG, 0.0)
            maddress = intent.getStringExtra(ADR)?:""
            binding.tvAddress.text = maddress
        }

        binding.ivReview.setOnClickListener {
            TedImagePicker.with(this)
                .start { uri -> showSingleImage(uri) }
        }
        binding.tvApply.setOnClickListener {
            makeBuilding()

        }
    }


    private fun initView() {
        db.child("building").child(mId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val building = snapshot.getValue(Review::class.java)
                if (building != null) {
                    mBuilding = building
                    Glide.with(this@ReviewBuildingActivity).load(mBuilding.imageUri).into(binding.ivReview)

                    when((mBuilding.type ?: "-1").toInt()) {
                        Review.HOSPITAL ->  { Glide.with(this@ReviewBuildingActivity).load(R.drawable.hospital).into(binding.ivType)
                            binding.tvType.text = "병원"
                        }
                        Review.DRUG ->  { Glide.with(this@ReviewBuildingActivity).load(R.drawable.drug).into(binding.ivType)
                            binding.tvType.text = "약국"
                        }
                        Review.REHABILITATION ->  { Glide.with(this@ReviewBuildingActivity).load(R.drawable.rehabilitation).into(binding.ivType)
                            binding.tvType.text = "재활센터"
                        }
                        else ->  { Glide.with(this@ReviewBuildingActivity).load(R.drawable.vrious).into(binding.ivType)
                            binding.tvType.text = "기타"
                        }
                    }

                    binding.tvAddress.text = mBuilding.address
                    binding.etContent.setText(mBuilding.content)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    private fun makeBuilding() {
        if(mImageUri == null) {
            Toast.makeText(this, "이미지를 선택해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val ref = db.child("building")
        var key = ""
        val obstacle = if(mId.isBlank()) {
            key = ref.push().key!!
            Obstacle().apply {
                id = key
                latitude = mlatitude.toString()
                longitude = mlongitude.toString()
                address = maddress
                type = mtype.toString()
                uid = fUser?.uid.toString()
                content = binding.etContent.text.toString()
                nickname = fUser?.displayName.toString()
                date = System.currentTimeMillis()
                dateText = SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.KOREAN).format(Date())
                imageUri = """https://firebasestorage.googleapis.com/v0/b/nulli-e491a.appspot.com/o/obstacle%2F$key?alt=media"""
            }
        } else {

            key = mId
            Obstacle().apply {
                id = key
                latitude = mBuilding.latitude.toString()
                longitude = mBuilding.longitude.toString()
                address = mBuilding.address
                type = mBuilding.type.toString()
                uid = fUser?.uid.toString()
                content = binding.etContent.text.toString()
                nickname = fUser?.displayName.toString()
                date = System.currentTimeMillis()
                dateText = SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.KOREAN).format(Date())
                imageUri =
                    """https://firebasestorage.googleapis.com/v0/b/nulli-e491a.appspot.com/o/obstacle%2F$key?alt=media"""
            }
        }

        storage.child("building").child(key).putFile(mImageUri!!).addOnCompleteListener {
            ref.child(key).setValue(obstacle).addOnCompleteListener {
                Toast.makeText(this, "등록 완료되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun showSingleImage(uri: Uri) {

        mImageUri = uri
        mtype = Random().nextInt(4)

        Glide.with(this).load(mImageUri).into(binding.ivReview)
        when(mtype) {
            Review.HOSPITAL -> {
                binding.tvType.text = "병원"
                Glide.with(this).load(R.drawable.hospital).into(binding.ivType)
            }
            Review.DRUG -> {
                binding.tvType.text = "약국"
                Glide.with(this).load(R.drawable.drug).into(binding.ivType)
            }
            Review.REHABILITATION -> {
                binding.tvType.text = "재활센터"
                Glide.with(this).load(R.drawable.rehabilitation).into(binding.ivType)
            }
            Review.VRIOUS -> {
                binding.tvType.text = "기타"
                Glide.with(this).load(R.drawable.vrious).into(binding.ivType)
            }
        }
    }
    companion object {
        const val ID = "id"
        const val LAT = "latitude"
        const val LNG = "longitude"
        const val ADR = "address"
    }
}