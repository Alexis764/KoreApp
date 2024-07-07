package com.project.koreapp.core.database

import android.content.Context
import androidx.room.Room
import com.project.koreapp.core.database.dao.TodoDao
import com.project.koreapp.core.database.dao.TodoDetailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "TodoDatabase"
        ).build()
    }

    @Provides
    fun provideTodoDao(todoDatabase: TodoDatabase): TodoDao {
        return todoDatabase.todoDao()
    }

    @Provides
    fun provideTodoDetailDao(todoDatabase: TodoDatabase): TodoDetailDao {
        return todoDatabase.todoDetailDao()
    }

}