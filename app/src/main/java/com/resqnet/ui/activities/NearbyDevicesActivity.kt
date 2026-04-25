package com.resqnet.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.resqnet.R
import com.resqnet.ui.adapters.DeviceAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NearbyDevicesActivity : AppCompatActivity() {

    private val viewModel: NearbyDevicesViewModel by viewModels()
    private lateinit var deviceAdapter: DeviceAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_devices)

        setupUI()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        recyclerView = findViewById(R.id.rvNearbyUsers)
        val tvNodeCount = findViewById<TextView>(R.id.tvNodeCount)
        val btnBroadcastLocation = findViewById<Button>(R.id.btnBroadcastLocation)

        btnBroadcastLocation.setOnClickListener {
            viewModel.refreshDevices()
            Toast.makeText(this, "Broadcasting location...", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Bottom navigation
        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.navChat).setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.navMap).setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.navSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        deviceAdapter = DeviceAdapter { device ->
            // Handle device click - open chat
            openChat(device)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@NearbyDevicesActivity)
            adapter = deviceAdapter
        }
    }

    private fun openChat(device: com.resqnet.data.models.Device) {
        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("device_id", device.deviceId)
            putExtra("device_name", device.deviceName)
        }
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.devices.observe(this) { devices ->
            deviceAdapter.submitList(devices)
            val tvNodeCount = findViewById<TextView>(R.id.tvNodeCount)
            tvNodeCount.text = "${devices.size} Nodes Connected"
        }

        viewModel.isScanning.observe(this) { isScanning ->
            val btnBroadcastLocation = findViewById<Button>(R.id.btnBroadcastLocation)
            btnBroadcastLocation.isEnabled = !isScanning
            if (isScanning) {
                Toast.makeText(this, "Scanning for devices...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
