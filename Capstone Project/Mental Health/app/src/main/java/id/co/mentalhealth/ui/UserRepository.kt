package id.co.mentalhealth.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.co.mentalhealth.data.UserPreferences
import id.co.mentalhealth.data.network.response.LoginResponse
import id.co.mentalhealth.data.network.response.RegisterResponse
import id.co.mentalhealth.data.network.retrofit.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val apiService: ApiService, private val userPreferences: UserPreferences) {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> get() = _registerResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun login(identifier: String, password: String) {
        val client = apiService.login(identifier, password)
        Log.d("UserRepository", "Memulai proses login dengan identifier: $identifier")
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _loginResult.value = Result.success(it)
                        _errorMessage.value = null
                        Log.d("UserRepository", "Login berhasil: ${it}")

                        CoroutineScope(Dispatchers.IO).launch {
                            userPreferences.saveToken(it.accessToken)
                        }

                    } ?: run {
                        _errorMessage.value = "Data tidak ditemukan"
                        Log.e("UserRepository", "Login gagal: Data body respons null")
                    }
                } else {
                    _errorMessage.value = "Email atau password salah"
                    Log.e("UserRepository", "Login gagal: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _errorMessage.value = "Gagal terhubung ke server: ${t.message}"
                Log.e("UserRepository", "Login gagal: ${t.message}", t)
            }
        })
    }

    fun register(
        fullName: String,
        email: String,
        age: String,
        gender: String,
        username: String,
        password: String
    ) {
        val call = apiService.register(fullName, email, age, gender, username, password)
        Log.d("UserRepository", "Memulai proses registrasi untuk username: $username")

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _registerResult.postValue(Result.success(it))
                        Log.d("UserRepository", "Registrasi berhasil: ${it}")
                    } ?: run {
                        _registerResult.postValue(Result.failure(Throwable("Response body is null")))
                        Log.e("UserRepository", "Registrasi gagal: Body respons null")
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    _registerResult.postValue(Result.failure(Throwable("Registration failed: $errorBody")))
                    Log.e("UserRepository", "Registrasi gagal: ${response.code()} - $errorBody")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registerResult.postValue(Result.failure(t))
                Log.e("UserRepository", "Registrasi gagal: ${t.message}", t)
            }
        })
    }

}

//    fun getQuestions() {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = apiService.getQuestions()
//
//                _questions.postValue(Result.success(response))
//            } catch (e: Exception) {
//                _questions.postValue(Result.failure(e))
//                _errorMessage.postValue("Gagal mengambil data pertanyaan: ${e.message}")
//                Log.e("UserRepository", "Error getQuestions: ${e.message}", e)
//            }
//        }
//    }

