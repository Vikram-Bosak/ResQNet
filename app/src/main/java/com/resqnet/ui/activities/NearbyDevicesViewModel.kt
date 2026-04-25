package com.resqnet.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resqnet.data.models.Device
import com.resqnet.domain.usecases.GetDevicesUseCase
import com.resqnet.network.bluetooth.BluetoothManager
import com.resqnet.network.wifidirect.WiFiDirectManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearbyDevicesViewModel @Inject constructor(
    private val getDevicesUseCase: GetDevicesUseCase,
    private val bluetoothManager: BluetoothManager,
    private val wifiDirectManager: WiFiDirectManager
) : ViewModel() {

    val devices: LiveData<List<Device>> = getDevicesUseCase()

    private val _isScanning = MutableLiveData<Boolean>()
    val isScanning: LiveData<Boolean> = _isScanning

    init {
        observeScanningState()
    }

    private fun observeScanningState() {
        viewModelScope.launch {
            bluetoothManager.isScanning.collect { isScanning ->
                _isScanning.value = isScanning
            }

            wifiDirectManager.isDiscovering.collect { isDiscovering ->
                if (isDiscovering) {
                    _isScanning.value = true
                }
            }
        }
    }

    fun refreshDevices() {
        viewModelScope.launch {
            bluetoothManager.startScanning()
            wifiDirectManager.startDiscovery()
        }
    }
}
