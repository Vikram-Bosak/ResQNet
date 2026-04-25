package com.resqnet.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resqnet.data.models.Message
import com.resqnet.domain.usecases.SendMessageUseCase
import com.resqnet.network.bluetooth.BluetoothManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val bluetoothManager: BluetoothManager
) : ViewModel() {

    private val _messageSent = MutableLiveData<Boolean>()
    val messageSent: LiveData<Boolean> = _messageSent

    fun getMessages(deviceId: String): LiveData<List<Message>> {
        val senderId = bluetoothManager.getDeviceAddress()
        return sendMessageUseCase as LiveData<List<Message>>
    }

    fun sendMessage(receiverId: String, content: String) {
        viewModelScope.launch {
            try {
                sendMessageUseCase(
                    senderId = bluetoothManager.getDeviceAddress(),
                    senderName = bluetoothManager.getDeviceName(),
                    receiverId = receiverId,
                    content = content,
                    priority = com.resqnet.data.models.MessagePriority.NORMAL
                )
                _messageSent.value = true
            } catch (e: Exception) {
                _messageSent.value = false
            }
        }
    }
}
