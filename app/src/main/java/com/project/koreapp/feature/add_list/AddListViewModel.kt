package com.project.koreapp.feature.add_list

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.koreapp.R
import com.project.koreapp.core.categories.CategoryModel
import com.project.koreapp.core.categories.ReadCategoryDocument
import com.project.koreapp.core.database.entities.TodoDetailEntity
import com.project.koreapp.core.database.entities.TodoEntity
import com.project.koreapp.ui.theme.CategoryColor1
import com.project.koreapp.ui.theme.CategoryColor2
import com.project.koreapp.ui.theme.CategoryColor3
import com.project.koreapp.ui.theme.CategoryColor4
import com.project.koreapp.ui.theme.CategoryColor5
import com.project.koreapp.ui.theme.CategoryColor6
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddListViewModel @Inject constructor(
    colorProvider: ColorProvider,
    readCategoryDocument: ReadCategoryDocument,
    private val addListRepository: AddListRepository
) : ViewModel() {

    private val _isDoneButtonEnable = MutableLiveData<Boolean>()
    val isDoneButtonEnable: LiveData<Boolean> = _isDoneButtonEnable

    private var categoryAdded = false
    private fun checkDoneEnable() {
        _isDoneButtonEnable.value = categoryAdded && _detailList.isNotEmpty()
    }


    private val _isShowTimePickerDialog = MutableLiveData<Boolean>()
    val isShowTimePickerDialog: LiveData<Boolean> = _isShowTimePickerDialog

    private val _hour = MutableLiveData<Int>()
    val hour: LiveData<Int> = _hour

    private val _minutes = MutableLiveData<Int>()
    val minutes: LiveData<Int> = _minutes

    private val _isNotification = MutableLiveData<Boolean>()
    val isNotification: LiveData<Boolean> = _isNotification

    fun showTimePickerDialog() {
        _isShowTimePickerDialog.value = true
    }

    fun hideTimePickerDialog() {
        _isShowTimePickerDialog.value = false
    }

    fun updateTime(hour: Int, minutes: Int) {
        _hour.value = hour
        _minutes.value = minutes
    }

    fun changeIsNotification(isNotification: Boolean) {
        _isNotification.value = isNotification
    }


    private val _colorList = mutableStateListOf<ColorModel>()
    val colorList: List<ColorModel> = _colorList

    private val _categorySelected = MutableLiveData<CategoryModel>()
    val categorySelected: LiveData<CategoryModel> = _categorySelected

    private val _categoryList = mutableStateListOf<CategoryModel>()
    val categoryList: List<CategoryModel> = _categoryList

    fun changeColorSelected(colorModel: ColorModel) {
        val colorIndex = _colorList.indexOf(colorModel)

        var i = 0
        while (i < _colorList.size) {
            _colorList[i] = _colorList[i].copy(isSelected = false)
            i++
        }

        _colorList[colorIndex] = _colorList[colorIndex].copy(isSelected = true)
    }

    fun changeCategorySelected(categoryModel: CategoryModel) {
        _categorySelected.value = categoryModel
        categoryAdded = true
        checkDoneEnable()
    }


    private val _isShowAddDetailDialog = MutableLiveData<Boolean>()
    val isShowAddDetailDialog: LiveData<Boolean> = _isShowAddDetailDialog

    private val _detailText = MutableLiveData<String>()
    val detailText: LiveData<String> = _detailText

    private val _detailList = mutableStateListOf<String>()
    val detailList: List<String> = _detailList

    private val _isShowDeleteDialog = MutableLiveData<Boolean>()
    val isShowDeleteDialog: LiveData<Boolean> = _isShowDeleteDialog

    private val _deleteDialogText = MutableLiveData<String>()
    val deleteDialogText: LiveData<String> = _deleteDialogText

    fun showAddDetailDialog() {
        _detailText.value = ""
        _isShowAddDetailDialog.value = true
    }

    fun hideAddDetailDialog() {
        _isShowAddDetailDialog.value = false
    }

    fun onDetailTextChanged(detailText: String) {
        if (detailText.length <= 20) {
            _detailText.value = detailText
        }
    }

    fun addDetailList(detail: String) {
        _detailList.add(detail)
        checkDoneEnable()
    }

    fun deleteDetailList(detail: String) {
        _detailList.remove(detail)
        checkDoneEnable()
    }

    fun onMoveDetailList(fromIndex: Int, toIndex: Int) {
        _detailList.move(fromIndex, toIndex)
    }

    fun showDeleteDialog(detail: String) {
        _deleteDialogText.value = detail
        _isShowDeleteDialog.value = true
    }

    fun hideDeleteDialog() {
        _isShowDeleteDialog.value = false
    }


    init {
        _colorList.addAll(colorProvider())
        _categoryList.addAll(readCategoryDocument())
    }

    fun insertTodoInformation(
        date: LocalDate,
        hour: Int,
        minutes: Int,
        isNotification: Boolean,
        color: Color,
        category: String,
        detailList: List<String>
    ) {
        viewModelScope.launch {
            val todoId = System.currentTimeMillis().hashCode()
            val colorId = when (color) {
                CategoryColor1 -> R.color.CategoryColor1
                CategoryColor2 -> R.color.CategoryColor2
                CategoryColor3 -> R.color.CategoryColor3
                CategoryColor4 -> R.color.CategoryColor4
                CategoryColor5 -> R.color.CategoryColor5
                CategoryColor6 -> R.color.CategoryColor6
                else -> R.color.CategoryColor1
            }

            addListRepository.insertTodo(
                TodoEntity(
                    id = todoId,
                    year = date.year,
                    month = date.monthValue,
                    day = date.dayOfMonth,
                    hour = hour,
                    minutes = minutes,
                    isNotification = isNotification,
                    color = colorId,
                    category = category
                )
            )

            addListRepository.insertTodoDetailList(
                detailList.map {
                    TodoDetailEntity(
                        todoId = todoId,
                        detail = it
                    )
                }
            )
        }
    }

}