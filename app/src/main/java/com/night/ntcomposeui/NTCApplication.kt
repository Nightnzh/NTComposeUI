package com.night.ntcomposeui

import android.app.Application
import com.night.ntcomposeui.db.AppDatabase
import com.night.ntcomposeui.demos.todo.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NTCApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    val db by lazy { AppDatabase.getInstance(this,applicationScope) }
    val repository by lazy { TodoRepository(db.todoModelDao()) }
}