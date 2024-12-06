package id.co.mentalhealth.di

import android.content.Context
import android.util.Log
import id.co.mentalhealth.data.network.retrofit.ApiConfig
import id.co.mentalhealth.data.pref.UserPreferences
import id.co.mentalhealth.data.pref.dataStore
import id.co.mentalhealth.ui.auth.AuthRepository
import id.co.mentalhealth.ui.home.HomeRepository
import id.co.mentalhealth.ui.profile.ProfileRepository
import id.co.mentalhealth.ui.psikolog.PsikologRepository
import id.co.mentalhealth.ui.quest.QuestionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun <T> provideRepository(context: Context, repositoryClass: Class<T>): T {
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()

        return when(repositoryClass) {
            AuthRepository::class.java -> AuthRepository.getInstance(apiService, pref)
            ProfileRepository::class.java -> ProfileRepository.getInstance(apiService)
            HomeRepository::class.java -> HomeRepository.getInstance(apiService, pref)
            PsikologRepository::class.java -> PsikologRepository.getInstance(apiService, pref)
            QuestionRepository::class.java -> QuestionRepository.getInstance(apiService, pref)
            else -> throw IllegalArgumentException("Unknown repository class: $repositoryClass")
        } as T
    }
}