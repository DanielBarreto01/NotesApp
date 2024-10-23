package com.example.notesapp.ui.screensFragments.add_tasks.uiState

import com.example.notesapp.data.models.Task

data class TaskUiState (
    val taskList: List<Task> = emptyList()
) {
    fun getPending() = taskList.filter { it.isChecked.not() }
    fun getCompleted() = taskList.filter { it.isChecked }
}