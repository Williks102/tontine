@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
        loadData()
    }

    private fun setupViews() {
        // Configuration du RecyclerView des transactions
        transactionAdapter = TransactionAdapter { transaction ->
            // Gérer le clic sur une transaction
            showTransactionDetails(transaction)
        }

        binding.rvTransactions.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }

        // Listeners des boutons
        binding.btnPayment.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_paymentFragment)
        }

        binding.btnParrainage.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_parrainageFragment)
        }
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let { updateUserInfo(it) }
        }

        viewModel.userBulle.observe(viewLifecycleOwner) { bulle ->
            updateBulleInfo(bulle)
        }

        viewModel.dashboardStats.observe(viewLifecycleOwner) { stats ->
            updateStatsDisplay(stats)
        }

        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            transactionAdapter.submitList(transactions.take(5)) // Afficher les 5 dernières
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.swipeRefresh.isRefreshing = loading
        }
    }

    private fun updateUserInfo(user: User) {
        binding.apply {
            tvUserName.text = user.fullName
            tvUserFormule.text = user.formule.displayName
            ivUserPhoto.loadImage(user.photo ?: "")

            // Afficher le statut de vérification
            when (user.status) {
                UserStatus.PENDING_VERIFICATION -> {
                    tvVerificationStatus.text = "Vérification en attente"
                    tvVerificationStatus.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.tontine_orange)
                    )
                }
                UserStatus.VERIFIED -> {
                    tvVerificationStatus.text = "Compte vérifié"
                    tvVerificationStatus.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.tontine_green)
                    )
                }
                else -> {
                    tvVerificationStatus.text = user.status.name
                    tvVerificationStatus.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.tontine_red)
                    )
                }
            }
        }
    }

    private fun updateBulleInfo(bulle: Bulle?) {
        binding.apply {
            if (bulle != null) {
                cardBulle.setVisible(true)
                tvBulleName.text = bulle.name
                tvBulleMembers.text = "${bulle.memberCount}/${bulle.maxMembers} membres"
                tvCurrentCycle.text = "Cycle ${bulle.currentCycle}"

                // Progress bar
                progressBulle.progress = (bulle.progress * 100).toInt()
                tvBulleProgress.text = "${(bulle.progress * 100).toInt()}%"

                // Position de l'utilisateur
                val userMember = bulle.members.find { it.userId == viewModel.user.value?.id }
                userMember?.let { member ->
                    tvUserPosition.text = "Votre position: ${member.position}"

                    // Statut de paiement
                    if (member.hasPaid) {
                        tvPaymentStatus.text = "Paiement effectué"
                        tvPaymentStatus.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.tontine_green)
                        )
                    } else {
                        tvPaymentStatus.text = "Paiement en attente"
                        tvPaymentStatus.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.tontine_orange)
                        )
                    }
                }

            } else {
                cardBulle.setVisible(false)
                cardNoBulle.setVisible(true)
            }
        }
    }

    private fun updateStatsDisplay(stats: DashboardStats) {
        binding.apply {
            tvTotalPaid.text = stats.formattedTotalPaid
            tvTotalEarned.text = stats.formattedTotalEarned
            tvParrainageEarnings.text = stats.formattedParrainageEarnings
            tvActiveParrainages.text = "${stats.activeParrainages} filleuls actifs"

            if (stats.cyclesRemaining > 0) {
                tvCyclesRemaining.text = "${stats.cyclesRemaining} cycles restants"
            } else {
                tvCyclesRemaining.text = "Votre tour est arrivé!"
                tvCyclesRemaining.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.tontine_green)
                )
            }
        }
    }

    private fun showTransactionDetails(transaction: Transaction) {
        // Créer un dialog avec les détails de la transaction
        val dialog = TransactionDetailsDialog(transaction)
        dialog.show(parentFragmentManager, "transaction_details")
    }

    private fun loadData() {
        // Récupérer l'ID utilisateur depuis les préférences ou la session
        val userId = getUserId() // À implémenter selon votre système d'auth
        viewModel.loadDashboardData(userId)
    }

    private fun getUserId(): String {
        // Implémentation pour récupérer l'ID utilisateur connecté
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("user_id", "") ?: ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
