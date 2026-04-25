package com.resqnet.network.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothManager @Inject constructor(
    private val context: Context
) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as android.bluetooth.BluetoothManager
        bluetoothManager.adapter
    }

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    private val _discoveredDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val discoveredDevices: StateFlow<List<BluetoothDevice>> = _discoveredDevices

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanCallback: ScanCallback? = null

    companion object {
        private const val TAG = "BluetoothManager"
        private const val SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB"
        private const val SCAN_PERIOD: Long = 10000 // 10 seconds
    }

    /**
     * Check if Bluetooth is available and enabled
     */
    fun isBluetoothAvailable(): Boolean {
        return bluetoothAdapter != null && bluetoothAdapter?.isEnabled == true
    }

    /**
     * Enable Bluetooth
     */
    @SuppressLint("MissingPermission")
    fun enableBluetooth(): Boolean {
        return bluetoothAdapter?.enable() ?: false
    }

    /**
     * Disable Bluetooth
     */
    @SuppressLint("MissingPermission")
    fun disableBluetooth(): Boolean {
        return bluetoothAdapter?.disable() ?: false
    }

    /**
     * Start scanning for nearby devices
     */
    @SuppressLint("MissingPermission")
    fun startScanning() {
        if (!isBluetoothAvailable()) {
            Log.e(TAG, "Bluetooth not available")
            return
        }

        if (_isScanning.value) {
            Log.d(TAG, "Already scanning")
            return
        }

        bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner

        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val device = result.device
                if (!_discoveredDevices.value.any { it.address == device.address }) {
                    _discoveredDevices.value = _discoveredDevices.value + device
                    Log.d(TAG, "Discovered device: ${device.name} - ${device.address}")
                }
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>) {
                results.forEach { result ->
                    val device = result.device
                    if (!_discoveredDevices.value.any { it.address == device.address }) {
                        _discoveredDevices.value = _discoveredDevices.value + device
                    }
                }
            }

            override fun onScanFailed(errorCode: Int) {
                Log.e(TAG, "Scan failed with error: $errorCode")
                _isScanning.value = false
            }
        }

        bluetoothLeScanner?.startScan(scanCallback)
        _isScanning.value = true
        Log.d(TAG, "Started scanning")

        // Auto-stop after SCAN_PERIOD
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            stopScanning()
        }, SCAN_PERIOD)
    }

    /**
     * Stop scanning for devices
     */
    @SuppressLint("MissingPermission")
    fun stopScanning() {
        scanCallback?.let {
            bluetoothLeScanner?.stopScan(it)
        }
        _isScanning.value = false
        Log.d(TAG, "Stopped scanning")
    }

    /**
     * Connect to a device
     */
    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice): Boolean {
        try {
            // For simplicity, we're just marking as connected
            // In a real implementation, you'd establish a socket connection
            _isConnected.value = true
            Log.d(TAG, "Connected to ${device.name}")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to connect to device", e)
            return false
        }
    }

    /**
     * Disconnect from current device
     */
    fun disconnect() {
        _isConnected.value = false
        Log.d(TAG, "Disconnected")
    }

    /**
     * Get device name
     */
    @SuppressLint("MissingPermission")
    fun getDeviceName(): String {
        return bluetoothAdapter?.name ?: "Unknown Device"
    }

    /**
     * Get device address
     */
    @SuppressLint("MissingPermission")
    fun getDeviceAddress(): String {
        return bluetoothAdapter?.address ?: ""
    }

    /**
     * Clear discovered devices list
     */
    fun clearDiscoveredDevices() {
        _discoveredDevices.value = emptyList()
    }

    /**
     * Get signal strength (RSSI) for a device
     */
    fun getSignalStrength(rssi: Int): Int {
        // Convert RSSI to percentage (0-100)
        // Typical range: -100 (weak) to -50 (strong)
        val minRssi = -100
        val maxRssi = -50
        val normalized = ((rssi - minRssi).toFloat() / (maxRssi - minRssi)) * 100
        return normalized.coerceIn(0f, 100f).toInt()
    }
}
