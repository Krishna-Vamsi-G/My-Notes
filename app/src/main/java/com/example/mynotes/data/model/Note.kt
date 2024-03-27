package com.example.mynotes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.ai.client.generativeai.type.Content

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: String // Consider using a more suitable type like Date and formatting it as needed

)
