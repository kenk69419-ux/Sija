package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey val id: Int = 1,
    val name: String = "Sobat Sinau",
    val xp: Int = 0,
    val streak: Int = 0,
    val lastActiveTime: Long = 0L,
    val levelIndex: Int = 1,
    val hearts: Int = 5
)

@Entity(tableName = "lessons")
data class Lesson(
    @PrimaryKey val id: Int,
    val unitId: Int, // 1 for Ngoko, 2 for Krama, 3 for Krama Inggil
    val title: String,
    val description: String,
    val category: String, // "Ngoko" / "Krama" / "Krama Inggil"
    val isCompleted: Boolean = false,
    val totalQuestions: Int = 5
)

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lessonId: Int,
    val type: String, // "PILIHAN_GANDA", "SUSUN_KATA", "LISTENING"
    val prompt: String, // Javanese text
    val secondaryPrompt: String, // Indonesian instruction / context
    val correctAnswer: String, // Correct translation or phrase order
    val wrongOptionsCsv: String, // Wrong options, or other word options for unscramble
    val phoneticText: String = "" // Phonetic Javanese pronunciation helpers
)

@Entity(tableName = "xp_logs")
data class XpLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val xpGained: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val source: String // e.g. "Lesson 1 Quiz", "Daily Login"
)
