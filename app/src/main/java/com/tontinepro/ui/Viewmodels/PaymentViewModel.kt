package com.tontinepro.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tontinepro.services.PaymentService
import com.tontinepro.data.repository.PaymentRepository
import com.tontinepro.data.models.Transaction
import com.tontinepro.data.models.dto.PaymentRequest
import com.tontinepro.data.models.enums.PaymentMethod
import com.tontinepro.data.models.enums.TransactionType
import com.tontinepro.data.models.states.PaymentState
import com.tontinepro.utils.Result

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentService: PaymentService,
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState> = _paymentState

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun initiatePayment(
        amount: Long,
        method: PaymentMethod,
        userId: String,
        bulleId: String?,
        userEmail: String,
        userFirstName: String,
        userLastName: String,
        userPhone: String
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                _paymentState.value = PaymentState.Initiating

                val paymentRequest = PaymentRequest(
                    amount = amount,
                    method = method,
                    userId = userId,
                    bulleId = bulleId,
                    type = TransactionType.PAYMENT,
                    currency = "XOF",
                    userEmail = userEmail,
                    userFirstName = userFirstName,
                    userLastName = userLastName,
                    userPhone = userPhone,
                    description = "Paiement TONTINE PRO"
                )

                val result = when (method) {
                    PaymentMethod.FLUTTERWAVE -> paymentService.initiateFlutterwavePayment(paymentRequest)
                    PaymentMethod.PAYSTACK -> paymentService.initiatePaystackPayment(paymentRequest)
                    else -> Result.Error("Méthode de paiement non supportée")
                }

                when (result) {
                    is Result.Success -> {
                        _paymentState.value = PaymentState.PaymentUrlGenerated(
                            result.data.paymentUrl ?: ""
                        )
                    }
                    is Result.Error -> {
                        _error.value = result.message
                        _paymentState.value = PaymentState.Failed
                    }
                }

            } catch (e: Exception) {
                _error.value = e.message ?: "Erreur lors de l'initiation du paiement"
                _paymentState.value = PaymentState.Failed
            } finally {
                _loading.value = false
            }
        }
    }

    fun verifyPayment(reference: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _paymentState.value = PaymentState.Verifying

                val result = paymentService.verifyPayment(reference)
                when (result) {
                    is Result.Success -> {
                        _paymentState.value = PaymentState.Success(result.data)
                    }
                    is Result.Error -> {
                        _error.value = result.message
                        _paymentState.value = PaymentState.Failed
                    }
                }

            } catch (e: Exception) {
                _error.value = e.message ?: "Erreur lors de la vérification"
                _paymentState.value = PaymentState.Failed
            } finally {
                _loading.value = false
            }
        }
    }

    fun resetPaymentState() {
        _paymentState.value = PaymentState.Idle
        _error.value = null
    }

    fun clearError() {
        _error.value = null
    }
}