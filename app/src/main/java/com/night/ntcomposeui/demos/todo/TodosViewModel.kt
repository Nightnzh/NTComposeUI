package com.night.ntcomposeui.demos.todo

import androidx.lifecycle.*
import com.night.ntcomposeui.db.dao.TodoModelDao
import com.night.ntcomposeui.model.TodoModel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.util.*

class TodosViewModel(private val todoRepository: TodoRepository) : ViewModel() {


    val todos = todoRepository.todos

    fun insertTodo(todo: TodoModel) = viewModelScope.launch {
        todoRepository.insert(todo)
    }

    fun deleteTodo(todo: TodoModel) = viewModelScope.launch {
        todoRepository.delete(todo)
    }

    fun updateTodo(todo: TodoModel) = viewModelScope.launch {
        todoRepository.update(todo)
    }

}


class TodoViewModelFactory(private val todoRepository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodosViewModel(todoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}