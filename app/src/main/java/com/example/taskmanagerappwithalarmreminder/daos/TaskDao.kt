package com.example.taskmanagerappwithalarmreminder.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanagerappwithalarmreminder.entities.TaskModel

@Dao
interface TaskDao {
    @Insert
    fun addTask(taskModel: TaskModel)

    @Update
    fun updateTask(taskModel: TaskModel)

    @Delete
    fun deleteTask(taskModel: TaskModel)

    @Query("select * from task order by id desc")
    fun getAllTask() : LiveData<List<TaskModel>>
}