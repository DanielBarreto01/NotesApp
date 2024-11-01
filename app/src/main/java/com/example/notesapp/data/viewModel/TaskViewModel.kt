package com.example.notesapp.data.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.db.TaskRepository
import com.example.notesapp.data.models.Task
import com.example.notesapp.ui.screensFragments.add_tasks.uiState.TaskUiState
import com.example.notesapp.ui.screensFragments.detailTasks.uiState.DetailTaskUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    private val _uiStateDetail = MutableStateFlow(DetailTaskUiState())
    val uiStateDetail: StateFlow<DetailTaskUiState> = _uiStateDetail.asStateFlow()

    private lateinit var selectedTask: Task

    init {
        // Observamos los cambios en la base de datos para mantener el `uiState` actualizado
        viewModelScope.launch {
            repository.allTasks.collect { taskList ->
                _uiState.value = _uiState.value.copy(taskList = taskList)
            }
        }
    }

    fun setPendingTasks(taskList: List<Task>) {
        updateUiState(taskList)
    }

    fun updateUiState(taskList: List<Task>) {
        viewModelScope.launch {
            val newUiState = _uiState.value.copy(
                taskList = taskList
            )
            _uiState.value = newUiState
        }
    }

    fun getSelectedTask(): Task {
        return selectedTask
    }

    fun addTask(title: String) {
        viewModelScope.launch {
            // Añade una tarea a la base de datos en lugar de a una lista local
            val task = Task(title = title, description = "", isChecked = false)
            repository.addTask(task)
            Log.d("TaskViewModel", "Tarea agregada: $task")
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.update(task)
        }
    }

    fun getTaskById(id: Int): Flow<Task?> {
        return repository.getTaskById(id) // Ensure this returns a Flow<Task?>
    }


    fun setSelectedTask(idTask: Int) {
        viewModelScope.launch {
            repository.getTaskById(idTask).collect { task ->
                task?.let {
                    selectedTask = it
                    _uiStateDetail.value = DetailTaskUiState(
                        id = it.id,
                        taskTitle = it.title,
                        taskDescription = it.description,
                        isChecked = it.isChecked
                    )
                }
            }
        }
    }
}