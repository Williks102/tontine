@HiltViewModel
class AdminViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val bulleRepository: BulleRepository,
    private val transactionRepository: TransactionRepository,
    private val feteRepository: FeteRepository
) : ViewModel() {

    private val _adminStats = MutableLiveData<AdminStats>()
    val adminStats: LiveData<AdminStats> = _adminStats

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _bulles = MutableLiveData<List<Bulle>>()
    val bulles: LiveData<List<Bulle>> = _bulles

    private val _feteEvents = MutableLiveData<List<FeteEvent>>()
    val feteEvents: LiveData<List<FeteEvent>> = _feteEvents

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun loadAdminData() {
        viewModelScope.launch {
            try {
                _loading.value = true

                // Charger toutes les données admin
                val usersDeferred = async { userRepository.getAllUsers() }
                val bullesDeferred = async { bulleRepository.getAllBulles() }
                val transactionsDeferred = async { transactionRepository.getAllTransactions() }
                val feteEventsDeferred = async { feteRepository.getAllFeteEvents() }

                val users = usersDeferred.await()
                val bulles = bullesDeferred.await()
                val transactions = transactionsDeferred.await()
                val feteEvents = feteEventsDeferred.await()

                _users.value = users
                _bulles.value = bulles
                _feteEvents.value = feteEvents

                // Calculer les statistiques admin
                calculateAdminStats(users, bulles, transactions)

            } catch (e: Exception) {
                // Gérer l'erreur
            } finally {
                _loading.value = false
            }
        }
    }

    private fun calculateAdminStats(
        users: List<User>,
        bulles: List<Bulle>,
        transactions: List<Transaction>
    ) {
        val totalUsers = users.size
        val activeUsers = users.count { it.status == UserStatus.ACTIVE }
        val totalRevenue = transactions
            .filter { it.isSuccessful && it.type == TransactionType.PAYMENT }
            .sumOf { it.amount }

        val bullesByFormule = bulles.groupBy { it.formule }
        val activeBulles = bulles.count { it.status == BulleStatus.ACTIVE }

        _adminStats.value = AdminStats(
            totalUsers = totalUsers,
            activeUsers = activeUsers,
            totalBulles = bulles.size,
            activeBulles = activeBulles,
            totalRevenue = totalRevenue,
            bullesByFormule = bullesByFormule.mapValues { it.value.size }
        )
    }

    fun activateFormule(formule: FormuleType, isActive: Boolean) {
        viewModelScope.launch {
            try {
                val result = userRepository.updateFormuleStatus(formule, isActive)
                if (result.isSuccess) {
                    // Rafraîchir les données
                    loadAdminData()
                }
            } catch (e: Exception) {
                // Gérer l'erreur
            }
        }
    }

    fun activateFeteEvent(month: Int, year: Int, isActive: Boolean) {
        viewModelScope.launch {
            try {
                val result = feteRepository.updateFeteEventStatus(month, year, isActive)
                if (result.isSuccess) {
                    loadAdminData()
                }
            } catch (e: Exception) {
                // Gérer l'erreur
            }
        }
    }

    fun suspendUser(userId: String) {
        viewModelScope.launch {
            try {
                val result = userRepository.updateUserStatus(userId, UserStatus.SUSPENDED)
                if (result.isSuccess) {
                    loadAdminData()
                }
            } catch (e: Exception) {
                // Gérer l'erreur
            }
        }
    }
}
