package com.tontinepro.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.tontinepro.data.models.enums.FormuleType
import com.tontinepro.data.models.enums.UserStatus
import com.tontinepro.utils.formatCurrency
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class User(
    val id: String,
    val nom: String,
    val prenom: String,
    val email: String,
    val phone: String?,
    val dateNaissance: String,
    val photo: String?,
    val formule: FormuleType,
    val status: UserStatus,
    val parrainCode: String?,
    val parrainedBy: String?,
    val isAdmin: Boolean = false,
    val createdAt: String,
    val updatedAt: String
) : Parcelable {
    val fullName: String get() = "$prenom $nom"
    val isVerified: Boolean get() = status == UserStatus.VERIFIED || status == UserStatus.ACTIVE
    val canParticipate: Boolean get() = status == UserStatus.ACTIVE

    val formattedDateNaissance: String get() = try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val date = inputFormat.parse(dateNaissance)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateNaissance
    }
}
