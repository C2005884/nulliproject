package com.example.nulli.ui.map

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nulli.R
import com.example.nulli.api.CallApi
import com.example.nulli.api.geocode.GeocodeResponse
import com.example.nulli.api.search.SearchResponse
import com.example.nulli.databinding.FragmentMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource

class MyMapFragment : Fragment() , OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private var isKeyboardOn = false
    private var isFabOpen = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)
        setSearch()
        setFABClickEvent()
    }

    private fun setSearch() {
        setRv()
        setKeyboard()
        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            search(textView.text.toString())
            val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(textView.windowToken, 0)
            textView.clearFocus()
            false
        }
    }

    private fun setKeyboard() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rec = Rect()
            binding.root.getWindowVisibleDisplayFrame(rec)

            //finding screen height
            val screenHeight = binding.root.rootView.height

            //finding keyboard height
            val keypadHeight = screenHeight - rec.bottom
            isKeyboardOn = keypadHeight <= screenHeight * 0.15
            binding.clPanel.isVisible = isKeyboardOn
        }
    }

    private fun setRv() {
        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SearchAdapter().apply {
                itemClick = {
                    CallApi().getLatLng(it){
                        //Toast.makeText(this@SearchActivity, "${it?.x} ${it?.y}", Toast.LENGTH_LONG).show()

                        //Toast.makeText(this@SearchActivity, it.toString(), Toast.LENGTH_SHORT).show()
                        jumpMarker(it)

                    }
                }
            }
        }
    }

    private fun jumpMarker(it: GeocodeResponse.Addresse?) {
        if(it?.x==null && it?.y==null) {
            Toast.makeText(requireContext(), "해당 위치로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(it.y.toDouble(), it.x.toDouble()))
            .animate(CameraAnimation.Fly, 1000)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun search(text: String) {
        CallApi().search(text) {
            (binding.rvSearch.adapter as SearchAdapter).setDatas(ArrayList(it))
            for(item in ArrayList(it)) {
                CallApi().getLatLng(item.roadAddress) {
                    makeMarker(item, it)
                }
            }
        }

    }

    private fun makeMarker(item: SearchResponse.Item, it: GeocodeResponse.Addresse?) {
        if(it?.x==null && it?.y==null) {
            Toast.makeText(requireContext(), "해당 위치로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val marker = Marker()
        marker.position = LatLng(it.y.toDouble(), it.x.toDouble())
        marker.captionText = item.title
        marker.captionColor = Color.BLUE
        marker.map = naverMap

        marker.setOnClickListener {
            Toast.makeText(requireContext(), "${item.title}", Toast.LENGTH_SHORT).show()
            false
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        naverMap.setOnMapClickListener { pointF, latLng ->

            if(isKeyboardOn) {
                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
            }
            false
        }
        setMapUiSettings()

    }

    private fun setMapUiSettings() {
        val uiSettings = naverMap.uiSettings
        uiSettings.isScaleBarEnabled = true
        uiSettings.isIndoorLevelPickerEnabled = true
        uiSettings.isLocationButtonEnabled = true
        NaverMapOptions().locationButtonEnabled(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
    private fun setFABClickEvent() {
        // 플로팅 버튼 클릭시 애니메이션 동작 기능
        binding.fabMain.setOnClickListener {
            toggleFab()
        }

        // 플로팅 버튼 클릭 이벤트 - 캡처
        binding.fabMap.setOnClickListener {
            Toast.makeText(this.context, "지도", Toast.LENGTH_SHORT).show()
        }

        // 플로팅 버튼 클릭 이벤트 - 공유
       binding.fabObstacleMap.setOnClickListener {
            Toast.makeText(this.context, "장애물지도", Toast.LENGTH_SHORT).show()
        }
    }
    private fun toggleFab(){
        // 플로팅 액션 버튼 닫기 - 열려있는 플로팅 버튼 집어넣는 애니메이션
        if (isFabOpen) {
            ObjectAnimator.ofFloat(binding.fabMap, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabObstacleMap, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabMain, View.ROTATION, 45f, 0f).apply { start() }
        } else { // 플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션
            ObjectAnimator.ofFloat(binding.fabObstacleMap, "translationY", -360f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabMap, "translationY", -180f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabMain, View.ROTATION, 0f, 45f).apply { start() }
        }

        isFabOpen = !isFabOpen

    }
}
