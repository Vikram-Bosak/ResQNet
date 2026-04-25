package com.resqnet.ui.activities

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.resqnet.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {

    private val viewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // In a real implementation, you would integrate a map library like Google Maps
        // or OpenStreetMap to display device locations
    }

    private fun observeViewModel() {
        viewModel.deviceLocations.observe(this) { locations ->
            // Update map with device locations
        }
    }
}
