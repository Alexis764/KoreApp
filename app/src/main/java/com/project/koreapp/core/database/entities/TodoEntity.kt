package com.project.koreapp.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.koreapp.R
import com.project.koreapp.feature.main.TodoModel
import com.project.koreapp.ui.theme.CategoryColor1
import com.project.koreapp.ui.theme.CategoryColor2
import com.project.koreapp.ui.theme.CategoryColor3
import com.project.koreapp.ui.theme.CategoryColor4
import com.project.koreapp.ui.theme.CategoryColor5
import com.project.koreapp.ui.theme.CategoryColor6
import java.time.LocalDate

@Entity
data class TodoEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id") val id: Int,

    @ColumnInfo("year") val year: Int,
    @ColumnInfo("month") val month: Int,
    @ColumnInfo("day") val day: Int,
    @ColumnInfo("hour") val hour: Int,
    @ColumnInfo("minutes") val minutes: Int,
    @ColumnInfo("isNotification") val isNotification: Boolean,
    @ColumnInfo("color") val color: Int,
    @ColumnInfo("category") val category: String
) {
    fun toModel(detailList: List<TodoDetailEntity>): TodoModel {
        val date = LocalDate.of(year, month, day)
        val colorCompose = when (color) {
            R.color.CategoryColor1 -> CategoryColor1
            R.color.CategoryColor2 -> CategoryColor2
            R.color.CategoryColor3 -> CategoryColor3
            R.color.CategoryColor4 -> CategoryColor4
            R.color.CategoryColor5 -> CategoryColor5
            R.color.CategoryColor6 -> CategoryColor6
            else -> CategoryColor1
        }

        return TodoModel(
            id = id,
            date = date,
            hour = hour,
            minutes = minutes,
            isNotification = isNotification,
            color = colorCompose,
            category = category,
            detailList = detailList
        )
    }
}
