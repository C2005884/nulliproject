package com.example.nulli.ui.map

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.nulli.databinding.ItemReviewBinding
import com.example.nulli.model.Review
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    val datas:ArrayList<Review> = arrayListOf()
    fun setDatas(arrayList: ArrayList<Review>){
        datas.clear()
        datas.addAll(arrayList)
        //안좋은 방식이다 최후의 방법으로 쓰라고 나온다
        //notifyDataSetChanged()
        notifyItemRangeInserted(0,datas.size)

    }

    fun addData(data : Review){
        datas.add(0,data)
        notifyItemInserted(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            Glide.with(binding.root).load(review.imageUrl).into(binding.ivReview)
            binding.tvReview.text = review.content
            Glide.with(binding.root).load(review.profileImageUri).into(binding.ivProfile)
            binding.tvNickname.text = review.nickname
            binding.tvDate.text = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN).format(review.date)
        }
    }
}