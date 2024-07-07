package com.project.koreapp.feature.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.koreapp.core.database.entities.TodoDetailEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _selectedDate = MutableLiveData<LocalDate>()
    val selectedDate: LiveData<LocalDate> = _selectedDate

    fun selectedDateChange(localDate: LocalDate) {
        _selectedDate.value = localDate
    }


    private val _isShowCalendarDialog = MutableLiveData<Boolean>()
    val isShowCalendarDialog: LiveData<Boolean> = _isShowCalendarDialog

    fun showCalendarDialog() {
        _isShowCalendarDialog.value = true
    }

    fun hideCalendarDialog() {
        _isShowCalendarDialog.value = false
    }


    private val _isShowDownloadDialog = MutableLiveData<Boolean>()
    val isShowDownloadDialog: LiveData<Boolean> = _isShowDownloadDialog

    fun showDownloadDialog() {
        _isShowDownloadDialog.value = true
    }

    fun hideDownloadDialog() {
        _isShowDownloadDialog.value = false
    }


    private val _todoList = mutableStateListOf<TodoModel>()
    val todoList: List<TodoModel> = _todoList

    init {
        viewModelScope.launch {
            mainRepository.getAllTodo().collect {
                _todoList.clear()
                _todoList.addAll(it)
            }
        }
    }

    fun updateTodoDetail(todoDetailEntity: TodoDetailEntity) {
        viewModelScope.launch {
            mainRepository.updateDetailTodo(todoDetailEntity.copy(isSelected = !todoDetailEntity.isSelected))
        }
    }

}