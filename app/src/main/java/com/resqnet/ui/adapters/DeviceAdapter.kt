package com.resqnet.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.resqnet.R
import com.resqnet.data.models.Device
import java.text.SimpleDateFormat
import java.util.*

class DeviceAdapter(
    private val onDeviceClick: (Device) -> Unit
) : ListAdapter<Device, DeviceAdapter.DeviceViewHolder>(DeviceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_device, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = getItem(position)
        holder.bind(device)
        holder.itemView.setOnClickListener {
            onDeviceClick(device)
        }
    }

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDeviceName: TextView = itemView.findViewById(R.id.tvDeviceName)
        private val tvDeviceType: TextView = itemView.findViewById(R.id.tvDeviceType)
        private val tvSignalStrength: TextView = itemView.findViewById(R.id.tvSignalStrength)
        private val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
        private val tvLastSeen: TextView = itemView.findViewById(R.id.tvLastSeen)
        private val tvConnectionStatus: TextView = itemView.findViewById(R.id.tvConnectionStatus)

        fun bind(device: Device) {
            tvDeviceName.text = device.deviceName
            tvDeviceType.text = device.deviceType.name
            tvSignalStrength.text = "${device.signalStrength}%"
            tvDistance.text = device.distance?.let { "${it.toInt()}m" } ?: "Unknown"
            tvLastSeen.text = formatLastSeen(device.lastSeen)
            tvConnectionStatus.text = if (device.isConnected) "Connected" else "Available"

            // Set connection status color
            if (device.isConnected) {
                tvConnectionStatus.setTextColor(itemView.context.getColor(android.R.color.holo_green_dark))
            } else {
                tvConnectionStatus.setTextColor(itemView.context.getColor(android.R.color.darker_gray))
            }

            // Set signal strength color
            when {
                device.signalStrength > 70 -> {
                    tvSignalStrength.setTextColor(itemView.context.getColor(android.R.color.holo_green_dark))
                }
                device.signalStrength > 40 -> {
                    tvSignalStrength.setTextColor(itemView.context.getColor(android.R.color.holo_orange_dark))
                }
                else -> {
                    tvSignalStrength.setTextColor(itemView.context.getColor(android.R.color.holo_red_dark))
                }
            }
        }

        private fun formatLastSeen(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp

            return when {
                diff < 60000 -> "Just now"
                diff < 3600000 -> "${diff / 60000}m ago"
                diff < 86400000 -> "${diff / 3600000}h ago"
                else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(timestamp))
            }
        }
    }

    class DeviceDiffCallback : DiffUtil.ItemCallback<Device>() {
        override fun areItemsTheSame(oldItem: Device, newItem: Device): Boolean {
            return oldItem.deviceId == newItem.deviceId
        }

        override fun areContentsTheSame(oldItem: Device, newItem: Device): Boolean {
            return oldItem == newItem
        }
    }
}
