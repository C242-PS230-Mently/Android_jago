package id.co.mentalhealth.data.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveSession(user: UserModel) {
        try {
            dataStore.edit { preferences ->
                preferences[PHOTO_KEY] = user.photo.toString()
                preferences[EMAIL_KEY] = user.email
                preferences[ID_KEY] = user.userId
                preferences[NAME_KEY] = user.name
                preferences[TOKEN_KEY] = user.token
                preferences[IS_LOGIN_KEY] = true
            }
            Log.d("UserPreferences", "Sesi berhasil disimpan.")
        } catch (e: Exception) {
            Log.e("UserPreferences", "Gagal menyimpan sesi: ${e.message}")
        }
    }

    // Retrieve the token from DataStore
    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[PHOTO_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[ID_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun logout() {
        try {
            dataStore.edit { preferences ->
                preferences.clear()
            }
            Log.d("UserPreferences", "Sesi berhasil dihapus.")
        } catch (e: Exception) {
            Log.e("UserPreferences", "Gagal menghapus sesi: ${e.message}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val PHOTO_KEY = stringPreferencesKey("photo")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val ID_KEY = stringPreferencesKey("id")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}