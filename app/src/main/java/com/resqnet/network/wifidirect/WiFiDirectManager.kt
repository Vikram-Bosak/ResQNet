package com.resqnet.network.wifidirect

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WiFiDirectManager @Inject constructor(
    private val context: Context
) {

    private val wifiP2pManager: WifiP2pManager? by lazy {
        context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    }

    private val channel: WifiP2pManager.Channel? by lazy {
        wifiP2pManager?.initialize(context, context.mainLooper, null)
    }

    private val _isDiscovering = MutableStateFlow(false)
    val isDiscovering: StateFlow<Boolean> = _isDiscovering

    private val _discoveredDevices = MutableStateFlow<List<WifiP2pDevice>>(emptyList())
    val discoveredDevices: StateFlow<List<WifiP2pDevice>> = _discoveredDevices

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _currentDevice = MutableStateFlow<WifiP2pDevice?>(null)
    val currentDevice: StateFlow<WifiP2pDevice?> = _currentDevice

    companion object {
        private const val TAG = "WiFiDirectManager"
    }

    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->
        _discoveredDevices.value = peerList.deviceList.toList()
        Log.d(TAG, "Discovered ${peerList.deviceList.size} peers")
    }

    private val connectionListener = object : WifiP2pManager.ConnectionInfoListener {
        override fun onConnectionInfoAvailable(info: android.net.wifi.p2p.WifiP2pInfo) {
            _isConnected.value = info.groupFormed
            Log.d(TAG, "Connection info: groupFormed=${info.groupFormed}")
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                    val state = intent.getIntExtra(
                        WifiP2pManager.EXTRA_WIFI_STATE,
                        WifiP2pManager.WIFI_P2P_STATE_DISABLED
                    )
                    Log.d(TAG, "WiFi P2P state changed: $state")
                }
                WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                    wifiP2pManager?.requestPeers(channel, peerListListener)
                }
                WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                    wifiP2pManager?.requestConnectionInfo(channel, connectionListener)
                }
                WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                    val device = intent.getParcelableExtra<WifiP2pDevice>(
                        WifiP2pManager.EXTRA_WIFI_P2P_DEVICE
                    )
                    _currentDevice.value = device
                    Log.d(TAG, "This device changed: ${device?.deviceName}")
                }
            }
        }
    }

    /**
     * Initialize WiFi Direct
     */
    fun initialize() {
        val intentFilter = IntentFilter().apply {
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }
        context.registerReceiver(broadcastReceiver, intentFilter)
        Log.d(TAG, "WiFi Direct initialized")
    }

    /**
     * Check if WiFi P2P is available
     */
    @SuppressLint("MissingPermission")
    fun isWiFiP2PAvailable(): Boolean {
        return wifiP2pManager != null && channel != null
    }

    /**
     * Start discovering peers
     */
    @SuppressLint("MissingPermission")
    fun startDiscovery() {
        if (!isWiFiP2PAvailable()) {
            Log.e(TAG, "WiFi P2P not available")
            return
        }

        if (_isDiscovering.value) {
            Log.d(TAG, "Already discovering")
            return
        }

        wifiP2pManager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                _isDiscovering.value = true
                Log.d(TAG, "Peer discovery started")
            }

            override fun onFailure(reason: Int) {
                _isDiscovering.value = false
                Log.e(TAG, "Peer discovery failed: $reason")
            }
        })
    }

    /**
     * Stop discovering peers
     */
    @SuppressLint("MissingPermission")
    fun stopDiscovery() {
        wifiP2pManager?.stopPeerDiscovery(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                _isDiscovering.value = false
                Log.d(TAG, "Peer discovery stopped")
            }

            override fun onFailure(reason: Int) {
                Log.e(TAG, "Failed to stop peer discovery: $reason")
            }
        })
    }

    /**
     * Connect to a peer
     */
    @SuppressLint("MissingPermission")
    fun connectToDevice(device: WifiP2pDevice) {
        val config = WifiP2pConfig().apply {
            deviceAddress = device.deviceAddress
        }

        wifiP2pManager?.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.d(TAG, "Connecting to ${device.deviceName}")
            }

            override fun onFailure(reason: Int) {
                Log.e(TAG, "Failed to connect to ${device.deviceName}: $reason")
            }
        })
    }

    /**
     * Disconnect from current group
     */
    @SuppressLint("MissingPermission")
    fun disconnect() {
        wifiP2pManager?.cancelConnect(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                _isConnected.value = false
                Log.d(TAG, "Disconnected")
            }

            override fun onFailure(reason: Int) {
                Log.e(TAG, "Failed to disconnect: $reason")
            }
        })
    }

    /**
     * Get this device info
     */
    @SuppressLint("MissingPermission")
    fun getThisDevice(): WifiP2pDevice? {
        return _currentDevice.value
    }

    /**
     * Get device name
     */
    @SuppressLint("MissingPermission")
    fun getDeviceName(): String {
        return _currentDevice.value?.deviceName ?: "Unknown Device"
    }

    /**
     * Clear discovered devices list
     */
    fun clearDiscoveredDevices() {
        _discoveredDevices.value = emptyList()
    }

    /**
     * Cleanup resources
     */
    fun cleanup() {
        try {
            context.unregisterReceiver(broadcastReceiver)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to unregister receiver", e)
        }
    }
}
