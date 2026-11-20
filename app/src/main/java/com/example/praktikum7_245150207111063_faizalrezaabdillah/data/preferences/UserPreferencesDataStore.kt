package com.example.praktikum7_245150207111063_faizalrezaabdillah.data.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

internal val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)
