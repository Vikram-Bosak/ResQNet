package com.resqnet.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resqnet.domain.usecases.DeleteOldMessagesUseCase
import com.resqnet.network.bluetooth.BluetoothManager
import com.resqnet.network.wifidirect.WiFiDirectManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val bluetoothManager: BluetoothManager,
    private val wifiDirectManager: WiFiDirectManager,
    private val deleteOldMessagesUseCase: DeleteOldMessagesUseCase
) : ViewModel() {

    private val _deviceName = MutableLiveData<String>()
    val deviceName: LiveData<String> = _deviceName

    private val _deviceId = MutableLiveData<String>()
    val deviceId: LiveData<String> = _deviceId

    private val _bluetoothEnabled = MutableLiveData<Boolean>()
    val bluetoothEnabled: LiveData<Boolean> = _bluetoothEnabled

    private val _wifiDirectEnabled = MutableLiveData<Boolean>()
    val wifiDirectEnabled: LiveData<Boolean> = _wifiDirectEnabled

    private val _autoRelayEnabled = MutableLiveData<Boolean>()
    val autoRelayEnabled: LiveData<Boolean> = _autoRelayEnabled

    init {
        loadDeviceInfo()
    }

    private fun loadDeviceInfo() {
        _deviceName.value = bluetoothManager.getDeviceName()
        _deviceId.value = bluetoothManager.getDeviceAddress()
        _bluetoothEnabled.value = bluetoothManager.isBluetoothAvailable()
        _wifiDirectEnabled.value = wifiDirectManager.isWiFiP2PAvailable()
        _autoRelayEnabled.value = true // Default enabled
    }

    fun toggleBluetooth(enabled: Boolean) {
        if (enabled) {
            bluetoothManager.enableBluetooth()
        } else {
            bluetoothManager.disableBluetooth()
        }
        _bluetoothEnabled.value = enabled
    }

    fun toggleWiFiDirect(enabled: Boolean) {
        if (enabled) {
            wifiDirectManager.startDiscovery()
        } else {
            wifiDirectManager.stopDiscovery()
        }
        _wifiDirectEnabled.value = enabled
    }

    fun toggleAutoRelay(enabled: Boolean) {
        _autoRelayEnabled.value = enabled
        // In a real implementation, this would control message relay behavior
    }

    fun clearMessages() {
        viewModelScope.launch {
            deleteOldMessagesUseCase(0) // Delete all messages
        }
    }

    fun clearDevices() {
        bluetoothManager.clearDiscoveredDevices()
        wifiDirectManager.clearDiscoveredDevices()
    }
}
