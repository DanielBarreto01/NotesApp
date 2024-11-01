package com.example.notesapp.ui.screensFragments.add_tasks.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.data.models.Task
import com.example.notesapp.databinding.TaskBinding

class TaskAdapter(
    private var addListTask: List<Task>,
    private val onCheckClickListener: (id: Int) -> Unit,
    private val onTaskDetailClickListener: (id: Int) -> Unit
) : RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(
            binding = binding,
            onCheckClickListener = onCheckClickListener,
            onTaskDetailClickListener = onTaskDetailClickListener
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(addListTask[position])
    }

    override fun getItemCount(): Int = addListTask.size

    fun updateTaskList(taskList: List<Task>) {
        this.addListTask = taskList
        notifyDataSetChanged()
    }
}