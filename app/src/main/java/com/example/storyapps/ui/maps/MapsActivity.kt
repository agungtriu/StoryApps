package com.example.storyapps.ui.maps

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyapps.R
import com.example.storyapps.databinding.ActivityMapsBinding
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.ui.viewmodel.ViewModelFactory
import com.example.storyapps.utils.Utils.Companion.showLoading
import com.example.storyapps.vo.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapsViewModel = obtainViewModel(this)
        mapsViewModel.getLoginStatus().observe(this@MapsActivity) {
            token = "Bearer ${it.token}"
        }
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.f_maps) as SupportMapFragment
        mapFragment.getMapAsync(this)

        with(binding) {
            ivMapsBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        setMapStyle()
        getData()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext, ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Toast.makeText(this, getString(R.string.maps_style_failed), Toast.LENGTH_LONG)
                    .show()
            }
        } catch (exception: Resources.NotFoundException) {
            Toast.makeText(
                this,
                getString(R.string.maps_style_cant_find).plus(exception),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getData() {
        with(binding) {
            pbMaps.showLoading(true)
            mapsViewModel.loadMapsStory(token).observe(this@MapsActivity) {
                when (it.status) {
                    Status.LOADING -> pbMaps.showLoading(false)
                    Status.SUCCESS -> {
                        pbMaps.showLoading(false)
                        if (it.data != null) {
                            addManyMarker(it.data)
                        } else {
                            Toast.makeText(
                                this@MapsActivity, R.string.all_no_data, Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    Status.ERROR -> {
                        pbMaps.showLoading(false)
                        if (!it.message.isNullOrBlank()) {
                            Toast.makeText(
                                this@MapsActivity, it.message, Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun addManyMarker(mapStories: List<StoryEntity>) {
        mapStories.forEach {
            if (it.lat != null && it.lon != null) {
                val latLng = LatLng(it.lat, it.lon)
                mMap.addMarker(
                    MarkerOptions().position(latLng).title(it.name).snippet(it.description)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            }
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity
    ): MapsViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MapsViewModel::class.java]
    }
}