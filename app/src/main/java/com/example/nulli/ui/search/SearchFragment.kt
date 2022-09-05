package com.example.nulli.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nulli.board.BoardAdapter
import com.example.nulli.board.BoardListActivity
import com.example.nulli.board.BoardReadActivity
import com.example.nulli.databinding.FragmentSearchBinding
import com.example.nulli.model.Content
import com.example.nulli.util.SharedPreferencesManager
import com.example.nulli.util.WrapContentLinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment() {

    private val db = Firebase.database.reference

    private val pref: SharedPreferencesManager by lazy {
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
            // TODO: textview.text 가 2글자 이상인지 판별
            if(!pref.searchHistoryList.contains(textView.text.toString())) {
                pref.addSearchHistoryList(textView.text.toString())
            }
            (binding.rvHistory.adapter as SearchHistoryAdapter).addData(textView.text.toString())
            setData(textView.text.toString())

            true
        }

        binding.tvRemoveAll.setOnClickListener {
            pref.searchHistoryList = "[]"
            (binding.rvHistory.adapter as SearchHistoryAdapter).removeAllData()
        }
    }

    private fun setData(target: String) {
        /**
         * const val FREE_BOARD = "freeBoard"
        const val EXTERNAL_DISABLED_BOARD = "externalDisabledBoard"
        const val INTERNAL_DISABLED_BOARD = "internalDisabledBoard"
        const val DEVELOP_DISABLED_BOARD = "developDisabledBoard"
        const val MENTALITY_DISABLED_BOARD = "mentalityDisabledBoard"
         */
        val arraylist: ArrayList<Content> = arrayListOf()
        var targetBoard = ""

        targetBoard = BoardListActivity.FREE_BOARD
        db.child(targetBoard)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (s in snapshot.children) {
                        try {
                            val data = s.getValue(Content::class.java)!!
                            if (data.title?.contains(target) == true ||
                                data.content?.contains(target) == true
                            ) {
                                arraylist.add(data)
                                (binding.rvSearch.adapter as BoardAdapter).addDataAtLast(data)

                                binding.rvSearch.visibility = View.VISIBLE
                                binding.tvText.visibility = View.GONE
                                binding.tvRemoveAll.visibility = View.GONE
                                binding.rvHistory.visibility = View.GONE
                            }
                        } catch (e: Exception) {

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        targetBoard = BoardListActivity.INTERNAL_DISABLED_BOARD
        db.child(BoardListActivity.INTERNAL_DISABLED_BOARD)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (s in snapshot.children) {
                        try {
                            val data = s.getValue(Content::class.java)!!
                            if (data.title?.contains(target) == true ||
                                data.content?.contains(target) == true
                            ) {
                                arraylist.add(data)
                                (binding.rvSearch.adapter as BoardAdapter).addDataAtLast(data)

                                binding.rvSearch.visibility = View.VISIBLE
                                binding.tvText.visibility = View.GONE
                                binding.tvRemoveAll.visibility = View.GONE
                                binding.rvHistory.visibility = View.GONE
                            }
                        } catch (e: Exception) {

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        targetBoard = BoardListActivity.EXTERNAL_DISABLED_BOARD
        db.child(BoardListActivity.EXTERNAL_DISABLED_BOARD)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (s in snapshot.children) {
                        try {
                            val data = s.getValue(Content::class.java)!!
                            if (data.title?.contains(target) == true ||
                                data.content?.contains(target) == true
                            ) {
                                arraylist.add(data)
                                (binding.rvSearch.adapter as BoardAdapter).addDataAtLast(data)

                                binding.rvSearch.visibility = View.VISIBLE
                                binding.tvText.visibility = View.GONE
                                binding.tvRemoveAll.visibility = View.GONE
                                binding.rvHistory.visibility = View.GONE
                            }
                        } catch (e: Exception) {

                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        targetBoard = BoardListActivity.DEVELOP_DISABLED_BOARD
        db.child(BoardListActivity.DEVELOP_DISABLED_BOARD)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (s in snapshot.children) {
                        try {
                            val data = s.getValue(Content::class.java)!!
                            if (data.title?.contains(target) == true ||
                                data.content?.contains(target) == true
                            ) {
                                arraylist.add(data)
                                (binding.rvSearch.adapter as BoardAdapter).addDataAtLast(data)

                                binding.rvSearch.visibility = View.VISIBLE
                                binding.tvText.visibility = View.GONE
                                binding.tvRemoveAll.visibility = View.GONE
                                binding.rvHistory.visibility = View.GONE
                            }
                        } catch (e: Exception) {

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        targetBoard = BoardListActivity.MENTALITY_DISABLED_BOARD
        db.child(BoardListActivity.MENTALITY_DISABLED_BOARD)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (s in snapshot.children) {
                        try {
                            val data = s.getValue(Content::class.java)!!
                            if (data.title?.contains(target) == true ||
                                data.content?.contains(target) == true
                            ) {
                                arraylist.add(data)
                                (binding.rvSearch.adapter as BoardAdapter).addDataAtLast(data)

                                binding.rvSearch.visibility = View.VISIBLE
                                binding.tvText.visibility = View.GONE
                                binding.tvRemoveAll.visibility = View.GONE
                                binding.rvHistory.visibility = View.GONE
                            }
                        } catch (e: Exception) {

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private fun setRv() {
        binding.rvSearch.apply {
            layoutManager = WrapContentLinearLayoutManager(requireContext())
            adapter = BoardAdapter().apply {
                itemClick = { s: String, s1: String ->
                    val intent = Intent(requireContext(), BoardReadActivity::class.java).apply { }
                    intent.putExtra(BoardListActivity.BOARD_ID, s)
                    intent.putExtra(BoardListActivity.ID, s1)
                    intent.putExtra(BoardListActivity.FROM, BoardListActivity.BOARD_LIST)
                    startActivity(intent)
                }
            }
        }

        binding.rvHistory.apply {
            layoutManager = WrapContentLinearLayoutManager(requireContext())
            adapter = SearchHistoryAdapter().apply {
                setDatas(pref.loadSearchHistoryList())
                stringClick = {
                    setData(it)
                    binding.etSearch.setText(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}