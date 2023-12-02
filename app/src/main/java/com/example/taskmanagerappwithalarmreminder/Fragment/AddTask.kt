package com.example.taskmanagerappwithalarmreminder.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.taskmanagerappwithalarmreminder.R
import com.example.taskmanagerappwithalarmreminder.databinding.FragmentAddTaskBinding
import com.example.taskmanagerappwithalarmreminder.db.TaskDatabase
import com.example.taskmanagerappwithalarmreminder.entities.TaskModel
import com.example.taskmanagerappwithalarmreminder.viewModel.TaskViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AddTask : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private val taskViewModel : TaskViewModel by viewModels()
    private var selectedText: String = "High"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the DatePicker
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
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

        //initialize the TimePicker
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText("Select time")
                .build()

        binding.timeText.setOnClickListener{
            picker.show(childFragmentManager, "time_picker");
        }

        picker.addOnPositiveButtonClickListener {
            // Format the hour and minute
            val hour = picker.hour
            val minute = picker.minute
            val formattedTime = String.format("%02d:%02d %s",
                if (hour == 0 || hour == 12) 12 else hour % 12,
                minute,
                if (hour < 12) "AM" else "PM")

            // Set the formatted time to the TextView
            binding.timeText.setText(formattedTime)
        }

        // chipGroup
        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            for (id in checkedIds) {
                val chip = group.findViewById<Chip>(id)
                if (chip.isChecked){
                    selectedText = chip.text.toString()
                }
            }
        }

        binding.saveButton.setOnClickListener{
            val tittle = binding.tittleText.text.toString()
            val description = binding.desText.text.toString()
            val date = binding.dateText.text.toString()
            val time = binding.timeText.text.toString()

            if (tittle.isNullOrBlank()){
                binding.tittleText.error = "Please provide a tittle"
            }else if (tittle.length > 21){
                binding.tittleText.error = "Tittle length can't getter than 20"
            }else if (description.isNullOrBlank()){
                binding.desText.error = "Please provide description"
            }else if (date.isNullOrBlank()){
                Toast.makeText(requireContext(), "Please provide a valid date", Toast.LENGTH_LONG).show()
            }else if (time.isNullOrBlank()){
                Toast.makeText(requireContext(), "Please provide a valid time", Toast.LENGTH_LONG).show()
            }else{
                val task = TaskModel(tittle = tittle,
                    des = description,
                    date = date,
                    time = time,
                    category = selectedText)

                // save data
                taskViewModel.insertTask(task)
                Toast.makeText(requireContext(), "Task save successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addTask_to_taskList)
            }
        }
    }
}