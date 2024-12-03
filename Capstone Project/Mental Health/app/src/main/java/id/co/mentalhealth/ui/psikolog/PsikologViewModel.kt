package id.co.mentalhealth.ui.psikolog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.co.mentalhealth.data.network.response.DoctorsResponse
import kotlinx.coroutines.launch

class PsikologViewModel(private val psikologRepository: PsikologRepository) : ViewModel() {

    private val _doctor= MutableLiveData<Result<DoctorsResponse>>()
    val doctor: LiveData<Result<DoctorsResponse>> get() = _doctor

    fun getDoctor(){
        viewModelScope.launch {
            val result = psikologRepository.getPsikolog()
            _doctor.value = result
        }
    }
}