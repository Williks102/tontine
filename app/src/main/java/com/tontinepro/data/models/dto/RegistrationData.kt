package com.tontinepro.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.tontinepro.data.models.enums.FormuleType

@Parcelize
data class RegistrationData(
    val nom: String,
    val prenom: String,
    val email: String,
    val phone: String?,
    val dateNaissance: String,
    val photo: String?,
    val formule: FormuleType,
    val parrainCode: String?
) : Parcelable {
    val fullName: String get() = "$prenom $nom"
    val hasPhoto: Boolean get() = !photo.isNullOrEmpty()
    val hasParrainCode: Boolean get() = !parrainCode.isNullOrEmpty()
}



