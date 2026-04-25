package com.resqnet.data.repository

import com.resqnet.data.database.DeviceDao
import com.resqnet.data.models.Device
import com.resqnet.data.models.ConnectionType
import com.resqnet.data.models.DeviceType
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(
    private val deviceDao: DeviceDao
) {

    fun getAllDevices(): Flow<List<Device>> = deviceDao.getAllDevices()

    fun getConnectedDevices(): Flow<List<Device>> = deviceDao.getConnectedDevices()

    fun getRecentDevices(timestamp: Long): Flow<List<Device>> =
        deviceDao.getRecentDevices(timestamp)

    suspend fun getDeviceById(deviceId: String): Device? = deviceDao.getDeviceById(deviceId)

    suspend fun insertDevice(device: Device) = deviceDao.insertDevice(device)

    suspend fun insertDevices(devices: List<Device>) = deviceDao.insertDevices(devices)

    suspend fun updateDevice(device: Device) = deviceDao.updateDevice(device)

    suspend fun deleteDevice(device: Device) = deviceDao.deleteDevice(device)

    suspend fun deleteDeviceById(deviceId: String) = deviceDao.deleteDeviceById(deviceId)

    suspend fun deleteOldDevices(timestamp: Long) = deviceDao.deleteOldDevices(timestamp)

    suspend fun disconnectAllDevices() = deviceDao.disconnectAllDevices()

    suspend fun getConnectedDeviceCount(): Int = deviceDao.getConnectedDeviceCount()

    /**
     * Create a new device entry
     */
    fun createDevice(
        deviceName: String,
        deviceType: DeviceType,
        connectionType: ConnectionType,
        signalStrength: Int,
        distance: Float? = null
    ): Device {
        return Device(
            deviceId = UUID.randomUUID().toString(),
            deviceName = deviceName,
            deviceType = deviceType,
            connectionType = connectionType,
            signalStrength = signalStrength,
            distance = distance,
            lastSeen = System.currentTimeMillis(),
            isConnected = false,
            isEmergencyMode = false
        )
    }

    /**
     * Update device connection status
     */
    suspend fun updateDeviceConnection(deviceId: String, isConnected: Boolean) {
        deviceDao.getDeviceById(deviceId)?.let { device ->
            deviceDao.updateDevice(device.copy(isConnected = isConnected))
        }
    }

    /**
     * Update device signal strength
     */
    suspend fun updateDeviceSignal(deviceId: String, signalStrength: Int) {
        deviceDao.getDeviceById(deviceId)?.let { device ->
            deviceDao.updateDevice(
                device.copy(
                    signalStrength = signalStrength,
                    lastSeen = System.currentTimeMillis()
                )
            )
        }
    }

    /**
     * Update device emergency mode
     */
    suspend fun updateDeviceEmergencyMode(deviceId: String, isEmergencyMode: Boolean) {
        deviceDao.getDeviceById(deviceId)?.let { device ->
            deviceDao.updateDevice(device.copy(isEmergencyMode = isEmergencyMode))
        }
    }
}
