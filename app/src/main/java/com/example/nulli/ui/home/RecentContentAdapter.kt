package com.example.nulli.ui.home

import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nulli.databinding.ItemRecentContentBinding

class RecentContentAdapter : RecyclerView.Adapter<RecentContentAdapter.ViewHolder>() {

    var clickEvent: (type: String, key: String) -> Unit = { _, _ -> }

    val datas: ArrayList<Pair<String, String>> = arrayListOf()
    fun setDatas(arrayList: ArrayList<Pair<String, String>>) {
        datas.clear()
        datas.addAll(arrayList)
        notifyItemRangeInserted(0, datas.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecentContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
    override fun getItemCount(): Int {
        return datas.size
    }

    inner class ViewHolder(val binding: ItemRecentContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Pair<String, String>) {
            binding.tvBoardName.text = data.first
            binding.tvRecentContent.text =
                if (data.second.isBlank()) "최근 게시물이 없습니다" else data.second

            binding.tvBoardName.setOnClickListener {
                clickEvent("board", adapterPosition.toString())
            }
            binding.tvRecentContent.setOnClickListener {
                clickEvent("content", adapterPosition.toString())

            }
        }
    }
}