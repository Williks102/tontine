package com.tontinepro.data.models

import com.tontinepro.data.models.enums.MessageType

data class ChatMessage(
    val id: String,
    val userId: String?,
    val message: String,
    val type: MessageType,
    val isRead: Boolean,
    val createdAt: String
) {
    val isFromUser: Boolean get() = type == MessageType.USER
    val isFromSupport: Boolean get() = type == MessageType.SUPPORT
    val formattedTime: String get() = formatTime(createdAt)

    private fun formatTime(timestamp: String): String {
        return try {
            val time = timestamp.toLong()
            val calendar = java.util.Calendar.getInstance()
            calendar.timeInMillis = time

            val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
            val minute = calendar.get(java.util.Calendar.MINUTE)

            String.format("%02d:%02d", hour, minute)
        } catch (e: Exception) {
            "00:00"
        }
    }
}
