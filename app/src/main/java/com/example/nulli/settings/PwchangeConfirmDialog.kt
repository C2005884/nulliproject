package com.example.nulli.settings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.nulli.databinding.DialogPwchangeConfirmBinding

class PwchangeConfirmDialog : DialogFragment() {
    private lateinit var binding:DialogPwchangeConfirmBinding
    // var clickEvent:(Int) -> {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPwchangeConfirmBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnConfirm.setOnClickListener {
            dismiss() // 확인(btnConfirm) 버튼 누르면 다이얼로그 화면 사라짐
        }
    }
}