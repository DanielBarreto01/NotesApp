package com.example.notesapp.ui.screensFragments.detailTasks

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.notesapp.data.viewModel.TaskViewModel
import com.example.notesapp.databinding.FragmentDetailTaskBinding
import kotlinx.coroutines.launch


class FragmentDetailTasks : Fragment(){

    private lateinit var binding: FragmentDetailTaskBinding
    private val taskViewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailTaskBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }

    private fun initView() {


            binding.cbIsCompleted.setOnClickListener{
                if(binding.cbIsCompleted.isChecked)
                    binding.tvTaskTitle.paintFlags = binding.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else
                    binding.tvTaskTitle.paintFlags = binding.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            lifecycleScope.launch {
                val task = taskViewModel.getSelectedTask()
                taskViewModel.uiStateDetail.collect{ uiStateDetail ->
                    with(binding) {
                    tvTaskTitle.setText(task.title)
                        cbIsCompleted.isChecked = task.isChecked
                        etDescriptionTask.setText(task.description)

                        if(task.isChecked){
                            tvTaskTitle.paintFlags = tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        }
                    }
                }
            }
    }

   override fun onPause() {
    super.onPause()
    with(binding) {
        taskViewModel.taskList.find { it.id == taskViewModel.getSelectedTask().id }?.let { task ->
            if (tvTaskTitle.text.toString().trim() != "") {
                task.title = tvTaskTitle.text.toString()
                task.description = etDescriptionTask.text.toString()
                task.isChecked = cbIsCompleted.isChecked
                taskViewModel.updateUiState()
            } else {
                Toast.makeText(context, "No puede dejar el título vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
}