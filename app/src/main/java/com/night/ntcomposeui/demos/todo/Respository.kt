package com.night.ntcomposeui.demos.todo

import androidx.annotation.WorkerThread
import com.night.ntcomposeui.db.dao.TodoModelDao
import com.night.ntcomposeui.model.TodoModel

class TodoRepository(private val todoModelDao: TodoModelDao) {

    val todos = todoModelDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(todoModel: TodoModel) = todoModelDao.insert(todoModel)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(todoModel: TodoModel) = todoModelDao.delete(todoModel)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(todoModel: TodoModel) = todoModelDao.update(todoModel)

}