// data/models/enums/TontineEnums.kt
package com.tontinepro.data.models.enums

import android.content.Context
import com.tontinepro.R
import com.tontinepro.utils.formatCurrency

enum class FormuleType(val displayName: String, val montant: Long, val gain: Long) {
    MINI("Mini Pro", 5500, 100000),
    PRO("Pro", 11000, 200000),
    PROMAX("Pro Max", 22000, 400000),
    GOLD("Pro Gold", 55000, 1000000);

    val formattedMontant: String get() = "${montant.formatCurrency()} FCFA"
    val formattedGain: String get() = "${gain.formatCurrency()} FCFA"
    val isAdminOnly: Boolean get() = this == PROMAX || this == GOLD
}

enum class UserStatus {
    PENDING_VERIFICATION,
    VERIFIED,
    ACTIVE,
    SUSPENDED,
    BLOCKED
}

enum class BulleStatus {
    ACTIVE,
    COMPLETED,
    PAUSED,
    CANCELLED
}

enum class TransactionStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELLED
}

enum class TransactionType {
    PAYMENT,
    WITHDRAWAL,
    BONUS,
    PENALTY
}

enum class PaymentMethod {
    FLUTTERWAVE,
    PAYSTACK,
    MOBILE_MONEY,
    BANK_TRANSFER
}

// =============================================================================
// AUTRES ENUMS POUR VOTRE PROJET
// =============================================================================

/**
 * Statuts des bulles de tontine
 */
enum class BulleStatus(val displayName: String, val colorRes: Int) {
    ACTIVE(
        displayName = "Active",
        colorRes = R.color.tontine_green
    ),
    COMPLETED(
        displayName = "Terminée",
        colorRes = R.color.tontine_blue
    ),
    PAUSED(
        displayName = "En pause",
        colorRes = R.color.tontine_orange
    ),
    CANCELLED(
        displayName = "Annulée",
        colorRes = R.color.tontine_red
    );

    val isOperational: Boolean get() = this == ACTIVE
    val canAcceptPayments: Boolean get() = this == ACTIVE
}

/**
 * Statuts des cycles de tontine
 */
enum class CycleStatus(val displayName: String, val colorRes: Int) {
    PENDING(
        displayName = "En attente",
        colorRes = R.color.tontine_orange
    ),
    ACTIVE(
        displayName = "En cours",
        colorRes = R.color.tontine_blue
    ),
    COMPLETED(
        displayName = "Terminé",
        colorRes = R.color.tontine_green
    ),
    CANCELLED(
        displayName = "Annulé",
        colorRes = R.color.tontine_red
    );

    val isInProgress: Boolean get() = this == ACTIVE
    val isFinished: Boolean get() = this == COMPLETED || this == CANCELLED
}

/**
 * Types de transactions
 */
enum class TransactionType(
    val displayName: String,
    val iconRes: Int,
    val colorRes: Int
) {
    PAYMENT(
        displayName = "Paiement",
        iconRes = R.drawable.ic_payment,
        colorRes = R.color.tontine_green
    ),
    WITHDRAWAL(
        displayName = "Retrait",
        iconRes = R.drawable.ic_withdrawal,
        colorRes = R.color.tontine_blue
    ),
    BONUS(
        displayName = "Bonus parrainage",
        iconRes = R.drawable.ic_bonus,
        colorRes = R.color.tontine_gold
    ),
    PENALTY(
        displayName = "Pénalité",
        iconRes = R.drawable.ic_penalty,
        colorRes = R.color.tontine_red
    ),
    FETE_PAYMENT(
        displayName = "Paiement Fête",
        iconRes = R.drawable.ic_party,
        colorRes = R.color.tontine_purple
    );

    val isIncome: Boolean get() = this == WITHDRAWAL || this == BONUS
    val isExpense: Boolean get() = this == PAYMENT || this == PENALTY || this == FETE_PAYMENT
}

/**
 * Méthodes de paiement supportées
 */
enum class PaymentMethod(
    val displayName: String,
    val iconRes: Int,
    val isEnabled: Boolean = true
) {
    FLUTTERWAVE(
        displayName = "Flutterwave",
        iconRes = R.drawable.ic_flutterwave,
        isEnabled = true
    ),
    PAYSTACK(
        displayName = "Paystack",
        iconRes = R.drawable.ic_paystack,
        isEnabled = true
    ),
    MOBILE_MONEY(
        displayName = "Mobile Money",
        iconRes = R.drawable.ic_mobile_money,
        isEnabled = true
    ),
    BANK_TRANSFER(
        displayName = "Virement bancaire",
        iconRes = R.drawable.ic_bank_transfer,
        isEnabled = false
    );

    companion object {
        fun getEnabledMethods(): List<PaymentMethod> = values().filter { it.isEnabled }
    }
}

/**
 * Statuts des transactions
 */
