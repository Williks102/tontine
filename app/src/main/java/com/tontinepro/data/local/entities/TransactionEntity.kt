
package com.tontinepro.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tontinepro.data.models.Transaction
import com.tontinepro.data.models.enums.PaymentMethod
import com.tontinepro.data.models.enums.TransactionStatus
import com.tontinepro.data.models.enums.TransactionType

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val amount: Long,
    val type: String,
    val method: String,
    val status: String,
    val reference: String,
    val description: String?,
    val bulleId: String?,
    val createdAt: String,
    val updatedAt: String
) {
    fun toDomain(): Transaction {
        return Transaction(
            id = id,
            userId = userId,
            amount = amount,
            type = TransactionType.valueOf(type),
            method = PaymentMethod.valueOf(method),
            status = TransactionStatus.valueOf(status),
            reference = reference,
            description = description,
            bulleId = bulleId,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        userId = userId,
        amount = amount,
        type = type.name,
        method = method.name,
        status = status.name,
        reference = reference,
        description = description,
        bulleId = bulleId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}