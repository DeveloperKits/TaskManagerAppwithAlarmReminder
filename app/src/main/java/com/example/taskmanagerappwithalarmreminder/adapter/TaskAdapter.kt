package com.example.taskmanagerappwithalarmreminder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerappwithalarmreminder.databinding.ItemTaskBinding
import com.example.taskmanagerappwithalarmreminder.entities.TaskModel

class TaskAdapter : ListAdapter<TaskModel, TaskAdapter.TaskViewHolder>(TaskDiffCallback()){
    class TaskViewHolder(private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(taskModel: TaskModel){
            binding.title.text = taskModel.tittle
            binding.description.text = taskModel.des
            binding.date.text = taskModel.date
            binding.time.text = taskModel.time

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskModel = getItem(position)
        holder.bind(taskModel)
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<TaskModel>(){
    override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
        return oldItem == newItem
    }

}