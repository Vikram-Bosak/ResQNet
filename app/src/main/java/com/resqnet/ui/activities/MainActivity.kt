package com.resqnet.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.resqnet.R
import com.resqnet.data.models.MessagePriority
import com.resqnet.domain.usecases.*
import com.resqnet.network.bluetooth.BluetoothManager
import com.resqnet.network.wifidirect.WiFiDirectManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var bluetoothManager: BluetoothManager

    @Inject
    lateinit var wifiDirectManager: WiFiDirectManager

    private val viewModel: MainViewModel by viewModels()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            initializeNetwork()
        } else {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()
        setupUI()
        observeViewModel()
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )

        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            locationPermissionRequest.launch(missingPermissions.toTypedArray())
        } else {
            initializeNetwork()
        }
    }

    private fun initializeNetwork() {
        // Initialize WiFi Direct
        wifiDirectManager.initialize()

        // Enable Bluetooth if not already enabled
        if (!bluetoothManager.isBluetoothAvailable()) {
            bluetoothManager.enableBluetooth()
        }

        // Start scanning for devices
        startScanning()
    }

    private fun startScanning() {
        bluetoothManager.startScanning()
        wifiDirectManager.startDiscovery()
    }

    private fun setupUI() {
        // SOS Button
        findViewById<android.widget.Button>(R.id.btnSOS).setOnClickListener {
            sendSOS()
        }

        // Menu Button
        findViewById<android.widget.ImageButton>(R.id.btnMenu).setOnClickListener {
            Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show()
        }

        // Broadcast Button
        findViewById<android.widget.LinearLayout>(R.id.btnBroadcast).setOnClickListener {
            startActivity(Intent(this, BroadcastActivity::class.java))
        }

        // Chat Button
        findViewById<android.widget.LinearLayout>(R.id.btnChat).setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        // Emergency Mode Button
        findViewById<android.widget.LinearLayout>(R.id.btnEmergencyMode).setOnClickListener {
            startActivity(Intent(this, EmergencyModeActivity::class.java))
        }

        // Bottom Navigation
        findViewById<android.widget.LinearLayout>(R.id.navHome).setOnClickListener {
            // Already on home
        }

        findViewById<android.widget.LinearLayout>(R.id.navChat).setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        findViewById<android.widget.LinearLayout>(R.id.navNearby).setOnClickListener {
            startActivity(Intent(this, NearbyDevicesActivity::class.java))
        }

        findViewById<android.widget.LinearLayout>(R.id.navMap).setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        findViewById<android.widget.LinearLayout>(R.id.navSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun sendSOS() {
        viewModel.sendSOS(
            senderId = bluetoothManager.getDeviceAddress(),
            senderName = bluetoothManager.getDeviceName()
        )
        Toast.makeText(this, "SOS Sent!", Toast.LENGTH_SHORT).show()
    }

    private fun observeViewModel() {
        viewModel.connectedDeviceCount.observe(this) { count ->
            // Update UI with connected device count
            val tvConnectedCount = findViewById<android.widget.TextView>(R.id.tvConnectedCount)
            tvConnectedCount.text = "$count Connected"
        }

        viewModel.isScanning.observe(this) { isScanning ->
            // Update scanning indicator
            val tvMeshStatus = findViewById<android.widget.TextView>(R.id.tvMeshStatus)
            tvMeshStatus.text = if (isScanning) "Scanning..." else "Mesh Active"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        wifiDirectManager.cleanup()
    }
}
