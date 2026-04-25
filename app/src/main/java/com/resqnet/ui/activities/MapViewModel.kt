package com.resqnet.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resqnet.data.models.Device
import com.resqnet.domain.usecases.GetDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getDevicesUseCase: GetDevicesUseCase
) : ViewModel() {

    private val _deviceLocations = MutableLiveData<List<DeviceLocation>>()
    val deviceLocations: LiveData<List<DeviceLocation>> = _deviceLocations

    init {
        loadDeviceLocations()
    }

    private fun loadDeviceLocations() {
        viewModelScope.launch {
            getDevicesUseCase().collect { devices ->
                val locations = devices.map { device ->
                    DeviceLocation(
                        deviceId = device.deviceId,
                        deviceName = device.deviceName,
                        latitude = 0.0, // In real implementation, get from device
                        longitude = 0.0,
                        lastSeen = device.lastSeen
                    )
                }
                _deviceLocations.value = locations
            }
        }
    }

    data class DeviceLocation(
        val deviceId: String,
        val deviceName: String,
        val latitude: Double,
        val longitude: Double,
        val lastSeen: Long
    )
}
