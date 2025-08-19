package com.tontinepro.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tontinepro.data.models.Bulle
import com.tontinepro.data.models.enums.BulleStatus
import com.tontinepro.data.models.enums.FormuleType

@Entity(tableName = "bulles")
data class BulleEntity(
    @PrimaryKey val id: String,
    val name: String,
    val formule: String,
    val memberCount: Int,
    val maxMembers: Int,
    val currentCycle: Int,
    val status: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toDomain(): Bulle {
        return Bulle(
            id = id,
            name = name,
            formule = FormuleType.valueOf(formule),
            memberCount = memberCount,
            maxMembers = maxMembers,
            currentCycle = currentCycle,
            status = BulleStatus.valueOf(status),
            members = emptyList(), // Les membres seront chargés séparément
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

fun Bulle.toEntity(): BulleEntity {
    return BulleEntity(
        id = id,
        name = name,
        formule = formule.name,
        memberCount = memberCount,
        maxMembers = maxMembers,
        currentCycle = currentCycle,
        status = status.name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}