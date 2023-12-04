package com.example.taskmanagerappwithalarmreminder.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.example.taskmanagerappwithalarmreminder.R
import com.example.taskmanagerappwithalarmreminder.adapter.SwipeToDeleteCallback
import com.example.taskmanagerappwithalarmreminder.adapter.TaskAdapter
import com.example.taskmanagerappwithalarmreminder.databinding.FragmentTaskListBinding
import com.example.taskmanagerappwithalarmreminder.entities.TaskModel
import com.example.taskmanagerappwithalarmreminder.viewModel.TaskViewModel


class TaskList : Fragment() {

    private lateinit var binding: FragmentTaskListBinding
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init adapter and set Model
        adapter = TaskAdapter(::taskAction)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter

        taskViewModel.fetchAllTasks().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // init swipe to delete
        val swipeToDeleteCallback = SwipeToDeleteCallback(requireContext(), adapter)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.addTask.setOnClickListener{
            findNavController().navigate(R.id.action_taskList_to_addTask)
        }
    }

    private fun taskAction(taskModel: TaskModel, tag: String){
        when(tag){
            "Edit" -> taskViewModel.updateTask(taskModel) // edit checkbox data from room
            "Delete" -> {
                taskViewModel.deleteTask(taskModel) // delete row data from room

                // cancel notification
                WorkManager.getInstance(requireContext()).cancelUniqueWork(taskModel.tittle)
            }
        }
    }
}