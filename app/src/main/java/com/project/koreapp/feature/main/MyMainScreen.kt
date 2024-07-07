package com.project.koreapp.feature.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Environment
import android.text.TextPaint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.project.koreapp.R
import com.project.koreapp.core.database.entities.TodoDetailEntity
import com.project.koreapp.navigation.Routes
import com.project.koreapp.ui.theme.CategoryColor1
import com.project.koreapp.ui.theme.CategoryColor2
import com.project.koreapp.ui.theme.CategoryColor3
import com.project.koreapp.ui.theme.CategoryColor4
import com.project.koreapp.ui.theme.CategoryColor5
import com.project.koreapp.ui.theme.CategoryColor6
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MyMainScreen(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = hiltViewModel(),
    isDialogChanged: (Boolean) -> Unit
) {
    val todayDate = LocalDate.now()

    val selectedDate by mainViewModel.selectedDate.observeAsState(LocalDate.now())
    val todoList = mainViewModel.todoList

    val isShowCalendarDialog by mainViewModel.isShowCalendarDialog.observeAsState(false)
    val isShowDownloadDialog by mainViewModel.isShowDownloadDialog.observeAsState(false)

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MainTopAppBar(selectedDate, todayDate, mainViewModel)
        MainWeekArea(selectedDate, todoList, mainViewModel)
        MainTodoListArea(navController, mainViewModel, todoList, selectedDate)
    }

    //Dialogs
    if (isShowCalendarDialog) {
        isDialogChanged(true)
        MyCalendarDialog(selectedDate, mainViewModel) { isDialogChanged(false) }
    }
    if (isShowDownloadDialog) {
        isDialogChanged(true)
        MyDownloadDialog(selectedDate, todoList, mainViewModel) { isDialogChanged(false) }
    }
}


@Composable
fun MyDownloadDialog(
    selectedDate: LocalDate,
    todoList: List<TodoModel>,
    mainViewModel: MainViewModel,
    isDialogFalse: () -> Unit
) {
    val todoListFilter = todoList.filter { it.date == selectedDate }
    var selectedCheckbox by rememberSaveable { mutableStateOf("JPG") }
    val context = LocalContext.current

    Dialog(onDismissRequest = { }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                Modifier.padding(12.dp)
            ) {
                TitleDownloadDialog()

                MyDownloadRadioButtonOptions(selectedCheckbox) { selectedCheckbox = it }

                MyDownloadButtons(
                    onCancelButtonClick = {
                        mainViewModel.hideDownloadDialog()
                        isDialogFalse()
                    },
                    onConfirmButtonClick = {
                        exportImage(
                            todoList = todoListFilter,
                            extension = selectedCheckbox,
                            context = context,
                            date = selectedDate
                        )

                        mainViewModel.hideDownloadDialog()
                        isDialogFalse()
                    }
                )
            }
        }
    }
}

