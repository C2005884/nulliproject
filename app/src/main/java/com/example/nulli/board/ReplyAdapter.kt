package com.example.nulli.board

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.nulli.databinding.ItemReplyBinding
import com.example.nulli.model.Reply
import java.text.SimpleDateFormat

class ReplyAdapter : RecyclerView.Adapter<ReplyAdapter.ViewHolder>() {

    var moreClick:(String?) -> Unit = { }
    val datas:ArrayList<Reply> = arrayListOf()
    fun setDatas(arrayList: ArrayList<Reply>) {
        datas.clear()
        datas.addAll(arrayList)
        notifyItemRangeInserted(0, datas.size)
    }
    fun addData(data:Reply) {
        datas.add(data)
        notifyItemInserted(datas.size-1) // 순서 오름차순 정렬
    }
    fun deleteData(data:Reply) {
        var targetPosition = -1
        for(i in datas.indices) {
            if(datas[i].id == data.id) {
                targetPosition = i
                break
            }
        }
        try {
            datas.remove(data)
            notifyItemRemoved(targetPosition)
        } catch (e:Exception) {

        }
    }

    fun deleteData(key:String) {
        var targetPosition = -1
        for(i in datas.indices) {
            if(datas[i].id == key) {
                targetPosition = i
                break
            }
        }
        try {
            datas.removeAt(targetPosition)
            notifyItemRemoved(targetPosition)
        } catch (e:Exception) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
    override fun getItemCount(): Int {
        return datas.size
    }

    inner class ViewHolder(private val binding: ItemReplyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reply: Reply) {
            Glide.with(binding.root).load(reply.profileImageUri).into(binding.ivProfile)
            binding.tvNickname.text = reply.nickname
            binding.tvDate.text = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(reply.date)
            binding.tvContent.text = reply.content
            binding.ivDelete.isVisible = reply.mine
            binding.ivDelete.setOnClickListener {
                moreClick(reply.id)
            }
        }
    }
}