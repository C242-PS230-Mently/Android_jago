package id.co.mentalhealth.ui.registrasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.co.mentalhealth.data.network.response.RegisterResponse
import id.co.mentalhealth.data.network.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistViewModel: ViewModel() {

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> get() = _registerResult

    fun register(
        fullName: String,
        email: String,
        age: String,
        gender: String,
        username: String,
        password: String
    ) {
        val call = ApiConfig.getApiService().register(fullName, email, age, gender, username, password)
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _registerResult.postValue(Result.success(it))
                    } ?: _registerResult.postValue(Result.failure(Throwable("Response body is null")))
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    _registerResult.postValue(Result.failure(Throwable("Registration failed: $errorBody")))
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registerResult.postValue(Result.failure(t))
            }
        })
    }
}