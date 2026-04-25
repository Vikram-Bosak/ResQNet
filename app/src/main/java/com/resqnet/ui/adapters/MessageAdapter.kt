package com.resqnet.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.resqnet.R
import com.resqnet.data.models.Message
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter : ListAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    private var onMessageClick: ((Message) -> Unit)? = null

    fun setOnMessageClickListener(listener: (Message) -> Unit) {
        onMessageClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
        holder.itemView.setOnClickListener {
            onMessageClick?.invoke(message)
        }
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSenderName: TextView = itemView.findViewById(R.id.tvSenderName)
        private val tvMessageContent: TextView = itemView.findViewById(R.id.tvMessageContent)
        private val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val tvPriority: TextView = itemView.findViewById(R.id.tvPriority)

        fun bind(message: Message) {
            tvSenderName.text = message.senderName
            tvMessageContent.text = message.content
            tvTimestamp.text = formatTimestamp(message.timestamp)
            tvStatus.text = message.status.name
            tvPriority.text = message.priority.name

            // Set priority color
            when (message.priority) {
                com.resqnet.data.models.MessagePriority.SOS -> {
                    tvPriority.setTextColor(itemView.context.getColor(android.R.color.holo_red_dark))
                }
                com.resqnet.data.models.MessagePriority.HIGH -> {
                    tvPriority.setTextColor(itemView.context.getColor(android.R.color.holo_orange_dark))
                }
                else -> {
                    tvPriority.setTextColor(itemView.context.getColor(android.R.color.darker_gray))
                }
            }
        }

        private fun formatTimestamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
}
