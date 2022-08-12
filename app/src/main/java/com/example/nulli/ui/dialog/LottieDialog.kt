package com.example.nulli.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.nulli.databinding.DialogBuildingTypeSelectorBinding
import com.example.nulli.databinding.DialogLottieBinding
import com.example.nulli.model.Building

class LottieDialog : DialogFragment() {

    private lateinit var binding:DialogLottieBinding
    var lottieRes:Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogLottieBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lav.setAnimation(lottieRes)
        binding.lav.playAnimation()

    }


}

