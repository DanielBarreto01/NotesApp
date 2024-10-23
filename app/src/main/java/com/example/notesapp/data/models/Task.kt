package com.example.notesapp.data.models

data class Task(
    val id:Int,
    var title: String,
    var description: String,
    var isChecked: Boolean
)
