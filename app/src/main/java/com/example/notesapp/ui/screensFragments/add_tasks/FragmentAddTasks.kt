package com.example.notesapp.ui.screensFragments.add_tasks

import TaskViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.R
import com.example.notesapp.data.db.TaskDatabase
import com.example.notesapp.data.db.TaskRepository
import com.example.notesapp.data.viewModel.TaskViewModel
import com.example.notesapp.databinding.FragmentAddTasksBinding
import com.example.notesapp.ui.screensFragments.add_tasks.rv.TaskAdapter
import kotlinx.coroutines.launch

class FragmentAddTasks : Fragment() {

    private lateinit var binding: FragmentAddTasksBinding
    private lateinit var rvTaskAdapter: TaskAdapter
    private val taskViewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(TaskRepository(TaskDatabase.getDatabase(requireContext()).taskDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTasksBinding.inflate(inflater, container, false)
        initRv()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCompletadas.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAddTasks_to_fragmentCompletedTasks2)
        }
        binding.btnAgregar.setOnClickListener {
            val taskTitle = binding.imputTask.text.toString()
            if (taskTitle.isNotEmpty()) {
                taskViewModel.addTask(taskTitle)
                cleanField()
            }
        }

        // Observe the uiState and update the adapter
        lifecycleScope.launch {
            taskViewModel.uiState.collect { uiState ->
                rvTaskAdapter.updateTaskList(uiState.getPending())
            }
        }
    }

    private fun cleanField() {
        binding.imputTask.setText("")
    }

    private fun checkTsk(idTask: Int) {
        lifecycleScope.launch {
            val taskList = taskViewModel.uiState.value.taskList
            taskList.find { it.id == idTask }?.isChecked = true
            taskViewModel.setPendingTasks(taskList)
            updateUiState()
        }
    }

    private fun updateUiState() {
        lifecycleScope.launch {
            taskViewModel.uiState.collect { uiState ->
                if (uiState.taskList.isNotEmpty()) {
                    rvTaskAdapter.updateTaskList(uiState.getPending())
                }
            }
        }
    }

    private fun initRv() {
        rvTaskAdapter = TaskAdapter(
            emptyList(),
            onCheckClickListener = { idTask ->
                checkTsk(idTask)
            },
            onTaskDetailClickListener = { idTask ->
                launchFragmentDetailTask(idTask)
            }
        )
        binding.rvTareas.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rvTaskAdapter
        }
    }

    override fun onPause() {
        super.onPause()
        cleanField()
    }

    private fun launchFragmentDetailTask(idTask: Int) {
        val action = FragmentAddTasksDirections.actionFragmentAddTasksToFragmentDetailTasks(idTask)
        findNavController().navigate(action)
    }
}