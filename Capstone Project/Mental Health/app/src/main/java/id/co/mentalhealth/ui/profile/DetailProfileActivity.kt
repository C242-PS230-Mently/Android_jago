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
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.co.mentalhealth.R
import id.co.mentalhealth.data.network.ResultState
import id.co.mentalhealth.databinding.ActivityDetailProfileBinding
import id.co.mentalhealth.ui.MainViewModel
import id.co.mentalhealth.ui.auth.AuthViewModelFactory
import kotlinx.coroutines.launch

class DetailProfileActivity : AppCompatActivity() {
    private var currentImageUri: Uri? = null

    private val profileViewModel by viewModels<ProfileViewModel> {
        ProfileViewModelFactory.getInstance(this)
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

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnBack.setOnClickListener { finish() }
        binding.fabProfileImage.setOnClickListener { showBottomSheet() }
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
        launcherIntentCamera.launch(currentImageUri!!)
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
            Toast.makeText(this, "Pilih gambar dulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImage() {
        if (currentImageUri != null) {
            binding.profileImage.setImageURI(currentImageUri)
        } else {
            binding.profileImage.setImageResource(R.drawable.foto_profile)
        }
    }

    private fun uploadImage() {
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
                        }

                        is ResultState.Error -> {
                            AlertDialog.Builder(this).apply {
                                setTitle("Hmm")
                                setMessage("Foto gagal diupload dengan error ${response.error}")
                                setPositiveButton("OK", null)
                                create()
                                show()
                            }
                            showLoading(false)
                        }
                    }
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