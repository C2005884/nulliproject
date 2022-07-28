package com.example.nulli.ui.map

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nulli.R
import com.example.nulli.databinding.FragmentMapBinding
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource

class MyMapFragment : Fragment() , OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

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
        binding.tvSearch.setOnClickListener {
            requireActivity().startActivity(Intent(requireActivity(),SearchActivity::class.java))
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
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
}