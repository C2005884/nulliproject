package com.example.nulli.ui.search

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.example.nulli.ui.search.SearchHistoryAdapter.ViewHolder
import android.view.ViewGroup
import com.example.nulli.databinding.ItemSearchHistoryBinding

class SearchHistoryAdapter : RecyclerView.Adapter<ViewHolder>() {

    val datas:ArrayList<String> = arrayListOf()
    fun setDatas(arrayList: ArrayList<String>) {
        datas.clear()
        datas.addAll(arrayList)
        notifyItemRangeInserted(0, datas.size)
    }

    fun removeAllData(){
        val size = datas.size
        datas.clear()
        notifyItemRangeRemoved(0,size)
    }

    fun removeData(str:String) {
        var targetIndex = -1
        for((index, value) in datas.withIndex()) {
            if(value==str) {
                targetIndex = index
                break
            }
        }
        try{
            datas.removeAt(targetIndex)
            notifyItemRemoved(targetIndex)
        } catch (e:Exception) {

        }
    }

    fun addData(str:String) {
        datas.add(str)
        notifyItemInserted(datas.size -1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
    override fun getItemCount(): Int {
        return datas.size
    }

    inner class ViewHolder(private val binding: ItemSearchHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(s: String) {
            binding.tvText.text = s
        }
    }
}