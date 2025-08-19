package com.tontinepro.data.local.dao

import androidx.room.*
import com.tontinepro.data.local.entities.TransactionEntity

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: String): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getUserTransactions(userId: String): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE reference = :reference")
    suspend fun getTransactionByReference(reference: String): TransactionEntity?

    @Query("SELECT * FROM transactions ORDER BY createdAt DESC")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE status = :status")
    suspend fun getTransactionsByStatus(status: String): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions")
    suspend fun clearAll()

    @Query("SELECT SUM(amount) FROM transactions WHERE userId = :userId AND type = :type AND status = 'COMPLETED'")
    suspend fun getTotalAmountByType(userId: String, type: String): Long?
}
