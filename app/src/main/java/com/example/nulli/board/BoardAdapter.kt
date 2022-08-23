package com.example.nulli.board

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nulli.databinding.ItemBoardBinding
import com.example.nulli.model.Board
import java.text.SimpleDateFormat
import java.util.*

class BoardAdapter : RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

    val datas:ArrayList<Board> = arrayListOf()
    fun setDatas(arrayList: ArrayList<Board>){
        datas.clear()
        datas.addAll(arrayList)
        //안좋은 방식이다 최후의 방법으로 쓰라고 나온다
        //notifyDataSetChanged()
        notifyItemRangeInserted(0,datas.size)

    }

    fun addData(data : Board){
        datas.add(0,data)
        notifyItemInserted(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBoardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    // PascalCase
    // camelCase
    // snake_case 단어 바뀔때마다 _ 추가

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.clear()
    }

    inner class ViewHolder(private val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(board: Board) {
            binding.cv.isVisible = !board.imageUri.isNullOrBlank()
            Glide.with(binding.root).load(board.imageUri).into(binding.ivReview)
            binding.ivProfile.isVisible = !board.imageUri.isNullOrBlank()
            binding.tvTitle.text = board.title
            binding.tvContent.text = board.content
            Glide.with(binding.root).load(board.profileImageUri).into(binding.ivProfile)
            binding.tvNickname.text = board.nickname
            binding.tvDate.text = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN).format(board.date)
        }

        fun clear() {
            Glide.with(binding.root).clear(binding.ivProfile)
            Glide.with(binding.root).clear(binding.ivProfile)
        }
    }
}