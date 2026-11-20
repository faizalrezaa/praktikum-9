package com.example.praktikum7_245150207111063_faizalrezaabdillah.ui.favorites

import com.example.praktikum7_245150207111063_faizalrezaabdillah.data.local.FavoriteStudentEntity

data class FavoritesUiState(
    val favorites: List<FavoriteStudentEntity> = emptyList(),
    val sortAscending: Boolean = true
)
