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
    suspend fun addTask(taskModel: TaskModel)

    @Update
    suspend fun updateTask(taskModel: TaskModel)

    @Delete
    suspend fun deleteTask(taskModel: TaskModel)

    @Query("select * from task order by id desc")
    fun getAllTask() : LiveData<List<TaskModel>>

    @Query("SELECT * FROM task WHERE category = :category")
    fun getTasksByCategory(category: String): LiveData<List<TaskModel>>

    @Query("SELECT * FROM task WHERE taskDate BETWEEN :startDate AND :endDate")
    fun getTaskWithDateFilter(startDate: String, endDate: String): LiveData<List<TaskModel>>
}