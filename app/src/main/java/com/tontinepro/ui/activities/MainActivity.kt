@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupNavigation()
        setupBottomNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Configuration de la navigation avec la bottom navigation
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun setupBottomNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.inscriptionFragment,
                R.id.verificationFragment,
                R.id.loginFragment -> {
                    binding.bottomNavigation.setVisible(false)
                }
                else -> {
                    binding.bottomNavigation.setVisible(true)
                }
            }
        }
    }
}
