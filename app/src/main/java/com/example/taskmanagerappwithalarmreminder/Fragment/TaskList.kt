package com.example.taskmanagerappwithalarmreminder.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanagerappwithalarmreminder.R
import com.example.taskmanagerappwithalarmreminder.adapter.TaskAdapter
import com.example.taskmanagerappwithalarmreminder.databinding.FragmentTaskListBinding
import com.example.taskmanagerappwithalarmreminder.viewModel.TaskViewModel


class TaskList : Fragment() {

    private lateinit var binding: FragmentTaskListBinding
    private val taskViewModel: TaskViewModel by viewModels()

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
        val adapter = TaskAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter

        taskViewModel.fetchAllTasks().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.addTask.setOnClickListener{
            findNavController().navigate(R.id.action_taskList_to_addTask)
        }
    }
}