package com.tontinepro.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BulleMember(
    val id: String,
    val userId: String,
    val bulleId: String,
    val position: Int,
    val hasReceived: Boolean,
    val hasPaid: Boolean,
    val joinedAt: String,
    val user: User? = null
) : Parcelable {
    val isCurrentBeneficiary: Boolean get() = position == 1 && !hasReceived
    val statusText: String get() = when {
        hasReceived -> "A reçu"
        hasPaid -> "A payé"
        else -> "En attente"
    }
}
