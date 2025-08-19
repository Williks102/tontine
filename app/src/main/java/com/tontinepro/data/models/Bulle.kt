data class Bulle(
    val id: String,
    val name: String,
    val formule: FormuleType,
    val memberCount: Int,
    val maxMembers: Int = 20,
    val currentCycle: Int,
    val status: BulleStatus,
    val members: List<BulleMember> = emptyList(),
    val createdAt: String,
    val updatedAt: String
) {
    val isComplete: Boolean get() = memberCount >= maxMembers
    val progress: Float get() = memberCount.toFloat() / maxMembers.toFloat()
}
