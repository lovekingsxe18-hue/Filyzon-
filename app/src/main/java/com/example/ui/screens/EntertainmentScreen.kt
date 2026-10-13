package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.GiftItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.FilyzonViewModel

data class LiveBattle(
    val id: String,
    val creator1Name: String,
    val creator1Avatar: String,
    val creator1Points: Int,
    val creator2Name: String,
    val creator2Avatar: String,
    val creator2Points: Int,
    val viewersCount: String,
    val topic: String
)

data class TrendingSound(
    val id: String,
    val title: String,
    val artist: String,
    val usesCount: String,
    val duration: String
)

@Composable
fun EntertainmentScreen(
    viewModel: FilyzonViewModel,
    modifier: Modifier = Modifier
) {
    var isLiveBattlePlaying by remember { mutableStateOf(false) }
    var selectedSound by remember { mutableStateOf<TrendingSound?>(null) }
    var battlePoints1 by remember { mutableIntStateOf(14200) }
    var battlePoints2 by remember { mutableIntStateOf(11850) }

    val trendingSounds = remember {
        listOf(
            TrendingSound("s1", "Tu Jhoom - Coke Studio Viral", "Naseebo Lal & Abida Parveen", "148K Reels", "0:45"),
            TrendingSound("s2", "Kahani Suno 2.0 (Acoustic Remix)", "Kaifi Khalil", "320K Reels", "0:30"),
            TrendingSound("s3", "Pasoori Groove VIP Beat", "Ali Sethi & Shae Gill", "580K Reels", "0:35"),
            TrendingSound("s4", "PSL 2026 Anthem Beatdrop", "Filyzon Music Official", "95K Reels", "0:25")
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FilyzonBackgroundDark)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Entertainment Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF0D47A1),
                                    Color(0xFF00B0FF),
                                    Color(0xFF6A1B9A)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .fillMaxWidth(0.7f)
                    ) {
                        Surface(
                            color = GoldVip,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "🔥 TIKTOK STYLE ENTERTAINMENT",
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Filyzon Live Stage & PK Battles",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Cheer your favorite Pakistani creators with Falcon Gifts & boost admin prize pools!",
                            color = Color(0xFFE1F5FE),
                            fontSize = 11.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🦅", fontSize = 32.sp)
                    }
                }
            }
        }

        // Live Creator PK Battle
        item {
            Text(
                text = "🔴 Live Creator Battle (PK Mode)",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = FilyzonCardDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("live_battle_card")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xFFD32F2F),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                "LIVE • 18.4K Watching",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            "Round 3: Comedy Roast Battle",
                            color = Color(0xFF90A4AE),
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Battle Avatars vs Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // Creator 1
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AsyncImage(
                                model = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
                                contentDescription = "Hamza Abbasi",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, FilyzonCyan, CircleShape)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Hamza Abbasi", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("⚡ $battlePoints1 pts", color = FilyzonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        // VS Center Badge
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFE53935),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("VS", color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("02:15 Left", color = GoldVip, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        // Creator 2
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AsyncImage(
                                model = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150&auto=format&fit=crop&q=80",
                                contentDescription = "Bilal Tariq",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, GoldVip, CircleShape)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Bilal Tariq", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("⚡ $battlePoints2 pts", color = GoldVip, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Linear Points indicator
                    val total = (battlePoints1 + battlePoints2).toFloat()
                    val ratio1 = battlePoints1 / total
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(ratio1)
                                .fillMaxHeight()
                                .background(FilyzonCyan)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f - ratio1)
                                .fillMaxHeight()
                                .background(GoldVip)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Send Battle Gift / Tap to cheer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                battlePoints1 += 500
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0091EA)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🎁 Cheer Hamza", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                battlePoints2 += 500
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8F00)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🎁 Cheer Bilal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Trending Pakistani Music & Sound Bites
        item {
            Text(
                text = "🎵 Trending Viral Sounds (TikTok / Reels)",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                trendingSounds.forEach { sound ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = FilyzonCardDark),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedSound = if (selectedSound?.id == sound.id) null else sound
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (selectedSound?.id == sound.id) FilyzonCyan else Color(0xFF1E3A5F),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (selectedSound?.id == sound.id) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = "Play sound",
                                            tint = if (selectedSound?.id == sound.id) Color.Black else Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        sound.title,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "${sound.artist} • ${sound.duration}",
                                        color = Color(0xFF90A4AE),
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF003049)
                            ) {
                                Text(
                                    sound.usesCount,
                                    color = FilyzonCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
