package com.example.taskmanagerappwithalarmreminder.repos

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.taskmanagerappwithalarmreminder.daos.TaskDao
import com.example.taskmanagerappwithalarmreminder.db.TaskDatabase
import com.example.taskmanagerappwithalarmreminder.entities.TaskModel

class TaskRepository(context: Context) {
    private val taskDao = TaskDatabase.getDB(context).getTaskDao()

    fun insertTask(taskModel: TaskModel){
        taskDao.addTask(taskModel)
    }

    fun getAllTasks() : LiveData<List<TaskModel>> {
        return taskDao.getAllTask()
    }
}