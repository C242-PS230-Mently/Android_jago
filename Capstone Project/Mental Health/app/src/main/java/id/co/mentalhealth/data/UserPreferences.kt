package id.co.mentalhealth.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import id.co.mentalhealth.data.network.response.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferences constructor(private val dataStore: DataStore<Preferences>) {
    companion object {

        private val TOKEN_KEY = stringPreferencesKey("token_key")

        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun saveToken(token: String) {
        Log.d("UserPreferences", "Menyimpan token: $token")
        try {
            dataStore.edit { preferences ->
                preferences[TOKEN_KEY] = token
            }
            Log.d("UserPreferences", "Token berhasil disimpan.")
        } catch (e: Exception) {
            Log.e("UserPreferences", "Gagal menyimpan token: ${e.message}")
        }
    }

    // Retrieve the token from DataStore
    fun getToken(): Flow<String?> {
        return dataStore.data
            .map { preferences ->
                val token = preferences[TOKEN_KEY]
                Log.d("UserPreferences", "Token yang diambil: $token")
                token
            }
    }

    // Ambil data User dalam bentuk Flow<User>
    fun getUser(): Flow<User> {
        return dataStore.data
            .map { preferences ->
                val token = preferences[TOKEN_KEY] ?: ""
                Log.d("UserPreferences", "Token ditemukan: $token")
                if (token.isEmpty()) {
                    throw IllegalStateException("Token tidak ditemukan. Pengguna belum login.")
                }
                User(token = token)
            }
    }

    // Kelas data User untuk menyimpan informasi pengguna
    data class User(
        val token: String
    )



    suspend fun clearToken() {
        Log.d("UserPreferences", "Menghapus token.")
        try {
            dataStore.edit { preferences ->
                preferences.remove(TOKEN_KEY)
            }
            Log.d("UserPreferences", "Token berhasil dihapus.")
        } catch (e: Exception) {
            Log.e("UserPreferences", "Gagal menghapus token: ${e.message}")
        }
    }
}