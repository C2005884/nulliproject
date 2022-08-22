package com.example.nulli.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nulli.board.BoardListActivity
import com.example.nulli.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private val db = Firebase.database.reference
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val dialog = LottieDialog().apply {
//            lottieRes = R.raw.loading
//        }.show(childFragmentManager,"lottie")
//
//        Handler(Looper.myLooper()!!).postDelayed({
//            childFragmentManager.findFragmentByTag("lottie")?.let {
//                (it as DialogFragment).dismiss()
//            }
//        }, 1000)

        setRv()
        loadRecentContent()
        loadFavoritContent()
    }

    private fun setRv() {
        binding.rvRecentContent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RecentContentAdapter().apply {
                clickEvent = { type, position ->
                    if (type == "board") {
                        val target = when (position) {
                            "0" -> BoardListActivity.FREE_BOARD
                            "1" -> BoardListActivity.EXTERNAL_DISABLED_BOARD
                            "2" -> BoardListActivity.INTERNAL_DISABLED_BOARD
                            "3" -> BoardListActivity.DEVELOP_DISABLED_BOARD
                            "4" -> BoardListActivity.MENTALITY_DISABLED_BOARD
                            else -> BoardListActivity.FREE_BOARD
                        }

                        requireActivity().startActivity(
                            Intent(
                                requireActivity(),
                                BoardListActivity::class.java
                            ).apply {
                                putExtra(BoardListActivity.ID, target)
                            })

                    } else if (type == "content") {

                    }
                }
            }
        }

        binding.rvContent.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadRecentContent() {
        db.child("recentContent").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val boardPair: ArrayList<Pair<String, String>> = arrayListOf(
                    Pair("자유 게시판", ""),
                    Pair("외부 장애 게시판", ""),
                    Pair("내부 장애 게시판", ""),
                    Pair("발달 장애 게시판", ""),
                    Pair("정신 장애 게시판", ""),
                )

                for (s in snapshot.children) {
                    Log.e("snapshot [${s.key}]", "${s.child("content").getValue(String::class.java)}")
                    when (s.key) {
                        BoardListActivity.FREE_BOARD -> {
                            boardPair[0] = Pair("자유 게시판", s.child("content").getValue(String::class.java).toString())
                        }
                        BoardListActivity.EXTERNAL_DISABLED_BOARD -> {
                            boardPair[1] = Pair("외부 장애 게시판", s.child("content").getValue(String::class.java).toString())
                        }
                        BoardListActivity.INTERNAL_DISABLED_BOARD -> {
                            boardPair[2] = Pair("내부 장애 게시판", s.child("content").getValue(String::class.java).toString())
                        }
                        BoardListActivity.DEVELOP_DISABLED_BOARD -> {
                            boardPair[3] = Pair("발달 장애 게시판", s.child("content").getValue(String::class.java).toString())
                        }
                        BoardListActivity.MENTALITY_DISABLED_BOARD -> {
                            boardPair[4] = Pair("정신 장애 게시판", s.child("content").getValue(String::class.java).toString())
                        }
                    }
                }

                (binding.rvRecentContent.adapter as RecentContentAdapter).setDatas(boardPair)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun loadFavoritContent() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}