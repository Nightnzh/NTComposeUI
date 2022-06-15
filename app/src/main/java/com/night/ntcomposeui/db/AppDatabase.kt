package com.night.ntcomposeui.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.night.ntcomposeui.db.dao.TodoModelDao
import com.night.ntcomposeui.model.TodoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [TodoModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoModelDao(): TodoModelDao


    private class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val dao = database.todoModelDao()

                    (1..10).forEach {
                        dao.insert(TodoModel(title = "Todo $it"))
                    }
                }
            }
        }
    }

    companion object{
        const val DB_NAME = "NTC_AppDatabase"

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context,scope: CoroutineScope) : AppDatabase{

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return@synchronized instance
            }
        }
    }
}