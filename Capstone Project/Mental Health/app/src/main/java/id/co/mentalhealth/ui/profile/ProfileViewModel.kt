package id.co.mentalhealth.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.co.mentalhealth.data.network.response.ChangePwResponse
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _resetPassword = MutableLiveData<Result<ChangePwResponse>>()
    val resetPassword: LiveData<Result<ChangePwResponse>> = _resetPassword



    suspend fun uploadImage(file: File) = repository.uploadImage(file)



    fun changePassword(newPassword: String){
        viewModelScope.launch {
            val result = repository.changePassword(newPassword)
            _resetPassword.value = result
        }

    }
}