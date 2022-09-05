package com.example.nulli.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nulli.databinding.FragmentSearchBinding
import com.example.nulli.util.SharedPreferencesManager
import com.example.nulli.util.WrapContentLinearLayoutManager

class SearchFragment : Fragment() {
    private val pref:SharedPreferencesManager by lazy {
        SharedPreferencesManager(requireContext())
    }
    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRv()
        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            //textview.text 가 2글자 이상인지 판별
            pref.addSearchHistoryList(textView.text.toString())
            (binding.rvHistory.adapter as SearchHistoryAdapter).addData(textView.text.toString())
            textView.setText("")
//            Toast.makeText(requireContext(),pref.searchHistorySet.toString(),Toast.LENGTH_SHORT).show()
            false
        }
        binding.tvRemoveAll.setOnClickListener {
            pref.searchHistoryList = "[]"
            (binding.rvHistory.adapter as SearchHistoryAdapter).removeAllData()
        }
    }

    private fun setRv() {
        binding.rvHistory.apply {
            layoutManager = WrapContentLinearLayoutManager(requireContext())
            adapter = SearchHistoryAdapter().apply {
                setDatas(pref.loadSearchHistoryList())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}