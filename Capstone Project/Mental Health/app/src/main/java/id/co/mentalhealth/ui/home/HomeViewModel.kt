package id.co.mentalhealth.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.co.mentalhealth.data.network.response.WorkshopResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _workShop= MutableLiveData<Result<WorkshopResponse>>()
    val workShop: LiveData<Result<WorkshopResponse>> get() = _workShop

    fun getWorkshop(){
        viewModelScope.launch {
            val result = homeRepository.getWorkshop()
            _workShop.value = result
        }
    }

}