package com.example.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import com.example.todolistapp.ui.theme.ToDoListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ToDoListApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoListApp() {
    var task by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf<Pair<String, Boolean>>()) }
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To-Do List") }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) {
                    Text("Tasks")
                }
                Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) {
                    Text("Completed")
                }
            }

            if (selectedTabIndex == 0) {
                TaskList(
                    task = task,
                    onTaskChange = { task = it },
                    onAddTask = {
                        if (task.isNotEmpty()) {
                            tasks = tasks + (task to false)
                            task = ""
                        }
                    },
                    tasks = tasks,
                    onTaskCheckChange = { index, isChecked ->
                        tasks = tasks.toMutableList().apply { this[index] = this[index].copy(second = isChecked) }
                    }
                )
            } else {
                CompletedTasks(
                    tasks = tasks.filter { it.second }
                )
            }
        }
    }
}

@Composable
fun TaskList(
    task: String,
    onTaskChange: (String) -> Unit,
    onAddTask: () -> Unit,
    tasks: List<Pair<String, Boolean>>,
    onTaskCheckChange: (Int, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Today-To-Do-List:")
        BasicTextField(
            value = task,
            onValueChange = onTaskChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(8.dp)
                .padding(bottom = 8.dp)
        )
        Button(
            onClick = onAddTask,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta)
        ) {
            Text("Add", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            tasks.forEachIndexed { index, taskItem ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = taskItem.second,
                        onCheckedChange = { onTaskCheckChange(index, it) },
                        colors = CheckboxDefaults.colors(Color.Cyan)
                    )
                    Text(taskItem.first, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun CompletedTasks(tasks: List<Pair<String, Boolean>>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Completed Tasks:")
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            tasks.forEach { taskItem ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = taskItem.second,
                        onCheckedChange = null, // Disable checking in Completed tab
                        colors = CheckboxDefaults.colors(Color.Cyan)
                    )
                    Text(taskItem.first, fontSize = 16.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToDoListAppTheme {
        ToDoListApp()
    }
}
