// data/models/dto/VerificationData.kt
package com.tontinepro.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerificationData(
    val userId: String,
    val pieceIdentite: String,
    val videoConfirmation: String,
    val acceptationJuridique: Boolean
) : Parcelable {
    val isComplete: Boolean get() =
        pieceIdentite.isNotEmpty() &&
                videoConfirmation.isNotEmpty() &&
                acceptationJuridique
}