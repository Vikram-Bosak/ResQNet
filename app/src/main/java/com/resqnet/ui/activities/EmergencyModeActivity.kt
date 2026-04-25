package com.resqnet.ui.activities

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.resqnet.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmergencyModeActivity : AppCompatActivity() {

    private val viewModel: EmergencyModeViewModel by viewModels()
    private lateinit var switchEmergencyMode: Switch
    private lateinit var tvBatteryStatus: TextView
    private lateinit var tvActiveConnections: TextView
    private lateinit var btnExitEmergency: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_mode)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        switchEmergencyMode = findViewById(R.id.switchEmergencyMode)
        tvBatteryStatus = findViewById(R.id.tvBatteryStatus)
        tvActiveConnections = findViewById(R.id.tvActiveConnections)
        btnExitEmergency = findViewById(R.id.btnExitEmergency)

        switchEmergencyMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleEmergencyMode(isChecked)
        }

        btnExitEmergency.setOnClickListener {
            viewModel.toggleEmergencyMode(false)
            finish()
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.isEmergencyMode.observe(this) { isEmergency ->
            switchEmergencyMode.isChecked = isEmergency
            if (isEmergency) {
                Toast.makeText(this, "Emergency Mode Activated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Emergency Mode Deactivated", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.batteryStatus.observe(this) { status ->
            tvBatteryStatus.text = status
        }

        viewModel.activeConnections.observe(this) { count ->
            tvActiveConnections.text = "$count Active Connections"
        }
    }
}
