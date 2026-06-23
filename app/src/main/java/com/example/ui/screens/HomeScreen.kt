package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Lesson
import com.example.data.model.UserProgress
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    userProgress: UserProgress,
    lessons: List<Lesson>,
    onStartLesson: (Lesson) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedUnitId by remember { mutableStateOf(1) } // 1 for Ngoko, 2 for Krama, 3 for Krama Inggil
    val filteredLessons = lessons.filter { it.unitId == selectedUnitId }
    val scrollState = rememberScrollState()

    // Dynamic progress calculations per unit
    val totalLessons = filteredLessons.size
    val completedLessons = filteredLessons.count { it.isCompleted }
    val progressFraction = if (totalLessons > 0) completedLessons.toFloat() / totalLessons.toFloat() else 0f
    val progressPercent = (progressFraction * 100).toInt()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(JavaneseLightBackground)
    ) {
        // --- TOP BENTO STATUS HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Elegant M3 Bento App Branding
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(JavaGreenPrimary, RoundedCornerShape(12.dp))
                        .shadow(1.dp, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "A",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Column {
                    Text(
                        text = "Aksara",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = JavaGreenDark,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "SinauJawa",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = JavaneseSecondaryText
                    )
                }
            }

            // High-Contrast Status Capsules (Bento Style)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Heart Vitality Capsule
                Row(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(24.dp))
                        .clickable {
                            if (userProgress.hearts < 5 && userProgress.xp >= 30) {
                                viewModel.buyHearts()
                            }
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Hati",
                        tint = DuolingoIncorrectRed,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userProgress.hearts}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = JavaneseBatikText
                    )
                    if (userProgress.hearts < 5) {
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Buy",
                            tint = JavaGreenPrimary,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }

                // Fire Streak Capsule
                Row(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(24.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔥",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userProgress.streak}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = JavaneseBatikText,
                        modifier = Modifier.testTag("streak_count")
                    )
                }

                // Gem / XP Gold Capsule
                Row(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(24.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💎",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userProgress.xp}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = JavaneseBatikText
                    )
                }
            }
        }

        // --- DASHBOARD AND PATHWAY SCROLLABLE SPACE ---
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            // --- PRIMARY LESSON CARD (LARGE BENTO) ---
            val unitTitleName = when (selectedUnitId) {
                1 -> "Ngoko Dasar"
                2 -> "Krama Madya"
                else -> "Krama Inggil"
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(32.dp)),
                colors = CardDefaults.cardColors(containerColor = JavaGreenPrimary),
                shape = RoundedCornerShape(32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Tag and Title Row
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "UNIT $selectedUnitId",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }
                            Text(
                                text = unitTitleName,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Sugeng Rawuh, ${userProgress.name}!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        lineHeight = 28.sp
                    )

                    Text(
                        text = when (selectedUnitId) {
                            1 -> "Belajar menyapa teman sebaya, keluarga akrab, dan situasi santai sehari-hari."
                            2 -> "Silaturahmi sopan dengan bahasa krama madya yang halus dan menghormati sesama."
                            else -> "Krama inggil tertinggi penuh rasa hormat luhur kepada sesepuh dan guru pepadi."
                        },
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Progress indicators
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "Kemajuan: $progressPercent%",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFFD700), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "+50 XP",
                                color = Color(0xFF423100),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Linear Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(Color.Black.copy(alpha = 0.15f), CircleShape)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction = progressFraction.coerceIn(0f, 1f))
                                .background(Color.White, CircleShape)
                        )
                    }
                }
            }

            // --- SECONDARY STATS GRID (SMALLER BENTO ITEMS) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Bento Item Left: Daily XP Tracker
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(130.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoTealBg),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.2.dp, BentoTealBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(JavaGreenPrimary, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = "Daily XP Growth",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "DAILY XP",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoTealText,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "${userProgress.xp} XP",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = JavaGreenDark
                            )
                        }
                    }
                }

                // Bento Item Right: Silver League / Sastrawan Level Tracker
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(130.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoAmberBg),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.2.dp, BentoAmberBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFFD97706), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Sastrawan Level Rank",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Tingkat ${userProgress.levelIndex}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoAmberText,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = when (userProgress.levelIndex) {
                                    1 -> "Muda"
                                    2 -> "Madya"
                                    else -> "Utama"
                                },
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF78350F)
                            )
                        }
                    }
                }
            }

            // --- BENTO SEGMENTED TABS ---
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .border(1.2.dp, Color(0xFFE2E8F0), RoundedCornerShape(24.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val unitTabs = listOf(
                    Triple(1, "Ngoko", "Sesi 1"),
                    Triple(2, "Krama", "Sesi 2"),
                    Triple(3, "Inggil", "Sesi 3")
                )

                unitTabs.forEach { (id, title, subtitle) ->
                    val isSelected = selectedUnitId == id
                    val tabBg = if (isSelected) JavaGreenPrimary else Color.Transparent
                    val tabTextColor = if (isSelected) Color.White else JavaneseSlateText
                    val subTextColor = if (isSelected) Color.White.copy(alpha = 0.8f) else JavaneseSecondaryText

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { selectedUnitId = id },
                        colors = CardDefaults.cardColors(containerColor = tabBg),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = title,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = tabTextColor
                            )
                            Text(
                                text = subtitle,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium,
                                color = subTextColor
                            )
                        }
                    }
                }
            }

            // --- BENTO DESCRIPTION BANNER ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.2.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(JavaGreenLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (selectedUnitId) {
                                1 -> Icons.Default.ChatBubble
                                2 -> Icons.Default.Face
                                else -> Icons.Default.Bookmark
                            },
                            contentDescription = "Hubungi",
                            tint = JavaGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        val catTitle = when (selectedUnitId) {
                            1 -> "Basa Jawa Ngoko"
                            2 -> "Basa Jawa Krama Madya"
                            else -> "Basa Jawa Krama Inggil"
                        }
                        val catDesc = when (selectedUnitId) {
                            1 -> "Basa akrab lan santai kanggo sesrawungan kanca kenthel sedina-dina."
                            2 -> "Tembung sopan kang laras kanggo pirembagan anyar utawa pasrawungan umum."
                            else -> "Unggah-ungguh dhuwur pinangka pakurmatan mring tiyang sepuh lan luhur."
                        }
                        Text(
                            text = catTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = JavaGreenDark
                        )
                        Text(
                            text = catDesc,
                            fontSize = 12.sp,
                            color = JavaneseSlateText,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // --- BENTO PREVIEW: LESSON MAP CONTAINER ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .heightIn(min = 400.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.2.dp, Color(0xFFE2E8F0))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Alur Belajar Sesi Ini",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = JavaGreenDark
                        )
                        Box(
                            modifier = Modifier
                                .background(BentoTealBg, RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "$completedLessons / $totalLessons Selesai",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = JavaGreenPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (filteredLessons.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = JavaGreenPrimary)
                        }
                    } else {
                        // Curved Drawing pathway
                        val pathLineColor = JavaGreenPrimary.copy(alpha = 0.25f)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .drawBehind {
                                    val radius = 35.dp.toPx()
                                    val stepHeight = 140.dp.toPx()
                                    val width = size.width

                                    for (i in 0 until filteredLessons.size - 1) {
                                        val startX = width / 2 + when (i % 3) {
                                            0 -> -40.dp.toPx()
                                            1 -> 40.dp.toPx()
                                            else -> 0f
                                        }
                                        val startY = 70.dp.toPx() + i * stepHeight + radius
                                        val endX = width / 2 + when ((i + 1) % 3) {
                                            0 -> -40.dp.toPx()
                                            1 -> 40.dp.toPx()
                                            else -> 0f
                                        }
                                        val endY = 70.dp.toPx() + (i + 1) * stepHeight - radius

                                        drawLine(
                                            color = pathLineColor,
                                            start = Offset(startX, startY),
                                            end = Offset(endX, endY),
                                            strokeWidth = 6f,
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                                        )
                                    }
                                }
                                .padding(bottom = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            filteredLessons.forEachIndexed { idx, lesson ->
                                val isUnlocked = idx == 0 || filteredLessons[idx - 1].isCompleted || lesson.isCompleted
                                val isCompleted = lesson.isCompleted

                                val horizontalOffset = when (idx % 3) {
                                    0 -> (-45).dp
                                    1 -> 45.dp
                                    else -> 0.dp
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .offset(x = horizontalOffset)
                                            .size(76.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // Progress Ring Glow
                                        Box(
                                            modifier = Modifier
                                                .size(76.dp)
                                                .background(
                                                    color = when {
                                                        isCompleted -> DuolingoCorrectGreen.copy(alpha = 0.15f)
                                                        isUnlocked -> JavaGreenPrimary.copy(alpha = 0.12f)
                                                        else -> Color.LightGray.copy(alpha = 0.15f)
                                                    },
                                                    shape = CircleShape
                                                )
                                        )

                                        // Node Button
                                        val nodeBgColor = when {
                                            isCompleted -> DuolingoCorrectGreen
                                            isUnlocked -> JavaGreenPrimary
                                            else -> Color(0xFFE2E8F0)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .size(58.dp)
                                                .shadow(
                                                    elevation = if (isUnlocked) 4.dp else 0.dp,
                                                    shape = CircleShape,
                                                    ambientColor = nodeBgColor,
                                                    spotColor = nodeBgColor
                                                )
                                                .background(nodeBgColor, CircleShape)
                                                .clickable(enabled = isUnlocked) {
                                                    onStartLesson(lesson)
                                                }
                                                .testTag("lesson_node_${lesson.id}"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = when {
                                                    isCompleted -> Icons.Default.Check
                                                    isUnlocked -> Icons.Default.PlayArrow
                                                    else -> Icons.Default.Lock
                                                },
                                                contentDescription = "Lesson Node",
                                                tint = if (isUnlocked) Color.White else Color(0xFF94A3B8),
                                                modifier = Modifier.size(26.dp)
                                            )
                                        }

                                        // Start Badge Label
                                        if (isUnlocked && !isCompleted) {
                                            Box(
                                                modifier = Modifier
                                                    .offset(y = (-32).dp)
                                                    .background(Color(0xFFFFD700), RoundedCornerShape(8.dp))
                                                    .border(1.dp, Color(0xFFD97706), RoundedCornerShape(8.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "MULAI",
                                                    fontWeight = FontWeight.Black,
                                                    fontSize = 8.sp,
                                                    color = Color(0xFF78350F)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Lesson Label details
                                    Card(
                                        modifier = Modifier
                                            .offset(x = horizontalOffset)
                                            .widthIn(max = 180.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isUnlocked) Color(0xFFF8FAFC) else Color.Transparent
                                        ),
                                        border = if (isUnlocked) BorderStroke(1.dp, Color(0xFFF1F5F9)) else null,
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "Sesi ${idx + 1}: ${lesson.title}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = if (isUnlocked) JavaGreenDark else JavaneseSecondaryText,
                                                textAlign = TextAlign.Center
                                            )
                                            Text(
                                                text = lesson.description,
                                                fontSize = 9.sp,
                                                color = if (isUnlocked) JavaneseSlateText else JavaneseSecondaryText,
                                                textAlign = TextAlign.Center,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
