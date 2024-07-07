package com.project.koreapp.feature.add_list

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.koreapp.core.categories.CategoryModel
import java.time.LocalDate

@Composable
fun MyAddListScreen(
    year: Int,
    month: Int,
    day: Int,
    navController: NavHostController = rememberNavController(),
    addListViewModel: AddListViewModel = hiltViewModel(),
    isDialogChanged: (Boolean) -> Unit
) {
    val date = LocalDate.of(year, month, day)

    val isDoneButtonEnable by addListViewModel.isDoneButtonEnable.observeAsState(false)

    val isShowTimePickerDialog by addListViewModel.isShowTimePickerDialog.observeAsState(false)
    val hour by addListViewModel.hour.observeAsState(0)
    val minutes by addListViewModel.minutes.observeAsState(0)
    val isNotification by addListViewModel.isNotification.observeAsState(false)

    val colorList = addListViewModel.colorList
    val categorySelected by addListViewModel.categorySelected.observeAsState(CategoryModel(""))

    val isShowAddDetailDialog by addListViewModel.isShowAddDetailDialog.observeAsState(false)
    val detailList = addListViewModel.detailList
    val isShowDeleteDialog by addListViewModel.isShowDeleteDialog.observeAsState(false)

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        AddListTopAppBar(navController, isDoneButtonEnable) {
            val color: ColorModel = colorList.find { it.isSelected }!!

            addListViewModel.insertTodoInformation(
                date,
                hour,
                minutes,
                isNotification,
                color.color,
                categorySelected.category,
                detailList
            )

            navController.popBackStack()
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MyDateText(date)

            MyTimeTextField(
                hour = hour,
                minutes = minutes,
                isNotification = isNotification,
                showTimePickerDialog = { addListViewModel.showTimePickerDialog() },
                changeIsNotification = { addListViewModel.changeIsNotification(it) }
            )
            Spacer(modifier = Modifier.height(4.dp))

            MyCategorySelection(
                categorySelected,
                addListViewModel,
                colorList
            ) { isDialogChanged(it) }
            Spacer(modifier = Modifier.height(4.dp))

            MyDetailList(
                detailList = detailList,
                onAddButtonClick = { addListViewModel.showAddDetailDialog() },
                onDeleteButtonClick = { addListViewModel.showDeleteDialog(it) },
                onMove = { fromIndex, toIndex ->
                    addListViewModel.onMoveDetailList(
                        fromIndex,
                        toIndex
                    )
                }
            )
        }


        //Dialogs
        if (isShowTimePickerDialog) {
            isDialogChanged(true)
            MyTimePickerDialog(addListViewModel) { isDialogChanged(false) }
        }
        if (isShowAddDetailDialog) {
            isDialogChanged(true)
            MyAddDetailDialog(addListViewModel) { isDialogChanged(false) }
        }
        if (isShowDeleteDialog) {
            isDialogChanged(true)
            MyDeleteDetailDialog(addListViewModel) { isDialogChanged(false) }
        }
    }
}


