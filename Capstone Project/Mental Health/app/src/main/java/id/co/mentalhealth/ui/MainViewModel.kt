package id.co.mentalhealth.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.co.mentalhealth.data.pref.UserModel
import id.co.mentalhealth.ui.auth.AuthRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AuthRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun updateUserName(newName: String) {
        viewModelScope.launch {
            repository.updateUserName(newName)
        }
    }

    fun updatePhoto(newUrl: String) {
        viewModelScope.launch {
            repository.updatePhoto(newUrl)
        }
    }

}