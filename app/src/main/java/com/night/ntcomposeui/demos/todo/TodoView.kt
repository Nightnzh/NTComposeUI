package com.night.ntcomposeui.demos.todo

import android.view.KeyEvent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mukesh.MarkDown
import com.night.ntcomposeui.NTCApplication
import com.night.ntcomposeui.component.MyTabsView
import com.night.ntcomposeui.model.MyTabView
import com.night.ntcomposeui.model.TodoModel
import com.night.ntcomposeui.ui.theme.Shapes
import org.joda.time.DateTime



@Composable
fun TodosDemo(){

    val ctx = LocalContext.current

    MyTabsView(tabViews = arrayOf(
        MyTabView(
            viewTitle = "Preview",
            ContentView = {
                TodosView()
            }
        ),
        MyTabView(
            viewTitle = "Code",
            ContentView = {
                MarkDown(text = ctx.assets.open("todoMVVM.md").buffered().readBytes().decodeToString())
            }
        )
    ))
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodosView(
    todosViewModel: TodosViewModel = viewModel(
        factory = TodoViewModelFactory((LocalContext.current.applicationContext as NTCApplication).repository)
    )
) {

    val todos by todosViewModel.todos.collectAsState(initial = listOf<TodoModel>())

    var showAddTodoDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(0.9f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            stickyHeader("Doing") {
                Text("Doing")
            }
            itemsIndexed(items = todos.filter { todo -> !todo.completed }) { _, todo ->
                TodoItem(todo,
                    onCheckedChange = {
                        todosViewModel.updateTodo(
                            todo.copy(
                                completed = it,
                                completedDateTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss")
                            )
                        )
                    },
                    onDelete = {
                        todosViewModel.deleteTodo(todo)
                    }
                )
            }

            stickyHeader("Completed") {
                Text("Completed")
            }
            itemsIndexed(items = todos.filter { todo -> todo.completed }) { _, todo ->
                TodoItem(todo,
                    onCheckedChange = {
                        todosViewModel.updateTodo(
                            todo.copy(
                                completed = it,
                                completedDateTime = null
                            )
                        )
                    },
                    onDelete = {
                        todosViewModel.deleteTodo(todo)
                    }
                )
            }

        }
        ExtendedFloatingActionButton(
            modifier = Modifier.fillMaxWidth(),
            text = { Text(text = "ADD") },
            onClick = { showAddTodoDialog = true }
        )
    }
    AddTodoDialog(
        showAddTodoDialog = showAddTodoDialog,
        onDismissRequest = { showAddTodoDialog = false },
        addTodo = {
            todosViewModel.insertTodo(it)
            showAddTodoDialog = false
        }
    )

}

@Composable
private fun AddTodoDialog(
    showAddTodoDialog: Boolean,
    onDismissRequest: () -> Unit,
    addTodo: (TodoModel) -> Unit
) {

    var todoTitle by remember { mutableStateOf("") }

    if (showAddTodoDialog)
        Dialog(onDismissRequest = { onDismissRequest(); todoTitle = "" }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background, shape = Shapes.medium)
                    .wrapContentHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Add Todo")
                OutlinedTextField(
                    value = todoTitle,
                    onValueChange = { todoTitle = it },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                )
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = {
                        addTodo(TodoModel(title = todoTitle.trim()))
                    }) {
                        Text("OK")
                    }
                }

            }
        }

}


@Composable
private fun TodoItem(
    todo: TodoModel,
    onCheckedChange: (Boolean) -> Unit = {},
    onDelete: () -> Unit = {}
) {

    var showAlertToCheckDelete by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.LightGray))
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.completed,
                onCheckedChange = onCheckedChange
            )
            Text(modifier = Modifier, text = todo.title)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { showAlertToCheckDelete = true }) {
                Text(text = "Delete")
            }
        }
    }

    if (showAlertToCheckDelete) {
        AlertDialog(

            title = { Text("Sure to delete this todo?") },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        onDelete()
                        showAlertToCheckDelete = false
                    }) {
                        Text("OK")
                    }
                }
            },
            onDismissRequest = {
                showAlertToCheckDelete = false
            },
        )
    }

}


