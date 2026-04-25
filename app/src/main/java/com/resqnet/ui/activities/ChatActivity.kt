package com.resqnet.ui.activities

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.resqnet.R
import com.resqnet.ui.adapters.MessageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var tvChatTitle: TextView

    private var currentDeviceId: String? = null
    private var currentDeviceName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Get device info from intent
        currentDeviceId = intent.getStringExtra("device_id")
        currentDeviceName = intent.getStringExtra("device_name")

        setupUI()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        recyclerView = findViewById(R.id.recyclerViewMessages)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        tvChatTitle = findViewById(R.id.tvChatTitle)

        tvChatTitle.text = currentDeviceName ?: "Chat"

        btnSend.setOnClickListener {
            sendMessage()
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
            }
            adapter = messageAdapter
        }
    }

    private fun sendMessage() {
        val message = etMessage.text.toString().trim()
        if (message.isEmpty()) return

        currentDeviceId?.let { deviceId ->
            viewModel.sendMessage(deviceId, message)
            etMessage.text.clear()
        }
    }

    private fun observeViewModel() {
        currentDeviceId?.let { deviceId ->
            viewModel.getMessages(deviceId).observe(this) { messages ->
                messageAdapter.submitList(messages)
                recyclerView.scrollToPosition(messages.size - 1)
            }
        }

        viewModel.messageSent.observe(this) { sent ->
            if (sent) {
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
