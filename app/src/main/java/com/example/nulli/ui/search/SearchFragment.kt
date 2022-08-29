package com.example.nulli.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
            pref.addSearchHistorySet(textView.text.toString())
            textView.setText("")
            Toast.makeText(requireContext(),pref.searchHistorySet.toString(),Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun setRv() {
        binding.rvSearch.apply {
            layoutManager = WrapContentLinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}