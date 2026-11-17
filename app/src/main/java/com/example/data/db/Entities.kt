package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val isUser: Boolean,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val subjectTag: String? = null
)

@Entity(tableName = "study_notes")
data class StudyNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String,
    val gradeLevel: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isBookmarked: Boolean = true
)

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val gradeLevel: String,
    val paper: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val eczGrade: String, // e.g. "Distinction 1", "Credit 5"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_tasks")
data class StudyTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String,
    val targetDate: String,
    val isCompleted: Boolean = false,
    val priority: String = "Normal"
)

@Entity(tableName = "downloaded_papers")
data class DownloadedPaperEntity(
    @PrimaryKey val paperId: String,
    val subject: String,
    val gradeLevel: String,
    val year: Int,
    val paperTitle: String,
    val downloadedAt: Long = System.currentTimeMillis()
)
