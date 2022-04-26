package com.returnz3ro.messystem.service.model.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.returnz3ro.messystem.service.model.datamodel.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore  by preferencesDataStore(name = "dataStore")

class DataStoreModule (private val context : Context){

    companion object{
        val USER_ID = stringPreferencesKey("userId")
        val USER_PW = stringPreferencesKey("userPw")
        val USER_NAME = stringPreferencesKey("userName")
        val USER_WORKTYPE = stringPreferencesKey("userWorktype")
        val USER_GROUP = stringPreferencesKey("userGroup")
    }
    // stringKey 키 값과 대응되는 값 반환
    val user : Flow<User> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            User(
                preferences[USER_ID] ?:"",
                preferences[USER_PW] ?:"",
                preferences[USER_NAME] ?:"",
                preferences[USER_WORKTYPE] ?:"",
                preferences[USER_GROUP] ?:""
            )


        }

    // String값을 stringKey 키 값에 저장
    suspend fun setUserData(user : User){
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = user.userId
            preferences[USER_PW] = user.userPw
            preferences[USER_NAME] = user.userName
            preferences[USER_WORKTYPE] = user.userWorktype
            preferences[USER_GROUP] = user.userGroup
        }
    }

    suspend fun clearData(){
        context.dataStore.edit {
            it.clear()
        }
    }
}