package com.example.taskmanagerappwithalarmreminder.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanagerappwithalarmreminder.entities.TaskModel
import com.example.taskmanagerappwithalarmreminder.repos.TaskRepository

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TaskRepository(application)

    fun insertTask(taskModel: TaskModel){
        repository.insertTask(taskModel)
    }

    fun fetchAllTasks() : LiveData<List<TaskModel>> {
        return repository.getAllTasks()
    }
}