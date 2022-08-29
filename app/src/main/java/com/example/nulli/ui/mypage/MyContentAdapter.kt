package com.example.nulli.ui.mypage

import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nulli.board.BoardListActivity
import com.example.nulli.databinding.ItemRecentContentBinding
import com.example.nulli.model.ContentSummary

class MyContentAdapter : RecyclerView.Adapter<MyContentAdapter.ViewHolder>() {

    var clickEvent: (data: ContentSummary) -> Unit = { }

    val datas: ArrayList<ContentSummary> = arrayListOf()
    fun setDatas(arrayList: ArrayList<ContentSummary>) {
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
        fun bind(data: ContentSummary) {
            binding.tvBoardName.text = when(data.boardId){
                BoardListActivity.FREE_BOARD -> BoardListActivity.FREE_BOARD_KOREAN
                BoardListActivity.EXTERNAL_DISABLED_BOARD -> BoardListActivity.EXTERNAL_DISABLED_BOARD_KOREAN
                BoardListActivity.INTERNAL_DISABLED_BOARD -> BoardListActivity.INTERNAL_DISABLED_BOARD_KOREAN
                BoardListActivity.DEVELOP_DISABLED_BOARD -> BoardListActivity.DEVELOP_DISABLED_BOARD_KOREAN
                BoardListActivity.MENTALITY_DISABLED_BOARD -> BoardListActivity.MENTALITY_DISABLED_BOARD_KOREAN
                else -> ""
            }
            binding.tvRecentContent.text = data.title
            binding.root.setOnClickListener {
                clickEvent(data)
            }
        }
    }
}