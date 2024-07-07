package com.project.koreapp.core.categories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ReadCategoryDocument @Inject constructor(@ApplicationContext private val context: Context) {

    operator fun invoke(): List<CategoryModel> {
        val gson = Gson()
        val newListJson = context.assets.open("category.json")
            .bufferedReader()
            .use { it.readText() }

        return gson.fromJson(
            newListJson,
            object : TypeToken<List<CategoryModel>>() {}.type
        )
    }

}