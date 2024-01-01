package com.example.taskmanagerappwithalarmreminder.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


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
            if (it.isEmpty()){
                binding.emptyText.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }else {
                binding.emptyText.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(it)
                Log.d("dataTable:", it.toString())
            }
        }

        // Initialize the DatePicker
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select start date")
            .build()

        val endDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select end date")
            .build()


        // Set OnClickListener on the EditText
        binding.dateText.setOnClickListener {
            datePicker.show(childFragmentManager, "DATE_PICKER")
        }

        // Handle the date selection
        datePicker.addOnPositiveButtonClickListener { selection ->
            // Format the date and set it to the EditText
            val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
            val formattedDate = Instant.ofEpochMilli(selection)
                .atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
            binding.dateText.setText(formattedDate)
        }

        // Set OnClickListener on the EditText
        binding.endDateText.setOnClickListener {
            endDatePicker.show(childFragmentManager, "DATE_PICKER")
        }

        // Handle the date selection
        endDatePicker.addOnPositiveButtonClickListener { selection ->
            // Format the date and set it to the EditText
            val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
            val formattedDate = Instant.ofEpochMilli(selection)
                .atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
            binding.endDateText.setText(formattedDate)
        }

        // init swipe to delete
        val swipeToDeleteCallback = SwipeToDeleteCallback(requireContext(), adapter)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.addTask.setOnClickListener{
            findNavController().navigate(R.id.action_taskList_to_addTask)
        }

        binding.toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                val checkedButton = toggleButton.findViewById<Button>(checkedId)

                binding.textView3.text = "Task overview ${checkedButton.text}:"

                // search data by category High/Low/Normal
                taskViewModel.fetchAllTasksByCategory(checkedButton.text.toString())
                    .observe(viewLifecycleOwner) {
                        if (it.isEmpty()){
                            binding.emptyText.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        }else {
                            binding.emptyText.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                            adapter.submitList(it)
                        }
                    }
            }
        }

        binding.search.setOnClickListener {
            val startDate = binding.dateText.text.toString()
            val endDate = binding.endDateText.text.toString()

            if (startDate.isNullOrBlank()){
                Toast.makeText(requireContext(), "Please provide a valid date", Toast.LENGTH_LONG).show()
            }else if (endDate.isNullOrBlank()){
                Toast.makeText(requireContext(), "Please provide a valid time", Toast.LENGTH_LONG).show()
            }else{
                taskViewModel.getTaskWithDateFilter(startDate, endDate)
                    .observe(viewLifecycleOwner) {
                        if (it.isEmpty()){
                            binding.emptyText.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        }else {
                            binding.emptyText.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                            adapter.submitList(it)
                        }
                    }
            }
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