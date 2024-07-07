package com.project.koreapp.feature.add_list

import com.project.koreapp.core.database.dao.TodoDao
import com.project.koreapp.core.database.dao.TodoDetailDao
import com.project.koreapp.core.database.entities.TodoDetailEntity
import com.project.koreapp.core.database.entities.TodoEntity
import javax.inject.Inject

class AddListRepository @Inject constructor(
    private val todoDao: TodoDao,
    private val todoDetailDao: TodoDetailDao
) {

    suspend fun insertTodo(todoEntity: TodoEntity) {
        todoDao.insertTodo(todoEntity)
    }

    suspend fun insertTodoDetailList(detailList: List<TodoDetailEntity>) {
        todoDetailDao.insertTodoDetailList(detailList)
    }

}