fun exportImage(
    todoList: List<TodoModel>,
    extension: String,
    context: Context,
    date: LocalDate
) {
    //Bitmap configuration
    val bitmap = Bitmap.createBitmap(470, 900, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val imagePaint = Paint()
    val titlePaint = TextPaint()


    //Paint bitmap
    canvas.drawColor(context.getColor(R.color.gray)) //Background

    val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo) //App logo
    val bitmapScale = Bitmap.createScaledBitmap(logoBitmap, 103, 105, false)
    canvas.drawBitmap(bitmapScale, 347f, 20f, imagePaint)

    titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) //Date title
    titlePaint.textSize = 20f
    canvas.drawText("${date.dayOfMonth} ${date.month} ${date.year}", 20f, 105f, titlePaint)

    var positionY = 140f //Load todoList date information
    todoList.forEach { todoModel ->
        val colorId = when (todoModel.color) {
            CategoryColor1 -> R.color.CategoryColor1
            CategoryColor2 -> R.color.CategoryColor2
            CategoryColor3 -> R.color.CategoryColor3
            CategoryColor4 -> R.color.CategoryColor4
            CategoryColor5 -> R.color.CategoryColor5
            CategoryColor6 -> R.color.CategoryColor6
            else -> R.color.CategoryColor1
        }

        val timePaint = TextPaint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = 17f
        }
        val isMorning = if (todoModel.hour < 12) "AM " else "PM "
        val timeString = isMorning + String.format("%02d:%02d", todoModel.hour, todoModel.minutes)
        canvas.drawText(timeString, 20f, positionY, timePaint)

        val notificationBitmap = BitmapFactory.decodeResource(
            context.resources,
            if (todoModel.isNotification) R.drawable.ic_notification else R.drawable.ic_notification_none
        )
        val notificationScale = Bitmap.createScaledBitmap(notificationBitmap, 32, 32, false)
        canvas.drawBitmap(notificationScale, 110f, (positionY - 22), imagePaint)


        val todoBitmap = Bitmap.createBitmap(
            /* width = */ 430,
            /* height = */ 60 + (40 * todoModel.detailList.size),
            /* config = */ Bitmap.Config.ARGB_8888
        )
        val todoCanvas = Canvas(todoBitmap)
        todoCanvas.drawColor(context.getColor(R.color.white))


        val todoCategoryBitmap =
            Bitmap.createBitmap((todoModel.category.length * 13), 30, Bitmap.Config.ARGB_8888)
        val todoCategoryCanvas = Canvas(todoCategoryBitmap)
        todoCategoryCanvas.drawColor(context.getColor(colorId))
        val categoryTextPaint = TextPaint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textSize = 16f
            color = context.getColor(R.color.white)
        }
        todoCategoryCanvas.drawText(todoModel.category, 8f, 20f, categoryTextPaint)
        todoCanvas.drawBitmap(todoCategoryBitmap, 20f, 20f, imagePaint)


        var detailPositionY = 80f
        todoModel.detailList.forEach { todoDetail ->
            val circlePaint = Paint().apply {
                color = context.getColor(colorId)
                style = Paint.Style.FILL
                isAntiAlias = true
            }
            todoCanvas.drawCircle(20f, detailPositionY, 6f, circlePaint)

            val detailTextPaint = TextPaint().apply {
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                textSize = 16f
            }
            todoCanvas.drawText(todoDetail.detail, 40f, (detailPositionY + 5), detailTextPaint)

            val checkedBitmap = BitmapFactory.decodeResource(
                context.resources,
                if (todoDetail.isSelected) R.drawable.ic_check else R.drawable.ic_uncheck
            )
            val checkedScale = Bitmap.createScaledBitmap(checkedBitmap, 24, 24, false)
            todoCanvas.drawBitmap(checkedScale, 380f, (detailPositionY - 10), imagePaint)

            detailPositionY += 35
        }


        canvas.drawBitmap(todoBitmap, 20f, (positionY + 20), imagePaint)
        positionY += todoBitmap.height + 60
    }


    //Folder creation or selection
    val folder = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "ToDoListFolder"
    )
    if (!folder.exists()) {
        folder.mkdirs()
    }


    //Image file creation
    val fileName = "$date-${System.currentTimeMillis().hashCode()}.$extension"
    val file = File(folder, fileName)
    var fileOutputStream: FileOutputStream? = null

    try {
        fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.flush()
        Toast.makeText(context, "Image exported", Toast.LENGTH_SHORT).show()

    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Image exported error", Toast.LENGTH_SHORT).show()

    } finally {
        fileOutputStream?.close()
    }
}

@Composable
fun MyDownloadButtons(onCancelButtonClick: () -> Unit, onConfirmButtonClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        TextButton(
            onClick = { onCancelButtonClick() }
        ) {
            Text(
                text = "Cancel",
                color = Color.Red,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        TextButton(
            onClick = { onConfirmButtonClick() }
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

@Composable
fun MyDownloadRadioButtonOptions(selectedCheckbox: String, onCheckBoxChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selectedCheckbox == "JPG",
            onClick = { onCheckBoxChange("JPG") },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Black,
                unselectedColor = Color.Black
            )
        )
        Text(text = "JPG")
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selectedCheckbox == "PNG",
            onClick = { onCheckBoxChange("PNG") },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Black,
                unselectedColor = Color.Black
            )
        )
        Text(text = "PNG")
    }
}

@Composable
fun TitleDownloadDialog() {
    Text(
        text = "Export",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color.Black
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCalendarDialog(
    selectedDate: LocalDate,
    mainViewModel: MainViewModel,
    isDialogFalse: () -> Unit
) {
    val selectedDateInMillis = selectedDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateInMillis)

    if (datePickerState.selectedDateMillis != selectedDateInMillis) {
        val instant = Instant.ofEpochMilli(
            datePickerState.selectedDateMillis ?: selectedDateInMillis
        )
        val newSelectedDate = instant.atZone(ZoneOffset.UTC).toLocalDate()
        mainViewModel.selectedDateChange(newSelectedDate)
        mainViewModel.hideCalendarDialog()
        isDialogFalse()
    }

    DatePickerDialog(
        onDismissRequest = {},
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = {
                mainViewModel.hideCalendarDialog()
                isDialogFalse()
            }) {
                Text(text = "Close")
            }
        }
    ) {
        DatePicker(state = datePickerState, showModeToggle = false)
    }
}


@Composable
fun MainTodoListArea(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    todoList: List<TodoModel>,
    selectedDate: LocalDate
) {
    val todoListFilter = todoList.filter { it.date == selectedDate }

    Column(Modifier.fillMaxWidth()) {
        MainTodoListToolBar(navController, selectedDate, mainViewModel)

        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(todoListFilter) {
                TodoItem(it) { todoDetailEntity ->
                    mainViewModel.updateTodoDetail(todoDetailEntity)
                }
            }
        }
    }
}


