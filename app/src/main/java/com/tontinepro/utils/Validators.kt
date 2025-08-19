package com.tontinepro.utils

import android.util.Patterns
import java.util.regex.Pattern

object Validators {

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        val phonePattern = "^[+]?[0-9]{8,15}$"
        return phone.isNotBlank() && Pattern.matches(phonePattern, phone.replace("\\s".toRegex(), ""))
    }

    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidParrainCode(code: String): Boolean {
        return code.isNotBlank() && code.length >= 4
    }

    fun isValidAmount(amount: String): Boolean {
        return try {
            val value = amount.toDouble()
            value > 0
        } catch (e: NumberFormatException) {
            false
        }
    }
}