@Composable
fun MyDeleteDetailDialog(addListViewModel: AddListViewModel, isDialogFalse: () -> Unit) {
    val deleteDialogText by addListViewModel.deleteDialogText.observeAsState("")

    Dialog(onDismissRequest = { }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                Modifier.padding(12.dp)
            ) {
                Text(
                    text = "Do you want to remove $deleteDialogText ?",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(
                        onClick = {
                            addListViewModel.hideDeleteDialog()
                            isDialogFalse()
                        }
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.Red,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            addListViewModel.deleteDetailList(deleteDialogText)
                            addListViewModel.hideDeleteDialog()
                            isDialogFalse()
                        }
                    )
                    {
                        Text(
                            text = "Confirm",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun MyAddDetailDialog(addListViewModel: AddListViewModel, isDialogFalse: () -> Unit) {
    val context = LocalContext.current
    val detailText by addListViewModel.detailText.observeAsState("")

    Dialog(onDismissRequest = { }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                Modifier.padding(12.dp)
            ) {
                OutlinedTextField(
                    value = detailText,
                    onValueChange = { addListViewModel.onDetailTextChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = "Detail")
                    },
                    singleLine = true,
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedLabelColor = Color.LightGray,
                        focusedLabelColor = Color.Black
                    )
                )

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(
                        onClick = {
                            addListViewModel.hideAddDetailDialog()
                            isDialogFalse()
                        }
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.Red,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            if (detailText.trim().isNotBlank()) {
                                addListViewModel.addDetailList(detailText)
                                addListViewModel.hideAddDetailDialog()
                                isDialogFalse()

                            } else {
                                Toast.makeText(
                                    context,
                                    "Add detail text",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                    {
                        Text(
                            text = "Confirm",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTimePickerDialog(addListViewModel: AddListViewModel, isDialogFalse: () -> Unit) {
    val timePickerState = rememberTimePickerState()

    Dialog(
        onDismissRequest = { }
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                Modifier.padding(12.dp)
            ) {
                TimePicker(state = timePickerState)

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(
                        onClick = {
                            addListViewModel.hideTimePickerDialog()
                            isDialogFalse()
                        }
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.Red,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            addListViewModel.updateTime(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            addListViewModel.hideTimePickerDialog()
                            isDialogFalse()
                        }
                    )
                    {
                        Text(
                            text = "Confirm",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun MyDetailList(
    detailList: List<String>,
    onAddButtonClick: () -> Unit,
    onDeleteButtonClick: (String) -> Unit,
    onMove: (Int, Int) -> Unit
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "List", color = Color.Black)

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    onAddButtonClick()
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.Black)
            }
        }

        DragDropList(
            items = detailList,
            onMove = { fromIndex, toIndex -> onMove(fromIndex, toIndex) }
        ) {
            onDeleteButtonClick(it)
        }
    }
}


@Composable
fun MyCategorySelection(
    categorySelected: CategoryModel,
    addListViewModel: AddListViewModel,
    colorList: List<ColorModel>,
    isDialogChanged: (Boolean) -> Unit
) {
    val categoryList = addListViewModel.categoryList

    Column(
        Modifier.fillMaxWidth()
    ) {
        CategoryColorSelect(colorList) { addListViewModel.changeColorSelected(it) }

        Spacer(modifier = Modifier.height(8.dp))

        CategoryDropDownMenu(
            value = categorySelected.category,
            categoryList = categoryList,
            onCategoryChanged = { addListViewModel.changeCategorySelected(it) },
            isDialogChanged = { isDialogChanged(it) }
        )
    }
}

@Composable
fun CategoryDropDownMenu(
    value: String,
    categoryList: List<CategoryModel>,
    onCategoryChanged: (CategoryModel) -> Unit,
    isDialogChanged: (Boolean) -> Unit
) {
    var expandedDropDownMenu by rememberSaveable { mutableStateOf(false) }
    if (expandedDropDownMenu) isDialogChanged(true) else isDialogChanged(false)

    OutlinedTextField(
        value = value,
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expandedDropDownMenu = true },
        readOnly = true,
        enabled = false,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            disabledTrailingIconColor = Color.Black,
            disabledTextColor = Color.Black,
            disabledBorderColor = Color.Black
        )
    )

    DropdownMenu(
        expanded = expandedDropDownMenu,
        onDismissRequest = { expandedDropDownMenu = false },
        modifier = Modifier.fillMaxWidth(0.87f)
    ) {
        categoryList.forEach {
            DropdownMenuItem(
                text = { Text(text = it.category) },
                onClick = {
                    expandedDropDownMenu = false
                    onCategoryChanged(it)
                },
                modifier = Modifier.height(30.dp)
            )
        }
    }
}

@Composable
fun CategoryColorSelect(colorList: List<ColorModel>, onColorClick: (ColorModel) -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Category", color = Color.Black)
        Spacer(modifier = Modifier.weight(1f))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(colorList) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    border = if (it.isSelected) BorderStroke(2.dp, Color.Black) else null,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onColorClick(it) },
                    colors = CardDefaults.cardColors(containerColor = it.color)
                ) {}
            }
        }
    }
}


@Composable
fun MyTimeTextField(
    hour: Int,
    minutes: Int,
    isNotification: Boolean,
    showTimePickerDialog: () -> Unit,
    changeIsNotification: (Boolean) -> Unit
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(text = "Time", color = Color.Black)
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = String.format("%02d:%02d", hour, minutes),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showTimePickerDialog() },
            readOnly = true,
            enabled = false,
            trailingIcon = {
                IconButton(onClick = { changeIsNotification(!isNotification) }) {
                    Icon(
                        imageVector = if (isNotification) Icons.Default.Notifications else Icons.Default.NotificationsNone,
                        contentDescription = null
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTrailingIconColor = Color.Black,
                disabledTextColor = Color.Black,
                disabledBorderColor = Color.Black
            )
        )
    }
}


@Composable
fun MyDateText(date: LocalDate) {
    Text(
        text = "${date.dayOfMonth} ${date.month} ${date.year}",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp
    )
}


@Composable
fun AddListTopAppBar(
    navController: NavHostController,
    enable: Boolean,
    onDoneButtonClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null)
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = { onDoneButtonClick() },
            enabled = enable,
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
        ) {
            Text(text = "Done", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}