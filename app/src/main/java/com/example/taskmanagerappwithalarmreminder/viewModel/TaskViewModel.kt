package com.example.taskmanagerappwithalarmreminder.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagerappwithalarmreminder.entities.TaskModel
import com.example.taskmanagerappwithalarmreminder.repos.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TaskRepository(application)

    fun insertTask(taskModel: TaskModel){
        viewModelScope.launch {
            repository.insertTask(taskModel)
        }
    }

    fun fetchAllTasks() : LiveData<List<TaskModel>> {
        return repository.getAllTasks()
    }

    fun updateTask(taskModel: TaskModel){
        taskModel.isCompleted = !taskModel.isCompleted

        viewModelScope.launch {
            repository.updateTask(taskModel)
        }
    }

    fun deleteTask(taskModel: TaskModel){
        viewModelScope.launch {
            repository.deleteTask(taskModel)
        }
    }
}