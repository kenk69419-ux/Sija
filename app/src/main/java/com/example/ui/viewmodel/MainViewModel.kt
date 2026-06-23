package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.Lesson
import com.example.data.model.Question
import com.example.data.model.UserProgress
import com.example.data.repository.LessonRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = LessonRepository(db)

    val userProgress: StateFlow<UserProgress?> = repository.userProgress
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val allLessons: StateFlow<List<Lesson>> = repository.allLessons
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val xpLogs = repository.xpLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            repository.checkAndSeedDatabase()
        }
    }

    // --- QUIZ GAME ENGINE STATE ---
    var activeLesson: Lesson? by mutableStateOf(null)
        private set

    var activeQuestions: List<Question> by mutableStateOf(emptyList())
        private set

    var currentQuestionIndex: Int by mutableStateOf(0)
        private set

    var selectedAnswer: String? by mutableStateOf(null)
        private set

    // For SUSUN_KATA unscrambling
    var scrambledSelectedWords: List<String> by mutableStateOf(emptyList())
        private set

    var scrambledUnselectedWords: List<String> by mutableStateOf(emptyList())
        private set

    var quizAnswersChecked: Boolean by mutableStateOf(false)
        private set

    var isAnswerCorrect: Boolean by mutableStateOf(false)
        private set

    var quizCompleted: Boolean by mutableStateOf(false)
        private set

    var correctAnswersCount: Int by mutableStateOf(0)
        private set

    var isQuizOutofHearts: Boolean by mutableStateOf(false)
        private set

    fun startQuiz(lesson: Lesson) {
        viewModelScope.launch {
            val qList = repository.getQuestionsForLesson(lesson.id)
            if (qList.isNotEmpty()) {
                activeLesson = lesson
                activeQuestions = qList
                currentQuestionIndex = 0
                selectedAnswer = null
                scrambledSelectedWords = emptyList()
                scrambledUnselectedWords = emptyList()
                quizAnswersChecked = false
                isAnswerCorrect = false
                quizCompleted = false
                correctAnswersCount = 0
                isQuizOutofHearts = false
                setupCurrentQuestion()
            }
        }
    }

    private fun setupCurrentQuestion() {
        if (activeQuestions.isEmpty() || currentQuestionIndex >= activeQuestions.size) return
        val currentQ = activeQuestions[currentQuestionIndex]
        selectedAnswer = null
        scrambledSelectedWords = emptyList()
        quizAnswersChecked = false
        isAnswerCorrect = false

        if (currentQ.type == "SUSUN_KATA") {
            // Set up word unscramble
            val correctWords = currentQ.prompt.split(" ").filter { it.isNotEmpty() }
            val fillerWords = currentQ.wrongOptionsCsv.split(",").filter { it.isNotEmpty() }
            val pool = (correctWords + fillerWords).shuffled()
            scrambledUnselectedWords = pool
        }
    }

    fun selectOption(option: String) {
        if (quizAnswersChecked) return
        selectedAnswer = option
    }

    fun addScrambledWord(word: String) {
        if (quizAnswersChecked) return
        scrambledSelectedWords = scrambledSelectedWords + word
        scrambledUnselectedWords = scrambledUnselectedWords - word
    }

    fun removeScrambledWord(word: String) {
        if (quizAnswersChecked) return
        scrambledUnselectedWords = scrambledUnselectedWords + word
        scrambledSelectedWords = scrambledSelectedWords - word
    }

    fun checkAnswer() {
        if (quizAnswersChecked) return
        val currentQ = activeQuestions[currentQuestionIndex]
        var correct = false

        when (currentQ.type) {
            "PILIHAN_GANDA", "LISTENING" -> {
                correct = selectedAnswer?.trim()?.lowercase() == currentQ.correctAnswer.trim().lowercase()
            }
            "SUSUN_KATA" -> {
                // Combine chosen words to form string
                val userSentence = scrambledSelectedWords.joinToString(" ").trim().lowercase()
                val correctSentence = currentQ.correctAnswer.trim().lowercase()
                // Let's also compare plain characters to ensure punctuation/case variations are clean
                val plainUser = userSentence.replace("[^A-Za-z0-9 ]".toRegex(), "")
                val plainCorrect = correctSentence.replace("[^A-Za-z0-9 ]".toRegex(), "")
                correct = plainUser == plainCorrect
            }
        }

        isAnswerCorrect = correct
        quizAnswersChecked = true

        if (correct) {
            correctAnswersCount++
        } else {
            // Deduct heart
            viewModelScope.launch {
                repository.deductHeart()
                // Check if out of hearts dynamically
                val currentP = repository.userProgress.stateIn(viewModelScope).value
                if (currentP != null && currentP.hearts <= 1) {
                    // Out of hearts soon! If heart count becomes 0, flag game over
                    if (currentP.hearts <= 0) {
                        isQuizOutofHearts = true
                    }
                }
            }
        }
    }

    fun nextQuestion() {
        viewModelScope.launch {
            val userP = userProgress.value
            if (userP != null && userP.hearts <= 0) {
                isQuizOutofHearts = true
                quizCompleted = true
                activeLesson = null
                return@launch
            }

            if (currentQuestionIndex + 1 < activeQuestions.size) {
                currentQuestionIndex++
                setupCurrentQuestion()
            } else {
                // Completed the quiz!
                val xpReward = correctAnswersCount * 5 + 10 // baseline rewards
                val lessonId = activeLesson?.id ?: 1
                repository.completeLesson(lessonId, xpReward)
                quizCompleted = true
            }
        }
    }

    fun buyHearts() {
        viewModelScope.launch {
            repository.buyHearts()
        }
    }

    fun resetAll() {
        viewModelScope.launch {
            repository.resetProgress()
        }
    }

    fun cancelQuiz() {
        activeLesson = null
        quizCompleted = false
    }
}
