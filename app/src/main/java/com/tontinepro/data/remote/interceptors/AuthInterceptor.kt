package com.tontinepro.data.remote.interceptors

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val context: Context
) : Interceptor {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("tontine_prefs", Context.MODE_PRIVATE)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Récupérer le token d'authentification
        val token = sharedPreferences.getString("access_token", null)

        // Ajouter le token à la requête si disponible
        val authenticatedRequest = if (token != null) {
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build()
        } else {
            request.newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build()
        }

        val response = chain.proceed(authenticatedRequest)

        // Gérer l'expiration du token (401 Unauthorized)
        if (response.code == 401 && token != null) {
            // Token expiré, le supprimer
            sharedPreferences.edit()
                .remove("access_token")
                .remove("refresh_token")
                .apply()

            // Rediriger vers l'écran de connexion
            // Cette logique peut être gérée différemment selon votre architecture
        }

        return response
    }
}
