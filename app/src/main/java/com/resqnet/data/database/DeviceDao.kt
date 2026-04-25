package com.resqnet.data.database

import androidx.room.*
import com.resqnet.data.models.Device
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {

    @Query("SELECT * FROM devices ORDER BY lastSeen DESC")
    fun getAllDevices(): Flow<List<Device>>

    @Query("SELECT * FROM devices WHERE isConnected = 1 ORDER BY lastSeen DESC")
    fun getConnectedDevices(): Flow<List<Device>>

    @Query("SELECT * FROM devices WHERE deviceId = :deviceId")
    suspend fun getDeviceById(deviceId: String): Device?

    @Query("SELECT * FROM devices WHERE lastSeen > :timestamp ORDER BY lastSeen DESC")
    fun getRecentDevices(timestamp: Long): Flow<List<Device>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: Device)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevices(devices: List<Device>)

    @Update
    suspend fun updateDevice(device: Device)

    @Delete
    suspend fun deleteDevice(device: Device)

    @Query("DELETE FROM devices WHERE deviceId = :deviceId")
    suspend fun deleteDeviceById(deviceId: String)

    @Query("DELETE FROM devices WHERE lastSeen < :timestamp")
    suspend fun deleteOldDevices(timestamp: Long)

    @Query("UPDATE devices SET isConnected = 0")
    suspend fun disconnectAllDevices()

    @Query("SELECT COUNT(*) FROM devices WHERE isConnected = 1")
    suspend fun getConnectedDeviceCount(): Int
}
