package com.tontinepro.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.tontinepro.utils.formatCurrency

@Parcelize
data class FeteEvent(
    val id: String,
    val name: String,
    val month: Int,
    val year: Int,
    val price: Long = 4200,
    val isActive: Boolean,
    val description: String?,
    val items: List<String> = listOf(
        "Poulet", "Sac de riz", "Cubes Maggi",
        "Sac de pommes de terre", "Petit pois", "Spaghettis"
    ),
    val createdAt: String
) : Parcelable {
    val formattedPrice: String get() = "${price.formatCurrency()} FCFA"
    val monthName: String get() = getMonthName(month)

    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "Janvier"
            2 -> "Février"
            3 -> "Mars"
            4 -> "Avril"
            5 -> "Mai"
            6 -> "Juin"
            7 -> "Juillet"
            8 -> "Août"
            9 -> "Septembre"
            10 -> "Octobre"
            11 -> "Novembre"
            12 -> "Décembre"
            else -> "Mois inconnu"
        }
    }
}