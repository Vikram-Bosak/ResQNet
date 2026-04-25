package com.resqnet.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resqnet.data.models.LocationData
import com.resqnet.data.models.MessagePriority
import com.resqnet.domain.usecases.*
import com.resqnet.network.bluetooth.BluetoothManager
import com.resqnet.network.wifidirect.WiFiDirectManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getConnectedDeviceCountUseCase: GetConnectedDeviceCountUseCase,
    private val bluetoothManager: BluetoothManager,
    private val wifiDirectManager: WiFiDirectManager
) : ViewModel() {

    private val _connectedDeviceCount = MutableLiveData<Int>()
    val connectedDeviceCount: LiveData<Int> = _connectedDeviceCount

    private val _isScanning = MutableLiveData<Boolean>()
    val isScanning: LiveData<Boolean> = _isScanning

    private val _nearbyDevices = MutableLiveData<List<String>>()
    val nearbyDevices: LiveData<List<String>> = _nearbyDevices

    init {
        observeNetworkState()
    }

    private fun observeNetworkState() {
        viewModelScope.launch {
            bluetoothManager.isScanning.collect { isScanning ->
                _isScanning.value = isScanning
            }

            wifiDirectManager.isDiscovering.collect { isDiscovering ->
                if (isDiscovering) {
                    _isScanning.value = true
                }
            }

            // Update connected device count
            updateConnectedDeviceCount()
        }
    }

    private fun updateConnectedDeviceCount() {
        viewModelScope.launch {
            val count = getConnectedDeviceCountUseCase()
            _connectedDeviceCount.value = count
        }
    }

    fun sendSOS(senderId: String, senderName: String) {
        viewModelScope.launch {
            val location = getCurrentLocation()
            sendMessageUseCase(
                senderId = senderId,
                senderName = senderName,
                receiverId = null, // Broadcast to all
                content = "SOS EMERGENCY! Need immediate assistance!",
                priority = MessagePriority.SOS,
                location = location
            )
        }
    }

    fun sendBroadcastMessage(senderId: String, senderName: String, message: String) {
        viewModelScope.launch {
            val location = getCurrentLocation()
            sendMessageUseCase(
                senderId = senderId,
                senderName = senderName,
                receiverId = null, // Broadcast to all
                content = message,
                priority = MessagePriority.HIGH,
                location = location
            )
        }
    }

    fun sendMessage(
        senderId: String,
        senderName: String,
        receiverId: String,
        message: String
    ) {
        viewModelScope.launch {
            val location = getCurrentLocation()
            sendMessageUseCase(
                senderId = senderId,
                senderName = senderName,
                receiverId = receiverId,
                content = message,
                priority = MessagePriority.NORMAL,
                location = location
            )
        }
    }

    private fun getCurrentLocation(): LocationData? {
        // In a real implementation, you'd get the actual GPS location
        // For now, return null or a dummy location
        return null
    }

    fun refreshDevices() {
        viewModelScope.launch {
            bluetoothManager.startScanning()
            wifiDirectManager.startDiscovery()
            updateConnectedDeviceCount()
        }
    }

    fun toggleEmergencyMode(isEnabled: Boolean) {
        // Implement emergency mode logic
        // This could include:
        // - Reducing battery usage
        // - Limiting background processes
        // - Keeping only essential services active
    }
}
