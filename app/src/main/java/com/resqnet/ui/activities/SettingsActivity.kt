package com.resqnet.ui.activities

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.resqnet.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var switchBluetooth: Switch
    private lateinit var switchWiFiDirect: Switch
    private lateinit var switchAutoRelay: Switch
    private lateinit var tvDeviceName: TextView
    private lateinit var tvDeviceId: TextView
    private lateinit var btnClearMessages: Button
    private lateinit var btnClearDevices: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        switchBluetooth = findViewById(R.id.switchBluetooth)
        switchWiFiDirect = findViewById(R.id.switchWiFiDirect)
        switchAutoRelay = findViewById(R.id.switchAutoRelay)
        tvDeviceName = findViewById(R.id.tvDeviceName)
        tvDeviceId = findViewById(R.id.tvDeviceId)
        btnClearMessages = findViewById(R.id.btnClearMessages)
        btnClearDevices = findViewById(R.id.btnClearDevices)

        switchBluetooth.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleBluetooth(isChecked)
        }

        switchWiFiDirect.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleWiFiDirect(isChecked)
        }

        switchAutoRelay.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleAutoRelay(isChecked)
        }

        btnClearMessages.setOnClickListener {
            viewModel.clearMessages()
            Toast.makeText(this, "Messages cleared", Toast.LENGTH_SHORT).show()
        }

        btnClearDevices.setOnClickListener {
            viewModel.clearDevices()
            Toast.makeText(this, "Devices cleared", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.deviceName.observe(this) { name ->
            tvDeviceName.text = "Device Name: $name"
        }

        viewModel.deviceId.observe(this) { id ->
            tvDeviceId.text = "Device ID: $id"
        }

        viewModel.bluetoothEnabled.observe(this) { enabled ->
            switchBluetooth.isChecked = enabled
        }

        viewModel.wifiDirectEnabled.observe(this) { enabled ->
            switchWiFiDirect.isChecked = enabled
        }

        viewModel.autoRelayEnabled.observe(this) { enabled ->
            switchAutoRelay.isChecked = enabled
        }
    }
}
