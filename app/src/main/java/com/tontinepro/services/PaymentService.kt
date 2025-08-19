package com.tontinepro.services

import android.content.Context
import co.paystack.android.Paystack
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction as PaystackTransaction
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.RaveConstants
import com.tontinepro.data.models.dto.PaymentRequest
import com.tontinepro.data.models.dto.PaymentResponse
import com.tontinepro.data.models.enums.PaymentMethod
import com.tontinepro.utils.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun initiateFlutterwavePayment(paymentRequest: PaymentRequest): Result<PaymentResponse> {
        return try {
            // Configuration Flutterwave
            val raveUiManager = RaveUiManager(context as Activity)
                .setAmount(paymentRequest.amount.toDouble())
                .setCurrency("XOF")
                .setEmail(paymentRequest.userEmail)
                .setfName(paymentRequest.userFirstName)
                .setlName(paymentRequest.userLastName)
                .setPhoneNumber(paymentRequest.userPhone)
                .setNarration("Paiement TONTINE PRO")
                .setPublicKey(BuildConfig.FLUTTERWAVE_PUBLIC_KEY)
                .setEncryptionKey(BuildConfig.FLUTTERWAVE_ENCRYPTION_KEY)
                .setTxRef(generateTransactionReference())
                .onStagingEnv(BuildConfig.DEBUG)
                .shouldDisplayFee(true)
                .showStagingLabel(BuildConfig.DEBUG)
                .isPreAuth(false)

            // Lancer le paiement
            raveUiManager.initialize()

            Result.Success(
                PaymentResponse(
                    transactionId = generateTransactionId(),
                    reference = generateTransactionReference(),
                    paymentUrl = null, // Flutterwave utilise son propre UI
                    status = "initiated"
                )
            )
        } catch (e: Exception) {
            Result.Error("Erreur Flutterwave: ${e.message}")
        }
    }

    suspend fun initiatePaystackPayment(paymentRequest: PaymentRequest): Result<PaymentResponse> {
        return try {
            val transaction = PaystackTransaction.builder()
                .setAmount((paymentRequest.amount * 100).toInt()) // Paystack utilise les kobo
                .setEmail(paymentRequest.userEmail)
                .setReference(generateTransactionReference())
                .build()

            Result.Success(
                PaymentResponse(
                    transactionId = generateTransactionId(),
                    reference = transaction.reference,
                    paymentUrl = null, // Paystack utilise son propre UI
                    status = "initiated"
                )
            )
        } catch (e: Exception) {
            Result.Error("Erreur Paystack: ${e.message}")
        }
    }

    suspend fun verifyPayment(reference: String): Result<com.tontinepro.data.models.Transaction> {
        return try {
            // Appel à votre API backend pour vérifier le paiement
            // Votre backend vérifiera auprès de Flutterwave/Paystack
            // et retournera le statut final

            // Simulation pour l'exemple
            Result.Success(
                com.tontinepro.data.models.Transaction(
                    id = generateTransactionId(),
                    userId = "",
                    amount = 0,
                    type = com.tontinepro.data.models.enums.TransactionType.PAYMENT,
                    method = PaymentMethod.FLUTTERWAVE,
                    status = com.tontinepro.data.models.enums.TransactionStatus.COMPLETED,
                    reference = reference,
                    description = "Paiement vérifié",
                    bulleId = null,
                    createdAt = System.currentTimeMillis().toString(),
                    updatedAt = System.currentTimeMillis().toString()
                )
            )
        } catch (e: Exception) {
            Result.Error("Erreur lors de la vérification: ${e.message}")
        }
    }

    private fun generateTransactionReference(): String {
        return "TONTINE_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }

    private fun generateTransactionId(): String {
        return "TX_${System.currentTimeMillis()}_${(10000..99999).random()}"
    }
}