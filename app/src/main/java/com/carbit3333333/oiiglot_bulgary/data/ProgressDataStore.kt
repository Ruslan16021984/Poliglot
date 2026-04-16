package com.carbit3333333.oiiglot_bulgary.data


import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.progressDataStore by preferencesDataStore(name = "lesson_progress")