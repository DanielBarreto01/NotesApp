package com.example.notesapp.data.db

import com.example.notesapp.data.models.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insert(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    fun getTaskById(id: Int): Flow<Task?> {
        return taskDao.getTaskById(id)
    }

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
    }
}