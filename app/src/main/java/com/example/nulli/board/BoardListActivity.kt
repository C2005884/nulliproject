package com.example.nulli.board

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nulli.databinding.ActivityBoardListBinding
import com.example.nulli.model.Board
import com.example.nulli.util.WrapContentLinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardListActivity : AppCompatActivity() {

    private val storage = Firebase.storage.reference
    private val db = Firebase.database.reference
    private val auth = Firebase.auth
    private val fUser = auth.currentUser
    private var boardList: ArrayList<Board> = arrayListOf()
    private var mid: String = ""

    private val binding: ActivityBoardListBinding by lazy {
        ActivityBoardListBinding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mid = intent.getStringExtra(ID) ?: ""
        supportActionBar?.hide()

        val title = when(mid){
            FREE_BOARD -> "자유 게시판"
            EXTERNAL_DISABLED_BOARD -> "외부장애 게시판"
            INTERNAL_DISABLED_BOARD -> "내부장애 게시판"
            DEVELOP_DISABLED_BOARD -> "발달장애 게시판"
            MENTALITY_DISABLED_BOARD -> "정신장애 게시판"
            else -> "게시판"
        }
        binding.tvTitle.text = title

        setRv()
        setData()


//setmessage 글씨작게하려면 62번줄
        binding.btnWrite.setOnClickListener {
            val intent = Intent(this, BoardWriteActivity::class.java).apply {
                putExtra(ID, mid)
                putExtra(FROM, BOARD_LIST)
            }
            startActivity(intent)
        }
    }


    private fun loadData() {
        db.child(mid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (s in snapshot.children) {
                    try {
                        boardList.add(s.getValue(Board::class.java)!!)
                        Log.e("boardList[${boardList.size}]", boardList.toString())
                    } catch (e: Exception) {
                        Log.e("loadData", "${e.message}")
                    }
                }
                setData()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setData(){
        (binding.rvContent.adapter as BoardAdapter).setDatas(boardList)
    }

    private fun setRv() {
        binding.rvContent.apply{
            layoutManager = WrapContentLinearLayoutManager(this@BoardListActivity)
            adapter = BoardAdapter()
        }
    }


    companion object {
        const val ID = "ID"
        const val FROM = "FROM"

        const val BOARD_LIST = "boardList"
        const val HOME_FRAGMENT = "homeFragment"

        const val FREE_BOARD = "freeBoard"
        const val EXTERNAL_DISABLED_BOARD = "externalDisabledBoard"
        const val INTERNAL_DISABLED_BOARD = "internalDisabledBoard"
        const val DEVELOP_DISABLED_BOARD = "developDisabledBoard"
        const val MENTALITY_DISABLED_BOARD = "mentalityDisabledBoard"
    }
}