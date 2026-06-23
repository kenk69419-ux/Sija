package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.data.model.UserProgress
import com.example.data.model.XpLog
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    userProgress: UserProgress,
    xpLogs: List<XpLog>,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(JavaneseLightBackground)
    ) {
        // --- BENTO HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                        text = "S",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Column {
                    Text(
                        text = "Sowan Adat",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = JavaGreenDark,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Profil Sastrawan Jawa",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = JavaneseSecondaryText
                    )
                }
            }
        }

        // --- DASHBOARD AND PROFILE INFO (SCROLLABLE BENTO GRIDS) ---
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Elegant Bento Profile Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(32.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(1.2.dp, Color(0xFFE2E8F0))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile initials avatar bubble
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(BentoTealBg, CircleShape)
                            .border(1.2.dp, BentoTealBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .shadow(2.dp, CircleShape)
                                .background(JavaGreenPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userProgress.name.take(1).uppercase(),
                                color = Color.White,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = userProgress.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = JavaGreenDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .background(BentoAmberBg, RoundedCornerShape(12.dp))
                            .border(1.dp, BentoAmberBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Tingkat ${userProgress.levelIndex}: Sastrawan Muda",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoAmberText
                        )
                    }
                }
            }

            // Achievements Badges grid-box
            Text(
                text = "Medali Kebudayaan Jawa",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = JavaGreenDark,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Badge 1: Keris Emas
                val hasKeris = userProgress.xp >= 100
                AchievementBadgeCard(
                    modifier = Modifier.weight(1f),
                    title = "Keris Emas",
                    desc = "Tembus 100+ XP belajar Basa Jawa",
                    icon = Icons.Default.Shield,
                    activeColor = JavaGoldPrimary,
                    isActive = hasKeris
                )

                // Badge 2: Pendekar Ngoko
                val hasNgoko = userProgress.xp >= 40
                AchievementBadgeCard(
                    modifier = Modifier.weight(1f),
                    title = "Pendekar Ngoko",
                    desc = "Selesaikan Sesi Ngoko harian",
                    icon = Icons.Default.Chat,
                    activeColor = JavaGreenPrimary,
                    isActive = hasNgoko
                )

                // Badge 3: Sowan Adat
                val hasStreak = userProgress.streak >= 2
                AchievementBadgeCard(
                    modifier = Modifier.weight(1f),
                    title = "Sowan Adat",
                    desc = "Miliki rekor streak harian aktif",
                    icon = Icons.Default.LocalFireDepartment,
                    activeColor = JavaTerracotta,
                    isActive = hasStreak
                )
            }

            // Real Time XP Logs (Large White Bento Card)
            Text(
                text = "Riwayat Sinau (Room Database)",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = JavaGreenDark,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )

            if (xpLogs.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.2.dp, Color(0xFFE2E8F0))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.HourglassEmpty,
                            contentDescription = "Kosong",
                            tint = Color.LightGray,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Belum ada kelas yang diselesaikan kanca.",
                            fontSize = 12.sp,
                            color = JavaneseSecondaryText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.2.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        xpLogs.forEachIndexed { index, log ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .background(
                                                if (log.xpGained >= 0) JavaGreenLight else Color(0xFFFDE2E2),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (log.xpGained >= 0) Icons.Default.AddCircle else Icons.Default.Cancel,
                                            contentDescription = "Log",
                                            tint = if (log.xpGained >= 0) JavaGreenPrimary else DuolingoIncorrectRed,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(log.source, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = JavaGreenDark)
                                        Text(dateFormat.format(Date(log.timestamp)), fontSize = 10.sp, color = JavaneseSecondaryText)
                                    }
                                }
                                Text(
                                    text = if (log.xpGained >= 0) "+${log.xpGained} XP" else "${log.xpGained} XP",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = if (log.xpGained >= 0) JavaGreenPrimary else DuolingoIncorrectRed
                                )
                            }
                            if (index < xpLogs.size - 1) {
                                HorizontalDivider(color = Color(0xFFF1F5F9))
                            }
                        }
                    }
                }
            }

            // Developer options Reset Button (Styled matching bento outline)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { viewModel.resetAll() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("reset_progress_button"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DuolingoIncorrectRed),
                border = BorderStroke(1.2.dp, DuolingoIncorrectRed.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.DeleteSweep,
                        contentDescription = "Reset",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Mulai Ulang Kemajuan Belajar (Reset)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AchievementBadgeCard(
    isActive: Boolean,
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    activeColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) activeColor.copy(alpha = 0.08f) else Color(0xFFF8FAFC)
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 1.2.dp,
            color = if (isActive) activeColor.copy(alpha = 0.25f) else Color(0xFFE2E8F0)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        color = if (isActive) activeColor.copy(alpha = 0.15f) else Color(0xFFF1F5F9),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isActive) activeColor else Color(0xFF94A3B8),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                color = if (isActive) JavaGreenDark else Color(0xFF64748B),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                fontSize = 8.sp,
                color = JavaneseSecondaryText,
                textAlign = TextAlign.Center,
                lineHeight = 10.sp
            )
        }
    }
}
