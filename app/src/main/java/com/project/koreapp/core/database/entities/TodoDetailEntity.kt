package com.project.koreapp.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoDetailEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Int = 0,

    @ColumnInfo("todoId") val todoId: Int,
    @ColumnInfo("detail") val detail: String,
    @ColumnInfo("isSelected") val isSelected: Boolean = false
)
