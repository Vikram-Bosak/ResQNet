package com.resqnet.ui.activities

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
    private lateinit var btnRefresh: ImageButton
    private lateinit var tvDeviceCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_devices)

        setupUI()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        recyclerView = findViewById(R.id.recyclerViewDevices)
        btnRefresh = findViewById(R.id.btnRefresh)
        tvDeviceCount = findViewById(R.id.tvDeviceCount)

        btnRefresh.setOnClickListener {
            viewModel.refreshDevices()
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
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
            tvDeviceCount.text = "${devices.size} Devices Found"
        }

        viewModel.isScanning.observe(this) { isScanning ->
            btnRefresh.isEnabled = !isScanning
            if (isScanning) {
                Toast.makeText(this, "Scanning for devices...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
