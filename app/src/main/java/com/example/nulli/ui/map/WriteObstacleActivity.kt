package com.example.nulli.ui.map

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.nulli.R
import com.example.nulli.databinding.ActivityWriteObstacleBinding
import com.example.nulli.model.Obstacle
import com.example.nulli.util.ImageAnalysis
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

class WriteObstacleActivity : AppCompatActivity() {

    private val storage = Firebase.storage.reference
    private val db = Firebase.database.reference
    private val auth = Firebase.auth
    private val fUser = auth.currentUser

    private var mObstacle:Obstacle = Obstacle()
    private var mId: String = ""
    private var mlatitude:Double = 0.0
    private var mlongitude:Double = 0.0
    private lateinit var maddress:String
    private var mtype: Int = -1
    private var mImageUri:Uri? = null

    private val binding:ActivityWriteObstacleBinding by lazy {
        ActivityWriteObstacleBinding.inflate(layoutInflater)
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

        binding.llType.setOnClickListener {
            val dialog = ObstacleTypeSelectorDialog().apply {
                clickEvent = {
                    when(it) {
                        1 -> {
                            Glide.with(this@WriteObstacleActivity).load(R.drawable.block).into(binding.ivType)
                            binding.tvType.text = "턱"
                            mtype = Obstacle.SLOPE
                        }
                        2 -> {
                            Glide.with(this@WriteObstacleActivity).load(R.drawable.slope).into(binding.ivType)
                            binding.tvType.text = "경사"
                            mtype = Obstacle.SLOPE
                        }
                        3 -> {
                            Glide.with(this@WriteObstacleActivity).load(R.drawable.stairs).into(binding.ivType)
                            binding.tvType.text = "계단"
                            mtype = Obstacle.STAIR
                        }
                        4 -> {
                            Glide.with(this@WriteObstacleActivity).load(R.drawable.warning).into(binding.ivType)
                            binding.tvType.text = "기타"
                            mtype = Obstacle.OTHER
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

        binding.ivObstacle.setOnClickListener {
            TedImagePicker.with(this)
                .start { uri -> showSingleImage(uri) }
        }
        binding.tvApply.setOnClickListener {
            makeObstacle()

        }
    }


    private fun initView() {
        db.child("obstacle").child(mId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val obstacle = snapshot.getValue(Obstacle::class.java)
                if (obstacle != null) {
                    mObstacle = obstacle
                    Glide.with(this@WriteObstacleActivity).load(mObstacle.imageUri).into(binding.ivObstacle)

                    when((mObstacle.type ?: "-1").toInt()) {
                        Obstacle.BLOCK ->  { Glide.with(this@WriteObstacleActivity).load(R.drawable.block).into(binding.ivType)
                        binding.tvType.text = "턱"
                    }
                        Obstacle.SLOPE ->  { Glide.with(this@WriteObstacleActivity).load(R.drawable.slope).into(binding.ivType)
                            binding.tvType.text = "경사"
                        }
                        Obstacle.STAIR ->  { Glide.with(this@WriteObstacleActivity).load(R.drawable.stairs).into(binding.ivType)
                            binding.tvType.text = "계단"
                        }
                        else ->  { Glide.with(this@WriteObstacleActivity).load(R.drawable.warning).into(binding.ivType)
                            binding.tvType.text = "기타"
                        }
                }

                    binding.tvAddress.text = mObstacle.address
                    binding.etContent.setText(mObstacle.content)
            }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    private fun makeObstacle() {
        if(mImageUri == null) {
            Toast.makeText(this, "이미지를 선택해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val ref = db.child("obstacle")
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
                latitude = mObstacle.latitude.toString()
                longitude = mObstacle.longitude.toString()
                address = mObstacle.address
                type = mObstacle.type.toString()
                uid = fUser?.uid.toString()
                content = binding.etContent.text.toString()
                nickname = fUser?.displayName.toString()
                date = System.currentTimeMillis()
                dateText = SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.KOREAN).format(Date())
                imageUri =
                    """https://firebasestorage.googleapis.com/v0/b/nulli-e491a.appspot.com/o/obstacle%2F$key?alt=media"""
            }
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
        //mtype = Random().nextInt(4)

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
        const val ID = "id"
        const val LAT = "latitude"
        const val LNG = "longitude"
        const val ADR = "address"
    }
}