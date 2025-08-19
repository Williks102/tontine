data class Parrainage(
    val id: String,
    val parrainId: String,
    val filleulId: String,
    val formule: FormuleType,
    val gain: Long,
    val isActive: Boolean,
    val createdAt: String,
    val filleul: User? = null
) {
    val formattedGain: String get() = "${gain.formatCurrency()} FCFA"
}