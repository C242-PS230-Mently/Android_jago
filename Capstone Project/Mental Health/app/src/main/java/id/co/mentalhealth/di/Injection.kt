package id.co.mentalhealth.di

import android.content.Context
import id.co.mentalhealth.data.pref.UserPreferences
import id.co.mentalhealth.data.pref.dataStore
import id.co.mentalhealth.data.network.retrofit.ApiConfig
import id.co.mentalhealth.ui.auth.AuthRepository

object Injection {
    fun provideRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreferences.getInstance(context.dataStore)
        return AuthRepository.getInstance(apiService, pref)
    }
}