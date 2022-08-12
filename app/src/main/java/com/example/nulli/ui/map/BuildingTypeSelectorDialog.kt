package com.example.nulli.ui.map

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.nulli.databinding.DialogBuildingTypeSelectorBinding
import com.example.nulli.model.Building

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
            clickEvent(Building.HOSPITAL)
            dismiss()
        }
        binding.llType2.setOnClickListener {
            clickEvent(Building.DRUG)
            dismiss()
        }
        binding.llType3.setOnClickListener {
            clickEvent(Building.REHABILITATION)
            dismiss()
        }
        binding.llType4.setOnClickListener {
            clickEvent(Building.VRIOUS)
            dismiss()
        }
    }


}