package com.example.taskmanagerappwithalarmreminder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskmanagerappwithalarmreminder.daos.TaskDao
import com.example.taskmanagerappwithalarmreminder.entities.TaskModel

@Database(entities = [TaskModel::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskDao() : TaskDao

    companion object {
        private var taskDatabase : TaskDatabase? = null

        fun getDB(context: Context) : TaskDatabase {
            return taskDatabase ?: synchronized(this) {
                val db = Room.databaseBuilder(context, TaskDatabase::class.java, "task")
                    .build()

                taskDatabase = db
                db
            }
        }
    }
}