package com.example.nulli.ui.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.nulli.databinding.FragmentMypageBinding
import com.example.nulli.settings.SettingsImgChangeActivity
import com.example.nulli.settings.SettingsNickChangeActivity
import com.example.nulli.settings.SettingsWithdrawalActivity
import com.example.nulli.ui.SplashActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val auth = Firebase.auth
    val fuser = auth.currentUser
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    var b4ClickTime = 0L

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(System.currentTimeMillis() - b4ClickTime < 3000){
                    requireActivity().finish()
                }else{
                    b4ClickTime = System.currentTimeMillis()
                    Toast.makeText(requireContext(),"한번 더 누르시면 앱이 꺼집니다.",Toast.LENGTH_SHORT).show()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(binding.root).load(fuser?.photoUrl).into(binding.ivProfile)
        binding.tvNickname.text = fuser?.displayName
        binding.tvProfile.text = """
            ${fuser?.email}
            ${fuser?.displayName}
            ${fuser?.uid}
        """.trimMargin("|")

        binding.tvWithhdrawal.setOnClickListener {
            val intent = Intent(requireActivity(),SettingsWithdrawalActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding.tvNicnameChange.setOnClickListener {
            val intent = Intent(requireActivity(),SettingsNickChangeActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding.tvProfileImageChange.setOnClickListener {
            val intent = Intent(requireActivity(),SettingsImgChangeActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding.tvPasswordChange.setOnClickListener {
            val emailAddress = fuser?.email

            Toast.makeText(requireContext(), "이메일이 전송되었습니다. 이메일을 확인해주세요", Toast.LENGTH_LONG).show()

            Firebase.auth.sendPasswordResetEmail(emailAddress!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    }
                }
        }

        binding.tvLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireActivity(),SplashActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}