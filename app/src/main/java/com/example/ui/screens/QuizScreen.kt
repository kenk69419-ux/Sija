package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Question
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuizScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val activeLesson = viewModel.activeLesson ?: return
    val questions = viewModel.activeQuestions
    val currentIndex = viewModel.currentQuestionIndex
    val checked = viewModel.quizAnswersChecked
    val correct = viewModel.isAnswerCorrect
    val completed = viewModel.quizCompleted
    val selectedOption = viewModel.selectedAnswer
    val selectedWords = viewModel.scrambledSelectedWords
    val unselectedWords = viewModel.scrambledUnselectedWords
    val isOutOfHearts = viewModel.isQuizOutofHearts
    val totalCorrectCount = viewModel.correctAnswersCount

    if (completed) {
        // --- VICTORY CELEBRATION SCREEN ---
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Large Gold Celebration Badge
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .shadow(8.dp, CircleShape)
                        .background(JavaGoldPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Piala Kemenangan",
                        tint = Color.White,
                        modifier = Modifier.size(72.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Apik Banget! 🥳",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = JavaGreenDark,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Sampeyan wis ngrampungake kelas\n\"${activeLesson.title}\"",
                    fontSize = 16.sp,
                    color = JavaneseBatikText,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Stats Summary Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // XP Earned (Bento Amber Box)
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = BentoAmberBg),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.2.dp, BentoAmberBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Celebration, contentDescription = "XP", tint = Color(0xFFD97706))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("XP Ditambah", fontSize = 12.sp, color = BentoAmberText, fontWeight = FontWeight.Bold)
                            Text("+${totalCorrectCount * 5 + 10} XP", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFF78350F))
                        }
                    }

                    // Accuracy rate (Bento Teal Box)
                    val accuracyPercentage = if (questions.isNotEmpty()) (totalCorrectCount * 100) / questions.size else 0
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = BentoTealBg),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.2.dp, BentoTealBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.DoneAll, contentDescription = "Benar", tint = JavaGreenPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Ketepatan", fontSize = 12.sp, color = BentoTealText, fontWeight = FontWeight.Bold)
                            Text("$accuracyPercentage%", fontSize = 20.sp, fontWeight = FontWeight.Black, color = JavaGreenDark)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                Button(
                    onClick = { viewModel.cancelQuiz() },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                        .testTag("quiz_complete_continue_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = JavaGreenPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Kembali ke Beranda", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
        return
    }

    if (isOutOfHearts) {
        // --- NO HEARTS GAME OVER SCREEN ---
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(DuolingoIncorrectRed.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Hati Habis",
                        tint = DuolingoIncorrectRed,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Aduh, Nyawa Habis! 💔",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DuolingoIncorrectRed,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Jangan menyerah, kanca! Pulihkan 5 Hati penuh dengan menukar 30 XP jika Anda memiliki saldo, atau kembali ke beranda untuk berlatih lagi.",
                    fontSize = 14.sp,
                    color = JavaneseSlateText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Recovery Action
                Button(
                    onClick = {
                        viewModel.buyHearts()
                        viewModel.cancelQuiz() // return to refresh state
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = JavaGreenPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FlashOn, contentDescription = "XP", tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pulihkan 5 Nyawa (Tukar 30 XP)", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { viewModel.cancelQuiz() },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = JavaneseBatikText),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Keluar ke Beranda", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    if (questions.isEmpty() || currentIndex >= questions.size) return
    val currentQuestion = questions[currentIndex]

    // Construct option pool for PILIHAN_GANDA / LISTENING
    val allOptions = remember(currentQuestion) {
        (currentQuestion.wrongOptionsCsv.split(",") + currentQuestion.correctAnswer)
            .filter { it.isNotEmpty() }
            .shuffled()
    }

    // --- MAIN INTERACTIVE QUESTION SHEET ---
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Exit Button
                    IconButton(onClick = { viewModel.cancelQuiz() }) {
                        Icon(Icons.Default.Close, contentDescription = "Exit Quiz", tint = JavaneseBatikText)
                    }

                    // Progress Bar
                    val progressValue = (currentIndex.toFloat()) / questions.size
                    LinearProgressIndicator(
                        progress = { progressValue },
                        modifier = Modifier
                            .weight(1f)
                            .height(14.dp)
                            .padding(horizontal = 12.dp)
                            .clip(CircleShape),
                        color = JavaGreenPrimary,
                        trackColor = Color.LightGray.copy(alpha = 0.3f),
                    )

                    // Remaining card count
                    Text(
                        text = "${currentIndex + 1}/${questions.size}",
                        fontWeight = FontWeight.ExtraBold,
                        color = JavaneseSlateText,
                        fontSize = 12.sp
                    )
                }
            }
        },
        bottomBar = {
            // Sliding bottom assessment sheets (Duolingo style)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        !checked -> MaterialTheme.colorScheme.surface
                        correct -> DuolingoCorrectGreenBg
                        else -> DuolingoIncorrectRedBg
                    }
                ),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(horizontal = 18.dp, vertical = 16.dp)
                ) {
                    if (checked) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = if (correct) DuolingoCorrectGreen else DuolingoIncorrectRed,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (correct) Icons.Default.Check else Icons.Default.PriorityHigh,
                                    contentDescription = if (correct) "Benar" else "Salah",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (correct) "Bener Banget! Hebat!" else "Kurang Tepat, Kanca!",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = if (correct) JavaGreenDark else JavaTerracotta
                                )
                                Text(
                                    text = "Kunci Jawaban: ${currentQuestion.correctAnswer}",
                                    fontSize = 13.sp,
                                    color = JavaneseSlateText,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    val buttonEnabled = when (currentQuestion.type) {
                        "SUSUN_KATA" -> selectedWords.isNotEmpty() || checked
                        else -> selectedOption != null || checked
                    }

                    Button(
                        onClick = {
                            if (!checked) {
                                viewModel.checkAnswer()
                            } else {
                                viewModel.nextQuestion()
                            }
                        },
                        enabled = buttonEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("quiz_action_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                !buttonEnabled -> Color.LightGray
                                checked -> if (correct) DuolingoCorrectGreen else DuolingoIncorrectRed
                                else -> JavaGreenPrimary
                            }
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (!checked) "PERIKSA JAWABAN" else "LANJUT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- CUTE INSTRUCTION BANNER ---
            Text(
                text = currentQuestion.secondaryPrompt,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = JavaneseBatikText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // --- DECORATIVE INTERACTIVE SPEAKER CHARACTER CARD ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                colors = CardDefaults.cardColors(containerColor = JavaGreenLight),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, JavaGreenPrimary.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sound Speach Icon
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(JavaGreenPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (currentQuestion.type) {
                                "LISTENING" -> Icons.Default.VolumeUp
                                "SUSUN_KATA" -> Icons.Default.List
                                else -> Icons.Default.Forum
                            },
                            contentDescription = "Speaker",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = currentQuestion.prompt,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = JavaGreenDark
                        )
                        if (currentQuestion.phoneticText.isNotEmpty()) {
                            Text(
                                text = "/ ${currentQuestion.phoneticText} /",
                                fontSize = 13.sp,
                                color = JavaneseSecondaryText,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // --- QUIZ GAMEPAD INTERACTIVE AREA ---
            when (currentQuestion.type) {
                "PILIHAN_GANDA", "LISTENING" -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        allOptions.forEach { option ->
                            val isSelected = selectedOption == option
                            val outlineColor = when {
                                isSelected -> if (checked) {
                                    if (correct) DuolingoCorrectGreen else DuolingoIncorrectRed
                                } else JavaGreenPrimary
                                else -> Color.LightGray.copy(alpha = 0.4f)
                            }

                            val contentBg = when {
                                isSelected -> if (checked) {
                                    if (correct) DuolingoCorrectGreenBg else DuolingoIncorrectRedBg
                                } else JavaGreenLight
                                else -> MaterialTheme.colorScheme.surface
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = !checked) { viewModel.selectOption(option) }
                                    .testTag("quiz_option_${option.replace(" ", "_")}"),
                                colors = CardDefaults.cardColors(containerColor = contentBg),
                                border = BorderStroke(if (isSelected) 3.dp else 1.5.dp, outlineColor),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Custom visual checkbox indicator
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .border(
                                                width = 2.dp,
                                                color = if (isSelected) JavaGreenPrimary else Color.LightGray,
                                                shape = CircleShape
                                            )
                                            .background(
                                                color = if (isSelected) JavaGreenPrimary else Color.Transparent,
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = option,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = JavaneseBatikText
                                    )
                                }
                            }
                        }
                    }
                }

                "SUSUN_KATA" -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // arranged words slot
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp)
                                .border(1.5.dp, Color.LightGray, RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                                .padding(12.dp)
                        ) {
                            if (selectedWords.isEmpty()) {
                                Text(
                                    text = "Ketuk kata di bawah untuk menyusun kalimat...",
                                    fontSize = 13.sp,
                                    color = JavaneseSecondaryText,
                                    modifier = Modifier.align(Alignment.Center),
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    selectedWords.forEach { word ->
                                        Card(
                                            modifier = Modifier.clickable(enabled = !checked) {
                                                viewModel.removeScrambledWord(word)
                                            },
                                            colors = CardDefaults.cardColors(containerColor = JavaGreenLight),
                                            border = BorderStroke(1.5.dp, JavaGreenPrimary),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = word,
                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                                fontWeight = FontWeight.Bold,
                                                color = JavaGreenDark,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // remaining word chips pool
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            unselectedWords.forEach { word ->
                                Card(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .clickable(enabled = !checked) {
                                            viewModel.addScrambledWord(word)
                                        }
                                        .testTag("susun_kata_chip_$word"),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(1.dp, Color.LightGray),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = word,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = JavaneseBatikText,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
