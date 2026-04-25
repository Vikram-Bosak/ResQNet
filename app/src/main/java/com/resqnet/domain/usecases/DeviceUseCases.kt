package com.resqnet.domain.usecases

import com.resqnet.data.models.Device
import com.resqnet.data.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDevicesUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    operator fun invoke(): Flow<List<Device>> = deviceRepository.getAllDevices()
}

class GetConnectedDevicesUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    operator fun invoke(): Flow<List<Device>> = deviceRepository.getConnectedDevices()
}

class UpdateDeviceConnectionUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: String, isConnected: Boolean) {
        deviceRepository.updateDeviceConnection(deviceId, isConnected)
    }
}

class UpdateDeviceSignalUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: String, signalStrength: Int) {
        deviceRepository.updateDeviceSignal(deviceId, signalStrength)
    }
}

class UpdateDeviceEmergencyModeUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: String, isEmergencyMode: Boolean) {
        deviceRepository.updateDeviceEmergencyMode(deviceId, isEmergencyMode)
    }
}

class DisconnectAllDevicesUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke() {
        deviceRepository.disconnectAllDevices()
    }
}

class GetConnectedDeviceCountUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(): Int = deviceRepository.getConnectedDeviceCount()
}
