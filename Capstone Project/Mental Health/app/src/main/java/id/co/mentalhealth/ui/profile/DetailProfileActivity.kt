package id.co.mentalhealth.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.co.mentalhealth.R
import id.co.mentalhealth.data.network.ResultState
import id.co.mentalhealth.databinding.ActivityDetailProfileBinding
import id.co.mentalhealth.ui.MainViewModel
import com.bumptech.glide.Glide
import id.co.mentalhealth.ui.auth.AuthViewModelFactory
import kotlinx.coroutines.launch

class DetailProfileActivity : AppCompatActivity() {
    private var currentImageUri: Uri? = null

    private val profileViewModel by viewModels<ProfileViewModel> {
        ProfileViewModelFactory.getInstance(this)
    }
    private val mainViewModel by viewModels<MainViewModel> {
        AuthViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailProfileBinding

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val genderOptions = resources.getStringArray(R.array.simple_items)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genderOptions)
        binding.edtJeniskelamin.setAdapter(adapter)

        showImage()
        setupInfoUser()

        binding.btnBack.setOnClickListener { finish() }
        binding.btnSimpan.setOnClickListener { updateProfile() }
        binding.fabProfileImage.setOnClickListener {
            if (!allPermissionsGranted()) {
                requestPermissionLauncher.launch(REQUIRED_PERMISSION)
            }
            showBottomSheet()
        }
    }

    private fun updateProfile() {
        AlertDialog.Builder(this).apply {
            setTitle("Hey!")
            setMessage("Yakin ingin menyimpan perubahan?")
            setPositiveButton("Ya") { _, _ ->
                lifecycleScope.launch { actionUpdateProfile() }
            }
            setNegativeButton("Batal") { _, _ ->
                setupInfoUser()
                binding.root.clearFocus()
            }
            create()
            show()
        }
    }

    private fun actionUpdateProfile() {
        val fullName = binding.edtNamalengkap.text.toString().trim()
        val username = binding.edtUsername.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val age = binding.edtUmur.text.toString().trim()
        val gender = binding.edtJeniskelamin.text.toString().trim()

        when {
            fullName.isEmpty() || username.isEmpty() || email.isEmpty() || age.isEmpty() || gender.isEmpty() -> {
                AlertDialog.Builder(this).apply {
                    setTitle("Aduh!")
                    setMessage("Isi semua kolom dengan lengkap")
                    setPositiveButton("OK", null)
                    create()
                    show()
                }
                if (fullName.isEmpty()) {
                    binding.edtNamalengkap.error = "Nama lengkap harus diisi"
                }
                if (fullName.length < 5) {
                    binding.edtNamalengkap.error =
                        "Nama lengkap minimal 5 karakter, saat ini hanya ${fullName.length} karakter"
                }
                if (username.isEmpty()) {
                    binding.edtUsername.error = "Username tidak boleh kosong"
                }
                if (username.length < 5) {
                    binding.edtUsername.error =
                        "Username minimal 6 karakter, saat ini hanya ${username.length} karakter"
                }
                if (gender.isEmpty()) {
                    binding.edtJeniskelamin.error = "Jenis kelamin harus diisi"
                }
                if (email.isEmpty()) {
                    binding.edtEmail.error = "Email tidak boleh kosong"
                }
                if (age.isEmpty()) {
                    binding.edtUmur.error = "Umur harus diisi"
                }
            }

            else -> {
                lifecycleScope.launch {
                    profileViewModel.editProfile(fullName, username, email, gender, age)
                        .observe(this@DetailProfileActivity) { response ->
                            if (response != null) {
                                when (response) {
                                    is ResultState.Loading -> {
                                        showLoading(true)
                                    }

                                    is ResultState.Success -> {
                                        AlertDialog.Builder(this@DetailProfileActivity).apply {
                                            setTitle("Yeah!")
                                            setMessage("Profile berhasil diupdate nih.")
                                            setPositiveButton("Lanjut") { dialog, _ ->
                                                dialog.dismiss()
                                                finish()
                                            }
                                            create()
                                            show()
                                        }
                                        showLoading(false)
                                        mainViewModel.updateUserName(fullName)
                                    }

                                    is ResultState.Error -> {
                                        AlertDialog.Builder(this@DetailProfileActivity).apply {
                                            setTitle("Hmm")
                                            setMessage("Terjadi Kesalahan \"${response.error}\" data gagal diupdate.")
                                            setPositiveButton("Coba lagi") { _, _ ->
                                                lifecycleScope.launch { updateProfile() }
                                            }
                                            setNegativeButton("Batal", null)
                                            create()
                                            show()
                                        }
                                        showLoading(false)
                                    }
                                }
                            }
                        }
                }
            }
        }
    }

    private fun setupInfoUser() {
        lifecycleScope.launch {
            profileViewModel.getProfile()
                .observe(this@DetailProfileActivity) { response ->
                    if (response != null) {
                        when (response) {
                            is ResultState.Loading ->
                                showLoading(true)

                            is ResultState.Success -> {
                                binding.edtNamalengkap.setText(response.data.data!!.fullName)
                                binding.edtUsername.setText(response.data.data.username)
                                binding.edtEmail.setText(response.data.data.email)
                                binding.edtUmur.setText(response.data.data.age.toString())
                                binding.edtJeniskelamin.setText(response.data.data.gender, false)
                                showLoading(false)
                            }

                            is ResultState.Error -> {
                                AlertDialog.Builder(this@DetailProfileActivity)
                                    .apply {
                                        setTitle("Hmm")
                                        setMessage("Terjadi Kesalahan \"${response.error}\" data gagal ditampilkan.")
                                        setPositiveButton("OK", null)
                                        create()
                                        show()
                                    }
                                showLoading(false)
                            }
                        }
                    } else {
                        AlertDialog.Builder(this@DetailProfileActivity).apply {
                            setTitle("Hmm")
                            setMessage("Terjadi Kesalahan data gagal ditampilkan.")
                            setPositiveButton("OK", null)
                            create()
                            show()
                        }
                        showLoading(false)
                    }
                }
        }
    }

    private fun showBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_profile)
        bottomSheetDialog.show()

        val fabCamera =
            bottomSheetDialog.findViewById<FloatingActionButton>(R.id.fab_camera)
        val fabGallery =
            bottomSheetDialog.findViewById<FloatingActionButton>(R.id.fab_gallery)
        val btnUpload = bottomSheetDialog.findViewById<Button>(R.id.btn_upload)

        fabCamera?.setOnClickListener { startCamera() }
        fabGallery?.setOnClickListener { startGallery() }
        btnUpload?.setOnClickListener {
            lifecycleScope.launch {
                uploadImage()
                bottomSheetDialog.dismiss()
            }
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let {
            launcherIntentCamera.launch(it)
        } ?: run {
            Toast.makeText(this, "Gagal memulai kamera", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(
                this,
                "Tidak ada gambar yang terpilih",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showImage() {
        mainViewModel.getSession().observe(this) { user ->
            if (currentImageUri != null) {
                binding.profileImage.setImageURI(currentImageUri)
            } else if (user.photo != null) {
                Glide.with(binding.root.context)
                    .load(user.photo)
                    .placeholder(R.drawable.foto_profile)
                    .error(R.drawable.ic_profile)
                    .into(binding.profileImage)
            } else {
                binding.profileImage.setImageResource(R.drawable.foto_profile)
            }
        }

        if (currentImageUri != null) {
            binding.profileImage.setImageURI(currentImageUri)
        } else {
            binding.profileImage.setImageResource(R.drawable.foto_profile)
        }
    }

    private suspend fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")

            profileViewModel.uploadImage(imageFile).observe(this) { response ->
                if (response != null) {
                    when (response) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            AlertDialog.Builder(this).apply {
                                setTitle("Yeah!")
                                setMessage("Foto berhasil diupload nih.")
                                setPositiveButton("Lanjut", null)
                                create()
                                show()
                            }
                            showLoading(false)
                            mainViewModel.updatePhoto(response.data.url.toString())
                        }

                        is ResultState.Error -> {
                            AlertDialog.Builder(this).apply {
                                setTitle("Hmm")
                                setMessage("Foto gagal diupload dengan error ${response.error}. Mau coba lagi?")
                                setPositiveButton("Coba lagi") { _, _ ->
                                    lifecycleScope.launch { uploadImage() }
                                }
                                setNegativeButton("Batal", null)
                                create()
                                show()
                            }
                            showLoading(false)
                        }
                    }
                } else {
                    AlertDialog.Builder(this).apply {
                        setTitle("Hmm")
                        setMessage("Foto gagal diupload.")
                        setPositiveButton("OK", null)
                        create()
                        show()
                    }
                    showLoading(false)
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}