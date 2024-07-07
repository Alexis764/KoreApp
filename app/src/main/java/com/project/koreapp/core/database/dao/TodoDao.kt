package com.project.koreapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.project.koreapp.core.database.entities.TodoDetailEntity
import com.project.koreapp.core.database.entities.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert
    suspend fun insertTodo(todoEntity: TodoEntity)

    @Query(
        "SELECT * FROM TodoEntity " +
                "JOIN TodoDetailEntity ON TodoEntity.id = TodoDetailEntity.todoId"
    )
    fun getAllTodo(): Flow<Map<TodoEntity, List<TodoDetailEntity>>>

}