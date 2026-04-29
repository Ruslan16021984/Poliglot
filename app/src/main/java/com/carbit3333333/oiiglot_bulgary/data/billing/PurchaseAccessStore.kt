package com.carbit3333333.oiiglot_bulgary.data.billing

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.purchaseAccessDataStore by preferencesDataStore(name = "purchase_access")

class PurchaseAccessStore(
    context: Context
) {
    private val appContext = context.applicationContext

    val hasFullCourseAccessFlow: Flow<Boolean> =
        appContext.purchaseAccessDataStore.data.map { preferences ->
            preferences[FULL_COURSE_ACCESS] ?: false
        }

    suspend fun setFullCourseAccess(enabled: Boolean) {
        appContext.purchaseAccessDataStore.edit { preferences ->
            preferences[FULL_COURSE_ACCESS] = enabled
        }
    }

    private companion object {
        val FULL_COURSE_ACCESS = booleanPreferencesKey("full_course_access")
    }
}
