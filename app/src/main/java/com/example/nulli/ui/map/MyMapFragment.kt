package com.example.nulli.ui.map

import android.animation.ObjectAnimator
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nulli.R
import com.example.nulli.api.CallApi
import com.example.nulli.api.geocode.GeocodeResponse
import com.example.nulli.api.search.SearchResponse
import com.example.nulli.databinding.FragmentMapBinding
import com.example.nulli.model.Building
import com.example.nulli.model.Obstacle
import com.example.nulli.util.SkeyBoard
import com.example.nulli.util.nulliUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firestore.v1.Write

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource

class MyMapFragment : Fragment(), OnMapReadyCallback {


    private val db = Firebase.database.reference

    private var _binding: FragmentMapBinding? = null
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private var isKeyboardOn = false
    private var isFabOpen = false
    private var isEdit = false
    private var b4GeocodingTime = 0L
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private var isFirstObstacle = true
    private var isFirstBuilding = true

    private val markerList:ArrayList<Marker> = arrayListOf()
    private val obstacleList: ArrayList<Obstacle> = arrayListOf()
    private val buildingList: ArrayList<Building> = arrayListOf()

    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    private var currentAddress: String = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEdit) {
                    setEditMode(CLEAR)

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
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onResume() {
        super.onResume()
        loadObstacle()
        loadBuilding()
    }

    private fun loadObstacle() {
        db.child("obstacle").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (s in snapshot.children) {
                    val obstacle = s.getValue(Obstacle::class.java)
                    if (obstacle != null && !obstacleList.contains(obstacle)) {
                        obstacleList.add(obstacle)
                    }
                }
                Log.e("size: ", obstacleList.size.toString())
                setObstacleMarker()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun loadBuilding() {
        db.child("building").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (s in snapshot.children) {
                    val building = s.getValue(Building::class.java)
                    if (building != null && !buildingList.contains(building)) {
                        buildingList.add(building)
                    }
                }
                Log.e("size: ", buildingList.size.toString())
                setBuildingMarker()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
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
        setChipClickEvent()


    }

    private fun setChipClickEvent() {
        binding.chipReview.setOnClickListener {
            setBuildingMarker()
        }
        binding.chipObstacle.setOnClickListener {
            setObstacleMarker()
        }
    }

    private fun setSearch() {
        setRv()
        setKeyboard()
        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            search(textView.text.toString())
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(textView.windowToken, 0)
            textView.clearFocus()
            false
        }
    }

    private fun setKeyboard() {
        val controlManager: InputMethodManager? =
            requireActivity().getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager?
        val softKeyboard = SkeyBoard(binding.root, controlManager!!)
        softKeyboard.setSoftKeyboardCallback(object : SkeyBoard.SoftKeyboardChanged {
            override fun onSoftKeyboardHide() {
                Handler(Looper.getMainLooper()).post {
                    Log.e("SKD", "hide")
                    isKeyboardOn = false
                    binding.rvSearch.isVisible = true
                }
            }

            override fun onSoftKeyboardShow() {
                Handler(Looper.getMainLooper()).post {
                    Log.e("SKD", "show")
                    isKeyboardOn = true
                    binding.rvSearch.isVisible = false
                }
            }
        })
    }

    private fun setRv() {
        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SearchAdapter().apply {
                itemClick = {
                    CallApi().getLatLng(it) {
                        //Toast.makeText(this@SearchActivity, "${it?.x} ${it?.y}", Toast.LENGTH_LONG).show()

                        //Toast.makeText(this@SearchActivity, it.toString(), Toast.LENGTH_SHORT).show()
                        jumpMarker(it)

                    }
                }
            }
        }
    }

    private fun jumpMarker(it: GeocodeResponse.Addresse?) {
        if (it?.x == null && it?.y == null) {
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
            for (item in ArrayList(it)) {
                CallApi().getLatLng(item.roadAddress) {
                    makeMarker(item, it)
                }
            }
        }

    }

    private fun makeMarker(item: SearchResponse.Item, it: GeocodeResponse.Addresse?) {
        if (it?.x == null && it?.y == null) {
            Toast.makeText(requireContext(), "해당 위치로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val marker = Marker()
        marker.position = LatLng(it.y.toDouble(), it.x.toDouble())
        marker.captionText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            Html.fromHtml(item.title).toString()
        }

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

            if (isKeyboardOn) {
                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
            } else {
                binding.rvSearch.isVisible = false
            }
            false
        }

        naverMap.addOnCameraChangeListener { reason, animated ->
            //Log.i("NaverMap", "카메라 변경 - reson: $reason, animated: $animated")
            if (isEdit) {
                if (System.currentTimeMillis() - b4GeocodingTime > 300) {
                    CallApi().getLocation(naverMap.cameraPosition.target) {
                        binding.tvAddress.text = it
                        currentLatitude = naverMap.cameraPosition.target.latitude
                        currentLongitude = naverMap.cameraPosition.target.longitude
                        currentAddress = it.toString()
                    }
                }
            }
        }


        setMapUiSettings()

    }

    private fun setObstacleMarker() {
        if(!isFirstObstacle) {
            clearMarker()
        }
        isFirstObstacle = false

        val blockIcon = OverlayImage.fromResource(R.drawable.block)
        val slopIcon = OverlayImage.fromResource(R.drawable.slope)
        val stairsIcon = OverlayImage.fromResource(R.drawable.stairs)
        val otherIcon = OverlayImage.fromResource(R.drawable.warning)

        val iconSize = nulliUtil().dp2Px(requireContext(), 30).toInt()

        for (obstacle in obstacleList) {
            if (obstacle.latitude == null || obstacle.longitude == null) {
                continue
            } else {
                val marker = Marker().apply {
                    position =
                        LatLng(obstacle.latitude!!.toDouble(), obstacle.longitude!!.toDouble())
                    icon = when ((obstacle.type ?: "-1").toInt()) {
                        Obstacle.STAIR -> stairsIcon
                        Obstacle.SLOPE -> slopIcon
                        Obstacle.BLOCK -> blockIcon
                        Obstacle.OTHER -> otherIcon
                        else -> otherIcon
                    }.apply {
                        width = iconSize
                        height = iconSize
                    }
                }
                markerList.add(marker)
                marker.setOnClickListener {
                    //Toast.makeText(requireContext(), obstacle.id, Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), WriteObstacleActivity::class.java).apply {
                        putExtra(WriteObstacleActivity.ID, obstacle.id)
                    }
                    startActivity(intent)
                    true
                }
                marker.map = naverMap
            }
        }
    }

    private fun setBuildingMarker() {
        if(!isFirstBuilding) {
            clearMarker()
        }
        isFirstBuilding= false

        val hospitalIcon = OverlayImage.fromResource(R.drawable.hospital)
        val drugIcon = OverlayImage.fromResource(R.drawable.drug)
        val rehabilitationIcon = OverlayImage.fromResource(R.drawable.rehabilitation)
        val vriousIcon = OverlayImage.fromResource(R.drawable.vrious)

        val iconSize = nulliUtil().dp2Px(requireContext(), 50).toInt()

        for (building in buildingList) {
            if (building.latitude == null || building.longitude == null) {
                continue
            } else {
                val marker = Marker().apply {
                    captionText = building.title?: ""
                    position =
                        LatLng(building.latitude!!.toDouble(), building.longitude!!.toDouble())
                    icon = when ((building.type ?: "-1").toInt()) {
                        Building.HOSPITAL -> hospitalIcon
                        Building.DRUG -> drugIcon
                        Building.REHABILITATION -> rehabilitationIcon
                        Building.VRIOUS -> vriousIcon
                        else -> vriousIcon
                    }.apply {
                        width = iconSize
                        height = iconSize
                    }
                }
                markerList.add(marker)
                marker.setOnClickListener {
                    //Toast.makeText(requireContext(), obstacle.id, Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), WriteBuildingActivity::class.java).apply {
                        putExtra(WriteBuildingActivity.ID, building.id)
                        putExtra(WriteBuildingActivity.LAT, building.latitude)
                        putExtra(WriteBuildingActivity.LNG, building.longitude)
                        putExtra(WriteBuildingActivity.ADR, building.address)
                        putExtra(WriteBuildingActivity.TYPE, building.type?.toInt())
                    }
                    startActivity(intent)
                    true
                }

                marker.map = naverMap
            }
        }
    }

    private fun clearMarker() {
        for (m in markerList) {
            m.map = null
        }
        markerList.clear()
    }

    private fun setMapUiSettings() {
        val uiSettings = naverMap.uiSettings
        uiSettings.isScaleBarEnabled = true
        uiSettings.isIndoorLevelPickerEnabled = true
        uiSettings.isLocationButtonEnabled = true
        uiSettings.isCompassEnabled = false
        NaverMapOptions().locationButtonEnabled(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val CLEAR = -1
        private const val OBSTACLE = 0
        private const val BUILDING = 1
    }

    private fun setFABClickEvent() {
        // 플로팅 버튼 클릭시 애니메이션 동작 기능
        binding.fabMain.setOnClickListener {
            toggleFab()
        }

        // 플로팅 버튼 클릭 이벤트 - 후기
        binding.fabMap.setOnClickListener {
            setEditMode(BUILDING)
        }

        // 플로팅 버튼 클릭 이벤트 - 장애물
        binding.fabObstacleMap.setOnClickListener {
            setEditMode(OBSTACLE)
        }
    }

    private fun setEditMode(type: Int) {
        when (type) {
            CLEAR -> {
                isEdit = false
                binding.etSearch.isVisible = true
                binding.fabMain.isVisible = true
                binding.fabMap.isVisible = true
                binding.fabObstacleMap.isVisible = true
                binding.clObstaclePanel.isVisible = false
                binding.ivTarget.isVisible = false
            }
            OBSTACLE, BUILDING -> {
                isEdit = true
                binding.etSearch.isVisible = false
                binding.rvSearch.isVisible = false
                binding.fabMain.isVisible = false
                binding.fabMap.isVisible = false
                binding.fabObstacleMap.isVisible = false
                binding.clObstaclePanel.isVisible = true
                binding.ivTarget.isVisible = true
            }
        }

        binding.tvApply.setOnClickListener {
//            if (type == OBSTACLE) {
//                saveData(type)
//            } else if (type == BUILDING) {
//                saveData(type)
//            }
            saveData(type)
            Toast.makeText(requireContext(), "등록", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData(type: Int) {
        val intent = if (type == OBSTACLE) {
            Intent(requireActivity(), WriteObstacleActivity::class.java).apply {
                putExtra(WriteObstacleActivity.LAT, currentLatitude)
                putExtra(WriteObstacleActivity.LNG, currentLongitude)
                putExtra(WriteObstacleActivity.ADR, currentAddress)
            }
        } else {
            Intent(requireActivity(), WriteBuildingActivity::class.java).apply {
                putExtra(WriteBuildingActivity.LAT, currentLatitude)
                putExtra(WriteBuildingActivity.LNG, currentLongitude)
                putExtra(WriteBuildingActivity.ADR, currentAddress)
            }
        }
        startActivity(intent)
    }

    private fun toggleFab() {
        // 플로팅 액션 버튼 닫기 - 열려있는 플로팅 버튼 집어넣는 애니메이션
        if (isFabOpen) {
            ObjectAnimator.ofFloat(binding.fabMap, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabObstacleMap, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabMain, View.ROTATION, 45f, 0f).apply { start() }
            binding.tvMapLable.visibility = View.GONE
            binding.tvObstacleMapLable.visibility = View.GONE
        } else { // 플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션
            ObjectAnimator.ofFloat(binding.fabObstacleMap, "translationY", -360f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabMap, "translationY", -180f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabMain, View.ROTATION, 0f, 45f).apply { start() }

            ObjectAnimator.ofFloat(binding.tvObstacleMapLable, "translationY", -360f).apply { start() }

            ObjectAnimator.ofFloat(binding.tvMapLable, "translationY", -180f).apply { start() }
            binding.tvMapLable.visibility = View.VISIBLE
            binding.tvObstacleMapLable.visibility = View.VISIBLE
        }

        isFabOpen = !isFabOpen

    }

}