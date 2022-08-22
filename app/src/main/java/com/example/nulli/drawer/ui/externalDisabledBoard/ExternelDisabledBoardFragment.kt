package com.example.nulli.drawer.ui.externalDisabledBoard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nulli.databinding.FragmentExternelDisabledBoardBinding


class ExternelDisabledBoardFragment : Fragment() {

    private var _binding: FragmentExternelDisabledBoardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() =
        _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentExternelDisabledBoardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}