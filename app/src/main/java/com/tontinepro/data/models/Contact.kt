package com.tontinepro.data.models

data class Contact(
    val name: String,
    val phone: String
) {
    val formattedPhone: String get() = formatPhoneNumber(phone)

    private fun formatPhoneNumber(phone: String): String {
        return when {
            phone.startsWith("+225") -> phone
            phone.startsWith("0") -> "+225${phone.substring(1)}"
            else -> "+225$phone"
        }
    }
}
