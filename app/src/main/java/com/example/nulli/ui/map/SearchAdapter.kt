package com.example.nulli.ui.map

import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.example.nulli.api.search.SearchResponse
import com.example.nulli.databinding.ItemSearchBinding

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val datas: ArrayList<SearchResponse.Item> = arrayListOf()
    var itemClick : (String) -> Unit = {}

    fun setDatas(arrayList: ArrayList<SearchResponse.Item>) {
        datas.clear()
        datas.addAll(arrayList)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int {
        //모든 데이터들을 이게 넣어야지만 된다
        return datas.size
    }

    inner class ViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SearchResponse.Item) {
            binding.root.setOnClickListener {
                Log.e("click", data.roadAddress)
                itemClick(data.roadAddress)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.tvTitle.text = Html.fromHtml(data.title, Html.FROM_HTML_MODE_LEGACY)
            } else {
                binding.tvTitle.text = Html.fromHtml(data.title)
            }
            binding.tvAddress.text = data.roadAddress

        }
    }
}


