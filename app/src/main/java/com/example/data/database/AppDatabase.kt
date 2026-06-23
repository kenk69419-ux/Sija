package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Lesson
import com.example.data.model.Question
import com.example.data.model.UserProgress
import com.example.data.model.XpLog
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE id = 1 LIMIT 1")
    fun getUserProgressFlow(): Flow<UserProgress?>

    @Query("SELECT * FROM user_progress WHERE id = 1 LIMIT 1")
    suspend fun getUserProgress(): UserProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProgress(progress: UserProgress)

    @Update
    suspend fun updateUserProgress(progress: UserProgress)
}

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons ORDER BY unitId ASC, id ASC")
    fun getAllLessonsFlow(): Flow<List<Lesson>>

    @Query("SELECT * FROM lessons WHERE id = :lessonId LIMIT 1")
    suspend fun getLessonById(lessonId: Int): Lesson?

    @Query("SELECT * FROM lessons WHERE unitId = :unitId ORDER BY id ASC")
    fun getLessonsByUnitFlow(unitId: Int): Flow<List<Lesson>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLessons(lessons: List<Lesson>)

    @Query("UPDATE lessons SET isCompleted = :isCompleted WHERE id = :lessonId")
    suspend fun updateLessonCompletion(lessonId: Int, isCompleted: Boolean)
}

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions WHERE lessonId = :lessonId")
    suspend fun getQuestionsForLesson(lessonId: Int): List<Question>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)
}

@Dao
interface XpLogDao {
    @Query("SELECT * FROM xp_logs ORDER BY timestamp DESC LIMIT 50")
    fun getAllXpLogsFlow(): Flow<List<XpLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertXpLog(log: XpLog)
}

@Database(
    entities = [UserProgress::class, Lesson::class, Question::class, XpLog::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProgressDao(): UserProgressDao
    abstract fun lessonDao(): LessonDao
    abstract fun questionDao(): QuestionDao
    abstract fun xpLogDao(): XpLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sinau_jawa_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
