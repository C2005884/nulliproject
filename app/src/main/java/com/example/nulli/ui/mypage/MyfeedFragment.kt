package com.example.nulli.ui.mypage

import android.R
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
import com.example.nulli.board.BoardListActivity
import com.example.nulli.board.BoardReadActivity
import com.example.nulli.databinding.FragmentMyfeedBinding
import com.example.nulli.model.ContentSummary
import com.example.nulli.model.UserData
import com.example.nulli.ui.home.RecentContentAdapter
import com.example.nulli.util.WrapContentLinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MyfeedFragment : Fragment() {

    private var _binding: FragmentMyfeedBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val auth = Firebase.auth
    val fuser = auth.currentUser
    val db = Firebase.database.reference

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

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun setData() {
        db.child("user").child(fuser?.uid!!).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserData::class.java)!!

                val myContentEntryList = ArrayList(user.myContentMap?.entries)
                myContentEntryList.sortByDescending { it.key }

                val myContentList:ArrayList<ContentSummary> = arrayListOf()
                for(i in 0 until 4){
                    try {
                        myContentList.add(myContentEntryList[i].value)
                    }catch (e: Exception) {

                    }

                }

                (binding.rvMyContent.adapter as MyContentAdapter).setDatas(myContentList)


                val scrapEntryList = ArrayList(user.scrapMap?.entries)
                scrapEntryList.sortByDescending { it.key }

                val scrapList:ArrayList<ContentSummary> = arrayListOf()
                for(i in 0 until 4){
                    try {
                        scrapList.add(scrapEntryList[i].value)
                    }catch (e: Exception) {

                    }

                }
                (binding.rvScrap.adapter as MyContentAdapter).setDatas(scrapList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyfeedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(binding.root).load(fuser?.photoUrl).into(binding.ivProfile)
        binding.tvNickname.text = fuser?.displayName

        binding.ivSettings.setOnClickListener {
            val intent = Intent(requireActivity(),MypageActivity::class.java)
            requireActivity().startActivity(intent)
        }

        setRv()
    }

    private fun setRv() {
        binding.rvMyContent.apply {
            layoutManager = WrapContentLinearLayoutManager(requireContext())
            adapter = MyContentAdapter().apply {
                clickEvent = {
                    requireActivity().startActivity(
                        Intent(
                            requireActivity(),
                            BoardReadActivity::class.java
                        ).apply {
                            putExtra(BoardListActivity.ID, it.contentId)
                            putExtra(BoardListActivity.BOARD_ID, it.boardId)
                            putExtra(BoardListActivity.FROM, BoardListActivity.BOARD_LIST)
                        })
                }
            }
        }
        binding.rvScrap.apply {
            layoutManager = WrapContentLinearLayoutManager(requireContext())
            adapter = MyContentAdapter().apply {
                clickEvent = {
                    requireActivity().startActivity(
                        Intent(
                            requireActivity(),
                            BoardReadActivity::class.java
                        ).apply {
                            putExtra(BoardListActivity.ID, it.contentId)
                            putExtra(BoardListActivity.BOARD_ID, it.boardId)
                            putExtra(BoardListActivity.FROM, BoardListActivity.BOARD_LIST)
                        })
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}