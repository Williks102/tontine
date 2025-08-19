// data/models/states/PaymentState.kt
package com.tontinepro.data.models.states

import com.tontinepro.data.models.Transaction

sealed class PaymentState {
    object Idle : PaymentState()
    object Initiating : PaymentState()
    data class PaymentUrlGenerated(val url: String) : PaymentState()
    object Verifying : PaymentState()
    data class Success(val transaction: Transaction) : PaymentState()
    object Failed : PaymentState()
}

