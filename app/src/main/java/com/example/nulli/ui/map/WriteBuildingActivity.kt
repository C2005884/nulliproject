package com.example.nulli.ui.map

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.nulli.R
import com.example.nulli.databinding.ActivityWriteBuildingBinding
import com.example.nulli.model.Building
import com.example.nulli.model.Obstacle
import com.example.nulli.model.Review
import com.example.nulli.util.WrapContentLinearLayoutManager
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
import kotlin.collections.ArrayList

class WriteBuildingActivity : AppCompatActivity() {

    private val storage = Firebase.storage.reference
    private val db = Firebase.database.reference
    private val auth = Firebase.auth
    private val fUser = auth.currentUser
    private var mBuilding = Building()
    private var mid:String = ""
    private var mlatitude:Double = 0.0
    private var mlongitude:Double = 0.0
    private lateinit var maddress:String
    private var mtype: Int = -1
    private var mImageUri:Uri? = null

    private val binding:ActivityWriteBuildingBinding by lazy {
        ActivityWriteBuildingBinding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()
        if(mid.isNotBlank()) {
            binding.etTitle.isEnabled = false
            loadData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mid = intent.getStringExtra(ID) ?: ""
        mlatitude = intent.getDoubleExtra(LAT, 0.0)
        mlongitude = intent.getDoubleExtra(LNG, 0.0)
        maddress = intent.getStringExtra(ADR)?:""
        mtype = intent.getIntExtra(TYPE, 0)
        setType(mtype)
        supportActionBar?.hide()


        binding.tvAddress.text = maddress

        setRv()
        setData()
//setmessage ????????????????????? 62??????
        binding.btnWrite.setOnClickListener {
            if(mid.isBlank()){
                AlertDialog.Builder(this)
                    .setTitle("????????? ?????????????????????????")
                    .setPositiveButton("????????????"){ dialogInterface: DialogInterface, i: Int ->
                        makeBuilding()
                    }
                    .setNegativeButton("????????????"){ dialogInterface: DialogInterface, i: Int ->

                    }.show()
            }else {
                goToWriteReview()
            }
        }

        binding.ivBuilding.setOnClickListener {
            TedImagePicker.with(this).start {
                showSingleImage(it)
            }
        }
        binding.llType.setOnClickListener {
            val dialog = BuildingTypeSelectorDialog().apply {
                clickEvent = {
                    mtype = it
                    setType(mtype)
                    //Toast.makeText(this@WriteBuildingActivity,it.toString(),Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show(supportFragmentManager, null)
        }

//        binding.ivObstacle.setOnClickListener {
//            TedImagePicker.with(this)
//                .start { uri -> showSingleImage(uri) }
//        }
//        binding.tvApply.setOnClickListener {
//            makeObstacle()
//
//        }

    }

    private fun setType(type:Int) {
        when(type) {
            Building.HOSPITAL -> {
                Glide.with(this@WriteBuildingActivity).load(R.drawable.hospital).into(binding.ivType)
                binding.tvType.text = "??????"
            }
            Building.DRUG -> {
                Glide.with(this@WriteBuildingActivity).load(R.drawable.drug).into(binding.ivType)
                binding.tvType.text = "??????"
            }
            Building.REHABILITATION -> {
                Glide.with(this@WriteBuildingActivity).load(R.drawable.rehabilitation).into(binding.ivType)
                binding.tvType.text = "????????????"
            }
            Building.VRIOUS -> {
                Glide.with(this@WriteBuildingActivity).load(R.drawable.vrious).into(binding.ivType)
                binding.tvType.text = "??????"
            }
        }
    }

    private fun loadData() {
        db.child("building").child(mid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mBuilding = snapshot.getValue(Building::class.java)!!
                setData()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setData(){
        val datas:ArrayList<Review> = arrayListOf()
        //Pair<String, Long>
        var reviewKeyList = mBuilding.review.toList()
        reviewKeyList = reviewKeyList.sortedBy { it.second }

        for(review in reviewKeyList) {
            Log.e("setData", "${review}")
            db.child("review").child(review.first)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val review = snapshot.getValue(Review::class.java)
                        Log.e("review","${review.toString()}")
                        binding.etTitle.setText(mBuilding.title?: "")
                        if (review != null) {
                            if (!datas.contains(review)) {
                                datas.add(review)
                                (binding.rvReview.adapter as ReviewAdapter).addData(review)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }
//            Review(
//                id = "",
//                buildingId = "",
//                content = "????????? ???????????? ????????? ????????? ?????? ????????? ?????? 1??? ????????????, ????????? ???????????? ????????? ?????? ?????????????????? 4?????? 1 ????????? ????????? ????????? ????????????. ??????????????? ???????????? ????????? ????????? ????????? ??????.".substring(0, Random().nextInt(100)),
//                date = 1660043301000,
//                dateText = "2022??? 8??? 9??? ????????? ?????? 8:08:21 GMT+09:00",
//                imageUri = "",
//                nickname = "????????????????????????",
//                profileImageUri = "https://picsum.photos/id/${Random().nextInt(500)}/200/300",
//                uid = ""
//            ),
//            Review(
//                id = "",
//                buildingId = "",
//                content = "????????? ???????????? ????????? ????????? ?????? ????????? ?????? 1??? ????????????, ????????? ???????????? ????????? ?????? ?????????????????? 4?????? 1 ????????? ????????? ????????? ????????????. ??????????????? ???????????? ????????? ????????? ????????? ??????.".substring(0, Random().nextInt(100)),
//                date = 1660043301000,
//                dateText = "2022??? 8??? 9??? ????????? ?????? 8:08:21 GMT+09:00",
//                imageUri = "https://picsum.photos/id/${Random().nextInt(500)}/200/300",
//                nickname = "????????????????????????",
//                profileImageUri = "https://picsum.photos/id/${Random().nextInt(500)}/200/300",
//                uid = ""
//            )
        (binding.rvReview.adapter as ReviewAdapter).setDatas(datas)
    }

    private fun setRv() {
        binding.rvReview.apply{
            layoutManager = WrapContentLinearLayoutManager(this@WriteBuildingActivity)
            adapter = ReviewAdapter()
        }
    }


    private fun makeBuilding() {
        if(binding.etTitle.text.toString().isBlank()) {
            Toast.makeText(this, "????????? ????????? ??????????????????", Toast.LENGTH_SHORT).show()
            return
        }

        val ref = db.child("building")

        // ????????? ?????? ???
        val key = ref.push().key!!
        mid = key

        val building = Building().apply {
            id = key
            title = binding.etTitle.text.toString()
            latitude = mlatitude.toString()
            longitude = mlongitude.toString()
            address = maddress
            type = mtype.toString()
            uid = fUser?.uid.toString()
            nickname = fUser?.displayName.toString()
            date = System.currentTimeMillis()
            dateText = SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.KOREAN).format(Date())
            imageUri = """https://firebasestorage.googleapis.com/v0/b/nulli-e491a.appspot.com/o/building%2F$key?alt=media"""

        }

        storage.child("building").child(key).putFile(mImageUri!!).addOnCompleteListener {
            ref.child(key).setValue(building).addOnCompleteListener {
                Toast.makeText(this, "?????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                goToWriteReview()
            }
        }
    }

    private fun goToWriteReview() {
        val intent = Intent(this, ReviewBuildingActivity::class.java)
        intent.putExtra(ID,mid)
        intent.putExtra(ADR, maddress)
        intent.putExtra(TYPE, mtype)
        startActivity(intent)
    }

    private fun showSingleImage(uri: Uri) {

        mImageUri = uri
        Glide.with(this).load(uri).into(binding.ivBuilding)
    }

    companion object {
        const val ID = "ID"
        const val LAT = "latitude"
        const val LNG = "longitude"
        const val ADR = "address"
        const val TYPE = "type"

    }
}