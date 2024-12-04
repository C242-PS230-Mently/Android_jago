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
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
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
        showImage()
        setupInfoUser()

        if (ContextCompat.checkSelfPermission(
                this,
                REQUIRED_PERMISSION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSION)) {
                Toast.makeText(
                    this,
                    "Aplikasi membutuhkan akses kamera untuk mengambil foto",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                requestPermissionLauncher.launch(REQUIRED_PERMISSION)
            }
        }
        binding.btnBack.setOnClickListener { finish() }
        binding.fabProfileImage.setOnClickListener { showBottomSheet() }
    }

    private fun setupInfoUser() {
        lifecycleScope.launch {
            profileViewModel.getProfile().observe(this@DetailProfileActivity) { response ->
                if (response != null) {
                    when (response) {
                        is ResultState.Loading ->
                            showLoading(true)

                        is ResultState.Success -> {
                            binding.edtNamalengkap.setText(response.data.data!!.fullName)
                            binding.edtEmail.setText(response.data.data.email)
                            binding.edtUmur.setText(response.data.data.age.toString())
                            binding.edtJeniskelamin.setText(response.data.data.gender)
                            showLoading(false)
                        }

                        is ResultState.Error -> {
                            AlertDialog.Builder(this@DetailProfileActivity).apply {
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

        val fabCamera = bottomSheetDialog.findViewById<FloatingActionButton>(R.id.fab_camera)
        val fabGallery = bottomSheetDialog.findViewById<FloatingActionButton>(R.id.fab_gallery)
        val btnUpload = bottomSheetDialog.findViewById<Button>(R.id.btn_upload)

        fabCamera?.setOnClickListener { startCamera() }
        fabGallery?.setOnClickListener { startGallery() }
        btnUpload?.setOnClickListener { lifecycleScope.launch { uploadImage() } }
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
            Toast.makeText(this, "Tidak ada gambar yang terpilih", Toast.LENGTH_SHORT).show()
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
                            mainViewModel.getSession().observe(this) { user ->
                                user.photo = response.data.url
                            }
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
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}