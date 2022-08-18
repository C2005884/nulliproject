package com.example.nulli.board

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
import com.example.nulli.databinding.ActivityBoardListBinding
import com.example.nulli.databinding.ActivityWriteBuildingBinding
import com.example.nulli.model.Board
import com.example.nulli.model.Building
import com.example.nulli.model.Obstacle
import com.example.nulli.model.Review

import com.example.nulli.ui.map.ReviewBuildingActivity
import com.example.nulli.ui.map.WriteBuildingActivity
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

class BoardListActivity : AppCompatActivity() {

    private val storage = Firebase.storage.reference
    private val db = Firebase.database.reference
    private val auth = Firebase.auth
    private val fUser = auth.currentUser
    private var mBoard = Board()
    private var mid:String = ""
    private lateinit var maddress:String
    private var mtype: Int = -1
    private var mImageUri:Uri? = null

    private val binding:ActivityBoardListBinding by lazy {
        ActivityBoardListBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mid = intent.getStringExtra(ID) ?: ""

        setRv()
        setData()

//setmessage 글씨작게하려면 62번줄
        binding.btnWrite.setOnClickListener {
            val intent = Intent(this, BoardWriteActivity::class.java)
            startActivity(intent)
        }
    }


    private fun loadData() {
        db.child("board").child(mid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mBoard = snapshot.getValue(Board::class.java)!!
                setData()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setData(){
        val datas:ArrayList<Board> = arrayListOf()
        //Pair<String, Long>
        var boardKeyList = mBoard.mboard.toList()
        boardKeyList = boardKeyList.sortedBy { it.second }

        for(board in boardKeyList) {
            Log.e("setData", "${board}")
            db.child("board").child(board.first)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val board = snapshot.getValue(Board::class.java)
                        Log.e("board","${board.toString()}")

                        if (board != null) {
                            if (!datas.contains(board)) {
                                datas.add(board)
                                (binding.rvContent.adapter as BoardAdapter).addData(board)
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
//                content = "국회의 정기회는 법률이 정하는 바에 의하여 매년 1회 집회되며, 국회의 임시회는 대통령 또는 국회재적의원 4분의 1 이상의 요구에 의하여 집회된다. 일반사면을 명하려면 국회의 동의를 얻어야 한다.".substring(0, Random().nextInt(100)),
//                date = 1660043301000,
//                dateText = "2022년 8월 9일 화요일 오후 8:08:21 GMT+09:00",
//                imageUri = "",
//                nickname = "닉네임은여덟자리",
//                profileImageUri = "https://picsum.photos/id/${Random().nextInt(500)}/200/300",
//                uid = ""
//            ),
//            Review(
//                id = "",
//                buildingId = "",
//                content = "국회의 정기회는 법률이 정하는 바에 의하여 매년 1회 집회되며, 국회의 임시회는 대통령 또는 국회재적의원 4분의 1 이상의 요구에 의하여 집회된다. 일반사면을 명하려면 국회의 동의를 얻어야 한다.".substring(0, Random().nextInt(100)),
//                date = 1660043301000,
//                dateText = "2022년 8월 9일 화요일 오후 8:08:21 GMT+09:00",
//                imageUri = "https://picsum.photos/id/${Random().nextInt(500)}/200/300",
//                nickname = "닉네임은여덟자리",
//                profileImageUri = "https://picsum.photos/id/${Random().nextInt(500)}/200/300",
//                uid = ""
//            )
        (binding.rvContent.adapter as BoardAdapter).setDatas(datas)
    }

    private fun setRv() {
        binding.rvContent.apply{
            layoutManager = WrapContentLinearLayoutManager(this@BoardListActivity)
            adapter = BoardAdapter()
        }
    }


    companion object {
        const val ID = "ID"
    }
}