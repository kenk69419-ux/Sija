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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun TheoryScreen(modifier: Modifier = Modifier) {
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
                        .background(JavaGoldPrimary, RoundedCornerShape(12.dp))
                        .shadow(1.dp, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "K",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Column {
                    Text(
                        text = "Pedoman Kamus",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = JavaGreenDark,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Unggah-Ungguh Basa Jawa",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = JavaneseSecondaryText
                    )
                }
            }
        }

        // --- HANDBOOK CONTENTS (SCROLLABLE BENTO GRIDS) ---
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // General Rule Card (Bento Hero Style)
            Card(
                colors = CardDefaults.cardColors(containerColor = BentoTealBg),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.2.dp, BentoTealBorder),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(JavaGreenPrimary, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = "Info Adat",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Apa itu Unggah-Ungguh?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = BentoTealText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Masyarakat Jawa sangat menjunjung luhur moralitas, silsilah keluarga, lan rasa hormat yang tercermin langsung dalam patrap lathi (ragam bahasa) sehari-hari.",
                            fontSize = 12.sp,
                            color = JavaneseSlateText,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Word Comparison Lookups Table (Large White Bento Card)
            Text(
                text = "Kamus Perbandingan Ragam",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = JavaGreenDark,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )

            val comparisons = listOf(
                VocabularyRow("Saya", "Aku", "Kula", "Dalem"),
                VocabularyRow("Kamu / Anda", "Kowe", "Sampeyan", "Panjenengan"),
                VocabularyRow("Makan", "Mangan", "Nedha", "Dahar"),
                VocabularyRow("Tidur", "Turu", "Tilem", "Sare"),
                VocabularyRow("Pergi", "Lunga", "Kesah", "Tindak"),
                VocabularyRow("Pulang", "Mulih", "Kondur", "Sugeng Kondur"),
                VocabularyRow("Berapa", "Pira", "Pinten", "Pinten"),
                VocabularyRow("Uang", "Dhuwit", "Arta", "Arta / Yatra"),
                VocabularyRow("Rumah", "Omah", "Griya", "Dalem")
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.2.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Header Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(JavaGreenLight, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Bahasa", modifier = Modifier.weight(1.1f), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = JavaGreenPrimary)
                        Text("Ngoko", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = JavaGreenPrimary)
                        Text("Krama", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = JavaGreenPrimary)
                        Text("Krama Inggil", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = JavaGreenPrimary)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    comparisons.forEachIndexed { i, row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(row.indonesian, modifier = Modifier.weight(1.1f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = JavaneseBatikText)
                            Text(row.ngoko, modifier = Modifier.weight(1f), fontSize = 11.sp, color = JavaneseSlateText)
                            Text(row.krama, modifier = Modifier.weight(1f), fontSize = 11.sp, color = JavaneseSlateText)
                            Text(row.inggil, modifier = Modifier.weight(1f), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = JavaGreenPrimary)
                        }
                        if (i < comparisons.size - 1) {
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                        }
                    }
                }
            }

            // Quick Tips Cards (Pill Bento layout style)
            Text(
                text = "Panduan Memilih Ragam",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = JavaGreenDark,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )

            TheoryGuideCard(
                category = "NGOKO",
                color = JavaGreenPrimary,
                bg = BentoTealBg,
                border = BentoTealBorder,
                rule = "Dianggo rembugan karo kanca kenthel, sedulur sing luwih enom, utawa nalika ngunandika (guneman dhewe). ORA entuk dienggo marang bapak/ibu guru lan tiyang sepuh!",
                eg = "Kowe wis mangan sega? (Kamu sudah makan nasi?)"
            )

            TheoryGuideCard(
                category = "KRAMA MADYA",
                color = Color(0xFFD97706),
                bg = BentoAmberBg,
                border = BentoAmberBorder,
                rule = "Digunakake kanggo srawung karo wong sing sadrajad nanging durung pati akrab, tamu umum ing dalan, utawa kahanan profesional semi-formal.",
                eg = "Sampeyan sampun nedha sekul? (Anda sudah makan nasi?)"
            )

            TheoryGuideCard(
                category = "KRAMA INGGIL",
                color = JavaTerracotta,
                bg = Color(0xFFFDF2F2),
                border = Color(0xFFFDE2E2),
                rule = "UNGGAH-UNGGUH LUHUR. Mligi kagem bapak ibu, simbah eyang, tokoh sepuh adat lan bapak/ibu guru pinangka kurmat dhumateng drajadipun.",
                eg = "Panjenengan sampun dahar sekul? (Kemutan: dahar mligi kagem lawan dudu awake dhewe)"
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

data class VocabularyRow(
    val indonesian: String,
    val ngoko: String,
    val krama: String,
    val inggil: String
)

@Composable
fun TheoryGuideCard(
    category: String,
    color: Color,
    bg: Color,
    border: Color,
    rule: String,
    eg: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bg),
        border = BorderStroke(1.2.dp, border),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(color, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = category,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    color = color,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = rule,
                fontSize = 12.sp,
                color = JavaneseBatikText,
                lineHeight = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider(color = border)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Bookmark,
                    contentDescription = "Contoh",
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text("Contoh Kalimat:", fontSize = 9.sp, color = JavaneseSecondaryText, fontWeight = FontWeight.Bold)
                    Text(eg, fontSize = 12.sp, color = JavaneseBatikText, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
