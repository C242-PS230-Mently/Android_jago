package id.co.mentalhealth.di

import android.content.Context
import id.co.mentalhealth.data.network.retrofit.ApiConfig
import id.co.mentalhealth.data.pref.UserPreferences
import id.co.mentalhealth.data.pref.dataStore
import id.co.mentalhealth.ui.QuestionRepository
import id.co.mentalhealth.ui.auth.AuthRepository
import id.co.mentalhealth.ui.profile.ProfileRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): AuthRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(apiService, pref)
    }

    fun provideQuestRepository(context: Context): QuestionRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user =  runBlocking { pref.getSession().first() }
        val token = user.token

        if (token.isNullOrEmpty()) {
            throw Exception("User token is missing.")
        }
        val apiService = ApiConfig.getApiService(user.token)
        return QuestionRepository.getInstance(apiService, pref)
    }

    fun provideProfileRepository(context: Context): ProfileRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return ProfileRepository.getInstance(apiService)
    }
}