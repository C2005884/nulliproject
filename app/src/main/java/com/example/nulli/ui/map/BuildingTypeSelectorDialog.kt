package com.example.nulli.ui.map

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.nulli.databinding.DialogBuildingTypeSelectorBinding

class BuildingTypeSelectorDialog : DialogFragment() {

    private lateinit var binding:DialogBuildingTypeSelectorBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogBuildingTypeSelectorBinding.inflate(layoutInflater)
    }
}