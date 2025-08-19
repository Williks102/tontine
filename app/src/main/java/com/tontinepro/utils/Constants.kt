package com.tontinepro.utils

object Constants {
    // API
    const val BASE_URL = "https://api.tontinepro.com/"
    const val API_VERSION = "v1"

    // SharedPreferences
    const val PREF_NAME = "tontine_prefs"
    const val KEY_ACCESS_TOKEN = "access_token"
    const val KEY_REFRESH_TOKEN = "refresh_token"
    const val KEY_USER_ID = "user_id"
    const val KEY_IS_LOGGED_IN = "is_logged_in"

    // Database
    const val DATABASE_NAME = "tontine_database"
    const val DATABASE_VERSION = 1

    // File Provider
    const val FILE_PROVIDER_AUTHORITY = "com.tontinepro.fileprovider"

    // Request Codes
    const val REQUEST_CAMERA_PERMISSION = 100
    const val REQUEST_CONTACTS_PERMISSION = 101
    const val REQUEST_STORAGE_PERMISSION = 102

    // Payment
    const val PAYMENT_TIMEOUT = 300000L // 5 minutes

    // Notification Channels
    const val CHANNEL_PAYMENT = "tontine_payment_channel"
    const val CHANNEL_REMINDER = "tontine_reminder_channel"
    const val CHANNEL_SUPPORT = "tontine_support_channel"

    // Date Formats
    const val DATE_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss"
    const val DATE_FORMAT_DISPLAY = "dd/MM/yyyy"
    const val TIME_FORMAT_DISPLAY = "HH:mm"
}
