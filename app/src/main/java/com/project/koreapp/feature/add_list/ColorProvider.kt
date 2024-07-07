package com.project.koreapp.feature.add_list

import com.project.koreapp.ui.theme.CategoryColor1
import com.project.koreapp.ui.theme.CategoryColor2
import com.project.koreapp.ui.theme.CategoryColor3
import com.project.koreapp.ui.theme.CategoryColor4
import com.project.koreapp.ui.theme.CategoryColor5
import com.project.koreapp.ui.theme.CategoryColor6
import javax.inject.Inject

class ColorProvider @Inject constructor() {

    operator fun invoke(): List<ColorModel> {
        return listOf(
            ColorModel(CategoryColor1, true),
            ColorModel(CategoryColor2, false),
            ColorModel(CategoryColor3, false),
            ColorModel(CategoryColor4, false),
            ColorModel(CategoryColor5, false),
            ColorModel(CategoryColor6, false)
        )
    }

}