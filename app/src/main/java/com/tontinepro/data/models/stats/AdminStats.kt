// data/models/stats/AdminStats.kt
package com.tontinepro.data.models.stats

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.tontinepro.data.models.enums.FormuleType
import com.tontinepro.utils.formatCurrency

@Parcelize
data class AdminStats(
    val totalUsers: Int,
    val activeUsers: Int,
    val totalBulles: Int,
    val activeBulles: Int,
    val totalRevenue: Long,
    val bullesByFormule: Map<FormuleType, Int>
) : Parcelable {
    val formattedTotalRevenue: String get() = "${totalRevenue.format