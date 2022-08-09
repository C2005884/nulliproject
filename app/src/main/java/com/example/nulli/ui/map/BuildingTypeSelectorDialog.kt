package com.example.nulli.ui.map

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.nulli.databinding.DialogBuildingTypeSelectorBinding

class BuildingTypeSelectorDialog : DialogFragment() {

    private lateinit var binding:DialogBuildingTypeSelectorBinding
    var clickEvent:(Int) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBuildingTypeSelectorBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.llType1.setOnClickListener {
            clickEvent(1)
            dismiss()
        }
        binding.llType2.setOnClickListener {
            clickEvent(2)
            dismiss()
        }
        binding.llType3.setOnClickListener {
            clickEvent(3)
            dismiss()
        }
        binding.llType4.setOnClickListener {
            clickEvent(4)
            dismiss()
        }
    }


}