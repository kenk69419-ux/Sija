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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProgress
import com.example.ui.theme.*

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val level: Int,
    val xp: Int,
    val isCurrentUser: Boolean = false
)

@Composable
fun LeaderboardScreen(
    userProgress: UserProgress,
    modifier: Modifier = Modifier
) {
    // Generate dynamic ranks incorporating active user progress
    val mockPlayers = listOf(
        LeaderboardUser(1, "Raden Mas Gondo", 9, 860),
        LeaderboardUser(2, "Siti Kartini", 7, 650),
        LeaderboardUser(3, "Bagus Wijaya", 5, 450),
        LeaderboardUser(4, userProgress.name + " (Sampeyan)", userProgress.levelIndex, userProgress.xp, true),
        LeaderboardUser(5, "Sekar Arum", 4, 320),
        LeaderboardUser(6, "Joko Tingkir", 3, 210),
        LeaderboardUser(7, "Slamet Santoso", 2, 90)
    ).sortedByDescending { it.xp }
     .mapIndexed { index, player -> player.copy(rank = index + 1) }

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
                        text = "P",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Column {
                    Text(
                        text = "Peringkat Jawara",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = JavaGreenDark,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Kompetisi Pasrawungan Jawa",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = JavaneseSecondaryText
                    )
                }
            }
        }

        // --- DASHBOARD AND LEADERBOARD (SCROLLABLE BENTO GRIDS) ---
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // High-Contrast Bento Trophy Hero Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(32.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(1.2.dp, Color(0xFFFFECB3))
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFFD97706), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.EmojiEvents,
                            contentDescription = "Papan Jawara",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(18.dp))
                    Column {
                        Text(
                            text = "Laga Sowan Adat",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF78350F)
                        )
                        Text(
                            text = "Kanca kang rajin sinau munggah ing peringkat ndhuwur. Buktekake keprigelanmu!",
                            fontSize = 12.sp,
                            color = Color(0xFF78350F).copy(alpha = 0.85f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Competitive Ranking List bento-box
            Text(
                text = "Papan Klasemen Jawa",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = JavaGreenDark,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.2.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    mockPlayers.forEachIndexed { idx, player ->
                        val rowBg = if (player.isCurrentUser) BentoTealBg else Color.Transparent
                        val rowBorderColor = if (player.isCurrentUser) BentoTealBorder else Color.Transparent

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(rowBg)
                                .border(1.dp, rowBorderColor, RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Rank Badge
                                    val medalBgColor = when (player.rank) {
                                        1 -> Color(0xFFFFD700)
                                        2 -> Color(0xFFE2E8F0)
                                        3 -> Color(0xFFFDBA74)
                                        else -> Color(0xFFF1F5F9)
                                    }
                                    val medalTextColor = when (player.rank) {
                                        1 -> Color(0xFF78350F)
                                        2 -> Color(0xFF475569)
                                        3 -> Color(0xFF9A3412)
                                        else -> Color(0xFF64748B)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .background(medalBgColor, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (player.rank <= 3) {
                                            Icon(
                                                Icons.Filled.MilitaryTech,
                                                contentDescription = "Medal",
                                                tint = medalTextColor,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        } else {
                                            Text(
                                                text = "${player.rank}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = medalTextColor
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    // User Avatar initials
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (player.isCurrentUser) JavaGreenPrimary else JavaGreenPrimary.copy(alpha = 0.1f)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = player.name.take(1).uppercase(),
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 14.sp,
                                            color = if (player.isCurrentUser) Color.White else JavaGreenPrimary
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    // Name and Level details
                                    Column {
                                        Text(
                                            text = player.name,
                                            fontWeight = if (player.isCurrentUser) FontWeight.ExtraBold else FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = JavaGreenDark
                                        )
                                        Text(
                                            text = "Sesi Tingkat ${player.level}",
                                            fontSize = 11.sp,
                                            color = JavaneseSecondaryText
                                        )
                                    }
                                }

                                // XP text
                                Text(
                                    text = "${player.xp} XP",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    color = if (player.isCurrentUser) JavaGreenPrimary else JavaneseBatikText
                                )
                            }
                        }

                        if (idx < mockPlayers.size - 1) {
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }
    }
}
