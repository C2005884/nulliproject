package com.example.nulli.board

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nulli.databinding.ItemBoardBinding
import com.example.nulli.model.Content
import java.text.SimpleDateFormat
import java.util.*

class BoardAdapter : RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

    var itemClick: (String) -> Unit = {}
    val datas: ArrayList<Content> = arrayListOf()
    fun setDatas(arrayList: ArrayList<Content>){
        datas.clear()
        datas.addAll(arrayList)
        //안좋은 방식이다 최후의 방법으로 쓰라고 나온다
        //notifyDataSetChanged()
        notifyItemRangeInserted(0,datas.size)

    }

    fun addData(data : Content){
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
        fun bind(content: Content) {
            binding.root.setOnClickListener {
                Log.e("viewholder", content.id)
                itemClick(content.id)
            }

            binding.cv.isVisible = !content.imageUri.isNullOrBlank()
            Glide.with(binding.root).load(content.imageUri).into(binding.ivReview)
            binding.ivProfile.isVisible = !content.imageUri.isNullOrBlank()
            binding.tvTitle.text = content.title
            binding.tvContent.text = content.content
            Glide.with(binding.root).load(content.profileImageUri).into(binding.ivProfile)
            binding.tvNickname.text = content.nickname
            binding.tvDate.text = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN).format(content.date)
            binding.tvLikeCount.text = content.likeMap.size.toString()
            binding.tvReplyCount.text = content.replyMap.size.toString()
        }

        fun clear() {
            Glide.with(binding.root).clear(binding.ivProfile)
            Glide.with(binding.root).clear(binding.ivProfile)
        }
    }
}