package id.co.mentalhealth.data

import android.content.Context
import android.util.Log
import id.co.mentalhealth.data.network.retrofit.ApiConfig
import id.co.mentalhealth.ui.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
    val pref = UserPreferences.getInstance(context.dataStore)
    val user = runBlocking { pref.getUser().first() }
        Log.d("Injection", "Token ditemukan: ${user.token}")
    val apiService = ApiConfig.getApiService(user.token)
    return UserRepository.getInstance(apiService, pref)
}
}