package com.saizad.mvvm

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreWrapper @Inject constructor(
    @ApplicationContext val context: Context,
    val gson: Gson
) {

    private val Context.dataStore by preferencesDataStore("user")

    fun getValue(prefKey: String): Flow<String?> {
        val key = stringPreferencesKey(prefKey)
        return context.dataStore.data.map { pref ->
            pref[key]
        }
    }

    suspend fun putValue(prefKey: String, value: String) {
        val key = stringPreferencesKey(prefKey)
        context.dataStore.edit {
            it[key] = value
        }
    }

    fun <T> getObject(prefKey: String, classOfT: Class<T>): Flow<T?> {
        val key = stringPreferencesKey(prefKey)
        return context.dataStore.data.map {
            gson.fromJson(it[key], classOfT)
        }
    }

    suspend fun putObject(prefKey: String, value: Any) {
        putValue(prefKey, gson.toJson(value))
    }

    suspend fun remove(prefKey: String){
        val key = stringPreferencesKey(prefKey)
        context.dataStore.edit {
            it.remove(key)
        }
    }

    suspend fun removeAll(){
        context.dataStore.edit {
            it.clear()
        }
    }
}