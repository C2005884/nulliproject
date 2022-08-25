package com.example.nulli.board

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nulli.R
import com.example.nulli.databinding.ActivityBoardReadBinding
import com.example.nulli.model.Content
import com.example.nulli.model.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat

class BoardReadActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private val fuser = Firebase.auth.currentUser
    private val storage = Firebase.storage.reference
    private val db = Firebase.database.reference

    var user = UserData()
    var content = Content()

    val binding: ActivityBoardReadBinding by lazy {
        ActivityBoardReadBinding.inflate(layoutInflater)
    }

    val boardId: String by lazy {
        intent.getStringExtra(BoardListActivity.BOARD_ID) ?: ""
    }


    val id: String by lazy {
        intent.getStringExtra(BoardListActivity.ID) ?: ""
    }

    val from: String by lazy {
        intent.getStringExtra(BoardListActivity.FROM) ?: ""
    }

    var existLikeTimeStamp = ""
    var isLike = false
    var existScrapTimeStamp = ""
    var isScrap = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

//        Toast.makeText(this, "$id $from", Toast.LENGTH_SHORT).show()

        loadUser()
        loadData()

        binding.ivLike.setOnClickListener {
            isLike = !isLike
            binding.ivLike.setImageResource(
                if (isLike) R.drawable.like else R.drawable.like2
            )
        }

        binding.ivScrap.setOnClickListener {
            isScrap = !isScrap
            binding.ivScrap.setImageResource(
                if (isScrap) R.drawable.star2 else R.drawable.star
            )
        }

        binding.ivDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder
                .setMessage("게시물을 삭제하시겠습니까?")
                .setPositiveButton("삭제",
                    DialogInterface.OnClickListener { dialog, id ->
                        // 삭제 버튼 선택시 수행
                        deleteContent()
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener {dialog, id ->
                        // 취소 버튼 선택시 수행
                    })
            builder.create()
            builder.show()
        }
    }

    private fun deleteContent() {
        db.child(boardId).child(id).removeValue()
    }

    private fun loadUser() {
        db.child("user").child(fuser?.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(UserData::class.java)!!
                    initScrap()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun initScrap() {
        for (entry in user.scrapMap!!) {
            if (entry.value == id) {
                isScrap = true
                existScrapTimeStamp = entry.key.toString()
                binding.ivScrap.setImageResource(
                    if (isScrap) R.drawable.star2 else R.drawable.star
                )
                break
            }
        }
    }

    private fun loadData() {
        db.child(boardId).child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    content = snapshot.getValue(Content::class.java)!!
                    initView()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun initView() {
        Glide.with(this).load(content.profileImageUri).into(binding.ivProfile)
        binding.tvNickname.text = content.nickname
        binding.tvDate.text = SimpleDateFormat("MM/dd HH:mm").format(content.date)
        binding.tvContent.text = content.content
        Glide.with(this).load(content.imageUri).into(binding.ivContent)
        binding.tvLike.text = content.likeMap.count().toString()
        binding.tvReply.text = content.replyMap.count().toString()


        for (map in content.likeMap) {
            if (map.value == fuser?.uid) {
                isLike = true
                existLikeTimeStamp = map.key.toString()
                binding.ivLike.setImageResource(
                    if (isLike) R.drawable.like else R.drawable.like2
                )
                break
            }
        }
    }

    override fun onPause() {
        super.onPause()

        if (isLike) {
            if(existLikeTimeStamp.isBlank()) {
                db.child(boardId).child(id).child("likeMap").child(System.currentTimeMillis().toString()).setValue(user.uid)
            }
        } else {
            if(existLikeTimeStamp.isNotBlank()) {
                db.child(boardId).child(id).child("likeMap").child(existLikeTimeStamp).setValue(null)
            }
        }


        if (isScrap) {
            if(existScrapTimeStamp.isBlank()) {
                db.child("user").child(user.uid!!).child("scrapMap").child(System.currentTimeMillis().toString()).setValue(
                    id
                )
            }
        } else {
            if(existScrapTimeStamp.isNotBlank()) {
                db.child("user").child(user.uid!!).child("scrapMap").child(existScrapTimeStamp).setValue(null)
            }
        }

        db.child(boardId).child(id).child("likeMap").child(System.currentTimeMillis().toString())
            .setValue(
                if (isLike) user.uid else null
            )

        db.child("user").child(user.uid!!).child("scrapMap")
            .child(System.currentTimeMillis().toString()).setValue(
                if (isScrap) id else null
            )

    }

    companion object {
        const val CONTENT_ID = "CONTENT_ID"
    }
}