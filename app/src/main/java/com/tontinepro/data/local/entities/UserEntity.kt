package com.tontinepro.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tontinepro.data.models.User
import com.tontinepro.data.models.enums.FormuleType
import com.tontinepro.data.models.enums.UserStatus

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val nom: String,
    val prenom: String,
    val email: String,
    val phone: String?,
    val dateNaissance: String,
    val photo: String?,
    val formule: String,
    val status: String,
    val parrainCode: String?,
    val parrainedBy: String?,
    val isAdmin: Boolean = false,
    val createdAt: String,
    val updatedAt: String
) {
    fun toDomain(): User {
        return User(
            id = id,
            nom = nom,
            prenom = prenom,
            email = email,
            phone = phone,
            dateNaissance = dateNaissance,
            photo = photo,
            formule = FormuleType.valueOf(formule),
            status = UserStatus.valueOf(status),
            parrainCode = parrainCode,
            parrainedBy = parrainedBy,
            isAdmin = isAdmin,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

// Extension pour convertir User en UserEntity
fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        nom = nom,
        prenom = prenom,
        email = email,
        phone = phone,
        dateNaissance = dateNaissance,
        photo = photo,
        formule = formule.name,
        status = status.name,
        parrainCode = parrainCode,
        parrainedBy = parrainedBy,
        isAdmin = isAdmin,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}