package com.example.notesapp.ui.screensFragments.detailTasks

import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.notesapp.data.models.Task
import com.example.notesapp.data.viewModel.TaskViewModel
import com.example.notesapp.databinding.FragmentDetailTaskBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FragmentDetailTasks : Fragment() {

    private lateinit var binding: FragmentDetailTaskBinding
    private val taskViewModel: TaskViewModel by activityViewModels()
    private val args: FragmentDetailTasksArgs by navArgs()
    private var currentTask: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailTaskBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        val idTask = args.idTask
        lifecycleScope.launch {
            taskViewModel.getTaskById(idTask).collect { task ->
                if (task != null) {
                    currentTask = task
                    // Configure the initial view
                    with(binding) {
                        tvTaskTitle.setText(task.title)
                        cbIsCompleted.isChecked = task.isChecked
                        etDescriptionTask.setText(task.description)

                        if (task.isChecked) {
                            tvTaskTitle.paintFlags = tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        }
                    }

                    // Setup listeners for changes
                    setupListeners()
                } else {
                    Toast.makeText(context, "Task not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.tvTaskTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No action needed here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etDescriptionTask.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No action needed here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.cbIsCompleted.setOnCheckedChangeListener { _, _ ->
            // No action needed here
        }
    }

    override fun onPause() {
        super.onPause()
        currentTask?.let { task ->
            val updatedTask = task.copy(
                title = binding.tvTaskTitle.text.toString(),
                description = binding.etDescriptionTask.text.toString(),
                isChecked = binding.cbIsCompleted.isChecked
            )
            lifecycleScope.launch {
                taskViewModel.updateTask(updatedTask)
            }
        }
    }
}