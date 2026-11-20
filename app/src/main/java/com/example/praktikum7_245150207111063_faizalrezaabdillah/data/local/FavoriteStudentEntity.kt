package com.example.praktikum7_245150207111063_faizalrezaabdillah.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_students")
data class FavoriteStudentEntity(
    @PrimaryKey val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String
)
