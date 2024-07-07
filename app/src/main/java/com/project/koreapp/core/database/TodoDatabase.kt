package com.project.koreapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.koreapp.core.database.dao.TodoDao
import com.project.koreapp.core.database.dao.TodoDetailDao
import com.project.koreapp.core.database.entities.TodoDetailEntity
import com.project.koreapp.core.database.entities.TodoEntity

@Database(entities = [TodoEntity::class, TodoDetailEntity::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    abstract fun todoDetailDao(): TodoDetailDao

}