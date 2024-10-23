package com.example.notesapp.ui.screensFragments.add_tasks.rv

import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.data.models.Task
import com.example.notesapp.databinding.FragmentAddTasksBinding
import com.example.notesapp.databinding.TaskBinding

class TaskViewHolder(
    private val binding: TaskBinding,
    private val onCheckClickListener: (id: Int) -> Unit,
    private val onTaskDetailClickListener: (id: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(task: Task) {
        with(binding) {
            tvTaskTitle.text = task.title
            taskIsCompleted.isChecked = task.isChecked

            taskIsCompleted.setOnClickListener {
                onCheckClickListener(task.id)
            }

            root.setOnClickListener {
                onTaskDetailClickListener(task.id)
            }
        }
    }
}