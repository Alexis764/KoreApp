package com.project.koreapp.feature.main

import androidx.compose.ui.graphics.Color
import com.project.koreapp.core.database.entities.TodoDetailEntity
import com.project.koreapp.ui.theme.CategoryColor1
import java.time.LocalDate

data class TodoModel(
    val id: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val hour: Int = 0,
    val minutes: Int = 0,
    val isNotification: Boolean = false,
    val color: Color = CategoryColor1,
    val category: String = "",
    val detailList: List<TodoDetailEntity> = emptyList()
)