@Composable
fun TodoItem(todoModel: TodoModel, checkBoxClick: (TodoDetailEntity) -> Unit) {
    val isMorning = if (todoModel.hour < 12) "AM " else "PM "
    val timeString = isMorning + String.format("%02d:%02d", todoModel.hour, todoModel.minutes)

    Column(Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = timeString, fontWeight = FontWeight.Bold, fontSize = 14.sp)

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = if (todoModel.isNotification) Icons.Default.Notifications else Icons.Default.NotificationsNone,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = todoModel.category,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(todoModel.color)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))

                todoModel.detailList.forEach {
                    TodoDetailItem(it, todoModel.color) { todoDetailEntity ->
                        checkBoxClick(todoDetailEntity)
                    }
                }
            }
        }
    }
}

@Composable
fun TodoDetailItem(
    todoDetailEntity: TodoDetailEntity,
    color: Color,
    checkBoxClick: (TodoDetailEntity) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Circle,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = todoDetailEntity.detail,
            textDecoration = if (todoDetailEntity.isSelected) TextDecoration.LineThrough else null
        )
        Spacer(modifier = Modifier.weight(1f))

        Checkbox(
            checked = todoDetailEntity.isSelected,
            onCheckedChange = { checkBoxClick(todoDetailEntity) },
            modifier = Modifier.size(20.dp),
            colors = CheckboxDefaults.colors(
                uncheckedColor = Color.Black,
                checkedColor = Color.Black
            )
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainTodoListToolBar(
    navController: NavHostController,
    selectedDate: LocalDate,
    mainViewModel: MainViewModel
) {
    val permission = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )

    Card(
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "List", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    navController.navigate(
                        Routes.AddListScreen.createRoute(
                            selectedDate.year,
                            selectedDate.monthValue,
                            selectedDate.dayOfMonth
                        )
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                onClick = {
                    when (permission.allPermissionsGranted) {
                        true -> {
                            mainViewModel.showDownloadDialog()
                        }

                        false -> {
                            permission.launchMultiplePermissionRequest()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainWeekArea(selectedDate: LocalDate, todoList: List<TodoModel>, mainViewModel: MainViewModel) {
    val daysOfMonth = List(selectedDate.lengthOfMonth()) { it }.map {
        val dayOfMonth = it + 1
        val date = LocalDate.of(selectedDate.year, selectedDate.month, dayOfMonth)
        DayWeekModel(dayOfMonth, date.dayOfWeek, dayOfMonth == selectedDate.dayOfMonth)
    }
    val colorList: List<Color> =
        todoList.filter { it.date == selectedDate }.map { it.color }.distinct()

    val lazyWeekState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()

    LazyRow(
        state = lazyWeekState
    ) {
        items(daysOfMonth) {
            WeekItem(it, colorList) { dayWeekModel ->
                mainViewModel.selectedDateChange(
                    LocalDate.of(
                        selectedDate.year,
                        selectedDate.month,
                        dayWeekModel.dayOfMonth
                    )
                )
            }
        }
    }

    coroutine.launch {
        lazyWeekState.animateScrollToItem(selectedDate.dayOfMonth - 1)
    }
}

@Composable
fun WeekItem(
    dayWeekModel: DayWeekModel,
    colorList: List<Color>,
    onWeekItemClick: (DayWeekModel) -> Unit
) {
    Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(Color.LightGray),
        border = if (dayWeekModel.isSelected) BorderStroke(2.dp, Color.Black) else null,
        modifier = Modifier.clickable { onWeekItemClick(dayWeekModel) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .height(80.dp)
                .padding(14.dp)
        ) {
            Text(
                text = dayWeekModel.dayNameOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                fontWeight = FontWeight.Bold
            )

            Text(text = dayWeekModel.dayOfMonth.toString())
            Spacer(modifier = Modifier.height(4.dp))

            if (dayWeekModel.isSelected) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    colorList.forEach {
                        Icon(
                            imageVector = Icons.Default.Circle,
                            contentDescription = null,
                            modifier = Modifier.size(6.dp),
                            tint = it
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun MainTopAppBar(selectedDate: LocalDate, todayDate: LocalDate, mainViewModel: MainViewModel) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = { mainViewModel.showCalendarDialog() }) {
            Text(
                text = "${selectedDate.year} ${selectedDate.month}",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(
            visible = selectedDate != todayDate
        ) {
            Button(
                onClick = { mainViewModel.selectedDateChange(todayDate) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(4.dp)
            ) {
                Icon(imageVector = Icons.Default.Today, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Back to today", color = Color.White)
            }
        }
    }
}