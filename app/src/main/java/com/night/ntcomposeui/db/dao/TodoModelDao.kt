package com.night.ntcomposeui.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.night.ntcomposeui.model.TodoModel
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoModelDao {

    @Query("SELECT * FROM TodoModel ORDER BY createdDateTime DESC")
    fun getAll(): Flow<List<TodoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todoModel: TodoModel)

    @Delete
    suspend fun delete(todoModel: TodoModel)

    @Update
    suspend fun update(todoModel: TodoModel)

}