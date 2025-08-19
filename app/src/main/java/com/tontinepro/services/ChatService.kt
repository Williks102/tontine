package com.tontinepro.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tontinepro.data.models.ChatMessage
import com.tontinepro.data.models.enums.MessageType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatService @Inject constructor() {

    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _isTyping = MutableLiveData<Boolean>()
    val isTyping: LiveData<Boolean> = _isTyping

    private val messagesList = mutableListOf<ChatMessage>()

    fun sendMessage(message: String, userId: String) {
        val chatMessage = ChatMessage(
            id = generateMessageId(),
            userId = userId,
            message = message,
            type = MessageType.USER,
            isRead = false,
            createdAt = System.currentTimeMillis().toString()
        )

        messagesList.add(chatMessage)
        _messages.value = messagesList.toList()

        // Simuler une réponse automatique après 2 secondes
        simulateAutoResponse()
    }

    fun markMessageAsRead(messageId: String) {
        val messageIndex = messagesList.indexOfFirst { it.id == messageId }
        if (messageIndex != -1) {
            messagesList[messageIndex] = messagesList[messageIndex].copy(isRead = true)
            _messages.value = messagesList.toList()
        }
    }

    fun getUnreadMessagesCount(): Int {
        return messagesList.count { !it.isRead && it.type == MessageType.SUPPORT }
    }

    private fun simulateAutoResponse() {
        // Simuler l'indicateur "en train d'écrire"
        _isTyping.value = true

        // Après 2 secondes, envoyer une réponse automatique
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            _isTyping.value = false

            val autoResponse = getAutoResponse()
            val supportMessage = ChatMessage(
                id = generateMessageId(),
                userId = null,
                message = autoResponse,
                type = MessageType.SUPPORT,
                isRead = false,
                createdAt = System.currentTimeMillis().toString()
            )

            messagesList.add(supportMessage)
            _messages.value = messagesList.toList()
        }, 2000)
    }

    private fun getAutoResponse(): String {
        val responses = listOf(
            "Merci pour votre message. Un agent va vous répondre dans les plus brefs délais.",
            "Nous avons bien reçu votre demande. Notre équipe support vous contactera bientôt.",
            "Votre message est important pour nous. Un conseiller vous répondra rapidement.",
            "Merci de nous avoir contactés. Nous étudions votre demande."
        )
        return responses.random()
    }

    private fun generateMessageId(): String {
        return "MSG_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }
}