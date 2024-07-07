package com.project.koreapp.feature.main

import com.project.koreapp.core.database.dao.TodoDao
import com.project.koreapp.core.database.dao.TodoDetailDao
import com.project.koreapp.core.database.entities.TodoDetailEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val todoDao: TodoDao,
    private val todoDetailDao: TodoDetailDao
) {

    fun getAllTodo(): Flow<List<TodoModel>> {
        return todoDao.getAllTodo().map { map ->
            map.keys.map { todoEntity ->
                todoEntity.toModel(map[todoEntity]!!)
            }
        }
    }

    suspend fun updateDetailTodo(todoDetailEntity: TodoDetailEntity) {
        todoDetailDao.updateTodoDetail(todoDetailEntity)
    }

}