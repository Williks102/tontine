@AndroidEntryPoint
class InscriptionFragment : Fragment() {

    private var _binding: FragmentInscriptionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private var photoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInscriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
        setupCamera()
    }

    private fun setupViews() {
        // Configuration des spinners de formules
        setupFormuleSpinner()

        // Listeners des boutons
        binding.btnTakePhoto.setOnClickListener { takePhoto() }
        binding.btnRegister.setOnClickListener { register() }

        // Configuration du date picker
        binding.etDateNaissance.setOnClickListener { showDatePicker() }
    }

    private fun setupFormuleSpinner() {
        val formules = FormuleType.values().filter { !it.isAdminOnly }
        val adapter = FormuleSpinnerAdapter(requireContext(), formules)
        binding.spinnerFormule.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Loading -> {
                    binding.progressBar.setVisible(true)
                    binding.btnRegister.isEnabled = false
                }
                is AuthState.RegistrationSuccess -> {
                    binding.progressBar.setVisible(false)
                    findNavController().navigate(
                        R.id.action_inscriptionFragment_to_verificationFragment
                    )
                }
                is AuthState.Error -> {
                    binding.progressBar.setVisible(false)
                    binding.btnRegister.isEnabled = true
                    requireContext().showToast(state.message)
                }
                else -> {
                    binding.progressBar.setVisible(false)
                    binding.btnRegister.isEnabled = true
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                requireContext().showToast(it)
            }
        }
    }

    private fun setupCamera() {
        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                photoUri?.let { uri ->
                    binding.ivPhoto.setImageURI(uri)
                    binding.ivPhoto.setVisible(true)
                    binding.btnTakePhoto.text = "Modifier la photo"
                }
            }
        }
    }

    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            return
        }

        val photoFile = File(
            requireContext().cacheDir,
            "photo_${System.currentTimeMillis()}.jpg"
        )

        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photoFile
        )

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }

        cameraLauncher.launch(intent)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.etDateNaissance.setText(selectedDate)
            },
            year - 18, // Date par défaut: il y a 18 ans
            month,
            day
        ).apply {
            // Limiter l'âge maximum à 80 ans et minimum à 18 ans
            datePicker.maxDate = System.currentTimeMillis() - (18 * 365 * 24 * 60 * 60 * 1000L)
            datePicker.minDate = System.currentTimeMillis() - (80 * 365 * 24 * 60 * 60 * 1000L)
        }.show()
    }

    private fun register() {
        if (!validateForm()) return

        val formule = binding.spinnerFormule.selectedItem as FormuleType

        val registrationData = RegistrationData(
            nom = binding.etNom.text.toString().trim(),
            prenom = binding.etPrenom.text.toString().trim(),
            email = binding.etEmail.text.toString().trim(),
            phone = binding.etPhone.text.toString().trim().takeIf { it.isNotEmpty() },
            dateNaissance = binding.etDateNaissance.text.toString(),
            photo = photoUri?.toString(),
            formule = formule,
            parrainCode = binding.etParrainCode.text.toString().trim().takeIf { it.isNotEmpty() }
        )

        viewModel.register(registrationData)
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (binding.etNom.text.toString().trim().isEmpty()) {
            binding.etNom.error = "Le nom est requis"
            isValid = false
        }

        if (binding.etPrenom.text.toString().trim().isEmpty()) {
            binding.etPrenom.error = "Le prénom est requis"
            isValid = false
        }

        if (binding.etEmail.text.toString().trim().isEmpty()) {
            binding.etEmail.error = "L'email est requis"
            isValid = false
        }

        if (binding.etDateNaissance.text.toString().isEmpty()) {
            binding.etDateNaissance.error = "La date de naissance est requise"
            isValid = false
        }

        if (photoUri == null) {
            requireContext().showToast("Veuillez prendre une photo")
            isValid = false
        }

        if (!binding.cbConditions.isChecked) {
            requireContext().showToast("Vous devez accepter les conditions générales")
            isValid = false
        }

        return isValid
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            } else {
                requireContext().showToast("Permission caméra requise")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
    }
}
