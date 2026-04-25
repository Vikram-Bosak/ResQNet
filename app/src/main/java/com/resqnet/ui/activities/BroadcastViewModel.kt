package com.resqnet.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resqnet.domain.usecases.SendMessageUseCase
import com.resqnet.network.bluetooth.BluetoothManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BroadcastViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val bluetoothManager: BluetoothManager
) : ViewModel() {

    private val _broadcastSent = MutableLiveData<Boolean>()
    val broadcastSent: LiveData<Boolean> = _broadcastSent

    fun sendBroadcast(message: String) {
        viewModelScope.launch {
            try {
                sendMessageUseCase(
                    senderId = bluetoothManager.getDeviceAddress(),
                    senderName = bluetoothManager.getDeviceName(),
                    receiverId = null, // Broadcast to all
                    content = message,
                    priority = com.resqnet.data.models.MessagePriority.HIGH
                )
                _broadcastSent.value = true
            } catch (e: Exception) {
                _broadcastSent.value = false
            }
        }
    }
}
