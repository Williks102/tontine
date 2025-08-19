package com.tontinepro.data.repository

import com.tontinepro.data.local.dao.TransactionDao
import com.tontinepro.data.models.Transaction
import com.tontinepro.data.models.dto.PaymentRequest
import com.tontinepro.data.models.enums.TransactionStatus
import com.tontinepro.data.remote.api.PaymentApiService
import com.tontinepro.utils.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(
    private val paymentApi: PaymentApiService,
    private val transactionDao: TransactionDao
) {

    suspend fun initiatePayment(paymentRequest: PaymentRequest): Result<PaymentResponse> {
        return try {
            val response = paymentApi.initiatePayment(paymentRequest)
            if (response.isSuccessful && response.body() != null) {
                val paymentResponse = response.body()!!
                // Sauvegarder la transaction en local
                val transaction = Transaction(
                    id = paymentResponse.transactionId,
                    userId = paymentRequest.userId,
                    amount = paymentRequest.amount,
                    type = paymentRequest.type,
                    method = paymentRequest.method,
                    status = TransactionStatus.PENDING,
                    reference = paymentResponse.reference,
                    description = paymentRequest.description,
                    bulleId = paymentRequest.bulleId,
                    createdAt = System.currentTimeMillis().toString(),
                    updatedAt = System.currentTimeMillis().toString()
                )
                transactionDao.insertTransaction(transaction.toEntity())
                Result.Success(paymentResponse)
            } else {
                Result.Error(response.message() ?: "Erreur lors de l'initiation du paiement")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur réseau")
        }
    }

    suspend fun verifyPayment(reference: String): Result<Transaction> {
        return try {
            val response = paymentApi.verifyPayment(reference)
            if (response.isSuccessful && response.body() != null) {
                val transaction = response.body()!!
                transactionDao.updateTransaction(transaction.toEntity())
                Result.Success(transaction)
            } else {
                Result.Error(response.message() ?: "Erreur lors de la vérification")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur réseau")
        }
    }

    suspend fun getUserTransactions(userId: String): List<Transaction> {
        return try {
            // Récupérer depuis l'API
            val response = paymentApi.getUserTransactions(userId)
            if (response.isSuccessful && response.body() != null) {
                val transactions = response.body()!!
                // Mettre à jour la base locale
                transactions.forEach { transaction ->
                    transactionDao.insertTransaction(transaction.toEntity())
                }
                transactions
            } else {
                // Récupérer depuis la base locale
                transactionDao.getUserTransactions(userId).map { it.toDomain() }
            }
        } catch (e: Exception) {
            transactionDao.getUserTransactions(userId).map { it.toDomain() }
        }
    }

    suspend fun getAllTransactions(): List<Transaction> {
        return try {
            val response = paymentApi.getAllTransactions()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}