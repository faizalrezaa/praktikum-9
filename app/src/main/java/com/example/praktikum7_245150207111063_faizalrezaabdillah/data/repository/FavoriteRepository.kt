package com.example.praktikum7_245150207111063_faizalrezaabdillah.data.repository

import com.example.praktikum7_245150207111063_faizalrezaabdillah.data.local.FavoriteStudentDao
import com.example.praktikum7_245150207111063_faizalrezaabdillah.data.local.FavoriteStudentEntity
import com.example.praktikum7_245150207111063_faizalrezaabdillah.data.model.Student
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(
    private val dao: FavoriteStudentDao
) {
    fun observeIsFavorite(id: Int): Flow<Boolean> =
        dao.observeIsFavorite(id)

    fun observeFavorites(): Flow<List<FavoriteStudentEntity>> =
        dao.observeFavorites()

    suspend fun add(student: Student) {
        dao.insert(
            FavoriteStudentEntity(
                id = student.id,
                firstName = student.name,
                lastName = "",
                email = student.email
            )
        )
    }

    suspend fun remove(id: Int) {
        dao.deleteById(id)
    }
}
