package id.co.mentalhealth.ui.profile

import androidx.lifecycle.ViewModel
import java.io.File

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    fun uploadImage(file: File) = repository.uploadImage(file)
}