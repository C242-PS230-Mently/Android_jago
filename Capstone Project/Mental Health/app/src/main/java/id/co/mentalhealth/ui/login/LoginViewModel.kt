package id.co.mentalhealth.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.co.mentalhealth.data.network.response.LoginResponse
import id.co.mentalhealth.data.network.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun login(identifier: String, password: String) {
            val client = ApiConfig.getApiService().login(identifier, password)
            client.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _loginResult.value = Result.success(it)
                            _errorMessage.value = null
                        } ?: run {
                            _errorMessage.value = "Data tidak ditemukan"
                        }
                    } else {
                        _errorMessage.value = "Email atau password salah"
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _errorMessage.value = "Gagal terhubung ke server: ${t.message}"
                }
            })
    }
}