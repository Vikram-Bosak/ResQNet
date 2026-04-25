package com.resqnet.ui.activities

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.resqnet.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BroadcastActivity : AppCompatActivity() {

    private val viewModel: BroadcastViewModel by viewModels()
    private lateinit var etBroadcastMessage: EditText
    private lateinit var btnSendBroadcast: Button
    private lateinit var spinnerTemplate: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broadcast)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        etBroadcastMessage = findViewById(R.id.etBroadcastMessage)
        btnSendBroadcast = findViewById(R.id.btnSendBroadcast)
        spinnerTemplate = findViewById(R.id.spinnerTemplate)

        // Setup template spinner
        val templates = arrayOf(
            "Custom Message",
            "Emergency: Need Help!",
            "Meeting Point: Safe Zone",
            "Status: All Clear",
            "Warning: Danger Ahead"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, templates)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTemplate.adapter = adapter

        spinnerTemplate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                if (position > 0) {
                    etBroadcastMessage.setText(templates[position])
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnSendBroadcast.setOnClickListener {
            sendBroadcast()
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun sendBroadcast() {
        val message = etBroadcastMessage.text.toString().trim()
        if (message.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.sendBroadcast(message)
        Toast.makeText(this, "Broadcast sent!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun observeViewModel() {
        viewModel.broadcastSent.observe(this) { sent ->
            if (sent) {
                Toast.makeText(this, "Broadcast sent successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
