package com.project.koreapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.project.koreapp.core.database.entities.TodoDetailEntity

@Dao
interface TodoDetailDao {

    @Insert
    suspend fun insertTodoDetailList(todoDetailList: List<TodoDetailEntity>)

    @Update
    suspend fun updateTodoDetail(todoDetailEntity: TodoDetailEntity)

}