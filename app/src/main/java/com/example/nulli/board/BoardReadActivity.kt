package com.example.nulli.board

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nulli.R
import com.example.nulli.databinding.ActivityBoardReadBinding
import com.example.nulli.model.Content
import com.example.nulli.model.Reply
import com.example.nulli.model.UserData
import com.example.nulli.util.WrapContentLinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
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

    var isLike = false
    var isScrap = false


    val replyListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            try {
                val data = snapshot.getValue(Reply::class.java)!!
                data.mine = (data.uid == user.uid)
                (binding.rvReply.adapter as ReplyAdapter).addData(data)
            } catch (e:Exception) {

            }

        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {

        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }

    override fun onResume() {
        super.onResume()
        db.child(boardId).child(id).child("replyMap").addChildEventListener(replyListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

//        Toast.makeText(this, "$id $from", Toast.LENGTH_SHORT).show()

        loadUser()
        loadData()
        setRV()

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

        binding.btnWriteReply.setOnClickListener {
            writeReply()
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

    private fun setRV() {

        binding.rvReply.apply {
            layoutManager = WrapContentLinearLayoutManager(this@BoardReadActivity)
            adapter = ReplyAdapter()
        }
    }

    private fun writeReply() {
        if(binding.etReply.text.toString().isBlank()) {
            Toast.makeText(this, "댓글을 입력해 주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val replyId = db.child("reply").push().key!!

        val reply = Reply (
                    id = replyId,
                    contentId = id,
                    boardId = boardId,
                    uid = user.uid,
                    profileImageUri = user.profileImageUri,
                    nickname = user.nickname,
                    content = binding.etReply.text.toString(),
                    date = System.currentTimeMillis(),
                    dateText = SimpleDateFormat("yyyy_MMdd HH:mm:ss").format(System.currentTimeMillis()),
                )

        binding.etReply.setText("")
        db.child("reply").child(replyId).setValue(reply).addOnCompleteListener {
            db.child(boardId).child(id).child("replyMap").child(replyId).setValue(reply)
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
            if (entry.key == id) {
                isScrap = true
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
            if (map.key == fuser?.uid) {
                isLike = true
                binding.ivLike.setImageResource(
                    if (isLike) R.drawable.like else R.drawable.like2
                )
                break
            }
        }

        val entries = ArrayList(content.replyMap.entries)
        entries.sortBy {it.value.date}
        val replyDatas : ArrayList<Reply> = arrayListOf()

        for(entry in entries) {
            replyDatas.add(entry.value)
        }

        (binding.rvReply.adapter as ReplyAdapter).setDatas(replyDatas)
    }

    override fun onPause() {
        super.onPause()

        db.child(boardId).child(id).child("replyMap")
            .removeEventListener(replyListener)

        if (isLike) {
                db.child(boardId).child(id).child("likeMap").child(user.uid!!).setValue(System.currentTimeMillis())
        } else {
                db.child(boardId).child(id).child("likeMap").child(user.uid!!).setValue(null)
        }


        if (isScrap) {

                db.child("user").child(user.uid!!).child("scrapMap").child(id).setValue(
                    System.currentTimeMillis()
                )

        } else {

            db.child("user").child(user.uid!!).child("scrapMap").child(user.uid!!).setValue(
                null
            )

        }

    }

    companion object {
        const val CONTENT_ID = "CONTENT_ID"
    }
}