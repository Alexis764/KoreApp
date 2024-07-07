package com.project.koreapp.feature.main

import java.time.DayOfWeek

data class DayWeekModel(
    val dayOfMonth: Int,
    val dayNameOfWeek: DayOfWeek,
    val isSelected: Boolean
)
