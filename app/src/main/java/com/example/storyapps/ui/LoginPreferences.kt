package com.example.storyapps.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.storyapps.datasource.local.entity.LoginModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class LoginPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val LOGIN_KEY = booleanPreferencesKey("login")
    private val TOKEN_KEY = stringPreferencesKey("token")

    fun getLoginStatus(): Flow<LoginModel> {
        return dataStore.data.map { preferences ->
            LoginModel(
                preferences[LOGIN_KEY] ?: false, preferences[TOKEN_KEY] ?: ""
            )
        }
    }

    suspend fun saveLoginStatus(loginModel: LoginModel) {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = loginModel.isLogin
            preferences[TOKEN_KEY] = loginModel.token
        }
    }

    suspend fun deleteLoginStatus() {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = false
            preferences[TOKEN_KEY] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}