enum class TransactionStatus(
    val displayName: String,
    val colorRes: Int,
    val iconRes: Int
) {
    PENDING(
        displayName = "En attente",
        colorRes = R.color.tontine_orange,
        iconRes = R.drawable.ic_pending
    ),
    PROCESSING(
        displayName = "En cours de traitement",
        colorRes = R.color.tontine_blue,
        iconRes = R.drawable.ic_processing
    ),
    COMPLETED(
        displayName = "Terminée",
        colorRes = R.color.tontine_green,
        iconRes = R.drawable.ic_completed
    ),
    FAILED(
        displayName = "Échouée",
        colorRes = R.color.tontine_red,
        iconRes = R.drawable.ic_failed
    ),
    CANCELLED(
        displayName = "Annulée",
        colorRes = R.color.tontine_red,
        iconRes = R.drawable.ic_cancelled
    );

    val isSuccessful: Boolean get() = this == COMPLETED
    val isFinal: Boolean get() = this == COMPLETED || this == FAILED || this == CANCELLED
    val canRetry: Boolean get() = this == FAILED
}

/**
 * Types de notifications
 */
enum class NotificationType(
    val displayName: String,
    val iconRes: Int,
    val priority: Int // 1 = haute, 2 = normale, 3 = basse
) {
    PAYMENT_REMINDER(
        displayName = "Rappel de paiement",
        iconRes = R.drawable.ic_reminder,
        priority = 1
    ),
    PAYMENT_RECEIVED(
        displayName = "Paiement reçu",
        iconRes = R.drawable.ic_payment_received,
        priority = 1
    ),
    TURN_NOTIFICATION(
        displayName = "Votre tour est arrivé",
        iconRes = R.drawable.ic_turn,
        priority = 1
    ),
    SYSTEM_UPDATE(
        displayName = "Mise à jour système",
        iconRes = R.drawable.ic_system,
        priority = 2
    ),
    PROMOTIONAL(
        displayName = "Promotion",
        iconRes = R.drawable.ic_promo,
        priority = 3
    ),
    PARRAINAGE_BONUS(
        displayName = "Bonus de parrainage",
        iconRes = R.drawable.ic_bonus,
        priority = 2
    ),
    FETE_AVAILABLE(
        displayName = "Fête disponible",
        iconRes = R.drawable.ic_party,
        priority = 2
    );

    val isHighPriority: Boolean get() = priority == 1
    val channelId: String get() = when (priority) {
        1 -> "HIGH_PRIORITY_CHANNEL"
        2 -> "NORMAL_PRIORITY_CHANNEL"
        else -> "LOW_PRIORITY_CHANNEL"
    }
}

/**
 * Types de messages du chat support
 */
enum class MessageType(val displayName: String) {
    USER("Message utilisateur"),
    SUPPORT("Réponse support"),
    SYSTEM("Message système");

    val isFromUser: Boolean get() = this == USER
    val isFromSupport: Boolean get() = this == SUPPORT
}

/**
 * Mois de l'année pour les événements fête
 */
enum class FeteMonth(val displayName: String, val monthNumber: Int) {
    JANVIER("Janvier", 1),
    DECEMBRE("Décembre", 12),
    TABASKI("Tabaski", 0); // 0 = mois variable selon calendrier islamique

    val isFixed: Boolean get() = monthNumber > 0

    companion object {
        fun getFixedMonths(): List<FeteMonth> = values().filter { it.isFixed }
        fun getVariableMonths(): List<FeteMonth> = values().filterNot { it.isFixed }
    }
}

/**
 * Permissions requises dans l'application
 */
enum class AppPermission(
    val permission: String,
    val displayName: String,
    val description: String,
    val isRequired: Boolean
) {
    CAMERA(
        permission = android.Manifest.permission.CAMERA,
        displayName = "Caméra",
        description = "Nécessaire pour prendre votre photo d'inscription",
        isRequired = true
    ),
    READ_CONTACTS(
        permission = android.Manifest.permission.READ_CONTACTS,
        displayName = "Contacts",
        description = "Pour inviter facilement vos contacts",
        isRequired = false
    ),
    WRITE_EXTERNAL_STORAGE(
        permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        displayName = "Stockage",
        description = "Pour sauvegarder vos documents",
        isRequired = false
    ),
    CALL_PHONE(
        permission = android.Manifest.permission.CALL_PHONE,
        displayName = "Téléphone",
        description = "Pour contacter le support directement",
        isRequired = false
    );

    val isCritical: Boolean get() = isRequired
}

/**
 * Configuration des gains de parrainage selon les formules
 */
enum class ParrainageGain(val formule: FormuleType, val gain: Long) {
    MINI_GAIN(FormuleType.MINI, 500),
    PRO_GAIN(FormuleType.PRO, 1000),
    PROMAX_GAIN(FormuleType.PROMAX, 2000),
    GOLD_GAIN(FormuleType.GOLD, 5000);

    val formattedGain: String get() = "${gain.formatCurrency()} FCFA"

    companion object {
        fun getGainForFormule(formule: FormuleType): Long {
            return values().find { it.formule == formule }?.gain ?: 0
        }

        const val BONUS_10_FILLEULS = 10000L // Bonus pour 10 filleuls actifs
        val formattedBonus10Filleuls: String get() = "${BONUS_10_FILLEULS.formatCurrency()} FCFA"
    }
}