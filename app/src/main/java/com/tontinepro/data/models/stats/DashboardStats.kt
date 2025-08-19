dpackage com.tontinepro.data.models.stats

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.tontinepro.utils.formatCurrency

@Parcelize
data class DashboardStats(
    val totalPaid: Long,
    val totalEarned: Long,
    val parrainageEarnings: Long,
    val activeParrainages: Int,
    val cyclesRemaining: Int,
    val bulleProgress: Float
) : Parcelable {
    val formattedTotalPaid: String get() = "${totalPaid.formatCurrency()} FCFA"
    val formattedTotalEarned: String get() = "${totalEarned.formatCurrency()} FCFA"
    val formattedParrainageEarnings: String get() = "${parrainageEarnings.formatCurrency()} FCFA"
    val totalBalance: Long get() = totalEarned - totalPaid + parrainageEarnings
    val formattedTotalBalance: String get() = "${totalBalance.formatCurrency()} FCFA"
    val progressPercentage: Int get() = (bulleProgress * 100).toInt()

    val hasEarnings: Boolean get() = totalEarned > 0 || parrainageEarnings > 0
    val hasPayments: Boolean get() = totalPaid > 0
    val isPositiveBalance: Boolean get() = totalBalance >= 0
}

