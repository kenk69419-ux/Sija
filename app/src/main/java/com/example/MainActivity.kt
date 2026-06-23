package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.JavaGreenPrimary
import com.example.ui.screens.*
import com.example.ui.viewmodel.MainViewModel

enum class LearnTab {
    BELAJAR, KAMUS, PERINGKAT, PROFIL
}

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Collect states from modern reactive architecture
                val userProgress by viewModel.userProgress.collectAsState()
                val lessons by viewModel.allLessons.collectAsState()
                val xpLogs by viewModel.xpLogs.collectAsState()

                val activeLesson = viewModel.activeLesson

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (activeLesson != null) {
                        // IMMERSIVE GAMIFIED QUIZ MODE
                        QuizScreen(viewModel = viewModel)
                    } else {
                        // STANDARD PORTAL DASHBOARD WITH NAVIGATION BAR
                        var currentTab by remember { mutableStateOf(LearnTab.BELAJAR) }

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            bottomBar = {
                                NavigationBar(
                                    modifier = Modifier.testTag("app_navigation_bar"),
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    tonalElevation = 8.dp
                                ) {
                                    // 1. Belajar Path
                                    NavigationBarItem(
                                        selected = currentTab == LearnTab.BELAJAR,
                                        onClick = { currentTab = LearnTab.BELAJAR },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentTab == LearnTab.BELAJAR) Icons.Filled.Book else Icons.Outlined.Book,
                                                contentDescription = "Menu Belajar"
                                            )
                                        },
                                        label = { Text("Sinau") },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = JavaGreenPrimary,
                                            selectedTextColor = JavaGreenPrimary,
                                            indicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                        ),
                                        modifier = Modifier.testTag("tab_belajar")
                                    )

                                    // 2. Kamus Grammar
                                    NavigationBarItem(
                                        selected = currentTab == LearnTab.KAMUS,
                                        onClick = { currentTab = LearnTab.KAMUS },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentTab == LearnTab.KAMUS) Icons.Filled.Bookmark else Icons.Outlined.Bookmark,
                                                contentDescription = "Menu Kamus"
                                            )
                                        },
                                        label = { Text("Kamus") },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = JavaGreenPrimary,
                                            selectedTextColor = JavaGreenPrimary,
                                            indicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                        ),
                                        modifier = Modifier.testTag("tab_kamus")
                                    )

                                    // 3. Peringkat Arena
                                    NavigationBarItem(
                                        selected = currentTab == LearnTab.PERINGKAT,
                                        onClick = { currentTab = LearnTab.PERINGKAT },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentTab == LearnTab.PERINGKAT) Icons.Filled.EmojiEvents else Icons.Outlined.EmojiEvents,
                                                contentDescription = "Menu Peringkat"
                                            )
                                        },
                                        label = { Text("Peringkat") },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = JavaGreenPrimary,
                                            selectedTextColor = JavaGreenPrimary,
                                            indicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                        ),
                                        modifier = Modifier.testTag("tab_peringkat")
                                    )

                                    // 4. Profil Admin
                                    NavigationBarItem(
                                        selected = currentTab == LearnTab.PROFIL,
                                        onClick = { currentTab = LearnTab.PROFIL },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentTab == LearnTab.PROFIL) Icons.Filled.Person else Icons.Outlined.Person,
                                                contentDescription = "Menu Profil"
                                            )
                                        },
                                        label = { Text("Profil") },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = JavaGreenPrimary,
                                            selectedTextColor = JavaGreenPrimary,
                                            indicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                        ),
                                        modifier = Modifier.testTag("tab_profil")
                                    )
                                }
                            }
                        ) { paddingValues ->
                            val contentModifier = Modifier.padding(paddingValues)

                            if (userProgress == null) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(androidx.compose.ui.Alignment.Center),
                                        color = JavaGreenPrimary
                                    )
                                }
                            } else {
                                when (currentTab) {
                                    LearnTab.BELAJAR -> {
                                        HomeScreen(
                                            viewModel = viewModel,
                                            userProgress = userProgress!!,
                                            lessons = lessons,
                                            onStartLesson = { viewModel.startQuiz(it) },
                                            modifier = contentModifier
                                        )
                                    }
                                    LearnTab.KAMUS -> {
                                        TheoryScreen(modifier = contentModifier)
                                    }
                                    LearnTab.PERINGKAT -> {
                                        LeaderboardScreen(
                                            userProgress = userProgress!!,
                                            modifier = contentModifier
                                        )
                                    }
                                    LearnTab.PROFIL -> {
                                        ProfileScreen(
                                            viewModel = viewModel,
                                            userProgress = userProgress!!,
                                            xpLogs = xpLogs,
                                            modifier = contentModifier
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
}
