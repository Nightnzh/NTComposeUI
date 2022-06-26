package com.night.ntcomposeui.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity
data class TodoModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val completed: Boolean = false,
    val createdDateTime : String = DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),
    val completedDateTime : String? = null
)
