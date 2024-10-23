package com.example.notesapp.ui.screensFragments.add_tasks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.R
import com.example.notesapp.data.viewModel.TaskViewModel
import com.example.notesapp.databinding.FragmentAddTasksBinding
import com.example.notesapp.ui.screensFragments.add_tasks.rv.TaskAdapter
import kotlinx.coroutines.launch

class FragmentAddTasks : Fragment() {

    private lateinit var binding: FragmentAddTasksBinding
    private lateinit var rvTaskAdapter: TaskAdapter
    private val taskViewModel: TaskViewModel by activityViewModels()

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
            taskViewModel.addTask(binding.imputTask.text.toString())
            cleanField()
            updateUiState()
        }
}

private fun cleanField() {
    binding.imputTask.setText("")
}

    private fun checkTsk(idTask: Int) {
    taskViewModel.taskList.find { it.id == idTask }!!.isChecked = true
    taskViewModel.setPendingTasks(taskList = taskViewModel.taskList)
    updateUiState()
}
    @SuppressLint("NotifyDataSetChanged")
    private fun updateUiState() {
        lifecycleScope.launch {
            taskViewModel.uiState.collect { uiState ->
                if (uiState.taskList.isNotEmpty()) {
                    rvTaskAdapter.updateTaskList(uiState.getPending())
                    rvTaskAdapter.notifyDataSetChanged()
                }
            }
        }


    }
        private fun initRv() {
        rvTaskAdapter = TaskAdapter(
            taskViewModel.taskList,
            onCheckClickListener = { idTask ->
                checkTsk(idTask) },
            onTaskDetailClickListener ={ idTask ->
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
        taskViewModel.setSelectedTask(idTask)
        findNavController().navigate(R.id.action_fragmentAddTasks_to_fragmentDetailTasks)
    }
}
