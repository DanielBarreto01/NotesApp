package com.example.notesapp.ui.screensFragments.completedTasks

import android.annotation.SuppressLint
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
import com.example.notesapp.data.viewModel.TaskViewModel
import com.example.notesapp.databinding.FragmentCompletedTasksBinding
import com.example.notesapp.ui.screensFragments.add_tasks.rv.TaskAdapter
import kotlinx.coroutines.launch

class FragmentCompletedTasks : Fragment() {

    private lateinit var binding: FragmentCompletedTasksBinding
    private lateinit var rvTasksAdapter: TaskAdapter
    private val tasksViewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompletedTasksBinding.inflate(inflater, container, false)
        initRv()
        return binding.root
    }

    private fun initRv() {
        rvTasksAdapter = TaskAdapter(
            tasksViewModel.uiState.value.getCompleted(),
            onCheckClickListener = { idTask ->
                checkTask(idTask)
            },
            onTaskDetailClickListener = { idTask ->
                launchActivityDetail(idTask)
            }
        )
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rvTasksAdapter
        }
    }

    private fun launchActivityDetail(idTask: Int) {
        tasksViewModel.setSelectedTask(idTask)
        findNavController().navigate(R.id.action_fragmentCompletedTasks_to_fragmentDetailTasks)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkTask(idTask: Int) {
        tasksViewModel.taskList.find { it.id == idTask }?.isChecked = false
        updateUiState()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUiState() {
        lifecycleScope.launch {
            tasksViewModel.uiState.collect { uiState ->
                if (uiState.taskList.isNotEmpty()) {
                    rvTasksAdapter.updateTaskList(uiState.getCompleted())
                    rvTasksAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}