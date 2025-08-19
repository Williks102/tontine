package com.tontinepro.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.tontinepro.data.models.enums.FormuleType
import com.tontinepro.utils.formatCurrency

@Parcelize
data class Parrainage(
    val id: String,
    val parrainId: String,
    val filleulId: String,
    val formule: FormuleType,
    val gain: Long,
    val isActive: Boolean,
    val createdAt: String,
    val filleul: User? = null
) : Parcelable {
    val formattedGain: String get() = "${gain.formatCurrency()} FCFA"
    val filleulName: String get() = filleul?.fullName ?: "Inconnu"
}

}