// data/models/dto/PaymentRequest.kt
package com.tontinepro.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.tontinepro.data.models.enums.PaymentMethod
import com.tontinepro.data.models.enums.TransactionType
import com.tontinepro.utils.formatCurrency

@Parcelize
data class PaymentRequest(
    val amount: Long,
    val method: PaymentMethod,
    val userId: String,
    val bulleId: String?,
    val type: TransactionType,
    val currency: String = "XOF",
    val userEmail: String,
    val userFirstName: String,
    val userLastName: String,
    val userPhone: String,
    val description: String?
) : Parcelable {
    val formattedAmount: String get() = "${amount.formatCurrency()} $currency"
    val userFullName: String get() = "$userFirstName $userLastName"
}
